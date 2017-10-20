package com.philips.platform.csw.wrapper;



import android.support.v4.app.FragmentManager;

import com.philips.platform.csw.CswFragment;
import com.philips.platform.csw.mock.FragmentManagerMock;

public class CswFragmentWrapper extends CswFragment {

    public void setupFragment(FragmentManagerMock childFragmentManager) {
        setChildFragmentManager(childFragmentManager);
    }
}
