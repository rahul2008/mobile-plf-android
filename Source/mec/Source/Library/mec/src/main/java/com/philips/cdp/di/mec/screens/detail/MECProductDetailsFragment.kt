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
import com.bazaarvoice.bvandroidsdk.*
import com.philips.cdp.di.ecs.model.asset.Asset
import com.philips.cdp.di.ecs.model.asset.Assets
import com.philips.cdp.di.ecs.model.products.ECSProduct

import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.databinding.MecProductDetailsBinding
import com.philips.cdp.di.mec.screens.Detail.TabPagerAdapter
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.cdp.di.mec.utils.MECDataHolder
import com.philips.platform.uid.view.widget.Button
import kotlinx.android.synthetic.main.mec_main_activity.*
import kotlinx.android.synthetic.main.mec_product_details.*
import java.text.DecimalFormat
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
            setButtonIcon(mec_find_retailer_button,getCartIcon())
            setButtonIcon(mec_add_to_cart_button,getCartIcon())
            hideProgressBar()
        }

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = MecProductDetailsBinding.inflate(inflater, container, false)

        val context = inflater.context


        ecsProductDetailViewModel = ViewModelProviders.of(this).get(EcsProductDetailViewModel::class.java)

        ecsProductDetailViewModel.ecsProduct.observe(this, productObserver)
        ecsProductDetailViewModel.mecError.observe(this,this)

        binding.indicator.viewPager = binding.pager

        val bundle = arguments
        product = bundle?.getSerializable(MECConstant.MEC_KEY_PRODUCT) as ECSProduct

        //val client = BVConversationsClient.Builder(BVSDK.getInstance()).build()
        // Send Request
        val bvClient = MECDataHolder.INSTANCE.bvClient
        var ctns = mutableListOf(product.codeForBazaarVoice)
        val request = BulkRatingsRequest.Builder(ctns, BulkRatingOptions.StatsType.All).addFilter(BulkRatingOptions.Filter.ContentLocale,EqualityOperator.EQ,"en_US").addCustomDisplayParameter("Locale","de_DE").build()
        bvClient!!.prepareCall(request).loadAsync(reviewsCb)
        /*val request = BulkRatingsRequest.Builder(ctns, BulkRatingOptions.StatsType.NativeReviews).build()
        bvClient!!.prepareCall(request).loadAsync(reviewsCb)*/

        //TODO Adding Default Asset
        var asset = Asset()
        asset.asset = "xyz"
        asset.type = "UNKNOWN"

        var assets= Assets()
        assets.asset = Arrays.asList(asset)
        product.assets = assets

        // Ends here
        binding.product = product

        val fragmentAdapter = TabPagerAdapter(activity!!.supportFragmentManager)
        binding.viewpagerMain.adapter = fragmentAdapter

        binding.tabsMain.setupWithViewPager(binding.viewpagerMain)
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

    private val reviewsCb = object : ConversationsDisplayCallback<BulkRatingsResponse> {
        override fun onSuccess(response: BulkRatingsResponse) {
            if (response.results.isEmpty()) {
                //Util.showMessage(this@DisplayOverallRatingActivity, "Empty results", "No ratings found for this product")
            } else {
                updateData(response.results)
            }
        }

        override fun onFailure(exception: ConversationsException) {
            //Util.showMessage(this@DisplayOverallRatingActivity, "Error occurred", Util.bvErrorsToString(exception.errors))
        }
    }

    fun updateData(results: List<Statistics>?) {
        if (results != null) {
            binding.mecRating.setRating((results.get(0).productStatistics.nativeReviewStatistics.averageOverallRating).toFloat())
            binding.mecRatingLebel.text =  DecimalFormat("#.#").format(results.get(0).productStatistics.nativeReviewStatistics.averageOverallRating)
        }
        ///////////
      /*  val fragmentAdapter = TabPagerAdapter(activity!!.supportFragmentManager)
        viewpager_main.adapter = fragmentAdapter

        tabs_main.setupWithViewPager(viewpager_main)*/
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
