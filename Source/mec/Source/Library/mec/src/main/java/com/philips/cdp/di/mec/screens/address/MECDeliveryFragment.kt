package com.philips.cdp.di.mec.screens.address

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
import com.philips.cdp.di.mec.screens.profile.ProfileViewModel
import com.philips.cdp.di.mec.screens.shoppingCart.EcsShoppingCartViewModel
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.cdp.di.mec.utils.MECDataHolder
import com.philips.cdp.di.mec.utils.MECutility
import java.io.Serializable

class MECDeliveryFragment : MecBaseFragment(), ItemClickListener {


    var mRootView: View? = null
    private lateinit var profileViewModel: ProfileViewModel
    lateinit var binding:MecDeliveryBinding
    private var mECDeliveryModesAdapter :MECDeliveryModesAdapter ?=null
    private lateinit var mECSDeliveryModeList : MutableList<ECSDeliveryMode>
    lateinit var ecsShoppingCartViewModel: EcsShoppingCartViewModel

    lateinit var ecsAddresses:List<ECSAddress>
    lateinit var mECSShoppingCart: ECSShoppingCart

    private lateinit var addressViewModel: AddressViewModel

    override fun getFragmentTag(): String {
        return "MECDeliveryFragment"
    }

    private  var bottomSheetFragment: ManageAddressFragment? = null

    private val ecsDeliveryModesObserver: Observer<List<ECSDeliveryMode>> = Observer  (fun(eCSDeliveryMode: List<ECSDeliveryMode>?) {
        binding.mecDeliveryProgressbar.visibility=View.GONE
        mECSDeliveryModeList.clear()
        if (eCSDeliveryMode != null && eCSDeliveryMode.size>0) {
            eCSDeliveryMode?.let { mECSDeliveryModeList.addAll(it) }
        }
        mECDeliveryModesAdapter?.setSelectedDeliveryModeAsCart(mECSShoppingCart.deliveryMode)
        mECDeliveryModesAdapter?.notifyDataSetChanged()

    })

    private val ecsSetDeliveryModeObserver: Observer<Boolean> = Observer {boolean->
        binding.mecDeliveryProgressbar.visibility=View.GONE
        ecsShoppingCartViewModel.getShoppingCart()

    }

    private val fetchProfileObserver: Observer<ECSUserProfile> = Observer { userProfile ->


        val profileECSAddress = MECutility.findGivenAddressInAddressList(userProfile.defaultAddress.id,ecsAddresses)
        if (userProfile.defaultAddress != null &&  profileECSAddress != null) {
            //if userProfile  delivery address and its ID is matching with one of the fetched address list
            setAndFetchDeliveryAddress(profileECSAddress)
        }else{
            //if delivery address  not present neither in Cart nor in user profile then set first address of fetched list
            setAndFetchDeliveryAddress(ecsAddresses[0])
        }

    }

    private val addressObserver: Observer<List<ECSAddress>> = Observer(fun(addressList: List<ECSAddress>?) {
        binding.mecDeliveryProgressbar.visibility=View.GONE
        ecsAddresses = addressList!!
        ecsShoppingCartViewModel.getShoppingCart()

    })

