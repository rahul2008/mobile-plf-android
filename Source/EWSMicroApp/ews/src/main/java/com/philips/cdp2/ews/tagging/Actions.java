/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.cdp2.ews.tagging;

public class Actions {

    public static final String MARKETING_TOUR_SKIP = "skipButtonSelected";
    

    public static final String START_CONNECTION = "startConnection";
    public static final String SUCCESS_CONNECTION = "successConnection";
    public static final String SEARCH_AGAIN = "searchAgain";

    public static final String PAIR_AGAIN = "pairAgain";
    public static final String START_AGAIN = "startAgain";
    public static final String CONNECT_LATER = "connectLater";
    public static final String CANCEL = "cancel";
    public static final String OK = "ok";
    public static final String YES = "yes";
    public static final String NO = "no";
    public static final String MAYBE_LATER = "maybeLater";

    public static final String PRODUCT_MODEL = "productModel";
    public static final String FIRMWARE_VERSION = "firmwareVersion";

    public static final String DEVICE_NOT_FOUND_DIALOG = "deviceNotFoundDialog";
    public static final String DEVICE_NOT_FOUND_ERROR = "deviceNotFound";
    public static final String PAIRING_NOT_SUCCESSFUL_DIALOG = "pairingNotSuccessfulDialog";
    public static final String PAIRING_NOT_SUCCESSFUL_ERROR = "pairingNotSuccessful";
    public static final String NO_DEVICE_PAIRED_DIALOG = "noDevicePairedDialog";

    public static final String TIME_TO_CONNECT = "timeToConnect";

    public static final String SCROLL_TO_BOTTOM = "scrolledToBottom";
    public static final String TIP_VIEWED = "sleepTipViewed";
    public static final String SLEEP_TIP_ID = "sleepTipID";
    public static final String TIP_READING_TIME = "tipReadingTime";
    public static final String HSDP_ACCESS_TOKEN_REFRESH_ERROR = "hsdpAccessTokenRefreshError";
    public static final String FETCH_INSIGHTS_ERROR = "fetchInsightsError";
    public static final String FETCH_ARTICLE_ERROR = "fetchArticleError";

    public static final String MORE_TAB_ABOUT = "aboutSelected";
    public static final String MORE_TAB_MY_DEVICE = "myDeviceSelected";
    public static final String MORE_TAB_ADD_DEVICE = "addDeviceSelected";
    public static final String MORE_TAB_SUPPORT = "supportSelected";
    public static final String VIEW_PROFILE_SUPPORT = "viewProfileSelected";
    public static final String EDIT_USER_GENDER = "editUserGender";

    public static final String APP_STATUS = "appStatus";

    public static final String CLOUD_SYNC_START = "syncStart";
    public static final String CLOUD_SYNC_COMPLETE = "syncComplete";
    public static final String SYNC_FAILED_POPOVER = "syncFailedPopover";
    public static final String CONNECTION_UNSUCCESSFUL = "connectionUnsuccessful";

    public static final String CONNECTION_FAILED_POPOVER = "connectionFailedPopover";
    public static final String SYNCHRONISING_POPOVER = "synchronisingPopover";

    public static final String BLUETOOTH_CONNECTION_FAILED = "bluetoothConnectionFailed";
    public static final String DI_COMM_PORT_ERROR = "diCommPortError";
    public static final String CLOUD_SYNC_ERROR = "cloudSyncError";

    public static final String USER_CONSENT_OPT_IN = "personalDataStorageOptedIn";
    public static final String DEVICE_ACCESS_OPT_IN = "deviceAccessOptIn";
    public static final String ANALYTICS_OPT_IN = "analyticsOptIn";
    public static final String ANALYTICS_OPT_OUT = "analyticsOptOut";

