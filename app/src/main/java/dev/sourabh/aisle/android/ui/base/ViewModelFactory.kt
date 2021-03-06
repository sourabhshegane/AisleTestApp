package dev.sourabh.aisle.android.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.sourabh.aisle.android.data.api.ApiHelper
import dev.sourabh.aisle.android.data.repository.MainRepository
import dev.sourabh.aisle.android.ui.main.viewmodel.MainViewModel
import dev.sourabh.aisle.android.ui.main.viewmodel.OTPVerificationViewModel

class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(MainRepository(apiHelper)) as T
        }
        if (modelClass.isAssignableFrom(OTPVerificationViewModel::class.java)) {
            return OTPVerificationViewModel(MainRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}