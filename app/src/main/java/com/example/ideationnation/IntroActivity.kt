package com.example.ideationnation


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible

class IntroActivity : AppCompatActivity() {
    private lateinit var textView1: TextView
    private lateinit var textView2: TextView
    private var currentIndex: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        textView1 = findViewById(R.id.textView2)
        textView2 = findViewById(R.id.textView3)
        showCurrentTextView()

        val nextButton = findViewById<Button>(R.id.next)
        val previousButton = findViewById<Button>(R.id.previous)
        nextButton.setOnClickListener {
            currentIndex++
            if (currentIndex == 2) {

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)

                finish()
            } else {
                // afficher l'élément suivant de la liste
                showCurrentTextView()
            }

        }

        previousButton.setOnClickListener {
            currentIndex--
            showCurrentTextView()
        }
    }

    private fun showCurrentTextView() {
        textView1.isVisible = currentIndex == 0
        textView2.isVisible = currentIndex == 1

    }
}

