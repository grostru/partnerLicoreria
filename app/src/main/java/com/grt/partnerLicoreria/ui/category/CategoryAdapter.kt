package com.grt.partnerLicoreria.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.grt.partnerLicoreria.R
import com.grt.partnerLicoreria.databinding.ItemCategoryBinding
import com.grt.partnerLicoreria.domain.model.CategoryModel

/**
 * Created por Gema Rosas Trujillo
 * 24/03/2022
 */
class CategoryAdapter(private val categoryList:List<CategoryModel> = emptyList(),
                      private val listener: (CategoryModel) -> Unit)
    : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var mutableCategoryList : MutableList<CategoryModel> = mutableListOf(*categoryList.toTypedArray())

    inner class CategoryViewHolder(val binding : ItemCategoryBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(categoryModel: CategoryModel){
            val context = itemView.context
            with(binding){
                categoryModel.also {

                    /*if(it.id == null){
                        containerCategory.visibility = View.GONE
                        btnMore.visibility = View.VISIBLE
                    } else {
                        containerCategory.visibility = View.VISIBLE
                        btnMore.visibility = View.GONE
*/
                    mtvCategory.text = it.name

                    // Imagen de la Categoria
                    Glide.with(context)
                        .load(categoryModel.imgUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ic_access_time)
                        .error(R.drawable.ic_broken_image)
                        .centerCrop()
                        .into(ivCategory)

                }
            }
            itemView.setOnClickListener {
                listener(categoryModel)
            }
        }
    }

    fun updateList(list: List<CategoryModel>){
        mutableCategoryList.clear()
        mutableCategoryList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(mutableCategoryList[position])
    }

    override fun getItemCount(): Int {
        return mutableCategoryList.size
    }
}