package com.philips.cdp.di.ecs.model.order

import com.philips.cdp.di.ecs.model.orders.ECSOrderDetail

class ECSOrders {

    val code: String? = null
    val guid: String? = null
    val placed: String? = null
    val status: String? = null
    val statusDisplay: String? = null

    val total: Total? = null

    lateinit var orderDetail: ECSOrderDetail


}
