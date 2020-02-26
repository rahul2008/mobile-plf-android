package com.philips.cdp.di.mec.screens.address

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.address.ECSAddress
import com.philips.cdp.di.ecs.model.address.ECSDeliveryMode
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.common.ItemClickListener
import com.philips.cdp.di.mec.common.MecError
import com.philips.cdp.di.mec.databinding.MecDeliveryBinding
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.cdp.di.mec.utils.MECDataHolder
import java.io.Serializable

class MECDeliveryFragment : MecBaseFragment(), ItemClickListener {


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
        mECDeliveryModesAdapter?.notifyDataSetChanged()

    })

    lateinit var binding:MecDeliveryBinding
    private var mECDeliveryModesAdapter :MECDeliveryModesAdapter ?=null
    private lateinit var mECSDeliveryModeList : MutableList<ECSDeliveryMode>

    lateinit var ecsAddresses:List<ECSAddress>
    private lateinit var addressViewModel: AddressViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = MecDeliveryBinding.inflate(inflater, container, false)

        addressViewModel = let { ViewModelProviders.of(it).get(AddressViewModel::class.java) }

        addressViewModel.ecsDeliveryModes.observe(this, ecsDeliveryModesObserver)
        addressViewModel.mecError.observe(this,this)

        ecsAddresses = arguments?.getSerializable(MECConstant.KEY_ECS_ADDRESSES) as List<ECSAddress>
        mECSDeliveryModeList= mutableListOf()
        mECDeliveryModesAdapter = MECDeliveryModesAdapter(mECSDeliveryModeList,this)
        binding.mecDeliveryModeRecyclerView.adapter=mECDeliveryModesAdapter

        binding.ecsAddressShipping = ecsAddresses[0]

        binding.tvShippingAddressName.setOnClickListener(object:View.OnClickListener{

            override fun onClick(v: View?) {
                onEditClick()
            }
        })

        binding.tvManageAddress.setOnClickListener(object:View.OnClickListener{

            override fun onClick(v: View?) {
                onManageAddressClick()
            }
        })

        fetchDeliveryModes()
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

            val eCSSetDeliveryModeCallback: ECSCallback<Boolean, Exception> = object : ECSCallback<Boolean, Exception> {

                override fun onResponse(result: Boolean?) {
                    binding.mecDeliveryProgressbar.visibility = View.GONE
                }

                override fun onFailure(error: Exception?, ecsError: ECSError?) {
                    binding.mecDeliveryProgressbar.visibility = View.GONE
                }
            }

            addressViewModel.setDeliveryMode(item as ECSDeliveryMode, eCSSetDeliveryModeCallback)
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

        //Testing ..adding 6 element
        val toMutableList = ecsAddresses.toMutableList()
        toMutableList.addAll(ecsAddresses)
        toMutableList.addAll(ecsAddresses)

        bundle.putSerializable(MECConstant.KEY_ECS_ADDRESSES, toMutableList.toList() as Serializable)
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
}