package com.marwadtech.userapp.ui.screens.levels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marwadtech.userapp.retrofit.models.BaseModel
import com.marwadtech.userapp.retrofit.models.request.LevelRequestModel
import com.marwadtech.userapp.retrofit.models.response.LevelResponseModel
import com.marwadtech.userapp.retrofit.repository.LevelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LevelViewModel @Inject constructor(
    private val levelRepository: LevelRepository
) : ViewModel() {

    private val _getAllLevels = MutableLiveData<BaseModel<ArrayList<LevelResponseModel>>>()
    val getAllLevels: LiveData<BaseModel<ArrayList<LevelResponseModel>>>
        get() = _getAllLevels
    fun getAllLevels() {
        viewModelScope.launch {
            _getAllLevels.value = BaseModel.loading()
            _getAllLevels.value = levelRepository.getAllLevels()
            _getAllLevels.value = BaseModel.clear()
        }
    }

    private val _deleteLevel = MutableLiveData<BaseModel<LevelResponseModel>>()
    val deleteLevel: LiveData<BaseModel<LevelResponseModel>>
        get() = _deleteLevel
    fun deleteLevel(levelId: String) {
        viewModelScope.launch {
            _deleteLevel.value = BaseModel.loading()
            _deleteLevel.value = levelRepository.deleteLevel(levelId)
            _deleteLevel.value = BaseModel.clear()
        }
    }

    private val _addLevel = MutableLiveData<BaseModel<LevelResponseModel>>()
    val addLevel: LiveData<BaseModel<LevelResponseModel>>
        get() = _addLevel
    fun addLevel(levelRequestModel: LevelRequestModel) {
        viewModelScope.launch {
            _addLevel.value = BaseModel.loading()
            _addLevel.value = levelRepository.addLevel(levelRequestModel)
            _addLevel.value = BaseModel.clear()
        }
    }

    private val _updateLevel = MutableLiveData<BaseModel<LevelResponseModel>>()
    val updateLevel: LiveData<BaseModel<LevelResponseModel>>
        get() = _updateLevel
    fun updateLevel(levelId : String, levelRequestModel: LevelRequestModel) {
        viewModelScope.launch {
            _updateLevel.value = BaseModel.loading()
            _updateLevel.value = levelRepository.updateLevel(levelId,levelRequestModel)
            _updateLevel.value = BaseModel.clear()
        }
    }

  }