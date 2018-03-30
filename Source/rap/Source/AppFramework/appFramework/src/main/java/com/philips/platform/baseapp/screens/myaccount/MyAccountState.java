package com.philips.platform.baseapp.screens.myaccount;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.philips.cdp.registration.User;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.flowmanager.exceptions.ConditionIdNotSetException;
import com.philips.platform.appframework.flowmanager.exceptions.NoConditionFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoEventFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoStateException;
import com.philips.platform.appframework.flowmanager.exceptions.StateIdNotSetException;
import com.philips.platform.appframework.homescreen.HamburgerActivity;
import com.philips.platform.appframework.logout.URLogout;
import com.philips.platform.appframework.logout.URLogoutInterface;
import com.philips.platform.appframework.ui.dialogs.DialogView;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.mya.MyaTabConfig;
import com.philips.platform.mya.error.MyaError;
import com.philips.platform.mya.interfaces.MyaListener;
import com.philips.platform.mya.launcher.MyaDependencies;
import com.philips.platform.mya.launcher.MyaInterface;
import com.philips.platform.mya.launcher.MyaLaunchInput;
import com.philips.platform.mya.launcher.MyaSettings;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import java.util.Arrays;

public class MyAccountState extends BaseState{

    public MyAccountState() {
        super(AppStates.MY_ACCOUNT);
    }

    private Context actContext;
    private FragmentLauncher fragmentLauncher;
    private static final String PRIVACY_SETTINGS_EVENT = "PrivacySettingsEvent";
    private static final String MY_DETAILS_EVENT = "MyDetailsEvent";

    @Override
    public void navigate(UiLauncher uiLauncher) {
        fragmentLauncher = (FragmentLauncher) uiLauncher;
        actContext = fragmentLauncher.getFragmentActivity();
        ((AbstractAppFrameworkBaseActivity) actContext).handleFragmentBackStack(null, "", getUiStateData().getFragmentLaunchState());

        MyaInterface myaInterface = getInterface();
        myaInterface.init(getUappDependencies(actContext), new MyaSettings(actContext.getApplicationContext()));

        MyaLaunchInput launchInput = new MyaLaunchInput(actContext);
        launchInput.setMyaListener(getMyaListener());
        MyaTabConfig myaTabConfig = new MyaTabConfig(actContext.getString(R.string.mya_config_tab), new TabTestFragment());
        launchInput.setMyaTabConfig(myaTabConfig);
        String[] profileItems = {"MYA_My_details"};
        String[] settingItems = {"MYA_Country", "MYA_Privacy_Settings"};
        launchInput.setUserDataInterface(getApplicationContext().getUserRegistrationState().getUserDataInterface());
        launchInput.setProfileMenuList(Arrays.asList(profileItems));
        launchInput.setSettingsMenuList(Arrays.asList(settingItems));
        myaInterface.launch(fragmentLauncher, launchInput);
    }

    private MyaListener getMyaListener() {
        return new MyaListener() {
            @Override
            public boolean onSettingsMenuItemSelected(final FragmentLauncher fragmentLauncher, String itemName) {
                if (itemName.equalsIgnoreCase(actContext.getString(com.philips.platform.mya.R.string.MYA_Logout)) && actContext instanceof HamburgerActivity) {
                    ((HamburgerActivity) actContext).onLogoutResultSuccess();
                } else if (itemName.equals("MYA_Privacy_Settings")) {
                    BaseFlowManager targetFlowManager = getApplicationContext().getTargetFlowManager();
                    BaseState baseState = null;
                    try {
                        baseState = targetFlowManager.getNextState(targetFlowManager.getState(AppStates.MY_ACCOUNT), PRIVACY_SETTINGS_EVENT);
                    } catch (NoEventFoundException | NoStateException | NoConditionFoundException | StateIdNotSetException | ConditionIdNotSetException
                            e) {
                        Toast.makeText(fragmentLauncher.getFragmentActivity(), fragmentLauncher.getFragmentActivity().getString(R.string.RA_something_wrong), Toast.LENGTH_SHORT).show();
                    }
                    if(null != baseState){
                        baseState.init(fragmentLauncher.getFragmentActivity().getApplicationContext());
                        baseState.navigate(new FragmentLauncher(fragmentLauncher.getFragmentActivity(), R.id.frame_container, (ActionBarListener) fragmentLauncher.getFragmentActivity()));
                    }
                    return true;
                }
                return false;
            }

            @Override
            public boolean onProfileMenuItemSelected(final FragmentLauncher fragmentLauncher, String itemName) {
                if (itemName.equalsIgnoreCase("MYA_My_details")) {
                    BaseFlowManager targetFlowManager = getApplicationContext().getTargetFlowManager();
                    BaseState baseState = null;
                    try {
                        baseState = targetFlowManager.getNextState(targetFlowManager.getState(AppStates.MY_ACCOUNT), MY_DETAILS_EVENT);
                    } catch (NoEventFoundException | NoStateException | NoConditionFoundException | StateIdNotSetException | ConditionIdNotSetException
                            e) {
                        Toast.makeText(fragmentLauncher.getFragmentActivity(), fragmentLauncher.getFragmentActivity().getString(R.string.RA_something_wrong), Toast.LENGTH_SHORT).show();
                    }
                    if(null != baseState){
                        baseState.init(actContext);
                        baseState.navigate(new FragmentLauncher(fragmentLauncher.getFragmentActivity(), R.id.frame_container, (ActionBarListener) fragmentLauncher.getFragmentActivity()));
                    }
                    return true;
                }
                return false;
            }

            @Override
            public void onError(MyaError myaError) {
                Toast.makeText(actContext, myaError.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLogoutClicked(final FragmentLauncher fragmentLauncher, final MyaLogoutListener myaLogoutListener) {

                URLogout urLogout = new URLogout();
                urLogout.setUrLogoutListener(new URLogoutInterface.URLogoutListener() {
                    @Override
                    public void onLogoutResultSuccess() {
                        ((HamburgerActivity) actContext).onLogoutResultSuccess();
                        myaLogoutListener.onLogoutSuccess();
                    }

                    @Override
                    public void onLogoutResultFailure(int i, String errorMessage) {
                        String title = actContext.getString(R.string.MYA_Offline_title);
                        String Message = actContext.getString(R.string.MYA_Offline_message);
                        new DialogView(title, Message).showDialog(getFragmentActivity());
                        myaLogoutListener.onLogOutFailure();
                    }

                    @Override
                    public void onNetworkError(String errorMessage) {
                        String title = actContext.getString(R.string.MYA_Offline_title);
                        String Message = actContext.getString(R.string.MYA_Offline_message);
                        new DialogView(title, Message).showDialog(getFragmentActivity());
                        myaLogoutListener.onLogOutFailure();
                    }
                });
                User user = getApplicationContext().getUserRegistrationState().getUserObject(actContext);
                urLogout.performLogout(actContext, user);
            }
        };
    }

    @Override
    public void init(Context context) {

    }


    @Override
    public void updateDataModel() {

    }

    public MyaInterface getInterface() {
        return new MyaInterface();
    }

    @NonNull
    protected MyaDependencies getUappDependencies(Context actContext) {
        AppInfraInterface appInfra = ((AppFrameworkApplication) actContext.getApplicationContext()).getAppInfra();
        return new MyaDependencies(appInfra);
    }

    protected AppFrameworkApplication getApplicationContext() {
        return (AppFrameworkApplication) getFragmentActivity().getApplication();
    }

    public FragmentActivity getFragmentActivity() {
        return fragmentLauncher.getFragmentActivity();
    }

}
