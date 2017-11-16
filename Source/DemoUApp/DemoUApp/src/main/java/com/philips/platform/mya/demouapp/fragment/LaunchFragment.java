package com.philips.platform.mya.demouapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

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
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;


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
        if (checkedId == R.id.radioButton) {
            MyaInterface myaInterface = new MyaInterface();
            myaInterface.init(new MyaDependencies(MyAccountDemoUAppInterface.getAppInfra()), new MyaSettings((getActivity())));

            ActivityLauncher  activityLauncher = new ActivityLauncher(ActivityLauncher.
                    ActivityOrientation.SCREEN_ORIENTATION_SENSOR, ((DemoAppActivity) getActivity()).getThemeConfig(),
                    ((DemoAppActivity) getActivity()).getThemeResourceId(), null);

            myaInterface.launch(activityLauncher,
                    new MyaLaunchInput(getActivity(), new MyaListener() {
                        @Override
                        public boolean onClickMyaItem(String itemName) {
                            return false;
                        }
                    }));
        } else {
            MyaInterface myaInterface = new MyaInterface();
            myaInterface.init(new MyaDependencies(MyAccountDemoUAppInterface.getAppInfra()),  new MyaSettings(getActivity()));
            MyaLaunchInput uappLaunchInput =  new MyaLaunchInput(getActivity(), new MyaListener() {
                @Override
                public boolean onClickMyaItem(String itemName) {
                    return false;
                }
            });
            myaInterface.launch(new FragmentLauncher(getActivity(), R.id.mainContainer, new ActionBarListener() {
                @Override
                public void updateActionBar(@StringRes int i, boolean b) {
                    ((DemoAppActivity) getActivity()).setTitle(i);
                }

                @Override
                public void updateActionBar(String s, boolean b) {
                    ((DemoAppActivity) getActivity()).setTitle(s);
                }
            }), uappLaunchInput);
        }
    }
}
