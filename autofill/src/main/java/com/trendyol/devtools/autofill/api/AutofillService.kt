package com.trendyol.devtools.autofill.api

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.trendyol.devtools.autofill.R
import com.trendyol.devtools.autofill.internal.ext.asAppcompatActivity
import com.trendyol.devtools.autofill.internal.ext.findAllInputs
import com.trendyol.devtools.autofill.internal.ext.getView
import com.trendyol.devtools.autofill.internal.ext.hasInputType
import com.trendyol.devtools.autofill.internal.lifecycle.AutofillViewLifecycleCallback

class AutofillService private constructor(
    application: Application,
    private val autoFillData: List<AutofillData>,
    private val environment: String?
) {

    private val viewLifecycleCallback = object: AutofillViewLifecycleCallback() {
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

        val inputEmail = inputs?.find {
            it.hasInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
        }
        val inputPassword = inputs?.find {
            it.hasInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
        }

        if (inputEmail != null) {
            showAutoFillSnackBar(activity) {
                showAutoFillDataSelectDialog(activity) { data ->
                    inputEmail.setText(data.email)
                    inputPassword?.setText(data.password)
                }
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

    private inline fun showAutoFillDataSelectDialog(
        activity: Activity,
        crossinline onSelected: (AutofillData) -> Unit
    ) {
        MaterialAlertDialogBuilder(activity)
            .setTitle(R.string.dev_tools_autofill_data_select_dialog_title)
            .setItems(
                autoFillData
                    .filter { environment.isNullOrBlank() || environment == it.environment }
                    .map { it.email }
                    .toTypedArray()
            ) { _, pos ->
                onSelected.invoke(autoFillData[pos])
            }.show()
    }

    class Builder(private val application: Application) {

        private var autoFillData: List<AutofillData>? = null

        private var environment: String? = null

        fun withAutoFillData(autoFillData: List<AutofillData>): Builder {
            this.autoFillData = autoFillData
            return this
        }

        fun withEnvironment(environment: String?): Builder {
            this.environment = environment
            return this
        }

        fun build(): AutofillService {
            return AutofillService(
                application = application,
                autoFillData = autoFillData.orEmpty(),
                environment = environment
            )
        }
    }
}
