package com.zaher.navigationapplication.model.api

import com.zaher.navigationapplication.model.pojo.Posts
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface APIInterface {
    @GET("posts")
    fun getPosts(): Single<List<Posts>>

    @GET("posts/{id}")
    fun getPostDetails(@Path("id") id: Int): Single<Posts>
}