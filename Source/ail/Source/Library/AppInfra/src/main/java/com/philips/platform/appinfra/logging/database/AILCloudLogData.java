package com.philips.platform.appinfra.logging.database;

import androidx.annotation.NonNull;

/**
 * Created by abhishek on 4/25/18.
 */
@Entity
public class AILCloudLogData {

    @PrimaryKey
    @NonNull
    public String logId;

    /**
     *
     */
    public String component;

    /**
     *
     */
    public String details;

    /**
     *
     */
    public String eventId;

    /**
     *
     */
    public String homecountry;

    /**
     *
     */
    public String locale;

    /**
     *
     */
    public long localtime;

    /**
     *
     */
    public String logDescription;


    /**
     *
     */
    public long logTime;

    /**
     *
     */
    public String networktype;

    /**
     *
     */
    public String userUUID;


    /**
     *
     */
    public String severity;


    /**
     *
     */
    public String appState;
    /**
     *
     */
    public String appVersion;
    /**
     *
     */
    public String appsId;

    /**
     *
     */
    public String serverName;

    public String status;


}
