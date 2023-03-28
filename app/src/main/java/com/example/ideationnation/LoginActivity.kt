package com.example.ideationnation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
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

        val loginButton = findViewById<Button>(R.id.button)
        val signButton = findViewById<Button>(R.id.button2)
        loginLayout = findViewById(R.id.loginLayout);
        signUpLayout = findViewById(R.id.signLayout);
        changecolor(loginButton, signButton)
        val skipButton = findViewById<Button>(R.id.button3)
        skipButton.setOnClickListener {
            val intent = Intent(this , ProfileActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            logIndex++
            changecolor(loginButton, signButton)
            loginLayout?.visibility = View.VISIBLE
            signUpLayout?.visibility = GONE
        }

        signButton.setOnClickListener {
            signIndex++
            changecolor(loginButton, signButton)
            loginLayout?.visibility = GONE
            signUpLayout?.visibility = View.VISIBLE
        }
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
