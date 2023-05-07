package com.example.ideationnation

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.UserHandle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.NonNull
import com.example.ideationnation.R
import com.example.ideationnation.databinding.ActivityAddIdeaBinding
import com.example.ideationnation.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.auth.User
import com.google.firebase.storage.StorageReference

class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddIdeaBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var dialog: Dialog
    private lateinit var user: User
    private lateinit var uid :String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddIdeaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addButton.setOnClickListener {
            val myIdea = binding.addIdea.text.toString()
            val title=binding.addTitle.text.toString()
            var id: Long = 0
            //ajout de uid
            auth = FirebaseAuth.getInstance()
            uid = auth.currentUser?.uid.toString()

            // ajout de id pour idée
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                         id = (dataSnapshot.getChildrenCount())

                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            }



            database=FirebaseDatabase.getInstance("https://ideation-nation-f87ec-default-rtdb.europe-west1.firebasedatabase.app").getReference("myIdeas")
            val idea= Idea(title, myIdea,uid, id)
            database.child(title).setValue(idea).addOnSuccessListener {

                binding.addIdea.text.clear()
                binding.addTitle.text.clear()

                Toast.makeText(this, "Successfully saved", Toast.LENGTH_SHORT).show()


            }.addOnFailureListener{

                Toast.makeText(this,"Failed", Toast.LENGTH_SHORT).show()
            }


        }






    }
}