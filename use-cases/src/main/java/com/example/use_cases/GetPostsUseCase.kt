package com.example.use_cases

import com.example.data.ApiServiceInteractor
import com.example.data.errors.ResponseError
import com.example.data.models.PostRemoteModel
import com.example.data.models.UserRemoteModel
import com.example.use_cases.models.PostItemModel
import dagger.Reusable
import javax.inject.Inject

@Reusable
class GetPostsUseCase @Inject constructor(private val apiService: ApiServiceInteractor) {
    suspend operator fun invoke(
    ): List<PostItemModel> {
        return try {
            val users = apiService.getUsers().await()
            val posts = apiService.getPosts().await()
            posts.documents.mapNotNull {
                val post = it as? PostRemoteModel
                val username =
                    (users.documents as List<UserRemoteModel>).mapNotNull { user -> user.userFields?.username?.stringValue }
                        .firstOrNull { username ->
                            username == post?.postFields?.authorID?.stringValue
                        }
                mapToItem(username, post)
            }
        } catch (e: ResponseError) {
            emptyList()
        } catch (e: ClassCastException) {
            emptyList()
        }
    }

    private fun mapToItem(username: String?, remoteModel: PostRemoteModel?): PostItemModel? {
        return remoteModel?.postFields?.let {
            with(it) {
                PostItemModel(
                    id = id?.stringValue,
                    caption = caption?.stringValue,
                    comments = comments?.arrayValue?.values?.mapNotNull { comment -> comment.stringValue },
                    mediaType = mediaType?.stringValue,
                    storageRef = storageRef?.stringValue,
                    authorUserName = username,
                    createdAt = createdAt?.timestampValue
                )
            }
        }
    }
}