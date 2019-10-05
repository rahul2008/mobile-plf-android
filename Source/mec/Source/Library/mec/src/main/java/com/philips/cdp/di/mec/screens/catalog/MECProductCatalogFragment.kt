package com.philips.cdp.di.mec.screens.catalog


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.ecs.model.products.ECSProducts

import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.activity.MecError
import com.philips.cdp.di.mec.databinding.MecProductCatalogFragmentBinding
import com.philips.cdp.di.mec.screens.InAppBaseFragment

/**
 * A simple [Fragment] subclass.
 */
class MECProductCatalogFragment : InAppBaseFragment(),Observer<MutableList<ECSProducts>> {



    override fun onChanged(ecsProductsList:MutableList<ECSProducts>?) {

      System.out.println("Size of products"+ (ecsProductsList?.size ?: 0))
    }

    val TAG = MECProductCatalogFragment::class.java.name

    lateinit var ecsProductViewModel :EcsProductViewModel

    lateinit var ecsProducts : List<ECSProduct>

    private lateinit var binding: MecProductCatalogFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        ecsProductViewModel = ViewModelProviders.of(this).get(EcsProductViewModel::class.java)

        ecsProductViewModel.ecsProductsList.observe(this, this);

        ecsProductViewModel.mecError.observe(this, object :Observer<MecError>{

            override fun onChanged(mecError: MecError?) {
                System.out.println("Error while  fetching")

            }
        })

        ecsProductViewModel.init(0,20);

        binding = DataBindingUtil.inflate(inflater ,R.layout.mec_product_catalog_fragment,container , false)
        var myView : View  = binding.root

        //ecsProducts = List<ECSProduct>();

       // MECProductCatalogAdapter()

        return myView;
    }

    override fun onResume() {
        super.onResume()
    }

    override fun handleBackEvent(): Boolean {
        return super.handleBackEvent()
    }


    public fun createInstance(args: Bundle): MECProductCatalogFragment {
        val fragment = MECProductCatalogFragment()
        fragment.arguments = args
        return fragment
    }


}
