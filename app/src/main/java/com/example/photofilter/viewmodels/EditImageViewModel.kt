package com.example.photofilter.viewmodels

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.photofilter.data.ImageFilter
import com.example.photofilter.repositories.EditImageRepository
import com.example.photofilter.utilities.Coroutines

class EditImageViewModel(private val editImageRepository: EditImageRepository) : ViewModel() {


    //region: 이미지 프리뷰
    private val imagePreviewDataState = MutableLiveData<ImagePreviewDataState>()
    val imagePreviewUiState: LiveData<ImagePreviewDataState> get() = imagePreviewDataState

    fun prepareImagePreview(imageUri: Uri) {
        Coroutines.io {
            kotlin.runCatching {
                emitImagePreviewUiState(isLoading = true)
                editImageRepository.prepareImagePreview(imageUri)

            }.onSuccess { bitmap ->
                if (bitmap != null) {
                    emitImagePreviewUiState(bitmap = bitmap)
                } else {
                    emitImagePreviewUiState(error = "이미지가 없습니다")
                }
            }.onFailure {
                emitImagePreviewUiState(error = it.message.toString())
            }
        }
    }

    private fun emitImagePreviewUiState(
        isLoading: Boolean = false,
        bitmap: Bitmap? = null,
        error: String? = null
    ) {
        val dataState = ImagePreviewDataState(isLoading, bitmap, error)
        imagePreviewDataState.postValue(dataState)
    }

    data class ImagePreviewDataState(
        val isLoading: Boolean,
        val bitmap: Bitmap?,
        val error: String?

    )
    //endregion

    //region:: 이미지 필터 저장
    private val imageFilterDataState = MutableLiveData<ImageFilterDataState>()
    val imageFiltersUistate: LiveData<ImageFilterDataState> get() = imageFilterDataState

    fun loadImageFilters(originalImage: Bitmap) {
        Coroutines.io {
            runCatching {
                emitImageFiltersUiState(isLoading = true)
                editImageRepository.getImageFilters(getPreviewImage(originalImage))
            }.onSuccess { imageFilters ->
                emitImageFiltersUiState(imageFilters = imageFilters)
            }.onFailure {
                emitImageFiltersUiState(error = it.message.toString())
            }

        }
    }

    private fun getPreviewImage(originalImage: Bitmap): Bitmap {
        return kotlin.runCatching {
            val previewWidth = 150
            val previewHeight = originalImage.height * previewWidth / originalImage.width
            Bitmap.createScaledBitmap(originalImage, previewWidth, previewHeight, false)
        }.getOrDefault(originalImage)
    }

    private fun emitImageFiltersUiState(
        isLoading: Boolean = false,
        imageFilters: List<ImageFilter>? = null,
        error: String? = null

    ) {
        val dataState = ImageFilterDataState(isLoading, imageFilters, error)
        imageFilterDataState.postValue(dataState)
    }

    data class ImageFilterDataState(
        val isLoading: Boolean,
        val imageFilters: List<ImageFilter>?,
        val error: String?

    )


    //endregion

    //region:: 필터 이미지 저장소
    private val saveFilteredImageDataState = MutableLiveData<SaveFilteredImageDataState>()
    val saveFilteredImageUiState : LiveData<SaveFilteredImageDataState> get()=  saveFilteredImageDataState

    fun saveFilteredImage(filteredBitmap: Bitmap){
        Coroutines.io{
            runCatching {
                emitSaveFilteredImageUiState(isLoading = true)
                editImageRepository.saveFilteredImage(filteredBitmap)

            }.onSuccess { saveImageUri ->
                emitSaveFilteredImageUiState(uri = saveImageUri)

            }.onFailure{
                emitSaveFilteredImageUiState(error = it.message.toString())
            }
        }
    }


    private fun emitSaveFilteredImageUiState(

        isLoading: Boolean = false,
        uri: Uri? = null,
        error: String? = null
    ){
        val dataState = SaveFilteredImageDataState(isLoading, uri , error)
        saveFilteredImageDataState.postValue(dataState)
    }


        data class  SaveFilteredImageDataState(
        val isLoading: Boolean,
        val uri: Uri?,
        val error:String?
        )




}
