package com.grt.partnerLicoreria.ui.track

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.FirebaseFirestore
import com.grt.partnerLicoreria.R
import com.grt.partnerLicoreria.common.BaseFragment
import com.grt.partnerLicoreria.data.Constants
import com.grt.partnerLicoreria.databinding.FragmentTrackBinding
import com.grt.partnerLicoreria.domain.model.OrderModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Created por Gema Rosas Trujillo
 * 04/04/2022
 */
class TrackFragment : BaseFragment<FragmentTrackBinding, TrackViewModel>() {

    override val vm: TrackViewModel by sharedViewModel()

    val args: TrackFragmentArgs by navArgs()

    override fun provideBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentTrackBinding {
        return FragmentTrackBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val orderSelected = args.orderSelected

        setupOrder(orderSelected)
    }

    private fun setupOrder(order: OrderModel) {
        order?.let {
            updateUI(it)

            getOrderInRealtime(it.id)

            setupActionBar()
            //configAnalytics()
        }

    }

    private fun updateUI(order: OrderModel) {
        binding?.let {
            it.progressBar.progress = order.status * (100/3) - 15

            it.cbOrdered.isChecked = order.status > 0
            it.cbPreparing.isChecked = order.status > 1
            it.cbSent.isChecked = order.status > 2
            it.cbDelivered.isChecked = order.status > 3
        }
    }

    private fun getOrderInRealtime(orderId: String){
        val db = FirebaseFirestore.getInstance()

        val orderRef = db.collection(Constants.COLL_REQUESTS).document(orderId)
        orderRef.addSnapshotListener { snapshot, error ->
            if (error != null){
                Toast.makeText(activity, "Error al consultar esta orden.", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()){
                val order = snapshot.toObject(OrderModel::class.java)
                order?.let {
                    it.id = snapshot.id

                    updateUI(it)
                }
            }
        }
    }

    private fun setupActionBar(){
        (activity as? AppCompatActivity)?.let {
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            it.supportActionBar?.title = getString(R.string.track_title)
            setHasOptionsMenu(true)
        }
    }
}