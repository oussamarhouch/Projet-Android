package com.example.ideationnation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.widget.Button
import android.widget.RatingBar
import android.widget.Toast

class AllIdeasActivity() : AppCompatActivity(), Parcelable {
    constructor(parcel: Parcel) : this() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.allideas)

        //ajout du bouton qui nous méne à comment (Toubil)
        val buttonComment = findViewById<Button>(R.id.commBtn)
        buttonComment.setOnClickListener {
            val intent = Intent(this, CommentActivity::class.java)
            startActivity(intent)
        }


        //ajout rating bar

        val myRatingBar = findViewById<RatingBar>(R.id.ratingBar)
        myRatingBar.stepSize =.25f
        myRatingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            Toast.makeText(this, "Rated: $rating !", Toast.LENGTH_SHORT).show()

        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AllIdeasActivity> {
        override fun createFromParcel(parcel: Parcel): AllIdeasActivity {
            return AllIdeasActivity(parcel)
        }

        override fun newArray(size: Int): Array<AllIdeasActivity?> {
            return arrayOfNulls(size)
        }
    }
}

