package com.grt.partnerLicoreria.data.firestore

import com.grt.partnerLicoreria.domain.model.CategoryModel

/**
 * Created por Gema Rosas Trujillo
 * 24/03/2022
 * Interface en el que se define los accesos a datos y se especifican las búsquedas u operaciones
 * a realizar en bbdd
 */
data class CategoryDataDB(
    var id: String? = null,
    var name:String? = null,
    var imgUrl: String? = null
)

fun CategoryDataDB.toDomain() : CategoryModel{
    return CategoryModel(id,name, imgUrl)
}