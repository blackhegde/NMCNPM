package com.example.layout

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import kotlin.UShortArray

class MainAdapter(private val userList: List<UserStatus>) :
    RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgAvatar: ImageView = itemView.findViewById(R.id.img_avatar)
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvAddress: TextView = itemView.findViewById(R.id.tv_address)
        val tvDistance: TextView = itemView.findViewById(R.id.tv_distance_value)
        val tvPace: TextView = itemView.findViewById(R.id.tv_pace_value)
        val tvTime: TextView = itemView.findViewById(R.id.tv_time_value)
        val imgMap: ImageView = itemView.findViewById(R.id.img_route_map)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_main, parent, false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val userStatus = userList[position]
        holder.imgAvatar.setImageResource(userStatus.avatarResId)
        holder.tvName.text = userStatus.name
        holder.tvAddress.text = userStatus.address
        holder.tvDistance.text = userStatus.distance
        holder.tvPace.text = userStatus.pace
        holder.tvTime.text = userStatus.time
        holder.imgMap.setImageResource(userStatus.mapResId)
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}