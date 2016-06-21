package com.philips.cdp.appframework.modularui;

import android.content.Context;

import com.philips.cdp.appframework.AppFrameworkBaseActivity;
import com.philips.cdp.registration.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 310240027 on 6/16/2016.
 */
public class UIFlowManager {


    public static List<UIStateBase> stateBaseList;
    public static UIStateBase startState;
    public static UIStateBase currentState;
    public static Map<Integer, String> activityMap;

   public static void populateStateBaseList(){
       stateBaseList = new ArrayList<UIStateBase>();
       stateBaseList.add(new UIState(new UISplashNavigationStateOne(), UIStateDefintions.UI_SPLASH_STATE_ONE));
       stateBaseList.add(new UIState(new UISplashNavigationStateTwo(), UIStateDefintions.UI_SPLASH_STATE_TWO));
       stateBaseList.add(new UIState(new UISplashNavigationStateThree(), UIStateDefintions.UI_SPLASH_STATE_THREE));
       stateBaseList.add(new UIState(new UIWSNavigationStateOne(), UIStateDefintions.UI_WELCOME_STATE_ONE));
       stateBaseList.add(new UIState(new UIWSNavigationStateTwo(), UIStateDefintions.UI_WELCOME_STATE_TWO));
       stateBaseList.add(new UIState(new UIWSNavigationStateOne(), UIStateDefintions.UI_WELCOME_STATE_THREE));
       stateBaseList.add(new UIState(new UIWSNavigationStateOne(), UIStateDefintions.UI_HOME_STATE_ONE));
       stateBaseList.add(new UIState(new UIUserRegNavigationStateOne(), UIStateDefintions.UI_REGISTRATION_STATE_ONE));

    }

    public static void checkUserSignInAndDonePressed(Context mContext){
        User user = new User(mContext);
        if (AppFrameworkBaseActivity.getIntroScreenDonePressed()) {
            if (user.isUserSignIn()) {
                currentState = new UIState(new UISplashNavigationStateOne() , UIStateDefintions.UI_SPLASH_STATE_ONE);
            } else {
                currentState = new UIState(new UISplashNavigationStateTwo() , UIStateDefintions.UI_SPLASH_STATE_TWO);
            }
        } else {
            currentState = new UIState(new UISplashNavigationStateThree() , UIStateDefintions.UI_SPLASH_STATE_THREE);
        }

    }
    public static void addToStateList(UIStateBase uiStateBase){
        if(null != stateBaseList) {
            stateBaseList.add(uiStateBase);
        }
    }

    public static UIStateBase getFromStateList(@UIStateDefintions.UIStateDef int stateID){
        UIStateBase uiStateBaseItem = null;
        for(UIStateBase uiStateBase:stateBaseList){
            if(uiStateBase.getStateID() == stateID){
                uiStateBaseItem = uiStateBase;
                break;
            }
        }
        return uiStateBaseItem;
    }

    public static Map<Integer, String> getActivityMap() {
        return activityMap;
    }

    public static void setActivityMap(Map<Integer, String> activityMap) {
        UIFlowManager.activityMap = activityMap;
    }

    public static void populateActivityMap(){
        activityMap = new HashMap<Integer, String>();
        activityMap.put(UIStateDefintions.UI_SPLASH_STATE_ONE, "HomeActivity");
        activityMap.put(UIStateDefintions.UI_SPLASH_STATE_TWO, "UserRegistration");
        activityMap.put(UIStateDefintions.UI_SPLASH_STATE_THREE, "WelcomeActivity");
        activityMap.put(UIStateDefintions.UI_WELCOME_STATE_ONE, "HomeActivity");
        activityMap.put(UIStateDefintions.UI_WELCOME_STATE_TWO, "UserRegistration");
        activityMap.put(UIStateDefintions.UI_REGISTRATION_STATE_ONE, "HomeActivity");
    }
}
