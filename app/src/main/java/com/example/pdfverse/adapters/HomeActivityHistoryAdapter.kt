package com.example.pdfverse.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pdfverse.dataModels.PdfInfo
import com.example.pdfverse.databinding.HomeActivityHistoryLayoutBinding
import com.example.pdfverse.utils.formatMillisToDateTime
import com.example.pdfverse.utils.getFileIcon
import com.example.pdfverse.utils.isPdfEncrypted
import com.example.pdfverse.utils.setMargin
import com.example.pdfverse.utils.setPadding
import com.example.pdfverse.utils.showLog
import com.example.pdfverse.utils.widthPer
import java.io.File

class HomeActivityHistoryAdapter(
    context: Context,
    private val pdfInfo: List<PdfInfo>,
    var onHomeHistoryOptionClick: OnHomeHistoryOptionClick
) : RecyclerView.Adapter<HomeActivityHistoryAdapter.ViewHolder>() {

    var myActivity = context as Activity


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            HomeActivityHistoryLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            setUpView()
            bindDataInView(pdfInfo[position])
        }
    }

    override fun getItemCount(): Int {
        return if (pdfInfo.size >= 4) 4 else pdfInfo.size
    }

    inner class ViewHolder(var binding: HomeActivityHistoryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setUpView() {
            binding.apply {
                //
                cvRvMain.setPadding(myActivity, top = 5f, start = 10f)
                cvRvMain.setMargin(myActivity, top = 5f)
                ivRvIcon.apply {
                    this.layoutParams.height = myActivity.widthPer(40f).toInt()
                    this.layoutParams.width = myActivity.widthPer(45f).toInt()
                }

                //
                tvRvTitle.setMargin(myActivity, start = 3f, end = 3f)
                tvRvDesc.setMargin(myActivity, start = 3f, top = -3f)
                ivRvLockIcon.setMargin(myActivity, end = 3f)
            }
        }

        fun bindDataInView(pdfInfo: PdfInfo) {
            binding.apply {
//                showLog("pdfInfo.path", pdfInfo.path.toString())

                /*  if (pdfInfo.isEncrypted == true) {
                      ivRvLockIcon.doVisible()
                  } else {
                      ivRvLockIcon.doInVisible()
                  }*/

                val isEncrypted = isPdfEncrypted(
                    myActivity, File(pdfInfo.path.toString())
                )
                if (isEncrypted) {
                    ivRvLockIcon.visibility = View.VISIBLE
                    showLog("pdfs.name", pdfInfo.name.toString())
                } else {
                    ivRvLockIcon.visibility = View.INVISIBLE
//                    ivRvLockIcon.doInVisible()
                }

                ivRvIcon.setImageResource(getFileIcon(pdfInfo.extension.toString()))
                tvRvTitle.text = pdfInfo.name
                tvRvDesc.text = formatMillisToDateTime(pdfInfo.date!!.toLong())
                cvRvMain.setOnClickListener {
                    /* val files = getFileFromFileName(myActivity, pdfInfo.name!!)*/
                    onHomeHistoryOptionClick.onHomeHistoryOptionClick(pdfInfo.name, isEncrypted)
                }
            }
        }
    }

    interface OnHomeHistoryOptionClick {
        fun onHomeHistoryOptionClick(pdfName: String?, isEncrypted: Boolean)
    }
}

