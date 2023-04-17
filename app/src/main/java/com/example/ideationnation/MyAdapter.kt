package com.example.ideationnation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class MyAdapter(private val commentList:ArrayList<Comment>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.comment_item,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem=commentList[position]

        holder.comment_show.text=currentitem.myComment
    }



    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val comment_show : TextView = itemView.findViewById(R.id.comment_disp)

    }
}