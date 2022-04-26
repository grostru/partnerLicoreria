package com.cursosandroidant.nilopartner.fcm

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

/****
 * Project: Nilo Partner
 * From: com.cursosandroidant.nilopartner.fcm
 * Created by Alain Nicolás Tello on 17/06/21 at 8:44
 * All rights reserved 2021.
 *
 * All my Courses(Only on Udemy):
 * https://www.udemy.com/user/alain-nicolas-tello/
 ***/
class VolleyHelper(context: Context) {

    companion object{
        @Volatile
        private var INSTANCE: VolleyHelper? = null
        fun getInstance(context: Context) = INSTANCE ?: synchronized(this){
            INSTANCE ?: VolleyHelper(context).also { INSTANCE = it }
        }
    }

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    fun <T> addToRequestQueue(req: Request<T>){
        requestQueue.add(req)
    }
}