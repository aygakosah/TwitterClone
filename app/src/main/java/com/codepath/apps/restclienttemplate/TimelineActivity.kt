package com.codepath.apps.restclienttemplate

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.apps.restclienttemplate.models.TweetModelDao
import com.codepath.apps.restclienttemplate.models.User
import com.codepath.apps.restclienttemplate.models.UserModelDao
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException
import java.lang.Double.POSITIVE_INFINITY

class TimelineActivity : AppCompatActivity() {
    lateinit var client: TwitterClient
    lateinit var rvTweets: RecyclerView
    lateinit var adapter: TweetsAdapter
    lateinit var swipeContainer: SwipeRefreshLayout

    val tweets = ArrayList<Tweet>()
    var tweetModelDao: TweetModelDao? = null
    lateinit var scrollListener: EndlessRecyclerViewScrollListener
    var maxId: Long = POSITIVE_INFINITY.toLong()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)
        swipeContainer = findViewById(R.id.swipeContainer)
        swipeContainer.setOnRefreshListener {
            // Your code to refresh the list here.
            // Make sure you call swipeContainer.setRefreshing(false)
            // once the network request has completed successfully.
            Log.i(TAG, "Refreshing the timeline")
            populateHomeTimeline()
        }



        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light)

        client = TwitterApplication.getRestClient(this)
        rvTweets=findViewById(R.id.rvTweets)
        adapter = TweetsAdapter(tweets)
        val linearLayoutManager = LinearLayoutManager(this)
        rvTweets.layoutManager=linearLayoutManager
        rvTweets.adapter = adapter
        populateHomeTimeline()
        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Log.i(TAG, "Reached end of screen")
                loadMoreData()
            }
        }
        rvTweets.addOnScrollListener(scrollListener)
    }

    fun loadMoreData() {
        client.getNextPageOfTweets(object: JsonHttpResponseHandler(){
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.i(TAG, "Failure")
            }

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i(TAG, "OnSucces, $json")
                try{
                    val jsonArray = json.jsonArray

                    val listOfNewTweets = Tweet.fromJsonArray(jsonArray)
                    for(item in listOfNewTweets){
                        if(item.uid<maxId){
                            var id: Long = item.uid
                            Log.i(TAG, "TWEET ID IS $id")
                            Log.i(TAG, "MAX ID NOW IS  $maxId")
                            maxId = item.uid - 1
                        }
                        val tweet = item
                        tweetModelDao = (applicationContext as TwitterApplication).myDatabase?.tweetModelDao()
                        AsyncTask.execute { tweetModelDao?.insertModel(tweet) }
                    }
                    Log.i(TAG, "$maxId")
                    tweets.addAll(listOfNewTweets)
                    adapter.notifyDataSetChanged()
                    swipeContainer.setRefreshing(false)
                }catch(e:  JSONException){
                    Log.e(TAG, "Json exception $e")
                }
            }

        }, maxId)

        // 1. Send an API request to retrieve appropriate paginated data
        // 2. Deserialize and construct new model objects from the API response
        // 3. Append the new data objects to the existing set of items inside the array of items
        // 4. Notify the adapter of the new items made with `notifyItemRangeInserted()`
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.compose){
            val intent = Intent(this, ComposeActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }
        return super.onOptionsItemSelected(item)
    }

    //Let's us return to the timeline activity after posting a tweet
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode== RESULT_OK && requestCode== REQUEST_CODE){
            val tweet = data?.getParcelableExtra("tweet") as Tweet?
            tweets.add(0, tweet!!)
            adapter.notifyItemInserted(0)
            rvTweets.smoothScrollToPosition(0)

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun populateHomeTimeline(){
        client.getHomeTimeline(object: JsonHttpResponseHandler(){
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.i(TAG, "Failure")
            }

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i(TAG, "OnSucces, $json")

                try{
                    adapter.clear()
                    val jsonArray = json.jsonArray

                    val listOfNewTweets = Tweet.fromJsonArray(jsonArray)
                    for(item in listOfNewTweets){
                        if(item.uid<maxId){
                            var id: Long = item.uid
                            Log.i(TAG, "TWEET ID IS $id")
                            Log.i(TAG, "MAX ID NOW IS  $maxId")
                            maxId = item.uid - 1
                        }
                    }
                    Log.i(TAG, "$maxId")
                    tweets.addAll(listOfNewTweets)
                    adapter.notifyDataSetChanged()
                    swipeContainer.setRefreshing(false)
                }catch(e:  JSONException){
                    Log.e(TAG, "Json exception $e")
                }

            }

        })

    }

    companion object{
        val TAG = "TimelineActivity"
        val REQUEST_CODE=10
    }
}