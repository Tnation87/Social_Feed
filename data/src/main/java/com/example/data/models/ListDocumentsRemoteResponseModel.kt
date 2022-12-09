package com.example.data.models

data class ListDocumentsRemoteResponseModel<T : DataModel>(
    val documents: List<T>,
    val nextPageToken: String?
)
