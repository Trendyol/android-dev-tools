package com.trendyol.devtools.autofill.api

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentOnAttachListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.trendyol.devtools.autofill.R
import com.trendyol.devtools.autofill.internal.ext.findAllInputs
import com.trendyol.devtools.autofill.internal.ext.getView
import com.trendyol.devtools.autofill.internal.ext.hasInputType

class AutofillService private constructor(
    private val autoFillData: List<AutofillData>,
) : AutofillLifecycleCallback() {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        processActivityView(activity)
        activity.asAppcompatActivity()
            .supportFragmentManager
            .addFragmentOnAttachListener(fragmentOnAttackListener)
    }

    override fun onActivityDestroyed(activity: Activity) {
        super.onActivityDestroyed(activity)
        activity.asAppcompatActivity()
            .supportFragmentManager
            .removeFragmentOnAttachListener(fragmentOnAttackListener)
    }

    private val fragmentOnAttackListener = FragmentOnAttachListener { _, fragment ->
        processFragmentView(fragment.requireActivity(), fragment)
    }

    private fun processActivityView(activity: Activity) {
        activity.getView { view -> processView(activity, view) }
    }

    private fun processFragmentView(activity: Activity, fragment: Fragment) {
        fragment.getView(activity.asAppcompatActivity()) { view -> processView(activity, view) }
    }

    private fun processView(activity: Activity, view: View) {
        val inputs = view.findAllInputs()

        val inputEmail = inputs.find {
            it.hasInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
        }
        val inputPassword = inputs.find {
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
                autoFillData.map { it.email }.toTypedArray()
            ) { _, pos ->
                onSelected.invoke(autoFillData[pos])
            }.show()
    }

    private fun Activity.asAppcompatActivity(): AppCompatActivity {
        return this as AppCompatActivity
    }

    class Builder(private val app: Application) {

        private var autoFillData: List<AutofillData>? = null

        fun withAutoFillData(autoFillData: List<AutofillData>): Builder {
            this.autoFillData = autoFillData
            return this
        }

        fun build(): AutofillService {
            val autoFillService = AutofillService(autoFillData.orEmpty())
            app.registerActivityLifecycleCallbacks(autoFillService)
            return autoFillService
        }
    }
}
