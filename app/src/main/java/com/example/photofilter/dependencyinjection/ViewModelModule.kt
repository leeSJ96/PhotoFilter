package com.example.photofilter.dependencyinjection

import com.example.photofilter.viewmodels.EditImageViewModel
import com.example.photofilter.viewmodels.SavedImagesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { EditImageViewModel(editImageRepository = get()) }
    viewModel { SavedImagesViewModel(savedImagesRepository = get()) }

}