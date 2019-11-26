package com.philips.cdp.di.mec.screens.retailers


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.screens.MecBaseFragment

class WebBuyFromRetailersFragment : MecBaseFragment(){

override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val group = inflater.inflate(R.layout.mec_web_fragment, container, false) as ViewGroup
    return group
}

}
