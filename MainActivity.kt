package com.example.dextreme.testapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.telephony.PhoneNumberUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    var phoneNumber: EditText? = null
    var continueButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        phoneNumber = findViewById(R.id.visitorPhoneNumber)
        continueButton=findViewById(R.id.buttonContinue)
        continueButton?.setOnClickListener(View.OnClickListener { v ->
            val number = phoneNumber?.text.toString().trim()
            if(number.isEmpty() == true|| number.length < 10){
                phoneNumber?.setError("Enter a valid phone number")
                phoneNumber?.requestFocus()
                return@OnClickListener
            }
            val intent: Intent = Intent(this@MainActivity,OTP::class.java)
            intent.putExtra("phNumber",number)
            startActivity(intent)

        })
    }
}
