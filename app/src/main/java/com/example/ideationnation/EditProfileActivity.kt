package com.example.ideationnation

import android.util.Base64
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.ideationnation.R.id.confirm
import java.io.ByteArrayOutputStream
import java.util.*


class EditProfileActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_IMAGE_GALLERY = 100
        const val REQUEST_IMAGE_CAPTURE = 101
    }

    private var profileImageView: ImageView? = null
    private var nameEditText: EditText? = null
    private var bioEditText: EditText? = null
    private var usernameEditText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        nameEditText = findViewById(R.id.editName)
        bioEditText = findViewById(R.id.editAboutme)
        usernameEditText = findViewById(R.id.editusername)
        loadUserData()
        profileImageView = findViewById(R.id.edit_profile_image)
        showCurrentProfileImage()
        findViewById<ImageView>(R.id.x).setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<ImageView>(R.id.check).setOnClickListener {
            saveUserData()
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }
        findViewById<Button>(R.id.Edit_picture).setOnClickListener { showImageSelectionDialog() }
    }
    private fun loadUserData() {
        val sharedPrefs = getSharedPreferences("my_app_prefs", MODE_PRIVATE)
        val name = sharedPrefs.getString("name", "")
        val bio = sharedPrefs.getString("bio", "")
        val username = sharedPrefs.getString("username", "")
        nameEditText?.setText(name)
        bioEditText?.setText(bio)
        usernameEditText?.setText(username)
    }

    private fun saveUserData() {
        val sharedPrefs = getSharedPreferences("my_app_prefs", MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        val name = nameEditText?.text.toString()
        val bio = bioEditText?.text.toString()
        val username = usernameEditText?.text.toString()
        Log.d("MyApp", "Name: $name")
        Log.d("MyApp", "Bio: $bio")
        Log.d("MyApp", "Username: $username")
        editor.putString("name", name)
        editor.putString("bio", bio)
        editor.putString("username", username)
        editor.apply()
    }

    private fun showCurrentProfileImage() {
        val sharedPrefs = getSharedPreferences("my_app_prefs", MODE_PRIVATE)
        val imageUriString = sharedPrefs.getString("profile_image_uri", "")
        if (!imageUriString.isNullOrEmpty()) {
            val imageUri = Uri.parse(imageUriString)
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            val circularBitmap = getRoundedBitmap(bitmap)
            profileImageView?.setImageBitmap(circularBitmap)
        }
    }
    private fun showImageSelectionDialog() {
        AlertDialog.Builder(this)
            .setTitle("Choose an option")
            .setItems(arrayOf<CharSequence>("Gallery", "Camera")) { dialog, which ->
                when (which) {
                    0 -> selectImageFromGallery()
                    1 -> takeImageFromCamera()
                }
            }
            .show()
    }
    private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, ProfilePicture.REQUEST_IMAGE_GALLERY)
    }

    private fun takeImageFromCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, ProfilePicture.REQUEST_IMAGE_CAPTURE)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                ProfilePicture.REQUEST_IMAGE_GALLERY -> {
                    if (data != null) {
                        val imageUri = data.data
                        if (imageUri != null) { // Vérification si imageUri est non nul
                            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                            val circularBitmap = getRoundedBitmap(bitmap)

                            val sharedPrefs = getSharedPreferences("my_app_prefs", Context.MODE_PRIVATE)
                            val editor = sharedPrefs.edit()
                            editor.putString("profile_image_uri", imageUri.toString())
                            editor.putString("profile_image_circular", encodeBitmap(circularBitmap))
                            editor.apply()

                            profileImageView?.setImageBitmap(circularBitmap)

                        }
                    }
                }
                ProfilePicture.REQUEST_IMAGE_CAPTURE -> {
                    if (data != null && data.extras != null) {
                        val imageBitmap = data.extras!!.get("data") as Bitmap?
                        val circularBitmap = getRoundedBitmap(imageBitmap!!)
                        val sharedPrefs = getSharedPreferences("my_app_prefs", Context.MODE_PRIVATE)
                        val editor = sharedPrefs.edit()
                        editor.putString("profile_image_circular", encodeBitmap(circularBitmap))
                        editor.apply()
                        profileImageView?.setImageBitmap(circularBitmap)

                    }
                }
            }
        }
    }

    private fun encodeBitmap(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
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



}



