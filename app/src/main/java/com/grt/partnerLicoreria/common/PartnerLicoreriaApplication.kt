package com.grt.partnerLicoreria.common

import android.app.Application
import com.grt.partnerLicoreria.data.di.dataModule
import com.grt.partnerLicoreria.di.uiModule
import com.grt.partnerLicoreria.di.usecaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
/**
 * Created por Gema Rosas Trujillo
 * 25/02/2022
 *
 * Clase en la que inciamos Koin y el contexto de la Aplicación. Esta clase siempre ha de estar
 * definida en el manifest
 */
class PartnerLicoreriaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PartnerLicoreriaApplication)
            modules(
                uiModule,
                dataModule,
                usecaseModule
            )
        }
    }
}