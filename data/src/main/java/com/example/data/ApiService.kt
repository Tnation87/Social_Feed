package com.example.data

import com.example.data.models.ListDocumentsRemoteResponseModel
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface ApiService {

    @GET("users")
    fun getUsersAsync(): Deferred<ListDocumentsRemoteResponseModel>

    @GET("posts")
    fun getPostsAsync(): Deferred<ListDocumentsRemoteResponseModel>

}
