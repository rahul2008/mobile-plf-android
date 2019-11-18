package com.philips.cdp.di.mec.screens.reviews

import java.text.SimpleDateFormat
import java.util.*

class MECReview(val title:String , val reviewText:String, val rating:String,val submitter:String,val date:Date, val pros:String,val cons:String) {


     fun getFormattedDate(): String? {
         val formateDate = SimpleDateFormat("dd MMM yyyy").format(date)
         return formateDate.toString()
         }
    }