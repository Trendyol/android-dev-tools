package com.trendyol.android.devtools.autofillservice.internal

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.trendyol.android.devtools.autofillservice.R
import com.trendyol.android.devtools.autofillservice.internal.coroutines.CoroutineRunner
import com.trendyol.android.devtools.autofillservice.internal.data.HistoryRepository
import com.trendyol.android.devtools.autofillservice.internal.data.HistoryRepositoryImpl
import com.trendyol.android.devtools.autofillservice.internal.ext.getAutofillListItems
import com.trendyol.android.devtools.autofillservice.internal.ext.getCategoryListItems
import com.trendyol.android.devtools.autofillservice.internal.ext.getSupportFragmentManager
import com.trendyol.android.devtools.autofillservice.internal.ext.getView
import com.trendyol.android.devtools.autofillservice.internal.ext.io
import com.trendyol.android.devtools.autofillservice.internal.ext.launchIO
import com.trendyol.android.devtools.autofillservice.internal.ext.launchMain
import com.trendyol.android.devtools.autofillservice.internal.io.FileReader
import com.trendyol.android.devtools.autofillservice.internal.lifecycle.AutofillInputObserver
import com.trendyol.android.devtools.autofillservice.internal.lifecycle.AutofillViewLifecycleCallback
import com.trendyol.android.devtools.autofillservice.internal.model.AutofillInput
import com.trendyol.android.devtools.autofillservice.internal.model.Form
import com.trendyol.android.devtools.autofillservice.internal.model.Forms
import com.trendyol.android.devtools.autofillservice.internal.model.ListItem
import com.trendyol.android.devtools.autofillservice.internal.ui.AutofillDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob

internal class AutofillProcessor(
    private val application: Application,
    private val filePath: String,
) : AutofillViewLifecycleCallback(), CoroutineRunner {

    override val job = SupervisorJob()

    override val scope = CoroutineScope(job)

    private val moshi by lazy { Moshi.Builder().add(KotlinJsonAdapterFactory()).build() }

    private val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(application)
    }

    private val historyRepository: HistoryRepository by lazy {
        HistoryRepositoryImpl(sharedPreferences, moshi)
    }

    private var autofillData: Forms? = null
    private var isEnabled = true
    private val screens = mutableMapOf<Activity, Screen>()

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        super.onActivityCreated(activity, savedInstanceState)
        activity.getSupportFragmentManager()
            ?.registerFragmentLifecycleCallbacks(this, true)
    }

    override fun onActivityStarted(activity: Activity) {
        activity.getView { view -> processView(activity, view) }
    }

    override fun onActivityDestroyed(activity: Activity) {
        super.onActivityDestroyed(activity)
        screens.remove(activity)?.let { screen ->
            screen.observer.dispose()
            screen.dismiss()
        }
        activity.getSupportFragmentManager()
            ?.unregisterFragmentLifecycleCallbacks(this)
    }

    override fun onActivityViewCreated(activity: Activity, view: View) {
        processView(activity, view)
    }

    override fun onFragmentViewCreated(activity: Activity, fragment: Fragment, view: View) {
        activity.getView { activityView -> processView(activity, activityView) }
    }

    init {
        application.registerActivityLifecycleCallbacks(this)
        launchIO {
            val fileData = FileReader.readAssetFile(application.applicationContext, filePath)
            val forms = runCatching {
                moshi.adapter(Forms::class.java).fromJson(fileData.orEmpty())
            }
                .onFailure {
                    Log.e("Autofill", "Exception occurred while reading $filePath: ${it.message}", it)
                    launchMain {
                        isEnabled = false
                        application.unregisterActivityLifecycleCallbacks(this@AutofillProcessor)
                        screens.forEach { (activity, screen) ->
                            activity.getSupportFragmentManager()
                                ?.unregisterFragmentLifecycleCallbacks(this@AutofillProcessor)
                            screen.observer.dispose()
                            screen.dismiss()
                        }
                        screens.clear()
                    }
                }
                .onSuccess {
                    Log.d("Autofill", "$filePath is read.\n$it")
                }
                .getOrNull()
            launchMain {
                autofillData = forms
                screens.forEach { (activity, screen) ->
                    processInputs(activity, screen, screen.observer.inputs)
                }
            }
        }
    }

    private fun processView(activity: Activity, view: View) {
        if (!isEnabled || activity.isDestroyed || screens.containsKey(activity)) return
        val screen = Screen(view)
        screens[activity] = screen
        screen.observer = AutofillInputObserver(
            view = view,
            onInputsChanged = { inputs ->
                processInputs(activity, screen, inputs)
            },
            onInputsRemoved = { values -> saveHistory(values) },
        )
        screen.observer.refresh()
    }

    private fun processInputs(activity: Activity, screen: Screen, inputs: Map<String, AutofillInput>) {
        screen.dismiss()
        val matchedForm = findMatchedForm(inputs.keys) ?: return
        val identities = inputs.mapValues { it.value.identity }
        screen.task = launchMain {
            val listItems = matchedForm.getCategoryListItems().toMutableList()
            io { historyRepository.getLast(matchedForm.fields) }?.let { listItems.add(it) }
            if (activity.isDestroyed || activity.isFinishing) return@launchMain

            screen.snackbar = Snackbar.make(screen.view, R.string.dev_tools_autofill_message, Snackbar.LENGTH_LONG)
                .setDuration(7000)
                .setAction(R.string.dev_tools_autofill_action_autofill) {
                    screen.observer.refresh()
                    if (screen.observer.inputs.mapValues { it.value.identity } != identities) return@setAction
                    val dialog = AutofillDialog(activity)
                    screen.dialog = dialog
                    dialog.updateItems(listItems)
                    dialog.onItemClickedListener = { item ->
                        when (item) {
                            is ListItem.Category -> dialog.updateItems(matchedForm.getAutofillListItems(item.name))
                            is ListItem.Autofill -> {
                                screen.observer.refresh()
                                if (screen.observer.inputs.mapValues { it.value.identity } == identities) {
                                    matchedForm.fields.forEachIndexed { index, inputId ->
                                        val input = screen.observer.inputs[inputId]
                                        val value = item.data.getOrNull(index)
                                        if (input?.isShown == true && value != null) input.fill(value)
                                    }
                                }
                                dialog.dismiss()
                            }
                        }
                    }
                    dialog.show()
                }
                .also { it.show() }
        }
    }

    private fun saveHistory(values: Map<String, String?>) {
        val form = findMatchedForm(values.keys) ?: return
        // Keep history in JSON field order, including forms that match only some fields.
        val data = form.fields.map { values[it] }
        if (data.any { it.isNullOrBlank() }) return
        val item = ListItem.Autofill(
            name = data.firstOrNull().orEmpty(),
            description = application.getString(R.string.dev_tools_autofill_history_list_item_description),
            data = data.filterNotNull(),
        )
        launchIO { historyRepository.save(form.fields, item) }
    }

    private fun findMatchedForm(inputIds: Set<String>): Form? {
        return autofillData?.data.orEmpty().find { form ->
            form.fields.isNotEmpty() && if (form.matchAnyField) {
                form.fields.any { it in inputIds }
            } else {
                inputIds.containsAll(form.fields)
            }
        }
    }

    private class Screen(val view: View) {
        lateinit var observer: AutofillInputObserver
        var task: Job? = null
        var snackbar: Snackbar? = null
        var dialog: AutofillDialog? = null

        fun dismiss() {
            task?.cancel()
            snackbar?.dismiss()
            dialog?.dismiss()
            snackbar = null
            dialog = null
        }
    }
}
