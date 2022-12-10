package com.example.repos.feed

import com.example.repos.models.FeedDataModel

interface PostsRepo {
    suspend fun getPosts(): FeedDataModel
}