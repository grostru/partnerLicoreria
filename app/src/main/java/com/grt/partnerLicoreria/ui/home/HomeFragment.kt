package com.grt.partnerLicoreria.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.grt.partnerLicoreria.common.BaseFragment
import com.grt.partnerLicoreria.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Created por Gema Rosas Trujillo
 * 24/03/2022
 */
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val vm: HomeViewModel by sharedViewModel()

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    override fun provideBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}