/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.flowmanager;

import com.philips.platform.appframework.flowmanager.base.BaseCondition;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.flowmanager.condition.UappConditionAppLaunch;
import com.philips.platform.flowmanager.condition.UappConditionIsDonePressed;
import com.philips.platform.screens.aboutscreen.AboutScreenState;
import com.philips.platform.screens.homefragment.HomeFragmentState;
import com.philips.platform.screens.introscreen.welcomefragment.WelcomeState;
import com.philips.platform.screens.sample.SampleState;
import com.philips.platform.screens.splash.SplashState;
import com.philips.platform.screens.stateimpl.HamburgerActivityState;

import java.util.Map;

public class UappFlowManager extends BaseFlowManager {

    public UappFlowManager(){}

    @Override
    public void populateStateMap(final Map<String, BaseState> uiStateMap) {
        uiStateMap.put(UappStates.WELCOME, new WelcomeState());
        uiStateMap.put(UappStates.HOME_FRAGMENT, new HomeFragmentState());
        uiStateMap.put(UappStates.ABOUT, new AboutScreenState());
        uiStateMap.put(UappStates.SPLASH, new SplashState());
        uiStateMap.put(UappStates.HAMBURGER_HOME,new HamburgerActivityState());
        uiStateMap.put(UappStates.SAMPLE, new SampleState());

    }

    @Override
    public void populateConditionMap(final Map<String, BaseCondition> baseConditionMap) {
        baseConditionMap.put(UappConditions.CONDITION_APP_LAUNCH, new UappConditionAppLaunch());
        baseConditionMap.put(UappConditions.IS_DONE_PRESSED, new UappConditionIsDonePressed());

    }
}
