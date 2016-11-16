package com.philips.platform.appframework.flowmanager;

import com.philips.platform.flowmanager.condition.BaseCondition;
import com.philips.platform.flowmanager.condition.ConditionAppLaunch;
import com.philips.platform.flowmanager.condition.ConditionIsDonePressed;
import com.philips.platform.flowmanager.condition.ConditionIsLoggedIn;
import com.philips.platform.modularui.statecontroller.BaseAppCondition;

import java.util.Map;

public class HamburgerAppCondition extends BaseAppCondition {

    public HamburgerAppCondition() {
        addMap(baseConditionMap);
    }

    private void addMap(final Map<String, BaseCondition> baseConditionMap) {
        baseConditionMap.put(IS_LOGGED_IN, new ConditionIsLoggedIn());
        baseConditionMap.put(IS_DONE_PRESSED, new ConditionIsDonePressed());
        baseConditionMap.put(CONDITION_APP_LAUNCH, new ConditionAppLaunch());
    }
}
