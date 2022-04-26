package com.grt.partnerLicoreria.ui.product

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.grt.partnerLicoreria.R
import com.grt.partnerLicoreria.common.BaseFragment
import com.grt.partnerLicoreria.common.NavData
import com.grt.partnerLicoreria.data.Constants
import com.grt.partnerLicoreria.databinding.FragmentProductBinding
import com.grt.partnerLicoreria.domain.model.CategoryModel
import com.grt.partnerLicoreria.domain.model.ProductModel
import com.grt.partnerLicoreria.ui.main.MainAux
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.qualifier._q

/**
 * Created por Gema Rosas Trujillo
 * 24/03/2022
 */
class ProductFragment : BaseFragment<FragmentProductBinding, ProductViewModel>(), MainAux {

    override val vm: ProductViewModel by sharedViewModel()

    val args: ProductFragmentArgs by navArgs()

    private var productSelected: ProductModel? = null

    private var categorySelected: CategoryModel? = null

    private val productAdapter by lazy {
        ProductAdapter(
            emptyList(),
            ::onClick,
            ::onLongClick)
    }

    private lateinit var firestoreListener: ListenerRegistration
    private var queryPagination: Query? = null

    override fun provideBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentProductBinding {
        return FragmentProductBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categorySelected = args.category
        vm.onAttachProductsCategory(categorySelected!!)

        setupBinding()

        setupRecycler()

        setupButtons()

    }

    private fun setupBinding() {
        observeData(vm.obsListProducts,::onObserveList)
    }

    private fun onObserveList(list: List<ProductModel>) {
        productAdapter.updateList(list)
    }

    private fun setupRecycler(){

        with(binding){
            rvCategorys.layoutManager = GridLayoutManager(requireContext(), 2,
                GridLayoutManager.HORIZONTAL, false)
            rvCategorys.adapter = productAdapter

            llProgress.visibility = View.GONE
            nsvCategorys.visibility = View.VISIBLE
        }
    }

    private fun setupButtons(){
        binding.btnCreateProd.setOnClickListener {
            productSelected = null
            findNavController().navigate(ProductFragmentDirections.actionNavProductToAddDialogFragment(categorySelected!!, productSelected))
        }
    }

    override fun onNavigate(navData: NavData) {
        when(navData.id) {
            ProductViewModel.NAV_ADD_PRODUCT -> {
                var product = navData.data as ProductModel?
                findNavController().navigate(
                    ProductFragmentDirections.actionNavProductToAddDialogFragment(
                        categorySelected!!,
                        product!!
                    )
                )
            }
            ProductViewModel.NAV_DEL_PRODUCT -> {
                var product = navData.data as ProductModel?
                onLongClick(product)
            }
        }
    }

    fun onClick(product:ProductModel){
        findNavController().navigate(
            ProductFragmentDirections.actionNavProductToAddDialogFragment(
                categorySelected!!,
                product!!
            )
        )
    }

    fun onLongClick(product: ProductModel?) {
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.select_dialog_singlechoice)
        adapter.add("Eliminar")

        MaterialAlertDialogBuilder(requireContext())
            .setAdapter(adapter){ dialogInteface: DialogInterface, position: Int ->
                when(position){
                    0 -> confirmDeleteProduct(product!!)
                }
            }
            .show()
    }

    private fun confirmDeleteProduct(product: ProductModel){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.product_dialog_delete_title)
            .setMessage(R.string.product_dialog_delete_msg)
            .setPositiveButton(R.string.product_dialog_delete_confirm){_,_->
                product.id?.let { id ->
                    product.imgUrl?.let { url ->
                        try {
                            val photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(url)
                            //FirebaseStorage.getInstance().reference.child(Constants.PATH_PRODUCT_IMAGES).child(id)
                            photoRef
                                .delete()
                                .addOnSuccessListener {
                                    deleteProductFromFirestore(id)
                                }
                                .addOnFailureListener {
                                    if ((it as StorageException).errorCode ==
                                        StorageException.ERROR_OBJECT_NOT_FOUND){
                                        deleteProductFromFirestore(id)
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            "Error al eliminar foto.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                .addOnCompleteListener {
                                    vm.onAttachProductsCategory(categorySelected!!)
                                }
                        } catch (e:Exception){
                            e.printStackTrace()
                            deleteProductFromFirestore(id)
                        }
                    }
                }
            }
            .setNegativeButton(R.string.dialog_cancel, null)
            .show()
    }

    private fun deleteProductFromFirestore(productId:String){
        val db = FirebaseFirestore.getInstance()
        val productRef = db.collection(Constants.COLL_PRODUCTS)
        productRef.document(productId)
            .delete()
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al eliminar registro.", Toast.LENGTH_SHORT).show()
            }
            .addOnCompleteListener {
                vm.onAttachProductsCategory(categorySelected!!)
            }
    }

    override fun getProductSelected(): ProductModel? = productSelected

    override fun getCategorySelected(): CategoryModel? = categorySelected
}