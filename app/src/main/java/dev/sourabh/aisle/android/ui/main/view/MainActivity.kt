package dev.sourabh.aisle.android.ui.main.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import dev.sourabh.aisle.android.R
import dev.sourabh.aisle.android.data.api.ApiHelper
import dev.sourabh.aisle.android.data.api.RetrofitBuilder
import dev.sourabh.aisle.android.data.model.OTPRequest
import dev.sourabh.aisle.android.databinding.ActivityMainBinding
import dev.sourabh.aisle.android.ui.base.ViewModelFactory
import dev.sourabh.aisle.android.ui.main.viewmodel.MainViewModel
import dev.sourabh.aisle.android.utils.Constants
import dev.sourabh.aisle.android.utils.Status

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setUpViewModel()
        initUI()
        //navigateToOTPVerificationActivity("8329855960");
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(MainViewModel::class.java)
    }

    private fun initUI() {

        activityMainBinding.edUserPhoneNumber.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(10))

        activityMainBinding.btnContinueToOtp.setOnClickListener {
            val enteredPhoneNumber = activityMainBinding.edUserPhoneNumber.text.toString()
            if(isEnteredPhoneNumberValid(enteredPhoneNumber)){
                sendOTP(enteredPhoneNumber)
            }else{
                Toast.makeText(this, getString(R.string.enter_valid_phone_number), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendOTP(enteredPhoneNumber: String) {
        val otpRequest = OTPRequest("+91$enteredPhoneNumber")

        viewModel.requestOTP(otpRequest).observe(this, {
            it?.let { resource ->
                when(resource.status){

                    Status.LOADING-> {
                        activityMainBinding.pbMainActivity.visibility = View.VISIBLE
                    }

                    Status.SUCCESS -> {
                        activityMainBinding.pbMainActivity.visibility = View.GONE
                        resource.data?.let {
                            it?.let {
                                val otpRequestStatus = it.status
                                if(otpRequestStatus){
                                    navigateToOTPVerificationActivity(enteredPhoneNumber)
                                }else{
                                    Toast.makeText(this, getString(R.string.something_went_wrong_please_try_again), Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }

                    Status.ERROR -> {
                        activityMainBinding.pbMainActivity.visibility = View.GONE
                        Toast.makeText(this, getString(R.string.something_went_wrong_please_try_again), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun navigateToOTPVerificationActivity(userPhoneNumber: String){
        val intentToOTPVerificationActivity = Intent(this, OTPVerificationActivity::class.java)
        intentToOTPVerificationActivity.putExtra(Constants.INTENT_KEY_USER_PHONE_NUMBER, userPhoneNumber)
        startActivity(intentToOTPVerificationActivity)
        finish()
    }

    private fun isEnteredPhoneNumberValid(enteredPhoneNumber: String): Boolean {
        return Patterns.PHONE.matcher(enteredPhoneNumber).matches() && enteredPhoneNumber.length == 10
    }
}