package com.philips.cdp.di.mec.screens.detail
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v4.app.Fragment
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bazaarvoice.bvandroidsdk.*
import com.philips.cdp.di.ecs.model.asset.Asset
import com.philips.cdp.di.ecs.model.asset.Assets
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.ecs.model.retailers.ECSRetailer
import com.philips.cdp.di.ecs.model.retailers.ECSRetailerList

import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.common.ItemClickListener
import com.philips.cdp.di.mec.databinding.MecProductDetailsBinding
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.screens.retailers.ECSRetailerViewModel
import com.philips.cdp.di.mec.screens.retailers.MECRetailersFragment
import com.philips.cdp.di.mec.screens.retailers.WebBuyFromRetailersFragment
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.cdp.di.mec.utils.MECDataHolder
import com.philips.cdp.di.mec.utils.MECutility
import com.philips.platform.uid.view.widget.Button
import kotlinx.android.synthetic.main.mec_main_activity.*
import kotlinx.android.synthetic.main.mec_product_details.*
import java.text.DecimalFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
open class MECProductDetailsFragment : MecBaseFragment(), ItemClickListener {

    private lateinit var bottomSheetFragment: MECRetailersFragment

    lateinit var param: String
    private var mUpdtedRetailers: ArrayList<ECSRetailer>? = null


    override fun onItemClick(item: Any) {
        val ecsRetailers = item as ECSRetailer
        param = ecsRetailers.xactparam
        val bundle = Bundle()
        bundle.putString(MECConstant.MEC_BUY_URL, uuidWithSupplierLink(ecsRetailers.buyURL))
        bundle.putString(MECConstant.MEC_STORE_NAME, ecsRetailers.name)
        bundle.putBoolean(MECConstant.MEC_IS_PHILIPS_SHOP, isPhilipsShop(ecsRetailers))
        if (bottomSheetFragment.isAdded && bottomSheetFragment.isVisible) {
            bottomSheetFragment.dismiss()
        }
        val fragment = WebBuyFromRetailersFragment()
        fragment.arguments = bundle
        addFragment(fragment, "retailers", true)

    }


    private lateinit var binding: MecProductDetailsBinding
    private lateinit var product: ECSProduct
    private lateinit var retailersList: ECSRetailerList
    private lateinit var ecsRetailerViewModel: ECSRetailerViewModel

    lateinit var ecsProductDetailViewModel: EcsProductDetailViewModel

    private val eCSRetailerListObserver: Observer<ECSRetailerList> = object : Observer<ECSRetailerList> {
        override fun onChanged(retailers: ECSRetailerList?) {
            retailersList = retailers!!
            binding.mecFindRetailerButton.isEnabled = retailers?.wrbresults?.onlineStoresForProduct!!.stores != null
        }

    }

    private val productObserver: Observer<ECSProduct> = object : Observer<ECSProduct> {

        override fun onChanged(ecsProduct: ECSProduct?) {

            binding.product = ecsProduct
            setButtonIcon(mec_find_retailer_button, getListIcon())
            setButtonIcon(mec_add_to_cart_button, getCartIcon())
            showPriceDetail()
            getRetailerDetails()
            hideProgressBar()
        }

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = MecProductDetailsBinding.inflate(inflater, container, false)

        binding.fragment = this

        ecsProductDetailViewModel = this!!.activity?.let { ViewModelProviders.of(it).get(EcsProductDetailViewModel::class.java) }!!
        ecsRetailerViewModel = this!!.activity?.let { ViewModelProviders.of(it).get(ECSRetailerViewModel::class.java) }!!

        ecsRetailerViewModel.ecsRetailerList.observe(this, eCSRetailerListObserver)

        ecsProductDetailViewModel.ecsProduct.observe(this, productObserver)
        ecsProductDetailViewModel.mecError.observe(this, this)

        binding.indicator.viewPager = binding.pager

        val bundle = arguments
        product = bundle?.getSerializable(MECConstant.MEC_KEY_PRODUCT) as ECSProduct

        //val client = BVConversationsClient.Builder(BVSDK.getInstance()).build()
        // Send Request
        val bvClient = MECDataHolder.INSTANCE.bvClient
        var ctns = mutableListOf(product.codeForBazaarVoice)
        val request = BulkRatingsRequest.Builder(ctns, BulkRatingOptions.StatsType.All).addFilter(BulkRatingOptions.Filter.ContentLocale, EqualityOperator.EQ, MECDataHolder.INSTANCE.locale).addCustomDisplayParameter(MECConstant.KEY_BAZAAR_LOCALE, MECDataHolder.INSTANCE.locale).build()
        bvClient!!.prepareCall(request).loadAsync(reviewsCb)


        //TODO Adding Default Asset
        var asset = Asset()
        asset.asset = "xyz"
        asset.type = "UNKNOWN"

        var assets = Assets()
        assets.asset = Arrays.asList(asset)
        product.assets = assets

        ecsProductDetailViewModel.ecsProduct.value = product

        // Ends here
        // binding.product = product

        val fragmentAdapter = TabPagerAdapter(activity!!.supportFragmentManager, product.code)
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

    open fun executeRequest() {
        createCustomProgressBar(container, MEDIUM)
        ecsProductDetailViewModel.getProductDetail(product)
    }

    private fun getRetailerDetails() {
        ecsRetailerViewModel.getRetailers(product.code)
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
            binding.mecRatingLebel.text = DecimalFormat("#.#").format(results.get(0).productStatistics.nativeReviewStatistics.averageOverallRating)
            binding.mecReviewLebel.text = " (" + results.get(0).productStatistics.nativeReviewStatistics.totalReviewCount.toString() + " reviews)"
        }

    }

