package com.example.newsapp.Adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.newsapp.R
import com.example.newsapp.room.NewStar
//开源框架picasso
import com.squareup.picasso.Picasso

class NewStarAdapter(activity: Activity, val resourceId: Int, data: List<NewStar>) :
    ArrayAdapter<NewStar>(activity, resourceId, data) {
    inner class ViewHolder(val TIM: ImageView, val Tname: TextView, val Ttime: TextView, val Tsource: TextView)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resourceId, parent, false)
            val timg: ImageView = view.findViewById(R.id.news_image)
            val Tname: TextView = view.findViewById(R.id.news_title)
            val Ttime: TextView = view.findViewById(R.id.news_title)
            val Tsource: TextView = view.findViewById(R.id.news_source)
            viewHolder = ViewHolder(timg,Tname,Ttime, Tsource)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        val newStar = getItem(position)
        if (newStar != null) {
            Picasso.with(context).load(newStar.pic1).error(R.drawable.beixuan).into(viewHolder.TIM)
            viewHolder.Tname.text=newStar.title
            viewHolder.Tsource.text=newStar.source
            viewHolder.Ttime.text=newStar.date
        }
        return view
    }
}
