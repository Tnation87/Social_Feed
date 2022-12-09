package com.example.use_cases

import com.example.data.ApiServiceInteractor
import com.example.data.errors.ResponseError
import com.example.data.models.PostRemoteModel
import com.example.use_cases.models.ItemMediaType
import com.example.use_cases.models.PostItemModel
import dagger.Reusable
import java.time.LocalDateTime
import javax.inject.Inject

@Reusable
class GetPostsUseCase @Inject constructor(private val apiService: ApiServiceInteractor) {
    suspend operator fun invoke(
    ): List<PostItemModel> {
        return try {
            val users = apiService.getUsers().await()
            val posts = apiService.getPosts().await()
            posts.documents.mapNotNull {
                val username =
                    users.documents.mapNotNull { user -> user.userFields?.username?.stringValue }
                        .firstOrNull { username ->
                            username == it.postFields?.authorID?.stringValue
                        }
                mapToItem(username, it)
            }
        } catch (e: ResponseError) {
            emptyList()
        }
    }

    private fun mapToItem(username: String?, remoteModel: PostRemoteModel?): PostItemModel? {
        return remoteModel?.postFields?.let {
            with(it) {
                val videoOrImage = when (mediaType?.stringValue) {
                    "photo" -> ItemMediaType.IMAGE
                    "video" -> ItemMediaType.VIDEO
                    else -> null
                }

                id.stringValue?.let { id ->
                    PostItemModel(
                        id = id,
                        caption = caption?.stringValue,
                        comments = comments?.arrayValue?.values?.mapNotNull { comment -> comment.stringValue },
                        mediaType = videoOrImage,
                        storageRef = storageRef?.stringValue,
                        authorUserName = username,
                        createdAt = createdAt?.timestampValue?.let { dateTime -> LocalDateTime.parse(dateTime) }
                    )
                }
            }
        }
    }
}