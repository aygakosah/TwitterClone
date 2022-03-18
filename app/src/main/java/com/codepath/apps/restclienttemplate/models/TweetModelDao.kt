package com.codepath.apps.restclienttemplate.models

import androidx.room.*


@Dao
interface TweetModelDao {
    @Query("SELECT * FROM Tweet WHERE id = :id")
    fun byId(id: Long): Tweet?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertModel(vararg tweets: Tweet?)

    @Delete
    fun deleteUser(tweet: Tweet)

}