package com.example.repos.feed

import android.net.Uri
import com.example.data.api.ApiServiceInteractor
import com.example.data.firebaseStorage.FirebaseStorageInteractor
import com.example.data.models.PostRemoteModel
import com.example.data.models.UserRemoteModel
import com.example.repos.models.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PostsRepoImpl @Inject constructor(
    private val apiService: ApiServiceInteractor,
    private val firebaseStorageInteractor: FirebaseStorageInteractor
) : PostsRepo {
    override suspend fun getPosts(): FeedDataModel {
        val users = apiService.getUsers().await()
        val posts = apiService.getPosts().await()

        val mappedPosts = posts.documents.map {

            val mediaRef = it.postFields?.storageRef?.stringValue?.let { storageRef ->
                firebaseStorageInteractor.getMedia(
                    storageRef
                ).await()
            }

            mapPostToItem(mediaRef, it)
        }

        val mappedUsers = users.documents.map {
            mapUserToItem(it)
        }

        return FeedDataModel(mappedUsers, mappedPosts)
    }

    private fun mapPostToItem(mediaRef: Uri?, remoteModel: PostRemoteModel?): PostItemModel? {
        return remoteModel?.postFields?.let {
            with(it) {
                PostItemModel(
                    PostFields(
                        id = id,
                        caption = caption,
                        comments = comments,
                        mediaType = mediaType,
                        storageRef = mediaRef,
                        authorID = authorID,
                        createdAt = createdAt
                    )
                )
            }
        }
    }

    private fun mapUserToItem(remoteModel: UserRemoteModel?): UserItemModel? {
        return remoteModel?.userFields?.let {
            UserItemModel(
                path = remoteModel.path,
                userFields = UserFields(
                    username = it.username
                )
            )
        }
    }
}