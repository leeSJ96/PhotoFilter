package com.example.photofilter.activites.savedimages

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.photofilter.activites.editimage.EditImageActivity
import com.example.photofilter.activites.filteredimage.FilteredImageActivity
import com.example.photofilter.adapters.SavedImagesAdapter
import com.example.photofilter.databinding.ActivitySavedImagesBinding
import com.example.photofilter.listeners.SavedImageListener
import com.example.photofilter.utilities.disPlayToast
import com.example.photofilter.viewmodels.SavedImagesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class SavedImagesActivity : AppCompatActivity(), SavedImageListener{

    private lateinit var binding : ActivitySavedImagesBinding
    private val viewModel : SavedImagesViewModel by viewModel()
    private var manager = StaggeredGridLayoutManager(3,LinearLayoutManager.VERTICAL)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupObserver()
        setListeners()
        viewModel.loadSavedImages()
    }

    private fun setupObserver(){
        viewModel.savedImagesUiState.observe(this, {
            val savedImagesDataState = it ?: return@observe
            binding.savedImagesProgressBar.visibility=
                if (savedImagesDataState.isLoading) View.VISIBLE else View.GONE
            savedImagesDataState.savedImages?.let{ savedImages ->
                SavedImagesAdapter(savedImages, this).also { adapter ->
                    with(binding.savedImagesRecyclerView){
                        this.adapter = adapter
                        binding.savedImagesRecyclerView.
                        visibility = View.VISIBLE
                        binding.savedImagesRecyclerView.layoutManager = manager


                    }

                }
            } ?: kotlin.run {
                savedImagesDataState.error?.let { error ->
                    disPlayToast(error)

            } }
        })
    }

    private fun setListeners(){
        binding.imageBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onImageClicked(file: File) {
       val fileUri = FileProvider.getUriForFile(
           applicationContext,
           "${packageName}.provider",
           file
       )


           Intent(applicationContext,
           FilteredImageActivity::class.java)
               .also { filteredImageIntent ->

                   filteredImageIntent.putExtra(EditImageActivity.KEY_FILTERED_IMAGE_URI, fileUri)

                   startActivity(filteredImageIntent)
       }

    }
}