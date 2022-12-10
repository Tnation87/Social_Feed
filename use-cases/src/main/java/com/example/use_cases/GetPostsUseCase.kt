package com.example.use_cases

import com.example.data.errors.ResponseError
import com.example.repos.feed.PostsRepo
import com.example.repos.models.PostItemModel
import com.example.use_cases.models.MediaType
import com.example.use_cases.models.PostModel
import dagger.Reusable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@Reusable
class GetPostsUseCase @Inject constructor(private val postsRepo: PostsRepo) {
    suspend operator fun invoke(
    ): List<PostModel> {
        return try {
            val response = postsRepo.getPosts()
            val users = response.users
            val posts = response.posts
            posts.mapNotNull {
                val username =
                    users.mapNotNull { user ->
                        user?.path?.replace(
                            "projects/peach-assessment/databases/(default)/documents/users/",
                            ""
                        ) to user?.userFields?.username
                    }
                        .firstOrNull { (id, _) ->
                            id == it?.postFields?.authorID?.stringValue
                        }?.second?.stringValue
                mapToItem(username, it)
            }
        } catch (e: ResponseError) {
            emptyList()
        }
    }

    private fun mapToItem(username: String?, itemModel: PostItemModel?): PostModel? {
        return itemModel?.postFields?.let {
            with(it) {
                val videoOrImage = when (mediaType?.stringValue) {
                    "photo" -> MediaType.IMAGE
                    "video" -> MediaType.VIDEO
                    else -> null
                }

                id.stringValue?.let { id ->
                    PostModel(
                        id = id,
                        caption = caption?.stringValue,
                        comments = comments?.arrayValue?.values?.mapNotNull { comment -> comment.stringValue },
                        mediaType = videoOrImage,
                        storageRef = storageRef,
                        authorUserName = username,
                        createdAt = createdAt?.timestampValue?.let { dateTime ->
                            LocalDateTime.parse(
                                dateTime,
                                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
                            )
                        }
                    )
                }
            }
        }
    }
}