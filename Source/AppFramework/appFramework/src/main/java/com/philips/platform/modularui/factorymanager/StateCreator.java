package com.philips.platform.modularui.factorymanager;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.modularui.eventbus.StateEvent;
import com.philips.platform.modularui.navigatorimpl.DebugTestFragmentNavigator;
import com.philips.platform.modularui.navigatorimpl.HomeActivityNavigator;
import com.philips.platform.modularui.navigatorimpl.HomeFragmentNavigator;
import com.philips.platform.modularui.navigatorimpl.IntroductionScreenNavigator;
import com.philips.platform.modularui.navigatorimpl.SettingsFragmentNavigator;
import com.philips.platform.modularui.navigatorimpl.SupportFragmentNavigator;
import com.philips.platform.modularui.navigatorimpl.UserRegistrationNavigator;
import com.philips.platform.modularui.statecontroller.UIBaseNavigator;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.util.UIConstants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by 310240027 on 7/7/2016.
 */
public class StateCreator {

    AppFrameworkApplication appFrameworkApplication;
    UIState uiState;
    UIBaseNavigator uiBaseNavigator;

    public StateCreator(){
        uiState = new UIState();
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void handleSplashEvent(StateEvent stateEvent){
        appFrameworkApplication = (AppFrameworkApplication) stateEvent.getContext().getApplicationContext();
        if(stateEvent.getStateID() == UIConstants.UI_SPLASH_UNREGISTERED_STATE){
            uiState = new UIState();
            uiBaseNavigator = new IntroductionScreenNavigator();
            uiState.setStateID(stateEvent.stateID);
            uiState.setNavigator(uiBaseNavigator);
            uiBaseNavigator.navigate(stateEvent.getContext());
            appFrameworkApplication.getFlowManager().setCurrentState(uiState);
            appFrameworkApplication.getFlowManager().addToStateMap(uiState);
        }
    }
    @Subscribe
    public void handleWelcomeEvent(StateEvent stateEvent){
        appFrameworkApplication = (AppFrameworkApplication) stateEvent.getContext().getApplicationContext();
        if(stateEvent.getStateID() == UIConstants.UI_WELCOME_STATE){
            uiState = new UIState();
            uiBaseNavigator = new IntroductionScreenNavigator();
            uiState.setStateID(stateEvent.stateID);
            uiState.setNavigator(uiBaseNavigator);
            uiBaseNavigator.navigate(stateEvent.getContext());
            appFrameworkApplication.getFlowManager().setCurrentState(uiState);
            appFrameworkApplication.getFlowManager().addToStateMap(uiState);
        }
    }
    @Subscribe
    public void handleUserRegistrationEvent(StateEvent stateEvent){
        appFrameworkApplication = (AppFrameworkApplication) stateEvent.getContext().getApplicationContext();
        if(stateEvent.getStateID() == UIConstants.UI_REGISTRATION_STATE){
            uiState = new UIState();
            uiBaseNavigator = new UserRegistrationNavigator();
            uiState.setStateID(stateEvent.stateID);
            uiState.setNavigator(uiBaseNavigator);
            uiBaseNavigator.navigate(stateEvent.getContext());
            appFrameworkApplication.getFlowManager().setCurrentState(uiState);
            appFrameworkApplication.getFlowManager().addToStateMap(uiState);
        }
    }
    @Subscribe
    public void handleHomeEvent(StateEvent stateEvent){
        appFrameworkApplication = (AppFrameworkApplication) stateEvent.getContext().getApplicationContext();
        if(stateEvent.getStateID() == UIConstants.UI_HOME_STATE){
            uiState = new UIState();
            uiBaseNavigator = new HomeActivityNavigator();
            uiState.setStateID(stateEvent.stateID);
            uiState.setNavigator(uiBaseNavigator);
            uiBaseNavigator.navigate(stateEvent.getContext());
            appFrameworkApplication.getFlowManager().setCurrentState(uiState);
            appFrameworkApplication.getFlowManager().addToStateMap(uiState);
        }
    }
    @Subscribe
    public void handleHomeFragmentEvent(StateEvent stateEvent){
        appFrameworkApplication = (AppFrameworkApplication) stateEvent.getContext().getApplicationContext();
        if(stateEvent.getStateID() == UIConstants.UI_HOME_FRAGMENT_STATE){
            uiState = new UIState();
            uiBaseNavigator = new HomeFragmentNavigator();
            uiState.setStateID(stateEvent.stateID);
            uiState.setNavigator(uiBaseNavigator);
            uiBaseNavigator.navigate(stateEvent.getContext());
            appFrameworkApplication.getFlowManager().setCurrentState(uiState);
            appFrameworkApplication.getFlowManager().addToStateMap(uiState);
        }
    }
    @Subscribe
    public void handleSupportFragmentEvent(StateEvent stateEvent){
        appFrameworkApplication = (AppFrameworkApplication) stateEvent.getContext().getApplicationContext();
        if(stateEvent.getStateID() == UIConstants.UI_SUPPORT_FRAGMENT_STATE){
            uiState = new UIState();
            uiBaseNavigator = new SupportFragmentNavigator();
            uiState.setStateID(stateEvent.stateID);
            uiState.setNavigator(uiBaseNavigator);
            uiBaseNavigator.navigate(stateEvent.getContext());
            appFrameworkApplication.getFlowManager().setCurrentState(uiState);
            appFrameworkApplication.getFlowManager().addToStateMap(uiState);
        }
    }
    @Subscribe
    public void handleSettingsFragmentEvent(StateEvent stateEvent){
        appFrameworkApplication = (AppFrameworkApplication) stateEvent.getContext().getApplicationContext();
        if(stateEvent.getStateID() == UIConstants.UI_SETTINGS_FRAGMENT_STATE){
            uiState = new UIState();
            uiBaseNavigator = new SettingsFragmentNavigator();
            uiState.setStateID(stateEvent.stateID);
            uiState.setNavigator(uiBaseNavigator);
            uiBaseNavigator.navigate(stateEvent.getContext());
            appFrameworkApplication.getFlowManager().setCurrentState(uiState);
            appFrameworkApplication.getFlowManager().addToStateMap(uiState);
        }
    }
    @Subscribe
    public void handleDebugFragmentEvent(StateEvent stateEvent){
        appFrameworkApplication = (AppFrameworkApplication) stateEvent.getContext().getApplicationContext();
        if(stateEvent.getStateID() == UIConstants.UI_DEBUG_FRAGMENT_STATE){
            uiState = new UIState();
            uiBaseNavigator = new DebugTestFragmentNavigator();
            uiState.setStateID(stateEvent.stateID);
            uiState.setNavigator(uiBaseNavigator);
            uiBaseNavigator.navigate(stateEvent.getContext());
            appFrameworkApplication.getFlowManager().setCurrentState(uiState);
            appFrameworkApplication.getFlowManager().addToStateMap(uiState);
        }
    }

}
