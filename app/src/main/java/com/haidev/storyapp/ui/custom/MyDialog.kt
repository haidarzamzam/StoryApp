package com.haidev.storyapp.ui.custom

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.TextView
import com.haidev.storyapp.R

object LoadingScreen {
    private var dialog: Dialog? = null //obj
    fun displayLoadingWithText(
        context: Context?,
        text: String?,
        isCancelable: Boolean
    ) {
        dialog = Dialog(context!!)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.custom_loading)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCancelable(isCancelable)
        val textView = dialog!!.findViewById<TextView>(R.id.tv_title)
        textView.text = text
        try {
            dialog?.show()
        } catch (e: Exception) {
        }
    }

    fun hideLoading() {
        try {
            if (dialog != null) {
                dialog?.dismiss()
            }
        } catch (e: Exception) {
        }
    }
}
