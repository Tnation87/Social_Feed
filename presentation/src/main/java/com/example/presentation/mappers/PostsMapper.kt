package com.example.presentation.mappers

import com.example.presentation.models.PostUiModel
import com.example.presentation.models.UiMediaType
import com.example.use_cases.models.ItemMediaType
import com.example.use_cases.models.PostItemModel

class PostsMapper :
    UiModelMapper<PostItemModel, PostUiModel> {
        override fun mapFromUiModel(model: PostUiModel): PostItemModel {
            return with(model) {
                val videoOrImage = when (mediaType) {
                    UiMediaType.IMAGE -> ItemMediaType.IMAGE
                    UiMediaType.VIDEO -> ItemMediaType.VIDEO
                    null -> null
                }

                PostItemModel(
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

        override fun mapToUiModel(model: PostItemModel): PostUiModel {
            return with(model) {
                val videoOrImage = when (mediaType) {
                    ItemMediaType.IMAGE -> UiMediaType.IMAGE
                    ItemMediaType.VIDEO -> UiMediaType.VIDEO
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