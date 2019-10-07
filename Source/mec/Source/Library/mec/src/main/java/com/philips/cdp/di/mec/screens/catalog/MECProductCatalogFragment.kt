package com.philips.cdp.di.mec.screens.catalog


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
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

        for (ecsProducts in ecsProductsList!!){


            for(ecsProduct in ecsProducts.products){

                pojoList.add(Pojo(ecsProduct.summary.productTitle,ecsProduct.summary.price.formattedDisplayPrice,ecsProduct.summary.imageURL))

            }
        }

        adapter.notifyDataSetChanged()

    }

    private lateinit var adapter: MECProductCatalogAdapter
    val TAG = MECProductCatalogFragment::class.java.name

    lateinit var ecsProductViewModel :EcsProductViewModel

    lateinit var ecsProducts : List<ECSProduct>

    lateinit var pojoList : MutableList<Pojo>

    private lateinit var binding: MecProductCatalogFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate<MecProductCatalogFragmentBinding>(inflater, R.layout.mec_product_catalog_fragment, container, false);


        ecsProductViewModel = ViewModelProviders.of(this).get(EcsProductViewModel::class.java)

        ecsProductViewModel.ecsProductsList.observe(this, this);

        ecsProductViewModel.mecError.observe(this, object :Observer<MecError>{

            override fun onChanged(mecError: MecError?) {
                System.out.println("Error while  fetching")

            }
        })

        ecsProductViewModel.init(0,20);




        pojoList = mutableListOf<Pojo>()


        return binding.root
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = MECProductCatalogAdapter(pojoList)

        binding.productCatalogRecyclerView.adapter = adapter

        binding.productCatalogRecyclerView.apply {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

    }


}
