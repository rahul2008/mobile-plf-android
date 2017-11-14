package com.philips.platform.baseapp.screens.myaccount;


import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.csw.ConsentDefinition;
import com.philips.platform.mya.MyaDependencies;
import com.philips.platform.mya.MyaFragment;
import com.philips.platform.mya.MyaInterface;
import com.philips.platform.mya.MyaLaunchInput;
import com.philips.platform.mya.MyaSettings;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyAccountState extends BaseState {
    public static final String APPLICATION_NAME = "OneBackend";
    public static final String PROPOSITION_NAME = "OneBackendProp";
    private List<ConsentDefinition> consentDefinitionList;

    public MyAccountState() {
        super(AppStates.MY_ACCOUNT);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        FragmentLauncher fragmentLauncher = (FragmentLauncher)uiLauncher;
        Context actContext = fragmentLauncher.getFragmentActivity();

        ((AbstractAppFrameworkBaseActivity)actContext).handleFragmentBackStack(null,MyaFragment.TAG,getUiStateData().getFragmentLaunchState());

        MyaLaunchInput launchInput = new MyaLaunchInput();
        launchInput.setContext(actContext);
        launchInput.addToBackStack(true);
        launchInput.setConsentDefinition(consentDefinitionList);
        MyaInterface myaInterface = getInterface();
        myaInterface.init(getUappDependencies(actContext), new MyaSettings(actContext.getApplicationContext()));
        myaInterface.launch(fragmentLauncher, launchInput);
    }

    private List<ConsentDefinition> getConsentDefinitions(Context context, Locale currentLocale) {
        final List<ConsentDefinition> definitions = new ArrayList<>();
        definitions.add(new ConsentDefinition("I allow Philips to store my data in cloud", "The actual content of the help text here", "moment", 1, currentLocale));
        return definitions;
    }

    @Override
    public void init(Context context) {
        consentDefinitionList = getConsentDefinitions(context, Locale.getDefault());
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
        myaDependencies.setApplicationName(APPLICATION_NAME);
        myaDependencies.setPropositionName(PROPOSITION_NAME);
        return myaDependencies;
    }
}
