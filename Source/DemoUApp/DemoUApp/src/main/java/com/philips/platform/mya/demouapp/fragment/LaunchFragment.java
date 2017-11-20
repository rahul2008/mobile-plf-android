package com.philips.platform.mya.demouapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

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
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.uappinput.UappSettings;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;

import static com.philips.platform.mya.demouapp.MyaConstants.APPLICATION_NAME;
import static com.philips.platform.mya.demouapp.MyaConstants.PROPOSITION_NAME;


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
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
        MyaLaunchInput launchInput = new MyaLaunchInput(((DemoAppActivity) getActivity()), new MyaListener() {
            @Override
            public boolean onClickMyaItem(String itemName) {
                if (itemName.equals("Mya_Privacy_Settings")) {
                    CswInterface cswInterface = new CswInterface();
                    CswDependencies cswDependencies = new CswDependencies(MyAccountDemoUAppInterface.getAppInfra());
                    cswDependencies.setApplicationName(APPLICATION_NAME);
                    cswDependencies.setPropositionName(PROPOSITION_NAME);
                    UappSettings uappSettings = new UappSettings(getContext());
                    cswInterface.init(cswDependencies, uappSettings);
                    ActivityLauncher activityLauncher = new ActivityLauncher(ActivityLauncher.
                            ActivityOrientation.SCREEN_ORIENTATION_SENSOR, ((DemoAppActivity) getActivity()).getThemeConfig(),
                            ((DemoAppActivity) getActivity()).getThemeResourceId(), null);

                    cswInterface.launch(activityLauncher, buildLaunchInput(true, getContext()));
                    return true;
                }
                return false;
            }

            @Override
            public boolean onLogOut() {
                return false;
            }
        });

        MyaInterface myaInterface = new MyaInterface();
        myaInterface.init(uappDependencies, new MyaSettings(((DemoAppActivity) getActivity())));

        if (checkedId == R.id.radioButton) {

            ActivityLauncher activityLauncher = new ActivityLauncher(ActivityLauncher.
                    ActivityOrientation.SCREEN_ORIENTATION_SENSOR, ((DemoAppActivity) getActivity()).getThemeConfig(),
                    ((DemoAppActivity) getActivity()).getThemeResourceId(), null);

            myaInterface.launch(activityLauncher, launchInput);
        } else {
            myaInterface.launch(new FragmentLauncher((DemoAppActivity) getActivity(), R.id.mainContainer, null), launchInput);
        }
    }


    private CswLaunchInput buildLaunchInput(boolean addToBackStack, Context context) {
        CswLaunchInput cswLaunchInput = new CswLaunchInput();
        cswLaunchInput.setPropositionName(PROPOSITION_NAME);
        cswLaunchInput.setApplicationName(APPLICATION_NAME);
        cswLaunchInput.addToBackStack(addToBackStack);
        cswLaunchInput.setContext(context);
        return cswLaunchInput;
    }
}
