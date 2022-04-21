package com.grt.partnerLicoreria.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.grt.partnerLicoreria.data.Constants
import com.grt.partnerLicoreria.common.BaseFragment
import com.grt.partnerLicoreria.databinding.FragmentOrderBinding
import com.grt.partnerLicoreria.domain.model.OrderModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Created por Gema Rosas Trujillo
 * 04/04/2022
 */
class OrderFragment : BaseFragment<FragmentOrderBinding, OrderViewModel>(), OnOrderListener {

    override val vm: OrderViewModel by sharedViewModel()

    private lateinit var adapter: OrderAdaper

    private lateinit var orderSelected: OrderModel

    override fun provideBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentOrderBinding {
        return FragmentOrderBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupFirestore()
    }

    private fun setupRecyclerView() {
        adapter = OrderAdaper(mutableListOf(), this)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@OrderFragment.adapter
        }
    }

    private fun setupFirestore(){
        FirebaseAuth.getInstance().currentUser?.let {  user ->
            val db = FirebaseFirestore.getInstance()

            db.collection(Constants.COLL_REQUESTS)
                .whereEqualTo(Constants.PROP_CLIENT_ID, user.uid)
                //.orderBy(Constants.PROP_DATE, Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener {
                    for (document in it){
                        val order = document.toObject(OrderModel::class.java)
                        order.id = document.id
                        adapter.add(order)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error al consultar los datos.", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }

    override fun onTrack(order: OrderModel) {
        orderSelected = order

        findNavController().navigate(OrderFragmentDirections.actionNavOrderToTrackFragment(orderSelected))
    }

    override fun onStartChat(order: OrderModel) {
        orderSelected = order

        findNavController().navigate(OrderFragmentDirections.actionNavOrderToChatFragment(orderSelected))
    }


}