package com.example.ideationnation

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.*
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import android.util.Log
import android.widget.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var adapter: ArticleAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var spinnerSort: Spinner
    private lateinit var layoutInflater: LayoutInflater

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
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
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            findViewById<TextView>(R.id.username)?.text = user.displayName
        }

        showUserData()
        layoutInflater = LayoutInflater.from(this@ProfileActivity)
        database = FirebaseDatabase.getInstance().reference
        adapter = ArticleAdapter()
        recyclerView = findViewById(R.id.recyclerView)
        val layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter



        val sortOptions = arrayOf(

            "Par titre (ascendant)",
            "Par titre (descendant)"

        )

        spinnerSort = findViewById(R.id.spinner)
        spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedOption = sortOptions[position]
                val sortBy = when (selectedOption) {

                    "Par titre (ascendant)" -> "title"
                    else -> "title DESC"
                }
                getArticlesByUserId(user?.uid ?: "", sortBy) { articles ->
                    adapter.setArticles(articles)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
    private fun getArticlesByUserId(userId: String, sortBy: String, callback: (List<Idea>) -> Unit) {
        val query = database.child("articles").orderByChild("userId").equalTo(userId)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val articles = mutableListOf<Idea>()
                for (articleSnapshot in dataSnapshot.children) {
                    val article = articleSnapshot.getValue(Idea::class.java)
                    if (article != null) {
                        articles.add(article)
                    }
                }
                when (sortBy) {


                    "title" -> articles.sortBy { it.title }

                    else -> articles.sortByDescending { it.title }
                }
                callback(articles)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "getArticlesByUserId:onCancelled", databaseError.toException())
                callback(emptyList())
            }
        })
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