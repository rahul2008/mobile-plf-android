package com.philips.cdp.di.mec.screens.address

import android.os.Bundle
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
import com.philips.cdp.di.mec.databinding.MecDeliveryBinding
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.utils.MECConstant

class MECDeliveryFragment : MecBaseFragment(), ItemClickListener {


    override fun getFragmentTag(): String {
        return "MECDeliveryFragment"
    }

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
        val eCSSetDeliveryModeCallback:ECSCallback<Boolean, Exception> = object: ECSCallback<Boolean, Exception>{

            override fun onResponse(result: Boolean?) {
                binding.mecDeliveryProgressbar.visibility=View.GONE
            }

            override fun onFailure(error: Exception?, ecsError: ECSError?) {
                binding.mecDeliveryProgressbar.visibility=View.GONE
            }
        }

        addressViewModel.setDeliveryMode(item as ECSDeliveryMode,eCSSetDeliveryModeCallback)
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
}