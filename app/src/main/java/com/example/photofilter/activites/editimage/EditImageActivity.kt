package com.example.photofilter.activites.editimage

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.photofilter.activites.filteredimage.FilteredImageActivity
import com.example.photofilter.activites.main.MainActivity
import com.example.photofilter.adapters.ImageFiltersAdapter
import com.example.photofilter.data.ImageFilter
import com.example.photofilter.databinding.ActivityEditImageBinding
import com.example.photofilter.listeners.ImageFilterListener
import com.example.photofilter.utilities.disPlayToast
import com.example.photofilter.utilities.show
import com.example.photofilter.viewmodels.EditImageViewModel
import jp.co.cyberagent.android.gpuimage.GPUImage
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditImageActivity : AppCompatActivity() , ImageFilterListener {

    companion object{
        const val KEY_FILTERED_IMAGE_URI = "filteredImageUri"
    }

    private val TAG = "로그"


    private lateinit var binding: ActivityEditImageBinding
    private val viewModel: EditImageViewModel by viewModel()
    private lateinit var gpuImage : GPUImage

    // 이미지 비트맵
    private lateinit var originalBitmap : Bitmap
    private val filteredBitmap = MutableLiveData<Bitmap>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = ActivityEditImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        setupObservers()
        prepareImagePreview()

    }

    private fun setupObservers(){
        viewModel.imagePreviewUiState.observe(this,{
            val dataState = it ?: return@observe
            binding.previewProgressBar.visibility =
                if (dataState.isLoading) View.VISIBLE else View.GONE
            dataState.bitmap?.let{ bitmap ->

                originalBitmap = bitmap
                filteredBitmap.value = bitmap

                with(originalBitmap){
                    gpuImage.setImage(this)
                    binding.imagePreview.show()
                    viewModel.loadImageFilters(this)
                }
            }
                ?: kotlin.run{
                    dataState.error?.let { error ->
                        disPlayToast(error)
                    }
                }
        })
        viewModel.imageFiltersUistate.observe(this,{
            val imageFiltersDataState = it?: return@observe
            Log.d(TAG, "체크3:${imageFiltersDataState} ")
            binding.imageFiltersProgressBar.visibility =
                if (imageFiltersDataState.isLoading) View.VISIBLE else View.GONE
            imageFiltersDataState.imageFilters?.let { imageFilters ->
                ImageFiltersAdapter(imageFilters, this).also { adapter ->
                    binding.filtersRecyclerView.adapter = adapter
                }
            } ?: kotlin.run{
                imageFiltersDataState.error?.let { error->
                    disPlayToast(error)
                }
            }
        })
        filteredBitmap.observe(this, {bitmap ->
            binding.imagePreview.setImageBitmap(bitmap)
        })
        viewModel.saveFilteredImageUiState.observe(this,{
            val saveFilteredImageDataState = it ?: return@observe
            Log.d(TAG, " check:${saveFilteredImageDataState} ")
            if (saveFilteredImageDataState.isLoading){
                binding.imageSave.visibility = View.GONE
                binding.savingProgressBar.visibility = View.VISIBLE
            } else{
                binding.savingProgressBar.visibility = View.GONE
                binding.imageSave.visibility = View.VISIBLE
            }
            saveFilteredImageDataState.uri?.let { savedImageUri ->
                Intent(applicationContext, FilteredImageActivity::class.java).also { filteredImageIntent ->
                    filteredImageIntent.putExtra(KEY_FILTERED_IMAGE_URI, savedImageUri)
                    startActivity(filteredImageIntent)
                    Log.d(TAG, " check2:${savedImageUri} ")
                }
            } ?: kotlin.run {
                saveFilteredImageDataState.error?.let { error ->
                    disPlayToast(error)

                }
            }
        })
    }

    private fun prepareImagePreview(){
        gpuImage = GPUImage(applicationContext)
        intent.getParcelableExtra<Uri>(MainActivity.KEY_IMAGE_URI)?.let { imageUri ->
            viewModel.prepareImagePreview(imageUri)
        }
    }


    private fun setListeners() {
        binding.imageBack.setOnClickListener {
            onBackPressed()
        }
        binding.imageSave.setOnClickListener {
            filteredBitmap.value?.let { bitmap ->
                viewModel.saveFilteredImage(bitmap)
                Log.d(TAG, "확인")
            }
        }

        binding.imagePreview.setOnLongClickListener {
            binding.imagePreview.setImageBitmap(originalBitmap)
            return@setOnLongClickListener false
        }
        binding.imagePreview.setOnClickListener {
            binding.imagePreview.setImageBitmap(filteredBitmap.value)
        }


    }

    override fun onFilterSelected(imageFilter: ImageFilter) {
        with(imageFilter){
            with(gpuImage){
                setFilter(filter)
                filteredBitmap.value = bitmapWithFilterApplied
            }
        }
    }

}