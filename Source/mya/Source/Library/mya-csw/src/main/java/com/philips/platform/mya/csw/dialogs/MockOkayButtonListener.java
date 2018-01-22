package com.philips.platform.mya.csw.dialogs;

class MockOkayButtonListener implements OkayButtonListener {
    public boolean okayWasClicked;

    @Override
    public void onOkayClicked() {
        okayWasClicked = true;
    }
}
