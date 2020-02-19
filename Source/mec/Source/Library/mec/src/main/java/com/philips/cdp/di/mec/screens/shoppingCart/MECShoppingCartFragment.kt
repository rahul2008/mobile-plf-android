package com.philips.cdp.di.mec.screens.shoppingCart


import android.graphics.Canvas
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.philips.cdp.di.ecs.model.address.ECSAddress
import com.philips.cdp.di.ecs.model.cart.ECSEntries
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart

import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.databinding.MecShoppingCartFragmentBinding
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.utils.AlertListener
import com.philips.cdp.di.mec.utils.MECutility
import com.philips.platform.uid.view.widget.UIPicker
import kotlinx.android.synthetic.main.mec_main_activity.*

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.address.ECSUserProfile
import com.philips.cdp.di.ecs.model.cart.AppliedVoucherEntity
import com.philips.cdp.di.ecs.model.config.ECSConfig
import com.philips.cdp.di.mec.analytics.MECAnalytics
import com.philips.cdp.di.mec.auth.HybrisAuth
import com.philips.cdp.di.mec.integration.MecHolder
import com.philips.cdp.di.mec.utils.MECDataHolder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import com.philips.cdp.di.mec.common.ItemClickListener
import com.philips.cdp.di.mec.common.MECRequestType
import com.philips.cdp.di.mec.common.MecError
import com.philips.cdp.di.mec.screens.address.*
import com.philips.cdp.di.mec.screens.address.AddressViewModel

import com.philips.cdp.di.mec.screens.address.MECDeliveryFragment
import com.philips.cdp.di.mec.screens.profile.ProfileViewModel
import com.philips.cdp.di.mec.utils.MECConstant
import java.io.Serializable
import com.philips.platform.appinfra.AppInfra
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface
import com.philips.platform.uid.view.widget.InputValidationLayout
import com.philips.platform.uid.view.widget.ValidationEditText


/**
 * A simple [Fragment] subclass.
 */
class MECShoppingCartFragment : MecBaseFragment(), AlertListener, ItemClickListener {
    override fun getFragmentTag(): String {
        return "MECShoppingCartFragment"
    }

    private lateinit var addressViewModel: AddressViewModel
    private lateinit var profileViewModel: ProfileViewModel

    private var mAddressList: List<ECSAddress>? = null

    var mRootView: View? = null

    companion object {
        val TAG = "MECShoppingCartFragment"
    }

    internal var swipeController: MECSwipeController? = null

    private lateinit var binding: MecShoppingCartFragmentBinding
    private var itemPosition: Int = 0
    private var mPopupWindow: UIPicker? = null
    private lateinit var shoppingCart: ECSShoppingCart
    lateinit var ecsShoppingCartViewModel: EcsShoppingCartViewModel
    private var productsAdapter: MECProductsAdapter? = null
    private var vouchersAdapter: MECVouchersAdapter? = null
    private lateinit var productReviewList: MutableList<MECCartProductReview>
    private lateinit var voucherList: MutableList<AppliedVoucherEntity>
    private var voucherCode: String = ""
    private var removeVoucher: Boolean = false
    var validationEditText: ValidationEditText? = null
    val list: ArrayList<String>? = ArrayList()

    private val cartObserver: Observer<ECSShoppingCart> = Observer<ECSShoppingCart> { ecsShoppingCart ->
        hideProgressBar()
        binding.mecProgress.visibility = View.GONE
        binding.shoppingCart = ecsShoppingCart
        shoppingCart = ecsShoppingCart!!

        if (ecsShoppingCart!!.entries.size != 0) {
            binding.mecEmptyResult.visibility = View.GONE
            binding.mecParentLayout.visibility = View.VISIBLE
            ecsShoppingCartViewModel.fetchProductReview(ecsShoppingCart.entries)
        } else if (ecsShoppingCart.entries.size == 0) {
            binding.mecEmptyResult.visibility = View.VISIBLE
            binding.mecParentLayout.visibility = View.GONE
        }

        voucherList.clear()
        if (ecsShoppingCart.appliedVouchers.size > 0) {
            ecsShoppingCart.appliedVouchers?.let { voucherList.addAll(it) }
        }
        vouchersAdapter?.notifyDataSetChanged()

        if (MECDataHolder.INSTANCE.voucherEnabled  && !(MECDataHolder.INSTANCE.voucherCode.isEmpty()) && !(MECDataHolder.INSTANCE.voucherCode.equals("invalid_code"))) {
            for (i in 0..ecsShoppingCart.appliedVouchers.size - 1) {
                list?.add(ecsShoppingCart.appliedVouchers.get(i).voucherCode!!)
                break
            }
            if (!list!!.contains(MECDataHolder.INSTANCE.voucherCode)) {
                ecsShoppingCartViewModel.addVoucher(MECDataHolder.INSTANCE.voucherCode, MECRequestType.MEC_APPLY_VOUCHER_SILENT)
                MECDataHolder.INSTANCE.voucherCode = ""
            }
        }


        if (ecsShoppingCart != null) {
            val quantity = MECutility.getQuantity(ecsShoppingCart)
            updateCount(quantity)
        }
    }


