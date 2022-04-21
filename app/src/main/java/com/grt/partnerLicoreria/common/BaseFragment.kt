package com.grt.partnerLicoreria.common

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.viewbinding.ViewBinding
import com.grt.partnerLicoreria.ui.dialog.DialogData
import com.grt.partnerLicoreria.ui.dialog.DialogFragment
import com.grt.partnerLicoreria.ui.home.HomeFragment
import com.grt.partnerLicoreria.ui.main.MainActivity
import com.grt.partnerLicoreria.ui.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Created por Gema Rosas Trujillo
 * 25/02/2022
 *
 * Clase abstracta común a todos los fragmentos, en el que iniciamos los fragmentos y observamos
 * los valores comunes a todos ellos
 */
abstract class BaseFragment<T:ViewBinding,VM: BaseViewModel> : Fragment() {

    private val dialogMsg by lazy { DialogFragment.newInstance() }

    private var _binding  : T? = null

    protected val binding
        get() =  _binding!!

    protected abstract val vm:VM

    private val mainVm: MainViewModel by sharedViewModel()

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = provideBinding(inflater,container)
        return binding.root
    }


    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        vm.attachMainViewModel(mainVm)
        vm.onInit()
        observeData(vm.obsShowLoading,::onObserveLoading)
        observeData(vm.obsShowDialog,::onObserveDialogData)
        observeData(vm.obsNavigate,::onObserveNavigation)

    }

    protected open fun onNavigate(navData: NavData){}

    protected open fun onObserveNavigation(navData: NavData?) {
        navData?.also {
            onNavigate(it)
        }
    }

    private fun onObserveLoading(show: Boolean) {
            (requireActivity() as? MainActivity)?.also {
                if(show)
                    it.showLoading()
                else
                    it.hideLoading()
            }
    }

    abstract fun provideBinding(inflater: LayoutInflater, container: ViewGroup?):T

    private fun onObserveMessage(message: String) {
        Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
    }

    private fun onObserveDialogData(dialogData: DialogData) {
        if(dialogData.show){
            dialogMsg.show(parentFragmentManager, HomeFragment::class.java.name,dialogData.description){
                vm.onActionMsgAcceptClicked()
            }
        }
        else{
            dialogMsg.dismiss(parentFragmentManager)
        }
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun <LD>observeData(liveData: LiveData<LD>,action:(LD)->Unit){
        liveData.observe(viewLifecycleOwner){
            action.invoke(it)
        }
    }

    protected fun hideKeyboard(myView: View){
        val imm: InputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(myView.getWindowToken(), 0)
    }

}