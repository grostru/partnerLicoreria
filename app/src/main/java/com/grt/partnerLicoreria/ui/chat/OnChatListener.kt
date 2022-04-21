package com.grt.partnerLicoreria.ui.chat

import com.grt.partnerLicoreria.domain.model.MessageModel

interface OnChatListener {
    fun deleteMessage(messageModel: MessageModel)
}