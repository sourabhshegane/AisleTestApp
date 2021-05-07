package dev.sourabh.aisle.android.data.repository

import dev.sourabh.aisle.android.data.api.ApiHelper
import dev.sourabh.aisle.android.data.model.OTPRequest

class MainRepository(private val apiHelper: ApiHelper) {
    suspend fun requestOTP(otpRequest: OTPRequest) = apiHelper.requestOTP(otpRequest);
}