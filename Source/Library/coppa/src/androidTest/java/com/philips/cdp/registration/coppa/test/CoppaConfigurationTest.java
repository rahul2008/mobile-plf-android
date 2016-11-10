/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.coppa.test;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

import com.janrain.android.Jump;
import com.janrain.android.utils.ThreadUtils;
import com.philips.cdp.registration.coppa.ui.activity.RegistrationCoppaActivity;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;

import static com.janrain.android.utils.LogUtils.throwDebugException;

/**
 * Created by 310202337 on 4/12/2016.
 */
public class CoppaConfigurationTest extends ActivityInstrumentationTestCase2<RegistrationCoppaActivity> {

        private static LoggingInterface mLoggingInterface;
        JSONObject signed_user ;
        String COPPA_CONFIRMED_SIGNED_USER = "{\n" +
                "\t\"original\": {\n" +
                "\t\t\"lastModifiedDate\": null,\n" +
                "\t\t\"providerMergedLast\": null,\n" +
                "\t\t\"preferredLanguage\": \"en\",\n" +
                "\t\t\"personalDataUsageAcceptance\": null,\n" +
                "\t\t\"post_login_confirmation\": [],\n" +
                "\t\t\"streamiumServicesTCAgreed\": null,\n" +
                "\t\t\"birthday\": null,\n" +
                "\t\t\"consumerPoints\": null,\n" +
                "\t\t\"batchId\": null,\n" +
                "\t\t\"familyName\": null,\n" +
                "\t\t\"weddingDate\": null,\n" +
                "\t\t\"interestStreamiumSurveys\": null,\n" +
                "\t\t\"interestAvent\": null,\n" +
                "\t\t\"lastModifiedSource\": null,\n" +
                "\t\t\"medicalProfessionalRoleSpecified\": null,\n" +
                "\t\t\"catalogLocaleItem\": null,\n" +
                "\t\t\"receiveMarketingEmail\": true,\n" +
                "\t\t\"familyId\": null,\n" +
                "\t\t\"children\": [],\n" +
                "\t\t\"consumerInterests\": [],\n" +
                "\t\t\"profiles\": [],\n" +
                "\t\t\"legacyID\": null,\n" +
                "\t\t\"id\": 1135585486,\n" +
                "\t\t\"deactivatedAccount\": null,\n" +
                "\t\t\"consentVerifiedAt\": null,\n" +
                "\t\t\"avmTCAgreed\": null,\n" +
                "\t\t\"interestCommunications\": null,\n" +
                "\t\t\"campaigns\": [],\n" +
                "\t\t\"badgeVillePlayerIDs\": [],\n" +
                "\t\t\"nettvTCAgreed\": null,\n" +
                "\t\t\"visitedMicroSites\": [{\n" +
                "\t\t\t\"microSiteID\": \"77000\",\n" +
                "\t\t\t\"id\": 1189292149,\n" +
                "\t\t\t\"timestamp\": \"2016-03-25 20:01:29 +0000\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"microSiteID\": \"77000\",\n" +
                "\t\t\t\"id\": 1214424860,\n" +
                "\t\t\t\"timestamp\": \"2016-04-12 15:04:33 +0000\"\n" +
                "\t\t}],\n" +
                "\t\t\"middleName\": null,\n" +
                "\t\t\"familyRole\": null,\n" +
                "\t\t\"emailVerified\": \"2016-03-11 09:53:58 +0000\",\n" +
                "\t\t\"primaryAddress\": {\n" +
                "\t\t\t\"company\": \"\",\n" +
                "\t\t\t\"address2\": \"\",\n" +
                "\t\t\t\"zipPlus4\": \"\",\n" +
                "\t\t\t\"city\": \"\",\n" +
                "\t\t\t\"state\": \"\",\n" +
                "\t\t\t\"address1\": \"\",\n" +
                "\t\t\t\"houseNumber\": \"\",\n" +
                "\t\t\t\"phone\": \"\",\n" +
                "\t\t\t\"dayTimePhoneNumber\": \"\",\n" +
                "\t\t\t\"zip\": \"\",\n" +
                "\t\t\t\"mobile\": \"\",\n" +
                "\t\t\t\"country\": \"GB\",\n" +
                "\t\t\t\"address3\": \"\"\n" +
                "\t\t},\n" +
                "\t\t\"gender\": null,\n" +
                "\t\t\"lastUpdated\": \"2016-04-12 09:34:33.947792 +0000\",\n" +
                "\t\t\"termsAndConditionsAcceptance\": null,\n" +
                "\t\t\"password\": {\n" +
                "\t\t\t\"value\": \"$2a$04$xlCW2tYE48OMAFsaqQ856eCRxWkZ3qTPWWyid1LAtpG179xHOKPhm\",\n" +
                "\t\t\t\"type\": \"password-bcrypt\"\n" +
                "\t\t},\n" +
                "\t\t\"roles\": [{\n" +
                "\t\t\t\"id\": 1135972315,\n" +
                "\t\t\t\"role\": \"consumer\",\n" +
                "\t\t\t\"role_assigned\": \"2016-03-11 15:24:00 +0000\"\n" +
                "\t\t}],\n" +
                "\t\t\"wishList\": null,\n" +
                "\t\t\"photos\": [],\n" +
                "\t\t\"email\": \"vin@bin.com\",\n" +
                "\t\t\"coppaCommunicationSentAt\": null,\n" +
                "\t\t\"givenName\": \"vin@bin.com\",\n" +
                "\t\t\"nettvTermsAgreedDate\": null,\n" +
                "\t\t\"currentLocation\": null,\n" +
                "\t\t\"lastLogin\": \"2016-04-12 09:34:33 +0000\",\n" +
                "\t\t\"interestPromotions\": null,\n" +
                "\t\t\"ssn\": null,\n" +
                "\t\t\"NRIC\": null,\n" +
                "\t\t\"interestCampaigns\": null,\n" +
                "\t\t\"interestSurveys\": null,\n" +
                "\t\t\"avmTermsAgreedDate\": null,\n" +
                "\t\t\"interestWULsounds\": null,\n" +
                "\t\t\"interestCategories\": null,\n" +
                "\t\t\"created\": \"2016-03-11 09:53:58.662891 +0000\",\n" +
                "\t\t\"interestStreamiumUpgrades\": null,\n" +
                "\t\t\"nickName\": null,\n" +
                "\t\t\"CPF\": null,\n" +
                "\t\t\"displayName\": null,\n" +
                "\t\t\"uuid\": \"97681eca-d2a1-4990-8c05-19c9a984f14d\",\n" +
                "\t\t\"olderThanAgeLimit\": true,\n" +
                "\t\t\"aboutMe\": null,\n" +
                "\t\t\"consents\": [{\n" +
                "\t\t\t\"confirmationGiven\": true,\n" +
                "\t\t\t\"microSiteID\": \"77000\",\n" +
                "\t\t\t\"communicationSentAt\": null,\n" +
                "\t\t\t\"confirmationStoredAt\": \"2016-04-12 06:28:05 +0000\",\n" +
                "\t\t\t\"confirmationCommunicationSentAt\": null,\n" +
                "\t\t\t\"campaignId\": \"CL20150501_PC_TB_COPPA\",\n" +
                "\t\t\t\"given\": true,\n" +
                "\t\t\t\"locale\": \"en_US\",\n" +
                "\t\t\t\"id\": 1214312592,\n" +
                "\t\t\t\"storedAt\": \"2016-03-12 06:27:04 +0000\",\n" +
                "\t\t\t\"confirmationCommunicationToSendAt\": null\n" +
                "\t\t}],\n" +
                "\t\t\"personalDataTransferAcceptance\": null,\n" +
                "\t\t\"salutation\": null,\n" +
                "\t\t\"display\": null,\n" +
                "\t\t\"statuses\": [],\n" +
                "\t\t\"maritalStatus\": null\n" +
                "\t},\n" +
                "\t\"accessToken\": \"accessToken2geyffjqmcwr746c\",\n" +
                "\t\"this\": {\n" +
                "\t\t\"CPF\": null,\n" +
                "\t\t\"NRIC\": null,\n" +
                "\t\t\"aboutMe\": null,\n" +
                "\t\t\"avmTCAgreed\": null,\n" +
                "\t\t\"avmTermsAgreedDate\": null,\n" +
                "\t\t\"badgeVillePlayerIDs\": [],\n" +
                "\t\t\"batchId\": null,\n" +
                "\t\t\"birthday\": null,\n" +
                "\t\t\"campaigns\": [],\n" +
                "\t\t\"catalogLocaleItem\": null,\n" +
                "\t\t\"children\": [],\n" +
                "\t\t\"consentVerifiedAt\": null,\n" +
                "\t\t\"consents\": [{\n" +
                "\t\t\t\"confirmationGiven\": true,\n" +
                "\t\t\t\"microSiteID\": \"77000\",\n" +
                "\t\t\t\"communicationSentAt\": null,\n" +
                "\t\t\t\"confirmationStoredAt\": \"2016-04-12 06:28:05 +0000\",\n" +
                "\t\t\t\"confirmationCommunicationSentAt\": null,\n" +
                "\t\t\t\"campaignId\": \"CL20150501_PC_TB_COPPA\",\n" +
                "\t\t\t\"given\": true,\n" +
                "\t\t\t\"locale\": \"en_US\",\n" +
                "\t\t\t\"id\": 1214312592,\n" +
                "\t\t\t\"storedAt\": \"2016-03-12 06:27:04 +0000\",\n" +
                "\t\t\t\"confirmationCommunicationToSendAt\": null\n" +
                "\t\t}],\n" +
                "\t\t\"consumerInterests\": [],\n" +
                "\t\t\"consumerPoints\": null,\n" +
                "\t\t\"coppaCommunicationSentAt\": null,\n" +
                "\t\t\"created\": \"2016-03-11 09:53:58.662891 +0000\",\n" +
                "\t\t\"currentLocation\": null,\n" +
                "\t\t\"deactivatedAccount\": null,\n" +
                "\t\t\"display\": null,\n" +
                "\t\t\"displayName\": null,\n" +
                "\t\t\"email\": \"vin@bin.com\",\n" +
                "\t\t\"emailVerified\": \"2016-03-11 09:53:58 +0000\",\n" +
                "\t\t\"familyId\": null,\n" +
                "\t\t\"familyName\": null,\n" +
                "\t\t\"familyRole\": null,\n" +
                "\t\t\"gender\": null,\n" +
                "\t\t\"givenName\": \"vin@bin.com\",\n" +
                "\t\t\"id\": 1135585486,\n" +
                "\t\t\"interestAvent\": null,\n" +
                "\t\t\"interestCampaigns\": null,\n" +
                "\t\t\"interestCategories\": null,\n" +
                "\t\t\"interestCommunications\": null,\n" +
                "\t\t\"interestPromotions\": null,\n" +
                "\t\t\"interestStreamiumSurveys\": null,\n" +
                "\t\t\"interestStreamiumUpgrades\": null,\n" +
                "\t\t\"interestSurveys\": null,\n" +
                "\t\t\"interestWULsounds\": null,\n" +
                "\t\t\"lastLogin\": \"2016-04-12 09:34:33 +0000\",\n" +
                "\t\t\"lastModifiedDate\": null,\n" +
                "\t\t\"lastModifiedSource\": null,\n" +
                "\t\t\"lastUpdated\": \"2016-04-12 09:34:33.947792 +0000\",\n" +
                "\t\t\"legacyID\": null,\n" +
                "\t\t\"maritalStatus\": null,\n" +
                "\t\t\"medicalProfessionalRoleSpecified\": null,\n" +
                "\t\t\"middleName\": null,\n" +
                "\t\t\"nettvTCAgreed\": null,\n" +
                "\t\t\"nettvTermsAgreedDate\": null,\n" +
                "\t\t\"nickName\": null,\n" +
                "\t\t\"olderThanAgeLimit\": true,\n" +
                "\t\t\"password\": {\n" +
                "\t\t\t\"value\": \"$2a$04$xlCW2tYE48OMAFsaqQ856eCRxWkZ3qTPWWyid1LAtpG179xHOKPhm\",\n" +
                "\t\t\t\"type\": \"password-bcrypt\"\n" +
                "\t\t},\n" +
                "\t\t\"personalDataTransferAcceptance\": null,\n" +
                "\t\t\"personalDataUsageAcceptance\": null,\n" +
                "\t\t\"photos\": [],\n" +
                "\t\t\"post_login_confirmation\": [],\n" +
                "\t\t\"preferredLanguage\": \"en\",\n" +
                "\t\t\"primaryAddress\": {\n" +
                "\t\t\t\"company\": \"\",\n" +
                "\t\t\t\"address2\": \"\",\n" +
                "\t\t\t\"zipPlus4\": \"\",\n" +
                "\t\t\t\"city\": \"\",\n" +
                "\t\t\t\"state\": \"\",\n" +
                "\t\t\t\"address1\": \"\",\n" +
                "\t\t\t\"houseNumber\": \"\",\n" +
                "\t\t\t\"phone\": \"\",\n" +
                "\t\t\t\"dayTimePhoneNumber\": \"\",\n" +
                "\t\t\t\"zip\": \"\",\n" +
                "\t\t\t\"mobile\": \"\",\n" +
                "\t\t\t\"country\": \"GB\",\n" +
                "\t\t\t\"address3\": \"\"\n" +
                "\t\t},\n" +
                "\t\t\"profiles\": [],\n" +
                "\t\t\"providerMergedLast\": null,\n" +
                "\t\t\"receiveMarketingEmail\": true,\n" +
                "\t\t\"roles\": [{\n" +
                "\t\t\t\"id\": 1135972315,\n" +
                "\t\t\t\"role\": \"consumer\",\n" +
                "\t\t\t\"role_assigned\": \"2016-03-11 15:24:00 +0000\"\n" +
                "\t\t}],\n" +
                "\t\t\"salutation\": null,\n" +
                "\t\t\"ssn\": null,\n" +
                "\t\t\"statuses\": [],\n" +
                "\t\t\"streamiumServicesTCAgreed\": null,\n" +
                "\t\t\"termsAndConditionsAcceptance\": null,\n" +
                "\t\t\"uuid\": \"97681eca-d2a1-4990-8c05-19c9a984f14d\",\n" +
                "\t\t\"visitedMicroSites\": [{\n" +
                "\t\t\t\"microSiteID\": \"77000\",\n" +
                "\t\t\t\"id\": 1189292149,\n" +
                "\t\t\t\"timestamp\": \"2016-03-25 20:01:29 +0000\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"microSiteID\": \"77000\",\n" +
                "\t\t\t\"id\": 1214424860,\n" +
                "\t\t\t\"timestamp\": \"2016-04-12 15:04:33 +0000\"\n" +
                "\t\t}],\n" +
                "\t\t\"weddingDate\": null,\n" +
                "\t\t\"wishList\": null\n" +
                "\t}\n" +
                "}";

