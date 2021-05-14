package com.example.photofilter.dependencyinjection

import com.example.photofilter.repositories.EditImageRepository
import com.example.photofilter.repositories.EditImageRepositoryImpl
import com.example.photofilter.repositories.SavedImagesRepository
import com.example.photofilter.repositories.SavedImagesRepositryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val repositoryModule = module {
    factory<EditImageRepository> { EditImageRepositoryImpl(androidContext()) }
    factory<SavedImagesRepository> { SavedImagesRepositryImpl(androidContext())}
}