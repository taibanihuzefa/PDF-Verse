@file:Suppress("UNCHECKED_CAST")

package com.example.pdfverse.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
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
import java.util.Locale

class FileHistoryListActivityAdapter(
    context: Context,
    private val pdfInfo: List<PdfInfo>,
    var onHomeHistoryOptionClick: OnHomeHistoryOptionClick
) : RecyclerView.Adapter<FileHistoryListActivityAdapter.ViewHolder>(), Filterable {

    var myActivity = context as Activity

    private var filteredPdfList: List<PdfInfo> = pdfInfo

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
            bindDataInView(filteredPdfList[position])
        }
    }

    override fun getItemCount(): Int {
        return filteredPdfList.size
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
                tvRvTitle.setMargin(myActivity, start = 3f)
                tvRvDesc.setMargin(myActivity, start = 3f, top = -3f)
            }
        }

        fun bindDataInView(pdfInfo: PdfInfo) {
            binding.apply {
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
                    onHomeHistoryOptionClick.onHomeHistoryOptionClick(pdfInfo.name, isEncrypted)
                }

                cvRvMain.setOnLongClickListener {
                    showLog("longpress", "longress, ${pdfInfo.name}")
                    true
                }
            }
        }
    }

    interface OnHomeHistoryOptionClick {
        fun onHomeHistoryOptionClick(pdfName: String?, isEncrypted: Boolean)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val filteredList = mutableListOf<PdfInfo>()
                val query = constraint?.toString()?.lowercase(Locale.getDefault())

                query?.let {
                    if (it.isEmpty()) {
                        filteredList.addAll(pdfInfo)
                    } else {
                        for (item in pdfInfo) {
                            if (item.name?.lowercase(Locale.getDefault())
                                    ?.contains(query) == true
                            ) {
                                filteredList.add(item)
                            }
                        }
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults

            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredPdfList = results?.values as List<PdfInfo>
                notifyDataSetChanged()
            }
        }
    }
}

