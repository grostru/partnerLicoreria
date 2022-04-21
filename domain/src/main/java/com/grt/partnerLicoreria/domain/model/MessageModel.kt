package com.grt.partnerLicoreria.domain.model

data class MessageModel(var id: String = "",
                        var message: String = "",
                        var sender: String = "",
                        var myUid: String = ""){

    fun isSentByMe(): Boolean = sender.equals(myUid)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MessageModel

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
