package com.grt.partnerLicoreria.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.grt.partnerLicoreria.R
import com.grt.partnerLicoreria.data.Constants
import com.grt.partnerLicoreria.common.BaseFragment
import com.grt.partnerLicoreria.databinding.FragmentOrderBinding
import com.grt.partnerLicoreria.domain.model.OrderModel
import com.grt.partnerLicoreria.ui.chat.ChatFragment
import com.grt.partnerLicoreria.ui.fcm.NotificationRS
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Created por Gema Rosas Trujillo
 * 04/04/2022
 */
class OrderFragment : BaseFragment<FragmentOrderBinding, OrderViewModel>(), OnOrderListener {

    override val vm: OrderViewModel by sharedViewModel()

    private lateinit var adapter: OrderAdaper

    private lateinit var orderSelected: OrderModel

    private val aValues: Array<String> by lazy {
        resources.getStringArray(R.array.status_value)
    }

    private val aKeys: Array<Int> by lazy {
        resources.getIntArray(R.array.status_key).toTypedArray()
    }

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

            val db = FirebaseFirestore.getInstance()

            db.collection(Constants.COLL_REQUESTS)
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

    override fun onStartChat(order: OrderModel) {
        orderSelected = order

        findNavController().navigate(OrderFragmentDirections.actionNavOrderToChatFragment(orderSelected))
    }

    override fun onStatusChange(order: OrderModel) {
        val db = FirebaseFirestore.getInstance()
        db.collection(Constants.COLL_REQUESTS)
            .document(order.id)
            .update(Constants.PROP_STATUS, order.status)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Orden actualizada.", Toast.LENGTH_SHORT).show()
                notifyClient(order)
                //Analytics
                /*firebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_SHIPPING_INFO){
                    val products = mutableListOf<Bundle>()
                    order.products.forEach {
                        val bundle = Bundle()
                        bundle.putString("id_product", it.key)
                        products.add(bundle)
                    }
                    param(FirebaseAnalytics.Param.SHIPPING, products.toTypedArray())
                    param(FirebaseAnalytics.Param.PRICE, order.totalPrice)
                }*/
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al actualizar orden.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun notifyClient(order: OrderModel){
        val db = FirebaseFirestore.getInstance()

        db.collection(Constants.COLL_USERS)
            .document(order.clientId)
            .collection(Constants.COLL_TOKENS)
            .get()
            .addOnSuccessListener {
                var tokensStr = ""
                for (document in it){
                    val tokenMap = document.data
                    tokensStr += "${tokenMap.getValue(Constants.PROP_TOKEN)},"
                }
                if (tokensStr.length > 0) {
                    tokensStr = tokensStr.dropLast(1)

                    var names = ""
                    order.products.forEach {
                        names += "${it.value.name}, "
                    }
                    names = names.dropLast(2)

                    val index = aKeys.indexOf(order.status)

                    val notificationRS = NotificationRS()
                    notificationRS.sendNotification(
                        "Tu pedido ha sido ${aValues[index]}",
                        names, tokensStr
                    )
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al consultar los datos.", Toast.LENGTH_SHORT)
                    .show()
            }
    }

}