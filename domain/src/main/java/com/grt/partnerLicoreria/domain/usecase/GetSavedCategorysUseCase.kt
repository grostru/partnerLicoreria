package com.grt.partnerLicoreria.domain.usecase

import com.grt.partnerLicoreria.domain.model.CategoryModel
import com.grt.partnerLicoreria.domain.repository.GetCategorysRepository

/**
 * Created por Gema Rosas Trujillo
 * 25/03/2022
 * Caso de uso que obtiene del repositorio la lista de categorias o en su defecto una lista vacía
 */
class GetSavedCategorysUseCase(
    private val categoryRepository: GetCategorysRepository
): UseCase<Unit, List<CategoryModel>>() {

    override suspend fun executeUseCase(input: Unit):List<CategoryModel> {
        return categoryRepository.getCategorys().getOrDefault(emptyList())
    }
}