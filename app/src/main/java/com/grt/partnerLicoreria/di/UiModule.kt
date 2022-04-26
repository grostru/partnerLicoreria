package com.grt.partnerLicoreria.di

import com.grt.partnerLicoreria.ui.category.CategoryViewModel
import com.grt.partnerLicoreria.ui.chat.ChatViewModel
import com.grt.partnerLicoreria.ui.home.HomeViewModel
import com.grt.partnerLicoreria.ui.licoreria.LicoreriaViewModel
import com.grt.partnerLicoreria.ui.main.MainViewModel
import com.grt.partnerLicoreria.ui.order.OrderViewModel
import com.grt.partnerLicoreria.ui.product.ProductViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
/**
 * Created por Gema Rosas Trujillo
 * 25/02/2022
 *
 * Modulo en el que iniciamos todos los ViewModel que vamos a usar como Injección de dependencias
 */
val uiModule = module {

    viewModel {
        MainViewModel()
    }

    viewModel {
        HomeViewModel()
    }

    viewModel {
        LicoreriaViewModel()
    }

    viewModel {
        CategoryViewModel(get())
    }

    viewModel {
        ProductViewModel(get())
    }

    viewModel {
        OrderViewModel()
    }

    viewModel {
        ChatViewModel()
    }
}