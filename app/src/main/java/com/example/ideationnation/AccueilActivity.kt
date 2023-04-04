package com.example.ideationnation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.provider.MediaStore
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import android.util.Base64


class AccueilActivity() : AppCompatActivity(), Parcelable {

    constructor(parcel: Parcel) : this() {
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acceuil)

        val profileImageView = findViewById<ImageView>(R.id.profile_icon)
        val sharedPrefs = getSharedPreferences("my_app_prefs", MODE_PRIVATE)
        val imageUriString = sharedPrefs.getString("profile_image_uri", "")

        if (!imageUriString.isNullOrEmpty()) {
            val imageUri = Uri.parse(imageUriString)
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            val circularBitmap = getRoundedBitmap(bitmap)
            profileImageView.setImageBitmap(circularBitmap)
            profileImageView.setOnClickListener {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }
        }
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

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AccueilActivity> {
        override fun createFromParcel(parcel: Parcel): AccueilActivity {
            return AccueilActivity(parcel)
        }

        override fun newArray(size: Int): Array<AccueilActivity?> {
            return arrayOfNulls(size)
        }
    }
}