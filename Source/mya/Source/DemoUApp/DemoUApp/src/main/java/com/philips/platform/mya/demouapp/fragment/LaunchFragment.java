/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.demouapp.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
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

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.catk.CatkInputs;
import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.catk.ConsentInteractor;
import com.philips.platform.consenthandlerinterface.ConsentHandlerMapping;
import com.philips.platform.consenthandlerinterface.datamodel.ConsentDefinition;
import com.philips.platform.mya.MyaHelper;
import com.philips.platform.mya.demouapp.DemoAppActivity;
import com.philips.platform.mya.demouapp.MyAccountDemoUAppInterface;
import com.philips.platform.mya.demouapp.R;
import com.philips.platform.mya.demouapp.theme.fragments.BaseFragment;
import com.philips.platform.mya.error.MyaError;
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
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.urdemo.URDemouAppDependencies;
import com.philips.platform.urdemo.URDemouAppInterface;
import com.philips.platform.urdemo.URDemouAppSettings;

public class LaunchFragment extends BaseFragment implements View.OnClickListener {

    public int checkedId = R.id.radioButton;

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

        Button launch_my_account = (Button) view.findViewById(R.id.launch_my_account);
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
        initCatk();
        MyaDependencies uappDependencies = new MyaDependencies(MyAccountDemoUAppInterface.getAppInfra(), MyaHelper.getInstance().getConsentHandlerMappingList());
        MyaLaunchInput launchInput = new MyaLaunchInput(getActivity(), getMyaListener());
        MyaInterface myaInterface = new MyaInterface();
        myaInterface.init(uappDependencies, new MyaSettings(getActivity()));
        if (checkedId == R.id.radioButton) {
            ActivityLauncher activityLauncher = new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_SENSOR,
                    ((DemoAppActivity) getActivity()).getThemeConfig(),
                    ((DemoAppActivity) getActivity()).getThemeResourceId(), null);
            myaInterface.launch(activityLauncher, launchInput);
        } else {
            myaInterface.launch(new FragmentLauncher(getActivity(), R.id.mainContainer, new ActionBarListener() {
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
    }

    private void initCatk() {
        List<ConsentDefinition> consentDefinitions = createConsentDefinitions(getLocale(MyAccountDemoUAppInterface.getAppInfra()));
        ConsentAccessToolKit kit = ConsentAccessToolKit.getInstance();

        CatkInputs.Builder builder = new CatkInputs.Builder();
        kit.init(builder.setContext(getActivity()
                .getApplicationContext())
                .setConsentDefinitions(consentDefinitions)
                .setAppInfraInterface(MyAccountDemoUAppInterface.getAppInfra())
                .build());

        List<ConsentHandlerMapping> consentHandlerMappingList = new ArrayList<>();
        consentHandlerMappingList.add(new ConsentHandlerMapping(consentDefinitions, new ConsentInteractor(kit)));
        MyaHelper.getInstance().setConfigurations(consentHandlerMappingList);
    }

    private Locale getLocale(AppInfraInterface appInfra) {
        Locale locale = Locale.US;
        if (appInfra != null && appInfra.getInternationalization() != null && appInfra.getInternationalization().getUILocaleString() != null) {
            String[] localeComponents = appInfra.getInternationalization().getUILocaleString().split("_");
            if (localeComponents.length == 2) {
                locale = new Locale(localeComponents[0], localeComponents[1]);
            }
        }
        return locale;
    }

    @NonNull
    private MyaListener getMyaListener() {
        return new MyaListener() {
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
                return LaunchFragment.this.getDataInterface(modelType);
            }

            @Override
            public void onError(MyaError myaError) {
                if (myaError == MyaError.USER_NOT_SIGNED_IN) {
                    Toast.makeText(getActivity(), "User not signed in", Toast.LENGTH_SHORT).show();
                    URDemouAppInterface uAppInterface;
                    uAppInterface = new URDemouAppInterface();
                    AppInfraInterface appInfraInterface = MyAccountDemoUAppInterface.getAppInfra();
                    uAppInterface.init(new URDemouAppDependencies(appInfraInterface), new URDemouAppSettings(getActivity()));
                    uAppInterface.launch(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED, 0), null);
                }
            }
        };
    }

    public DataInterface getDataInterface(DataModelType modelType) {
        if (modelType == DataModelType.USER)
            return new UserDataModelProvider(this.getActivity());

        return null;
    }


    private List<ConsentDefinition> createConsentDefinitions(Locale currentLocale) {
        final List<ConsentDefinition> definitions = new ArrayList<>();
        definitions.add(new ConsentDefinition("I allow Philips to store my data in cloud", "Giving this consent you are allowing Philips to store information related to you", Collections.singletonList("moment"), 1,
                currentLocale));
        definitions.add(new ConsentDefinition("I allow Philips to use my data for Coaching purposes", "Giving this consent you are allowing Philips to store information related to you", Collections.singletonList("coaching"),
                1, currentLocale));
        definitions.add(new ConsentDefinition("I allow Philips to store binary data", "Giving this consent you are allowing Philips to store information related to you", Collections.singletonList("binary"), 1,
                currentLocale));
        definitions.add(new ConsentDefinition("I allow Philips to use my mobile application usage statistics", "Giving this consent you are allowing Philips to process mobile usage statistics related to you", Collections.singletonList("clickstream"), 1,
                currentLocale));
        definitions.add(new ConsentDefinition("I allow Philips to use my data for Research and Analytics purposes", "Research and Analytics purpose explanation", Arrays.asList("research", "analytics"), 1, currentLocale));
        return definitions;

    }

}
