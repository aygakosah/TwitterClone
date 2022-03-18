package com.codepath.apps.restclienttemplate.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import org.json.JSONObject
import androidx.room.PrimaryKey

import androidx.room.ColumnInfo
import android.provider.ContactsContract.CommonDataKinds.Organization
import kotlinx.parcelize.Parcelize


@Entity
@Parcelize
class User(@ColumnInfo @PrimaryKey(autoGenerate = true) var id: Long? = null,
           @ColumnInfo var name: String ="",
           @ColumnInfo var screenName: String="",
           @ColumnInfo var publicImageUrl: String=""):Parcelable{


    companion object{
        fun fromJSON(jsonObject: JSONObject): User {
            val user = User()
            user.name = jsonObject.getString("name")
            user.screenName=jsonObject.getString("screen_name")
            user.publicImageUrl = jsonObject.getString("profile_image_url_https")
            return user
        }
    }
}