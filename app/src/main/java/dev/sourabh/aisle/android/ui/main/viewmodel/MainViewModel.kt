package dev.sourabh.aisle.android.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dev.sourabh.aisle.android.data.model.OTPRequest
import dev.sourabh.aisle.android.data.repository.MainRepository
import dev.sourabh.aisle.android.utils.Resource
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {
    fun requestOTP(otpRequest: OTPRequest) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.requestOTP(otpRequest)))
        }catch (exception: Exception){
            emit(Resource.error(data = null, message = exception.message ?: "An error occurred"))
        }
    }
}