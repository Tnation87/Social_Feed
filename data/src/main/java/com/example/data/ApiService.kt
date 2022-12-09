package com.example.data

import com.example.data.models.ListDocumentsRemoteResponseModel
import com.example.data.models.PostRemoteModel
import com.example.data.models.UserRemoteModel
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface ApiService {

    @GET("users")
    fun getUsersAsync(): Deferred<ListDocumentsRemoteResponseModel<UserRemoteModel>>

    @GET("posts")
    fun getPostsAsync(): Deferred<ListDocumentsRemoteResponseModel<PostRemoteModel>>

}
