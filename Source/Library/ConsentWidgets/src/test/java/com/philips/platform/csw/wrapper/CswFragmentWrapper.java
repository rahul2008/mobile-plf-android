package com.philips.platform.csw.wrapper;


import com.philips.platform.csw.CswFragment;
import com.philips.platform.csw.mock.FragmentManagerMock;

public class CswFragmentWrapper extends CswFragment {

    public void setupFragment(FragmentManagerMock childFragmentManager) {
        setChildFragmentManager(childFragmentManager);
    }
}
