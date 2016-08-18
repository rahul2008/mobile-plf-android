/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.timesync;

import java.util.Date;

/**
 * Created by 310238655 on 6/30/2016.
 */
public interface TimeInterface {
    /**
     * Gets utc time from NTP server.
     *
     * @return the utc time
     */
    public Date getUTCTime();

    /**
     * Refresh time.
     * Calls UTCtime and update local Offset variable (Offset=UTCtime-Devicetime)
     */
    public void refreshTime();
}
