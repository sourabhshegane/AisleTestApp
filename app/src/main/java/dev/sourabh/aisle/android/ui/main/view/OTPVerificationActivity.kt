package dev.sourabh.aisle.android.ui.main.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import dev.sourabh.aisle.android.R
import dev.sourabh.aisle.android.data.api.ApiHelper
import dev.sourabh.aisle.android.data.api.RetrofitBuilder
import dev.sourabh.aisle.android.data.model.OTPValidationRequest
import dev.sourabh.aisle.android.databinding.ActivityOtpVerificationBinding
import dev.sourabh.aisle.android.ui.base.ViewModelFactory
import dev.sourabh.aisle.android.ui.main.viewmodel.OTPVerificationViewModel
import dev.sourabh.aisle.android.utils.Constants
import dev.sourabh.aisle.android.utils.Status

class OTPVerificationActivity : AppCompatActivity() {

    private lateinit var userPhoneNumber: String
    private lateinit var activityOtpVerificationBinding: ActivityOtpVerificationBinding;
    private lateinit var otpVerificationViewModel: OTPVerificationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityOtpVerificationBinding = DataBindingUtil.setContentView(this, R.layout.activity_otp_verification)

        setUpViewModel()
        initUI()
    }

    private fun setUpViewModel() {
        otpVerificationViewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(OTPVerificationViewModel::class.java)
    }

    private fun initUI() {

        startCountDownTimer();

        activityOtpVerificationBinding.edEnteredOtp.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(4))

        if(intent.hasExtra(Constants.INTENT_KEY_USER_PHONE_NUMBER)){
            userPhoneNumber = intent.getStringExtra(Constants.INTENT_KEY_USER_PHONE_NUMBER).toString()
            activityOtpVerificationBinding.phoneNumber = userPhoneNumber
        }

        activityOtpVerificationBinding.tvOtpTimer.setOnClickListener {
            val otpTimerText = activityOtpVerificationBinding.tvOtpTimer.text.toString()
            if(otpTimerText == getString(R.string.resent_otp)){
                resendOTP()
            }
        }

        activityOtpVerificationBinding.btnValidateOtp.setOnClickListener {
            val enteredOTP = activityOtpVerificationBinding.edEnteredOtp.text.toString();

            if(isValidOTPByLength(enteredOTP)){
                validateOTPFromServer(enteredOTP, userPhoneNumber)
            }else{
                Toast.makeText(this, "Please enter an valid OTP", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateOTPFromServer(enteredOTP: String, userPhoneNumber: String) {
        val userPhoneNumberWithCountryCode = getUserPhoneNumberWithCountryCode(userPhoneNumber);

        val otpValidationRequest = OTPValidationRequest(number = userPhoneNumberWithCountryCode, otp = enteredOTP)

        otpVerificationViewModel.requestOTPValidation(otpValidationRequest).observe(this, {
            it?.let { resource ->
                when(resource.status){
                    Status.LOADING -> {
                        activityOtpVerificationBinding.pbOtpVerificationActivity.visibility = View.VISIBLE
                    }
                    Status.SUCCESS -> {
                        activityOtpVerificationBinding.pbOtpVerificationActivity.visibility = View.GONE
                        resource.data?.let {
                            it?.let {
                                val receivedToken = it.token
                                if (receivedToken == null){
                                    Toast.makeText(
                                        this,
                                        getString(R.string.wrong_otp_entered),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }else{
                                    navigateToHomeScreen();
                                }
                            }
                        }
                    }
                    Status.ERROR -> {
                        Log.d("XXX", "validateOTPFromServer: " + resource.data)
                        activityOtpVerificationBinding.pbOtpVerificationActivity.visibility = View.GONE
                        Toast.makeText(this, getString(R.string.something_went_wrong_please_try_again), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun getUserPhoneNumberWithCountryCode(userPhoneNumber: String): String {
        return "+91$userPhoneNumber"
    }

    private fun navigateToHomeScreen() {
        startActivity(Intent(this, HostActivity::class.java))
        finish()
    }

    private fun isValidOTPByLength(enteredOTP: String): Boolean {
        return enteredOTP.isNotEmpty() && enteredOTP.length == 4
    }

    private fun resendOTP() {

    }

    private fun startCountDownTimer() {
        val timer = object: CountDownTimer(20000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val toSeconds = (millisUntilFinished/1000)
                if(toSeconds < 10)
                    activityOtpVerificationBinding.tvOtpTimer.text = "00:0$toSeconds"
                else
                    activityOtpVerificationBinding.tvOtpTimer.text = "00:$toSeconds"
            }

            override fun onFinish() {
                activityOtpVerificationBinding.tvOtpTimer.text = getString(R.string.resent_otp)
            }
        }
        timer.start()
    }
}