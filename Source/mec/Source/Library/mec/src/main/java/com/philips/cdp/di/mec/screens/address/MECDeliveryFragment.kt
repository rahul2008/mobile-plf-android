package com.philips.cdp.di.mec.screens.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.philips.cdp.di.ecs.model.address.ECSAddress
import com.philips.cdp.di.ecs.model.address.ECSDeliveryMode
import com.philips.cdp.di.mec.common.ItemClickListener
import com.philips.cdp.di.mec.databinding.MecDeliveryBinding
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.screens.retailers.MECRetailersFragment
import com.philips.cdp.di.mec.utils.MECConstant
import java.io.Serializable

class MECDeliveryFragment : MecBaseFragment(), ItemClickListener {


    override fun getFragmentTag(): String {
        return "MECDeliveryFragment"
    }

    private val ecsDeliveryModesObserver: Observer<List<ECSDeliveryMode>> = Observer  (fun(eCSDeliveryMode: List<ECSDeliveryMode>?) {
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

    override fun onItemClick(item: Any) {
       //todo radio button selection
    }


    fun onEditClick(){

        var editAddressFragment = EditAddressFragment()
        var bundle = Bundle()
        bundle.putSerializable(MECConstant.KEY_ECS_ADDRESS, binding.ecsAddressShipping)
        editAddressFragment.arguments = bundle
        replaceFragment(editAddressFragment,editAddressFragment.getFragmentTag(),true)
    }

    private fun onManageAddressClick() {
        val bundle = Bundle()
        var bottomSheetFragment = ManageAddressFragment()

        //Testing ..adding 6 element
        val toMutableList = ecsAddresses.toMutableList()
        toMutableList.addAll(ecsAddresses)
        toMutableList.addAll(ecsAddresses)

        bundle.putSerializable(MECConstant.KEY_ECS_ADDRESSES, toMutableList.toList() as Serializable)
        bottomSheetFragment.arguments = bundle
        fragmentManager?.let { bottomSheetFragment.show(it, bottomSheetFragment.tag) }

    }

    fun fetchDeliveryModes(){
        addressViewModel.fetchDeliveryModes()
    }
}