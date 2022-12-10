package com.example.repos.models

import com.example.data.models.StringValue

data class UserItemModel(
    val path: String?,
    val userFields: UserFields?
) : ItemModel

data class UserFields(
    val username: StringValue?
)
