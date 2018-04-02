
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.coppa.base;

/**
 * Holds user consent information.Like confirmaton given ,locale ,cummunication status,locale and timestanps of all the activities.
 */
public class Consent {

    private String confirmationGiven;

    private String microSiteID;

    private String communicationSentAt;

    private String confirmationStoredAt;

    private String confirmationCommunicationSentAt;

    private String campaignId;

    private String given;

    private String locale;

    private String id;

    private String storedAt;

    private String confirmationCommunicationToSendAt;


    /**
     * Get Confirmation Given Status.
     *
     * @return String Confirmation given Status or Null.
     */
    public String getConfirmationGiven() {
        return confirmationGiven;
    }

    /**
     * Set Confirmation Given Status.
     *
     * @param confirmationGiven String
     */
    public void setConfirmationGiven(String confirmationGiven) {
        this.confirmationGiven = confirmationGiven;
    }

    /**
     * Get Microsite Id.
     *
     * @return String Microsite ID
     */
    public String getMicroSiteID() {
        return microSiteID;
    }

    /**
     * Set microsite ID.
     *
     * @param microSiteID String
     */
    public void setMicroSiteID(String microSiteID) {
        this.microSiteID = microSiteID;
    }

    /**
     * Get Communication Set timestamp in String.
     *
     * @return String cummunication sent time stamp.
     */
    public String getCommunicationSentAt() {
        return communicationSentAt;
    }

    /**
     * Set Communication Sent time stamp.
     *
     * @param communicationSentAt String
     */
    public void setCommunicationSentAt(String communicationSentAt) {
        this.communicationSentAt = communicationSentAt;
    }

    /**
     * @return String Confirmation Stored
     */
    public String getConfirmationStoredAt() {
        return confirmationStoredAt;
    }

    /**
     * Set Confirmation stored time stamp.
     *
     * @param confirmationStoredAt String Time stamp.
     */
    public void setConfirmationStoredAt(String confirmationStoredAt) {
        this.confirmationStoredAt = confirmationStoredAt;
    }

    /**
     * Get Confirmation communication sent time stamp.
     *
     * @return String TimeStamp.
     */
    public String getConfirmationCommunicationSentAt() {
        return confirmationCommunicationSentAt;
    }

    /**
     * Set Confirmation Communication Sent time stamp.
     *
     * @param confirmationCommunicationSentAt String.
     */
    public void setConfirmationCommunicationSentAt(String confirmationCommunicationSentAt) {
        this.confirmationCommunicationSentAt = confirmationCommunicationSentAt;
    }

    /**
     * Get Campaign Id.
     *
     * @return String Campaign Id.
     */
    public String getCampaignId() {
        return campaignId;
    }

    /**
     * Set Campaingn Id .
     *
     * @param campaignId String
     */
    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    /**
     * Get Consent given status
     *
     * @return String consent given status.
     */
    public String getGiven() {
        return given;
    }

    /**
     * Set Consent given Status.
     *
     * @param given String.
     */
    public void setGiven(String given) {
        this.given = given;
    }

    /**
     * Get Locale in string .
     *
     * @return String Locale example en_us.
     */
    public String getLocale() {
        return locale;
    }

    /**
     * Set Locale.
     *
     * @param locale String locale in string format.
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * Get Id .
     *
     * @return String id.
     */
    public String getId() {
        return id;
    }

    /**
     * @param id String id.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get Stored time stamp.
     *
     * @return get time stamp.
     */
    public String getStoredAt() {
        return storedAt;
    }

    /**
     * Set Stored time stamp.
     *
     * @param storedAt String time stamp.
     */
    public void setStoredAt(String storedAt) {
        this.storedAt = storedAt;
    }

    /**
     * Get Confirmation Sent time stamp.
     *
     * @return String timestamp.
     */
    public String getConfirmationCommunicationToSendAt() {
        return confirmationCommunicationToSendAt;
    }

    /**
     * Set Configuartion Communicaton Sent timestamp.
     *
     * @param confirmationCommunicationToSendAt String timestamp.
     */
    public void setConfirmationCommunicationToSendAt(String confirmationCommunicationToSendAt) {
        this.confirmationCommunicationToSendAt = confirmationCommunicationToSendAt;
    }
}
