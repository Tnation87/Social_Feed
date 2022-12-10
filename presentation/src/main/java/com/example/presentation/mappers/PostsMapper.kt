package com.example.presentation.mappers

import com.example.presentation.models.PostUiModel
import com.example.presentation.models.UiMediaType
import com.example.use_cases.models.MediaType
import com.example.use_cases.models.PostModel

class PostsMapper :
    UiModelMapper<PostModel, PostUiModel> {
        override fun mapFromUiModel(model: PostUiModel): PostModel {
            return with(model) {
                val videoOrImage = when (mediaType) {
                    UiMediaType.IMAGE -> MediaType.IMAGE
                    UiMediaType.VIDEO -> MediaType.VIDEO
                    null -> null
                }

                PostModel(
                    id = id,
                    caption = caption,
                    comments = comments,
                    createdAt = createdAt,
                    mediaType = videoOrImage,
                    storageRef = storageRef,
                    authorUserName = authorUserName
                )
            }
        }

        override fun mapToUiModel(model: PostModel): PostUiModel {
            return with(model) {
                val videoOrImage = when (mediaType) {
                    MediaType.IMAGE -> UiMediaType.IMAGE
                    MediaType.VIDEO -> UiMediaType.VIDEO
                    null -> null
                }

                PostUiModel(
                    id = id,
                    caption = caption,
                    comments = comments,
                    createdAt = createdAt,
                    mediaType = videoOrImage,
                    storageRef = storageRef,
                    authorUserName = authorUserName
                )
            }
        }
    }