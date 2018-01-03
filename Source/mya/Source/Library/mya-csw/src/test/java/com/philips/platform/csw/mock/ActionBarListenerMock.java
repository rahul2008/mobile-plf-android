package com.philips.platform.csw.mock;

import com.philips.platform.uappframework.listener.ActionBarListener;

public class ActionBarListenerMock implements ActionBarListener {
    public int updatedActionBarInt;
    public boolean updateActionBarBoolean;

    @Override
    public void updateActionBar(int i, boolean b) {
        updatedActionBarInt = i;
        updateActionBarBoolean = b;
    }

    @Override
    public void updateActionBar(String s, boolean b) {
    }
}
