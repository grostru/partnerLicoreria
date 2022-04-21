package com.grt.partnerLicoreria.ui.order

import com.grt.partnerLicoreria.domain.model.OrderModel

/****
 * Project: Nilo
 * From: com.cursosandroidant.nilo.order
 * Created by Alain Nicolás Tello on 10/06/21 at 10:55
 * All rights reserved 2021.
 *
 * All my Courses(Only on Udemy):
 * https://www.udemy.com/user/alain-nicolas-tello/
 ***/
interface OnOrderListener {
    fun onTrack(order: OrderModel)
    fun onStartChat(order: OrderModel)
}