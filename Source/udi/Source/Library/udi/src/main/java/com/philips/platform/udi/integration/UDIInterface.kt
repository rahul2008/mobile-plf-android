package com.philips.platform.udi.integration

import android.content.Intent
import android.os.Bundle
import android.util.Log

import com.philips.platform.uappframework.UappInterface
import com.philips.platform.uappframework.launcher.ActivityLauncher
import com.philips.platform.uappframework.launcher.FragmentLauncher
import com.philips.platform.uappframework.launcher.UiLauncher
import com.philips.platform.uappframework.uappinput.UappDependencies
import com.philips.platform.uappframework.uappinput.UappLaunchInput
import com.philips.platform.uappframework.uappinput.UappSettings
import com.philips.platform.udi.LoginActivity

class UDIInterface : UappInterface {
    private val TAG = UDIInterface::class.java.simpleName

    override fun init(uappDependencies: UappDependencies, uappSettings: UappSettings) {

    }

    override fun launch(uiLauncher: UiLauncher, uappLaunchInput: UappLaunchInput) {
        if (uiLauncher is ActivityLauncher) {
            launchAsActivity(uiLauncher, uappLaunchInput)
            Log.i(TAG, "Launch : Launched as activity")
        } else if (uiLauncher is FragmentLauncher) {
            launchAsFragment(uiLauncher, uappLaunchInput)
            Log.i(TAG, "Launch : Launched as fragment")
        }
    }

    private fun launchAsFragment(uiLauncher: FragmentLauncher, uappLaunchInput: UappLaunchInput) {
        //TODO
    }

    private fun launchAsActivity(uiLauncher: ActivityLauncher, uappLaunchInput: UappLaunchInput) {
        val registrationIntent = Intent(uiLauncher.activityContext, LoginActivity::class.java)
        val bundle = Bundle()

        bundle.putInt(ORIENTAION, uiLauncher.screenOrientation.orientationValue)

        registrationIntent.putExtras(bundle)
        registrationIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        uiLauncher.activityContext.startActivity(registrationIntent)
    }

    companion object {

        private val ORIENTAION = "Orientaion"
    }
}
