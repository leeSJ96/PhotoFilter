package com.example.photofilter.adapters

import android.app.Activity
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.photofilter.databinding.ItemContainerSavedImageBinding
import com.example.photofilter.listeners.SavedImageListener
import java.io.File
import java.net.URI.create


class SavedImagesAdapter(private val savedImages: List<Pair<File,Bitmap>>,
                         private val savedImageListener: SavedImageListener):
RecyclerView.Adapter<SavedImagesAdapter.SavedImagesViewHolder>(){


   


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedImagesViewHolder {
        val binding = ItemContainerSavedImageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SavedImagesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedImagesViewHolder, position: Int) {
        with(holder){
            with(savedImages[position]){


                binding.imageSaved.setImageBitmap(second)
                binding.imageSaved.setOnClickListener {
                    savedImageListener.onImageClicked(first)
                }

            }
        }

    }



    override fun getItemCount() = savedImages.size

    inner class SavedImagesViewHolder(val binding: ItemContainerSavedImageBinding):
        RecyclerView.ViewHolder(binding.root)


}