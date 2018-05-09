/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.timesync;

import java.io.Serializable;
import java.util.Date;

/**
 * The UTC Time Sync Interface
 */
public interface TimeInterface extends Serializable {
    /**
     * Gets utc time from NTP server.
     *
     * @return the utc time
     * @since 1.0.0
     */
    Date getUTCTime();

    /**
     * Refresh time.
     * Calls UTCtime and update local Offset variable (Offset=UTCtime-Devicetime)
     * @since 1.0.0
     */
    void refreshTime();

    /**
     * isSynchronized.
     * @return the boolean value true or false refresh time is Synchronized or not
     * @since 2.2.0
     */
    boolean isSynchronized();

}
