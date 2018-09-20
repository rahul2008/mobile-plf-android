/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.csw;

public class CswConstants {
    public static final String DLS_THEME = "dlsTheme";
    public static final String CONSENT_DEFINITIONS = "consentDefinitions";

    public static class Tagging {
        public static final String COMPONENT_ID = "CSW";
        public static final String CONSENT_TYPE = "consentType";
        public static final String SPECIAL_EVENTS = "specialEvents";
        public static final String CONSENT_ACCEPTED = "consentAccepted";
        public static final String CONSENT_REJECTED = "consentRejected";
        public static final String IN_APP_NOTIFICATION = "inAppNotification";
        public static final String IN_APP_NOTIFICATION_RESPONSE = "inAppNotificationResponse";
        public static final String REVOKE_CONSENT_POPUP = "Revoke consent popup";

        public static class Action {
            public static final String ACTION_YES = "Yes";
            public static final String ACTION_CANCEL = "Cancel";
        }
    }
}
