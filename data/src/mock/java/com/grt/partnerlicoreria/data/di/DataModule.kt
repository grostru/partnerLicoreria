package com.grt.pokemon.data.di

import com.grt.pokemon.data.remote.MockLocalRepository
import com.grt.pokemon.data.remote.MockRemoteRepository
import com.grt.pokemon.data.repository.DataStoreProfileRepository
import com.grt.pokemon.domain.repository.EditPokemonsRepository
import com.grt.pokemon.domain.repository.GetPokemonsRepository
import com.grt.pokemon.domain.repository.ProfileRepository
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

val dataModule = module {

    single { MockLocalRepository(get()) }

    single<GetPokemonsRepository>(qualifier = qualifier(InjectionQualifiers.DB)) {
        get<MockLocalRepository>()
    }

    single<GetPokemonsRepository>(qualifier = qualifier(InjectionQualifiers.REMOTE)) {
        MockRemoteRepository()
    }

    single<EditPokemonsRepository> {
        get<MockLocalRepository>()
    }

    single<ProfileRepository> {
        DataStoreProfileRepository(get())
    }
}