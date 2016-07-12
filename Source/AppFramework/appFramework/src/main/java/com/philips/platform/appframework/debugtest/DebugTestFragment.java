/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.platform.appframework.debugtest;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.Flow;
import com.philips.cdp.registration.configuration.JanRainConfiguration;
import com.philips.cdp.registration.configuration.PILConfiguration;
import com.philips.cdp.registration.configuration.RegistrationClientId;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.configuration.RegistrationDynamicConfiguration;
import com.philips.cdp.registration.configuration.SigninProviders;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.tagging.Tagging;
import com.philips.platform.appframework.AppFrameworkBaseFragment;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DebugTestFragment extends AppFrameworkBaseFragment {

    String configurationType[] = {"Evaluation", "Testing", "Development", "Staging", "Production"};
    int count = 0;
    List<String> list = Arrays.asList(configurationType);
    private String TAG = getClass().toString();
    private TextView txt_title, configurationTextView;
    private Spinner spinner;
    private ListView listview;
    private SharedPreferences sharedPreferences;
    private Context context;
    private TextView version;

    @Override
    public String getActionbarTitle() {
        return "Debug and Test ";
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.debug_test, container, false);
        setUp(view);
        return view;
    }

    private void setUp(final View view) {
        context = getActivity();
        final String PRODUCT_REGISTRATION = "prod_demo";
        sharedPreferences = context.getSharedPreferences(PRODUCT_REGISTRATION, Context.MODE_PRIVATE);
        initViews(view);
        setSpinnerAdaptor();
        final int position = list.indexOf(sharedPreferences.getString("reg_env", "Evaluation"));
        setSpinnerSelection(position);
        spinner.setOnItemSelectedListener(getSpinnerListener());
    }

    @NonNull
    private AdapterView.OnItemSelectedListener getSpinnerListener() {
        return new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                final String configuration = adapter.getItemAtPosition(position).toString();
                ((TextView) adapter.getChildAt(0)).setTextColor(Color.WHITE);
                if (count > 0) {
                    User user = new User(context);
                    user.logout(null);
                    Log.d(TAG, "Before Configuration" + configuration);
                    if (configuration.equalsIgnoreCase("Development")) {
                        initialiseUserRegistration("Development");
                    } else if (configuration.equalsIgnoreCase("Testing")) {
                        initialiseUserRegistration("Testing");
                    } else if (configuration.equalsIgnoreCase("Evaluation")) {
                        initialiseUserRegistration("Evaluation");
                    } else if (configuration.equalsIgnoreCase("Staging")) {
                        initialiseUserRegistration("Staging");
                    } else if (configuration.equalsIgnoreCase("Production")) {
                        initialiseUserRegistration("Production");
                    }
                    Log.d(TAG, "After Configuration" + configuration);
                    configurationTextView.setText(configuration);
                }
                count++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        };
    }

    private void setSpinnerSelection(final int position) {
        if (position >= 0) {
            spinner.setSelection(position);

            configurationTextView.setText(configurationType[position]);
            configurationTextView.setTextColor(getResources().getColor(R.color.white));
        } else {
            configurationTextView.setText(configurationType[0]);
            configurationTextView.setTextColor(getResources().getColor(R.color.white));
        }
    }

    private void setSpinnerAdaptor() {
        final ArrayAdapter<String> configType = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, configurationType);
        configType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(configType);
    }

    private void initViews(final View view) {
        version = (TextView) view.findViewById(R.id.version);

        version.setText(" App Version " + BuildConfig.VERSION_NAME);

        spinner = (Spinner) view.findViewById(R.id.spinner);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        configurationTextView = (TextView) view.findViewById(R.id.configuration);
    }


    private void initialiseUserRegistration(final String development) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("reg_env", development);
        editor.commit();
        final JanRainConfiguration janRainConfiguration = new JanRainConfiguration();
        final RegistrationClientId registrationClientId = new RegistrationClientId();
        registrationClientId.setDevelopmentId("ad7nn99y2mv5berw5jxewzagazafbyhu");
        registrationClientId.setEvaluationId("4r36zdbeycca933nufcknn2hnpsz6gxu");
        registrationClientId.setProductionId("mz6tg5rqrg4hjj3wfxfd92kjapsrdhy3");
        registrationClientId.setStagingId("f2stykcygm7enbwfw2u9fbg6h6syb8yd");
        registrationClientId.setTestingId("xru56jcnu3rpf8q7cgnkr7xtf9sh8pp7");
        janRainConfiguration.setClientIds(registrationClientId);
        RegistrationDynamicConfiguration.getInstance().setJanRainConfiguration(janRainConfiguration);

        PILConfiguration pilConfiguration = new PILConfiguration();
        pilConfiguration.setMicrositeId("77000");
        pilConfiguration.setRegistrationEnvironment(development);
        RegistrationDynamicConfiguration.getInstance().setPilConfiguration(pilConfiguration);
        Flow flow = new Flow();
        flow.setEmailVerificationRequired(true);
        flow.setTermsAndConditionsAcceptanceRequired(true);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("NL", "16");
        hashMap.put("GB", "16");
        hashMap.put("default", "16");
        flow.setMinAgeLimit(hashMap);
        RegistrationDynamicConfiguration.getInstance().setFlow(flow);
        SigninProviders signinProviders = new SigninProviders();
        HashMap<String, ArrayList<String>> providers = new HashMap<>();
        ArrayList<String> defaultSignInProviders = new ArrayList<>();
        defaultSignInProviders.add("facebook");
        defaultSignInProviders.add("googleplus");
        providers.put("default", defaultSignInProviders);

        signinProviders.setProviders(providers);
        RegistrationDynamicConfiguration.getInstance().setSignInProviders(signinProviders);

        initRegistration();

    }

    private void initRegistration() {

        Tagging.enableAppTagging(true);
        Tagging.setTrackingIdentifier("integratingApplicationAppsId");
        Tagging.setLaunchingPageName("demo_app_home");
        RegistrationConfiguration.getInstance().setPrioritisedFunction(RegistrationFunction.Registration);

        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();

        PILLocaleManager localeManager = new PILLocaleManager(context);
        localeManager.setInputLocale(languageCode, countryCode);

        RegistrationHelper.getInstance().initializeUserRegistration(context);
        Tagging.init(context, "Product Registration");
    }

}
