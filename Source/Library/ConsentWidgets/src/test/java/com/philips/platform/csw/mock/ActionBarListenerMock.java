package com.philips.platform.csw.mock;

import com.philips.platform.uappframework.listener.ActionBarListener;

public class ActionBarListenerMock implements ActionBarListener {
    public int updatedActionBarintBoolInt;
    public boolean updateActionBarIntBoolBoolean;
    public String updateActionBarStringBoolString;
    public boolean updateActionBarStringBoolBoolean;

    @Override
    public void updateActionBar(int i, boolean b) {
        updatedActionBarintBoolInt = i;
        updateActionBarIntBoolBoolean = b;
    }

    @Override
    public void updateActionBar(String s, boolean b) {
        updateActionBarStringBoolString = s;
        updateActionBarStringBoolBoolean = b;
    }
}
