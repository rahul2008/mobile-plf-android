package com.philips.platform.mya.demouapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.philips.cdp.registration.User;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.catk.CatkInputs;
import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.catk.model.ConsentDefinition;
import com.philips.platform.csw.ConsentBundleConfig;
import com.philips.platform.csw.CswDependencies;
import com.philips.platform.csw.CswInterface;
import com.philips.platform.csw.CswLaunchInput;
import com.philips.platform.mya.demouapp.DemoAppActivity;
import com.philips.platform.mya.demouapp.MyAccountDemoUAppInterface;
import com.philips.platform.mya.demouapp.R;
import com.philips.platform.mya.demouapp.theme.fragments.BaseFragment;
import com.philips.platform.mya.interfaces.MyaListener;
import com.philips.platform.mya.launcher.MyaDependencies;
import com.philips.platform.mya.launcher.MyaInterface;
import com.philips.platform.mya.launcher.MyaLaunchInput;
import com.philips.platform.mya.launcher.MyaSettings;
import com.philips.platform.myaplugin.uappadaptor.DataInterface;
import com.philips.platform.myaplugin.uappadaptor.DataModelType;
import com.philips.platform.myaplugin.user.UserDataModelProvider;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.uappinput.UappSettings;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static com.philips.platform.mya.demouapp.MyaConstants.APPLICATION_NAME;
import static com.philips.platform.mya.demouapp.MyaConstants.PROPOSITION_NAME;


/**
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
public class LaunchFragment extends BaseFragment implements View.OnClickListener {

    public int checkedId = R.id.radioButton;
    List<ConsentDefinition> createConsentDefinitions;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.demo_u_app_layout, container, false);
        setUp(view);
        return view;
    }

    private void setUp(final View view) {
        initViews(view);
    }



    private void initViews(final View view) {
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                LaunchFragment.this.checkedId = checkedId;
            }
        });

        Button launch_my_account=(Button)view.findViewById(R.id.launch_my_account);
        launch_my_account.setOnClickListener(this);
    }


    protected ThemeConfiguration getDLSThemeConfiguration(Context context) {
        return new ThemeConfiguration(context, ColorRange.ORANGE, ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT, AccentRange.ORANGE);
    }


    @Override
    public int getPageTitle() {
        return 0;
    }


    @Override
    public void onClick(View v) {
        MyaDependencies uappDependencies = new MyaDependencies(MyAccountDemoUAppInterface.getAppInfra());

        MyaLaunchInput launchInput = new MyaLaunchInput(((DemoAppActivity) getActivity()), getMyaListener());

        MyaInterface myaInterface = new MyaInterface();

        myaInterface.init(uappDependencies, new MyaSettings((DemoAppActivity) getActivity()));

        if(new User(getActivity()).isUserSignIn()) {
            if (checkedId == R.id.radioButton) {
                ActivityLauncher activityLauncher = new ActivityLauncher(ActivityLauncher.
                        ActivityOrientation.SCREEN_ORIENTATION_SENSOR, ((DemoAppActivity) getActivity()).getThemeConfig(),
                        ((DemoAppActivity) getActivity()).getThemeResourceId(), null);

                myaInterface.launch(activityLauncher, launchInput);
            } else {
                myaInterface.launch(new FragmentLauncher((DemoAppActivity) getActivity(), R.id.mainContainer, new ActionBarListener() {
                    @Override
                    public void updateActionBar(@StringRes int i, boolean b) {
                        ((DemoAppActivity) getActivity()).setTitle(i);
                    }

                    @Override
                    public void updateActionBar(String s, boolean b) {
                        ((DemoAppActivity) getActivity()).setTitle(s);
                    }
                }), launchInput);
            }
        } else
            Toast.makeText(getActivity(), "User not signed in", Toast.LENGTH_SHORT).show();
    }

    @NonNull
    private MyaListener getMyaListener() {
        return new MyaListener() {
            @Override
            public boolean onClickMyaItem(String itemName) {
                if (itemName.equals("Mya_Privacy_Settings")) {
                    ConsentAccessToolKit.getInstance().init(initConsentToolKit("OneBackend", "OneBackendProp", getContext(), MyAccountDemoUAppInterface.getAppInfra()));
                    CswInterface cswInterface = new CswInterface();
                    CswDependencies cswDependencies = new CswDependencies(MyAccountDemoUAppInterface.getAppInfra());
                    UappSettings uappSettings = new UappSettings(getContext());
                    cswInterface.init(cswDependencies, uappSettings);
                    FragmentLauncher fragmentLauncher = new FragmentLauncher(getActivity(), R.id.mainContainer, null);
                    cswInterface.launch(fragmentLauncher, buildLaunchInput(true, getContext()));
                    return true;
                }
                return false;
            }

            @Override
            public boolean onLogOut() {
                return false;
            }

            @Override
            public DataInterface getDataInterface(DataModelType modelType) {
                return LaunchFragment.this.getDataInterface();
            }
        };
    }

    public CatkInputs initConsentToolKit(String applicationName, String propostionName, Context context, AppInfraInterface appInfra) {
        CatkInputs catkInputs = new CatkInputs();
        catkInputs.setContext(context);
        catkInputs.setAppInfra(appInfra);
        catkInputs.setApplicationName(applicationName);
        catkInputs.setPropositionName(propostionName);
        return catkInputs;
    }

    private CswLaunchInput buildLaunchInput(boolean addToBackStack, Context context) {
        ConsentBundleConfig config = new ConsentBundleConfig(APPLICATION_NAME, PROPOSITION_NAME, createConsentDefinitions(getContext(), Locale.US));
        CswLaunchInput cswLaunchInput = new CswLaunchInput(config,context);
        cswLaunchInput.addToBackStack(addToBackStack);
        return cswLaunchInput;
    }

    public DataInterface getDataInterface() {
        return new UserDataModelProvider(this.getActivity());
    }
    private List<ConsentDefinition> createConsentDefinitions(Context context, Locale currentLocale) {
        final List<ConsentDefinition> definitions = new ArrayList<>();
        definitions.add(new ConsentDefinition("I allow Philips to store my data in cloud", "The actual content of the help text here", Collections.singletonList("moment"), 1, currentLocale));
        definitions.add(new ConsentDefinition("I allow don't Philips to store my data in cloud", "No one is able to see this text in the app", Collections.singletonList("tnemom"), 1, currentLocale));
        return definitions;

    }
}
