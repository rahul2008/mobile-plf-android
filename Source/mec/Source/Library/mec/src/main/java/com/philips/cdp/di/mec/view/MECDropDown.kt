/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.view

import android.widget.AdapterView


import com.philips.cdp.di.mec.R
import com.philips.platform.uid.thememanager.UIDHelper
import com.philips.platform.uid.view.widget.UIPicker
import com.philips.platform.uid.view.widget.ValidationEditText

class MECDropDown(private val validationEditText: ValidationEditText, private val dropDownStrings : Array<String>) {

    private var mPopUp: UIPicker? = null

    private var mSalutationListener: DropDownSelectListener? = null

    private val mListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
        val salutation = parent.getItemAtPosition(position) as String

        validationEditText.setText(salutation)

        mSalutationListener?.onDropDownSelect(validationEditText,salutation)

        dismiss()
    }

    private fun isShowing() = mPopUp!!.isShowing

    interface DropDownSelectListener {
        fun onDropDownSelect(validationEditText: ValidationEditText, salutation: String)
    }

    fun setDataChangeListener( salutationListener: DropDownSelectListener){
        mSalutationListener = salutationListener
    }

    fun createPopUp() {
        val popupThemedContext = UIDHelper.getPopupThemedContext(validationEditText.context)
        mPopUp = UIPicker(popupThemedContext)
        mPopUp!!.setAdapter(UIPickerAdapter(popupThemedContext, R.layout.mec_uipicker_item_text, dropDownStrings))
        mPopUp!!.anchorView = validationEditText
        mPopUp!!.isModal = true
        mPopUp!!.setOnItemClickListener(mListener)
    }


    fun show() {
        if (!isShowing()) {
            mPopUp!!.show()
        }
    }

    fun dismiss() {
        mPopUp!!.dismiss()
    }

}