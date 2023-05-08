package com.example.ideationnation.data
import android.Manifest


import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ideationnation.EditProfileActivity
import com.example.ideationnation.MyIdeas

import com.example.ideationnation.R
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {


    val REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION =101
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION)
        }





        val profileImageView = findViewById<ImageView>(R.id.profile_picture)
        val sharedPrefs = getSharedPreferences("my_app_prefs", MODE_PRIVATE)
        val imageUriString = sharedPrefs.getString("profile_image_uri", "")
        if (!imageUriString.isNullOrEmpty()) {
            val imageUri = Uri.parse(imageUriString)
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            val circularBitmap = getRoundedBitmap(bitmap)
            profileImageView.setImageBitmap(circularBitmap)
        }


        val editButton = findViewById<Button>(R.id.edit_profile)
        editButton.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }
        val ShowIdeas = findViewById<Button>(R.id.show_myideas)
        ShowIdeas.setOnClickListener {
            val intent = Intent(this, MyIdeas::class.java)
            startActivity(intent)
        }

        saveUserData()
        showUserData()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted
        } else {
            // Permission denied
        }
    }

    private fun saveUserData() {
        val sharedPrefs = getSharedPreferences("my_app_prefs", MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val username = user.email
            editor.putString("username", username)
            editor.apply()
        }

    }
    private fun showUserData() {
        val sharedPrefs = getSharedPreferences("my_app_prefs", MODE_PRIVATE)
        val username = sharedPrefs.getString("username", "")
        val bio = sharedPrefs.getString("bio", "")
        val name = sharedPrefs.getString("name", "")
        Log.d("MyApp", "Username: $username")
        Log.d("MyApp", "Bio: $bio")
        Log.d("MyApp", "Name: $name")
        findViewById<TextView>(R.id.username)?.text = username
        findViewById<TextView>(R.id.Aboutme)?.setText(bio)
        findViewById<TextView>(R.id.name)?.text = name
    }




    private fun getRoundedBitmap(bitmap: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width * 2, bitmap.height * 2, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val radius = bitmap.width / 2.toFloat() *2

        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        canvas.drawCircle(radius, radius, radius, paint)

        return output
    }

    private fun decodeBase64(input: String): Bitmap {
        val decodedBytes = Base64.decode(input, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }
}