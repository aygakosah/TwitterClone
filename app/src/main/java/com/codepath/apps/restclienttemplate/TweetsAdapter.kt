package com.codepath.apps.restclienttemplate

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.codepath.apps.restclienttemplate.models.Tweet

class TweetsAdapter( val tweets:
ArrayList<Tweet>) : RecyclerView.Adapter<TweetsAdapter.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_tweet, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tweet: Tweet = tweets.get(position)
        holder.tvUserName.text = tweet.user?.name
        holder.tvScreenName.text = tweet.user?.screenName
        holder.tvTweetBody.text = tweet.body
        holder.tvTweetMin.text = tweet.duration

        Glide.with(holder.itemView).load(tweet.user?.publicImageUrl).into(holder.ivProfileImage)

    }

    override fun getItemCount() = tweets.size

    fun clear() {
        tweets.clear()
        notifyDataSetChanged()
    }

    // Add a list of items -- change to type used
    fun addAll(tweetList: List<Tweet>) {
        tweets.addAll(tweetList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView){
         val  ivProfileImage = itemView.findViewById<ImageView>(R.id.ivProfileImage)
         val  tvUserName = itemView.findViewById<TextView>(R.id.tvUsername)
         val  tvScreenName = itemView.findViewById<TextView>(R.id.tvHandle)
         val  tvTweetBody = itemView.findViewById<TextView>(R.id.tvTweetBody)
        val  tvTweetMin = itemView.findViewById<TextView>(R.id.min)


    }

}