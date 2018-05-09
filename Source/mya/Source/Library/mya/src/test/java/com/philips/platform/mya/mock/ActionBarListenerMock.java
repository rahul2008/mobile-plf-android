package com.philips.platform.mya.mock;

import com.philips.platform.uappframework.listener.ActionBarListener;

public class ActionBarListenerMock implements ActionBarListener {
    public int updatedActionBarInt;
    public boolean updateActionBarBoolean;
    public String updatedActionBarString;

    @Override
    public void updateActionBar(int i, boolean b) {
        updatedActionBarInt = i;
        updateActionBarBoolean = b;
    }

    @Override
    public void updateActionBar(String s, boolean b) {
        updatedActionBarString = s;
        updateActionBarBoolean = b;
    }
}