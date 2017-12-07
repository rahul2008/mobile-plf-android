/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.demoapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.configuration.RuntimeConfiguration;
import com.philips.cdp2.commlib.lan.context.LanTransportContext;
import com.philips.cdp2.ews.appliance.BEApplianceFactory;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.configuration.ContentConfiguration;
import com.philips.cdp2.ews.configuration.HappyFlowContentConfiguration;
import com.philips.cdp2.ews.configuration.TroubleShootContentConfiguration;
import com.philips.cdp2.ews.microapp.EWSActionBarListener;
import com.philips.cdp2.ews.microapp.EWSDependencies;
import com.philips.cdp2.ews.microapp.EWSInterface;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappSettings;

import java.util.HashMap;
import java.util.Map;

import static com.philips.platform.uappframework.launcher.ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT;

public class OptionSelectionFragment extends Fragment implements View.OnClickListener {

    private Spinner configSpinner;

    // Constants for configuration value
    private static final String WAKEUP_LIGHT = "Wakeup Light";
    private static final String AIRPURIFIER = "Air Purifier";
    private static final String DEFAULT = "Default";
    private AppInfraInterface appInfra;

    @Nullable
    private static CommCentral commCentral;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_ewsdemo, container, false);
        view.findViewById(R.id.btnLaunchEws).setOnClickListener(this);
        view.findViewById(R.id.btnTheme).setOnClickListener(this);
        view.findViewById(R.id.btnLaunchFragmentEws).setOnClickListener(this);
        appInfra = new AppInfra.Builder().build(getActivity());
        configSpinner = view.findViewById(R.id.configurationSelection);
        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.configurations));
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        configSpinner.setAdapter(aa);
        return view;

    }

    @Override


    public void onResume() {
        super.onResume();
        ((EWSActionBarListener) getActivity()).updateActionBar(getString(R.string.app_name), true);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnLaunchEws) {
            launchEwsUApp();
        } else if (i == R.id.btnLaunchFragmentEws) {
            launchEWSFragmentUApp();
        } else if (i == R.id.btnTheme) {
            ((EWSDemoUActivity) getActivity()).openThemeScreen();
        }
    }

    private void launchEwsUApp() {
        EWSInterface ewsInterface = new EWSInterface();
        ewsInterface.init(createUappDependencies(appInfra, createProductMap()), new UappSettings(getActivity()));
        //its up to proposition to pass theme or not, if not passing theme then it will show default theme of library
        ewsInterface.launch(new ActivityLauncher(SCREEN_ORIENTATION_PORTRAIT, ((EWSDemoUActivity) getActivity()).getThemeConfig(), -1, null), ((EWSDemoUActivity) getActivity()).getEwsLauncherInput());
    }

    private void launchEWSFragmentUApp() {
        EWSInterface ewsInterface = new EWSInterface();
        ewsInterface.init(createUappDependencies(appInfra, createProductMap()), new UappSettings(getActivity()));
        FragmentLauncher fragmentLauncher = new FragmentLauncher
                (getActivity(), R.id.mainContainer, ((ActionBarListener) getActivity()));
        ewsInterface.launch(fragmentLauncher, ((EWSDemoUActivity) getActivity()).getEwsLauncherInput());
    }

    /**
     *
     * Singleton instance for commCentral is necessary as only one instance is allowed for CommCentral.
     * @param context
     * @param appInfraInterface
     * @return Singleton commCental to be passed to micro app
     */
    @NonNull
    private CommCentral createCommCentral(Context context, AppInfraInterface appInfraInterface) {
        if (commCentral == null){
            LanTransportContext lanTransportContext = new LanTransportContext(
                    new RuntimeConfiguration(context, appInfraInterface));
            BEApplianceFactory factory = new BEApplianceFactory(lanTransportContext);
            commCentral =  new CommCentral(factory, lanTransportContext);
        }
        return commCentral;
    }

    /**
     * create uApp dependency from proposition for EWS microapp.
     * commCentral should be created and passed from proposition.
     * @param appInfra
     * @param productKeyMap
     * @return
     */
    @NonNull
    private UappDependencies createUappDependencies(final AppInfraInterface appInfra,
                                                    Map<String, String> productKeyMap) {
        return new EWSDependencies(appInfra, productKeyMap,
                new ContentConfiguration(createBaseContentConfiguration(),
                        createHappyFlowConfiguration(),
                        createTroubleShootingConfiguration())) {
            @Override
            public CommCentral getCommCentral() {
                return  createCommCentral(getActivity(), appInfra);
            }
        };
    }


    @NonNull
    private Map<String, String> createProductMap() {
        Map<String, String> productKeyMap = new HashMap<>();
        switch ((String)configSpinner.getSelectedItem()){
            case DEFAULT:
                productKeyMap.put(EWSInterface.PRODUCT_NAME, getString(R.string.ews_device_name_default));
                break;
            case WAKEUP_LIGHT:
                productKeyMap.put(EWSInterface.PRODUCT_NAME, getString(R.string.ews_device_name_wl));
                break;
            case AIRPURIFIER:
                productKeyMap.put(EWSInterface.PRODUCT_NAME, getString(R.string.ews_device_name_ap));
                break;

        }
        return productKeyMap;
    }

    @NonNull
    private BaseContentConfiguration createBaseContentConfiguration() {
        switch ((String)configSpinner.getSelectedItem()){
            case WAKEUP_LIGHT:
                return new BaseContentConfiguration(R.string.ews_device_name_wl, R.string.ews_app_name_wl);
            case AIRPURIFIER:
                return new BaseContentConfiguration(R.string.ews_device_name_ap, R.string.ews_app_name_ap);
            case DEFAULT:
            default:
                return new BaseContentConfiguration();
        }
    }

    @NonNull
    private HappyFlowContentConfiguration createHappyFlowConfiguration() {
        switch ((String)configSpinner.getSelectedItem()){
            case WAKEUP_LIGHT:
                return new HappyFlowContentConfiguration.Builder()
                        .setGettingStartedScreenTitle(R.string.label_ews_get_started_title_wl)
                        .setSetUpScreenTitle(R.string.label_ews_plug_in_title_wl)
                        .setSetUpScreenBody(R.string.label_ews_plug_in_body_wl)
                        .setSetUpVerifyScreenTitle(R.string.label_ews_verify_ready_title_wl)
                        .setSetUpVerifyScreenQuestion(R.string.label_ews_verify_ready_question_wl)
                        .setSetUpVerifyScreenYesButton(R.string.button_ews_verify_ready_yes_wl)
                        .setSetUpVerifyScreenNoButton(R.string.button_ews_verify_ready_no_wl)
                        .setGettingStartedScreenImage(R.drawable.ews_start_wl)
                        .setSetUpScreenImage(R.drawable.ews_setup_wl)
                        .setSetUpVerifyScreenImage(R.drawable.ews_setup_wl)
                        .build();
            case AIRPURIFIER:
                return new HappyFlowContentConfiguration.Builder()
                        .setGettingStartedScreenTitle(R.string.label_ews_get_started_title_ap)
                        .setSetUpScreenTitle(R.string.label_ews_plug_in_title_ap)
                        .setSetUpScreenBody(R.string.label_ews_plug_in_body_ap)
                        .setSetUpVerifyScreenTitle(R.string.label_ews_verify_ready_title_ap)
                        .setSetUpVerifyScreenQuestion(R.string.label_ews_verify_ready_question_ap)
                        .setSetUpVerifyScreenYesButton(R.string.button_ews_verify_ready_yes_ap)
                        .setSetUpVerifyScreenNoButton(R.string.button_ews_verify_ready_no_ap)
                        .setGettingStartedScreenImage(R.drawable.ews_start_ap)
                        .setSetUpScreenImage(R.drawable.ews_setup_ap)
                        .setSetUpVerifyScreenImage(R.drawable.ews_setup_ap)
                        .build();
            case DEFAULT:
            default:
                return new HappyFlowContentConfiguration.Builder().build();
        }
    }

    @NonNull
    private TroubleShootContentConfiguration createTroubleShootingConfiguration() {
        switch ((String)configSpinner.getSelectedItem()){
            case WAKEUP_LIGHT:
                return new TroubleShootContentConfiguration.Builder()
                        .setResetConnectionTitle(R.string.label_ews_support_reset_connection_title_wl)
                        .setResetConnectionBody(R.string.label_ews_support_reset_connection_body_wl)
                        .setResetConnectionImage(R.drawable.ews_support_wl)

                        .setResetDeviceTitle(R.string.label_ews_support_reset_device_title_wl)
                        .setResetDeviceBody(R.string.label_ews_support_reset_device_body_wl)
                        .setResetDeviceImage(R.drawable.ews_support_wl)

                        .setSetUpAccessPointTitle(R.string.label_ews_support_setup_access_point_title_wl)
                        .setSetUpAccessPointBody(R.string.label_ews_support_setup_access_point_body_wl)
                        .setSetUpAccessPointImage(R.drawable.ews_support_wl)

                        .setConnectWrongPhoneTitle(R.string.label_ews_connect_to_wrongphone_title_wl)
                        .setConnectWrongPhoneBody(R.string.label_ews_connect_to_wrongphone_body_wl)
                        .setConnectWrongPhoneImage(R.drawable.ews_support_wl)
                        .setConnectWrongPhoneQuestion(R.string.label_ews_connect_to_wrongphone_question_wl)
                        .build();
            case AIRPURIFIER:
                return new TroubleShootContentConfiguration.Builder()
                        .setResetConnectionTitle(R.string.label_ews_support_reset_connection_title_ap)
                        .setResetConnectionBody(R.string.label_ews_support_reset_connection_body_ap)
                        .setResetConnectionImage(R.drawable.ews_support_ap)

                        .setResetDeviceTitle(R.string.label_ews_support_reset_device_title_ap)
                        .setResetDeviceBody(R.string.label_ews_support_reset_device_body_ap)
                        .setResetDeviceImage(R.drawable.ews_support_ap)

                        .setSetUpAccessPointTitle(R.string.label_ews_support_setup_access_point_title_ap)
                        .setSetUpAccessPointBody(R.string.label_ews_support_setup_access_point_body_ap)
                        .setSetUpAccessPointImage(R.drawable.ews_support_ap)

                        .setConnectWrongPhoneTitle(R.string.label_ews_connect_to_wrongphone_title_ap)
                        .setConnectWrongPhoneBody(R.string.label_ews_connect_to_wrongphone_body_ap)
                        .setConnectWrongPhoneImage(R.drawable.ews_support_ap)
                        .setConnectWrongPhoneQuestion(R.string.label_ews_connect_to_wrongphone_question_ap)
                        .build();
            case DEFAULT:
            default:
                return new TroubleShootContentConfiguration.Builder().build();
        }
    }
}
