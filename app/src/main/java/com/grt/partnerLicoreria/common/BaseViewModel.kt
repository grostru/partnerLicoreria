package com.grt.partnerLicoreria.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grt.partnerLicoreria.ui.dialog.DialogData
import com.grt.partnerLicoreria.ui.main.MainViewModel
import kotlinx.coroutines.launch
/**
 * Created por Gema Rosas Trujillo
 * 08/03/2022
 *
 * Clase abstracta común a todos los ViewModel en el que iniciamos todos los LiveData
 * comunes a todos los ViewModel a crear
 */
abstract class BaseViewModel : ViewModel() {

    protected val liveShowLoading = MutableLiveData<Boolean>()
    val obsShowLoading: LiveData<Boolean> = liveShowLoading

    protected val liveShowDialog = MutableLiveData<DialogData>()
    val obsShowDialog:LiveData<DialogData> = liveShowDialog

    protected val liveNavigation = SingleLiveEvent<NavData?>()
    val obsNavigate = liveNavigation

    protected lateinit var mainViewModel: MainViewModel

    fun attachMainViewModel(mainViewModel: MainViewModel){
        this.mainViewModel = mainViewModel
    }

    protected fun navigate(navData: NavData){
        liveNavigation.value = navData
    }

    protected fun showLoading(){
        liveShowLoading.value = true
    }

    protected fun hideLoading(){
        liveShowLoading.value = false
    }

    open fun onActionMsgAcceptClicked() {
        liveShowDialog.value = DialogData(show = false)
    }

    protected fun executeUseCase(
        exceptionAction : (suspend ((Throwable)->Unit))?=null,
        finalAction : (suspend  ()->Unit)?=null,
        usecaseAction: suspend ()->Unit
    ){
        viewModelScope.launch {
            try {
                usecaseAction.invoke()
            }
            catch (e:Throwable){
                exceptionAction?.invoke(e)
            }
            finally {
                finalAction?.invoke()
            }
        }
    }

    fun onInit() {
        onInitialization()
    }

    abstract fun onInitialization()

}