package com.example.photofilter.activites.editimage

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.photofilter.activites.main.MainActivity
import com.example.photofilter.adapters.ImageFiltersAdapter
import com.example.photofilter.databinding.ActivityEditImageBinding
import com.example.photofilter.utilities.disPlayToast
import com.example.photofilter.utilities.show
import com.example.photofilter.viewmodels.EditImageViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditImageBinding

    private val viewModel: EditImageViewModel by viewModel()

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
                binding.imagePreview.setImageBitmap(bitmap)
            binding.imagePreview.show()
                viewModel.loadImageFilters(bitmap)
            }

                ?: kotlin.run{
                    dataState.error?.let { error ->
                        disPlayToast(error)
                    }
                }
        })
        viewModel.imageFiltersUistate.observe(this,{
            val imageFiltersDataState = it?: return@observe
            binding.imageFiltersProgressBar.visibility =
                if (imageFiltersDataState.isLoading) View.VISIBLE else View.GONE
            imageFiltersDataState.imageFilters?.let { imagefilters ->
                ImageFiltersAdapter(imagefilters).also { adapter ->
                    binding.filtersRecyclerView.adapter = adapter
                }
            } ?: kotlin.run{
                imageFiltersDataState.error?.let { error->
                    disPlayToast(error)
                }
            }
        })
    }

    private fun prepareImagePreview(){
        intent.getParcelableExtra<Uri>(MainActivity.KEY_IMAGE_URI)?.let { imageUri ->
            viewModel.prepareImagePreview(imageUri)
        }
    }


    private fun displayImagePreview(){
        intent.getParcelableExtra<Uri>(MainActivity.KEY_IMAGE_URI)?.let {
            val inputStream = contentResolver.openInputStream(it)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            binding.imagePreview.setImageBitmap(bitmap)
            binding.imagePreview.visibility = View.VISIBLE

        }
    }

    private fun setListeners() {
        binding.imageBack.setOnClickListener {
            onBackPressed()
        }

    }

}