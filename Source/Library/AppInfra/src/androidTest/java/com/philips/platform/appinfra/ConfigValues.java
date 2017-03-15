package com.philips.platform.appinfra;

/**
 * Created by 310243577 on 2/8/2017.
 */

public class ConfigValues {

    static String testJsonString = null;

    public static String testJson() {
        testJsonString = "{\n" +
                "  \"UR\": {\n" +
                "\n" +
                "    \"DEVELOPMENT\": \"ad7nn99y2mv5berw5jxewzagazafbyhu\",\n" +
                "    \"TESTING\": \"xru56jcnu3rpf8q7cgnkr7xtf9sh8pp7\",\n" +
                "    \"EVALUATION\": \"4r36zdbeycca933nufcknn2hnpsz6gxu\",\n" +
                "    \"STAGING\": \"f2stykcygm7enbwfw2u9fbg6h6syb8yd\",\n" +
                "    \"PRODUCTION\": \"mz6tg5rqrg4hjj3wfxfd92kjapsrdhy3\"\n" +
                "\n" +
                "  },\n" +
                "  \"AI\": {\n" +
                "    \"MICROSITEID\": 77001,\n" +
                "    \"REGISTRATIONENVIRONMENT\": \"Staging\",\n" +
                "    \"NL\": [\"googleplus\", \"facebook\"  ],\n" +
                "    \"US\": [\"facebook\",\"googleplus\" ],\n" +
                "    \"MAP\": {\"one\": \"123\", \"two\": \"123.45\"},\n" +
                "    \"EE\": [123,234 ]\n" +
                "  }, \n" +
                " \"APPINFRA\": { \n" +
                "   \"APPIDENTITY.MICROSITEID\" : \"77000\",\n" +
                "  \"APPIDENTITY.SECTOR\"  : \"B2C\",\n" +
                " \"APPIDENTITY.APPSTATE\"  : \"Staging\",\n" +
                "\"APPIDENTITY.SERVICEDISCOVERYENVIRONMENT\"  : \"Staging\",\n" +
                "\"RESTCLIENT.CACHESIZEINKB\"  : 1024, \n" +
                " \"TAGGING.SENSITIVEDATA\": [\"bundleId, language\"] ,\n" +
                "  \"ABTEST.PRECACHE\":[\"philipsmobileappabtest1content\",\"philipsmobileappabtest1success\"],\n" +
                "    \"CONTENTLOADER.LIMITSIZE\":100,\n" +
                "    \"SERVICEDISCOVERY.PLATFORMMICROSITEID\":\"77000\",\n" +
                "    \"SERVICEDISCOVERY.PLATFORMENVIRONMENT\":\"production\",\n" +
                "    \"APPCONFIG.CLOUDSERVICEID\":\" appinfra.appconfigdownload\",\n" +
                "    \"LANGUAGEPACK.SERVICEID\":\"appinfra.languagePack\"\n" +

                "  }\n" +
                "}\n";

        return testJsonString;
    }


}
