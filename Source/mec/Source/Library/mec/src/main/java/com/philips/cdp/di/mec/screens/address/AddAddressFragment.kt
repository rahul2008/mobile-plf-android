package com.philips.cdp.di.mec.screens.address

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.philips.cdp.di.ecs.model.region.ECSRegion
import com.philips.cdp.di.mec.databinding.MecAddressBinding
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.platform.uid.view.widget.InputValidationLayout
import com.philips.platform.uid.view.widget.ValidationEditText
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
import android.widget.*
import com.philips.cdp.di.ecs.model.address.ECSAddress
import com.philips.cdp.di.ecs.util.ECSConfiguration
import kotlinx.android.synthetic.main.mec_main_activity.*


class AddAddressFragment : MecBaseFragment() {


    private var addressFieldEnabler: MECAddressFieldEnabler? = null

    lateinit var binding: MecAddressBinding

    lateinit var addressViewModel: AddressViewModel

    var isError = false

    var eCSAddressShipping : ECSAddress? = null

    var eCSAddressBilling : ECSAddress? = null


    private val regionListObserver: Observer<List<ECSRegion>> = object : Observer<List<ECSRegion>> {

        override fun onChanged(regionList: List<ECSRegion>?) {

            val mecRegions = MECRegions(regionList!!)
            binding.mecRegions = mecRegions
            hideProgressBar()
        }

    }

    private val createAddressObserver: Observer<ECSAddress> = object : Observer<ECSAddress> {

        override fun onChanged(ecsAddress: ECSAddress?) {

           Log.d(this@AddAddressFragment.javaClass.name, ecsAddress?.id)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = MecAddressBinding.inflate(inflater, container, false)
        binding.pattern = MECRegexPattern()

        addressViewModel = ViewModelProviders.of(this).get(AddressViewModel::class.java)

        addressViewModel.regionsList.observe(this, regionListObserver)
        addressViewModel.mecError.observe(this, this)
        addressViewModel.eCSAddress.observe(this,createAddressObserver)

        addressFieldEnabler = context?.let { addressViewModel.getAddressFieldEnabler(ECSConfiguration.INSTANCE.country, it) }

        binding.addressFieldEnabler = addressFieldEnabler


        binding.btnContinue.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                isError = false
                validateEditTexts(binding.addressContainer)

                if(!isError){

                    eCSAddressShipping = addressViewModel.getECSAddress(binding.llShipping,binding.mecRegions)
                    eCSAddressBilling = addressViewModel.getECSAddress(binding.llBilling,binding.mecRegions)
                    addressViewModel.createAddress(eCSAddressShipping!!)
                }
            }
        })

        return binding.root
    }

    private fun validateEditTexts(v: ViewGroup){

        for (i in 0 until v.childCount) {
            val child = v.getChildAt(i)

            if (v is InputValidationLayout && child is ValidationEditText && child.visibility == View.VISIBLE) {

                Log.d("MEC",child.hint.toString())

                var validator:InputValidationLayout.Validator

                if(child.inputType == InputType.TYPE_CLASS_PHONE){
                    validator = PhoneNumberInputValidator(MECRegexPattern().PHONE_NUMBER_PATTERN)
                }else{
                    validator = EmptyInputValidator()
                }

                if(!validator.validate(child.text.toString())){
                    child.startAnimation(addressViewModel.shakeError())
                    v.showError()
                    isError = true
                }

            } else if (child is ViewGroup) {

                if(child.visibility == View.VISIBLE) {
                    validateEditTexts(child)  // Recursive call.
                }

            }
        }
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(addressFieldEnabler?.isStateEnabled!!) {
            addressViewModel.fetchRegions()
            createCustomProgressBar(container,MEDIUM)
        }
    }


}