    fun getListIcon(): Drawable? {
        return VectorDrawableCompat.create(resources, R.drawable.mec_ic_hamburger_icon, context!!.theme)
    }

    fun getCartIcon(): Drawable? {
        return VectorDrawableCompat.create(resources, R.drawable.mec_shopping_cart, context!!.theme)
    }

    private fun setButtonIcon(button: Button, drawable: Drawable?) {
        var mutateDrawable = drawable
        if (drawable != null) {
            mutateDrawable = drawable.constantState!!.newDrawable().mutate()
        }
        button.setImageDrawable(mutateDrawable)
    }


    fun showPriceDetail() {

        val textSize16 = getResources().getDimensionPixelSize(com.philips.cdp.di.mec.R.dimen.mec_product_detail_discount_price_label_size);
        val textSize12 = getResources().getDimensionPixelSize(com.philips.cdp.di.mec.R.dimen.mec_product_detail_price_label_size);


        if (product.discountPrice.formattedValue != null && product.discountPrice.formattedValue.length > 0) {
            mecPriceDetailId.visibility = View.VISIBLE
            mec_priceDetailIcon.visibility = View.VISIBLE
            mec_priceDiscount.visibility = View.VISIBLE
            mec_priceDiscountIcon.visibility = View.VISIBLE
            val price = SpannableString(product.price.formattedValue);
            price.setSpan(AbsoluteSizeSpan(textSize12), 0, product.price.formattedValue.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            price.setSpan(StrikethroughSpan(), 0, product.price.formattedValue.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            price.setSpan(ForegroundColorSpan(R.attr.uidContentItemTertiaryNormalTextColor), 0, product.price.formattedValue.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            val discountPrice = SpannableString(product.discountPrice.formattedValue);
            discountPrice.setSpan(AbsoluteSizeSpan(textSize16), 0, product.discountPrice.formattedValue.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            val CharSequence = TextUtils.concat(price, "  ", discountPrice);
            mecPriceDetailId.text = CharSequence;
            val discount = (product.price.value - product.discountPrice.value) / product.price.value * 100

            val discountRounded: String = String.format("%.2f", discount).toString()
            mec_priceDiscount.text = discountRounded + "%"
        } else if (product.price.formattedValue != null && product.price.formattedValue.length > 0) {
            mecPriceDetailId.visibility = View.VISIBLE
            mec_priceDetailIcon.visibility = View.VISIBLE

            mec_priceDiscount.visibility = View.GONE
            mec_priceDiscountIcon.visibility = View.GONE
            mecPriceDetailId.text = product.price.formattedValue;

        } else {
            mecPriceDetailId.visibility = View.GONE
            mec_priceDetailIcon.visibility = View.GONE
            mec_priceDiscount.visibility = View.GONE
            mec_priceDiscountIcon.visibility = View.GONE
        }

    }


    private fun uuidWithSupplierLink(buyURL: String): String {

        val propositionId = MECDataHolder.INSTANCE.propositionId

        val supplierLinkWithUUID = "$buyURL&wtbSource=mobile_$propositionId&$param="

        return supplierLinkWithUUID + UUID.randomUUID().toString()
    }

    fun isPhilipsShop(retailer: ECSRetailer): Boolean {
        return retailer.isPhilipsStore.equals("Y", ignoreCase = true)
    }


    fun onBuyFromRetailerClick() {
        buyFromRetailers(retailersList)
    }

    private fun buyFromRetailers(ecsRetailerList: ECSRetailerList) {
        val bundle = Bundle()
        //bundle.putSerializable(MECConstant.MEC_KEY_PRODUCT,retailersList)
        bundle.putSerializable(MECConstant.MEC_CLICK_LISTENER, this)
        val removedBlacklistedRetailers = removedBlacklistedRetailers(ecsRetailerList)

        val retailers = removedBlacklistedRetailers.retailers;

        if (retailers.size.equals(1) && retailers.get(0).isPhilipsStore.equals("Y")) {
            bundle.putString(MECConstant.MEC_BUY_URL, retailers.get(0).buyURL)
            bundle.putString(MECConstant.MEC_STORE_NAME, retailers.get(0).name)
            bundle.putString(MECConstant.MEC_IS_PHILIPS_SHOP, isPhilipsShop(retailers.get(0)).toString())
            val fragment = WebBuyFromRetailersFragment()
            fragment.arguments = bundle
            addFragment(fragment, "retailers", true)
        } else {
            bottomSheetFragment = MECRetailersFragment()
            bundle.putSerializable(MECConstant.MEC_KEY_PRODUCT, retailersList)
            bundle.putSerializable(MECConstant.MEC_CLICK_LISTENER, this)
            bottomSheetFragment.arguments = bundle
            bottomSheetFragment.show(fragmentManager, bottomSheetFragment.tag)
        }


    }

    private fun removedBlacklistedRetailers(ecsRetailers: ECSRetailerList): ECSRetailerList {
        val list = MECDataHolder.INSTANCE.blackListedRetailers

        for (name in list) {

            val iterator = ecsRetailers.retailers.iterator()

            while (iterator.hasNext()) {

                val retailerName = iterator.next().getName().replace("\\s+".toRegex(), "")
                if (name.equals(retailerName, true)) {

                    if (MECutility.indexOfSubString(true, retailerName, name) >= 0) {
                        iterator.remove()

                    }
                }
            }

        }

        return ecsRetailers
    }
}
