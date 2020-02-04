package com.philips.cdp.di.mec.screens.detail

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bazaarvoice.bvandroidsdk.*
import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart
import com.philips.cdp.di.ecs.model.config.ECSConfig
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.ecs.model.retailers.ECSRetailer
import com.philips.cdp.di.ecs.model.retailers.ECSRetailerList

import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.analytics.MECAnalyticPageNames.productDetails
import com.philips.cdp.di.mec.analytics.MECAnalytics
import com.philips.cdp.di.mec.analytics.MECAnalyticsConstant.prodView
import com.philips.cdp.di.mec.analytics.MECAnalyticsConstant.mecProducts
import com.philips.cdp.di.mec.analytics.MECAnalyticsConstant.outOfStock
import com.philips.cdp.di.mec.analytics.MECAnalyticsConstant.retailerName
import com.philips.cdp.di.mec.analytics.MECAnalyticsConstant.sendData
import com.philips.cdp.di.mec.analytics.MECAnalyticsConstant.specialEvents
import com.philips.cdp.di.mec.analytics.MECAnalyticsConstant.stockStatus
import com.philips.cdp.di.mec.auth.HybrisAuth
import com.philips.cdp.di.mec.common.MecError
import com.philips.cdp.di.mec.databinding.MecProductDetailsBinding
import com.philips.cdp.di.mec.integration.MecHolder
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.screens.retailers.ECSRetailerViewModel
import com.philips.cdp.di.mec.screens.retailers.MECRetailersFragment
import com.philips.cdp.di.mec.screens.retailers.WebBuyFromRetailersFragment
import com.philips.cdp.di.mec.screens.shoppingCart.MECShoppingCartFragment
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.cdp.di.mec.utils.MECConstant.MEC_PRODUCT
import com.philips.cdp.di.mec.utils.MECDataHolder
import com.philips.cdp.di.mec.utils.MECutility
import com.philips.platform.pif.DataInterface.USR.enums.UserLoggedInState
import kotlinx.android.synthetic.main.mec_main_activity.*
import kotlinx.android.synthetic.main.mec_product_details.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass.
 */
open class MECProductDetailsFragment : MecBaseFragment() {

    var mRootView: View? = null
    private lateinit var bottomSheetFragment: MECRetailersFragment

    lateinit var param: String

    private lateinit var binding: MecProductDetailsBinding
    private  var product: ECSProduct? =null
    private lateinit var retailersList: ECSRetailerList
    private lateinit var ecsRetailerViewModel: ECSRetailerViewModel

    lateinit var ecsProductDetailViewModel: EcsProductDetailViewModel

    private val eCSRetailerListObserver: Observer<ECSRetailerList> = object : Observer<ECSRetailerList> {
        override fun onChanged(retailers: ECSRetailerList?) {
            retailersList = retailers!!
            ecsProductDetailViewModel.removeBlacklistedRetailers(retailersList)
            if (retailers.wrbresults.onlineStoresForProduct != null) {
                if (retailersList.wrbresults.onlineStoresForProduct.stores.retailerList.size > 0) {
                    if (binding.mecAddToCartButton.visibility == View.GONE) {
                        binding.mecFindRetailerButtonPrimary.visibility = View.VISIBLE
                        binding.mecFindRetailerButtonSecondary.visibility = View.GONE
                        binding.mecFindRetailerButtonPrimary.isEnabled = true
                    } else if (binding.mecAddToCartButton.visibility == View.VISIBLE) {
                        binding.mecFindRetailerButtonSecondary.visibility = View.VISIBLE
                        binding.mecFindRetailerButtonPrimary.visibility = View.GONE
                        binding.mecFindRetailerButtonSecondary.isEnabled = true
                    }
                }
            } else {
                binding.mecFindRetailerButtonPrimary.isEnabled = false
                binding.mecFindRetailerButtonSecondary.isEnabled = false
            }
            ecsProductDetailViewModel.setStockInfoWithRetailer(binding.mecProductDetailStockStatus, product, retailersList)
            hideProgressBar()
            binding.progressImage.visibility = View.GONE
            //getStock(binding.mecProductDetailStockStatus.text.toString())
        }

    }

