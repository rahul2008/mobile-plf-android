package com.philips.cdp.di.mec.screens.shoppingCart


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.databinding.MecEmptyShoppingCartBinding

class MECEmptyCartFragment : Fragment() {
    private lateinit var binding:MecEmptyShoppingCartBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = MecEmptyShoppingCartBinding.inflate(inflater, container, false)
        binding.fragment = this

        return binding.root
    }

    fun onClick() {

    }

}
