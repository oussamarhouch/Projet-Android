package com.example.ideationnation

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.graphics.*
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MyIdeas : AppCompatActivity() {


    private lateinit var database:  DatabaseReference

    private lateinit var recyclerView: RecyclerView
   private lateinit var  articles : ArrayList<Idea>


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myideas)



     /*   val profileImageView = findViewById<ImageView>(R.id.profile_picture)
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
        showUserData()*/



        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager =  LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        articles = arrayListOf<Idea>()

        val user = FirebaseAuth.getInstance().currentUser
        getArticlesByUserId(user?.uid ?: "")



        }

    private fun getArticlesByUserId(userId: String) {
        database=FirebaseDatabase.getInstance().getReference("myIdeas")

        database.orderByChild("userId").equalTo(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {


                if(dataSnapshot.exists()) {
                    for (articleSnapshot in dataSnapshot.children) {

                        val article = articleSnapshot.getValue(Idea::class.java)

                        articles.add(article!!)



                    }
                    recyclerView.adapter = ArticleAdapter(articles)
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "getArticlesByUserId:onCancelled", databaseError.toException())

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