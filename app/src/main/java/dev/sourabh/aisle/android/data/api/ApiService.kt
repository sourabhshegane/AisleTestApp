package dev.sourabh.aisle.android.data.api

import dev.sourabh.aisle.android.data.model.GenericResponse
import dev.sourabh.aisle.android.data.model.OTPRequest
import dev.sourabh.aisle.android.data.model.OTPValidationRequest
import dev.sourabh.aisle.android.data.model.OTPValidationResponse
import dev.sourabh.aisle.android.utils.Constants
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("phone_number_login")
    suspend fun requestOTP(@Header("Cookie") cookie: String = Constants.COOKIE_VALUE, @Body otpRequest: OTPRequest): GenericResponse

    @POST("verify_otp")
    suspend fun validateOTP(@Header("Cookie") cookie: String = Constants.COOKIE_VALUE, @Body otpValidationRequest: OTPValidationRequest): OTPValidationResponse
}