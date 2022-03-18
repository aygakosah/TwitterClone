package com.codepath.apps.restclienttemplate.models

import android.icu.util.Calendar
import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize
import org.json.JSONArray
import org.json.JSONObject


@Entity(
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("user_id")
    )]
)

@Parcelize
class Tweet(@ColumnInfo @PrimaryKey(autoGenerate = true) var id: Long? = null,
            @ColumnInfo var body: String = "", @ColumnInfo var createdAt: String="",
            @ColumnInfo var duration: String="",
            @ColumnInfo var uid: Long = 0,
            @Ignore var user: User? = null,
            @ColumnInfo var user_id: Long? = null,
            var mediaImageUrl: String=""):Parcelable{

    companion object{
        fun fromJson(jsonObject: JSONObject): Tweet{
            val tweet = Tweet()
            tweet.body = jsonObject.getString("text")
            tweet.createdAt=jsonObject.getString("created_at")
            tweet.duration = TimeFormatter.getTimeDifference(tweet.createdAt)
            tweet.uid=jsonObject.getLong("id")
            tweet.user= User.fromJSON(jsonObject.getJSONObject("user"))
            tweet.user_id =tweet.user?.id
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