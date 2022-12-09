package com.example.presentation.mappers

import com.example.presentation.models.PostUiModel
import com.example.use_cases.models.PostItemModel

class PostsMapper :
    UiModelMapper<PostItemModel, PostUiModel> {
        override fun mapFromUiModel(model: PostUiModel): PostItemModel {
            return with(model) {
                PostItemModel(
                    id = id,
                    caption = caption,
                    comments = comments,
                    createdAt = createdAt,
                    mediaType = mediaType,
                    storageRef = storageRef,
                    authorUserName = authorUserName
                )
            }
        }

        override fun mapToUiModel(model: PostItemModel): PostUiModel {
            return with(model) {
                PostUiModel(
                    id = id,
                    caption = caption,
                    comments = comments,
                    createdAt = createdAt,
                    mediaType = mediaType,
                    storageRef = storageRef,
                    authorUserName = authorUserName
                )
            }
        }
    }