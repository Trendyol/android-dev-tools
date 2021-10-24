package com.trendyol.devtools.autofill

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.trendyol.devtools.autofill.internal.ext.asAppcompatActivity
import com.trendyol.devtools.autofill.internal.ext.findAllInputs
import com.trendyol.devtools.autofill.internal.ext.getView
import com.trendyol.devtools.autofill.internal.form.Form
import com.trendyol.devtools.autofill.internal.form.Forms
import com.trendyol.devtools.autofill.internal.io.FileReader
import com.trendyol.devtools.autofill.internal.lifecycle.AutofillViewLifecycleCallback
import com.trendyol.devtools.autofill.internal.model.ListItem
import com.trendyol.devtools.autofill.internal.ui.DialogAutofill
import com.trendyol.devtools.autofill.internal.ui.DialogAutofillArguments

class AutofillService private constructor(
    private val application: Application
) {

    private val moshi by lazy { Moshi.Builder().add(KotlinJsonAdapterFactory()).build() }

    private val data by lazy {
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

        data?.forms?.forEach { form ->
            if (form.fields.containsAll(inputs.keys) && inputs.keys.containsAll(form.fields)) {
                showAutoFillSnackBar(activity) {
                    showFormCategorySelectDialog(activity, form) { data ->
                        form.fields.forEachIndexed{ index, inputId ->
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
        val dialog = DialogAutofill.newInstance(
            DialogAutofillArguments(form.categories.keys.map { ListItem.Category(it) })
        )

        dialog.onItemClickedListener = { item ->
            when (item) {
                is ListItem.Category -> {
                    dialog.setArguments(
                        DialogAutofillArguments(
                            form.categories[item.name].orEmpty().map { values ->
                                ListItem.Autofill(values.first(), values)
                            }
                        )
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
            DialogAutofill.DIALOG_TAG
        )
    }

    class Builder(private val application: Application) {

        fun build(): AutofillService {
            return AutofillService(
                application = application
            )
        }
    }
}
