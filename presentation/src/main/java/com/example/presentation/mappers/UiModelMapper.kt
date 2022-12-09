package com.example.presentation.mappers

import com.example.presentation.models.UiModel

/**
 * maps between [UiModel] to another model [R]
 */
interface UiModelMapper<R, U : UiModel> {
    fun mapFromUiModel(model: U): R
    fun mapToUiModel(model: R): U
}