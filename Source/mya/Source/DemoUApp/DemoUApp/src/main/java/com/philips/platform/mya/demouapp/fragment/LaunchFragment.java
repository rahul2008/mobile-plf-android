/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.demouapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationLaunchMode;
import com.philips.cdp.registration.consents.URConsentProvider;
import com.philips.cdp.registration.handlers.LogoutHandler;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URLaunchInput;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.mya.MyaTabConfig;
import com.philips.platform.catk.CatkInputs;
import com.philips.platform.catk.ConsentsClient;
import com.philips.platform.csw.CswDependencies;
import com.philips.platform.csw.CswInterface;
import com.philips.platform.csw.CswLaunchInput;
import com.philips.platform.csw.dialogs.DialogView;
import com.philips.platform.csw.permission.MyAccountUIEventListener;
import com.philips.platform.csw.permission.PermissionHelper;
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
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.uappinput.UappSettings;
import com.philips.platform.urdemo.URDemouAppDependencies;
import com.philips.platform.urdemo.URDemouAppInterface;
import com.philips.platform.urdemo.URDemouAppSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class LaunchFragment extends BaseFragment implements View.OnClickListener,MyAccountUIEventListener {

    public int checkedId = R.id.radioButton;
    Context context;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.demo_u_app_layout, container, false);
        setUp(view);
        init();
        return view;
    }

    private void setUp(final View view) {
        initViews(view);
    }

    private void initViews(final View view) {
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        context = getActivity();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                LaunchFragment.this.checkedId = checkedId;
            }
        });

        Button launch_my_account = (Button) view.findViewById(R.id.launch_my_account);
        launch_my_account.setOnClickListener(this);
    }

    @Override
    public int getPageTitle() {
        return 0;
    }

    @Override
    public void onClick(View v) {

            MyaLaunchInput launchInput = new MyaLaunchInput(getContext());
            MyaTabConfig myaTabConfig = new MyaTabConfig(getString(R.string.mya_config_tab), new TabTestFragment());
            String[] profileItems = {"MYA_My_details","Test_fragment"};
            String[] settingItems = {"MYA_Country", "MYA_Privacy_Settings","Test_fragment"};
            launchInput.setProfileMenuList(Arrays.asList(profileItems));
            launchInput.setSettingsMenuList(Arrays.asList(settingItems));
            launchInput.setMyaListener(getMyaListener());
            launchInput.setUserDataInterface(MyAccountDemoUAppInterface.getUserDataInterface());
            launchInput.setMyaTabConfig(myaTabConfig);
            MyaInterface myaInterface = new MyaInterface();
            myaInterface.init(getUappDependencies(), new MyaSettings(getContext()));
            if (checkedId == R.id.radioButton) {
                ActivityLauncher activityLauncher = new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_SENSOR,
                        ((DemoAppActivity) getActivity()).getThemeConfig(),
                        ((DemoAppActivity) getActivity()).getThemeResourceId(), null);
                myaInterface.launch(activityLauncher, launchInput);
            } else {
                myaInterface.launch(new FragmentLauncher(getActivity(), R.id.mainContainer, new ActionBarListener() {
                    @Override
                    public void updateActionBar(@StringRes int i, boolean b) {
                        DemoAppActivity activity = (DemoAppActivity) getActivity();
                        if (activity != null) activity.setTitle(i);
                    }

                    @Override
                    public void updateActionBar(String s, boolean b) {
                        DemoAppActivity activity = (DemoAppActivity) getActivity();
                        if (activity != null)
                            ((DemoAppActivity) getActivity()).setTitle(s);
                    }
                }), launchInput);
            }

    }

    private MyaListener getMyaListener() {
        return new MyaListener() {
            @Override
            public boolean onSettingsMenuItemSelected(final FragmentLauncher fragmentLauncher, String itemName) {
                if (fragmentLauncher != null) {
                    Activity activity = fragmentLauncher.getFragmentActivity();
                    if (itemName.equalsIgnoreCase(activity.getString(com.philips.platform.mya.R.string.MYA_Logout))) {

                    } else if (itemName.equals("MYA_Privacy_Settings")) {
                        RestInterface restInterface = getRestClient();
                        if (restInterface.isInternetReachable()) {
                            CswDependencies dependencies = new CswDependencies(MyAccountDemoUAppInterface.getAppInfra());
                            PermissionHelper.getInstance().setMyAccountUIEventListener(LaunchFragment.this);
                            CswInterface cswInterface = getCswInterface();
                            UappSettings uappSettings = new UappSettings(activity);
                            cswInterface.init(dependencies, uappSettings);
                            cswInterface.launch(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_FULL_SENSOR,  ((DemoAppActivity) getActivity()).getThemeConfig(),
                                    ((DemoAppActivity) getActivity()).getThemeResourceId(), null), buildLaunchInput(true, activity));
                            return true;
                        } else {
                            String title = activity.getString(com.philips.platform.mya.R.string.MYA_Offline_title);
                            String message = activity.getString(com.philips.platform.mya.R.string.MYA_Offline_message);
                            showDialog(activity, title, message);
                        }
                    } else if (itemName.equalsIgnoreCase("Test_fragment")) {
                        launchTestFragment(fragmentLauncher);
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean onProfileMenuItemSelected(final FragmentLauncher fragmentLauncher, String itemName) {
                if (fragmentLauncher != null) {
                    if (itemName.equalsIgnoreCase("MYA_My_details")) {
                        URLaunchInput urLaunchInput = new URLaunchInput();
                        urLaunchInput.enableAddtoBackStack(true);
                        urLaunchInput.setEndPointScreen(RegistrationLaunchMode.USER_DETAILS);
                        URInterface urInterface = new URInterface();
                        ActivityLauncher activityLauncher = new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_SENSOR,
                                ((DemoAppActivity) getActivity()) != null ? ((DemoAppActivity) getActivity()).getThemeConfig() : null,
                                ((DemoAppActivity) getActivity()) != null ? ((DemoAppActivity) getActivity()).getThemeResourceId() : -1, null);
                        urInterface.launch(activityLauncher, urLaunchInput);
                        return true;
                    } else if (itemName.equalsIgnoreCase("Test_fragment")) {
                        launchTestFragment(fragmentLauncher);
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void onError(MyaError myaError) {
                if (myaError == MyaError.USERNOTLOGGEDIN) {
                    Toast.makeText(getActivity(), "User not signed in", Toast.LENGTH_SHORT).show();
                    URDemouAppInterface uAppInterface;
                    uAppInterface = new URDemouAppInterface();
                    AppInfraInterface appInfraInterface = MyAccountDemoUAppInterface.getAppInfra();
                    uAppInterface.init(new URDemouAppDependencies(appInfraInterface), new URDemouAppSettings(getActivity()));
                    ActivityLauncher activityLauncher = new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_SENSOR,
                            ((DemoAppActivity) getActivity()).getThemeConfig(),
                            ((DemoAppActivity) getActivity()).getThemeResourceId(), null);
                    uAppInterface.launch(activityLauncher, null);
                }
            }


            @Override
            public void onLogoutClicked(final FragmentLauncher fragmentLauncher, final MyaLogoutListener myaLogoutListener) {
                final FragmentActivity activity = fragmentLauncher.getFragmentActivity();
                if (activity != null) {
                    User user = new User(activity);
                    if (user.isUserSignIn()) {
                        user.logout(new LogoutHandler() {
                            @Override
                            public void onLogoutSuccess() {
                                myaLogoutListener.onLogoutSuccess();
                            }

                            @Override
                            public void onLogoutFailure(int responseCode, String message) {
                                String string = activity.getString(R.string.mya_logout_failed);
                                new DialogView().showDialog((FragmentActivity) activity, string, message);
                            }
                        });
                    }

                }
            }
        };
    }

    void launchTestFragment(FragmentLauncher fragmentLauncher) {
        FragmentActivity fragmentActivity = fragmentLauncher.getFragmentActivity();
        FragmentTransaction fragmentTransaction = fragmentActivity
                .getSupportFragmentManager().beginTransaction();
        TabTestFragment fragment = new TabTestFragment();
        final String simpleName = fragment.getClass().getSimpleName();
        fragmentTransaction.replace(fragmentLauncher.getParentContainerResourceID(), fragment, simpleName);
        fragmentTransaction.addToBackStack(simpleName);
        fragmentTransaction.commitAllowingStateLoss();
    }


    @NonNull
    protected MyaDependencies getUappDependencies() {
        return new MyaDependencies(MyAccountDemoUAppInterface.getAppInfra());
    }

    private RestInterface getRestClient() {
        return MyAccountDemoUAppInterface.getAppInfra().getRestClient();
    }

    @Override
    public void onPrivacyNoticeClicked() {

    }

    private CswInterface getCswInterface() {
        return new CswInterface();
    }

    private void init() {
        CatkInputs catkInputs = new CatkInputs.Builder()
                .setContext(getContext())
                .setAppInfraInterface(MyAccountDemoUAppInterface.getAppInfra())
                .build();
        ConsentsClient.getInstance().init(catkInputs);
        List<ConsentDefinition> urDefinitions = createUserRegistrationDefinitions(getContext());
    }

    private ConsentDefinition getClickStreamConsentDefinition(Context context) {
        return new ConsentDefinition(R.string.RA_MYA_Click_Stream_Hosting_Consent, R.string.RA_MYA_Consent_Clickstream_Help,
                Collections.singletonList(MyAccountDemoUAppInterface.getAppInfra().getTagging().getClickStreamConsentIdentifier()), 1);
    }


    private List<ConsentDefinition> createUserRegistrationDefinitions(Context context) {
        final List<ConsentDefinition> definitions = new ArrayList<>();
        return definitions;
    }

    private void showDialog(Activity activity, String title, String message) {
        new DialogView().showDialog((FragmentActivity) activity, title, message);
    }

    private CswLaunchInput buildLaunchInput(boolean addToBackStack, Context context) {
        CswLaunchInput cswLaunchInput = new CswLaunchInput(context,createConsentDefinitions(context));
        cswLaunchInput.addToBackStack(addToBackStack);
        return cswLaunchInput;
    }

    @VisibleForTesting
    List<ConsentDefinition> createConsentDefinitions(Context context) {
        final List<ConsentDefinition> consentDefinitions = new ArrayList<>();
        consentDefinitions.add(URConsentProvider.fetchMarketingConsentDefinition());
        consentDefinitions.add(getClickStreamConsentDefinition(context));
        MyAccountDemoUAppInterface.getAppInfra().getConsentManager().registerConsentDefinitions(consentDefinitions);
        return consentDefinitions;
    }

}
