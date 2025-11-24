package com.example.pdfverse.adapters

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pdfverse.R
import com.example.pdfverse.databinding.FileDetailsActivityListLayoutBinding
import com.example.pdfverse.utils.setMargin
import com.example.pdfverse.utils.setPadding
import com.example.pdfverse.utils.widthPer

class FileDetailsActivityListAdapter(
    private var context: Context,
    private var imageResourceIds: TypedArray,
    private var textStrings: Array<String>,
    var onFileDetailsOptionClick: OnFileDetailsOptionClick
) : RecyclerView.Adapter<FileDetailsActivityListAdapter.ViewHolder>() {

    var myActivity = context as Activity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FileDetailsActivityListLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            setUpView()
            bindDataInView(position)
        }
    }

    override fun getItemCount(): Int {
        return imageResourceIds.length()
    }

    inner class ViewHolder(var binding: FileDetailsActivityListLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setUpView() {
            binding.apply {
                //
                clRvMainConstraint.setPadding(myActivity, top = 5f, start = 10f)
                clRvMainConstraint.setMargin(myActivity, top = 5f)
                ivRvIcon.apply {
                    this.layoutParams.height = myActivity.widthPer(30f).toInt()
                    this.layoutParams.width = myActivity.widthPer(30f).toInt()
                }

                //
                tvRvTitle.setMargin(myActivity, start = 5f)
            }
        }

        fun bindDataInView(position: Int) {
            binding.apply {
                val imageResourceId = imageResourceIds.getResourceId(position, 0)
                val text = textStrings[position]

                ivRvIcon.setImageResource(imageResourceId)
                tvRvTitle.text = text

                // Set icon tint
                val iconTint = ContextCompat.getColor(myActivity, R.color.pink)
                DrawableCompat.setTint(ivRvIcon.drawable, iconTint)

                //constraint layout
                clRvMainConstraint.setOnClickListener {
                    onFileDetailsOptionClick.onFileDetailsOptionClick(imageResourceId)
                }

            }
        }
    }

    interface OnFileDetailsOptionClick {
        fun onFileDetailsOptionClick(imageResourceIds: Int)
    }
}

