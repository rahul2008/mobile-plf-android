package com.philips.platform.appinfra.logging.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by abhishek on 4/25/18.
 */
@Entity
public class AILCloudLogData {

    @PrimaryKey
    @NonNull
    public String id;

    /**
     *
     */
    public String appstate;

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
    public String logId;

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
    public String originatingUser;


    /**
     *
     */
    public String severity;

    /**
     *
     */
    public String transactionId;
}
