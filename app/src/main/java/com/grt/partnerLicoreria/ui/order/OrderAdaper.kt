package com.grt.partnerLicoreria.ui.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.grt.partnerLicoreria.R
import com.grt.partnerLicoreria.databinding.ItemOrderBinding
import com.grt.partnerLicoreria.domain.model.OrderModel

class OrderAdaper(private val orderList: MutableList<OrderModel>, private val listener: OnOrderListener) :
    RecyclerView.Adapter<OrderAdaper.ViewHolder>() {

    private lateinit var context: Context

    private val aValues: Array<String> by lazy {
        context.resources.getStringArray(R.array.status_value)
    }

    private val aKeys: Array<Int> by lazy {
        context.resources.getIntArray(R.array.status_key).toTypedArray()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orderList[position]

        holder.setListener(order)

        holder.binding.tvClientName.text = order.clientName
        holder.binding.tvId.text = context.getString(R.string.order_id, order.id)

        var names = ""
        order.products.forEach{
            names += "${it.value.name}, "
        }
        holder.binding.tvProductNames.text = names.dropLast(2)

        holder.binding.tvTotalPrice.text = context.getString(R.string.order_total_price, order.totalPrice)

        val index = aKeys.indexOf(order.status)
        val statusAdapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, aValues)
        holder.binding.actvStatus.setAdapter(statusAdapter)
        if (index != -1){
            holder.binding.actvStatus.setText(aValues[index], false)
        } else {
            holder.binding.actvStatus.setText(context.getText(R.string.order_status_unknown), false)
        }
    }

    override fun getItemCount(): Int = orderList.size

    fun add(order: OrderModel){
        orderList.add(order)
        notifyItemInserted(orderList.size - 1)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding = ItemOrderBinding.bind(view)

        fun setListener(order: OrderModel){
            binding.actvStatus.setOnItemClickListener { adapterView, view, position, id ->
                order.status = aKeys[position]
                listener.onStatusChange(order)
            }

            binding.chpChat.setOnClickListener {
                listener.onStartChat(order)
            }
        }
    }
}