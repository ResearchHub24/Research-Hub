package com.atech.core.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun <T,R> networkAccessBond(
    networkCall: suspend () -> T,
    dbCall: suspend () -> R,
    saveCall: suspend (T) -> Unit,
    mapper: (R) -> T,
    canFetch: Boolean = true
) :Flow<T> {
    return flow {
        emit(mapper(dbCall()))
        if(canFetch) {
            val networkData = networkCall()
            saveCall(networkData)
            emit(mapper(dbCall()))
        }
    }
}