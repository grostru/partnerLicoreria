package com.grt.partnerLicoreria.domain.model

data class ProductOrderModel(var id: String = "",
                             var name: String = "",
                             var quantity: Int = 0){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductOrderModel

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
