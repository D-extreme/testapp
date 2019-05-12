package com.example.dextreme.testapp

import android.arch.core.executor.TaskExecutor
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.ContactsContract
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.auth.PhoneAuthProvider.getInstance
import java.util.concurrent.TimeUnit

class OTP : AppCompatActivity() {

    var verificationID: String? = null
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
//function for manually verifying the OTP
  fun sendVerificationCode(number: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                    override fun onCodeSent(p0: String?, p1: PhoneAuthProvider.ForceResendingToken?) {
                        super.onCodeSent(p0, p1)
                        verificationID = p0
                    }

                    override fun onVerificationCompleted(p0: PhoneAuthCredential?) {
                        val code: String = p0!!.smsCode.toString()
                        if(code!=null){
                            editTextOTP!!.setText(code)

                            verifyVerificationCode(code)
                        }
                    }

                    override fun onVerificationFailed(p0: FirebaseException?) {
                        Toast.makeText(this@OTP,p0?.message,Toast.LENGTH_SHORT).show()
                    }

                })
  }
//function for verifying the OTP sent
    fun verifyVerificationCode(code: String){
        val Credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(verificationID as String,code)

        //to be used for signing in

        signInWithPhoneAuthCredential(Credential)
    }

    //once the OTP is verified with his credential this method signs in the user into his account
    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential){
        mAuth!!.signInWithCredential(credential).addOnCompleteListener {object: OnCompleteListener<AuthResult>{
            override fun onComplete(p0: Task<AuthResult>) {
                if(p0.isSuccessful){
                    val intent: Intent = Intent(this@OTP,Visitor::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or  Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }else{

                    var message = "Something went wrong :("
                    //Not working in Android
                    if(p0.exception is FirebaseAuthInvalidCredentialsException){
                        message = "Invalid code entered...."
                    }

                    Toast.makeText(this@OTP as Context, message, Toast.LENGTH_LONG).show()

            }

        }
        }
    }
}
}
