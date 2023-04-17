package com.example.ideationnation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.ideationnation.R
import com.example.ideationnation.databinding.ActivityAddIdeaBinding
import com.example.ideationnation.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddIdeaBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddIdeaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addButton.setOnClickListener {
            val myIdea = binding.addIdea.text.toString()
            val title=binding.addTitle.text.toString()

            database=FirebaseDatabase.getInstance("https://ideation-nation-f87ec-default-rtdb.europe-west1.firebasedatabase.app").getReference("myIdeas")
            val idea= Idea(title, myIdea)
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