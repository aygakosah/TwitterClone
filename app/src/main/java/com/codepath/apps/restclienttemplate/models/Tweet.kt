package com.codepath.apps.restclienttemplate.models

import android.icu.util.Calendar
import org.json.JSONArray
import org.json.JSONObject

class Tweet {

    var body: String = ""
    var createdAt: String=""
    var duration: String=""
    var uid: Long = 0
    var user: User? = null

    companion object{
        fun fromJson(jsonObject: JSONObject): Tweet{
            val tweet = Tweet()
            tweet.body = jsonObject.getString("text")
            tweet.createdAt=jsonObject.getString("created_at")
            tweet.duration = TimeFormatter.getTimeDifference(tweet.createdAt)
            tweet.uid=jsonObject.getLong("id")
            tweet.user= User.fromJSON(jsonObject.getJSONObject("user"))
            return tweet
        }

        fun fromJsonArray(jsonArray: JSONArray): List<Tweet> {
            val tweets = ArrayList<Tweet>()
            for(i in 0 until jsonArray.length()){
                tweets.add(fromJson(jsonArray.getJSONObject(i)))
            }
            return tweets
        }

    }

}