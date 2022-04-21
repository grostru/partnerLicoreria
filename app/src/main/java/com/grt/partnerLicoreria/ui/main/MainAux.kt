package com.grt.partnerLicoreria.ui.main

import com.grt.partnerLicoreria.domain.model.CategoryModel
import com.grt.partnerLicoreria.domain.model.ProductModel

interface MainAux {
    fun getProductSelected(): ProductModel?
    fun getCategorySelected(): CategoryModel?
}