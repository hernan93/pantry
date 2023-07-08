package com.example.mypantry.Profile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mypantry.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth


class ForgetPassword : AppCompatActivity() {

    private var forgetEmail: EditText? = null
    private var sendEmail: Button? = null
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        forgetEmail = findViewById(R.id.forgetEmail)
        sendEmail = findViewById(R.id.sendemailButton)



        sendEmail?.setOnClickListener {
            mAuth = FirebaseAuth.getInstance()

            val email = forgetEmail?.getText().toString().trim { it <= ' ' }

            mAuth!!.sendPasswordResetEmail(email)
                    .addOnCompleteListener(OnCompleteListener<Void?> { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(applicationContext, "The email to reset the password has been sent", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@ForgetPassword, LoginActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(applicationContext, "The email to reset the password could not be sent", Toast.LENGTH_SHORT).show()
                        }
                    })
        }

    }


}