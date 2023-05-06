package com.example.ideationnation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.graphics.Color as Color1


class LoginActivity : AppCompatActivity() {
    private var logIndex: Int = 0
    private var signIndex: Int = 0
    private var loginLayout: View? = null
    private var signUpLayout: View? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginactivity)

        val SwitchToLoginButton = findViewById<Button>(R.id.button)
        val SwitchToSignUpButton = findViewById<Button>(R.id.button2)
        loginLayout = findViewById(R.id.loginLayout);
        signUpLayout = findViewById(R.id.signLayout);
        changecolor(SwitchToLoginButton, SwitchToSignUpButton)

        val LoginInButton = findViewById<Button>(R.id.button3)
        LoginInButton.setOnClickListener {
            val email = findViewById<EditText>(R.id.editTextTextEmailAddress).text.toString()
            val password = findViewById<EditText>(R.id.editTextTextPassword2).text.toString()
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this,"Success",Toast.LENGTH_LONG).show()
                        val intent = Intent(this, ProfileActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this,"Something Wrong",Toast.LENGTH_LONG).show()
                    }
                }

        }
        val SignUpButton = findViewById<Button>(R.id.signupbutton)
        SignUpButton.setOnClickListener {
            val email = findViewById<EditText>(R.id.editTextTextEmailAddress2).text.toString()
            val password = findViewById<EditText>(R.id.editTextTextPassword3).text.toString()
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this,"Success",Toast.LENGTH_LONG).show()
                        val intent = Intent(this,ProfilePicture::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this,"Something Wrong",Toast.LENGTH_LONG).show()
                    }
                }

        }

        SwitchToLoginButton.setOnClickListener {
            logIndex++
            changecolor(SwitchToLoginButton, SwitchToSignUpButton)
            loginLayout?.visibility = View.VISIBLE
            signUpLayout?.visibility = GONE
        }

        SwitchToSignUpButton.setOnClickListener {
            signIndex++
            changecolor(SwitchToLoginButton, SwitchToSignUpButton)
            loginLayout?.visibility = GONE
            signUpLayout?.visibility = View.VISIBLE
        }
        /*val SwitchTologinButtonBTN = findViewById<Button>(R.id.SwitchTologinButtonBTN)
        SwitchTologinButtonBTN.setOnClickListener {
            logIndex++
            changecolor(SwitchToLoginButton, SwitchToSignUpButton)
            loginLayout?.visibility = View.VISIBLE
            signUpLayout?.visibility = GONE
        }*/
    }

    private fun changecolor(loginButton: Button, signButton: Button) {
        if (logIndex >= signIndex) {
            loginButton.setTextColor(Color1.parseColor("#E2010529"))
            signButton.setTextColor(Color1.parseColor("#E2868E8F"))
        } else if (signIndex >= logIndex) {
            loginButton.setTextColor(Color1.parseColor("#E2868E8F"))
            signButton.setTextColor(Color1.parseColor("#E2010529"))
        }
    }

}
