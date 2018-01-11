package com.philips.platform.mya.csw.wrapper;


import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.mya.csw.CswFragment;
import com.philips.platform.mya.dialogs.DialogView;
import com.philips.platform.mya.csw.mock.DialogViewMock;
import com.philips.platform.mya.csw.mock.FragmentActivityMock;
import com.philips.platform.mya.csw.mock.FragmentManagerMock;
import com.philips.platform.mya.csw.mock.FragmentTransactionMock;
import com.philips.platform.mya.csw.mock.RestInterfaceMock;

public class CswFragmentWrapper extends CswFragment {
    public FragmentManagerMock fragmentManagerMock = new FragmentManagerMock(new FragmentTransactionMock());
    public boolean mockOnBackPressed = false;
    public boolean onBackPressedInvoked = false;
    public FragmentActivityMock fragmentActivity = new FragmentActivityMock(fragmentManagerMock);
    public RestInterfaceMock restInterface = new RestInterfaceMock();
    public DialogViewMock dialogViewMock = new DialogViewMock();

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

    @Override
    protected RestInterface getRestClient() {
        return restInterface;
    }

    @Override
    protected DialogView getDialogView() {
        return dialogViewMock;
    }
}
