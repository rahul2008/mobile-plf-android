package com.philips.cdp.di.mec.screens.address

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.support.graphics.drawable.VectorDrawableCompat
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import com.philips.cdp.di.ecs.model.address.ECSAddress
import com.philips.cdp.di.ecs.model.region.ECSRegion
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.common.CommonViewModel
import com.philips.cdp.di.mec.integration.MecHolder
import com.philips.cdp.di.mec.utils.MECDataHolder
import com.philips.cdp.di.mec.view.MECDropDown
import com.philips.platform.uid.view.widget.CheckBox
import com.philips.platform.uid.view.widget.InputValidationLayout
import com.philips.platform.uid.view.widget.ValidationEditText
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.regex.Pattern

class AddressViewModel : CommonViewModel() {


    private var ecsRegionListCallback = ECSRegionListCallback(this)

    private var ecsCreateAddressCallBack = ECSCreateAddressCallBack(this)

    var ecsServices = MecHolder.INSTANCE.eCSServices

    var addressRepository = AddressRepository(ecsServices)

    var regionsList = MutableLiveData<List<ECSRegion>>()

    var eCSAddress = MutableLiveData<ECSAddress>()

    fun fetchRegions() {
        addressRepository.getRegions(ecsRegionListCallback)
    }

    fun createAddress(ecsAddress: ECSAddress){
        addressRepository.createAddress(ecsAddress,ecsCreateAddressCallBack)
    }



    private fun getAddressFieldEnablerJson(context: Context): String {

        val manager = context.assets
        val file = manager.open("mec_address_config.json")
        val formArray = ByteArray(file.available())
        file.read(formArray)
        file.close()
        return String(formArray)
    }

    fun getAddressFieldEnabler(country: String , context: Context): MECAddressFieldEnabler? {

        var addressFieldEnablerJson: String? = null
        var addressFieldEnabler: MECAddressFieldEnabler? = null
        try {
            addressFieldEnablerJson = getAddressFieldEnablerJson(context)

            var jsonObject: JSONObject? = null

            jsonObject = JSONObject(addressFieldEnablerJson)

            val addressEnablerJsonObject = jsonObject.getJSONObject("excludedFields")
            addressFieldEnabler = MECAddressFieldEnabler()

            val jsonArray = addressEnablerJsonObject.getJSONArray(country)


            for (i in 0 until jsonArray.length()) {
                val excludedField = jsonArray.getString(i)
                val addressFieldJsonEnum = AddressFieldJsonEnum.getAddressFieldJsonEnumFromField(excludedField)
                setAddressFieldEnabler(addressFieldEnabler, addressFieldJsonEnum!!)
            }

        } catch (e: JSONException) {

        } catch (e: IOException) {

        }

        return addressFieldEnabler
    }

    private fun setAddressFieldEnabler(addressFieldEnabler: MECAddressFieldEnabler, addressFieldJsonEnum: AddressFieldJsonEnum) {

        when (addressFieldJsonEnum) {

            AddressFieldJsonEnum.ADDRESS_ONE -> addressFieldEnabler.isAddress1Enabled = false

            AddressFieldJsonEnum.ADDRESS_TWO -> addressFieldEnabler.isAddress2Enabled = false
            AddressFieldJsonEnum.PHONE -> addressFieldEnabler.isPhoneEnabled = false
            AddressFieldJsonEnum.FIRST_NAME -> addressFieldEnabler.isFirstNameEnabled = false
            AddressFieldJsonEnum.LAST_NAME -> addressFieldEnabler.isLastNmeEnabled = false
            AddressFieldJsonEnum.STATE -> addressFieldEnabler.isStateEnabled= false
            AddressFieldJsonEnum.SALUTATION -> addressFieldEnabler.isSalutationEnabled = false
            AddressFieldJsonEnum.COUNTRY -> addressFieldEnabler.isCountryEnabled = false
            AddressFieldJsonEnum.POSTAL_CODE -> addressFieldEnabler.isPostalCodeEnabled = false
            AddressFieldJsonEnum.HOUSE_NUMBER -> addressFieldEnabler.isHouseNumberEnabled =false
            AddressFieldJsonEnum.TOWN -> addressFieldEnabler.isTownEnabled = false
        }

    }



