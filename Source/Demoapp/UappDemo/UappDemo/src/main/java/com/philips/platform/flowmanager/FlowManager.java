/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.flowmanager;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.base.BaseCondition;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.flowmanager.condition.ConditionAppLaunch;
import com.philips.platform.flowmanager.condition.ConditionIsDonePressed;
import com.philips.platform.screens.aboutscreen.AboutScreenState;
import com.philips.platform.screens.homefragment.HomeFragmentState;
import com.philips.platform.screens.introscreen.welcomefragment.WelcomeState;
import com.philips.platform.screens.sample.SampleState;
import com.philips.platform.screens.splash.SplashState;
import com.philips.platform.screens.stateimpl.HamburgerActivityState;

import java.util.Map;

public class FlowManager extends BaseFlowManager {

    public FlowManager(Context applicationContext, String path) {
        super(applicationContext,path);
    }

    public FlowManager(){}

    @Override
    public void populateStateMap(final Map<String, BaseState> uiStateMap) {
        uiStateMap.put(AppStates.WELCOME, new WelcomeState());
        uiStateMap.put(AppStates.HOME_FRAGMENT, new HomeFragmentState());
        uiStateMap.put(AppStates.ABOUT, new AboutScreenState());
        uiStateMap.put(AppStates.SPLASH, new SplashState());
        uiStateMap.put(AppStates.HAMBURGER_HOME,new HamburgerActivityState());
        uiStateMap.put(AppStates.SAMPLE, new SampleState());

    }

    @Override
    public void populateConditionMap(final Map<String, BaseCondition> baseConditionMap) {
        baseConditionMap.put(AppConditions.CONDITION_APP_LAUNCH, new ConditionAppLaunch());
        baseConditionMap.put(AppConditions.IS_DONE_PRESSED, new ConditionIsDonePressed());

    }
}
