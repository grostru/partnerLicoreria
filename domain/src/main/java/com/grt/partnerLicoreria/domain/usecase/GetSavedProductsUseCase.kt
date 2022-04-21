package com.grt.partnerLicoreria.domain.usecase

import com.grt.partnerLicoreria.domain.model.CategoryModel
import com.grt.partnerLicoreria.domain.model.ProductModel
import com.grt.partnerLicoreria.domain.repository.GetProductsRepository

/**
 * Created por Gema Rosas Trujillo
 * 25/03/2022
 * Caso de uso que obtiene del repositorio la lista de productos o en su defecto una lista vacía
 */
class GetSavedProductsUseCase(
    private val productRepository: GetProductsRepository
): UseCase<CategoryModel, List<ProductModel>>() {

    override suspend fun executeUseCase(categoryModel: CategoryModel): List<ProductModel> {
        return productRepository.getProducts(categoryModel).getOrDefault(emptyList())
    }
}