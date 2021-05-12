package com.example.photofilter.repositories

import android.graphics.Bitmap
import android.net.Uri
import com.example.photofilter.data.ImageFilter

interface EditImageRepository {
    suspend fun prepareImagePreview(imageUri: Uri): Bitmap?
    suspend fun getImagefilters(image:Bitmap): List<ImageFilter>

}