    fun getStock(stock: String) {
        if (stock.equals(R.string.mec_out_of_stock)) {

        }
    }

    private val ratingObserver: Observer<BulkRatingsResponse> = object : Observer<BulkRatingsResponse> {
        override fun onChanged(response: BulkRatingsResponse?) {
            updateData(response?.results)
        }

    }

    private val productObserver: Observer<ECSProduct> = object : Observer<ECSProduct> {

        override fun onChanged(ecsProduct: ECSProduct?) {

            binding.product = ecsProduct
            showPriceDetail()
            addToCartVisibility(ecsProduct!!)
            if (!MECDataHolder.INSTANCE.hybrisEnabled && !MECDataHolder.INSTANCE.retailerEnabled) {
                binding.mecProductDetailStockStatus.text = binding.mecProductDetailStockStatus.context.getString(R.string.mec_out_of_stock)
                binding.mecProductDetailStockStatus.setTextColor(binding.mecProductDetailStockStatus.context.getColor(R.color.uid_signal_red_level_30))
            }
            if (MECDataHolder.INSTANCE.retailerEnabled) {
                getRetailerDetails()
            } else {
                binding.mecFindRetailerButtonPrimary.visibility = View.GONE
                binding.mecFindRetailerButtonSecondary.visibility = View.GONE
                if (MECDataHolder.INSTANCE.hybrisEnabled) {
                    if (null != product && null != product!!.stock) {
                        if (MECutility.isStockAvailable(product!!.stock!!.stockLevelStatus, product!!.stock!!.stockLevel)) {
                            binding.mecProductDetailStockStatus.text = binding.mecProductDetailStockStatus.context.getString(R.string.mec_in_stock)
                            binding.mecProductDetailStockStatus.setTextColor(binding.mecProductDetailStockStatus.context.getColor(R.color.uid_signal_green_level_30))
                        } else {
                            binding.mecProductDetailStockStatus.text = binding.mecProductDetailStockStatus.context.getString(R.string.mec_out_of_stock)
                            binding.mecProductDetailStockStatus.setTextColor(binding.mecProductDetailStockStatus.context.getColor(R.color.uid_signal_red_level_30))
                            tagOutOfStockActions(product!!)
                        }
                    }
                }
            }
            hideProgressBar()
        }

    }

