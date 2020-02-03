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
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Spinner
import com.philips.cdp.di.ecs.model.address.Country
import com.philips.cdp.di.ecs.model.address.ECSAddress
import com.philips.cdp.di.ecs.util.ECSConfiguration
import com.philips.cdp.di.mec.utils.MECDataHolder
import kotlinx.android.synthetic.main.mec_enter_address.view.*
import java.util.*


class AddAddressFragment : MecBaseFragment() {


    lateinit var binding: MecAddressBinding

    lateinit var addressViewModel: AddressViewModel

    var isError = false

    var eCSAddressShipping : ECSAddress? = null

    var eCSAddressBilling : ECSAddress? = null


    private val regionListObserver: Observer<List<ECSRegion>> = object : Observer<List<ECSRegion>> {

        override fun onChanged(regionList: List<ECSRegion>?) {

            val mecRegions = MECRegions(regionList!!)
            binding.mecRegions = mecRegions
        }

    }

    private val createAddressObserver: Observer<ECSAddress> = object : Observer<ECSAddress> {

        override fun onChanged(ecsAddress: ECSAddress?) {

           Log.d(this@AddAddressFragment.javaClass.name, ecsAddress?.id)

            // jump to next fragment
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = MecAddressBinding.inflate(inflater, container, false)
        binding.pattern = MECRegexPattern()

        addressViewModel = ViewModelProviders.of(this).get(AddressViewModel::class.java)

        addressViewModel.regionsList.observe(this, regionListObserver)
        addressViewModel.mecError.observe(this, this)


        binding.btnContinue.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                isError = false
                validateEditTexts(binding.addressContainer)

                if(!isError){

                    eCSAddressShipping = getECSAddress(binding.llShipping)
                    eCSAddressBilling = getECSAddress(binding.llBilling)
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

                var validator:InputValidationLayout.Validator

                if(child.inputType == InputType.TYPE_CLASS_PHONE){
                    validator = EmptyInputValidator()
                }else{
                    validator = EmptyInputValidator()
                }

                if(!validator.validate(child.text.toString())){
                    child.startAnimation(shakeError())
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
        addressViewModel.fetchRegions()
    }

    private fun shakeError(): TranslateAnimation {
        val shake = TranslateAnimation(0f, 10f, 0f, 0f)
        shake.duration = 500
        shake.interpolator = CycleInterpolator(7f)
        return shake
    }

    fun getECSAddress(linearLayout: LinearLayout): ECSAddress {

        val firstName = linearLayout.et_first_name.text.toString()
        val lastName = linearLayout.et_last_name.text.toString()
       // val countryCode = linearLayout.et_country.text.toString()
        val addressLineOne = linearLayout.et_address_line_one.text.toString()
        val postalCode = linearLayout.et_postal_code.text.toString()
        val phoneOne = linearLayout.et_phone1.text.toString()
        val town = linearLayout.et_town.text.toString()
        val houseNumber = linearLayout.et_house_no.text.toString()
        val title = linearLayout.et_salutation.text.toString()
        val state =linearLayout.et_state.text.toString()


        val ecsAddress = ECSAddress()

        ecsAddress.firstName = firstName
        ecsAddress.lastName = lastName
        ecsAddress.title = title.toLowerCase() // Todo , pass the locale to lower case

        val country = Country()
        country.isocode = ECSConfiguration.INSTANCE.country
        ecsAddress.country = country

        ecsAddress.line1 = addressLineOne
        ecsAddress.postalCode = postalCode
        ecsAddress.phone1 = phoneOne
        ecsAddress.town = town
        ecsAddress.houseNumber = houseNumber
        ecsAddress.region = binding.mecRegions?.getRegion(state)

        return ecsAddress
    }

}



