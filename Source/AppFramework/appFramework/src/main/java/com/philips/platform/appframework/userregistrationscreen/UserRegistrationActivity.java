package com.philips.platform.appframework.userregistrationscreen;

import android.os.Bundle;

import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.R;
import com.philips.platform.modularui.cocointerface.UICoCoInterface;
import com.philips.platform.modularui.factorymanager.CoCoFactory;
import com.philips.platform.modularui.util.UIConstants;

/**
 * Created by 310240027 on 7/12/2016.
 */
public class UserRegistrationActivity extends AppFrameworkBaseActivity{
    UICoCoInterface uiCoCoUserReg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.af_user_registration_activity);
        uiCoCoUserReg = CoCoFactory.getInstance().getCoCo(UIConstants.UI_COCO_USER_REGISTRATION);
        uiCoCoUserReg.loadPlugIn(this);
        uiCoCoUserReg.runCoCo(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        uiCoCoUserReg.unloadCoCo();
    }

}
