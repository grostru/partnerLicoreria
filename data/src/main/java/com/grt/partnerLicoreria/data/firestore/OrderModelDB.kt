package com.grt.partnerLicoreria.data.firestore

import com.google.firebase.database.Exclude
import com.grt.partnerLicoreria.domain.model.ProductOrderModel
import java.sql.Date

data class OrderModelDB(
    @get:Exclude var id: String = "",
    var clientId: String = "",
    var products: Map<String, ProductOrderModel> = hashMapOf(),
    var totalPrice: Double = 0.0,
    var status: Int = 0,
    var date: Date? = null,
){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderModelDB

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
