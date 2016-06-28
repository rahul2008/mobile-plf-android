package com.philips.cdp.di.iapdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentTransaction;
/**
 * Created by 310164421 on 6/8/2016.
 */
public class LauncherFragmentActivity extends AppCompatActivity {
    FragmentLauncher mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_on_activity);
        getSupportActionBar().hide();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragment= new FragmentLauncher();
        fragmentTransaction.add(R.id.activity_container, mFragment, mFragment.getClass().getName());
        fragmentTransaction.commit();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
