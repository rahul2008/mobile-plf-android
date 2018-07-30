package com.philips.platform.baseapp.screens.Optin;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationLaunchMode;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.ui.utils.RegistrationContentConfiguration;
import com.philips.cdp.registration.ui.utils.UIFlow;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URLaunchInput;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.flowmanager.exceptions.ConditionIdNotSetException;
import com.philips.platform.appframework.flowmanager.exceptions.NoConditionFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoEventFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoStateException;
import com.philips.platform.appframework.flowmanager.exceptions.StateIdNotSetException;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.utility.BaseAppUtil;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.dscdemo.utility.SyncScheduler;
import com.philips.platform.referenceapp.PushNotificationManager;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;


public class MarketingOptin extends BaseState implements UserRegistrationUIEventListener {

    Context context;
    private FragmentLauncher fragmentLauncher;
    private User userObject;

    public MarketingOptin() {
        super(AppStates.MY_DETAILS_STATE);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {

        fragmentLauncher = (FragmentLauncher) uiLauncher;
        URLaunchInput urLaunchInput = new URLaunchInput();
        urLaunchInput.setEndPointScreen(RegistrationLaunchMode.MARKETING_OPT);
        urLaunchInput.setRegistrationFunction(RegistrationFunction.Registration);
        urLaunchInput.setRegistrationContentConfiguration(getRegistrationContentConfiguration());
        urLaunchInput.setUIFlow(UIFlow.FLOW_B);
        urLaunchInput.enableAddtoBackStack(true);
        urLaunchInput.setUserRegistrationUIEventListener(this);
        URInterface urInterface = new URInterface();
        urInterface.launch(fragmentLauncher,urLaunchInput);

    }

    public RegistrationContentConfiguration getRegistrationContentConfiguration() {
        RegistrationContentConfiguration registrationContentConfiguration = new RegistrationContentConfiguration();
        registrationContentConfiguration.enableMarketImage(R.drawable.ref_app_home_page);
        return registrationContentConfiguration;
    }
    @Override
    public void init(Context context) {
        this.context = context;

    }

    @Override
    public void updateDataModel() {

    }

    public User getUserObject(Context context) {
        userObject = new User(context);
        return userObject;
    }

    @Override
    public void onUserRegistrationComplete(Activity activity) {
        Toast.makeText(context, "Counted Successfully", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onPrivacyPolicyClick(Activity activity) {

    }

    @Override
    public void onTermsAndConditionClick(Activity activity) {

    }
}