    private val shoppingCartObserver: Observer<ECSShoppingCart> = object : Observer<ECSShoppingCart>{
        /**
         * Called when the data is changed.
         * @param t  The new data
         */
        override fun onChanged(eCSShoppingCart: ECSShoppingCart?) {

            if(null!= eCSShoppingCart && null!=eCSShoppingCart.code){
                Log.v("cart_addded",eCSShoppingCart.totalPrice.formattedValue )
                // product added to cart
                // go to Shoppingcart fragment

                val bundle = Bundle()
                bundle.putSerializable(MECConstant.MEC_SHOPPING_CART, eCSShoppingCart)
                binding.progress.visibility = View.GONE
                val fragment = MECShoppingCartFragment()
                fragment.arguments = bundle
                replaceFragment(fragment,"cart",true)

            }


        }

    }




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        if(null==mRootView) {
            binding = MecProductDetailsBinding.inflate(inflater, container, false)

            binding.fragment = this
            binding.mecDataHolder = MECDataHolder.INSTANCE

            ecsProductDetailViewModel = activity!!?.let { ViewModelProviders.of(it).get(EcsProductDetailViewModel::class.java) }!!

            ecsRetailerViewModel = this!!?.let { ViewModelProviders.of(it).get(ECSRetailerViewModel::class.java) }!!

            ecsRetailerViewModel.ecsRetailerList.observe(this, eCSRetailerListObserver)


            ecsProductDetailViewModel.ecsProduct.observe(this, productObserver)



            ecsProductDetailViewModel.bulkRatingResponse.observe(this, ratingObserver)
            ecsProductDetailViewModel.mecError.observe(this, this)



            binding.indicator.viewPager = binding.pager
            val bundle = arguments

            var ctns = bundle!!.getStringArrayList(MECConstant.CATEGORISED_PRODUCT_CTNS)

            if( bundle.containsKey(MECConstant.MEC_KEY_PRODUCT)) { // if   // launched from catalog
                product = bundle?.getSerializable(MECConstant.MEC_KEY_PRODUCT) as ECSProduct
                updateProductInfo(product!!)
                getECSConfig(null)
            }else{
                var ctns = bundle!!.getStringArrayList(MECConstant.CATEGORISED_PRODUCT_CTNS)
                val ctn = ctns.get(0)
                getECSConfig(ctn)
            }



            mRootView=binding.root
        }
        return binding.root
    }

    private fun updateProductInfo(product :ECSProduct){
        //if assets are not available , we should show one Default image
        ecsProductDetailViewModel.addNoAsset(product)

        ecsProductDetailViewModel.ecsProduct.value = product

        val fragmentAdapter = TabPagerAdapter(this.childFragmentManager, product.code)
        binding.viewpagerMain.offscreenPageLimit = 4
        binding.viewpagerMain.adapter = fragmentAdapter
        binding.tabsMain.setupWithViewPager(binding.viewpagerMain)
        MECAnalytics.trackPage(productDetails)
        tagActions(product);

    }

    fun getProduct(ctn :String){

        if( MECDataHolder.INSTANCE.hybrisEnabled){
            val productCallBack = object :ECSCallback<ECSProduct, Exception>{

                override fun onResponse(result: ECSProduct?) {
                    product= result!!
                    updateProductInfo(product!!)
                    executeRequest()
                    getRatings()
                }

                override fun onFailure(error: Exception?, ecsError: ECSError?) {
                    Log.v("PR FETCH", ecsError?.errorType.toString())
                }

            }
            MecHolder.INSTANCE.eCSServices.fetchProduct(ctn, productCallBack)
        }else{
            val productListCallback  =  object : ECSCallback<List<ECSProduct>,Exception>{
                override fun onResponse(result: List<ECSProduct>?) {
                    if (result != null) {
                        product=result.get(0)
                        updateProductInfo(product!!)
                        executeRequest()
                        getRatings()
                    }
                }

                override fun onFailure(error: Exception?, ecsError: ECSError?) {
                    Log.v("PR FETCH", ecsError?.errorType.toString())
                }

            }
            MecHolder.INSTANCE.eCSServices.fetchProductSummaries(Arrays.asList(ctn), productListCallback)
        }

    }


    override fun onResume() {
        super.onResume()
        setTitleAndBackButtonVisibility(R.string.mec_product_detail_title, true)
    }


    fun addToCartVisibility(product: ECSProduct) {
        if (MECDataHolder.INSTANCE.hybrisEnabled.equals(false)) {
            binding.mecAddToCartButton.visibility = View.GONE
        } else if ((MECDataHolder.INSTANCE.hybrisEnabled.equals(true)) && !(MECutility.isStockAvailable(product!!.stock!!.stockLevelStatus, product!!.stock!!.stockLevel))) {
            binding.mecAddToCartButton.visibility = View.VISIBLE
            binding.mecAddToCartButton.isEnabled = false
        } else {
            binding.mecAddToCartButton.visibility = View.VISIBLE
            binding.mecAddToCartButton.isEnabled = true
        }
    }

    open fun executeRequest() {
        createCustomProgressBar(container, MEDIUM)
        binding.progressImage.visibility = View.VISIBLE
        ecsProductDetailViewModel.getProductDetail(product!!)
    }

    private fun getRetailerDetails() {
        if(null!=product) {
            ecsRetailerViewModel.getRetailers(product!!.code)
        }
    }

    private fun getRatings() {
        ecsProductDetailViewModel.getRatings(product!!.codeForBazaarVoice)
    }


    //TODO bind it
    fun updateData(results: List<Statistics>?) {
        if (results != null) {
            binding.mecRating.setRating((results.get(0).productStatistics.reviewStatistics.averageOverallRating).toFloat())
            binding.mecRatingLebel.text = DecimalFormat("#.#").format(results.get(0).productStatistics.reviewStatistics.averageOverallRating)
            binding.mecReviewLebel.text = " (" + results.get(0).productStatistics.reviewStatistics.totalReviewCount.toString() + " reviews)"
        }

    }


    fun showPriceDetail() {

        val textSize16 = getResources().getDimensionPixelSize(com.philips.cdp.di.mec.R.dimen.mec_product_detail_discount_price_label_size);
        val textSize12 = getResources().getDimensionPixelSize(com.philips.cdp.di.mec.R.dimen.mec_product_detail_price_label_size);
        if(null!=product) {

            if (product!!.discountPrice != null && product!!.discountPrice.formattedValue != null && product!!.discountPrice.formattedValue.length > 0 && (product!!.price.value - product!!.discountPrice.value) > 0) {
                mecPriceDetailId.visibility = View.VISIBLE
                mec_priceDetailIcon.visibility = View.VISIBLE
                mec_priceDiscount.visibility = View.VISIBLE
                mec_priceDiscountIcon.visibility = View.VISIBLE
                val price = SpannableString(product!!.price.formattedValue);
                price.setSpan(AbsoluteSizeSpan(textSize12), 0, product!!.price.formattedValue.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                price.setSpan(StrikethroughSpan(), 0, product!!.price.formattedValue.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                price.setSpan(ForegroundColorSpan(R.attr.uidContentItemTertiaryNormalTextColor), 0, product!!.price.formattedValue.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                val discountPrice = SpannableString(product!!.discountPrice.formattedValue);
                discountPrice.setSpan(AbsoluteSizeSpan(textSize16), 0, product!!.discountPrice.formattedValue.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                val CharSequence = TextUtils.concat(price, "  ", discountPrice);
                mecPriceDetailId.text = CharSequence;
                val discount = (product!!.price.value - product!!.discountPrice.value) / product!!.price.value * 100

                val discountRounded: String = String.format("%.2f", discount).toString()
                mec_priceDiscount.text = "-" + discountRounded + "%"
            } else if (product!!.price != null && product!!.price.formattedValue != null && product!!.price.formattedValue.length > 0) {
                mecPriceDetailId.visibility = View.VISIBLE
                mec_priceDetailIcon.visibility = View.VISIBLE

                mec_priceDiscount.visibility = View.GONE
                mec_priceDiscountIcon.visibility = View.GONE
                mecPriceDetailId.text = product!!.price.formattedValue;

            } else {
                mecPriceDetailId.visibility = View.GONE
                mec_priceDetailIcon.visibility = View.GONE
                mec_priceDiscount.visibility = View.GONE
                mec_priceDiscountIcon.visibility = View.GONE
            }
        }

    }


    fun onBuyFromRetailerClick() {
        buyFromRetailers()
    }

    fun addToCartClick(){
        if(null!=binding.product ) {
            if (MECDataHolder.INSTANCE.userDataInterface != null && MECDataHolder.INSTANCE.userDataInterface.getUserLoggedInState() == UserLoggedInState.USER_LOGGED_IN) {
                val addToProductCallback =  object: ECSCallback<ECSShoppingCart, Exception> {

                    override fun onResponse(eCSShoppingCart: ECSShoppingCart?) {
                        val bundle = Bundle()
                        bundle.putSerializable(MECConstant.MEC_SHOPPING_CART, eCSShoppingCart)
                        val fragment = MECShoppingCartFragment()
                        fragment.arguments = bundle
                        replaceFragment(fragment,MECShoppingCartFragment.TAG,true)
                    }

                    override fun onFailure(error: Exception?, ecsError: ECSError?) {
                        val mecError = MecError(error, ecsError)
                        fragmentManager?.let { context?.let { it1 -> MECutility.showErrorDialog(it1, it,getString(R.string.mec_ok), getString(R.string.mec_error), mecError!!.exception!!.message.toString()) } }
                    }

                }
                product?.let { ecsProductDetailViewModel.addProductToShoppingcart(it,addToProductCallback) }
            }else{
                fragmentManager?.let { context?.let { it1 -> MECutility.showErrorDialog(it1, it,getString(R.string.mec_ok), getString(R.string.mec_error), getString(R.string.mec_cart_login_error_message)) } }
            }
        }
    }

    private fun buyFromRetailers() {
        val bundle = Bundle()
        bottomSheetFragment = MECRetailersFragment()
        bundle.putSerializable(MECConstant.MEC_KEY_PRODUCT, retailersList)
        bundle.putSerializable(MEC_PRODUCT,binding.product)
        bottomSheetFragment.arguments = bundle
        bottomSheetFragment.setTargetFragment(this, MECConstant.RETAILER_REQUEST_CODE)
        bottomSheetFragment.show(fragmentManager, bottomSheetFragment.tag)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == MECConstant.RETAILER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            if (data?.extras?.containsKey(MECConstant.SELECTED_RETAILER)!!) {
                val ecsRetailer: ECSRetailer = data.getSerializableExtra(MECConstant.SELECTED_RETAILER) as ECSRetailer
                param = ecsRetailer.xactparam
                val bundle = Bundle()
                bundle.putString(MECConstant.MEC_BUY_URL, ecsProductDetailViewModel.uuidWithSupplierLink(ecsRetailer.buyURL, param))
                bundle.putString(MECConstant.MEC_STORE_NAME, ecsRetailer.name)
                bundle.putBoolean(MECConstant.MEC_IS_PHILIPS_SHOP, ecsProductDetailViewModel.isPhilipsShop(ecsRetailer))

                tagActionsforRetailer(ecsRetailer.name, MECutility.stockStatus(ecsRetailer.availability))
                val fragment = WebBuyFromRetailersFragment()
                fragment.arguments = bundle
                addFragment(fragment, "retailers", true)
            }
        }
    }

    override fun processError(mecError: MecError?) {
        binding.mecProductDetailsEmptyTextLabel.visibility = View.VISIBLE
    }


    override fun onChanged(mecError: MecError?) {
        binding.mecProductDetailsEmptyTextLabel.visibility = View.VISIBLE

    }


    private fun tagActions(product: ECSProduct) {
        var map = HashMap<String, String>()
        map.put(specialEvents, prodView)
        map.put(mecProducts, MECAnalytics.getProductInfo(product))
        MECAnalytics.trackMultipleActions(sendData, map)
    }


    private fun tagActionsforRetailer(name: String, status: String) {
        var map = HashMap<String, String>()
        map.put(retailerName, name)
        map.put(stockStatus, status)
        map.put(mecProducts, MECAnalytics.getProductInfo(product!!))
        MECAnalytics.trackMultipleActions(sendData, map)
    }

    companion object {

        val TAG = "MECProductDetailsFragment"

        @JvmStatic
        fun tagOutOfStockActions(product: ECSProduct) {
            var map = HashMap<String, String>()
            map.put(specialEvents, outOfStock)
            map.put(mecProducts, MECAnalytics.getProductInfo(product))
            MECAnalytics.trackMultipleActions(sendData, map)
        }
    }

    private fun getECSConfig(productFetchRequiredForCTN : String?){
        MecHolder.INSTANCE.eCSServices.configureECSToGetConfiguration(object: ECSCallback<ECSConfig, Exception> {

            override fun onResponse(config: ECSConfig?) {
                if (MECDataHolder.INSTANCE.hybrisEnabled) {
                    MECDataHolder.INSTANCE.hybrisEnabled = config?.isHybris ?: return

                }
                MECDataHolder.INSTANCE.locale = config!!.locale
                MECAnalytics.setCurrencyString(MECDataHolder.INSTANCE.locale)
                if(null!=config!!.rootCategory){
                    MECDataHolder.INSTANCE.rootCategory = config!!.rootCategory
                }
                /////////////////
                if (MECDataHolder.INSTANCE.hybrisEnabled) {
                    binding.mecFindRetailerButtonPrimary.visibility = View.GONE
                    binding.mecFindRetailerButtonSecondary.visibility = View.VISIBLE
                } else if (!MECDataHolder.INSTANCE.hybrisEnabled) {
                    binding.mecFindRetailerButtonPrimary.visibility = View.VISIBLE
                    binding.mecFindRetailerButtonSecondary.visibility = View.GONE
                }
                GlobalScope.launch{
                    HybrisAuth.authHybrisIfNotAlready()
                }
                if(null==productFetchRequiredForCTN) {
                    executeRequest()
                    getRatings()
                }else{
                    getProduct(productFetchRequiredForCTN)
                }

            }

            override fun onFailure(error: Exception?, ecsError: ECSError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        } )
    }

}
