package com.philips.cdp.di.mec.screens.address

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.philips.cdp.di.ecs.model.address.ECSAddress
import com.philips.cdp.di.ecs.model.address.ECSDeliveryMode
import com.philips.cdp.di.ecs.model.address.ECSUserProfile
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart
import com.philips.cdp.di.ecs.model.payment.CardType
import com.philips.cdp.di.ecs.model.payment.ECSPayment
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.common.ItemClickListener
import com.philips.cdp.di.mec.common.MecError
import com.philips.cdp.di.mec.databinding.MecDeliveryBinding
import com.philips.cdp.di.mec.payment.MECPayment
import com.philips.cdp.di.mec.payment.MECPayments
import com.philips.cdp.di.mec.payment.PaymentRecyclerAdapter
import com.philips.cdp.di.mec.payment.PaymentViewModel
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.screens.orderSummary.MECOrderSummaryFragment
import com.philips.cdp.di.mec.screens.profile.ProfileViewModel
import com.philips.cdp.di.mec.screens.shoppingCart.EcsShoppingCartViewModel
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.cdp.di.mec.utils.MECDataHolder
import com.philips.cdp.di.mec.utils.MECutility
import java.io.Serializable

class MECDeliveryFragment : MecBaseFragment(), ItemClickListener {


    private lateinit var mutableListOfPayments: MutableList<MECPayment>
    private lateinit var paymentViewModel: PaymentViewModel
    var mRootView: View? = null
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var mecPayment: MECPayment
    private lateinit var binding: MecDeliveryBinding
    private var mECDeliveryModesAdapter: MECDeliveryModesAdapter? = null
    private var mecPaymentAdapter: PaymentRecyclerAdapter? = null
    private lateinit var ecsPayment: ECSPayment
    private lateinit var mECSDeliveryModeList: MutableList<ECSDeliveryMode>
    lateinit var ecsShoppingCartViewModel: EcsShoppingCartViewModel

    lateinit var ecsAddresses: List<ECSAddress>
    lateinit var mECSShoppingCart: ECSShoppingCart

    private lateinit var addressViewModel: AddressViewModel

    var ecsBillingAddress: ECSAddress? = null


    override fun getFragmentTag(): String {
        return "MECDeliveryFragment"
    }

    private var bottomSheetFragment: ManageAddressFragment? = null


    private val ecsDeliveryModesObserver: Observer<List<ECSDeliveryMode>> = Observer(fun(eCSDeliveryMode: List<ECSDeliveryMode>?) {
        dismissProgressBar(binding.mecProgress.mecProgressBarContainer)
        mECSDeliveryModeList.clear()
        if (eCSDeliveryMode != null && eCSDeliveryMode.isNotEmpty()) {
            eCSDeliveryMode?.let { mECSDeliveryModeList.addAll(it) }
        }
        if (null != mECSShoppingCart.deliveryMode) {
            mECDeliveryModesAdapter?.setSelectedDeliveryModeAsCart(mECSShoppingCart.deliveryMode)
        }
        mECDeliveryModesAdapter?.notifyDataSetChanged()
        binding.mecOrderSummaryBtn.visibility = View.VISIBLE
    })

    private val ecsSetDeliveryModeObserver: Observer<Boolean> = Observer { boolean ->

        ecsShoppingCartViewModel.getShoppingCart()

    }

    private val fetchProfileObserver: Observer<ECSUserProfile> = Observer { userProfile ->
        dismissProgressBar(binding.mecProgress.mecProgressBarContainer)
        var profileECSAddress: ECSAddress? = null

        if (userProfile.defaultAddress != null) {
            profileECSAddress = MECutility.findGivenAddressInAddressList(userProfile.defaultAddress.id, ecsAddresses)
        }

        if (profileECSAddress != null) {
            //if userProfile  delivery address and its ID is matching with one of the fetched address list
            setAndFetchDeliveryAddress(profileECSAddress)
            binding.ecsAddressShipping = profileECSAddress
        } else {
            //if delivery address  not present neither in Cart nor in user profile then set first address of fetched list
            setAndFetchDeliveryAddress(ecsAddresses[0])
        }

    }

    private val addressObserver: Observer<List<ECSAddress>> = Observer(fun(addressList: List<ECSAddress>?) {

        Log.d("Pabitra", "In addressObserver")

        ecsAddresses = addressList!!
        ecsShoppingCartViewModel.getShoppingCart()

    })

