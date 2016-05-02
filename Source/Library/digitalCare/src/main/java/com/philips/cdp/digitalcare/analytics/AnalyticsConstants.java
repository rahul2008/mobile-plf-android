/**
 * AnalyticsConstants is holding constant members used for TAGGING, Adobe
 * Analytics.
 * <p>
 * = author: ritesh.jha= philips.com = since: Mar 26, 2015
 *
 * Copyright (c) 2016 Philips. All rights reserved.
 */

package com.philips.cdp.digitalcare.analytics;


public class AnalyticsConstants {

    /* PAGE CONSTANTS */
    public static final String PAGE_HOME = "digitalcare:home";
    public static final String PAGE_CONTACT_US = "digitalcare:contactus";
    public static final String PAGE_RATE_THIS_APP = "digitalcare:ratethisapp";
    public static final String PAGE_PRODUCT_REGISTRATION = "digitalcare:productregistration";
    public static final String PAGE_FIND_PHILIPS_NEAR_YOU = "digitalcare:findphilips";
    public static final String PAGE_FAQ = "digitalcare:faq";
    public static final String PAGE_FAQ_QUESTION_ANSWER = "digitalcare:faq:questionandanswer";

    public static final String PAGE_CONTACTUS_TWITTER = "digitalcare:contactus:twitter";
    public static final String PAGE_CONTACTUS_FACEBOOK = "digitalcare:contactus:facebook";
    public static final String PAGE_CONTACTUS_CHATNOW = "digitalcare:contactus:chatnow";
    public static final String PAGE_CONTACTUS_LIVECHAT = "digitalcare:contactus:livechat";
    public static final String PAGE_CONTACTUS_EMAIL = "digitalcare:contactus:email";

    public static final String PAGE_REVIEW_GUIDE_LINE = "digitalcare:ratethisapp:guideLines";
    public static final String PAGE_REVIEW_WRITING = "digitalcare:ratethisapp:writeReview";
    public static final String PAGE_REVIEW_PREVIEW = "digitalcare:ratethisapp:guideLines:writeReview:preview";
    public static final String PAGE_REVIEW_THANKS = "digitalcare:ratethisapp:guideLines:writeReview:preview:thankYou";

    public static final String PAGE_VIEW_PRODUCT_DETAILS = "digitalcare:productdetails";
    public static final String PAGE_VIEW_PRODUCT_WEBSITE = "digitalcare:productdetails:website";
    public static final String PAGE_VIEW_PRODUCT_MANUAL = "digitalcare:productdetails:manual";
        /* ACTION KEY CONSTANTS */
    /*****************
     * Page Context Data start
     **************/
    public static final String KEY_APPNAME = "app.name";
    public static final String KEY_VERSION = "app.version";
    public static final String KEY_OS = "app.os";
    public static final String KEY_LANGUAGE = "locale.language";
    public static final String KEY_CURRENCY = "locale.currency";
    public static final String KEY_COUNTRY = "locale.country";
    public static final String KEY_TIME_STAMP = "timestamp";
    public static final String KEY_APP_ID = "appsId";
    public static final String KEY_PREVIOUS_PAGENAME = "previousPagename";
    public static final String KEY_DIGITALCARE_VERSION = "digCareVersion";
    public static final String KEY_COMPONENT_VERSION = "componentVersion";

    /***************** Page Context Data End **************/

    /*****************
     * Action Names
     **************/
    public static final String ACTION_SET_ERROR = "setError";
    public static final String ACTION_RECEIPT_PHOTO = "receiptPhoto";
    public static final String ACTION_SOCIAL_SHARE = "socialShare";
    public static final String ACTION_EXIT_LINK = "exit link";
    public static final String ACTION_SERVICE_REQUEST = "serviceRequest";
    /* ACTION KEY FOR LOCATE PHILIPS */
    public static final String ACTION_SEND_DATA = "sendData";

    /*****************
     * Action Keys
     **************/
    public static final String ACTION_KEY_USER_ERROR = "userError";
    public static final String ACTION_KEY_TECHNICAL_ERROR = "technicalError";
    public static final String ACTION_KEY_SOCIAL_TYPE = "socialType";
    public static final String ACTION_KEY_SERVICE_CHANNEL = "serviceChannel";
    public static final String ACTION_KEY_PHOTO = "photo"; // TODO: Has to be
    public static final String ACTION_KEY_URL = "url";
    // confirmed.
    public static final String ACTION_KEY_EXIT_LINK = "exitLinkName";

    /* Locate Near To You */
    public static final String ACTION_KEY_LOCATE_PHILIPS_SEARCH_TERM = "searchTerm";
    public static final String ACTION_KEY_LOCATE_PHILIPS_SEARCH_RESULTS = "numberOfSearchResults";
    public static final String ACTION_KEY_LOCATE_PHILIPS_LOCATION_VIEW = "locationView";
//	public static final String ACTION_KEY_LOCATE_PHILIPS_SPECIAL_EVENTS = "specialEvents";

    /*  Action keys Product Review*/
    public static final String ACTION_KEY_REVIEWER_STAR_RATING = "starRating";
    public static final String ACTION_KEY_REVIEWER_NAME = "reviewerName";
    public static final String ACTION_KEY_REVIEWER_SUMMARY = "reviewSummary";
    public static final String ACTION_KEY_REVIEWER_EMAIL = "reviewerEmail";

    /*  Action keys View Product Details*/
    public static final String ACTION_KEY_VIEW_PRODUCT_VIDEO_START = "videoStart";
    public static final String ACTION_KEY_VIEW_PRODUCT_VIDEO_NAME = "videoName";

    /*****************
     * Action Values
     **************/
    public static final String ACTION_VALUE_APPNAME = "Consumer Care ";
    public static final String ACTION_VALUE_ANDROID = "Android ";
    public static final String ACTION_VALUE_FACEBOOK = "Facebook";
    public static final String ACTION_VALUE_PHOTO_VALUE = "productimage";
    public static final String ACTION_VALUE_LOCATE_PHILIPS_SEND_GET_DIRECTIONS = "getLocationDirections";
    public static final String ACTION_VALUE_LOCATE_PHILIPS_CALL_LOCATION = "callLocation";

    /* Error text */
    public static final String ACTION_VALUE_TECHNICAL_ERROR_NETWORK_CONNECITON = "Error connecting to network";
    public static final String ACTION_VALUE_TECHNICAL_ERROR_RESPONSE_CDLS = "Error response from CDLS server";
    public static final String ACTION_VALUE_TECHNICAL_ERROR_LOADING = "Error in loading";

    /* Service Channel */
    public static final String ACTION_VALUE_SERVICE_CHANNEL_CHAT = "chat";
    public static final String ACTION_VALUE_SERVICE_CHANNEL_EMAIL = "email";
    public static final String ACTION_VALUE_SERVICE_CHANNEL_CALL = "call";
    public static final String ACTION_VALUE_SERVICE_CHANNEL_TWITTER = "Twitter";
    public static final String ACTION_VALUE_SERVICE_CHANNEL_FAQ = "FAQ";
    public static final String ACTION_VALUE_SERVICE_CHANNEL_Facebook = "Facebook";
}
