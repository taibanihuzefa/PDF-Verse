package com.example.pdfverse.dialogs

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.pdfverse.R
import com.example.pdfverse.databinding.CustomAppDialogLayoutBinding
import com.example.pdfverse.utils.GlobalMethods
import com.example.pdfverse.utils.doHide
import com.example.pdfverse.utils.setMargin
import com.example.pdfverse.utils.setPadding

@Suppress("DEPRECATION")
class CustomAppDialog(
    context: Context,
    private val title: String,
    private val message: String,
    private val positiveButtonText: String? = null,
    private val negativeButtonText: String? = null,
    private val customDialogButtons: CustomDialogButtons
) : DialogFragment() {

    /** write this line while calling this dialog**/
    /** MyCustomDialog().show(supportFragmentManager, "MyCustomFragment") **/

    lateinit var binding: CustomAppDialogLayoutBinding
    private var myActivity = context as Activity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = CustomAppDialogLayoutBinding.inflate(inflater, container, false)
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
            tvDialogSubHeadText.text = message

            btnDialogYes.text = positiveButtonText ?: "Yes"
            btnDialogNo.text = negativeButtonText ?: "No"

            isCancelable = true

            if (btnDialogYes.text == "Settings") {
                btnDialogNo.doHide()
                isCancelable = false
            }

            dialog!!.window?.setBackgroundDrawableResource(R.drawable.rounded_corner)
//            dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_title_background)
            val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
//            val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
            dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)


            btnDialogYes.setOnClickListener {
                customDialogButtons.onYesButtonClick()
                dismiss()
                GlobalMethods.removeTheBackgroundDiv(myActivity)

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
            tvDialogSubHeadText.setMargin(myActivity, start = 30f, top = 20f, end = 30f)

            //
            btnDialogYes.setMargin(myActivity, start = 15f, top = 25f, end = 15f)
            btnDialogYes.setPadding(myActivity, top = 10f, bottom = 10f)

            //
            btnDialogNo.setMargin(myActivity, start = 15f, top = 5f, end = 15f)
            btnDialogNo.setPadding(myActivity, top = 10f, bottom = 10f)

        }

    }

    interface CustomDialogButtons {
        fun onYesButtonClick()
        fun onNoButtonClick()
    }
}