    public static final String USER_PROFILE_GENDER = "gender";
    public static final String USER_PROFILE_GENDER_MALE = "Male";
    public static final String USER_PROFILE_GENDER_FEMALE = "Female";
    public static final String USER_PROFILE_DESIRED_HOURS = "desiredSleepTimeUpdated";
    public static final String USER_PROFILE_AVERAGE_HOURS = "averageSleepTimeUpdated";

    public static final String SYNC_START = "syncStart";
    public static final String SYNC_COMPLETE = "syncComplete";
    public static final String SYNC_RETRY = "retryDeviceStatusSync";
    public static final String SYNC_ABORTED = "syncAborted";

    public static final String DEVICE_PLACEMENT_OK = "OK";
    public static final String DEVICE_PLACEMENT_ERROR = "error";

    public static final String BATTERY_LEVEL_FULL = "Full";
    public static final String BATTERY_LEVEL_NOMINAL = "Nominal";
    public static final String BATTERY_LEVEL_CHARGING = "Charging";

    public static final String TRY_AGAIN = "tryAgain";
    public static final String CLOSE = "close";

    public static final String ALARM_ADDED = "alarmAdded";
    public static final String ALARM_DELETE = "alarmDeleted";
    public static final String ALARM_LIGHT = "alarmLight";
    public static final String ALARM_ENABLED = "alarmEnabled";
    public static final String ALARM_DISABLED = "alarmDisabled";
    public static final String ALARM_SOUND = "alarmSound";

    public static final String MANDATORY_UPDATE_DIALOG = "mandatoryAppUpdate";
    public static final String MANDATORY_UPDATE_DIALOG_ACTION = "UpdateNow";

    public static final String SKIP_BUTTON_SELECTED = "skipButtonSelected";
    public static final String CONNECT_DEVICE = "Connect a device";

    public static final String BE_TRY_AGAIN_PAIRING = "tryAgainPairing";
    public static final String BE_STILL_HAVING_ISSUES = "stillHavingIssues";

    public static final String FORGET_DEVICE = "forgetDevice";
    public static final String FORGET_DEVICE_DIALOG = "Forget PowerSleep Device";
    public static final String FORGET_DEVICE_DIALOG_OK = "forgetDevice";
    public static final String FORGET_DEVICE_DIALOG_CANCEL = "cancel";

    public static final String RLX_BREATH_STARTED = "relaxBreatheStarted";
    public static final String RLX_BREATH_STOPPED = "relaxBreatheStopped";
    public static final String RLX_BREATH_PACE_BPM = "relaxBreatheBreathingPace";

    public static final String DUSK_STARTED = "duskSessionStarted";
    public static final String DUSK_STOPPED = "duskSessionStopped";

    public static final String VIDEO_START = "videoStart";
    public static final String VIDEO_PLAYER_NOT_FOUND = "videoPlayerNotFound";
    public static final String TROUBLESHOOT_LINK_SELECTED = "bluetoothTroubleShootLinkSelected";



    public static class Key {
        public static final String GENDER = "gender";
        public static final String YEAR_OF_BIRTH = "yearOfBirth";

        public static final String SEND_DATA = "sendData";
        public static final String SPECIAL_EVENTS = "specialEvents";

        public static final String IN_APP_NOTIFICATION = "inAppNotification";
        public static final String IN_APP_NOTIFICATION_RESPONSE = "inAppNotificationResponse";
        public static final String CONNECTED_PRODUCT_NAME = "connectedProductName";
        public static final String ERROR = "error";
        public static final String TECHNICAL_ERROR = "technicalError";

        public static final String TRACK_ONLY = "trackOnly";
        public static final String DEVICE_PLACEMENT = "powerSleepDevicePositionStatus";
        public static final String BATTERY_LEVEL = "batteryLevel";

        public static final String URL = "url";

        public static final String VIDEO_NAME = "videoName";
    }

    public static class Value {
        public static final String PRODUCT_NAME_SMARTSLEEP = "SmartSleep";
        public static final String PRODUCT_NAME_SOMNEO = "Philips Somneo";
    }
}
