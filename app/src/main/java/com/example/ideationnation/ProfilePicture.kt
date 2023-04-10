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
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.ideationnation.R.id.confirm
import java.io.ByteArrayOutputStream
import java.util.*


class ProfilePicture : AppCompatActivity() {

    companion object {
        const val REQUEST_IMAGE_GALLERY = 100
        const val REQUEST_IMAGE_CAPTURE = 101
    }

    private var profileImageView: ImageView? = null

    @SuppressLint("CutPasteId", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_picture)

        profileImageView = findViewById(R.id.profile_image)
        showImageSelectionDialog()

        findViewById<Button>(R.id.Add_photo).setOnClickListener { showImageSelectionDialog() }

        findViewById<Button>(R.id.skip).setOnClickListener {
            startActivity(Intent(this, AccueilActivity::class.java))
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
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
    }

    private fun takeImageFromCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_GALLERY -> {
                    if (data != null) {
                        val imageUri = data.data
                        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                        val circularBitmap = getRoundedBitmap(bitmap)

                        val sharedPrefs = getSharedPreferences("my_app_prefs", Context.MODE_PRIVATE)
                        val editor = sharedPrefs.edit()
                        editor.putString("profile_image_uri", imageUri.toString())
                        editor.putString("profile_image_circular", encodeBitmap(circularBitmap))
                        editor.apply()

                        profileImageView?.setImageBitmap(circularBitmap)
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
