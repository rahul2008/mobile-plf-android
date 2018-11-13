package com.philips.platform.uappdemo.screens.navigation;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.philips.platform.uappdemolibrary.R;
import com.philips.platform.uid.utils.UIDActivity;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class NavigationImplActivity extends UIDActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_impl);
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            NavigationImplModeFragment fragment = new NavigationImplModeFragment();
            transaction.replace(R.id.main_container, fragment);
            transaction.commit();
        }
    }
}
