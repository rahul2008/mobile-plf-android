package com.philips.platform.pim.listeners;

import com.philips.platform.pif.DataInterface.USR.enums.Error;

public interface PIMUserMigrationListener {
    void onUserMigrationSuccess();

    void onUserMigrationFailed(Error error);
}
