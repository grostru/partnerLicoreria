package com.grt.partnerLicoreria.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.grt.partnerLicoreria.common.BaseViewModel
import com.grt.partnerLicoreria.common.NavData
import com.grt.partnerLicoreria.domain.model.CategoryModel
import com.grt.partnerLicoreria.domain.model.ProductModel
import com.grt.partnerLicoreria.domain.usecase.GetSavedProductsUseCase

/**
 * Created por Gema Rosas Trujillo
 * 24/03/2022
 */
class ProductViewModel(
    private val getSavedProductsUseCase: GetSavedProductsUseCase
) : BaseViewModel() {

    companion object{
        const val NAV_ADD_PRODUCT = 0
        const val NAV_DEL_PRODUCT = 1
    }

    private val liveListProducts: MutableLiveData<List<ProductModel>> = MutableLiveData()
    val obsListProducts: LiveData<List<ProductModel>> = liveListProducts

    // Función que obtiene la lista de Pokemons de BBDD para su posterior pintado
    fun onAttachProductsCategory(categoryModel: CategoryModel) {
        executeUseCase {
            liveListProducts.value = getSavedProductsUseCase.execute(categoryModel)
        }
    }

    // Función encargada del manejo de haber hecho click en uno de los elementos de la lista de
    // Productos
    fun onActionProductClicked(productModel: ProductModel, action:Int) {
        navigate(NavData(NAV_ADD_PRODUCT, productModel))
    }

    fun onActionProductLongClicked(productModel: ProductModel, action:Int) {
        navigate(NavData(NAV_DEL_PRODUCT, productModel))
    }

    override fun onInitialization() {}
}