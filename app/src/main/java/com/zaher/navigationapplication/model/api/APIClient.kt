package com.zaher.navigationapplication.model.api

import com.zaher.navigationapplication.model.pojo.Posts
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class APIClient {
    private val BASE_URL = "https://jsonplaceholder.typicode.com/"
    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(APIInterface::class.java)

    fun getPosts(): Single<List<Posts>> {
        return api.getPosts()
    }

    fun getSinglePost(id: Int): Single<Posts> {
        return api.getPostDetails(id)
    }
}