package com.philips.platform.pim.listeners;

import integration.PIMUserDataInterface;
import com.philips.platform.pim.models.PIMOIDCUserProfile;

public interface PIMOIDCLoginListener {
    void onSuccess(PIMOIDCUserProfile PIMOIDCUserProfile);
    void onFailure(PIMUserDataInterface.PIMError erro);
}
