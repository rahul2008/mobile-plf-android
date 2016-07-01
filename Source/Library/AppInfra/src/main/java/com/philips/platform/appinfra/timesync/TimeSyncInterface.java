package com.philips.platform.appinfra.timesync;

import java.util.Date;

/**
 * Created by 310238655 on 6/30/2016.
 */
public interface TimeSyncInterface {

//    public String getLocalTime();
//    public String getTimezone();
    public String getUTCTime();
    public void refreshTime();
}
