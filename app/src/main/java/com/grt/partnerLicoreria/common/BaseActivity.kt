package com.grt.partnerLicoreria.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.viewbinding.ViewBinding

/**
 * Created por Gema Rosas Trujillo
 * 25/02/2022
 *
 * Clase abstracata creada con el contenido común de todas las actividades
 */
abstract class BaseActivity<T: ViewBinding>(val bindingInflater: (LayoutInflater)->T) : AppCompatActivity(){

    protected lateinit var binding :T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingInflater.invoke(layoutInflater)
        setContentView(binding.root)
    }

    protected fun <LD>observeData(liveData: LiveData<LD>, action:(LD)->Unit){
        liveData.observe(this){
            action.invoke(it)
        }
    }

    // Método común a las actividades, para ocultar el teclado
    protected fun hideKeyboard(){
        val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.applicationWindowToken, 0)
    }
}