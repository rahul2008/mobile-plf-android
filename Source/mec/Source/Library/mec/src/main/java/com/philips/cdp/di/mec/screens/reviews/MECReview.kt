package com.philips.cdp.di.mec.screens.reviews

class MECReview(val title:String , val reviewText:String, val rating:String,val date:String, val pros:String,val cons:String) {


    fun getFormattedDate():String{
        return "01/12/2019"
    }
}