    private val paymentObserver: Observer<MECPayments> = Observer(fun(mecPayments: MECPayments) {


        /* //Added for testing purpose
         var ecspayment1 = ECSPayment()
         var ecspayment2 = ECSPayment()
         var ecspayment3 = ECSPayment()

         var ecsAddress1 = ECSAddress()
         ecsAddress1.firstName = "VISA"
         ecsAddress1.id = "1233344554545"
         ecspayment1.billingAddress =ecsAddress1


         var ecsAddress2 = ECSAddress()
         ecsAddress2.firstName = "VISA"
         ecsAddress2.id = "1233344554545"
         ecspayment2.billingAddress =ecsAddress2


         var ecsAddress3 = ECSAddress()
         ecsAddress3.firstName = "VISA"
         ecsAddress3.id = "1233344554545"
         ecspayment3.billingAddress =ecsAddress3

         var mecPayment1 = MECPayment(ecspayment1,false)
         var mecPayment2 = MECPayment(ecspayment2,false)
         var mecPayment3 = MECPayment(ecspayment3,false)

         mutableListOfPayments.add(mecPayment1)
         mutableListOfPayments.add(mecPayment2)
         mutableListOfPayments.add(mecPayment3)

         var paymentList = mutableListOfPayments.toList()

         val mecPayments = MECPayments(paymentList, true)

         //===========*/

        binding.mecPayments = mecPayments
        mecPaymentAdapter = PaymentRecyclerAdapter(mecPayments, this)
        binding.mecPaymentRecyclerView.adapter = mecPaymentAdapter
    })

    private val cartObserver: Observer<ECSShoppingCart> = Observer<ECSShoppingCart> { ecsShoppingCart ->
        dismissProgressBar(binding.mecProgress.mecProgressBarContainer)
        mECSShoppingCart = ecsShoppingCart

        onRefreshCart()
    }

