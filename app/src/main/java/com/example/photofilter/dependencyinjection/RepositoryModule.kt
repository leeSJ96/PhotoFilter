package com.example.photofilter.dependencyinjection

import com.example.photofilter.repositories.EditImageRepository
import com.example.photofilter.repositories.EditImageRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val repositoryModule = module {
    factory<EditImageRepository> { EditImageRepositoryImpl(androidContext()) }
}