package com.codepath.apps.restclienttemplate.models

import androidx.room.*


@Dao
interface UserModelDao {

    @Query("SELECT * FROM User WHERE id = :id")
    fun byId(id: Long): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertModel(vararg users: User?)

    @Delete
    fun deleteUser(user: User)
}