    private val productReviewObserver: Observer<MutableList<MECCartProductReview>> = Observer { mecProductReviews ->
        hideProgressBar()
        productReviewList.clear()
        mecProductReviews?.let { productReviewList.addAll(it) }
        productsAdapter?.notifyDataSetChanged()
        if (productsAdapter != null) {
            //MECProductsAdapter.CloseWindow(this.mPopupWindow).onStop()
        }
    }

    private val fetchProfileObserver: Observer<ECSUserProfile> = Observer { userProfile ->

        hideProgressBar()
        if (userProfile.defaultAddress != null) {
            mAddressList?.let { moveDefaultAddressToTopOfTheList(it, shoppingCart.deliveryAddress.id) }
        }
        if(null!=mAddressList) {
            gotoDeliveryAddress(mAddressList)
        }
    }


    private val addressObserver: Observer<List<ECSAddress>> = Observer(fun(addressList: List<ECSAddress>?) {

        mAddressList = addressList

        if (mAddressList.isNullOrEmpty()) {
            replaceFragment(AddAddressFragment(), AddAddressFragment().getFragmentTag(), true)
            hideProgressBar()
        } else {

            if (shoppingCart.deliveryAddress != null) {
                moveDefaultAddressToTopOfTheList(mAddressList!!, shoppingCart.deliveryAddress.id)
                gotoDeliveryAddress(mAddressList)
                hideProgressBar()
            } else {
                profileViewModel.fetchUserProfile()
            }

        }

    })

    private fun moveDefaultAddressToTopOfTheList(addressList: List<ECSAddress>, defaultAddressID: String) {
        val mutableAddressList = addressList.toMutableList()

        val iterator = mutableAddressList.iterator()

        while (iterator.hasNext()) {

            val ecsAddress = iterator.next()

            if (ecsAddress.id.equals(defaultAddressID, true)) {
                mutableAddressList.remove(ecsAddress)
                mutableAddressList.add(0, ecsAddress)
            }
        }
    }

