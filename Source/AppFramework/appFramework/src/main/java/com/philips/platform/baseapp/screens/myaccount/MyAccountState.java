package com.philips.platform.baseapp.screens.myaccount;


import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.catk.CatkInputs;
import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.catk.model.ConsentDefinition;
import com.philips.platform.mya.MyaFragment;
import com.philips.platform.mya.error.MyaError;
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

import java.util.ArrayList;
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

        ((AbstractAppFrameworkBaseActivity) actContext).handleFragmentBackStack(null, MyaFragment.TAG, getUiStateData().getFragmentLaunchState());

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
        launchInput.setConsentDefinitions(createConsentDefinitions(actContext, getLocale(actContext)));
        MyaInterface myaInterface = getInterface();
        myaInterface.init(getUappDependencies(actContext), new MyaSettings(actContext.getApplicationContext()));
        myaInterface.launch(fragmentLauncher, launchInput);
    }

    private Locale getLocale(Context actContext) {
        Locale locale;
        if (actContext != null && actContext.getResources() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = actContext.getResources().getConfiguration().getLocales().get(0);
            } else {
                locale = actContext.getResources().getConfiguration().locale;
            }
        } else {
            locale = Locale.US;
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
        return definitions;
    }

    @Override
    public void init(Context context) {
        CatkInputs catkInputs = new CatkInputs();
        catkInputs.setContext(context);
        catkInputs.setAppInfra(((AppFrameworkApplication) context.getApplicationContext()).appInfra);
        ConsentAccessToolKit.getInstance().init(catkInputs);
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
