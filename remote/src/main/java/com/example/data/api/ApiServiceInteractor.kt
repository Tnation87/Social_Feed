package com.example.data.api

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiServiceInteractor @Inject constructor(private val apiService: ApiService) {

    fun getPosts() = apiService.getPostsAsync()

    fun getUsers() = apiService.getUsersAsync()
}