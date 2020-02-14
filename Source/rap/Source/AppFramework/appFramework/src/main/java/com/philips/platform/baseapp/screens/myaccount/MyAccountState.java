package com.philips.platform.baseapp.screens.myaccount;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.widget.Toast;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.flowmanager.base.UIStateData;
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
import com.philips.platform.baseapp.screens.utility.Constants;
import com.philips.platform.baseapp.screens.utility.RALog;
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
    private static final String MY_ORDERS_EVENT = "MyOrdersEvent";
    private static final String MY_OPTIN_EVENT = "MyOptinEvent";

    private static final String TAG = MyAccountState.class.getSimpleName();

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
        String[] profileItems;

            profileItems = new String[]{"MYA_My_details", "MYA_Marketing_Optin"};

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
                    getBaseState(fragmentLauncher, MY_DETAILS_EVENT);
                    return true;
                } else if (itemName.equalsIgnoreCase("MYA_My_orders")) {
                    getBaseState(fragmentLauncher, MY_ORDERS_EVENT);
                    return true;
                } else if(itemName.equalsIgnoreCase("MYA_Marketing_Optin")){
                    getBaseState(fragmentLauncher, MY_OPTIN_EVENT);

                }
                return false;
            }

            private void getStateData(BaseState baseState) {
                final UIStateData stateData = new UIStateData();
                stateData.setFragmentLaunchType(Constants.CLEAR_TILL_HOME);
                baseState.setUiStateData(stateData);
            }

            private void getBaseState(FragmentLauncher fragmentLauncher, String event) {
                BaseState baseState = null;
                BaseFlowManager targetFlowManager = getApplicationContext().getTargetFlowManager();

                try {
                    baseState = targetFlowManager.getNextState(targetFlowManager.getState(AppStates.MY_ACCOUNT), event);
                } catch (NoEventFoundException | NoStateException | NoConditionFoundException | StateIdNotSetException | ConditionIdNotSetException
                        e) {
                    Toast.makeText(fragmentLauncher.getFragmentActivity(), fragmentLauncher.getFragmentActivity().getString(R.string.RA_something_wrong), Toast.LENGTH_SHORT).show();
                }

                if (null != baseState) {
                    getStateData(baseState);

                    baseState.init(actContext.getApplicationContext());
                    baseState.navigate(new FragmentLauncher(fragmentLauncher.getFragmentActivity(), R.id.frame_container, (ActionBarListener) fragmentLauncher.getFragmentActivity()));
                }
            }

            @Override
            public void onError(MyaError myaError) {
                Toast.makeText(actContext, myaError.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLogoutClicked(final FragmentLauncher fragmentLauncher, final MyaLogoutListener myaLogoutListener) {
                RALog.d(TAG, "onLogoutClicked: on Logout clicked callback received from MYA to RefApp");
                URLogout urLogout = new URLogout();
                RALog.d(TAG,"onLogoutClicked: urLogout created");
                ((HamburgerActivity) actContext).showProgressBar();
                RALog.d(TAG,"onLogoutClicked: progressBar started created");
                urLogout.setUrLogoutListener(new URLogoutInterface.URLogoutListener() {
                    @Override
                    public void onLogoutResultSuccess() {
                        RALog.d(TAG,"onLogoutClicked: onLogoutResultSuccess started");
                        ((HamburgerActivity) actContext).onLogoutResultSuccess();
                        myaLogoutListener.onLogoutSuccess();
                        ((HamburgerActivity) actContext).hideProgressBar();
                        RALog.d(TAG,"onLogoutClicked: onLogoutResultSuccess completed");
                    }

                    @Override
                    public void onLogoutResultFailure(int i, String errorMessage) {
                        RALog.d(TAG,"onLogoutClicked: onLogoutResultFailure started");
                        String title = actContext.getString(R.string.MYA_Offline_title);
                        String Message = actContext.getString(R.string.MYA_Offline_message);
                        Fragment fragmentById = ((HamburgerActivity) actContext).getSupportFragmentManager().findFragmentById(R.id.frame_container);
                        if(fragmentById != null && fragmentById.isVisible()) {
                            new DialogView(title, Message).showDialog(getFragmentActivity());
                        }
                        myaLogoutListener.onLogOutFailure();
                        ((HamburgerActivity) actContext).hideProgressBar();
                        RALog.d(TAG,"onLogoutClicked: onLogoutResultFailure completed");

                    }

                    @Override
                    public void onNetworkError(String errorMessage) {
                        RALog.d(TAG,"onLogoutClicked: onNetworkError started");
                        String title = actContext.getString(R.string.MYA_Offline_title);
                        String Message = actContext.getString(R.string.MYA_Offline_message);
                        Fragment fragmentById = ((HamburgerActivity) actContext).getSupportFragmentManager().findFragmentById(R.id.frame_container);
                        if(fragmentById != null && fragmentById.isVisible()) {
                            new DialogView(title, Message).showDialog(getFragmentActivity());
                        }
                        myaLogoutListener.onLogOutFailure();
                        ((HamburgerActivity) actContext).hideProgressBar();
                        RALog.d(TAG,"onLogoutClicked: onNetworkError completed");
                    }
                });
               urLogout.performLogout(actContext);
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
    private MyaDependencies getUappDependencies(Context actContext) {
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
