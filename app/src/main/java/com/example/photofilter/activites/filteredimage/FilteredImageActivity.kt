package com.example.photofilter.activites.filteredimage

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.photofilter.R
import com.example.photofilter.activites.editimage.EditImageActivity
import com.example.photofilter.databinding.ActivityFilteredImageBinding

class FilteredImageActivity : AppCompatActivity() {


    private lateinit var fileUri : Uri
    private lateinit var binding : ActivityFilteredImageBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilteredImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        displayFilteredImage()
    }

    private fun displayFilteredImage(){
        intent.getParcelableExtra<Uri>(EditImageActivity.KEY_FILTERED_IMAGE_URI)?.let { imageUri ->
            fileUri = imageUri
            binding.imageFilteredImage.setImageURI(imageUri)
        }
    }

    private fun setListeners(){
        binding.fabShare.setOnClickListener {
            with(Intent(Intent.ACTION_SEND)){
                putExtra(Intent.EXTRA_STREAM, fileUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                type = "image/*"
                startActivity(this)
            }
        }
    }
}