package com.philips.cdp.prodreg;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.philips.cdp.prodreg.launcher.FragmentLauncher;
import com.philips.cdp.prodreg.listener.ActionbarUpdateListener;
import com.philips.cdp.prodreg.util.ProdRegConfigManager;
import com.philips.cdp.uikit.UiKitActivity;

public class TestURActivity extends UiKitActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_ur);

        fragmentManager = getSupportFragmentManager();
        FragmentLauncher fragLauncher = new FragmentLauncher(
                this, R.id.parent_layout, new ActionbarUpdateListener() {
            @Override
            public void updateActionbar(final String var1, final Boolean var2) {
            }
        });
        fragLauncher.setAnimation(0, 0);
        ProdRegConfigManager.getInstance().invokeProductRegistration(fragLauncher);
    }

    private boolean backStackFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            fragmentManager.popBackStack();
            removeCurrentFragment();
        }
        return true;
    }

    private void removeCurrentFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment currentFrag = fragmentManager
                .findFragmentById(R.id.parent_layout);

        if (currentFrag != null) {
            transaction.remove(currentFrag);
        }
        transaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return backStackFragment();
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
