package com.example.data.models

data class ListDocumentsRemoteResponseModel<T : RemoteModel>(
    val documents: List<T>
)
