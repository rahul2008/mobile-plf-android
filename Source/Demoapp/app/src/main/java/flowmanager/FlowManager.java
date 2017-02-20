/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package flowmanager;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.base.BaseCondition;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;

import java.util.Map;

import flowmanager.condition.ConditionAppLaunch;
import flowmanager.condition.ConditionIsDonePressed;
import philips.app.aboutscreen.AboutScreenState;
import philips.app.homefragment.HomeFragmentState;
import philips.app.introscreen.welcomefragment.WelcomeState;
import philips.app.splash.SplashState;
import philips.app.stateimpl.HamburgerActivityState;

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

    }

    @Override
    public void populateConditionMap(final Map<String, BaseCondition> baseConditionMap) {
        baseConditionMap.put(AppConditions.CONDITION_APP_LAUNCH, new ConditionAppLaunch());
        baseConditionMap.put(AppConditions.IS_DONE_PRESSED, new ConditionIsDonePressed());

    }
}
