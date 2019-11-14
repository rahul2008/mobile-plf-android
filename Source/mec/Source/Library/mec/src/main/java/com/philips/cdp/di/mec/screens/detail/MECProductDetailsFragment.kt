package com.philips.cdp.di.mec.screens.detail
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.philips.cdp.di.ecs.model.asset.Asset
import com.philips.cdp.di.ecs.model.asset.Assets
import com.philips.cdp.di.ecs.model.products.ECSProduct

import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.databinding.MecProductDetailsBinding
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.utils.MECConstant
import kotlinx.android.synthetic.main.mec_main_activity.*
import kotlinx.android.synthetic.main.mec_product_details.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
open class MECProductDetailsFragment : MecBaseFragment() {

    lateinit var ecsProductDetailViewModel: EcsProductDetailViewModel
    private lateinit var binding: MecProductDetailsBinding
    private lateinit var product: ECSProduct

    val productObserver : Observer<ECSProduct> = object : Observer<ECSProduct> {

        override fun onChanged(ecsProduct: ECSProduct?) {

            binding.product = ecsProduct
            mec_find_retailer_button.setCompoundDrawablesWithIntrinsicBounds(getCartIcon(),null,null,null)
            mec_add_to_cart_button.setCompoundDrawablesWithIntrinsicBounds(getCartIcon(),null,null,null)
            hideProgressBar()
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = MecProductDetailsBinding.inflate(inflater, container, false)

        ecsProductDetailViewModel = ViewModelProviders.of(this).get(EcsProductDetailViewModel::class.java)

        ecsProductDetailViewModel.ecsProduct.observe(this, productObserver)
        ecsProductDetailViewModel.mecError.observe(this,this)

        binding.indicator.viewPager = binding.pager

        val bundle = arguments
        product = bundle?.getSerializable(MECConstant.MEC_KEY_PRODUCT) as ECSProduct

        //TODO Adding Default Asset
        var asset = Asset()
        asset.asset = "xyz"
        asset.type = "UNKNOWN"

        var assets= Assets()
        assets.asset = Arrays.asList(asset)
        product.assets = assets

        // Ends here
        binding.product = product
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setTitleAndBackButtonVisibility(R.string.mec_product_detail, true)
    }

    override fun onStart() {
        super.onStart()
        executeRequest()
    }

    override fun handleBackEvent(): Boolean {
        return super.handleBackEvent()
    }

    open fun executeRequest(){
        createCustomProgressBar(container, MEDIUM)
        val ecsProduct = ECSProduct()
        ecsProduct.code = product.code
        ecsProductDetailViewModel.getProductDetail(ecsProduct)
    }

    fun getEmailIcon(): Drawable? {
        return VectorDrawableCompat.create(resources, R.drawable.ic_email_icon, context!!.theme)
    }

    fun getCartIcon(): Drawable? {
        return VectorDrawableCompat.create(resources, R.drawable.mec_shopping_cart, context!!.theme)
    }

}
