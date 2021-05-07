package dev.sourabh.aisle.android.data.model

data class OTPValidationRequest(
    val number: String,
    val otp: String
)