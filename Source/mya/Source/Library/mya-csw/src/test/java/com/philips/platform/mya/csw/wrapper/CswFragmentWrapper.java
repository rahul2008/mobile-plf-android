package com.philips.platform.mya.csw.wrapper;


import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.philips.platform.mya.csw.CswFragment;
import com.philips.platform.mya.csw.mock.FragmentActivityMock;
import com.philips.platform.mya.csw.mock.FragmentManagerMock;
import com.philips.platform.mya.csw.mock.FragmentTransactionMock;

public class CswFragmentWrapper extends CswFragment {
    public FragmentManagerMock fragmentManagerMock = new FragmentManagerMock(new FragmentTransactionMock());
    public boolean mockOnBackPressed = false;
    public boolean onBackPressedInvoked = false;
    public FragmentActivityMock fragmentActivity = new FragmentActivityMock(fragmentManagerMock);

    public void setupFragment(FragmentManagerMock childFragmentManager) {
        setChildFragmentManager(childFragmentManager);
    }

    @Override
    public boolean onBackPressed() {
        if (mockOnBackPressed) {
            onBackPressedInvoked = true;
            return true;
        } else {
            return super.onBackPressed();
        }
    }

    @Override
    protected FragmentManager getmFragmentManager() {
        return fragmentManagerMock;
    }

    protected FragmentActivity getCurrentActivity() {
        return fragmentActivity;
    }
}
