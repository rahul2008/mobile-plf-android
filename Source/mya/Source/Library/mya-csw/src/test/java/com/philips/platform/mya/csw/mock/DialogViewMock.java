package com.philips.platform.mya.csw.mock;

import com.philips.platform.mya.csw.dialogs.DialogView;


public class DialogViewMock extends DialogView {

    public boolean isDialogVisible = false;

    @Override
    public void showDialog() {
        this.isDialogVisible = true;
    }

    @Override
    public void hideDialog() {
        this.isDialogVisible = false;
    }
}
