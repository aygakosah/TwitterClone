package com.codepath.apps.restclienttemplate

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codepath.apps.restclienttemplate.models.*

@Database(entities = [Tweet::class, User::class], version = 2)
abstract class MyDatabase : RoomDatabase() {
    abstract fun userModelDao(): UserModelDao?
    abstract fun tweetModelDao(): TweetModelDao?

    companion object {
        // Database name to be used
        const val NAME = "TweetsData"
    }
}