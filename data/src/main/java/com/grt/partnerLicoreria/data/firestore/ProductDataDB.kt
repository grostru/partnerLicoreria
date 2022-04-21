package com.grt.licoreriaelgato.data.firebase.firestore

import com.google.firebase.firestore.Exclude

/**
 * Created por Gema Rosas Trujillo
 * 25/03/2022
 * Clase de Datos
 */
data class ProductDataDB(@get:Exclude var id: String? = null,
                         var idCategory:String? = null,
                         var name:String? = null,
                         var description: String? = null,
                         var imgUrl: String? = null,
                         var quantity: Int = 0,
                         @get:Exclude var newQuantity: Int = 1,
                         var price: Double = 0.0,
                         var sellerId: String = ""){

    fun totalPrice(): Double = newQuantity * price

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductDataDB

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}