    private fun gotoDeliveryAddress(addressList: List<ECSAddress>?) {
        var deliveryFragment = MECDeliveryFragment()
        var bundle = Bundle()
        bundle.putSerializable(MECConstant.KEY_ECS_ADDRESSES, addressList as Serializable)
        bundle.putSerializable(MECConstant.KEY_ECS_SHOPPING_CART, shoppingCart)
        deliveryFragment.arguments = bundle
        replaceFragment(deliveryFragment, deliveryFragment.getFragmentTag(), true)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        if (null == mRootView) {
            binding = MecShoppingCartFragmentBinding.inflate(inflater, container, false)
            binding.fragment = this
            binding.mecDataHolder = MECDataHolder.INSTANCE
            ecsShoppingCartViewModel = activity!!.let { ViewModelProviders.of(it).get(EcsShoppingCartViewModel::class.java) }
            addressViewModel = activity!!.let { ViewModelProviders.of(it).get(AddressViewModel::class.java) }
            ecsShoppingCartViewModel.ecsShoppingCart.reObserve(viewLifecycleOwner, cartObserver)
            ecsShoppingCartViewModel.ecsProductsReviewList.reObserve(viewLifecycleOwner, productReviewObserver)
            addressViewModel.ecsAddresses.reObserve(viewLifecycleOwner, addressObserver)
            ecsShoppingCartViewModel.mecError.reObserve(viewLifecycleOwner, this)
            addressViewModel.mecError.reObserve(viewLifecycleOwner, this)
            profileViewModel = activity!!.let { ViewModelProviders.of(it).get(ProfileViewModel::class.java) }


            profileViewModel.userProfile.reObserve(viewLifecycleOwner, fetchProfileObserver)

            val bundle = arguments
            //ecsShoppingCart = bundle?.getSerializable(MECConstant.MEC_SHOPPING_CART) as ECSShoppingCart

            productReviewList = mutableListOf()

            voucherList = mutableListOf()

            productsAdapter = MECProductsAdapter(productReviewList, this)

            productsAdapter = MECProductsAdapter(productReviewList, this)
            vouchersAdapter = MECVouchersAdapter(voucherList, this)

            binding.mecCartSummaryRecyclerView.adapter = productsAdapter


            binding.mecAcceptedCodeRecyclerView.adapter = vouchersAdapter

            binding.mecCartSummaryRecyclerView.adapter = productsAdapter

            val appInfra = AppInfra.Builder().build(context)
            val configInterface = appInfra.configInterface
            val configError = AppConfigurationInterface.AppConfigurationError()



            swipeController = MECSwipeController(binding.mecCartSummaryRecyclerView.context, object : SwipeControllerActions() {
                override fun onRightClicked(position: Int) {
                    itemPosition = position
                    showDialog()
                }

                override fun onLeftClicked(position: Int) {
                    itemPosition = position
                    showDialog()
                }
            })

            val itemTouchHelper = ItemTouchHelper(swipeController!!)
            itemTouchHelper.attachToRecyclerView(binding.mecCartSummaryRecyclerView)

            binding.mecCartSummaryRecyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                    swipeController!!.onDraw(c)
                }
            })
            mRootView = binding.root
            executeRequest()
        }
        return binding.root
    }

    fun showDialog() {
        if (removeVoucher) {
            MECutility.showActionDialog(binding.mecVoucherEditText.context, getString(R.string.mec_remove_voucher), getString(R.string.mec_cancel), getString(R.string.mec_delete_voucher), getString(R.string.mec_delete_voucher_description), fragmentManager!!, this)
        } else {
            MECutility.showActionDialog(binding.mecVoucherEditText.context, getString(R.string.mec_remove_product), getString(R.string.mec_cancel), getString(R.string.mec_delete_item), getString(R.string.mec_delete_item_description), fragmentManager!!, this)
        }
    }

    override fun onPositiveBtnClick() {
        if (removeVoucher) {
            createCustomProgressBar(container, MEDIUM)
            removeVoucher = false
            ecsShoppingCartViewModel.removeVoucher(vouchersAdapter?.getVoucher()?.voucherCode.toString())
        } else {
            updateCartRequest(shoppingCart.entries.get(itemPosition), 0)
        }
    }

    override fun onNegativeBtnClick() {

    }

    override fun onResume() {
        super.onResume()
        setTitleAndBackButtonVisibility(R.string.mec_shopping_cart, true)
        setCartIconVisibility(false)
    }



    override fun onDestroy() {
        super.onDestroy()
        ecsShoppingCartViewModel.ecsShoppingCart.removeObserver(cartObserver)
        ecsShoppingCartViewModel.ecsProductsReviewList.removeObserver(productReviewObserver)
        addressViewModel.ecsAddresses.removeObserver(addressObserver)
        ecsShoppingCartViewModel.mecError.removeObserver(this)
        addressViewModel.mecError.removeObserver( this)
        profileViewModel.userProfile.removeObserver( fetchProfileObserver)
    }

    fun executeRequest() {
        binding.mecProgress.visibility = View.VISIBLE
        //createCustomProgressBar(container, MEDIUM)
        ecsShoppingCartViewModel.getShoppingCart()
    }

    fun updateCartRequest(entries: ECSEntries, int: Int) {
        createCustomProgressBar(container, MEDIUM)
        ecsShoppingCartViewModel.updateQuantity(entries, int)
    }

    fun afterUserNameChange(s: CharSequence) {
        binding.mecVoucherBtn.isEnabled = true
        voucherCode = s.toString()
    }

    fun onClickAddVoucher() {
        createCustomProgressBar(container, MEDIUM)
        ecsShoppingCartViewModel.addVoucher(voucherCode, MECRequestType.MEC_APPLY_VOUCHER)
        binding.mecVoucherEditText.text?.clear()
    }

    fun onCheckOutClick() {
        if(MECDataHolder.INSTANCE.maxCartCount!=0 && shoppingCart.deliveryItemsQuantity > MECDataHolder.INSTANCE.maxCartCount){
            fragmentManager?.let { context?.let { it1 -> MECutility.showErrorDialog(it1, it, getString(R.string.mec_ok), getString(R.string.mec_exceed_cart_limit), getString(R.string.mec_cannot_add)+ MECDataHolder.INSTANCE.maxCartCount + getString(R.string.mec_product_in_cart)) } }
        } else {
            createCustomProgressBar(container, MEDIUM)
            addressViewModel.fetchAddresses()
        }
    }

    fun gotoProductCatalog() {
        showProductCatalogFragment(TAG)
    }

    override fun onItemClick(item: Any) {
        removeVoucher = true
        showDialog()
    }

    fun disableButton() {
        binding.mecContinueCheckoutBtn.isEnabled = false
    }



    override fun processError(mecError: MecError?, bool: Boolean) {
        MECDataHolder.INSTANCE.voucherCode = "invalid_code"
        if (mecError!!.mECRequestType == MECRequestType.MEC_APPLY_VOUCHER) {
            super.processError(mecError, false)
            if (mecError!!.exception!!.message.toString().contentEquals(getString(R.string.mec_invalid_voucher_error))) {
                validationEditText = null
                binding.mecVoucherEditText.startAnimation(addressViewModel.shakeError())
                binding.llAddVoucher.showError()
                validationEditText?.requestFocus()
            }
        }else{
            super.processError(mecError, true)
        }
    }
}
