package com.philips.platform.csw.wrapper;

import com.philips.platform.csw.CswBaseFragment;
import com.philips.platform.csw.CswFragment;
import com.philips.platform.csw.mock.ActionBarListenerMock;

public class CswBaseFragmentWrapper extends CswBaseFragment {
    public int resourceTitleId = 0;
    private CswFragment cswFragment = new CswFragment();

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
