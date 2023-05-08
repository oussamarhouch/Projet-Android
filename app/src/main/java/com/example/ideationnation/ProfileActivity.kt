package com.example.ideationnation
import android.Manifest

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {
    private val REQUEST_CODE = 1001

    private lateinit var database:  DatabaseReference

    private lateinit var adapter: ArticleAdapter
    private lateinit var recyclerView: RecyclerView

    private lateinit var layoutInflater: LayoutInflater

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), REQUEST_CODE)
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


        saveUserData()
        showUserData()

        layoutInflater = LayoutInflater.from(this@ProfileActivity)
        database=FirebaseDatabase.getInstance("https://ideation-nation-b83f9-default-rtdb.firebaseio.com").getReference("myIdeas")

        adapter = ArticleAdapter()
        recyclerView = findViewById(R.id.recyclerView)
        val layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        val sortOptions = arrayOf(
            "Par titre (ascendant)",
            "Par titre (descendant)"
        )



                val user = FirebaseAuth.getInstance().currentUser
                getArticlesByUserId(user?.uid ?: "") { articles ->
                    adapter.setArticles(articles)
                }



        }

    private fun getArticlesByUserId(userId: String,  callback: (List<Idea>) -> Unit) {

        database.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {


                val articles = mutableListOf<Idea>()
                for (articleSnapshot in dataSnapshot.children) {


                    val article = articleSnapshot.getValue(Idea::class.java)
                    if (article != null) {
                        articles.add(article)
                    }
                }
                Log.d("MyApp", "Nombre d'idées récupérées : " + articles.size)

                callback(articles)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "getArticlesByUserId:onCancelled", databaseError.toException())
                callback(emptyList())
            }
        })
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