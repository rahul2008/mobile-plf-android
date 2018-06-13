package com.philips.platform.appinfra.rest.hpkp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class HPKPExpirationHelper {

    static final String EXPIRY_DATE_REGEX = "expiry-date=\"(.+?)\";";
    private static final String MAX_AGE_REGEX = "max-age=([0-9]+)";
    private static final String EXPIRY_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final int BUFFER_TIME_IN_SECONDS = 86400;

    private String networkPinsExpiryDate;
    private String storedPinsExpiryDate;
    private Date storedPinsExpiry;
    private Date networkPinsExpiry;

    HPKPExpirationHelper(String storedPublicKeyDetails, String networkPublicKeyDetails) {
        int maxAge = getMaxAgeValue(networkPublicKeyDetails);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, maxAge);
        networkPinsExpiryDate = getDateStringFromDate(calendar.getTime());
        storedPinsExpiryDate = getExpiryDateString(storedPublicKeyDetails);
        networkPinsExpiry = getDateFromDateString(networkPinsExpiryDate);
        storedPinsExpiry = getDateFromDateString(storedPinsExpiryDate);
    }

    String getNetworkPinsExpiryDate() {
        return networkPinsExpiryDate;
    }

    String getStoredPinsExpiryDate() {
        return storedPinsExpiryDate;
    }

    boolean shouldExpiryBeUpdated() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(storedPinsExpiry);
        calendar.add(Calendar.SECOND, BUFFER_TIME_IN_SECONDS);
        return networkPinsExpiry != null && networkPinsExpiry.compareTo(calendar.getTime()) > 0;
    }

    boolean isPinnedPublicKeyExpired() {
        Calendar calendar = Calendar.getInstance();
        Date currentTime = calendar.getTime();
        return storedPinsExpiry != null && currentTime.compareTo(storedPinsExpiry) > 0;
    }

    private int getMaxAgeValue(String publicKeyDetails) {
        Pattern pattern = Pattern.compile(MAX_AGE_REGEX);
        Matcher matcher = pattern.matcher(publicKeyDetails);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : 0;
    }

    private String getExpiryDateString(String publicKeyDetails) {
        Pattern pattern = Pattern.compile(EXPIRY_DATE_REGEX);
        Matcher matcher = pattern.matcher(publicKeyDetails);
        return matcher.find() ? matcher.group(1) : "";
    }

    private Date getDateFromDateString(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(EXPIRY_DATE_FORMAT, Locale.ENGLISH);
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    private String getDateStringFromDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(EXPIRY_DATE_FORMAT, Locale.ENGLISH);
        return dateFormat.format(date);
    }
}
