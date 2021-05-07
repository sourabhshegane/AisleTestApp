package dev.sourabh.aisle.android.data.api

import dev.sourabh.aisle.android.data.model.OTPRequest

class ApiHelper(private val apiService: ApiService) {

    suspend fun requestOTP(otpRequest: OTPRequest) = apiService.requestOTP(otpRequest = otpRequest)

}