package com.example.pdfverse.dialogs

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.pdfverse.R
import com.example.pdfverse.databinding.CustomEditDialogLayoutBinding
import com.example.pdfverse.utils.GlobalMethods
import com.example.pdfverse.utils.checkEmptyString
import com.example.pdfverse.utils.setMargin
import com.example.pdfverse.utils.setPadding
import com.example.pdfverse.utils.showToast
import com.google.android.material.textfield.TextInputLayout

@Suppress("DEPRECATION")
class CustomEditDialog(
    context: Context,
    private val title: String,
    private val fileName: String,
    private val customDialogButtons: CustomDialogButtons
) : DialogFragment() {

    /** write this line while calling this dialog**/
    /** MyCustomDialog().show(supportFragmentManager, "MyCustomFragment") **/

    lateinit var binding: CustomEditDialogLayoutBinding
    private var myActivity = context as Activity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = CustomEditDialogLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView()
        setUpDialog()
    }

    private fun setUpDialog() {
        binding.apply {
            tvDialogHeadText.text = title
            edtText.hint = title

            if (title == "Enter password") {
                edtTextLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
            }
            edtText.setText(fileName)

            dialog!!.window?.setBackgroundDrawableResource(R.drawable.rounded_corner)
//            dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_title_background)
            val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
//            val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
            dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)

            isCancelable = false


            btnDialogYes.setOnClickListener {
                if (edtText.text.checkEmptyString()) {
                    myActivity.showToast(title)
                } else {
                    dismiss()
                    GlobalMethods.removeTheBackgroundDiv(myActivity)
                    customDialogButtons.onYesButtonClick(edtText.text.toString())
                    /* Handler(Looper.getMainLooper()).postDelayed({
                     }, 1000)*/
                }
            }

            btnDialogNo.setOnClickListener {
                customDialogButtons.onNoButtonClick()
                dismiss()
                GlobalMethods.removeTheBackgroundDiv(myActivity)
            }
        }
    }


    private fun setUpView() {
        binding.apply {
            //
            tvDialogHeadText.setMargin(myActivity, top = 20f)

            //
//            tvDialogSubHeadText.setMargin(myActivity, start = 30f, top = 20f, end = 30f)
            edtTextLayout.setMargin(myActivity, top = 20f, start = 15f, end = 15f)

            //
            btnDialogYes.setMargin(myActivity, top = 15f, end = 15f)
            btnDialogYes.setPadding(myActivity, top = 10f, bottom = 10f, start = 5f, end = 5f)

            //
            btnDialogNo.setMargin(myActivity, top = 15f, end = 15f)
            btnDialogNo.setPadding(myActivity, top = 10f, bottom = 10f, start = 5f, end = 5f)

        }

    }

    interface CustomDialogButtons {
        fun onYesButtonClick(fileName: String)
        fun onNoButtonClick()
    }
}
