package com.philips.platform.mya.csw.wrapper;

import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.view.View;

import com.philips.platform.mya.csw.CswBaseFragment;
import com.philips.platform.mya.csw.mock.ActionBarListenerMock;
import com.philips.platform.mya.csw.mock.CswFragmentMock;

public class CswBaseFragmentWrapper extends CswBaseFragment {
    public int resourceTitleId = 0;
    public CswFragmentMock cswFragment;

    @Override
    protected void setViewParams(Configuration config, int width) {

    }

    @Override
    protected void handleOrientation(View view) {

    }

    @Override
    public int getTitleResourceId() {
        return resourceTitleId;
    }

    public void setActionBarListener(ActionBarListenerMock actionBaristener) {
        this.cswFragment.setOnUpdateTitleListener(actionBaristener);
    }

    @Override
    protected Fragment overridableGetParentFragment() {
        return cswFragment;
    }

}
