package com.example.presentation.mappers

import com.example.presentation.models.FilerFeedValueUiModel
import com.example.use_cases.models.FilterFeedValue

class FilterFeedValueMapper :
    UiModelMapper<FilterFeedValue, FilerFeedValueUiModel> {
    override fun mapFromUiModel(model: FilerFeedValueUiModel): FilterFeedValue {
        return when (model) {
            FilerFeedValueUiModel.VIDEOS -> FilterFeedValue.VIDEOS
            FilerFeedValueUiModel.IMAGES -> FilterFeedValue.IMAGES
            FilerFeedValueUiModel.NONE -> FilterFeedValue.NONE
        }
    }

    override fun mapToUiModel(model: FilterFeedValue): FilerFeedValueUiModel {
        return when (model) {
            FilterFeedValue.VIDEOS -> FilerFeedValueUiModel.VIDEOS
            FilterFeedValue.IMAGES -> FilerFeedValueUiModel.IMAGES
            FilterFeedValue.NONE -> FilerFeedValueUiModel.NONE
        }
    }
}