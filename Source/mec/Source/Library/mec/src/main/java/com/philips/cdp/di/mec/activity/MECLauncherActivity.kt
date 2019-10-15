package com.philips.cdp.di.mec.activity

import android.os.Bundle
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.platform.uid.utils.UIDActivity


class MECLauncherActivity : UIDActivity() {
      lateinit var bundle: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.philips.cdp.di.mec.R.layout.mec_activity_launcher)
        bundle = intent.getExtras()
        val landingScreen:Int = bundle.getInt(MECConstant.MEC_LANDING_SCREEN)
       // var mECLaunchInput = bundle.getSerializable("LaunchInput") as MECLaunchInput
        loadDecisionFragment();
    }

    private fun loadDecisionFragment( ){

           // mECFragmentLauncher.setActionBarListener(this, this)
            val mECFragmentLauncher = MECFragmentLauncher()
             mECFragmentLauncher.arguments = bundle
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(com.philips.cdp.di.mec.R.id.container, mECFragmentLauncher, mECFragmentLauncher.TAG)
            transaction.addToBackStack(mECFragmentLauncher.TAG)
            transaction.commitAllowingStateLoss()

    }


}