package com.philips.cdp.di.mec.screens.address


import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.philips.cdp.di.ecs.model.region.ECSRegion
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.platform.uid.view.widget.InputValidationLayout
import com.philips.platform.uid.view.widget.ValidationEditText
import com.philips.cdp.di.ecs.model.address.ECSAddress
import com.philips.cdp.di.ecs.util.ECSConfiguration
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.databinding.MecAddressCreateBinding
import kotlinx.android.synthetic.main.mec_main_activity.*


class AddAddressFragment : MecBaseFragment() {
    override fun getFragmentTag(): String {
        return "AddAddressFragment"
    }


    private var addressFieldEnabler: MECAddressFieldEnabler? = null

    lateinit var binding: MecAddressCreateBinding

    lateinit var addressViewModel: AddressViewModel

    var isError = false
    var validationEditText : ValidationEditText ? =null

    var eCSAddressShipping : ECSAddress? = ECSAddress()

    var eCSAddressBilling : ECSAddress? = ECSAddress()


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
            hideProgressBar()
        }

    }

    override fun onResume() {
        super.onResume()
        setTitleAndBackButtonVisibility(R.string.mec_address, true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = MecAddressCreateBinding.inflate(inflater, container, false)
        binding.pattern = MECRegexPattern()

       //Set Country before binding
        eCSAddressShipping!!.country =addressViewModel.getCountry()
        eCSAddressBilling!!.country = addressViewModel.getCountry()
        binding.ecsAddressShipping = eCSAddressShipping
        binding.ecsAddressBilling = eCSAddressBilling


        addressViewModel = ViewModelProviders.of(this).get(AddressViewModel::class.java)

        addressViewModel.regionsList.observe(this, regionListObserver)
        addressViewModel.mecError.observe(this, this)
        addressViewModel.eCSAddress.observe(this,createAddressObserver)

        addressFieldEnabler = context?.let { addressViewModel.getAddressFieldEnabler(ECSConfiguration.INSTANCE.country, it) }

        binding.addressFieldEnabler = addressFieldEnabler


        binding.btnContinue.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                //Re assign
                isError = false
                validationEditText = null

                validateEditTexts(binding.addressContainer)

                if(isError){
                    validationEditText?.requestFocus()

                }else{

                    // update region properly ..as two way data binding for region is not possible : Shipping
                    val ecsAddressShipping = binding.ecsAddressShipping
                    addressViewModel.setRegion(binding.llShipping,binding.mecRegions,ecsAddressShipping!!)

                    //  // update region properly ..as two way data binding for region is not possible : Billing

                    val ecsAddressBilling = binding.ecsAddressBilling
                    addressViewModel.setRegion(binding.llBilling,binding.mecRegions,ecsAddressBilling!!)


                    addressViewModel.createAddress(ecsAddressShipping!!)
                    createCustomProgressBar(container,MEDIUM)
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
                    if(validationEditText==null) {
                        validationEditText = child
                    }
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



