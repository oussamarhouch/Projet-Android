package com.example.ideationnation

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import android.util.Log

class ProfileActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var adapter: ArticleAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var spinnerSort: Spinner
    private val layoutInflater: LayoutInflater = LayoutInflater.from(this@ProfileActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        database = FirebaseDatabase.getInstance().reference
        adapter = ArticleAdapter()
        recyclerView = findViewById(R.id.recyclerView)
        val layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        val user = FirebaseAuth.getInstance().currentUser
        getArticlesByUserId(user?.uid ?: "", "date DESC") { articles ->
            adapter.setArticles(articles)
        }

        val sortOptions = arrayOf(
            "Par date (ascendant)",
            "Par titre (ascendant)",
            "Par titre (descendant)",
            "Par date (descendant)"
        )

        spinnerSort = findViewById(R.id.spinner)
        spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedOption = sortOptions[position]
                val sortBy = when (selectedOption) {
                    "Par date (ascendant)" -> "date"
                    "Par titre (ascendant)" -> "title"
                    "Par titre (descendant)" -> "title DESC"
                    else -> "date DESC"
                }
                getArticlesByUserId(user?.uid ?: "", sortBy) { articles ->
                    adapter.setArticles(articles)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun getArticlesByUserId(userId: String, sortBy: String, callback: (List<Article>) -> Unit) {
        val query = database.child("articles").orderByChild("userId").equalTo(userId)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val articles = mutableListOf<Article>()
                for (articleSnapshot in dataSnapshot.children) {
                    val article = articleSnapshot.getValue(Article::class.java)
                    if (article != null) {
                        articles.add(article)
                    }
                }
                when (sortBy) {
                    "date" -> articles.sortBy { it.date }
                    "title" -> articles.sortBy { it.title }
                    "title DESC" -> articles.sortByDescending { it.title }
                    else -> articles.sortByDescending { it.date }
                }
                callback(articles)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "getArticlesByUserId:onCancelled", databaseError.toException())
                callback(emptyList())
            }
        })
    }
}
