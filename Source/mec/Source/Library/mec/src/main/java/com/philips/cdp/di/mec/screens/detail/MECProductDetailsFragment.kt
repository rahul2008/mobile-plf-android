package com.philips.cdp.di.mec.screens.detail
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
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
import com.philips.cdp.di.mec.common.MECLauncherActivity
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
open class MECProductDetailsFragment : MecBaseFragment() {

    private lateinit var bottomSheetFragment: MECRetailersFragment

    lateinit var param: String

    private lateinit var binding: MecProductDetailsBinding
    private lateinit var product: ECSProduct
    private lateinit var retailersList: ECSRetailerList
    private lateinit var ecsRetailerViewModel: ECSRetailerViewModel

    lateinit var ecsProductDetailViewModel: EcsProductDetailViewModel

    private val eCSRetailerListObserver: Observer<ECSRetailerList> = object : Observer<ECSRetailerList> {
        override fun onChanged(retailers: ECSRetailerList?) {
            retailersList = retailers!!
            if(retailers.wrbresults.onlineStoresForProduct.stores!=null) {
                binding.mecFindRetailerButton.isEnabled = true
            } else {
                binding.mecFindRetailerButton.isEnabled = false
            }
        }

    }

    private val ratingObserver: Observer<BulkRatingsResponse> = object : Observer<BulkRatingsResponse> {
        override fun onChanged(response :BulkRatingsResponse?) {
            updateData(response?.results)
        }

    }

    private val productObserver: Observer<ECSProduct> = object : Observer<ECSProduct> {

        override fun onChanged(ecsProduct: ECSProduct?) {

            binding.product = ecsProduct
            //setButtonIcon(mec_find_retailer_button, getListIcon())
            //setButtonIcon(mec_add_to_cart_button, getCartIcon())
            showPriceDetail()
            binding.product?.let { addToCartVisibility(it) }
            getRetailerDetails()
            hideProgressBar()
        }

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = MecProductDetailsBinding.inflate(inflater, container, false)

        binding.fragment = this

        ecsProductDetailViewModel = activity!!?.let { ViewModelProviders.of(it).get(EcsProductDetailViewModel::class.java) }!!

        ecsRetailerViewModel = this!!?.let { ViewModelProviders.of(it).get(ECSRetailerViewModel::class.java) }!!

        ecsRetailerViewModel.ecsRetailerList.observe(this, eCSRetailerListObserver)


        ecsProductDetailViewModel.ecsProduct.observe(this, productObserver)


        ecsProductDetailViewModel.bulkRatingResponse.observe(this, ratingObserver)
        ecsProductDetailViewModel.mecError.observe(this, this)


        binding.indicator.viewPager = binding.pager
        val bundle = arguments
        product = bundle?.getSerializable(MECConstant.MEC_KEY_PRODUCT) as ECSProduct


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

        val fragmentAdapter = TabPagerAdapter(this.childFragmentManager, product.code)
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
        getRatings()
    }

    fun addToCartVisibility(product : ECSProduct){
        if(MECDataHolder.INSTANCE.hybrisEnabled.equals(false)){
            binding.mecAddToCartButton.visibility = View.GONE
        } else if((MECDataHolder.INSTANCE.hybrisEnabled.equals(true)) && !(MECutility.isStockAvailable(product!!.stock!!.stockLevelStatus, product!!.stock!!.stockLevel))) {
            binding.mecAddToCartButton.isEnabled = false
        } else{
            binding.mecAddToCartButton.visibility = View.VISIBLE
        }
    }

    open fun executeRequest() {
        createCustomProgressBar(container, MEDIUM)
        binding.progressImage.visibility = View.VISIBLE
        ecsProductDetailViewModel.getProductDetail(product)
    }

    private fun getRetailerDetails() {
        ecsRetailerViewModel.getRetailers(product.code)
    }

    private fun getRatings(){
        ecsProductDetailViewModel.getRatings(product.codeForBazaarVoice)
    }


    //TODO bind it
    fun updateData(results: List<Statistics>?) {
        if (results != null) {
            binding.mecRating.setRating((results.get(0).productStatistics.reviewStatistics.averageOverallRating).toFloat())
            binding.mecRatingLebel.text = DecimalFormat("#.#").format(results.get(0).productStatistics.reviewStatistics.averageOverallRating)
            binding.mecReviewLebel.text = " (" + results.get(0).productStatistics.reviewStatistics.totalReviewCount.toString() + " reviews)"
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


        if (product.discountPrice.formattedValue != null && product.discountPrice.formattedValue.length > 0 && (product.price.value - product.discountPrice.value)>0) {
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
            mec_priceDiscount.text = "-"+discountRounded + "%"
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





    fun onBuyFromRetailerClick() {
        buyFromRetailers(retailersList)
    }

    private fun buyFromRetailers(ecsRetailerList: ECSRetailerList) {
        val bundle = Bundle()
        //bundle.putSerializable(MECConstant.MEC_KEY_PRODUCT,retailersList)
        bundle.putSerializable(MECConstant.MEC_CLICK_LISTENER, this)
        val removedBlacklistedRetailers = ecsProductDetailViewModel.removedBlacklistedRetailers(ecsRetailerList)

            bottomSheetFragment = MECRetailersFragment()
            bundle.putSerializable(MECConstant.MEC_KEY_PRODUCT, retailersList)
            bottomSheetFragment.arguments = bundle
            bottomSheetFragment.setTargetFragment(this,MECConstant.RETAILER_REQUEST_CODE)
            bottomSheetFragment.show(fragmentManager, bottomSheetFragment.tag)


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == MECConstant.RETAILER_REQUEST_CODE && resultCode == Activity.RESULT_OK){

            if (data?.extras?.containsKey(MECConstant.SELECTED_RETAILER)!!) {
                val ecsRetailer : ECSRetailer = data.getSerializableExtra(MECConstant.SELECTED_RETAILER) as ECSRetailer
                param = ecsRetailer.xactparam
                val bundle = Bundle()
                bundle.putString(MECConstant.MEC_BUY_URL, ecsProductDetailViewModel.uuidWithSupplierLink(ecsRetailer.buyURL,param))
                bundle.putString(MECConstant.MEC_STORE_NAME, ecsRetailer.name)
                bundle.putBoolean(MECConstant.MEC_IS_PHILIPS_SHOP, ecsProductDetailViewModel.isPhilipsShop(ecsRetailer))

                val fragment = WebBuyFromRetailersFragment()
                fragment.arguments = bundle
                addFragment(fragment, "retailers", true)
            }
        }
    }

}
