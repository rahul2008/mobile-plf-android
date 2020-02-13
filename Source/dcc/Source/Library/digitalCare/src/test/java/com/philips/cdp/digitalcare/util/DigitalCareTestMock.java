package com.philips.cdp.digitalcare.util;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;

import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.listeners.ActivityTitleListener;

/**
 * Created by philips on 7/14/17.
 */

public class DigitalCareTestMock extends FragmentActivity implements ActivityTitleListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consumercare_activity_digi_care);
    }

    @Override
    public void setTitle(String title) {

    }
}
