package com.example.ideationnation

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private var logIndex: Int = 0
    private var signIndex: Int = 0
    private lateinit var loginLayout: View
    private lateinit var signUpLayout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginactivity)

        val switchToLoginButton = findViewById<Button>(R.id.button)
        val switchToSignUpButton = findViewById<Button>(R.id.button2)
        loginLayout = findViewById(R.id.loginLayout)
        signUpLayout = findViewById(R.id.signLayout)

        switchToLoginButton.setOnClickListener {
            logIndex++
            updateLayoutVisibility()
            changeColor(switchToLoginButton, switchToSignUpButton)
        }

        switchToSignUpButton.setOnClickListener {
            signIndex++
            updateLayoutVisibility()
            changeColor(switchToLoginButton, switchToSignUpButton)
        }

        findViewById<Button>(R.id.button3).setOnClickListener {
            loginUser()
        }

        findViewById<Button>(R.id.signupbutton).setOnClickListener {
            signUpUser()
        }
    }

    private fun updateLayoutVisibility() {
        loginLayout.visibility = if (logIndex >= signIndex) View.VISIBLE else View.GONE
        signUpLayout.visibility = if (signIndex >= logIndex) View.VISIBLE else View.GONE
    }

    private fun changeColor(loginButton: Button, signButton: Button) {
        val loginColor = if (logIndex >= signIndex) "#E2010529" else "#E2868E8F"
        val signUpColor = if (signIndex >= logIndex) "#E2010529" else "#E2868E8F"
        loginButton.setTextColor(Color.parseColor(loginColor))
        signButton.setTextColor(Color.parseColor(signUpColor))
    }

    private fun loginUser() {
        val email = findViewById<EditText>(R.id.editTextTextEmailAddress).text.toString()
        val password = findViewById<EditText>(R.id.editTextTextPassword2).text.toString()

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast("Success")
                    navigateToProfilePicture()
                } else {
                    showToast("Something went wrong")
                }
            }
    }

    private fun signUpUser() {
        val email = findViewById<EditText>(R.id.editTextTextEmailAddress2).text.toString()
        val password = findViewById<EditText>(R.id.editTextTextPassword3).text.toString()

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast("Success")
                    navigateToProfilePicture()
                } else {
                    showToast("Something went wrong")
                }
            }
    }

    private fun navigateToProfilePicture() {
        val intent = Intent(this, ProfilePicture::class.java)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
