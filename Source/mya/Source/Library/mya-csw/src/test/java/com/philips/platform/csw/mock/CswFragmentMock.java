package com.philips.platform.csw.mock;

import com.philips.platform.csw.CswFragment;

public class CswFragmentMock extends CswFragment {
    public int fragmentCount = 0;

    @Override
    public int getFragmentCount() {
        return fragmentCount;
    }
}
