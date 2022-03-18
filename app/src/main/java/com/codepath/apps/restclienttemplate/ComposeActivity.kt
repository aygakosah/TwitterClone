package com.codepath.apps.restclienttemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

const val TAG = "Compose"
class ComposeActivity : AppCompatActivity() {
    lateinit var  etCompose : EditText
    lateinit var btnTweet : Button
    lateinit var tvCharCount: TextView
    lateinit var client : TwitterClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.etTweetCompose)
        btnTweet = findViewById(R.id.btnTweet)
        tvCharCount = findViewById(R.id.tvCharCount)

        client = TwitterApplication.getRestClient(this)


        etCompose.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(charsequence: CharSequence, start : Int, end: Int, count : Int) {
                Log.i(TAG, "cHAR COUNT IS $charsequence.length.toString()")
                val charLength = charsequence.length.toString()
                tvCharCount.text = "Char count: $charLength"

                if(charLength.toInt() > 240){
                    btnTweet.setEnabled(false)
                }else{
                    btnTweet.setEnabled(true)
                }
            }

            override fun afterTextChanged(p0: Editable?) {


            }

        })

        btnTweet.setOnClickListener {

            val tweetContent = etCompose.text.toString()

            if(tweetContent.isEmpty()){
                Toast.makeText(this, "Field cannnot be empty", Toast.LENGTH_SHORT)
                    .show()
            }else if(tweetContent.length > 140){
                Toast.makeText(this, "Tweet is too long", Toast.LENGTH_SHORT)
                    .show()
            }else{
                client.publishTweet(tweetContent, object: JsonHttpResponseHandler(){

                    override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                        Log.i(TAG, "Tweet Successfully published" )
                        val tweet = Tweet.fromJson(json.jsonObject)
                        val intent = Intent()
                        intent.putExtra("tweet", tweet)
                        setResult(RESULT_OK, intent)
                        finish()
                    }

                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        response: String?,
                        throwable: Throwable?
                    ) {
                        Log.e(TAG, "Error publishing tweet" )
                    }



                })
            }

        }
    }
}