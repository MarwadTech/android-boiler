package com.marwadtech.userapp.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marwadtech.userapp.retrofit.models.BaseModel
import com.marwadtech.userapp.retrofit.models.request.UserRequestModel
import com.marwadtech.userapp.retrofit.models.response.UserAuthResponseModel
import com.marwadtech.userapp.retrofit.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _login = MutableLiveData<BaseModel<UserAuthResponseModel>>()
    val login: LiveData<BaseModel<UserAuthResponseModel>>
        get() = _login

    fun login(userRequestModel: UserRequestModel) {
        viewModelScope.launch {
            _login.value = BaseModel.loading()
            _login.value = authRepository.login(userRequestModel)
            _login.value = BaseModel.clear()
        }
    }

    private val _loginWithGoogle = MutableLiveData<BaseModel<UserAuthResponseModel>>()
    val loginWithGoogle: LiveData<BaseModel<UserAuthResponseModel>>
        get() = _loginWithGoogle

    fun loginWithGoogle(userRequestModel: UserRequestModel) {
        viewModelScope.launch {
            _loginWithGoogle.value = BaseModel.loading()
            _loginWithGoogle.value = authRepository.loginWithGoogle(userRequestModel)
            _loginWithGoogle.value = BaseModel.clear()
        }
    }

    private val _checkUser = MutableLiveData<BaseModel<UserAuthResponseModel>>()
    val checkUser: LiveData<BaseModel<UserAuthResponseModel>>
        get() = _checkUser

    fun checkUser(userRequestModel: UserRequestModel) {
        viewModelScope.launch {
            _checkUser.value = BaseModel.loading()
            _checkUser.value = authRepository.checkUser(userRequestModel)
            _checkUser.value = BaseModel.clear()
        }
    }

    private val _sendOtp = MutableLiveData<BaseModel<UserAuthResponseModel>>()
    val sendOtp: LiveData<BaseModel<UserAuthResponseModel>>
        get() = _sendOtp

    fun sendOtp(userRequestModel: UserRequestModel) {
        viewModelScope.launch {
            _sendOtp.value = BaseModel.loading()
            _sendOtp.value = authRepository.sendOtp(userRequestModel)
            _sendOtp.value = BaseModel.clear()
        }
    }

    private val _verifyOtp = MutableLiveData<BaseModel<UserAuthResponseModel>>()
    val verifyOtp: LiveData<BaseModel<UserAuthResponseModel>>
        get() = _verifyOtp

    fun verifyOtp(userRequestModel: UserRequestModel) {
        viewModelScope.launch {
            _verifyOtp.value = BaseModel.loading()
            _verifyOtp.value = authRepository.verifyOtp(userRequestModel)
            _verifyOtp.value = BaseModel.clear()
        }
    }

    private val _registerWithOtp = MutableLiveData<BaseModel<UserAuthResponseModel>>()
    val registerWithOtp: LiveData<BaseModel<UserAuthResponseModel>>
        get() = _registerWithOtp

    fun registerWithOtp(userRequestModel: UserRequestModel) {
        viewModelScope.launch {
            _registerWithOtp.value = BaseModel.loading()
            _registerWithOtp.value = authRepository.registerWithOtp(userRequestModel)
            _registerWithOtp.value = BaseModel.clear()
        }
    }

    private val _forgotPassword = MutableLiveData<BaseModel<UserAuthResponseModel>>()
    val forgotPassword: LiveData<BaseModel<UserAuthResponseModel>>
        get() = _forgotPassword

    fun forgotPassword(userRequestModel: UserRequestModel) {
        viewModelScope.launch {
            _forgotPassword.value = BaseModel.loading()
            _forgotPassword.value = authRepository.forgotPassword(userRequestModel)
            _forgotPassword.value = BaseModel.clear()
        }
    }

    private val _resetPassword = MutableLiveData<BaseModel<UserAuthResponseModel>>()
    val resetPassword: LiveData<BaseModel<UserAuthResponseModel>>
        get() = _resetPassword

    fun resetPassword(userRequestModel: UserRequestModel) {
        viewModelScope.launch {
            _resetPassword.value = BaseModel.loading()
            _resetPassword.value = authRepository.resetPassword(userRequestModel)
            _resetPassword.value = BaseModel.clear()
        }
    }


}