    companion object DataBindingAdapter {

        //for Address

        @JvmStatic
        @BindingAdapter("billingView","scrollView")
        fun enableBillingForm(checkBox: CheckBox, view: LinearLayout , scrollView:ScrollView){

            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                val animate = view.animate()
                animate.duration = 500

                if(isChecked){
                    animate.alpha(0.0f)
                    animate.setListener(object: AnimatorListenerAdapter() {

                        override fun onAnimationEnd(animation: Animator) {

                            view.visibility = View.GONE
                        }
                    })

                } else {
                    animate.alpha(1.0f)
                    animate.setListener(object: AnimatorListenerAdapter() {

                        override fun onAnimationEnd(animation: Animator) {
                            scrollView.scrollTo(0,scrollView.bottom + 500) //TODO
                            view.visibility = View.VISIBLE
                        }
                    })
                }
            }
        }


        @JvmStatic
        @BindingAdapter("phoneNumberRegex")
        fun setPhoneNumberValidator(inputValidationLayout: InputValidationLayout, valid_regex_pattern: Pattern) {
            inputValidationLayout.setValidator(PhoneNumberInputValidator(valid_regex_pattern))
        }

        @JvmStatic
        @BindingAdapter("emptyValidator")
        fun setEmptyValidator(inputValidationLayout: InputValidationLayout, obj:Any?) { // As binding Adapter without parameter not possible

            inputValidationLayout.setValidator(EmptyInputValidator())
        }

        @JvmStatic
        @BindingAdapter("dropDownData")
        fun setDropDown(validationEditText: ValidationEditText, dropDownData:Array<String> ?){

            if (dropDownData.isNullOrEmpty()) return

            validationEditText.setCompoundDrawables(null, null, getImageArrow(validationEditText.context), null)
            val salutationDropDown = MECDropDown(validationEditText,dropDownData)

            salutationDropDown.createPopUp()
            validationEditText.setOnTouchListener(fun(v: View, event: MotionEvent): Boolean {
                salutationDropDown.show()
                return false
            })
        }

        private fun getImageArrow(mContext: Context): Drawable {
            val width = mContext.resources.getDimension(R.dimen.mec_drop_down_icon_width_size).toInt()
            val height = mContext.resources.getDimension(R.dimen.mec_drop_down_icon_height_size).toInt()
            val imageArrow = VectorDrawableCompat.create(mContext.resources, R.drawable.mec_product_count_drop_down, mContext.theme)
            imageArrow!!.setBounds(0, 0, width, height)
            return imageArrow
        }


        @JvmStatic
        @BindingAdapter("firstName")
        fun setFirstName(validationEditText: ValidationEditText, obj:Any?){

            val firstName = MECDataHolder.INSTANCE.getUserInfo().firstName
            if(!firstName.isNullOrEmpty() && !firstName.equals("null",true)){
                validationEditText.setText(firstName)
            }

        }

        @JvmStatic
        @BindingAdapter("lastName")
        fun setLastName(validationEditText: ValidationEditText, obj:Any?){

            val lastName = MECDataHolder.INSTANCE.getUserInfo().lastName
            if(!lastName.isNullOrEmpty() && !lastName.equals("null",true)){
                validationEditText.setText(lastName)
            }

        }

    }

    enum class AddressFieldJsonEnum (val addressField: String) {

        SALUTATION("salutation"),
        FIRST_NAME("firstName"),
        LAST_NAME("lastName"),
        EMAIL("email"),
        PHONE("phone"),
        HOUSE_NUMBER("houseNumber"),
        ADDRESS_ONE("address1"),
        ADDRESS_TWO("address2"),
        TOWN("town"),
        POSTAL_CODE("postalCode"),
        STATE("state"),
        COUNTRY("country");


        companion object {

            fun getAddressFieldJsonEnumFromField(field: String): AddressFieldJsonEnum? {
                val values = AddressFieldJsonEnum.values()

                for (addressFieldJsonEnum in values) {

                    if (addressFieldJsonEnum.addressField.equals(field.trim { it <= ' ' }, ignoreCase = true)) {
                        return addressFieldJsonEnum
                    }
                }
                return null
            }
        }
    }
}