    private val cartObserver: Observer<ECSShoppingCart> = Observer<ECSShoppingCart> { ecsShoppingCart ->
        binding.mecDeliveryProgressbar.visibility=View.GONE
        mECSShoppingCart=ecsShoppingCart
        if(null!=mECSDeliveryModeList && !mECSDeliveryModeList.isNullOrEmpty()){
            // if delivery modes are already fetched
            mECDeliveryModesAdapter?.setSelectedDeliveryModeAsCart(mECSShoppingCart.deliveryMode)
            mECDeliveryModesAdapter?.notifyDataSetChanged()
        }else {
            fetchDeliveryModes()
        }
    }





    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        if (null == mRootView) {
            binding = MecDeliveryBinding.inflate(inflater, container, false)

        addressViewModel = let { ViewModelProviders.of(it).get(AddressViewModel::class.java) }

        addressViewModel.ecsDeliveryModes.observe(this, ecsDeliveryModesObserver)
        addressViewModel.mecError.observe(this,this)

        ecsAddresses = arguments?.getSerializable(MECConstant.KEY_ECS_ADDRESSES) as List<ECSAddress>
        mECSDeliveryModeList= mutableListOf()
        mECDeliveryModesAdapter = MECDeliveryModesAdapter(mECSDeliveryModeList,this)
        binding.mecDeliveryModeRecyclerView.adapter=mECDeliveryModesAdapter


            ecsAddresses = arguments?.getSerializable(MECConstant.KEY_ECS_ADDRESSES) as List<ECSAddress>
            mECSShoppingCart = arguments?.getSerializable(MECConstant.KEY_ECS_SHOPPING_CART) as ECSShoppingCart
            mECSDeliveryModeList = mutableListOf()
            profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
            profileViewModel.userProfile.observe(this, fetchProfileObserver)

            addressViewModel = let { ViewModelProviders.of(it).get(AddressViewModel::class.java) }
            addressViewModel.ecsDeliveryModes.observe(this, ecsDeliveryModesObserver)
            addressViewModel.ecsDeliveryModeSet.observe(this, ecsSetDeliveryModeObserver)

            addressViewModel.ecsAddresses.observe(this, addressObserver)

            ecsShoppingCartViewModel = ViewModelProviders.of(this).get(EcsShoppingCartViewModel::class.java)
            ecsShoppingCartViewModel.ecsShoppingCart.observe(this, cartObserver)


            mECDeliveryModesAdapter = MECDeliveryModesAdapter(mECSDeliveryModeList, this)
            binding.mecDeliveryModeRecyclerView.adapter = mECDeliveryModesAdapter

            binding.ecsAddressShipping = ecsAddresses[0]

            binding.tvShippingAddressName.setOnClickListener(object : View.OnClickListener {

                override fun onClick(v: View?) {
                    onEditClick()
                }
            })

        binding.tvManageAddress.setOnClickListener(object:View.OnClickListener{

            override fun onClick(v: View?) {
                onManageAddressClick()
            }
        })


            mRootView=binding.root
            checkDeliveryAddressSet()
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setTitleAndBackButtonVisibility(R.string.mec_delivery, true)
        setCartIconVisibility(false)
    }

    override fun onItemClick(item: Any) {


        binding.mecDeliveryProgressbar.visibility=View.VISIBLE

        if(item is ECSDeliveryMode) {
            addressViewModel.setDeliveryMode(item as ECSDeliveryMode)

        }
        if(item is String && item.equals(MECConstant.CREATE_ADDRESS,true)){

            //dismiss the bottom sheet fragment

            if(bottomSheetFragment!=null){
                if(bottomSheetFragment?.isVisible!!){
                    bottomSheetFragment?.dismiss()
                }
            }

            Log.d("ADDRESS","CREATE_ADDRESS")
            // create Address ======= starts
            val ecsAddress = createNewAddress()
            gotoCreateOrEditAddress(ecsAddress)
        }


    }






    private fun createNewAddress() : ECSAddress {
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


    fun onEditClick(){

        gotoCreateOrEditAddress(binding.ecsAddressShipping!!)
    }

    private fun gotoCreateOrEditAddress(ecsAddress: ECSAddress) {
        var editAddressFragment = CreateOrEditAddressFragment()
        var bundle = Bundle()
        bundle.putSerializable(MECConstant.KEY_ECS_ADDRESS,ecsAddress)
        editAddressFragment.arguments = bundle
        replaceFragment(editAddressFragment, editAddressFragment.getFragmentTag(), true)
    }

    private fun onManageAddressClick() {
        val bundle = Bundle()

        bottomSheetFragment = ManageAddressFragment()
        bundle.putSerializable(MECConstant.KEY_ECS_ADDRESSES, ecsAddresses as Serializable)
        bundle.putSerializable(MECConstant.KEY_ITEM_CLICK_LISTENER,this)
        bottomSheetFragment?.arguments = bundle
        fragmentManager?.let { bottomSheetFragment?.show(it, bottomSheetFragment?.tag) }

    }

    private fun fetchDeliveryModes(){
        binding.mecDeliveryProgressbar.visibility=View.VISIBLE
        addressViewModel.fetchDeliveryModes()
    }

    override fun processError(mecError: MecError?, showDialog: Boolean) {
        binding.mecDeliveryProgressbar.visibility=View.GONE
        super.processError(mecError, showDialog)
    }

    private fun checkDeliveryAddressSet(){

        if(null!=mECSShoppingCart.deliveryAddress && null!=MECutility.findGivenAddressInAddressList(mECSShoppingCart.deliveryAddress.id,ecsAddresses)){
            // if shopping cart has delivery address and its ID is matching with one of the fetched address list
            fetchDeliveryModes()
        }else {
            binding.mecDeliveryProgressbar.visibility=View.VISIBLE
            profileViewModel.fetchUserProfile()

        }

    }

    private fun setAndFetchDeliveryAddress(ecsAddress: ECSAddress){
        binding.mecDeliveryProgressbar.visibility=View.VISIBLE
        addressViewModel.setAndFetchDeliveryAddress(ecsAddress!!)
    }
}