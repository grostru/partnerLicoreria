package com.grt.partnerLicoreria.ui.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.grt.partnerLicoreria.common.BaseViewModel
import com.grt.partnerLicoreria.domain.model.ProductModel

/**
 * Created por Gema Rosas Trujillo
 * 24/03/2022
 */
class OrderViewModel : BaseViewModel() {

    private var liveListCartProducts : MutableLiveData<List<ProductModel>> = MutableLiveData(listOf())
    var obsListCartProducts : LiveData<List<ProductModel>> = liveListCartProducts

    private var liveTotalCart = MutableLiveData<Double>()
    var obsTotalCart = liveTotalCart

    override fun onInitialization() {
        liveTotalCart.value = 0.0
    }

    fun addProductToCart(product: ProductModel) {

        var listaProductos = obsListCartProducts.value?.toMutableList() as MutableList
        val index = listaProductos.indexOf(product)
        if (index != -1){
            listaProductos.set(index, product)
        } else {
            listaProductos?.add(product)
        }

        liveListCartProducts.value = listaProductos

        updateTotal()
    }

    fun updateTotal() {
        var total = 0.0
        obsListCartProducts.value?.toMutableList()?.forEach { product ->
            total += product.totalPrice()
        }

        liveTotalCart.value = total
    }
}