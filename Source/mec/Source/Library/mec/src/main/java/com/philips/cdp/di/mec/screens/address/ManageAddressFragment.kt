package com.philips.cdp.di.mec.screens.address

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.philips.cdp.di.ecs.model.address.ECSAddress
import com.philips.cdp.di.mec.common.ItemClickListener
import com.philips.cdp.di.mec.common.MecError
import com.philips.cdp.di.mec.databinding.MecAddressManageBinding
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.cdp.di.mec.utils.MECutility
import com.philips.platform.uid.view.widget.ProgressBarWithLabel
import kotlinx.android.synthetic.main.mec_address_manage.view.*
import java.io.Serializable


class ManageAddressFragment : BottomSheetDialogFragment(){


    private lateinit var addressViewModel: AddressViewModel
    private lateinit var binding: MecAddressManageBinding

    private lateinit var mecAddresses: MECAddresses

    private lateinit var addressBottomSheetRecyclerAdapter : AddressBottomSheetRecyclerAdapter

    private lateinit var defaultAddressId : String

    companion object {
        val TAG:String="ManageAddressFragment"
    }

    private val fetchAddressObserver: Observer<List<ECSAddress>> = Observer(fun(addressList: List<ECSAddress>?) {

        val intent = Intent()
        val bundle = Bundle()
        bundle.putSerializable(MECConstant.KEY_ECS_ADDRESSES,addressList as Serializable)
        intent.putExtra(MECConstant.BUNDLE_ADDRESSES,bundle)
        targetFragment?.onActivityResult(MECConstant.REQUEST_CODE_ADDRESSES, Activity.RESULT_OK,intent)
        dismissProgressBar(binding.mecProgress.mecProgressBarContainer)
        dismiss()
    })

    private val errorObserver : Observer<MecError>  = Observer(fun( mecError: MecError?) {
        dismissProgressBar(binding.mecProgress.mecProgressBarContainer)
        Log.d(TAG ,"Error on deleting or setting address" )
        MECutility.tagAndShowError(mecError,true,fragmentManager,context)

    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = MecAddressManageBinding.inflate(inflater, container, false)

        addressViewModel = ViewModelProviders.of(this).get(AddressViewModel::class.java)
        activity?.let { addressViewModel.ecsAddresses.observe(it,fetchAddressObserver) }
        addressViewModel.mecError.observe(this,errorObserver)

        var ecsAddresses = arguments?.getSerializable(MECConstant.KEY_ECS_ADDRESSES) as List<ECSAddress>
        defaultAddressId = arguments?.getSerializable(MECConstant.KEY_MEC_DEFAULT_ADDRESSES_ID) as String
        var itemClickListener = arguments?.getSerializable(MECConstant.KEY_ITEM_CLICK_LISTENER) as ItemClickListener

        mecAddresses = MECAddresses(ecsAddresses)

        //if only one Address is there , make the delete button disable
        if(ecsAddresses.size == 1){
            binding.root.mec_btn_delete_address.isEnabled = false
        }

        addressBottomSheetRecyclerAdapter = AddressBottomSheetRecyclerAdapter(mecAddresses, defaultAddressId,itemClickListener)
        addressBottomSheetRecyclerAdapter.setDefaultSelectedAddressAndPosition()
        binding.recyclerView.adapter = addressBottomSheetRecyclerAdapter



        binding.mecBtnDeleteAddress.setOnClickListener {
            showProgressBar(binding.mecProgress.mecProgressBarContainer)
            val mSelectedAddress = addressBottomSheetRecyclerAdapter.mSelectedAddress
            addressViewModel.deleteAndFetchAddress(mSelectedAddress)
        }

        binding.mecBtnSetAddress.setOnClickListener {
            showProgressBar(binding.mecProgress.mecProgressBarContainer)
            val mSelectedAddress = addressBottomSheetRecyclerAdapter.mSelectedAddress
            addressViewModel.setAndFetchDeliveryAddress(mSelectedAddress)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissProgressBar(binding.mecProgress.mecProgressBarContainer)
    }


    fun showProgressBar(mecProgressBar: FrameLayout?) {
        mecProgressBar?.visibility = View.VISIBLE
        if (activity != null) {
            activity?.getWindow()?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    fun dismissProgressBar(mecProgressBar: FrameLayout?){
        mecProgressBar?.visibility = View.GONE
        if (activity != null) {
            activity?.getWindow()?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

}
