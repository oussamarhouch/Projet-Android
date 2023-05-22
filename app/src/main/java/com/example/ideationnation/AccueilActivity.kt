
package com.example.ideationnation

import android.annotation.SuppressLint
import android.content.ContentValues
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
import android.util.Log
import android.widget.Button
import android.widget.RatingBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ideationnation.data.ProfileActivity
import com.google.firebase.database.*


class AccueilActivity() : AppCompatActivity(), Parcelable {

    private lateinit var database: DatabaseReference

    private lateinit var recyclerView: RecyclerView
    private lateinit var  articles : ArrayList<Idea>

    constructor(parcel: Parcel) : this() {
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acceuil)


       recyclerView=findViewById(R.id.allideas)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        articles = arrayListOf<Idea>()
        getArticles()
        recyclerView.adapter = IdeasAdapter(articles)


        // ajout du boutton qui nous méne à add (Toubil)
        val buttonClick = findViewById<Button>(R.id.button5)
        buttonClick.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }







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
    private fun getArticles() {
        database = FirebaseDatabase.getInstance().getReference("myIdeas")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {


                if(dataSnapshot.exists()) {
                    for (articleSnapshot in dataSnapshot.children) {
                        val ideaMap = articleSnapshot.getValue() as? Map<*, *>

                        val title = ideaMap?.get("title") as? String
                        val content = ideaMap?.get("myIdea") as? String

                        val idea = Idea(title, content)


                        articles.add(idea!!)



                    }
                    recyclerView.adapter = IdeasAdapter(articles)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Gestion des erreurs lors de la récupération des données
            }
        })
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