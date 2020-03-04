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
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.common.ItemClickListener
import com.philips.cdp.di.mec.common.MecError
import com.philips.cdp.di.mec.databinding.MecDeliveryBinding
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.screens.orderSummary.MECOrderSummaryFragment
import com.philips.cdp.di.mec.screens.profile.ProfileViewModel
import com.philips.cdp.di.mec.screens.shoppingCart.EcsShoppingCartViewModel
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.cdp.di.mec.utils.MECDataHolder
import com.philips.cdp.di.mec.utils.MECutility
import java.io.Serializable

class MECDeliveryFragment : MecBaseFragment(), ItemClickListener {


    var mRootView: View? = null
    private lateinit var profileViewModel: ProfileViewModel
    lateinit var binding: MecDeliveryBinding
    private var mECDeliveryModesAdapter: MECDeliveryModesAdapter? = null
    private lateinit var mECSDeliveryModeList: MutableList<ECSDeliveryMode>
    lateinit var ecsShoppingCartViewModel: EcsShoppingCartViewModel

    lateinit var ecsAddresses: List<ECSAddress>
    lateinit var mECSShoppingCart: ECSShoppingCart

    private lateinit var addressViewModel: AddressViewModel

    override fun getFragmentTag(): String {
        return "MECDeliveryFragment"
    }

    private var bottomSheetFragment: ManageAddressFragment? = null


    private val ecsDeliveryModesObserver: Observer<List<ECSDeliveryMode>> = Observer  (fun(eCSDeliveryMode: List<ECSDeliveryMode>?) {
        dismissProgressBar(binding.mecProgress.mecProgressBarContainer)
        mECSDeliveryModeList.clear()
        if (eCSDeliveryMode != null && eCSDeliveryMode.size > 0) {
            eCSDeliveryMode?.let { mECSDeliveryModeList.addAll(it) }
        }
        if(null!=mECSShoppingCart.deliveryMode){
            mECDeliveryModesAdapter?.setSelectedDeliveryModeAsCart(mECSShoppingCart.deliveryMode)
        }
        mECDeliveryModesAdapter?.notifyDataSetChanged()

    })

    private val ecsSetDeliveryModeObserver: Observer<Boolean> = Observer {boolean->

        ecsShoppingCartViewModel.getShoppingCart()

    }

    private val fetchProfileObserver: Observer<ECSUserProfile> = Observer { userProfile ->
        dismissProgressBar(binding.mecProgress.mecProgressBarContainer)
        var   profileECSAddress : ECSAddress? = null

        if(userProfile.defaultAddress != null) {
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

        Log.d("Pabitra" , "In addressObserver")

        ecsAddresses = addressList!!
        ecsShoppingCartViewModel.getShoppingCart()

    })

    private val cartObserver: Observer<ECSShoppingCart> = Observer<ECSShoppingCart> { ecsShoppingCart ->
        dismissProgressBar(binding.mecProgress.mecProgressBarContainer)
        mECSShoppingCart=ecsShoppingCart

        if(mECSShoppingCart.deliveryAddress!=null) {

            val findGivenAddressInAddressList = MECutility.findGivenAddressInAddressList(mECSShoppingCart.deliveryAddress.id, ecsAddresses)

            if(findGivenAddressInAddressList!=null) binding.ecsAddressShipping = findGivenAddressInAddressList

            if (null != mECSDeliveryModeList && !mECSDeliveryModeList.isNullOrEmpty()) {
                // if delivery modes are already fetched
                mECDeliveryModesAdapter?.setSelectedDeliveryModeAsCart(mECSShoppingCart.deliveryMode)
                mECDeliveryModesAdapter?.notifyDataSetChanged()
            } else {
                fetchDeliveryModes()
            }
        }else{
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


            //observe addressViewModel
            addressViewModel.mecError.observe(this, this)
            activity?.let { addressViewModel.ecsAddresses.observe(it, addressObserver) }
            addressViewModel.ecsDeliveryModes.observe(this, ecsDeliveryModesObserver)
            addressViewModel.ecsDeliveryModeSet.observe(this, ecsSetDeliveryModeObserver)


            //observe ProfileViewModel
            profileViewModel.userProfile.observe(this, fetchProfileObserver)
            profileViewModel.mecError.observe(this,this)


            //observe ecsShoppingCartViewModel
            ecsShoppingCartViewModel.ecsShoppingCart.observe(this, cartObserver)
            ecsShoppingCartViewModel.mecError.observe(this,this)


            ecsAddresses = arguments?.getSerializable(MECConstant.KEY_ECS_ADDRESSES) as List<ECSAddress>
            mECSDeliveryModeList = mutableListOf()

            mECDeliveryModesAdapter = MECDeliveryModesAdapter(mECSDeliveryModeList, this)
            binding.mecDeliveryModeRecyclerView.adapter = mECDeliveryModesAdapter

            ecsAddresses = arguments?.getSerializable(MECConstant.KEY_ECS_ADDRESSES) as List<ECSAddress>
            mECSShoppingCart = arguments?.getSerializable(MECConstant.KEY_ECS_SHOPPING_CART) as ECSShoppingCart


            binding.ecsAddressShipping = ecsAddresses[0]

            binding.mecAddressEditIcon.setOnClickListener { onEditClick() }

            binding.tvManageAddress.setOnClickListener { onManageAddressClick() }

            binding.mecDeliveryFragment = this
            mRootView = binding.root
            checkDeliveryAddressSet()
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
        editAddressFragment.setTargetFragment(this,MECConstant.REQUEST_CODE_ADDRESSES)
        var bundle = Bundle()
        bundle.putSerializable(MECConstant.KEY_ECS_ADDRESS, ecsAddress)
        editAddressFragment.arguments = bundle
        replaceFragment(editAddressFragment, editAddressFragment.getFragmentTag(), true)
    }

    private fun onManageAddressClick() {
        val bundle = Bundle()

        bottomSheetFragment = ManageAddressFragment()
        bottomSheetFragment?.setTargetFragment(this,MECConstant.REQUEST_CODE_ADDRESSES)
        bundle.putSerializable(MECConstant.KEY_ECS_ADDRESSES, ecsAddresses as Serializable)
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
        if(null != mECSShoppingCart.deliveryAddress) {
             findGivenAddressInAddressList = MECutility.findGivenAddressInAddressList(mECSShoppingCart.deliveryAddress.id, ecsAddresses)
        }
        if ( null != findGivenAddressInAddressList) {
            // if shopping cart has delivery address and its ID is matching with one of the fetched address list
            binding.ecsAddressShipping = findGivenAddressInAddressList
            fetchDeliveryModes()
        }else {

            profileViewModel.fetchUserProfile()

        }

    }

    private fun setAndFetchDeliveryAddress(ecsAddress: ECSAddress){
        showProgressBar(binding.mecProgress.mecProgressBarContainer)
        addressViewModel.setAndFetchDeliveryAddress(ecsAddress!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode == MECConstant.REQUEST_CODE_ADDRESSES){
            val bundleExtra = data?.getBundleExtra(MECConstant.BUNDLE_ADDRESSES)
            ecsAddresses = bundleExtra?.getSerializable(MECConstant.KEY_ECS_ADDRESSES) as List<ECSAddress>
            showProgressBar(binding.mecProgress.mecProgressBarContainer)
            ecsShoppingCartViewModel.getShoppingCart()
        }
    }

    fun onOrderSummaryClick(){
        val mecOrderSummaryFragment = MECOrderSummaryFragment()
        replaceFragment(mecOrderSummaryFragment,MECOrderSummaryFragment.TAG, true)
    }
}