        String COPPA_CONSENT_SIGNED_USER = "{\n" +
                "\t\"original\": {\n" +
                "\t\t\"lastModifiedDate\": null,\n" +
                "\t\t\"providerMergedLast\": null,\n" +
                "\t\t\"preferredLanguage\": \"en\",\n" +
                "\t\t\"personalDataUsageAcceptance\": null,\n" +
                "\t\t\"post_login_confirmation\": [],\n" +
                "\t\t\"streamiumServicesTCAgreed\": null,\n" +
                "\t\t\"birthday\": null,\n" +
                "\t\t\"consumerPoints\": null,\n" +
                "\t\t\"batchId\": null,\n" +
                "\t\t\"familyName\": null,\n" +
                "\t\t\"weddingDate\": null,\n" +
                "\t\t\"interestStreamiumSurveys\": null,\n" +
                "\t\t\"interestAvent\": null,\n" +
                "\t\t\"lastModifiedSource\": null,\n" +
                "\t\t\"medicalProfessionalRoleSpecified\": null,\n" +
                "\t\t\"catalogLocaleItem\": null,\n" +
                "\t\t\"receiveMarketingEmail\": true,\n" +
                "\t\t\"familyId\": null,\n" +
                "\t\t\"children\": [],\n" +
                "\t\t\"consumerInterests\": [],\n" +
                "\t\t\"profiles\": [],\n" +
                "\t\t\"legacyID\": null,\n" +
                "\t\t\"id\": 1135585486,\n" +
                "\t\t\"deactivatedAccount\": null,\n" +
                "\t\t\"consentVerifiedAt\": null,\n" +
                "\t\t\"avmTCAgreed\": null,\n" +
                "\t\t\"interestCommunications\": null,\n" +
                "\t\t\"campaigns\": [],\n" +
                "\t\t\"badgeVillePlayerIDs\": [],\n" +
                "\t\t\"nettvTCAgreed\": null,\n" +
                "\t\t\"visitedMicroSites\": [{\n" +
                "\t\t\t\"microSiteID\": \"77000\",\n" +
                "\t\t\t\"id\": 1189292149,\n" +
                "\t\t\t\"timestamp\": \"2016-03-25 20:01:29 +0000\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"microSiteID\": \"77000\",\n" +
                "\t\t\t\"id\": 1214424860,\n" +
                "\t\t\t\"timestamp\": \"2016-04-12 15:04:33 +0000\"\n" +
                "\t\t}],\n" +
                "\t\t\"middleName\": null,\n" +
                "\t\t\"familyRole\": null,\n" +
                "\t\t\"emailVerified\": \"2016-03-11 09:53:58 +0000\",\n" +
                "\t\t\"primaryAddress\": {\n" +
                "\t\t\t\"company\": \"\",\n" +
                "\t\t\t\"address2\": \"\",\n" +
                "\t\t\t\"zipPlus4\": \"\",\n" +
                "\t\t\t\"city\": \"\",\n" +
                "\t\t\t\"state\": \"\",\n" +
                "\t\t\t\"address1\": \"\",\n" +
                "\t\t\t\"houseNumber\": \"\",\n" +
                "\t\t\t\"phone\": \"\",\n" +
                "\t\t\t\"dayTimePhoneNumber\": \"\",\n" +
                "\t\t\t\"zip\": \"\",\n" +
                "\t\t\t\"mobile\": \"\",\n" +
                "\t\t\t\"country\": \"GB\",\n" +
                "\t\t\t\"address3\": \"\"\n" +
                "\t\t},\n" +
                "\t\t\"gender\": null,\n" +
                "\t\t\"lastUpdated\": \"2016-04-12 09:34:33.947792 +0000\",\n" +
                "\t\t\"termsAndConditionsAcceptance\": null,\n" +
                "\t\t\"password\": {\n" +
                "\t\t\t\"value\": \"$2a$04$xlCW2tYE48OMAFsaqQ856eCRxWkZ3qTPWWyid1LAtpG179xHOKPhm\",\n" +
                "\t\t\t\"type\": \"password-bcrypt\"\n" +
                "\t\t},\n" +
                "\t\t\"roles\": [{\n" +
                "\t\t\t\"id\": 1135972315,\n" +
                "\t\t\t\"role\": \"consumer\",\n" +
                "\t\t\t\"role_assigned\": \"2016-03-11 15:24:00 +0000\"\n" +
                "\t\t}],\n" +
                "\t\t\"wishList\": null,\n" +
                "\t\t\"photos\": [],\n" +
                "\t\t\"email\": \"vin@bin.com\",\n" +
                "\t\t\"coppaCommunicationSentAt\": null,\n" +
                "\t\t\"givenName\": \"vin@bin.com\",\n" +
                "\t\t\"nettvTermsAgreedDate\": null,\n" +
                "\t\t\"currentLocation\": null,\n" +
                "\t\t\"lastLogin\": \"2016-04-12 09:34:33 +0000\",\n" +
                "\t\t\"interestPromotions\": null,\n" +
                "\t\t\"ssn\": null,\n" +
                "\t\t\"NRIC\": null,\n" +
                "\t\t\"interestCampaigns\": null,\n" +
                "\t\t\"interestSurveys\": null,\n" +
                "\t\t\"avmTermsAgreedDate\": null,\n" +
                "\t\t\"interestWULsounds\": null,\n" +
                "\t\t\"interestCategories\": null,\n" +
                "\t\t\"created\": \"2016-03-11 09:53:58.662891 +0000\",\n" +
                "\t\t\"interestStreamiumUpgrades\": null,\n" +
                "\t\t\"nickName\": null,\n" +
                "\t\t\"CPF\": null,\n" +
                "\t\t\"displayName\": null,\n" +
                "\t\t\"uuid\": \"97681eca-d2a1-4990-8c05-19c9a984f14d\",\n" +
                "\t\t\"olderThanAgeLimit\": true,\n" +
                "\t\t\"aboutMe\": null,\n" +
                "\t\t\"consents\": [{\n" +
                "\t\t\t\"confirmationGiven\": \"null\",\n" +
                "\t\t\t\"microSiteID\": \"77000\",\n" +
                "\t\t\t\"communicationSentAt\": null,\n" +
                "\t\t\t\"confirmationStoredAt\": \"null\",\n" +
                "\t\t\t\"confirmationCommunicationSentAt\": null,\n" +
                "\t\t\t\"campaignId\": \"CL20150501_PC_TB_COPPA\",\n" +
                "\t\t\t\"given\": \"true\",\n" +
                "\t\t\t\"locale\": \"en_US\",\n" +
                "\t\t\t\"id\": 1214312592,\n" +
                "\t\t\t\"storedAt\": \"2016-03-12 06:27:04 +0000\",\n" +
                "\t\t\t\"confirmationCommunicationToSendAt\": null\n" +
                "\t\t}],\n" +
                "\t\t\"personalDataTransferAcceptance\": null,\n" +
                "\t\t\"salutation\": null,\n" +
                "\t\t\"display\": null,\n" +
                "\t\t\"statuses\": [],\n" +
                "\t\t\"maritalStatus\": null\n" +
                "\t},\n" +
                "\t\"accessToken\": \"accessToken2geyffjqmcwr746c\",\n" +
                "\t\"this\": {\n" +
                "\t\t\"CPF\": null,\n" +
                "\t\t\"NRIC\": null,\n" +
                "\t\t\"aboutMe\": null,\n" +
                "\t\t\"avmTCAgreed\": null,\n" +
                "\t\t\"avmTermsAgreedDate\": null,\n" +
                "\t\t\"badgeVillePlayerIDs\": [],\n" +
                "\t\t\"batchId\": null,\n" +
                "\t\t\"birthday\": null,\n" +
                "\t\t\"campaigns\": [],\n" +
                "\t\t\"catalogLocaleItem\": null,\n" +
                "\t\t\"children\": [],\n" +
                "\t\t\"consentVerifiedAt\": null,\n" +
                "\t\t\"consents\": [{\n" +
                "\t\t\t\"confirmationGiven\": \"null\",\n" +
                "\t\t\t\"microSiteID\": \"77000\",\n" +
                "\t\t\t\"communicationSentAt\": null,\n" +
                "\t\t\t\"confirmationStoredAt\": \null\",\n" +
                "\t\t\t\"confirmationCommunicationSentAt\": null,\n" +
                "\t\t\t\"campaignId\": \"CL20150501_PC_TB_COPPA\",\n" +
                "\t\t\t\"given\": \"true\",\n" +
                "\t\t\t\"locale\": \"en_US\",\n" +
                "\t\t\t\"id\": 1214312592,\n" +
                "\t\t\t\"storedAt\": \"2016-03-12 06:27:04 +0000\",\n" +
                "\t\t\t\"confirmationCommunicationToSendAt\": null\n" +
                "\t\t}],\n" +
                "\t\t\"consumerInterests\": [],\n" +
                "\t\t\"consumerPoints\": null,\n" +
                "\t\t\"coppaCommunicationSentAt\": null,\n" +
                "\t\t\"created\": \"2016-03-11 09:53:58.662891 +0000\",\n" +
                "\t\t\"currentLocation\": null,\n" +
                "\t\t\"deactivatedAccount\": null,\n" +
                "\t\t\"display\": null,\n" +
                "\t\t\"displayName\": null,\n" +
                "\t\t\"email\": \"vin@bin.com\",\n" +
                "\t\t\"emailVerified\": \"2016-03-11 09:53:58 +0000\",\n" +
                "\t\t\"familyId\": null,\n" +
                "\t\t\"familyName\": null,\n" +
                "\t\t\"familyRole\": null,\n" +
                "\t\t\"gender\": null,\n" +
                "\t\t\"givenName\": \"vin@bin.com\",\n" +
                "\t\t\"id\": 1135585486,\n" +
                "\t\t\"interestAvent\": null,\n" +
                "\t\t\"interestCampaigns\": null,\n" +
                "\t\t\"interestCategories\": null,\n" +
                "\t\t\"interestCommunications\": null,\n" +
                "\t\t\"interestPromotions\": null,\n" +
                "\t\t\"interestStreamiumSurveys\": null,\n" +
                "\t\t\"interestStreamiumUpgrades\": null,\n" +
                "\t\t\"interestSurveys\": null,\n" +
                "\t\t\"interestWULsounds\": null,\n" +
                "\t\t\"lastLogin\": \"2016-04-12 09:34:33 +0000\",\n" +
                "\t\t\"lastModifiedDate\": null,\n" +
                "\t\t\"lastModifiedSource\": null,\n" +
                "\t\t\"lastUpdated\": \"2016-04-12 09:34:33.947792 +0000\",\n" +
                "\t\t\"legacyID\": null,\n" +
                "\t\t\"maritalStatus\": null,\n" +
                "\t\t\"medicalProfessionalRoleSpecified\": null,\n" +
                "\t\t\"middleName\": null,\n" +
                "\t\t\"nettvTCAgreed\": null,\n" +
                "\t\t\"nettvTermsAgreedDate\": null,\n" +
                "\t\t\"nickName\": null,\n" +
                "\t\t\"olderThanAgeLimit\": true,\n" +
                "\t\t\"password\": {\n" +
                "\t\t\t\"value\": \"$2a$04$xlCW2tYE48OMAFsaqQ856eCRxWkZ3qTPWWyid1LAtpG179xHOKPhm\",\n" +
                "\t\t\t\"type\": \"password-bcrypt\"\n" +
                "\t\t},\n" +
                "\t\t\"personalDataTransferAcceptance\": null,\n" +
                "\t\t\"personalDataUsageAcceptance\": null,\n" +
                "\t\t\"photos\": [],\n" +
                "\t\t\"post_login_confirmation\": [],\n" +
                "\t\t\"preferredLanguage\": \"en\",\n" +
                "\t\t\"primaryAddress\": {\n" +
                "\t\t\t\"company\": \"\",\n" +
                "\t\t\t\"address2\": \"\",\n" +
                "\t\t\t\"zipPlus4\": \"\",\n" +
                "\t\t\t\"city\": \"\",\n" +
                "\t\t\t\"state\": \"\",\n" +
                "\t\t\t\"address1\": \"\",\n" +
                "\t\t\t\"houseNumber\": \"\",\n" +
                "\t\t\t\"phone\": \"\",\n" +
                "\t\t\t\"dayTimePhoneNumber\": \"\",\n" +
                "\t\t\t\"zip\": \"\",\n" +
                "\t\t\t\"mobile\": \"\",\n" +
                "\t\t\t\"country\": \"GB\",\n" +
                "\t\t\t\"address3\": \"\"\n" +
                "\t\t},\n" +
                "\t\t\"profiles\": [],\n" +
                "\t\t\"providerMergedLast\": null,\n" +
                "\t\t\"receiveMarketingEmail\": true,\n" +
                "\t\t\"roles\": [{\n" +
                "\t\t\t\"id\": 1135972315,\n" +
                "\t\t\t\"role\": \"consumer\",\n" +
                "\t\t\t\"role_assigned\": \"2016-03-11 15:24:00 +0000\"\n" +
                "\t\t}],\n" +
                "\t\t\"salutation\": null,\n" +
                "\t\t\"ssn\": null,\n" +
                "\t\t\"statuses\": [],\n" +
                "\t\t\"streamiumServicesTCAgreed\": null,\n" +
                "\t\t\"termsAndConditionsAcceptance\": null,\n" +
                "\t\t\"uuid\": \"97681eca-d2a1-4990-8c05-19c9a984f14d\",\n" +
                "\t\t\"visitedMicroSites\": [{\n" +
                "\t\t\t\"microSiteID\": \"77000\",\n" +
                "\t\t\t\"id\": 1189292149,\n" +
                "\t\t\t\"timestamp\": \"2016-03-25 20:01:29 +0000\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"microSiteID\": \"77000\",\n" +
                "\t\t\t\"id\": 1214424860,\n" +
                "\t\t\t\"timestamp\": \"2016-04-12 15:04:33 +0000\"\n" +
                "\t\t}],\n" +
                "\t\t\"weddingDate\": null,\n" +
                "\t\t\"wishList\": null\n" +
                "\t}\n" +
                "}";
        Context context;// = getActivity();
        public CoppaConfigurationTest() {
                super(RegistrationCoppaActivity.class);
        }

