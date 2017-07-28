package com.philips.platform;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HamburgerActivity;

/**
 * Created by philips on 28/07/17.
 */

public class TestActivity extends HamburgerActivity {
    @Override
    public void initDLS(){
        setTheme(R.style.Theme_Philips_BrightBlue_Gradient_NoActionBar);
    }
}
