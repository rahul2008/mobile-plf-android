package com.philips.cdp.di.mec.screens.address

import com.philips.cdp.di.mec.R

enum class SalutationEnum(private val requestString:String, private val stringID:Int) {

    MR("Mr.", R.string.mec_mr),
    MS("Ms.",R.string.mec_mrs);

    fun getStringID(lRequestString:String):Int{

        for (value in values()){

           if(lRequestString.equals(value.requestString,true)){
               return value.stringID
           }
        }
        return MR.stringID
    }

    fun getRequestString(lStringID:Int):Int{

        for (value in values()){
            if(lStringID == value.stringID){
                return value.stringID
            }
        }
        return MR.stringID
    }
}