package com.philips.platform.baseapp.screens.myaccount;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.catk.CatkInputs;
import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.csw.ConsentBundleConfig;
import com.philips.platform.csw.ConsentDefinition;
import com.philips.platform.csw.CswDependencies;
import com.philips.platform.csw.CswInterface;
import com.philips.platform.csw.CswSettings;
import com.philips.platform.csw.CswLaunchInput;
import com.philips.platform.mya.MyaFragment;
import com.philips.platform.mya.interfaces.MyaListener;
import com.philips.platform.mya.launcher.MyaDependencies;
import com.philips.platform.mya.launcher.MyaInterface;
import com.philips.platform.mya.launcher.MyaLaunchInput;
import com.philips.platform.mya.launcher.MyaSettings;
import com.philips.platform.myaplugin.uappadaptor.DataInterface;
import com.philips.platform.myaplugin.uappadaptor.DataModelType;
import com.philips.platform.myaplugin.user.UserDataModelProvider;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyAccountState extends BaseState implements MyaListener{
    public static final String APPLICATION_NAME = "OneBackend";
    public static final String PROPOSITION_NAME = "OneBackendProp";
    private final String SETTINGS_MYA_PRIVACY_SETTINGS = "Mya_Privacy_Settings";
    private static List<ConsentDefinition> consentDefinitionList;

    public MyAccountState() {
        super(AppStates.MY_ACCOUNT);
    }
    Context actContext;
    FragmentLauncher fragmentLauncher;
    @Override
    public void navigate(UiLauncher uiLauncher) {
         fragmentLauncher = (FragmentLauncher)uiLauncher;
         actContext = fragmentLauncher.getFragmentActivity();

        ((AbstractAppFrameworkBaseActivity)actContext).handleFragmentBackStack(null,MyaFragment.TAG,getUiStateData().getFragmentLaunchState());

        MyaLaunchInput launchInput = new MyaLaunchInput(actContext,this);
        launchInput.setContext(actContext);
        launchInput.addToBackStack(true);
        MyaInterface myaInterface = getInterface();
        myaInterface.init(getUappDependencies(actContext), new MyaSettings(actContext.getApplicationContext()));
        myaInterface.launch(fragmentLauncher, launchInput);
    }

    /**
     * <p>Creates a list of ConsentDefinitions</p
     * @param context : can be used to for localized strings <code>context.getString(R.string.consent_definition)</code>
     * @param currentLocale : locale of the strings
     * @return non-null list (may be empty though)
     */
    @VisibleForTesting
    List<ConsentDefinition> createConsentDefinitions(Context context, Locale currentLocale) {
        final List<ConsentDefinition> definitions = new ArrayList<>();
        definitions.add(new ConsentDefinition("I allow Philips to store my data in cloud", "The actual content of the help text here", "moment", 1, currentLocale));
        definitions.add(new ConsentDefinition("I allow Philips to generate insights base on my data", "The actual content of the help text here", "coaching", 1, currentLocale));
        return definitions;
    }

    @Override
    public void init(Context context) {
        CatkInputs catkInputs = new CatkInputs();
        catkInputs.setContext(context);
        catkInputs.setAppInfra(((AppFrameworkApplication)context.getApplicationContext()).appInfra);
        catkInputs.setApplicationName(APPLICATION_NAME);
        catkInputs.setApplicationName(PROPOSITION_NAME);
        ConsentAccessToolKit.getInstance().init(catkInputs);

        consentDefinitionList = createConsentDefinitions(context, Locale.getDefault());
    }


    @Override
    public void updateDataModel() {

    }

    public MyaInterface getInterface() {
        return new MyaInterface();
    }

    @NonNull
    protected MyaDependencies getUappDependencies(Context actContext) {
        MyaDependencies myaDependencies = new MyaDependencies(((AppFrameworkApplication) actContext.getApplicationContext()).getAppInfra());

        return myaDependencies;
    }

    @Override
    public boolean onClickMyaItem(String s) {
        if (s.equals(SETTINGS_MYA_PRIVACY_SETTINGS)) {
            CswInterface cswInterface = new CswInterface();
            CswDependencies cswDependencies = new CswDependencies(((AppFrameworkApplication) actContext.getApplicationContext()).getAppInfra());
            cswDependencies.setApplicationName(APPLICATION_NAME);
            cswDependencies.setPropositionName(PROPOSITION_NAME);
            CswSettings cswSettings = new CswSettings(actContext);
            cswInterface.init(cswDependencies, cswSettings);

            cswInterface.launch(fragmentLauncher, buildLaunchInput(true, actContext));
            return true;
        }

        return false;

    }

    @Override
    public boolean onLogOut() {
        return false;
    }

    @Override
    public DataInterface getDataInterface(DataModelType dataModelType) {
        return new UserDataModelProvider(actContext);

    }
    private CswLaunchInput buildLaunchInput(boolean addToBackStack, Context context) {

        ConsentBundleConfig config = new ConsentBundleConfig(APPLICATION_NAME, PROPOSITION_NAME, createConsentDefinitions(actContext, Locale.US));


        CswLaunchInput cswLaunchInput = new CswLaunchInput(config,context);
        cswLaunchInput.addToBackStack(addToBackStack);
        return cswLaunchInput;
    }
}
