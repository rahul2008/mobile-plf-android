package com.philips.platform.appinfra.timesync;

/**
 * Created by 310238655 on 6/30/2016.
 */
public interface TimeSyncInterface {


    /**
     * Gets utc time from NTP server.
     *
     * @return the utc time
     */
    public String getUTCTime();

    /**
     * Refresh time.
     * Calls UTCtime and update local Offset variable (Offset=UTCtime-Devicetime)
     */
    public void refreshTime();
}
