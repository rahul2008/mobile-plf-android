package com.philips.platform.pim.listeners;

import integration.PIMUserDataInterface;

public interface PIMLoginManagerListener {
    void onSuccess();
    void onFailure(PIMUserDataInterface.PIMError pimError);
}