        @Override
        protected void setUp() throws Exception {
                super.setUp();

                context = getInstrumentation().getContext();
                if(RegistrationHelper.getInstance().getAppInfraInstance() == null){
                        RegistrationHelper.getInstance().setAppInfraInstance(new AppInfra.Builder().build(context));
                }
                RLog.init();




                System.setProperty("dexmaker.dexcache", context.getCacheDir().getPath());
                //Configure PIL


        }



        private void saveToDisk(final String data) {
                Jump.getSecureStorageInterface().storeValueForKey("jr_capture_signed_in_user",data, new SecureStorageInterface.SecureStorageError());
                ThreadUtils.executeInBg(new Runnable() {
                        public void run() {
                        try {
                                context.deleteFile("jr_capture_signed_in_user");
                        }  catch (Exception e) {
                                throwDebugException(new RuntimeException(e));
                        }
                        }
                });
        }

       /* public void test_ConfirmationStatus(){
                //deleteFromDisk();

                try {
                        saveToDisk(COPPA_CONFIRMED_SIGNED_USER);
                        SecureStorage.init(context);
                        Jump.loadUserFromDiskInternal(context);
                        CoppaExtension coppaExtension = new CoppaExtension(context);
                        coppaExtension.buildConfiguration();

                        assertNotNull(coppaExtension.getCoppaEmailConsentStatus());
                        assertEquals(CoppaStatus.kDICOPPAConfirmationGiven, coppaExtension.getCoppaEmailConsentStatus());
                        Jump.signOutCaptureUser(context);
                }catch (ConcurrentModificationException e){

                }


        }*/

        private void deleteFromDisk(){
                context.deleteFile("jr_capture_signed_in_user");
        }

        /*public void test_ConsentStatus(){
                try {
                //deleteFromDisk();
                Jump.signOutCaptureUser(context);
               saveToDisk(COPPA_CONSENT_SIGNED_USER);
                SecureStorage.init(context);
                Jump.loadUserFromDiskInternal(context);
                CoppaExtension coppaExtension = new CoppaExtension(context);
                coppaExtension.resetConfiguration();
                coppaExtension.buildConfiguration();

                assertNotNull(coppaExtension.getCoppaEmailConsentStatus());
                assertEquals(CoppaStatus.kDICOPPAConfirmationPending, coppaExtension.getCoppaEmailConsentStatus());
                }catch (ConcurrentModificationException e){

                }

        }*/
}
