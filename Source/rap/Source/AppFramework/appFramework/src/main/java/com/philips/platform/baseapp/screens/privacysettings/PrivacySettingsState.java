package com.philips.platform.baseapp.screens.privacysettings;

import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.philips.cdp.digitalcare.CcConsentProvider;
import com.philips.cdp.registration.consents.URConsentProvider;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.flowmanager.exceptions.ConditionIdNotSetException;
import com.philips.platform.appframework.flowmanager.exceptions.NoConditionFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoEventFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoStateException;
import com.philips.platform.appframework.flowmanager.exceptions.StateIdNotSetException;
import com.philips.platform.appframework.ui.dialogs.DialogView;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.neura.NeuraConsentProvider;
import com.philips.platform.baseapp.screens.utility.Constants;
import com.philips.platform.baseapp.screens.webview.WebViewStateData;
import com.philips.platform.mya.catk.CatkInitializer;
import com.philips.platform.mya.catk.CatkInputs;
import com.philips.platform.mya.catk.CatkInterface;
import com.philips.platform.mya.csw.CswDependencies;
import com.philips.platform.mya.csw.CswInterface;
import com.philips.platform.mya.csw.CswLaunchInput;
import com.philips.platform.mya.csw.permission.MyAccountUIEventListener;
import com.philips.platform.mya.csw.permission.PermissionHelper;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.ths.consent.THSLocationConsentProvider;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.uappinput.UappSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PrivacySettingsState extends BaseState implements MyAccountUIEventListener {

    public PrivacySettingsState() {
        super(AppStates.PRIVACY_SETTINGS_STATE);
    }

    private static final String PRIVACY_NOTICE = "PrivacyNotice";
    private Context actContext;
    private FragmentLauncher fragmentLauncher;

    @Override
    public void navigate(UiLauncher uiLauncher) {
        fragmentLauncher = (FragmentLauncher) uiLauncher;
        actContext = fragmentLauncher.getFragmentActivity();
        RestInterface restInterface = getRestClient();
        if (restInterface.isInternetReachable()) {
            CswDependencies dependencies = new CswDependencies(getApplicationContext().getAppInfra());
            PermissionHelper.getInstance().setMyAccountUIEventListener(PrivacySettingsState.this);
            CswInterface cswInterface = getCswInterface();
            UappSettings uappSettings = new UappSettings(actContext);
            cswInterface.init(dependencies, uappSettings);
            cswInterface.launch(fragmentLauncher, buildLaunchInput(true, actContext));
        } else {
            String title = actContext.getString(R.string.MYA_Offline_title);
            String message = actContext.getString(R.string.MYA_Offline_message);
            showDialog(title, message);
        }
    }

    private void showDialog(String title, String message) {
        new DialogView(title, message).showDialog(getFragmentActivity());
    }

    private CswInterface getCswInterface() {
        return new CswInterface();
    }

    private CswLaunchInput buildLaunchInput(boolean addToBackStack, Context context) {
        CswLaunchInput cswLaunchInput = new CswLaunchInput(context, createConsentDefinitions(context));
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
    List<ConsentDefinition> createConsentDefinitions(Context context) {
        AppFrameworkApplication app = (AppFrameworkApplication) context.getApplicationContext();
        final List<ConsentDefinition> consentDefinitions = new ArrayList<>();
        consentDefinitions.addAll(getCATKConsentDefinitions());
        consentDefinitions.add(NeuraConsentProvider.getNeuraConsentDefinition());
        consentDefinitions.add(THSLocationConsentProvider.getTHSConsentDefinition());
        consentDefinitions.add(CcConsentProvider.fetchLocationConsentDefinition());
        consentDefinitions.add(URConsentProvider.fetchMarketingConsentDefinition());
        consentDefinitions.add(getClickStreamConsentDefinition(context));
        app.getAppInfra().getConsentManager().registerConsentDefinitions(consentDefinitions);
        return consentDefinitions;
    }

    private List<ConsentDefinition> getCATKConsentDefinitions() {
        final List<ConsentDefinition> definitions = new ArrayList<>();
        definitions.add(new ConsentDefinition(
                R.string.RA_MYA_Moment_Consent,
                R.string.RA_MYA_Consent_Moment_Help_Text,
                Collections.singletonList("moment"),
                1,
                R.string.RA_MYA_Consent_Moments_Revoke_Warning_Text
        ));
        definitions.add(new ConsentDefinition(
                R.string.RA_MYA_Coaching_Consent,
                R.string.RA_MYA_Consent_Coaching_Help_Text,
                Collections.singletonList("coaching"),
                1,
                R.string.RA_MYA_Consent_Coaching_Revoke_Warning_Text
        ));
        definitions.add(new ConsentDefinition(
                R.string.RA_MYA_Binary_Hosting_Consent,
                R.string.RA_MYA_Consent_Binary_Help_Text,
                Collections.singletonList("binary"),
                1,
                R.string.RA_MYA_Consent_Binary_Revoke_Warning_Text
        ));
        definitions.add(new ConsentDefinition(
                R.string.RA_MYA_Research_Analytics_Consent,
                R.string.RA_MYA_Consent_Research_Analytics_Help_Text,
                Arrays.asList("research", "analytics"),
                1,
                R.string.RA_MYA_Consent_Research_Analytics_Revoke_Warning_Text
        ));
        return definitions;
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

    private ConsentDefinition getClickStreamConsentDefinition(Context context) {
        List<String> consentTypes = new ArrayList<>();
        consentTypes.add(((AppFrameworkApplication) context.getApplicationContext()).getAppInfra().getTagging().getClickStreamConsentIdentifier());
        consentTypes.add(((AppFrameworkApplication) context.getApplicationContext()).getAppInfra().getLogging().getCloudLoggingConsentIdentifier());
        return new ConsentDefinition(R.string.RA_MYA_Click_Stream_Hosting_Consent, R.string.RA_MYA_Consent_Clickstream_Help,
                consentTypes, 1);
    }

    @Override
    public void init(Context context) {
        AppFrameworkApplication app = (AppFrameworkApplication) context.getApplicationContext();

        createConsentDefinitions(context);

        CatkInputs catkInputs = new CatkInputs.Builder()
                .setContext(context)
                .setAppInfraInterface(app.getAppInfra())
                .build();
        CatkInterface catkInterface = new CatkInitializer();
        catkInterface.initCatk(catkInputs);
    }

    @Override
    public void updateDataModel() {

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
            webViewStateData.setTitle(actContext.getString(R.string.csw_privacy_notice));
            baseState.setUiStateData(webViewStateData);
            baseState.navigate(new FragmentLauncher(getFragmentActivity(), R.id.frame_container, (ActionBarListener) getFragmentActivity()));
        }
    }
}
