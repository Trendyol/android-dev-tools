package com.trendyol.android.devtools.autofillservice

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.view.doOnDetach
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.trendyol.android.devtools.autofillservice.internal.coroutines.CoroutineRunner
import com.trendyol.android.devtools.autofillservice.internal.data.HistoryRepository
import com.trendyol.android.devtools.autofillservice.internal.data.HistoryRepositoryImpl
import com.trendyol.android.devtools.autofillservice.internal.ext.findAllInputs
import com.trendyol.android.devtools.autofillservice.internal.ext.getAutofillListItems
import com.trendyol.android.devtools.autofillservice.internal.ext.getCategoryListItems
import com.trendyol.android.devtools.autofillservice.internal.ext.getSupportFragmentManager
import com.trendyol.android.devtools.autofillservice.internal.ext.getView
import com.trendyol.android.devtools.autofillservice.internal.ext.launchDefault
import com.trendyol.android.devtools.autofillservice.internal.ext.launchIO
import com.trendyol.android.devtools.autofillservice.internal.io.FileReader
import com.trendyol.android.devtools.autofillservice.internal.lifecycle.AutofillViewLifecycleCallback
import com.trendyol.android.devtools.autofillservice.internal.model.Form
import com.trendyol.android.devtools.autofillservice.internal.model.Forms
import com.trendyol.android.devtools.autofillservice.internal.model.ListItem
import com.trendyol.android.devtools.autofillservice.internal.ui.AutofillDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect

class AutofillService private constructor() {

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

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            super.onActivityCreated(activity, savedInstanceState)
            activity.getSupportFragmentManager()
                ?.registerFragmentLifecycleCallbacks(this, true)
        }

        override fun onActivityDestroyed(activity: Activity) {
            super.onActivityDestroyed(activity)
            activity.getSupportFragmentManager()
                ?.unregisterFragmentLifecycleCallbacks(this)
        }

        override fun onActivityViewCreated(activity: Activity, view: View) {
            processView(activity, view)
        }

        override fun onFragmentViewCreated(activity: Activity, fragment: Fragment, view: View) {
            processView(activity, view)
        }

        init {
            application.registerActivityLifecycleCallbacks(this)
            launchIO {
                val fileData = FileReader.readAssetFile(application.applicationContext, filePath)
                autofillData = runCatching {
                    moshi.adapter(Forms::class.java).fromJson(fileData.orEmpty())
                }.getOrNull()
            }
        }

        private fun processView(
            activity: Activity,
            view: View?
        ) = launchDefault {
            val inputs = view?.findAllInputs()
                .orEmpty()
                .map { application.resources.getResourceEntryName(it.id) to it }
                .toMap()

            val matchedForm = findMatchedForm(inputs) ?: return@launchDefault
            val listItems = matchedForm.getCategoryListItems().toMutableList()

            historyRepository.getLast(matchedForm.fields)?.let { itemEntity ->
                listItems.add(itemEntity)
            }

            val listItemsFlow = MutableStateFlow<List<ListItem>>(listItems)
            detectAndSaveInputDataOnDetach(inputs)

            showAutoFillSnackBar(activity) {
                showFormCategorySelectDialog(activity, listItemsFlow) { item ->
                    when (item) {
                        is ListItem.Category -> {
                            launchDefault {
                                listItemsFlow.emit(matchedForm.getAutofillListItems(item.name))
                            }
                            false
                        }
                        is ListItem.Autofill -> {
                            matchedForm.fields.forEachIndexed { index, inputId ->
                                inputs[inputId]?.setText(item.data[index])
                            }
                            true
                        }
                    }
                }
            }
        }

        private fun detectAndSaveInputDataOnDetach(inputs: Map<String, EditText>) {
            inputs.values.firstOrNull()?.doOnDetach {
                val fields = inputs.keys.toList()
                val values = inputs.values.map { it.text.toString() }.toList()
                if (values.all { it.isNotBlank() }) {
                    launchIO {
                        historyRepository.save(
                            fields,
                            ListItem.Autofill(
                                name = values.firstOrNull().orEmpty(),
                                description = application.applicationContext.getString(
                                    R.string.dev_tools_autofill_history_list_item_description
                                ),
                                data = values,
                            )
                        )
                    }
                }
            }
        }

        private fun findMatchedForm(inputs: Map<String, EditText>): Form? {
            return autofillData?.data.orEmpty().find { form -> inputs.keys.containsAll(form.fields) }
        }

        private inline fun showAutoFillSnackBar(activity: Activity, crossinline onAccepted: () -> Unit) {
            activity.getView { activityView ->
                Snackbar.make(activityView, R.string.dev_tools_autofill_message, Snackbar.LENGTH_LONG)
                    .setDuration(7000)
                    .setAction(R.string.dev_tools_autofill_action_autofill) { onAccepted.invoke() }
                    .show()
            }
        }

        private inline fun showFormCategorySelectDialog(
            activity: Activity,
            items: Flow<List<ListItem>>,
            crossinline onItemSelected: (ListItem) -> Boolean
        ) {
            val dialog = AutofillDialog.newInstance()
            val fragmentManager = activity.getSupportFragmentManager() ?: return
            dialog.lifecycleScope.launchWhenStarted {
                items.collect {
                    dialog.updateItems(it)
                }
            }
            dialog.onItemClickedListener = { item ->
                if (onItemSelected.invoke(item)) dialog.dismiss()
            }
            dialog.show(
                fragmentManager,
                AutofillDialog.DIALOG_TAG
            )
        }
    }

    class Builder(private val application: Application) {

        private var filePath: String = DEFAULT_FILE_PATH

        fun withFilePath(filePath: String): Builder {
            this.filePath = filePath
            return this
        }

        fun build() {
            AutofillProcessor(
                application = application,
                filePath = filePath,
            )
        }

        companion object {
            private const val DEFAULT_FILE_PATH = "autofill.json"
        }
    }
}
