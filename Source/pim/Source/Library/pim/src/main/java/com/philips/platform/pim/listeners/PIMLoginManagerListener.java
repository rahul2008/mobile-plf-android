package com.philips.platform.pim.listeners;

import com.philips.platform.pim.integration.PIMUserDataInterface;

public interface PIMLoginManagerListener {
    void onSuccess();
    void onFailure(PIMUserDataInterface.PIMError pimError);
}
