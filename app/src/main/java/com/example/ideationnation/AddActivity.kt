package com.example.ideationnation

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ideationnation.databinding.ActivityAddIdeaBinding
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
    var id: Long=0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddIdeaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addButton.setOnClickListener {
            val myIdea = binding.addIdea.text.toString()
            val title=binding.addTitle.text.toString()

            //ajout de uid
            auth = FirebaseAuth.getInstance()
            uid = auth.currentUser?.uid.toString()

            database=FirebaseDatabase.getInstance("https://ideation-nation-b83f9-default-rtdb.firebaseio.com").getReference("myIdeas")
           /*
            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val childrenCount = dataSnapshot.childrenCount
                        id= childrenCount
                        // Do something with the children count
                    } else {
                        // Handle the case where the reference doesn't exist
                    }
                }


                override fun onCancelled(databaseError: DatabaseError) {

                }
            })*/








            val idea= Idea(title, myIdea,uid, database.push().key)
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