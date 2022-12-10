package com.example.data.models

data class ArrayValue(val arrayValue: Values?)

data class Values(
    val values: List<StringValue>?
)