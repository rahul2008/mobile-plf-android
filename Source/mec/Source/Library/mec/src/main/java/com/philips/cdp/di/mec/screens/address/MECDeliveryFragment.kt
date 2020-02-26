package com.philips.cdp.di.mec.screens.address

import android.os.Bundle
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
import com.philips.cdp.di.mec.databinding.MecDeliveryBinding
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.screens.profile.ProfileViewModel
import com.philips.cdp.di.mec.screens.shoppingCart.EcsShoppingCartViewModel
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.cdp.di.mec.utils.MECutility

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
        addressViewModel.setDeliveryMode(item as ECSDeliveryMode)
    }


    fun onEditClick(){

        var editAddressFragment = EditAddressFragment()
        var bundle = Bundle()
        bundle.putSerializable(MECConstant.KEY_ECS_ADDRESS, binding.ecsAddressShipping)
        editAddressFragment.arguments = bundle
        replaceFragment(editAddressFragment,editAddressFragment.getFragmentTag(),true)
    }

    fun fetchDeliveryModes(){
        binding.mecDeliveryProgressbar.visibility=View.VISIBLE
        addressViewModel.fetchDeliveryModes()
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