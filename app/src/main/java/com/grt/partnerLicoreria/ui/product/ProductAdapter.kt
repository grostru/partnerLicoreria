package com.grt.partnerLicoreria.ui.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.grt.partnerLicoreria.R
import com.grt.partnerLicoreria.databinding.ItemProductBinding
import com.grt.partnerLicoreria.domain.model.ProductModel

/**
 * Created por Gema Rosas Trujillo
 * 24/03/2022
 */
class ProductAdapter(private val productList:List<ProductModel> = emptyList(),
                     private val listener: (ProductModel) -> Unit)
    : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var mutableProductList : MutableList<ProductModel> = mutableListOf(*productList.toTypedArray())

    inner class ProductViewHolder(val binding : ItemProductBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(productModel: ProductModel){
            val context = itemView.context
            with(binding){
                productModel.also {

                    tvName.text = it.name
                    tvPrice.text = it.price.toString()+"€"
                    tvQuantity.text = it.quantity.toString()

                    // Imagen de la Categoria
                    Glide.with(context)
                        .load(productModel.imgUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ic_access_time)
                        .error(R.drawable.ic_broken_image)
                        .centerCrop()
                        .into(ivCategory)

                }
            }
            itemView.setOnClickListener {
                listener(productModel)
            }
        }
    }

    fun updateList(list: List<ProductModel>){
        mutableProductList.clear()
        mutableProductList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(mutableProductList[position])
    }

    override fun getItemCount(): Int {
        return mutableProductList.size
    }
}