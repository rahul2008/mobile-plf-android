package com.philips.platform.baseapp.screens.myaccount;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.philips.cdp.digitalcare.CcConsentProvider;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationLaunchMode;
import com.philips.cdp.registration.consents.MarketingConsentHandler;
import com.philips.cdp.registration.consents.URConsentProvider;
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
import com.philips.platform.appframework.homescreen.HamburgerActivity;
import com.philips.platform.appframework.logout.URLogout;
import com.philips.platform.appframework.logout.URLogoutInterface;
import com.philips.platform.appframework.ui.dialogs.DialogView;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.utility.Constants;
import com.philips.platform.baseapp.screens.webview.WebViewStateData;
import com.philips.platform.mya.MyaTabConfig;
import com.philips.platform.mya.catk.CatkInputs;
import com.philips.platform.mya.catk.ConsentsClient;
import com.philips.platform.mya.catk.device.DeviceStoredConsentHandler;
import com.philips.platform.mya.csw.CswDependencies;
import com.philips.platform.mya.csw.CswInterface;
import com.philips.platform.mya.csw.CswLaunchInput;
import com.philips.platform.mya.csw.permission.MyAccountUIEventListener;
import com.philips.platform.mya.csw.permission.PermissionHelper;
import com.philips.platform.mya.error.MyaError;
import com.philips.platform.mya.interfaces.MyaListener;
import com.philips.platform.mya.launcher.MyaDependencies;
import com.philips.platform.mya.launcher.MyaInterface;
import com.philips.platform.mya.launcher.MyaLaunchInput;
import com.philips.platform.mya.launcher.MyaSettings;
import com.philips.platform.pif.chi.ConsentDefinitionRegistry;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.ths.consent.THSLocationConsentProvider;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.uappinput.UappSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MyAccountState extends BaseState implements MyAccountUIEventListener {

    public MyAccountState() {
        super(AppStates.MY_ACCOUNT);
    }

    private Context actContext;
    private FragmentLauncher fragmentLauncher;
    private static final String PRIVACY_NOTICE = "PrivacyNotice";
    private List<ConsentDefinition> consentDefinitionList;

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
            public boolean onSettingsMenuItemSelected(String itemName) {
                if (itemName.equalsIgnoreCase(actContext.getString(com.philips.platform.mya.R.string.MYA_Logout)) && actContext instanceof HamburgerActivity) {
                    ((HamburgerActivity) actContext).onLogoutResultSuccess();
                } else if (itemName.equals("MYA_Privacy_Settings")) {
                    RestInterface restInterface = getRestClient();
                    if (restInterface.isInternetReachable()) {
                        CswDependencies dependencies = new CswDependencies(getApplicationContext().getAppInfra());
                        PermissionHelper.getInstance().setMyAccountUIEventListener(MyAccountState.this);
                        CswInterface cswInterface = getCswInterface();
                        UappSettings uappSettings = new UappSettings(actContext);
                        cswInterface.init(dependencies, uappSettings);
                        cswInterface.launch(fragmentLauncher, buildLaunchInput(true, actContext));
                        return true;
                    } else {
                        String title = actContext.getString(R.string.MYA_Offline_title);
                        String message = actContext.getString(R.string.MYA_Offline_message);
                        showDialog(title, message);
                    }
                }
                return false;
            }

            @Override
            public boolean onProfileMenuItemSelected(String itemName) {
                if (itemName.equals(actContext.getString(R.string.MYA_My_details)) || itemName.equalsIgnoreCase("MYA_My_details")) {
                    URLaunchInput urLaunchInput = new URLaunchInput();
                    urLaunchInput.enableAddtoBackStack(true);
                    urLaunchInput.setEndPointScreen(RegistrationLaunchMode.USER_DETAILS);
                    URInterface urInterface = new URInterface();
                    urInterface.launch(fragmentLauncher,urLaunchInput);
                    return true;
                }
                return false;
            }

            @Override
            public void onError(MyaError myaError) {
                Toast.makeText(actContext, myaError.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLogoutClicked(final MyaLogoutListener myaLogoutListener) {
                URLogout urLogout=new URLogout();
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
                urLogout.performLogout(actContext,user);
            }
        };
    }

    private void showDialog(String title, String message) {
        new DialogView(title, message).showDialog(getFragmentActivity());
    }

    private CswInterface getCswInterface() {
        return new CswInterface();
    }

    private CswLaunchInput buildLaunchInput(boolean addToBackStack, Context context) {
        CswLaunchInput cswLaunchInput = new CswLaunchInput(context, consentDefinitionList);
        cswLaunchInput.addToBackStack(addToBackStack);
        return cswLaunchInput;
    }

    /**
     * <p>
     * Creates a list of ConsentDefinitions</p
     *
     * @param context : can be used to for localized strings <code>context.getString(R.string.consent_definition)</code>
     * @return non-null list (may be empty though)
     */
    @VisibleForTesting
    List<ConsentDefinition> createCatkDefinitions(Context context) {
        final List<ConsentDefinition> definitions = new ArrayList<>();
        ConsentDefinition momentConsentDefinition = new ConsentDefinition(context.getString(R.string.RA_MYA_Consent_Moment_Text), context.getString(R.string.RA_MYA_Consent_Moment_Help),
                Collections.singletonList("moment"), 1);
        ConsentDefinitionRegistry.add(momentConsentDefinition);
        definitions.add(momentConsentDefinition);
        ConsentDefinition coachingConsentDefinition = new ConsentDefinition(context.getString(R.string.RA_MYA_Consent_Coaching_Text), context.getString(R.string.RA_MYA_Consent_Coaching_Help),
                Collections.singletonList("coaching"), 1);
        ConsentDefinitionRegistry.add(coachingConsentDefinition);
        definitions.add(coachingConsentDefinition);
        ConsentDefinition binaryConsentDefinition = new ConsentDefinition(context.getString(R.string.RA_MYA_Consent_Binary_Text), context.getString(R.string.RA_MYA_Consent_Binary_Help),
                Collections.singletonList("binary"), 1);
        ConsentDefinitionRegistry.add(binaryConsentDefinition);
        definitions.add(binaryConsentDefinition);
        ConsentDefinition clickStreamConsentDefinition = new ConsentDefinition(context.getString(R.string.RA_MYA_Consent_Clickstream_Text), context.getString(R.string.RA_MYA_Consent_Clickstream_Help),
                Collections.singletonList("clickstream"), 1);
        definitions.add(clickStreamConsentDefinition);
        ConsentDefinitionRegistry.add(clickStreamConsentDefinition);
        ConsentDefinition researchConsentDefinition = new ConsentDefinition(context.getString(R.string.RA_MYA_Research_Analytics_Consent), context.getString(R.string.RA_MYA_Consent_Research_Analytics_Help_Text),
                Arrays.asList("research", "analytics"), 1);
        ConsentDefinitionRegistry.add(researchConsentDefinition);
        definitions.add(researchConsentDefinition);
        definitions.add(THSLocationConsentProvider.getTHSConsentDefinition(context));
        return definitions;
    }

    private List<ConsentDefinition> createUserRegistrationDefinitions(Context context) {
        final List<ConsentDefinition> definitions = new ArrayList<>();
        definitions.add(new ConsentDefinition(context.getString(R.string.RA_Setting_Philips_Promo_Title), context
                .getString(R.string.RA_MYA_Marketing_Help_Text), Collections.singletonList(URConsentProvider.USR_MARKETING_CONSENT), 1));
        return definitions;
    }

    @Override
    public void init(Context context) {
        AppFrameworkApplication app = (AppFrameworkApplication) context.getApplicationContext();

        List<ConsentDefinition> catkConsentDefinitions = createCatkDefinitions(context);
//        List<ConsentDefinition> urConsentDefinitions = createUserRegistrationDefinitions(context);

        CatkInputs catkInputs = new CatkInputs.Builder()
                .setContext(context)
                .setAppInfraInterface(app.getAppInfra())
                .setConsentManager(app.getAppInfra().getConsentManager())
                .setConsentDefinitions(catkConsentDefinitions)
                .build();
        ConsentsClient.getInstance().init(catkInputs);

        consentDefinitionList = new ArrayList<>();
        consentDefinitionList.addAll(catkConsentDefinitions);
//        consentDefinitionList.addAll(urConsentDefinitions);
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

    @Override
    public void onPrivacyNoticeClicked() {
        launchWebView(Constants.PRIVACY);
    }

    private void launchWebView(String serviceId) {
        BaseFlowManager targetFlowManager = getApplicationContext().getTargetFlowManager();
        BaseState baseState = null;
        try {
            baseState = targetFlowManager.getNextState(targetFlowManager.getCurrentState(), PRIVACY_NOTICE);
        } catch (NoEventFoundException | NoStateException | NoConditionFoundException | StateIdNotSetException | ConditionIdNotSetException
                e) {
            Toast.makeText(getFragmentActivity(), getFragmentActivity().getString(R.string.RA_something_wrong), Toast.LENGTH_SHORT).show();
        }
        if (null != baseState) {
            WebViewStateData webViewStateData = new WebViewStateData();
            webViewStateData.setServiceId(serviceId);
            baseState.setUiStateData(webViewStateData);
            baseState.navigate(new FragmentLauncher(getFragmentActivity(), R.id.frame_container, (ActionBarListener) getFragmentActivity()));
        }
    }

    protected AppFrameworkApplication getApplicationContext() {
        return (AppFrameworkApplication) getFragmentActivity().getApplication();
    }

    public FragmentActivity getFragmentActivity() {
        return fragmentLauncher.getFragmentActivity();
    }

    private RestInterface getRestClient() {
        return ((AppFrameworkApplication) actContext.getApplicationContext()).getAppInfra().getRestClient();
    }
}
