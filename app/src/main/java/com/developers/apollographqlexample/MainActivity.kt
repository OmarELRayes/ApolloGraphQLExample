package com.developers.apollographqlexample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import java.util.logging.Logger

class MainActivity : AppCompatActivity() {

    private val BASE_URL = "https://api.github.com/graphql"
    private lateinit var client: ApolloClient
    private var repos: ArrayList<GetUserRepos.Node>? = null
    private var reposAdapter: ReposAdapter? = null

    companion object {
        val Log = Logger.getLogger(MainActivity::class.java.name)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        reposAdapter = ReposAdapter(repos,this)
        recycle_view.adapter = reposAdapter
        client = setupApollo()
        client.query(GetUserRepos
                .builder()
                .build())
                .enqueue(object : ApolloCall.Callback<GetUserRepos.Data>() {

                    override fun onFailure(e: ApolloException) {
                        Log.info(e.message.toString())
                    }

                    override fun onResponse(response: Response<GetUserRepos.Data>) {
                        Log.info(" " + response.data()?.viewer()?.avatarUrl())
                        runOnUiThread({
                            Glide.with(this@MainActivity)
                                    .load(response.data()?.viewer()?.avatarUrl())
                                    .into(user_image_view)
                            reposAdapter?.setPosts(response.data()?.viewer()?.repositories()?.nodes() as MutableList)
                        })
                    }

                })
    }

    private fun setupApollo(): ApolloClient {
        val okHttp = OkHttpClient
                .Builder()
                .addInterceptor({ chain ->
                    val original = chain.request()
                    val builder = original.newBuilder().method(original.method(),
                            original.body())
                    builder.addHeader("Authorization"
                            , "Bearer " + "677ea36eafd106c92b747ad84b1577eba37aef12")
                    chain.proceed(builder.build())
                })
                .build()
        return ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttp)
                .build()
    }
}