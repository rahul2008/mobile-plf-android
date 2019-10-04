package com.philips.cdp.di.mec.activity

import android.os.Bundle
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.screens.catalog.MECProductCatalogFragment
import com.philips.platform.uid.utils.UIDActivity

class MECActivity : UIDActivity(){

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)

      setContentView(R.layout.mec_activity)
      loadFragment()
   }

   private fun loadFragment() {

         val newFragment = MECProductCatalogFragment().createInstance(Bundle())
         val fragmentTransaction = supportFragmentManager.beginTransaction()
         fragmentTransaction.replace(R.id.container, newFragment!!).commit()

   }

}

