package com.philips.platform.baseapp.screens.myaccount;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.catk.CatkInputs;
import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.catk.model.ConsentDefinition;
import com.philips.platform.mya.base.MyaBaseFragment;
import com.philips.platform.mya.error.MyaError;
import com.philips.platform.mya.interfaces.MyaListener;
import com.philips.platform.mya.launcher.MyaDependencies;
import com.philips.platform.mya.launcher.MyaInterface;
import com.philips.platform.mya.launcher.MyaLaunchInput;
import com.philips.platform.mya.launcher.MyaSettings;
import com.philips.platform.mya.tabs.MyaTabFragment;
import com.philips.platform.myaplugin.uappadaptor.DataInterface;
import com.philips.platform.myaplugin.uappadaptor.DataModelType;
import com.philips.platform.myaplugin.user.UserDataModelProvider;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


public class MyAccountState extends BaseState {
    private final String SETTINGS_MYA_PRIVACY_SETTINGS = "Mya_Privacy_Settings";

    public MyAccountState() {
        super(AppStates.MY_ACCOUNT);
    }

    private Context actContext;
    FragmentLauncher fragmentLauncher;

    @Override
    public void navigate(UiLauncher uiLauncher) {
        fragmentLauncher = (FragmentLauncher) uiLauncher;
        actContext = fragmentLauncher.getFragmentActivity();

        ((AbstractAppFrameworkBaseActivity) actContext).handleFragmentBackStack(null, MyaBaseFragment.MY_ACCOUNTS_CALLEE_TAG, getUiStateData().getFragmentLaunchState());

        MyaLaunchInput launchInput = new MyaLaunchInput(actContext, new MyaListener() {
            @Override
            public boolean onClickMyaItem(String itemName) {
                return false;
            }

            @Override
            public boolean onLogOut() {
                return false;
            }

            @Override
            public DataInterface getDataInterface(DataModelType modelType) {
                return new UserDataModelProvider(actContext);

            }

            @Override
            public void onError(MyaError myaError) {

            }
        });
        launchInput.setContext(actContext);
        launchInput.addToBackStack(true);
        launchInput.setConsentDefinitions(createConsentDefinitions(actContext, getLocale((AppFrameworkApplication) actContext.getApplicationContext())));
        MyaInterface myaInterface = getInterface();
        myaInterface.init(getUappDependencies(actContext), new MyaSettings(actContext.getApplicationContext()));
        myaInterface.launch(fragmentLauncher, launchInput);
    }

    private Locale getLocale(AppFrameworkApplication frameworkApplication) {
        Locale locale = Locale.US;
        if (frameworkApplication != null && frameworkApplication.getAppInfra().getInternationalization() != null && frameworkApplication.getAppInfra().getInternationalization().getUILocaleString() != null) {
            String[] localeComponents = frameworkApplication.getAppInfra().getInternationalization().getUILocaleString().split("_");
            // TODO AppInfra should add method `getHsdpLocaleString()`: getUILocaleString mostly returns just 'en', but we need 'en_US' or 'ca_FR' -> right now, falling back to Locale.US
            if (localeComponents.length == 2) {
                locale = new Locale(localeComponents[0], localeComponents[1]);
            }
        }
        return locale;
    }

    /**
     * <p>Creates a list of ConsentDefinitions</p
     *
     * @param context       : can be used to for localized strings <code>context.getString(R.string.consent_definition)</code>
     * @param currentLocale : locale of the strings
     * @return non-null list (may be empty though)
     */
    @VisibleForTesting
    List<ConsentDefinition> createConsentDefinitions(Context context, Locale currentLocale) {
        final List<ConsentDefinition> definitions = new ArrayList<>();
        definitions.add(new ConsentDefinition(context.getString(R.string.RA_MYA_Consent_Moment_Text), context.getString(R.string.RA_MYA_Consent_Moment_Help), Collections.singletonList("moment"), 1, currentLocale));
        definitions.add(new ConsentDefinition(context.getString(R.string.RA_MYA_Consent_Coaching_Text), context.getString(R.string.RA_MYA_Consent_Coaching_Help), Collections.singletonList("coaching"), 1, currentLocale));
        definitions.add(new ConsentDefinition(context.getString(R.string.RA_MYA_Consent_Binary_Text), context.getString(R.string.RA_MYA_Consent_Binary_Help), Collections.singletonList("binary"), 1, currentLocale));
        definitions.add(new ConsentDefinition(context.getString(R.string.RA_MYA_Consent_Clickstream_Text), context.getString(R.string.RA_MYA_Consent_Clickstream_Help), Collections.singletonList("clickstream"), 1, currentLocale));
        definitions.add(new ConsentDefinition("I allow Philips to use my data for Research and Analytics purposes", "Research and Analytics purpose explanation", Arrays.asList("research", "analytics"), 1, currentLocale));
        return definitions;
    }


    @Override
    public void init(Context context) {
        AppFrameworkApplication app = (AppFrameworkApplication) context.getApplicationContext();
        ConsentAccessToolKit.getInstance().init(new CatkInputs.Builder()
                .setContext(context)
                .setAppInfraInterface(app.getAppInfra())
                .setConsentDefinitions(createConsentDefinitions(context, getLocale(app))).build());
    }


    @Override
    public void updateDataModel() {

    }

    public MyaInterface getInterface() {
        return new MyaInterface();
    }

    @NonNull
    protected MyaDependencies getUappDependencies(Context actContext) {

        return new MyaDependencies(((AppFrameworkApplication) actContext.getApplicationContext()).getAppInfra());
    }
}
