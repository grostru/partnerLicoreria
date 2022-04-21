package com.grt.partnerLicoreria.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

/**
 * Created por Gema Rosas Trujillo
 * 28/01/2022
 * Clase abstrata donde se definen las operaciones a realizar por los casos de uso
 */
abstract class UseCase<I, O> {

    protected abstract suspend fun executeUseCase(input: I): O

    suspend fun execute(input: I): O {
        return withContext(dispatcher) {
            executeUseCase(input)
        }
    }

    fun executeSyncInCurrentThread(input: I): O {
        return runBlocking {
            executeUseCase(input)
        }
    }

    open val dispatcher = Dispatchers.IO
}