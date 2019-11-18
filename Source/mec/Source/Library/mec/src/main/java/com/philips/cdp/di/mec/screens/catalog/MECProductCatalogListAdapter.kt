package com.philips.cdp.di.mec.screens.catalog

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bazaarvoice.bvandroidsdk.Statistics
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.common.ItemClickListener
import com.philips.cdp.di.mec.databinding.MecProductCatalogItemListBinding
import java.text.DecimalFormat


class MECProductCatalogListAdapter(private val items: MutableList<ECSProduct> ,private val itemClickListener : ItemClickListener) :MECProductCatalogBaseAbstractAdapter(items) {

    private lateinit var binding: MecProductCatalogItemListBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MECProductCatalogAbstractViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = MecProductCatalogItemListBinding.inflate(inflater)
        return MECProductCatalogListHolder( binding ,itemClickListener!!)
    }

    override fun updateData(results: List<Statistics>?) {
        if (results != null) {
           // binding.iapRatingLebel.text = " (" + results.get(0).productStatistics.nativeReviewStatistics.totalReviewCount.toString() + " reviews)"
        }
    }
}