package com.grt.partnerLicoreria.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.grt.partnerLicoreria.common.BaseFragment
import com.grt.partnerLicoreria.common.NavData
import com.grt.partnerLicoreria.databinding.FragmentProductBinding
import com.grt.partnerLicoreria.domain.model.CategoryModel
import com.grt.partnerLicoreria.domain.model.ProductModel
import com.grt.partnerLicoreria.ui.main.MainAux
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

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
        ProductAdapter(){
            // Capturamos la acción de pulsar en un elemento de la lista
            vm.onActionProductClicked(it)
        }
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
        when(navData.id){
            ProductViewModel.NAV_ADD_PRODUCT ->{
                var product = navData.data as ProductModel?
                findNavController().navigate(ProductFragmentDirections.actionNavProductToAddDialogFragment(categorySelected!!,product!!))
            }
        }
    }

    override fun getProductSelected(): ProductModel? = productSelected

    override fun getCategorySelected(): CategoryModel? = categorySelected
}