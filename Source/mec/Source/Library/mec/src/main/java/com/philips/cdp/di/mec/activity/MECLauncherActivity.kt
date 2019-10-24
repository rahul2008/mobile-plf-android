package com.philips.cdp.di.mec.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.databinding.MecActivityBinding
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.platform.uid.utils.UIDActivity
import kotlinx.android.synthetic.main.mec_action_bar.*
import kotlinx.android.synthetic.main.mec_activity.*
import kotlinx.android.synthetic.main.mec_activity.iap_iv_header_back_button


class MECLauncherActivity : UIDActivity(), View.OnClickListener {

    lateinit var bundle: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<MecActivityBinding>(this, R.layout.mec_activity_launcher)

        setSupportActionBar(mec_toolbar)
        getSupportActionBar()?.setDisplayShowTitleEnabled(false)

        mec_iv_header_back_button.setOnClickListener(this)
        bundle = intent.getExtras()
        val landingScreen:Int = bundle.getInt(MECConstant.MEC_LANDING_SCREEN)

        loadDecisionFragment();
    }

    override fun onClick(v: View?) {
        onBackPressed()
    }

    private fun loadDecisionFragment( ){
            val mECFragmentLauncher = MECFragmentLauncher()
             mECFragmentLauncher.arguments = bundle
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(com.philips.cdp.di.mec.R.id.mec_fragment_container, mECFragmentLauncher, mECFragmentLauncher.TAG)
            transaction.addToBackStack(mECFragmentLauncher.TAG)
            transaction.commitAllowingStateLoss()

    }


}