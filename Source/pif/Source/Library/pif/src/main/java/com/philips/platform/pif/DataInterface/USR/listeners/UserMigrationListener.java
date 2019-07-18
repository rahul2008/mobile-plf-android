package com.philips.platform.pif.DataInterface.USR.listeners;

import com.philips.platform.pif.DataInterface.USR.enums.Error;

public interface UserMigrationListener {
    void userMigrationSuccess();

    void userMigrationFailed(Error error);
}
