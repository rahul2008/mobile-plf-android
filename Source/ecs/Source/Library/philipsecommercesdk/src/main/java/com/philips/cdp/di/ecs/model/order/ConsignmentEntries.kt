package com.philips.cdp.di.ecs.model.order

class ConsignmentEntries {
    val entryNumber: Int = 0

    val quantity: Int = 0

    var orderEntry: OrderEntry? = null

    val totalPrice: Cost? = null
    var trackAndTraceIDs: List<String>? = null
    var trackAndTraceUrls: List<String>? = null
}
