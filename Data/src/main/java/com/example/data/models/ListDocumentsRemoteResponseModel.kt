package com.example.data.models

data class ListDocumentsRemoteResponseModel(val documents :List<DataModel>,
                                            val nextPageToken : String?)
