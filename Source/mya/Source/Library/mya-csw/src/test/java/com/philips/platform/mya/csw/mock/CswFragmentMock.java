package com.philips.platform.mya.csw.mock;

import com.philips.platform.mya.csw.CswFragment;

public class CswFragmentMock extends CswFragment {
    public int fragmentCount = 0;

    @Override
    public int getFragmentCount() {
        return fragmentCount;
    }
}
