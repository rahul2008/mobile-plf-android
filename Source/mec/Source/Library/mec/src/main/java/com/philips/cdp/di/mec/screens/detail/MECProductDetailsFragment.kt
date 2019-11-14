package com.philips.cdp.di.mec.screens.Detail
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.philips.cdp.di.ecs.model.asset.Asset
import com.philips.cdp.di.ecs.model.products.ECSProduct

import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.databinding.MecProductDetailsBinding
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.screens.catalog.MECProduct
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.platform.uid.view.widget.Button
import kotlinx.android.synthetic.main.mec_main_activity.*
import kotlinx.android.synthetic.main.mec_product_details.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
open class MECProductDetailsFragment : MecBaseFragment() {

    lateinit var ecsProductDetailViewModel: EcsProductDetailViewModel
    private lateinit var binding: MecProductDetailsBinding
    private lateinit var mecProduct: MECProduct
    private lateinit var mecProductDetail: MECProductDetail

    private val RTP = 1
    private val APP = 2
    private val DPP = 3
    private val MI1 = 4
    private val PID = 5


    val productObserver : Observer<ECSProduct> = object : Observer<ECSProduct> {

        override fun onChanged(ecsProduct: ECSProduct?) {

            val mutableList = ecsProduct?.assets?.asset
            
            val fetchImageUrlsFromPRXAssets = fetchImageUrlsFromPRXAssets(mutableList as List<Asset>)

            mecProductDetail = MECProductDetail(fetchImageUrlsFromPRXAssets,mecProduct.name,mecProduct.code)
            binding.detail = mecProductDetail
            setButtonIcon(mec_find_retailer_button,getCartIcon())
            setButtonIcon(mec_add_to_cart_button,getCartIcon())
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
        mecProduct = bundle?.getSerializable(MECConstant.MEC_KEY_PRODUCT) as MECProduct

        var mecAssets  = mutableListOf<MECAsset>()
        mecAssets.add(MECAsset("just to intit")) //TODO
        mecProductDetail = MECProductDetail(mecAssets,mecProduct.name,mecProduct.code)
        binding.detail = mecProductDetail
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
        ecsProduct.code = mecProduct.code
        ecsProductDetailViewModel.getProductDetail(ecsProduct)
    }


    private fun fetchImageUrlsFromPRXAssets(assets: List<Asset>): List<MECAsset> {

        var mecAssets  = mutableListOf<MECAsset>()

        var mAssetsFromPRX = ArrayList<String>()
        val sortedAssetsFromPRX = TreeMap<Int, String>()
        val getHeightAndWidth = GetHeightAndWidth().invoke()
        val width = getHeightAndWidth.width
        val height = getHeightAndWidth.height

        for (asset in assets) {
            val assetType = getAssetType(asset)
            if (assetType != -1) {
                val imagepath = asset.asset + "?wid=" + width +
                        "&hei=" + height + "&\$pnglarge$" + "&fit=fit,1"
                sortedAssetsFromPRX[assetType] = imagepath
            }
            mAssetsFromPRX = ArrayList(sortedAssetsFromPRX.values)
        }

        for(imagePath in mAssetsFromPRX){
            mecAssets.add(MECAsset(imagePath))
        }
        return mecAssets
    }


    private inner class GetHeightAndWidth {
        var width: Int = 0
            private set
        var height: Int = 0
            private set

        internal operator fun invoke(): GetHeightAndWidth {
            width = 0
            height = 0
            width = activity?.getResources()?.displayMetrics?.widthPixels ?: 0
            height = activity?.getResources()?.getDimension(R.dimen.iap_product_detail_image_height)?.toInt()
                    ?:0

            return this
        }
    }

    private fun getAssetType(asset: Asset): Int {
        when (asset.type) {
            "RTP" -> return RTP
            "APP" -> return APP
            "DPP" -> return DPP
            "MI1" -> return MI1
            "PID" -> return PID
            else -> return -1
        }
    }
    

    fun getCartIcon(): Drawable? {
        return VectorDrawableCompat.create(resources, R.drawable.mec_shopping_cart, context!!.theme)
    }

    private fun setButtonIcon(button: Button, drawable: Drawable?){
        var mutateDrawable = drawable
        if (drawable != null) {
            mutateDrawable = drawable.constantState!!.newDrawable().mutate()
        }
        button.setImageDrawable(mutateDrawable)
    }

}
