package dev.sourabh.aisle.android.data.api

import dev.sourabh.aisle.android.data.model.OTPRequest
import dev.sourabh.aisle.android.data.model.OTPValidationRequest

class ApiHelper(private val apiService: ApiService) {

    suspend fun requestOTP(otpRequest: OTPRequest) = apiService.requestOTP(otpRequest = otpRequest)

    suspend fun requestOTPValidation(otpValidationRequest: OTPValidationRequest) = apiService.validateOTP(otpValidationRequest = otpValidationRequest)

}