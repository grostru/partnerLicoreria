package com.grt.partnerLicoreria.ui.chat

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.grt.partnerLicoreria.R
import com.grt.partnerLicoreria.databinding.ItemChatBinding
import com.grt.partnerLicoreria.domain.model.MessageModel

class ChatAdapter(private val messageModelList: MutableList<MessageModel>, private val listener: OnChatListener)
    : RecyclerView.Adapter<ChatAdapter.ViewHolder>(){

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messageModelList[position]

        holder.setListener(message)

        var gravity = Gravity.END
        var background = ContextCompat.getDrawable(context, R.drawable.background_chat_client)
        var textColor = ContextCompat.getColor(context, R.color.colorOnSecondary)

        val marginHorizontal = context.resources.getDimensionPixelSize(R.dimen.chat_margin_horizontal)
        val params = holder.binding.tvMessage.layoutParams as ViewGroup.MarginLayoutParams
        params.marginStart = marginHorizontal
        params.marginEnd = 0
        params.topMargin = 0

        if (position > 0 && message.isSentByMe() != messageModelList[position - 1].isSentByMe()){
            params.topMargin = context.resources.getDimensionPixelSize(R.dimen.common_padding_min)
        }

        if (!message.isSentByMe()){
            gravity = Gravity.START
            background = ContextCompat.getDrawable(context, R.drawable.background_chat_support)
            textColor = ContextCompat.getColor(context, R.color.colorOnPrimary)
            params.marginStart = 0
            params.marginEnd = marginHorizontal
        }

        holder.binding.root.gravity = gravity

        holder.binding.tvMessage.layoutParams = params
        holder.binding.tvMessage.setBackground(background)
        holder.binding.tvMessage.setTextColor(textColor)
        holder.binding.tvMessage.text = message.message
    }

    override fun getItemCount(): Int = messageModelList.size

    fun add(messageModel: MessageModel){
        if (!messageModelList.contains(messageModel)){
            messageModelList.add(messageModel)
            notifyItemInserted(messageModelList.size - 1)
        }
    }

    fun update(messageModel: MessageModel){
        val index = messageModelList.indexOf(messageModel)
        if (index != -1){
            messageModelList.set(index, messageModel)
            notifyItemChanged(index)
        }
    }

    fun delete(messageModel: MessageModel){
        val index = messageModelList.indexOf(messageModel)
        if (index != -1){
            messageModelList.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding = ItemChatBinding.bind(view)

        fun setListener(messageModel: MessageModel){
            binding.tvMessage.setOnLongClickListener {
                listener.deleteMessage(messageModel)
                true
            }
        }
    }
}