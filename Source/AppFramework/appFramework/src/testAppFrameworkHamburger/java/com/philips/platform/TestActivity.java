package com.philips.platform;

import android.support.annotation.StringRes;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HamburgerActivity;

/**
 * Created by philips on 28/07/17.
 */

public class TestActivity extends HamburgerActivity {

    @Override
    protected void initializeActivityContents() {

    }

    @Override
    public int getContainerId() {
        return R.id.frame_container;
    }

    @Override
    public void initDLS(){
        setTheme(R.style.Theme_Philips_BrightBlue_Gradient_NoActionBar);
    }

    @Override
    public void updateActionBarIcon(boolean b) {

    }

    @Override
    public void updateActionBar(@StringRes int i, boolean b) {

    }

    @Override
    public void updateActionBar(String s, boolean b) {

    }

    @Override
    public void updateSelectionIndex(int position) {

    }
}
