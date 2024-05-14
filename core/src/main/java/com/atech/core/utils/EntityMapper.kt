package com.atech.core.utils

interface EntityMapper<A, B> {
    fun mapFromEntity(entity: A): B
    fun mapToEntity(domainModel: B): A
}