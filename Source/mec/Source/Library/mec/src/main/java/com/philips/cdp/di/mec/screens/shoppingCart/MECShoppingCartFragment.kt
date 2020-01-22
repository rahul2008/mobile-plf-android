package com.philips.cdp.di.mec.screens.shoppingCart


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.philips.cdp.di.mec.R

/**
 * A simple [Fragment] subclass.
 */
class MECShoppingCartFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.mecshopping_cart_fragment, container, false)
    }


}
