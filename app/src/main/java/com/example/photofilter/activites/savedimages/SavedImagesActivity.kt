package com.example.photofilter.activites.savedimages

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.photofilter.databinding.ActivitySavedImagesBinding
import com.example.photofilter.utilities.disPlayToast
import com.example.photofilter.viewmodels.SavedImagesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SavedImagesActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySavedImagesBinding
    private val viewModel : SavedImagesViewModel by viewModel()

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
                disPlayToast("저장된 이미지가 ${savedImages.size}개 있습니다")
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
}