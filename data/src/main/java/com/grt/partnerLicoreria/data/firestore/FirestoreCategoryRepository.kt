package com.grt.partnerLicoreria.data.firestore

import android.content.Context
import com.google.firebase.firestore.*
import com.grt.partnerLicoreria.data.Constants
import com.grt.partnerLicoreria.domain.model.CategoryModel
import com.grt.partnerLicoreria.domain.repository.GetCategorysRepository
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

/**
 * Created por Gema Rosas Trujillo
 * 25/03/2022
 *
 * Clase que implementa la conexión a la BBDD Firestore para obtener las Categorias
 */
class FirestoreCategoryRepository(private val context: Context):
    GetCategorysRepository {

    private val db = FirebaseFirestore.getInstance()
    private var listaCategorias = mutableListOf<CategoryModel>()

    override suspend fun getCategorys(): Result<List<CategoryModel>> {

        runBlocking {
            var de =  db.collection(Constants.COLL_CATEGORYS).get()
            var result = de.await()
            for (document in result){
                val category = getCategoryByDocument(document)

                add(category)
            }
        }

        return Result.success(listaCategorias.sortedBy {
            it.name
        })
    }

    fun add(category: CategoryModel){

            if (!listaCategorias.contains(category)){
                listaCategorias.add(listaCategorias.size, category)

            } else {
                update(category)
            }

    }

    fun update(category: CategoryModel){

            val index = listaCategorias.indexOf(category)
            if (index != -1){
                listaCategorias.set(index, category)
            }

    }

    fun getCategoryByDocument(document: QueryDocumentSnapshot):CategoryModel{
        var id = document.getString("id")
        var name = document.getString("name")
        var imgUrl = document.getString("imgUrl")

        return CategoryModel(id,name!!,imgUrl!!)
    }
}
