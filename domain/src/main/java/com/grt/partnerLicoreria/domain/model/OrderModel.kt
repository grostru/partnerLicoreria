package com.grt.partnerLicoreria.domain.model

import java.io.Serializable
import java.sql.Timestamp

data class OrderModel(
    var id: String = "",
    var clientId: String = "",
    var products: Map<String, ProductOrderModel> = hashMapOf(),
    var totalPrice: Double = 0.0,
    var status: Int = 0,
    var date: Timestamp? = null
): Serializable
{

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderModel

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
