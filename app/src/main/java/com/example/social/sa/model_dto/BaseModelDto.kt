package com.example.social.sa.model_dto

interface BaseModelDto<T,F> {
    fun fromDtoToModel(dto:F):T
    fun fromModelToDto(model:T):F
}