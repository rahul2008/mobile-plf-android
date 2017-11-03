package com.philips.platform.csw.wrapper;

import com.philips.platform.csw.CswBaseFragment;
import com.philips.platform.csw.CswFragment;
import com.philips.platform.csw.mock.ActionBarListenerMock;
import com.philips.platform.csw.mock.CswFragmentMock;

public class CswBaseFragmentWrapper extends CswBaseFragment {
    public int resourceTitleId = 0;
    public CswFragmentMock cswFragment;

    @Override
    public int getTitleResourceId() {
        return resourceTitleId;
    }

    @Override
    protected CswFragment overridableGetParentFragment() {
        return cswFragment;
    }

    public void setActionBarListener(ActionBarListenerMock actionBaristener) {
        this.cswFragment.setOnUpdateTitleListener(actionBaristener);
    }
}
