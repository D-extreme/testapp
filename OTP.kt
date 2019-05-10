package com.example.dextreme.testapp

import android.arch.core.executor.TaskExecutor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class OTP : AppCompatActivity() {

    val verificationID: String? = null
    var editTextOTP: EditText? = null
    var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        editTextOTP = findViewById(R.id.editTextOTP)
        mAuth = FirebaseAuth.getInstance()

        val intent = intent
        val number = intent.getStringExtra("phNumber")
        sendVerificationCode(number)

        findViewById<Button>(R.id.buttonSignIn).setOnClickListener(View.OnClickListener { v ->

            val code: String = editTextOTP?.text.toString().trim()
            if (code.isEmpty() || code.length < 6) {
                editTextOTP?.setError("Enter Valid OTP")
                editTextOTP?.requestFocus()
                return@OnClickListener
            }
            //this will be used for manually verifying the OTP
            verifyVerificationCode(code)

        })
    }

    fun sendVerificationCode(number: String) {
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                "+91" + number,
//                30,
//                TimeUnit.SECONDS,
//                TaskExecutors.MAIN_THREAD,
//                //entering callback is left
//        )}
    }

    fun verifyVerificationCode(code: String){

    }
}
