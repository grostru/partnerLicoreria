package com.grt.partnerLicoreria.ui.licoreria

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.grt.partnerLicoreria.common.BaseFragment
import com.grt.partnerLicoreria.databinding.FragmentLicoreriaBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Created por Gema Rosas Trujillo
 * 24/03/2022
 */
class LicoreriaFragment : BaseFragment<FragmentLicoreriaBinding, LicoreriaViewModel>() {

    override val vm: LicoreriaViewModel by sharedViewModel()

    override fun provideBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentLicoreriaBinding {
        return FragmentLicoreriaBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}