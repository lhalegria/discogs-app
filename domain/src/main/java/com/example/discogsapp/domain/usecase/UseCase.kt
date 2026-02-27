package com.example.discogsapp.domain.usecase

import kotlinx.coroutines.flow.Flow

interface UseCase<in P, out R> {
    operator fun invoke(param: P): Flow<R>
}