    private fun onRefreshCart() {
        if (mECSShoppingCart.deliveryAddress != null) {

            val findGivenAddressInAddressList = MECutility.findGivenAddressInAddressList(mECSShoppingCart.deliveryAddress.id, ecsAddresses)

            if (findGivenAddressInAddressList != null) binding.ecsAddressShipping = findGivenAddressInAddressList

            if (!mECSDeliveryModeList.isNullOrEmpty()) {
                // if delivery modes are already fetched
                mECDeliveryModesAdapter?.setSelectedDeliveryModeAsCart(mECSShoppingCart.deliveryMode)
                mECDeliveryModesAdapter?.notifyDataSetChanged()
            } else {
                fetchDeliveryModes()
            }
        } else {
            checkDeliveryAddressSet()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        if (null == mRootView) {

            binding = MecDeliveryBinding.inflate(inflater, container, false)

            addressViewModel = ViewModelProviders.of(this).get(AddressViewModel::class.java)
            profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
            ecsShoppingCartViewModel = ViewModelProviders.of(this).get(EcsShoppingCartViewModel::class.java)
            paymentViewModel = activity?.let { ViewModelProviders.of(it).get(PaymentViewModel::class.java) }!!


            //observe addressViewModel
            addressViewModel.mecError.observe(this, this)
            activity?.let { addressViewModel.ecsAddresses.observe(it, addressObserver) }
            addressViewModel.ecsDeliveryModes.observe(this, ecsDeliveryModesObserver)
            addressViewModel.ecsDeliveryModeSet.observe(this, ecsSetDeliveryModeObserver)


            //observe ProfileViewModel
            profileViewModel.userProfile.observe(this, fetchProfileObserver)
            profileViewModel.mecError.observe(this, this)


            //observe ecsShoppingCartViewModel
            ecsShoppingCartViewModel.ecsShoppingCart.observe(this, cartObserver)
            ecsShoppingCartViewModel.mecError.observe(this, this)
            paymentViewModel.mecPayments.observe(this, paymentObserver)

            //observe paymentViewmodel
            // activity?.let { paymentViewModel.mecPayments.observe(it,paymentObserver) }
            activity?.let { paymentViewModel.mecError.observe(it, this) }



            ecsAddresses = arguments?.getSerializable(MECConstant.KEY_ECS_ADDRESSES) as List<ECSAddress>
            mECSDeliveryModeList = mutableListOf()

            mECDeliveryModesAdapter = MECDeliveryModesAdapter(mECSDeliveryModeList, this)
            binding.mecDeliveryModeRecyclerView.adapter = mECDeliveryModesAdapter

            ecsAddresses = arguments?.getSerializable(MECConstant.KEY_ECS_ADDRESSES) as List<ECSAddress>
            mECSShoppingCart = arguments?.getSerializable(MECConstant.KEY_ECS_SHOPPING_CART) as ECSShoppingCart
            if (arguments?.getSerializable(MECConstant.KEY_ECS_BILLING_ADDRESS) != null) {
                ecsBillingAddress = arguments?.getSerializable(MECConstant.KEY_ECS_BILLING_ADDRESS) as ECSAddress
            }

            //Create a empty payment list
            mutableListOfPayments = mutableListOf<MECPayment>() //creating empty payment

            if (ecsBillingAddress != null) {
                ecsPayment = ECSPayment()
                ecsPayment.id = MECConstant.NEW_CARD_PAYMENT
                ecsPayment.billingAddress = ecsBillingAddress
                mecPayment = MECPayment(ecsPayment, true)
                mutableListOfPayments.add(mecPayment!!)
            }

            val mecPayments = MECPayments(mutableListOfPayments.toList(), false)
            mecPaymentAdapter = PaymentRecyclerAdapter(mecPayments, this)
            binding.mecPaymentRecyclerView.adapter = mecPaymentAdapter

            // Payment logic ends


            binding.ecsAddressShipping = ecsAddresses[0]

            binding.mecAddressEditIcon.setOnClickListener { onEditClick() }

            binding.tvManageAddress.setOnClickListener { onManageAddressClick() }

            binding.mecDeliveryFragment = this

            mRootView = binding.root
            checkDeliveryAddressSet()

            if (paymentViewModel.mecPayments.value == null || !paymentViewModel.mecPayments.value!!.isPaymentDownloaded) {
                paymentViewModel.fetchPaymentDetails()
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setTitleAndBackButtonVisibility(R.string.mec_delivery, true)
        setCartIconVisibility(false)
    }

    override fun onStop() {
        super.onStop()
        dismissProgressBar(binding.mecProgress.mecProgressBarContainer)
    }


    override fun onItemClick(item: Any) {

        if (item is MECPayment) {
            mecPayment = item
        }
        if (item is ECSDeliveryMode) {
            showProgressBar(binding.mecProgress.mecProgressBarContainer)
            addressViewModel.setDeliveryMode(item as ECSDeliveryMode)

        }

        // When Create Address from BottomSheet is clicked
        if (item is String && item.equals(MECConstant.CREATE_ADDRESS, true)) {

            //dismiss the bottom sheet fragment

            if (bottomSheetFragment != null) {
                if (bottomSheetFragment?.isVisible!!) {
                    bottomSheetFragment?.dismiss()
                }
            }
            Log.d("ADDRESS", "CREATE_ADDRESS")
            // create Address ======= starts
            val ecsAddress = createNewAddress()
            gotoCreateOrEditAddress(ecsAddress)
        }

        // When Create Billing Address  is clicked
        if (item is String && item.equals(MECConstant.CREATE_BILLING_ADDRESS, true)) {
            Log.d("ADDRESS", "CREATE_BILLING_ADDRESS")
            // create Address ======= starts
            val ecsAddress = createNewAddress()
            gotoCreateOrEditBillingAddress(ecsAddress)
        }

        // when Edit Billing Address is clicked
        if (item is MECPayment && item.ecsPayment.id.equals(MECConstant.NEW_CARD_PAYMENT, true)) {
            Log.d("ADDRESS", "CREATE_BILLING_ADDRESS")
            gotoCreateOrEditBillingAddress(item.ecsPayment.billingAddress)
        }


    }

    private fun gotoCreateOrEditBillingAddress(ecsAddress: ECSAddress) {

        var editAddressFragment = AddOrEditBillingAddressFragment()
        editAddressFragment.setTargetFragment(this, MECConstant.REQUEST_CODE_BILLING_ADDRESS)
        var bundle = Bundle()
        bundle.putSerializable(MECConstant.KEY_ECS_ADDRESS, ecsAddress)
        editAddressFragment.arguments = bundle
        replaceFragment(editAddressFragment, editAddressFragment.getFragmentTag(), true)
    }


    private fun createNewAddress(): ECSAddress {
        val ecsAddress = ECSAddress()

        //Set Country before binding
        ecsAddress.country = addressViewModel.getCountry()

        //set First Name
        val firstName = MECDataHolder.INSTANCE.getUserInfo().firstName
        if (!firstName.isNullOrEmpty() && !firstName.equals("null", true)) {
            ecsAddress.firstName = firstName
        }

        //set Last Name
        val lastName = MECDataHolder.INSTANCE.getUserInfo().lastName
        if (!lastName.isNullOrEmpty() && !lastName.equals("null", true)) {
            ecsAddress.lastName = lastName
        }
        return ecsAddress
    }


    fun onEditClick() {
        gotoCreateOrEditAddress(binding.ecsAddressShipping!!)
    }

    private fun gotoCreateOrEditAddress(ecsAddress: ECSAddress) {
        var editAddressFragment = CreateOrEditAddressFragment()
        editAddressFragment.setTargetFragment(this, MECConstant.REQUEST_CODE_ADDRESSES)
        var bundle = Bundle()
        bundle.putSerializable(MECConstant.KEY_ECS_ADDRESS, ecsAddress)
        editAddressFragment.arguments = bundle
        replaceFragment(editAddressFragment, editAddressFragment.getFragmentTag(), true)
    }

    private fun onManageAddressClick() {
        val bundle = Bundle()

        bottomSheetFragment = ManageAddressFragment()
        bottomSheetFragment?.setTargetFragment(this, MECConstant.REQUEST_CODE_ADDRESSES)
        bundle.putSerializable(MECConstant.KEY_ECS_ADDRESSES, ecsAddresses as Serializable)
        bundle.putSerializable(MECConstant.KEY_MEC_DEFAULT_ADDRESSES_ID, binding.ecsAddressShipping?.id)
        bundle.putSerializable(MECConstant.KEY_ITEM_CLICK_LISTENER, this)
        bottomSheetFragment?.arguments = bundle
        fragmentManager?.let { bottomSheetFragment?.show(it, bottomSheetFragment?.tag) }
    }

    private fun fetchDeliveryModes() {
        showProgressBar(binding.mecProgress.mecProgressBarContainer)
        addressViewModel.fetchDeliveryModes()
    }

    override fun processError(mecError: MecError?, showDialog: Boolean) {
        dismissProgressBar(binding.mecProgress.mecProgressBarContainer)

        super.processError(mecError, showDialog)
    }

    private fun checkDeliveryAddressSet() {
        var findGivenAddressInAddressList: ECSAddress? = null
        if (null != mECSShoppingCart.deliveryAddress) {
            findGivenAddressInAddressList = MECutility.findGivenAddressInAddressList(mECSShoppingCart.deliveryAddress.id, ecsAddresses)
        }
        if (null != findGivenAddressInAddressList) {
            // if shopping cart has delivery address and its ID is matching with one of the fetched address list
            binding.ecsAddressShipping = findGivenAddressInAddressList
            fetchDeliveryModes()
        } else {

            profileViewModel.fetchUserProfile()

        }

    }

    private fun setAndFetchDeliveryAddress(ecsAddress: ECSAddress) {
        showProgressBar(binding.mecProgress.mecProgressBarContainer)
        addressViewModel.setAndFetchDeliveryAddress(ecsAddress)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == MECConstant.REQUEST_CODE_ADDRESSES) {
            val bundleExtra = data?.getBundleExtra(MECConstant.BUNDLE_ADDRESSES)
            ecsAddresses = bundleExtra?.getSerializable(MECConstant.KEY_ECS_ADDRESSES) as List<ECSAddress>
            mECSShoppingCart = bundleExtra?.getSerializable(MECConstant.KEY_ECS_SHOPPING_CART) as ECSShoppingCart
            onRefreshCart()
        }

        if (requestCode == MECConstant.REQUEST_CODE_BILLING_ADDRESS) {

            //TODO duplicate issue

            val bundleExtra = data?.getBundleExtra(MECConstant.BUNDLE_BILLING_ADDRESS)
            val ecsBillingAddress = bundleExtra?.getSerializable(MECConstant.KEY_ECS_BILLING_ADDRESS) as ECSAddress
            ecsPayment = ECSPayment()

            ecsPayment.id = MECConstant.NEW_CARD_PAYMENT

            val newCardType = CardType()
            newCardType.name = "New Card"
            ecsPayment.cardType = newCardType
            ecsPayment.billingAddress = ecsBillingAddress

            mutableListOfPayments.add(MECPayment(ecsPayment, true))

            mecPaymentAdapter = PaymentRecyclerAdapter(MECPayments(mutableListOfPayments.toList(), true), this)
            binding.mecPaymentRecyclerView.adapter = mecPaymentAdapter
        }
    }

    fun onOrderSummaryClick() {
        val mecOrderSummaryFragment = MECOrderSummaryFragment()
        var bundle = Bundle()
        bundle.putSerializable(MECConstant.KEY_ECS_ADDRESS, ecsAddresses[0])
        bundle.putSerializable(MECConstant.KEY_ECS_SHOPPING_CART, mECSShoppingCart)
        bundle.putSerializable(MECConstant.MEC_PAYMENT_METHOD, mecPayment)
        mecOrderSummaryFragment.arguments = bundle
        replaceFragment(mecOrderSummaryFragment, MECOrderSummaryFragment.TAG, true)
    }
}