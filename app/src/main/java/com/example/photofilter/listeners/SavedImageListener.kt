package com.example.photofilter.listeners

import java.io.File

interface SavedImageListener {
    fun onImageClicked(file : File)
}