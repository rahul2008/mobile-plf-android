/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.securestorage;

import android.content.Context;
import android.content.SharedPreferences;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.MockitoTestCase;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Created by 310238114 on 4/7/2016.
 */

public class SecureStorageTest extends MockitoTestCase {
    SecureStorageInterface mSecureStorage=null;
   // Context context = Mockito.mock(Context.class);

    private Context context;
    private AppInfra mAppInfra;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
//        context = getInstrumentation().getContext();
//

//        MockitoAnnotations.initMocks(this);
        context = getInstrumentation().getContext();
        assertNotNull(context);
        mAppInfra =  new AppInfra.Builder().build(context);
        mSecureStorage = mAppInfra.getSecureStorage();
        assertNotNull(mSecureStorage);



    }




    public void testStoreValueForKey() throws Exception {

        SecureStorage secureStorageMock = mock(SecureStorage.class);

        SecureStorageError sse = new SecureStorageError();

        assertFalse(mSecureStorage.storeValueForKey("", "value",sse));
        assertFalse(mSecureStorage.storeValueForKey("", "",sse));
        assertFalse(mSecureStorage.storeValueForKey("key", null,sse));
        assertFalse(mSecureStorage.storeValueForKey(null, "value",sse));
        assertFalse(mSecureStorage.storeValueForKey(null, null,sse));
        assertTrue(mSecureStorage.storeValueForKey("key", "",sse)); // value can be empty
        assertFalse(mSecureStorage.storeValueForKey(" ", "val",sse)); // value can be empty
        assertFalse(mSecureStorage.storeValueForKey("   ", "val",sse)); // value can be empty

        assertTrue(mSecureStorage.storeValueForKey("key", "value",sse)); // true condition

        // value passed by user should not be same as that of its encrypted equivalent

        }

    public void testFetchValuetForKey() throws Exception {
        SecureStorageError sse = new SecureStorageError();
        assertNull(mSecureStorage.fetchValueForKey(null,sse));
        assertNull(mSecureStorage.fetchValueForKey("",sse));
        assertNull(mSecureStorage.fetchValueForKey("NotSavedKey",sse));

    }

    public void testSharedPreferences(){
        final SharedPreferences sharedPreferencesMock = mock(SharedPreferences.class);
        when(sharedPreferencesMock.getString("key",null)).thenReturn("value");
        when(sharedPreferencesMock.getString("",null)).thenReturn(null);
        when(sharedPreferencesMock.getString(null,null)).thenReturn(null);
        SecureStorage secureStorage = new SecureStorage(mAppInfra){

            private SharedPreferences getSharedPreferences() {
                return sharedPreferencesMock;
            }
        };
    }

    public void testRemoveValueForKey() throws Exception {

        assertFalse(mSecureStorage.removeValueForKey(""));
        assertFalse(mSecureStorage.removeValueForKey(null));

        //assertEquals(mSecureStorage.RemoveValueForKey("key"),mSecureStorage.deleteEncryptedData("key"));


    }
    public void testHappyPath()throws Exception {
        String valueStored= "value";
        String keyStored= "key";
        SecureStorageError sse = new SecureStorageError();
        assertTrue(mSecureStorage.storeValueForKey(keyStored, valueStored,sse));
//        assertEquals(valueStored, mSecureStorage.fetchValueForKey(keyStored));
        assertTrue(mSecureStorage.removeValueForKey(keyStored));
        assertNull(mSecureStorage.fetchValueForKey(keyStored,sse));
    }

    public void testMultipleCallIndependentMethods()throws Exception {
        String valueStored= "value";
        String keyStored= "key";
        SecureStorageError sse = new SecureStorageError();
        int iCount;
        for(iCount=0;iCount<10;iCount++){
            assertTrue(mSecureStorage.storeValueForKey(keyStored, valueStored,sse));
        }
        for(iCount=0;iCount<10;iCount++) {
//            assertEquals(valueStored, mSecureStorage.fetchValueForKey(keyStored));
        }

        assertTrue(mSecureStorage.removeValueForKey(keyStored));
        for(iCount=0;iCount<10;iCount++) {
            assertFalse(mSecureStorage.removeValueForKey(keyStored));
        }
    }

    public void testMultipleCallSequentialMethods()throws Exception {
        String valueStored= "value";
        String keyStored= "key";
        SecureStorageError sse = new SecureStorageError();
        int iCount;
        for(iCount=0;iCount<10;iCount++){
            assertTrue(mSecureStorage.storeValueForKey(keyStored, valueStored,sse));
//            assertEquals(valueStored, mSecureStorage.fetchValueForKey(keyStored));
        }


    }

    public void testLargeValue()throws Exception {
        String valueStored= getLargeString();
        String keyStored= "keyLarge";
        SecureStorageError sse = new SecureStorageError();
        assertTrue(mSecureStorage.storeValueForKey(keyStored, valueStored,sse));
        assertNotNull(mSecureStorage.fetchValueForKey(keyStored,sse));
        assertEquals(valueStored, mSecureStorage.fetchValueForKey(keyStored,sse));
    }

    private String getLargeString(){
        /*
        * This sample text in User Registration  (33KB)
        * */

        return
                "/*\n" +
                        " *  Copyright (c) Koninklijke Philips N.V., 2016\n" +
                        " *  All rights are reserved. Reproduction or dissemination\n" +
                        " *  * in whole or in part is prohibited without the prior written\n" +
                        " *  * consent of the copyright holder.\n" +
                        " * /\n" +
                        " */\n" +
                        "\n" +
                        "package com.philips.cdp.registration;\n" +
                        "\n" +
                        "import android.content.Context;\n" +
                        "import android.test.ActivityInstrumentationTestCase2;\n" +
                        "\n" +
                        "import com.janrain.android.Jump;\n" +
                        "import com.philips.cdp.registration.configuration.Configuration;\n" +
                        "import com.philips.cdp.registration.configuration.PILConfiguration;\n" +
                        "import com.philips.cdp.registration.configuration.RegistrationConfiguration;\n" +
                        "import com.philips.cdp.registration.configuration.RegistrationDynamicConfiguration;\n" +
                        "import com.philips.cdp.registration.dao.DIUserProfile;\n" +
                        "import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;\n" +
                        "import com.philips.cdp.registration.handlers.SocialProviderLoginHandler;\n" +
                        "import com.philips.cdp.registration.handlers.TraditionalLoginHandler;\n" +
                        "import com.philips.cdp.registration.settings.UserRegistrationInitializer;\n" +
                        "import com.philips.cdp.registration.ui.traditional.RegistrationActivity;\n" +
                        "import com.philips.cdp.registration.ui.utils.RegPreferenceUtility;\n" +
                        "import com.philips.cdp.security.SecureStorage;\n" +
                        "\n" +
                        "import org.json.JSONObject;\n" +
                        "\n" +
                        "import java.io.FileNotFoundException;\n" +
                        "import java.io.FileOutputStream;\n" +
                        "import java.io.IOException;\n" +
                        "import java.io.ObjectOutputStream;\n" +
                        "import java.io.UnsupportedEncodingException;\n" +
                        "import java.lang.reflect.InvocationTargetException;\n" +
                        "import java.lang.reflect.Method;\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "import static com.janrain.android.utils.LogUtils.throwDebugException;\n" +
                        "\n" +
                        "/**\n" +
                        " * Created by 310202337 on 4/12/2016.\n" +
                        " */\n" +
                        "public class UserTest extends ActivityInstrumentationTestCase2<RegistrationActivity> {\n" +
                        "\n" +
                        "        public UserTest() {\n" +
                        "                super(RegistrationActivity.class);\n" +
                        "        }\n" +
                        "        JSONObject signed_user ;\n" +
                        "        String COPPA_CONFIRMED_SIGNED_USER = \"{\\n\" +\n" +
                        "                \"\\t\\\"original\\\": {\\n\" +\n" +
                        "                \"\\t\\t\\\"lastModifiedDate\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"providerMergedLast\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"preferredLanguage\\\": \\\"en\\\",\\n\" +\n" +
                        "                \"\\t\\t\\\"personalDataUsageAcceptance\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"post_login_confirmation\\\": [],\\n\" +\n" +
                        "                \"\\t\\t\\\"streamiumServicesTCAgreed\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"birthday\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"consumerPoints\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"batchId\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"familyName\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"weddingDate\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"interestStreamiumSurveys\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"interestAvent\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"lastModifiedSource\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"medicalProfessionalRoleSpecified\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"catalogLocaleItem\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"receiveMarketingEmail\\\": true,\\n\" +\n" +
                        "                \"\\t\\t\\\"familyId\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"children\\\": [],\\n\" +\n" +
                        "                \"\\t\\t\\\"consumerInterests\\\": [],\\n\" +\n" +
                        "                \"\\t\\t\\\"profiles\\\": [],\\n\" +\n" +
                        "                \"\\t\\t\\\"legacyID\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"id\\\": 1135585486,\\n\" +\n" +
                        "                \"\\t\\t\\\"deactivatedAccount\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"consentVerifiedAt\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"avmTCAgreed\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"interestCommunications\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"campaigns\\\": [],\\n\" +\n" +
                        "                \"\\t\\t\\\"badgeVillePlayerIDs\\\": [],\\n\" +\n" +
                        "                \"\\t\\t\\\"nettvTCAgreed\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"visitedMicroSites\\\": [{\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"microSiteID\\\": \\\"77000\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"id\\\": 1189292149,\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"timestamp\\\": \\\"2016-03-25 20:01:29 +0000\\\"\\n\" +\n" +
                        "                \"\\t\\t}, {\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"microSiteID\\\": \\\"77000\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"id\\\": 1214424860,\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"timestamp\\\": \\\"2016-04-12 15:04:33 +0000\\\"\\n\" +\n" +
                        "                \"\\t\\t}],\\n\" +\n" +
                        "                \"\\t\\t\\\"middleName\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"familyRole\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"emailVerified\\\": \\\"2016-03-11 09:53:58 +0000\\\",\\n\" +\n" +
                        "                \"\\t\\t\\\"primaryAddress\\\": {\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"company\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"address2\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"zipPlus4\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"city\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"state\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"address1\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"houseNumber\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"phone\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"dayTimePhoneNumber\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"zip\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"mobile\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"country\\\": \\\"GB\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"address3\\\": \\\"\\\"\\n\" +\n" +
                        "                \"\\t\\t},\\n\" +\n" +
                        "                \"\\t\\t\\\"gender\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"lastUpdated\\\": \\\"2016-04-12 09:34:33.947792 +0000\\\",\\n\" +\n" +
                        "                \"\\t\\t\\\"termsAndConditionsAcceptance\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"password\\\": {\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"value\\\": \\\"$2a$04$xlCW2tYE48OMAFsaqQ856eCRxWkZ3qTPWWyid1LAtpG179xHOKPhm\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"type\\\": \\\"password-bcrypt\\\"\\n\" +\n" +
                        "                \"\\t\\t},\\n\" +\n" +
                        "                \"\\t\\t\\\"roles\\\": [{\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"id\\\": 1135972315,\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"role\\\": \\\"consumer\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"role_assigned\\\": \\\"2016-03-11 15:24:00 +0000\\\"\\n\" +\n" +
                        "                \"\\t\\t}],\\n\" +\n" +
                        "                \"\\t\\t\\\"wishList\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"photos\\\": [],\\n\" +\n" +
                        "                \"\\t\\t\\\"email\\\": \\\"vin@bin.com\\\",\\n\" +\n" +
                        "                \"\\t\\t\\\"coppaCommunicationSentAt\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"givenName\\\": \\\"vin@bin.com\\\",\\n\" +\n" +
                        "                \"\\t\\t\\\"nettvTermsAgreedDate\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"currentLocation\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"lastLogin\\\": \\\"2016-04-12 09:34:33 +0000\\\",\\n\" +\n" +
                        "                \"\\t\\t\\\"interestPromotions\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"ssn\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"NRIC\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"interestCampaigns\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"interestSurveys\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"avmTermsAgreedDate\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"interestWULsounds\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"interestCategories\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"created\\\": \\\"2016-03-11 09:53:58.662891 +0000\\\",\\n\" +\n" +
                        "                \"\\t\\t\\\"interestStreamiumUpgrades\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"nickName\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"CPF\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"displayName\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"uuid\\\": \\\"97681eca-d2a1-4990-8c05-19c9a984f14d\\\",\\n\" +\n" +
                        "                \"\\t\\t\\\"olderThanAgeLimit\\\": true,\\n\" +\n" +
                        "                \"\\t\\t\\\"aboutMe\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"consents\\\": [{\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"confirmationGiven\\\": true,\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"microSiteID\\\": \\\"77000\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"communicationSentAt\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"confirmationStoredAt\\\": \\\"2016-04-12 06:28:05 +0000\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"confirmationCommunicationSentAt\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"campaignId\\\": \\\"CL20150501_PC_TB_COPPA\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"given\\\": true,\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"locale\\\": \\\"en_US\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"id\\\": 1214312592,\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"storedAt\\\": \\\"2016-03-12 06:27:04 +0000\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"confirmationCommunicationToSendAt\\\": null\\n\" +\n" +
                        "                \"\\t\\t}],\\n\" +\n" +
                        "                \"\\t\\t\\\"personalDataTransferAcceptance\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"salutation\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"display\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"statuses\\\": [],\\n\" +\n" +
                        "                \"\\t\\t\\\"maritalStatus\\\": null\\n\" +\n" +
                        "                \"\\t},\\n\" +\n" +
                        "                \"\\t\\\"accessToken\\\": \\\"accessToken2geyffjqmcwr746c\\\",\\n\" +\n" +
                        "                \"\\t\\\"this\\\": {\\n\" +\n" +
                        "                \"\\t\\t\\\"CPF\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"NRIC\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"aboutMe\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"avmTCAgreed\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"avmTermsAgreedDate\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"badgeVillePlayerIDs\\\": [],\\n\" +\n" +
                        "                \"\\t\\t\\\"batchId\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"birthday\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"campaigns\\\": [],\\n\" +\n" +
                        "                \"\\t\\t\\\"catalogLocaleItem\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"children\\\": [],\\n\" +\n" +
                        "                \"\\t\\t\\\"consentVerifiedAt\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"consents\\\": [{\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"confirmationGiven\\\": true,\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"microSiteID\\\": \\\"77000\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"communicationSentAt\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"confirmationStoredAt\\\": \\\"2016-04-12 06:28:05 +0000\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"confirmationCommunicationSentAt\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"campaignId\\\": \\\"CL20150501_PC_TB_COPPA\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"given\\\": true,\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"locale\\\": \\\"en_US\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"id\\\": 1214312592,\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"storedAt\\\": \\\"2016-03-12 06:27:04 +0000\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"confirmationCommunicationToSendAt\\\": null\\n\" +\n" +
                        "                \"\\t\\t}],\\n\" +\n" +
                        "                \"\\t\\t\\\"consumerInterests\\\": [],\\n\" +\n" +
                        "                \"\\t\\t\\\"consumerPoints\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"coppaCommunicationSentAt\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"created\\\": \\\"2016-03-11 09:53:58.662891 +0000\\\",\\n\" +\n" +
                        "                \"\\t\\t\\\"currentLocation\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"deactivatedAccount\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"display\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"displayName\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"email\\\": \\\"vin@bin.com\\\",\\n\" +\n" +
                        "                \"\\t\\t\\\"emailVerified\\\": \\\"2016-03-11 09:53:58 +0000\\\",\\n\" +\n" +
                        "                \"\\t\\t\\\"familyId\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"familyName\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"familyRole\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"gender\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"givenName\\\": \\\"vin@bin.com\\\",\\n\" +\n" +
                        "                \"\\t\\t\\\"id\\\": 1135585486,\\n\" +\n" +
                        "                \"\\t\\t\\\"interestAvent\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"interestCampaigns\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"interestCategories\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"interestCommunications\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"interestPromotions\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"interestStreamiumSurveys\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"interestStreamiumUpgrades\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"interestSurveys\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"interestWULsounds\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"lastLogin\\\": \\\"2016-04-12 09:34:33 +0000\\\",\\n\" +\n" +
                        "                \"\\t\\t\\\"lastModifiedDate\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"lastModifiedSource\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"lastUpdated\\\": \\\"2016-04-12 09:34:33.947792 +0000\\\",\\n\" +\n" +
                        "                \"\\t\\t\\\"legacyID\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"maritalStatus\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"medicalProfessionalRoleSpecified\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"middleName\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"nettvTCAgreed\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"nettvTermsAgreedDate\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"nickName\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"olderThanAgeLimit\\\": true,\\n\" +\n" +
                        "                \"\\t\\t\\\"password\\\": {\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"value\\\": \\\"$2a$04$xlCW2tYE48OMAFsaqQ856eCRxWkZ3qTPWWyid1LAtpG179xHOKPhm\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"type\\\": \\\"password-bcrypt\\\"\\n\" +\n" +
                        "                \"\\t\\t},\\n\" +\n" +
                        "                \"\\t\\t\\\"personalDataTransferAcceptance\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"personalDataUsageAcceptance\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"photos\\\": [],\\n\" +\n" +
                        "                \"\\t\\t\\\"post_login_confirmation\\\": [],\\n\" +\n" +
                        "                \"\\t\\t\\\"preferredLanguage\\\": \\\"en\\\",\\n\" +\n" +
                        "                \"\\t\\t\\\"primaryAddress\\\": {\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"company\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"address2\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"zipPlus4\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"city\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"state\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"address1\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"houseNumber\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"phone\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"dayTimePhoneNumber\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"zip\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"mobile\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"country\\\": \\\"GB\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"address3\\\": \\\"\\\"\\n\" +\n" +
                        "                \"\\t\\t},\\n\" +\n" +
                        "                \"\\t\\t\\\"profiles\\\": [],\\n\" +\n" +
                        "                \"\\t\\t\\\"providerMergedLast\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"receiveMarketingEmail\\\": true,\\n\" +\n" +
                        "                \"\\t\\t\\\"roles\\\": [{\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"id\\\": 1135972315,\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"role\\\": \\\"consumer\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"role_assigned\\\": \\\"2016-03-11 15:24:00 +0000\\\"\\n\" +\n" +
                        "                \"\\t\\t}],\\n\" +\n" +
                        "                \"\\t\\t\\\"salutation\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"ssn\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"statuses\\\": [],\\n\" +\n" +
                        "                \"\\t\\t\\\"streamiumServicesTCAgreed\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"termsAndConditionsAcceptance\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"uuid\\\": \\\"97681eca-d2a1-4990-8c05-19c9a984f14d\\\",\\n\" +\n" +
                        "                \"\\t\\t\\\"visitedMicroSites\\\": [{\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"microSiteID\\\": \\\"77000\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"id\\\": 1189292149,\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"timestamp\\\": \\\"2016-03-25 20:01:29 +0000\\\"\\n\" +\n" +
                        "                \"\\t\\t}, {\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"microSiteID\\\": \\\"77000\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"id\\\": 1214424860,\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"timestamp\\\": \\\"2016-04-12 15:04:33 +0000\\\"\\n\" +\n" +
                        "                \"\\t\\t}],\\n\" +\n" +
                        "                \"\\t\\t\\\"weddingDate\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"wishList\\\": null\\n\" +\n" +
                        "                \"\\t}\\n\" +\n" +
                        "                \"}\";\n" +
                        "\n" +
                        "        String COPPA_CONSENT_SIGNED_USER = \"{\\n\" +\n" +
                        "                \"\\t\\\"original\\\": {\\n\" +\n" +
                        "                \"\\t\\t\\\"lastModifiedDate\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"providerMergedLast\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"preferredLanguage\\\": \\\"en\\\",\\n\" +\n" +
                        "                \"\\t\\t\\\"personalDataUsageAcceptance\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"post_login_confirmation\\\": [],\\n\" +\n" +
                        "                \"\\t\\t\\\"streamiumServicesTCAgreed\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"birthday\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"consumerPoints\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"batchId\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"familyName\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"weddingDate\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"interestStreamiumSurveys\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"interestAvent\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"lastModifiedSource\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"medicalProfessionalRoleSpecified\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"catalogLocaleItem\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"receiveMarketingEmail\\\": true,\\n\" +\n" +
                        "                \"\\t\\t\\\"familyId\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"children\\\": [],\\n\" +\n" +
                        "                \"\\t\\t\\\"consumerInterests\\\": [],\\n\" +\n" +
                        "                \"\\t\\t\\\"profiles\\\": [],\\n\" +\n" +
                        "                \"\\t\\t\\\"legacyID\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"id\\\": 1135585486,\\n\" +\n" +
                        "                \"\\t\\t\\\"deactivatedAccount\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"consentVerifiedAt\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"avmTCAgreed\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"interestCommunications\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"campaigns\\\": [],\\n\" +\n" +
                        "                \"\\t\\t\\\"badgeVillePlayerIDs\\\": [],\\n\" +\n" +
                        "                \"\\t\\t\\\"nettvTCAgreed\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"visitedMicroSites\\\": [{\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"microSiteID\\\": \\\"77000\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"id\\\": 1189292149,\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"timestamp\\\": \\\"2016-03-25 20:01:29 +0000\\\"\\n\" +\n" +
                        "                \"\\t\\t}, {\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"microSiteID\\\": \\\"77000\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"id\\\": 1214424860,\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"timestamp\\\": \\\"2016-04-12 15:04:33 +0000\\\"\\n\" +\n" +
                        "                \"\\t\\t}],\\n\" +\n" +
                        "                \"\\t\\t\\\"middleName\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"familyRole\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"emailVerified\\\": \\\"2016-03-11 09:53:58 +0000\\\",\\n\" +\n" +
                        "                \"\\t\\t\\\"primaryAddress\\\": {\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"company\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"address2\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"zipPlus4\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"city\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"state\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"address1\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"houseNumber\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"phone\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"dayTimePhoneNumber\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"zip\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"mobile\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"country\\\": \\\"GB\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"address3\\\": \\\"\\\"\\n\" +\n" +
                        "                \"\\t\\t},\\n\" +\n" +
                        "                \"\\t\\t\\\"gender\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"lastUpdated\\\": \\\"2016-04-12 09:34:33.947792 +0000\\\",\\n\" +\n" +
                        "                \"\\t\\t\\\"termsAndConditionsAcceptance\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"password\\\": {\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"value\\\": \\\"$2a$04$xlCW2tYE48OMAFsaqQ856eCRxWkZ3qTPWWyid1LAtpG179xHOKPhm\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"type\\\": \\\"password-bcrypt\\\"\\n\" +\n" +
                        "                \"\\t\\t},\\n\" +\n" +
                        "                \"\\t\\t\\\"roles\\\": [{\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"id\\\": 1135972315,\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"role\\\": \\\"consumer\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"role_assigned\\\": \\\"2016-03-11 15:24:00 +0000\\\"\\n\" +\n" +
                        "                \"\\t\\t}],\\n\" +\n" +
                        "                \"\\t\\t\\\"wishList\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"photos\\\": [],\\n\" +\n" +
                        "                \"\\t\\t\\\"email\\\": \\\"vin@bin.com\\\",\\n\" +\n" +
                        "                \"\\t\\t\\\"coppaCommunicationSentAt\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"givenName\\\": \\\"vin@bin.com\\\",\\n\" +\n" +
                        "                \"\\t\\t\\\"nettvTermsAgreedDate\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"currentLocation\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"lastLogin\\\": \\\"2016-04-12 09:34:33 +0000\\\",\\n\" +\n" +
                        "                \"\\t\\t\\\"interestPromotions\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"ssn\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"NRIC\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"interestCampaigns\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"interestSurveys\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"avmTermsAgreedDate\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"interestWULsounds\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"interestCategories\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"created\\\": \\\"2016-03-11 09:53:58.662891 +0000\\\",\\n\" +\n" +
                        "                \"\\t\\t\\\"interestStreamiumUpgrades\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"nickName\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"CPF\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"displayName\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"uuid\\\": \\\"97681eca-d2a1-4990-8c05-19c9a984f14d\\\",\\n\" +\n" +
                        "                \"\\t\\t\\\"olderThanAgeLimit\\\": true,\\n\" +\n" +
                        "                \"\\t\\t\\\"aboutMe\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"consents\\\": [{\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"confirmationGiven\\\": \\\"null\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"microSiteID\\\": \\\"77000\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"communicationSentAt\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"confirmationStoredAt\\\": \\\"null\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"confirmationCommunicationSentAt\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"campaignId\\\": \\\"CL20150501_PC_TB_COPPA\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"given\\\": \\\"true\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"locale\\\": \\\"en_US\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"id\\\": 1214312592,\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"storedAt\\\": \\\"2016-03-12 06:27:04 +0000\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"confirmationCommunicationToSendAt\\\": null\\n\" +\n" +
                        "                \"\\t\\t}],\\n\" +\n" +
                        "                \"\\t\\t\\\"personalDataTransferAcceptance\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"salutation\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"display\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"statuses\\\": [],\\n\" +\n" +
                        "                \"\\t\\t\\\"maritalStatus\\\": null\\n\" +\n" +
                        "                \"\\t},\\n\" +\n" +
                        "                \"\\t\\\"accessToken\\\": \\\"accessToken2geyffjqmcwr746c\\\",\\n\" +\n" +
                        "                \"\\t\\\"this\\\": {\\n\" +\n" +
                        "                \"\\t\\t\\\"CPF\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"NRIC\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"aboutMe\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"avmTCAgreed\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"avmTermsAgreedDate\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"badgeVillePlayerIDs\\\": [],\\n\" +\n" +
                        "                \"\\t\\t\\\"batchId\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"birthday\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"campaigns\\\": [],\\n\" +\n" +
                        "                \"\\t\\t\\\"catalogLocaleItem\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"children\\\": [],\\n\" +\n" +
                        "                \"\\t\\t\\\"consentVerifiedAt\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"consents\\\": [{\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"confirmationGiven\\\": \\\"null\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"microSiteID\\\": \\\"77000\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"communicationSentAt\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"confirmationStoredAt\\\": \\null\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"confirmationCommunicationSentAt\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"campaignId\\\": \\\"CL20150501_PC_TB_COPPA\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"given\\\": \\\"true\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"locale\\\": \\\"en_US\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"id\\\": 1214312592,\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"storedAt\\\": \\\"2016-03-12 06:27:04 +0000\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"confirmationCommunicationToSendAt\\\": null\\n\" +\n" +
                        "                \"\\t\\t}],\\n\" +\n" +
                        "                \"\\t\\t\\\"consumerInterests\\\": [],\\n\" +\n" +
                        "                \"\\t\\t\\\"consumerPoints\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"coppaCommunicationSentAt\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"created\\\": \\\"2016-03-11 09:53:58.662891 +0000\\\",\\n\" +\n" +
                        "                \"\\t\\t\\\"currentLocation\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"deactivatedAccount\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"display\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"displayName\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"email\\\": \\\"vin@bin.com\\\",\\n\" +\n" +
                        "                \"\\t\\t\\\"emailVerified\\\": \\\"2016-03-11 09:53:58 +0000\\\",\\n\" +\n" +
                        "                \"\\t\\t\\\"familyId\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"familyName\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"familyRole\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"gender\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"givenName\\\": \\\"vin@bin.com\\\",\\n\" +\n" +
                        "                \"\\t\\t\\\"id\\\": 1135585486,\\n\" +\n" +
                        "                \"\\t\\t\\\"interestAvent\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"interestCampaigns\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"interestCategories\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"interestCommunications\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"interestPromotions\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"interestStreamiumSurveys\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"interestStreamiumUpgrades\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"interestSurveys\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"interestWULsounds\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"lastLogin\\\": \\\"2016-04-12 09:34:33 +0000\\\",\\n\" +\n" +
                        "                \"\\t\\t\\\"lastModifiedDate\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"lastModifiedSource\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"lastUpdated\\\": \\\"2016-04-12 09:34:33.947792 +0000\\\",\\n\" +\n" +
                        "                \"\\t\\t\\\"legacyID\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"maritalStatus\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"medicalProfessionalRoleSpecified\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"middleName\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"nettvTCAgreed\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"nettvTermsAgreedDate\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"nickName\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"olderThanAgeLimit\\\": true,\\n\" +\n" +
                        "                \"\\t\\t\\\"password\\\": {\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"value\\\": \\\"$2a$04$xlCW2tYE48OMAFsaqQ856eCRxWkZ3qTPWWyid1LAtpG179xHOKPhm\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"type\\\": \\\"password-bcrypt\\\"\\n\" +\n" +
                        "                \"\\t\\t},\\n\" +\n" +
                        "                \"\\t\\t\\\"personalDataTransferAcceptance\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"personalDataUsageAcceptance\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"photos\\\": [],\\n\" +\n" +
                        "                \"\\t\\t\\\"post_login_confirmation\\\": [],\\n\" +\n" +
                        "                \"\\t\\t\\\"preferredLanguage\\\": \\\"en\\\",\\n\" +\n" +
                        "                \"\\t\\t\\\"primaryAddress\\\": {\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"company\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"address2\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"zipPlus4\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"city\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"state\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"address1\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"houseNumber\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"phone\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"dayTimePhoneNumber\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"zip\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"mobile\\\": \\\"\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"country\\\": \\\"GB\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"address3\\\": \\\"\\\"\\n\" +\n" +
                        "                \"\\t\\t},\\n\" +\n" +
                        "                \"\\t\\t\\\"profiles\\\": [],\\n\" +\n" +
                        "                \"\\t\\t\\\"providerMergedLast\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"receiveMarketingEmail\\\": true,\\n\" +\n" +
                        "                \"\\t\\t\\\"roles\\\": [{\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"id\\\": 1135972315,\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"role\\\": \\\"consumer\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"role_assigned\\\": \\\"2016-03-11 15:24:00 +0000\\\"\\n\" +\n" +
                        "                \"\\t\\t}],\\n\" +\n" +
                        "                \"\\t\\t\\\"salutation\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"ssn\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"statuses\\\": [],\\n\" +\n" +
                        "                \"\\t\\t\\\"streamiumServicesTCAgreed\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"termsAndConditionsAcceptance\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"uuid\\\": \\\"97681eca-d2a1-4990-8c05-19c9a984f14d\\\",\\n\" +\n" +
                        "                \"\\t\\t\\\"visitedMicroSites\\\": [{\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"microSiteID\\\": \\\"77000\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"id\\\": 1189292149,\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"timestamp\\\": \\\"2016-03-25 20:01:29 +0000\\\"\\n\" +\n" +
                        "                \"\\t\\t}, {\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"microSiteID\\\": \\\"77000\\\",\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"id\\\": 1214424860,\\n\" +\n" +
                        "                \"\\t\\t\\t\\\"timestamp\\\": \\\"2016-04-12 15:04:33 +0000\\\"\\n\" +\n" +
                        "                \"\\t\\t}],\\n\" +\n" +
                        "                \"\\t\\t\\\"weddingDate\\\": null,\\n\" +\n" +
                        "                \"\\t\\t\\\"wishList\\\": null\\n\" +\n" +
                        "                \"\\t}\\n\" +\n" +
                        "                \"}\";\n" +
                        "        Context context;// = getActivity();\n" +
                        "        @Override\n" +
                        "        protected void setUp() throws Exception {\n" +
                        "                super.setUp();\n" +
                        "                context = getInstrumentation().getTargetContext();\n" +
                        "                System.setProperty(\"dexmaker.dexcache\", context.getCacheDir().getPath());\n" +
                        "                //Configure PIL\n" +
                        "                PILConfiguration pilConfiguration = new PILConfiguration();\n" +
                        "\n" +
                        "                RegistrationDynamicConfiguration.getInstance().getPilConfiguration().setCampaignID(\"CL20150501_PC_TB_COPPA\");\n" +
                        "                RegistrationDynamicConfiguration.getInstance().getPilConfiguration().setMicrositeId(\"77000\");\n" +
                        "                RegistrationDynamicConfiguration.getInstance().getPilConfiguration().setRegistrationEnvironment(Configuration.EVALUATION);\n" +
                        "                UserRegistrationInitializer.getInstance().setJumpInitializationInProgress(true);\n" +
                        "                UserRegistrationInitializer.getInstance().setJanrainIntialized(true);\n" +
                        "\n" +
                        "\n" +
                        "        }\n" +
                        "\n" +
                        "        private void saveToDisk(final String data) {\n" +
                        "                FileOutputStream fos = null;\n" +
                        "                try {\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "                        fos = context.openFileOutput(\"jr_capture_signed_in_user\", 0);\n" +
                        "\n" +
                        "                        ObjectOutputStream oos = new ObjectOutputStream(fos);\n" +
                        "\n" +
                        "                        oos.writeObject(SecureStorage.encrypt(data));\n" +
                        "                        oos.close();\n" +
                        "                        fos.close();\n" +
                        "                } catch (FileNotFoundException e1) {\n" +
                        "                        e1.printStackTrace();\n" +
                        "                } catch (UnsupportedEncodingException e) {\n" +
                        "                        throwDebugException(new RuntimeException(\"Unexpected\", e));\n" +
                        "                } catch (IOException e) {\n" +
                        "                        throwDebugException(new RuntimeException(\"Unexpected\", e));\n" +
                        "                } finally {\n" +
                        "                        if (fos != null) try {\n" +
                        "                                fos.close();\n" +
                        "                        } catch (IOException e) {\n" +
                        "                                throwDebugException(new RuntimeException(\"Unexpected\", e));\n" +
                        "                        }\n" +
                        "                }\n" +
                        "        }\n" +
                        "\n" +
                        "        //Assuming jump always return successful login when correct credentials are passed, this test case intends to increase the code coverage, as Mockito is not able to cover the code when mock objects  are created\n" +
                        "        //and made to simulate the actual code.\n" +
                        "        public void testTraditionalLogin(){\n" +
                        "\n" +
                        "                User user = new User(context);\n" +
                        "                Class userClass = user.getClass();\n" +
                        "                try {\n" +
                        "                        Method loginMethod = userClass.getMethod(\"loginUsingTraditional\", new Class[]{String.class,String.class, TraditionalLoginHandler.class});\n" +
                        "\n" +
                        "                        loginMethod.invoke(user, new Object[]{\"a\",\"b\",new TraditionalLoginHandler() {\n" +
                        "                                @Override\n" +
                        "                                public void onLoginSuccess() {\n" +
                        "                                        saveToDisk(COPPA_CONFIRMED_SIGNED_USER);//Test Case assumes correct credentials will always cause Jump to successfully login the user\n" +
                        "                                        Jump.loadUserFromDiskInternal(context);\n" +
                        "                                }\n" +
                        "\n" +
                        "                                @Override\n" +
                        "                                public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {\n" +
                        "                                        saveToDisk(COPPA_CONFIRMED_SIGNED_USER);//Since no network will be available during test case run, onLoginFailedWithError will be executed, but the safe assumption\n" +
                        "                                                                                //that Jump works correctly on internet made us simulate the successful login scenario here.\n" +
                        "                                        Jump.loadUserFromDiskInternal(context);\n" +
                        "                                }\n" +
                        "                        }});\n" +
                        "                } catch (NoSuchMethodException e) {\n" +
                        "                        e.printStackTrace();\n" +
                        "                } catch (InvocationTargetException e) {\n" +
                        "                        e.printStackTrace();\n" +
                        "                } catch (IllegalAccessException e) {\n" +
                        "                        e.printStackTrace();\n" +
                        "                }\n" +
                        "\n" +
                        "                saveToDisk(COPPA_CONFIRMED_SIGNED_USER);\n" +
                        "                Jump.loadUserFromDiskInternal(context);\n" +
                        "                assertNotNull(Jump.getSignedInUser());\n" +
                        "\n" +
                        "        }\n" +
                        "\n" +
                        "        public void test_getJanrainUUID(){\n" +
                        "                Jump.signOutCaptureUser(context);\n" +
                        "                User user = new User(context);\n" +
                        "                assertNull(user.getJanrainUUID()); //user not logged in so expect a null\n" +
                        "                saveToDisk(COPPA_CONFIRMED_SIGNED_USER);\n" +
                        "                Jump.loadUserFromDiskInternal(context);\n" +
                        "                assertNotNull(user.getJanrainUUID()); //capture files exists, so hjanrainid must be set\n" +
                        "        }\n" +
                        "\n" +
                        "        public void test_isUserSignIn(){\n" +
                        "                Jump.signOutCaptureUser(context);\n" +
                        "                User user = new User(context);\n" +
                        "                assertFalse(user.isUserSignIn());\n" +
                        "                saveToDisk(COPPA_CONFIRMED_SIGNED_USER);\n" +
                        "                RegistrationConfiguration.getInstance().getFlow().setTermsAndConditionsAcceptanceRequired(true);\n" +
                        "                RegPreferenceUtility.storePreference(context,user.getEmail(),true);\n" +
                        "                Jump.loadUserFromDiskInternal(context);\n" +
                        "                assertTrue(user.isUserSignIn());\n" +
                        "\n" +
                        "        }\n" +
                        "\n" +
                        "        public void test_getEmailVerificationStatus(){\n" +
                        "                Jump.signOutCaptureUser(context);\n" +
                        "                User user = new User(context);\n" +
                        "                assertFalse(user.getEmailVerificationStatus());\n" +
                        "                saveToDisk(COPPA_CONFIRMED_SIGNED_USER);\n" +
                        "                Jump.loadUserFromDiskInternal(context);\n" +
                        "                assertTrue(user.getEmailVerificationStatus());\n" +
                        "\n" +
                        "        }\n" +
                        "\n" +
                        "        public void test_LoginUsingSocialProvider(){\n" +
                        "               // ClassPool objClassPool = ClassPool.getDefault();\n" +
                        "\n" +
                        "                        SocialProviderLoginHandler socialProviderLoginHandler = new SocialProviderLoginHandler() {\n" +
                        "                                @Override\n" +
                        "                                public void onLoginSuccess() {\n" +
                        "                                        System.out.println(\"SocialProviderLoginHandler success\");\n" +
                        "                                }\n" +
                        "\n" +
                        "                                @Override\n" +
                        "                                public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {\n" +
                        "\n" +
                        "                                }\n" +
                        "\n" +
                        "                                @Override\n" +
                        "                                public void onLoginFailedWithTwoStepError(JSONObject prefilledRecord, String socialRegistrationToken) {\n" +
                        "\n" +
                        "                                }\n" +
                        "\n" +
                        "                                @Override\n" +
                        "                                public void onLoginFailedWithMergeFlowError(String mergeToken, String existingProvider, String conflictingIdentityProvider, String conflictingIdpNameLocalized, String existingIdpNameLocalized, String emailId) {\n" +
                        "\n" +
                        "                                }\n" +
                        "\n" +
                        "                                @Override\n" +
                        "                                public void onContinueSocialProviderLoginSuccess() {\n" +
                        "\n" +
                        "                                }\n" +
                        "\n" +
                        "                                @Override\n" +
                        "                                public void onContinueSocialProviderLoginFailure(UserRegistrationFailureInfo userRegistrationFailureInfo) {\n" +
                        "\n" +
                        "                                }\n" +
                        "                        };\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "        }\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "}\n";
    }


}