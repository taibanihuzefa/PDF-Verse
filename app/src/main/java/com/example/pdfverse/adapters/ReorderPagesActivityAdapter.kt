package com.example.pdfverse.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pdfverse.databinding.ReorderPagesActivityLayoutBinding
import com.example.pdfverse.utils.getNumberOfPages
import com.example.pdfverse.utils.getPdfPageAsBitmap
import com.example.pdfverse.utils.setMargin
import com.example.pdfverse.utils.widthPer
import java.io.File
import java.util.Collections

class ReorderPagesActivityAdapter(
    context: Context,
    private val selectedFile: File,
) : RecyclerView.Adapter<ReorderPagesActivityAdapter.ViewHolder>() {

    var myActivity = context as Activity
    private val numberOfPages: Int
    val pages = mutableListOf<Int>()
    var originalPages: List<Int>

    init {
        // Get the number of pages in the PDF file
        numberOfPages = getNumberOfPages(myActivity, file = selectedFile)
        pages.addAll(0 until numberOfPages)
        originalPages = pages

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ReorderPagesActivityLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            setUpView()
            bindDataInView(pages[position])
        }
    }

    override fun getItemCount(): Int {
        return pages.size
    }

    fun swapItems(fromPosition: Int, toPosition: Int) {
        Collections.swap(pages, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
//        pages.replaceAll(originalPages)
    }


    inner class ViewHolder(var binding: ReorderPagesActivityLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bindDataInView(pageIndex: Int) {
            binding.apply {
                tvSrNumbers.text = (adapterPosition + 1).toString()
                ivPages.setImageBitmap(getPdfPageAsBitmap(selectedFile, pageIndex))
            }
        }

        fun setUpView() {
            binding.apply {
                //
                clMainLayout.setMargin(myActivity, start = 5f, end = 5f, top = 15f)

                ivPages.apply {
                    layoutParams.height = myActivity.widthPer(200f).toInt()
                    layoutParams.width = myActivity.widthPer(150f).toInt()
                    setMargin(myActivity, start = 10f)
                }
            }
        }
    }
}

