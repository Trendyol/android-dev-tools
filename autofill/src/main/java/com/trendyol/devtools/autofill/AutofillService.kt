package com.trendyol.devtools.autofill

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.doOnDetach
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.trendyol.devtools.autofill.internal.data.HistoryRepository
import com.trendyol.devtools.autofill.internal.data.HistoryRepositoryImpl
import com.trendyol.devtools.autofill.internal.ext.asAppcompatActivity
import com.trendyol.devtools.autofill.internal.ext.findAllInputs
import com.trendyol.devtools.autofill.internal.ext.getView
import com.trendyol.devtools.autofill.internal.model.Form
import com.trendyol.devtools.autofill.internal.model.Forms
import com.trendyol.devtools.autofill.internal.io.FileReader
import com.trendyol.devtools.autofill.internal.lifecycle.AutofillViewLifecycleCallback
import com.trendyol.devtools.autofill.internal.model.AutofillEntity
import com.trendyol.devtools.autofill.internal.model.ListItem
import com.trendyol.devtools.autofill.internal.ui.AutofillDialog
import com.trendyol.devtools.autofill.internal.ui.AutofillDialogArguments
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch

class AutofillService private constructor(
    private val application: Application
) {

    private val autofillJob = SupervisorJob()

    private val autofillScope = CoroutineScope(autofillJob + Dispatchers.IO)

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private val moshi by lazy { Moshi.Builder().add(KotlinJsonAdapterFactory()).build() }

    private val historyRepository: HistoryRepository by lazy {
        HistoryRepositoryImpl(
            application.applicationContext.dataStore,
            moshi,
        )
    }

    private val autofillData by lazy {
        val c = FileReader.readAssetFile(application.applicationContext, "autofill.json")
        runCatching { moshi.adapter(Forms::class.java).fromJson(c.orEmpty()) }.getOrNull()
    }

    private val viewLifecycleCallback = object : AutofillViewLifecycleCallback() {

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            super.onActivityCreated(activity, savedInstanceState)
            activity.asAppcompatActivity()
                .supportFragmentManager
                .registerFragmentLifecycleCallbacks(this, true)
        }

        override fun onActivityDestroyed(activity: Activity) {
            super.onActivityDestroyed(activity)
            activity.asAppcompatActivity()
                .supportFragmentManager
                .unregisterFragmentLifecycleCallbacks(this)
        }

        override fun onActivityViewCreated(activity: Activity, view: View) {
            processView(activity, view)
        }

        override fun onFragmentViewCreated(activity: Activity, fragment: Fragment, view: View) {
            processView(activity, view)
        }
    }

    init {
        application.registerActivityLifecycleCallbacks(
            viewLifecycleCallback
        )
    }

    private fun processView(activity: Activity, view: View?) {
        val inputs = view?.findAllInputs()
            .orEmpty()
            .map { application.resources.getResourceEntryName(it.id) to it }
            .toMap()

        autofillData?.forms?.forEach { form ->
            if (form.fields.containsAll(inputs.keys) && inputs.keys.containsAll(form.fields)) {

                inputs.values.first().doOnDetach {
                    val keys = inputs.keys.toList()
                    val values = inputs.values.map { it.text.toString() }.toList()
                    if (values.all { it.isNotBlank() }) {
                        val entity = AutofillEntity(keys, values)

                        autofillScope.launch {
                            historyRepository.save(entity)
                        }
                    }
                }

                showAutoFillSnackBar(activity) {
                    showFormCategorySelectDialog(activity, form) { data ->
                        form.fields.forEachIndexed { index, inputId ->
                            inputs[inputId]?.setText(data.data[index])
                        }
                   }
                }
                return
            }
        }
    }

    private inline fun showAutoFillSnackBar(activity: Activity, crossinline onAccepted: () -> Unit) {
        activity.getView { activityView ->
            Snackbar.make(activityView, R.string.dev_tools_autofill_message, Snackbar.LENGTH_LONG)
                .setAction(R.string.dev_tools_autofill_action_autofill) { onAccepted.invoke() }
                .show()
        }
    }

    private inline fun showFormCategorySelectDialog(
        activity: Activity,
        form: Form,
        crossinline onSelected: (ListItem.Autofill) -> Unit
    ) {
        val dialog = AutofillDialog.newInstance(
            AutofillDialogArguments(form.getCategoryListItems())
        )

        dialog.onItemClickedListener = { item ->
            when (item) {
                is ListItem.Category -> {
                    dialog.setArguments(
                        AutofillDialogArguments(form.getAutofillListItems(item.name))
                    )
                }
                is ListItem.Autofill -> {
                    onSelected.invoke(item)
                    dialog.dismiss()
                }
            }
        }

        dialog.show(
            activity.asAppcompatActivity().supportFragmentManager,
            AutofillDialog.DIALOG_TAG
        )
    }

    private fun Form.getAutofillListItems(category: String): List<ListItem.Autofill> {
        return categories[category].orEmpty().map { values ->
            ListItem.Autofill(values.first(), values)
        }
    }

    private fun Form.getCategoryListItems(): List<ListItem.Category> {
        val extra = 
        return categories.keys.map { name ->
            ListItem.Category(name)
        }
    }

    class Builder(private val application: Application) {

        fun build(): AutofillService {
            return AutofillService(
                application = application
            )
        }
    }
}
