import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.canhub.cropper.CropImageView
import com.example.pdfverse.databinding.ViewPagerItemLayoutBinding
import com.example.pdfverse.utils.setMargin
import com.example.pdfverse.utils.showLog


class ImagePagerAdapter(
    private val context: Context,
    private val imageUris: List<Uri>,
    private val onViewClick: OnViewClick
) : RecyclerView.Adapter<ImagePagerAdapter.ViewHolder>() {

    var myActivity = context as Activity

    val _updatedImageMap = mutableMapOf<Int, Pair<Bitmap?, Float>>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ViewPagerItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )

        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {

            setUpView()
            bindDataInView(position)
          //  holder.setIsRecyclable(false)
        }
    }


    override fun getItemCount(): Int {
        return imageUris.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    inner class ViewHolder(var binding: ViewPagerItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

//        private var croppedImageUri: Bitmap? = null

        fun bindDataInView(position: Int) {
            binding.apply {
                val uri = imageUris[position]
                ivCropImageView.setImageUriAsync(uri)

                /* var cropped: Bitmap? = null
                 ivCropImageView.setOnCropImageCompleteListener { _, _ ->
                     showLog("cropping Complete", "cropping Complete")
                     cropped = ivCropImageView.getCroppedImage()
                     if (cropped != null) {
                         _updatedImageMap[position] = Pair(cropped, ivCropImageView.rotation)
                     }
                 }
                 showLog("cropped", cropped.toString())*/

                onViewClick.onViewClick(ivCropImageView)

                ivCropImageView.setOnClickListener {
                    showLog("ivCropImageViewClicked", "ivCropImageView clicked")
                }
//                val croppedBitmap: Bitmap? = ivCropImageView.getCroppedImage()
//                showLog("croppedBitmap", croppedBitmap.toString())
            }
        }

        fun setUpView() {
            binding.apply {
                ivCropImageView.setMargin(myActivity, end = 10f)
            }

        }
    }

    fun getUpdatedImageMap(): Map<Int, Pair<Bitmap?, Float>> = _updatedImageMap


    interface OnViewClick {
        fun onViewClick(ivCropImageView: CropImageView)
    }
}