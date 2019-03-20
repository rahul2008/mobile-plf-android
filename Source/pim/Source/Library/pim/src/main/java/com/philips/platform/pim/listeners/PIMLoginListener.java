package com.philips.platform.pim.listeners;

import com.philips.platform.pim.integration.PIMUserDataInterface;
import com.philips.platform.pim.models.PIMOIDCUserProfile;

public interface PIMLoginListener {
    void onSuccess(PIMOIDCUserProfile PIMOIDCUserProfile);
    void onFailure(PIMUserDataInterface.PIMError erro);
}
