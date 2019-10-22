package com.philips.cdp.di.mec.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.databinding.MecActivityBinding
import com.philips.cdp.di.mec.screens.catalog.MECProductCatalogFragment
import com.philips.platform.uid.utils.UIDActivity
import kotlinx.android.synthetic.main.mec_activity.*


class MECActivity : UIDActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)

       DataBindingUtil.setContentView<MecActivityBinding>(this, R.layout.mec_activity)

       setSupportActionBar(toolbar)
       getSupportActionBar()?.setDisplayShowTitleEnabled(false)

       iap_iv_header_back_button.setOnClickListener(this)

       loadFragment()
   }

    override fun onClick(v: View?) {
        onBackPressed()
    }

   private fun loadFragment() {
         val newFragment = MECProductCatalogFragment().createInstance(Bundle())
         val fragmentTransaction = supportFragmentManager.beginTransaction()
         fragmentTransaction.replace(R.id.container, newFragment!!).commit()

   }

}



