package com.example.ideationnation

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler().postDelayed({
            val intent = Intent(/* packageContext = */ this@MainActivity, /* cls = */ IntroActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}
