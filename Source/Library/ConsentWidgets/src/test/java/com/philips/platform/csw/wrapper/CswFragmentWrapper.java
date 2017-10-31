package com.philips.platform.csw.wrapper;


import android.support.v4.app.FragmentManager;

import com.philips.platform.csw.CswFragment;
import com.philips.platform.csw.mock.FragmentManagerMock;
import com.philips.platform.csw.mock.FragmentTransactionMock;

public class CswFragmentWrapper extends CswFragment {

    public void setupFragment(FragmentManagerMock childFragmentManager) {
        setChildFragmentManager(childFragmentManager);
    }

    @Override
    protected FragmentManager getmFragmentManager(){
        return new FragmentManagerMock(new FragmentTransactionMock());
    }
}
