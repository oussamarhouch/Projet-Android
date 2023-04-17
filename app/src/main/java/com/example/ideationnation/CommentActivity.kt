package com.example.ideationnation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ideationnation.databinding.ActivityCommentBinding
import com.google.firebase.database.*

class CommentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommentBinding
    private lateinit var database: DatabaseReference
    private lateinit var commentRecyclerView: RecyclerView
    private lateinit var commentArrayList: ArrayList<Comment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addCommBtn.setOnClickListener {
            val comment_string = binding.addComment.text.toString()

            database =
                FirebaseDatabase.getInstance("https://ideation-nation-f87ec-default-rtdb.europe-west1.firebasedatabase.app")
                    .getReference("Comments")
            val comment = Comment(comment_string)
            database.child(comment_string).setValue(comment).addOnSuccessListener {

                binding.addComment.text.clear()






                Toast.makeText(this, "Successfully saved", Toast.LENGTH_SHORT).show()


            }.addOnFailureListener {

                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
         //   displaying comments
            commentRecyclerView = findViewById(R.id.commentList)
           commentRecyclerView.layoutManager = LinearLayoutManager(this)
            commentRecyclerView.setHasFixedSize(true)

          commentArrayList = arrayListOf<Comment>()
           getCommentData()


       }
    }

    private fun getCommentData() {
        database = FirebaseDatabase.getInstance().getReference("Comments")
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    for (commentSnapshot in snapshot.children) {

                        val comment = commentSnapshot.getValue(Comment::class.java)
                        commentArrayList.add(comment!!)
                    }

                    commentRecyclerView.adapter = MyAdapter(commentArrayList)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
        }


