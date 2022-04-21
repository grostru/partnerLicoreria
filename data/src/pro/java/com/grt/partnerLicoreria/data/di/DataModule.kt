package com.grt.partnerLicoreria.data.di

import com.grt.partnerLicoreria.data.firestore.FirestoreCategoryRepository
import com.grt.partnerLicoreria.data.firestore.FirestoreProductsRepository
import com.grt.partnerLicoreria.domain.repository.GetCategorysRepository
import com.grt.partnerLicoreria.domain.repository.GetProductsRepository
import org.koin.dsl.module

/**
 * Created por Gema Rosas Trujillo
 * 28/01/2022
 *
 * Modulo de Datos del inyector de Dependencias
 */
val dataModule = module {

    single { FirestoreCategoryRepository(get()) }

    single { FirestoreProductsRepository(get()) }

    single<GetCategorysRepository>() {
       get<FirestoreCategoryRepository>()
    }

    single<GetProductsRepository>() {
        get<FirestoreProductsRepository>()
    }

}