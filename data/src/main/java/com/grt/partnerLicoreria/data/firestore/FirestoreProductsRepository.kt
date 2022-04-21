package com.grt.partnerLicoreria.data.firestore

import android.content.Context
import com.google.firebase.firestore.*
import com.grt.partnerLicoreria.data.Constants
import com.grt.partnerLicoreria.domain.model.CategoryModel
import com.grt.partnerLicoreria.domain.model.ProductModel
import com.grt.partnerLicoreria.domain.repository.GetProductsRepository
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

/**
 * Created por Gema Rosas Trujillo
 * 29/03/2022
 *
 * Clase que implementa la conexión a la BBDD Firestore para obtener los Productos de cada Categoria
 */
class FirestoreProductsRepository(private val context: Context):
    GetProductsRepository {

    private val db = FirebaseFirestore.getInstance()
    private val categoryRef = db.collection(Constants.COLL_PRODUCTS)
    private lateinit var firestoreListener: ListenerRegistration
    private var listaProductos = mutableListOf<ProductModel>()

    override suspend fun getProducts(categoryModel: CategoryModel): Result<List<ProductModel>> {

        // Cada vez que obtenemos los productos de una determinada categoria, limpiamos la lista
        listaProductos.clear()

        runBlocking {
            var de =  db.collection(Constants.COLL_PRODUCTS).whereEqualTo("idCategory", categoryModel.id.toString()).get()
            var result = de.await()
            for (document in result){
                val product = getProductByDocument(document)

                add(product)
            }
        }

        return Result.success(listaProductos.sortedBy {
            it.name
        })
    }

    fun add(product: ProductModel){
            if (!listaProductos.contains(product)){
                listaProductos.add(listaProductos.size, product)

            } else {
                update(product)
            }
    }

    fun update(product: ProductModel){

            val index = listaProductos.indexOf(product)
            if (index != -1){
                listaProductos.set(index, product)
            }

    }

    fun getProductByDocument(document: QueryDocumentSnapshot):ProductModel{
        var id = document.getString("id")
        var idCategory = document.getString("idCategory")
        var name = document.getString("name")
        var description = document.getString("description")
        var imgUrl = document.getString("imgUrl")
        var quantity = document.getDouble("quantity")
        var price = document.getDouble("price")

        return ProductModel(id, idCategory, name, description, imgUrl, quantity?.toInt()?:0, 0, price?.toDouble()?:0.0,"")
    }
}
