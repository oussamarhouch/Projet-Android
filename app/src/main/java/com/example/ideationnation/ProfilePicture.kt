package com.example.ideationnation

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.ideationnation.R.id.confirm

class ProfilePicture : AppCompatActivity() {
    private val REQUEST_IMAGE_GALLERY = 100
    private val REQUEST_IMAGE_CAPTURE = 101
    private var mProfileImage: ImageView? = null

    @SuppressLint("CutPasteId", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_picture)
        mProfileImage = findViewById(R.id.profile_image)

        showImageSelectionDialog()

        val addPhotoButton = findViewById<Button>(R.id.Add_photo)

        addPhotoButton.setOnClickListener {
            showImageSelectionDialog()
        }

        val skipButton = findViewById<Button>(R.id.skip)
        skipButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showImageSelectionDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose an option")
        builder.setItems(
            arrayOf<CharSequence>("Gallery", "Camera"),
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    0 -> {
                        val intent = Intent(Intent.ACTION_GET_CONTENT)
                        intent.type = "image/*"
                        startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
                    }
                    1 -> {
                        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        if (takePictureIntent.resolveActivity(packageManager) != null) {
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                        }
                    }
                }
            })
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_GALLERY -> {
                    if (data != null) {
                        val imageUri = data.data
                        mProfileImage?.setImageURI(imageUri)
                        val confirmButton = findViewById<Button>(R.id.confirm)
                            confirmButton?.visibility = View.VISIBLE
                        confirmButton.setOnClickListener {

                            val intent = Intent(this, AccueilActivity::class.java)
                            startActivity(intent)
                        }

                    }
                }
                REQUEST_IMAGE_CAPTURE -> {
                    if (data != null && data.extras != null) {
                        val imageBitmap = data.extras!!.get("data") as Bitmap?
                        mProfileImage?.setImageBitmap(imageBitmap)
                        
                    }
                }
            }
        }
    }
}
