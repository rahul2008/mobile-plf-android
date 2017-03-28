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


	public static String getMultipleConfigJson() {
		String resJson = "\t{\"success\":true,\"payload\":{\"country\":\"SG\",\"matchByLanguage\":{\"available\":true,\"results\":[{\"locale\":\"en_SG\",\"configs\":[{\"micrositeId\":\"77001\",\"urls\":{\"appinfra.testing.service\":\"https://test.appinfra.testing.service/en_SG/B2C/77001\",\"appinfra.testing.identity.service\":\"https://test.appinfra.testing.identity.service/en_SG/B2C/77001\",\"appinfra.testing.configuration.service\":\"https://test.appinfra.testing.configuration.service/en_SG/B2C/77001\",\"appinfra.testing.discovery.service\":\"https://www.philips.com/api/v1/discovery/b2c/77001?locale=en_SG&country=SG&testappstate=apps%2b%2benv%2btest\"},\"tags\":[{\"id\":\"apps:env/test\",\"name\":\"test\",\"key\":\"apps++env+test\"}]},{\"micrositeId\":\"77001\",\"urls\":{\"appinfra.appconfigdownload\":\"https://test.prod.appinfra.testing.service/en_SG/B2C/77001\"},\"tags\":[{\"id\":\"apps:env/test\",\"name\":\"test\",\"key\":\"apps++env+test\"},{\"id\":\"apps:env/prod\",\"name\":\"prod\",\"key\":\"apps++env+prod\"}]}]}]},\"matchByCountry\":{\"available\":true,\"results\":[{\"locale\":\"en_SG\",\"configs\":[{\"micrositeId\":\"77001\",\"urls\":{\"appinfra.testing.service\":\"https://test.appinfra.testing.service/en_SG/B2C/77001\",\"appinfra.testing.identity.service\":\"https://test.appinfra.testing.identity.service/en_SG/B2C/77001\",\"appinfra.testing.configuration.service\":\"https://test.appinfra.testing.configuration.service/en_SG/B2C/77001\",\"appinfra.testing.discovery.service\":\"https://www.philips.com/api/v1/discovery/b2c/77001?locale=en_SG&country=SG&testappstate=apps%2b%2benv%2btest\"},\"tags\":[{\"id\":\"apps:env/test\",\"name\":\"test\",\"key\":\"apps++env+test\"}]},{\"micrositeId\":\"77001\",\"urls\":{\"appinfra.appconfigdownload\":\"https://test.prod.appinfra.testing.service/en_SG/B2C/77001\"},\"tags\":[{\"id\":\"apps:env/test\",\"name\":\"test\",\"key\":\"apps++env+test\"},{\"id\":\"apps:env/prod\",\"name\":\"prod\",\"key\":\"apps++env+prod\"}]}]}]}},\"httpStatus\":\"OK\"}";
		return resJson;
	}

	public static String getsdUrlJson() {
		String sdUrlJson = "{\n" +
				"\t\"success\": true,\n" +
				"\t\"payload\": {\n" +
				"\t\t\"country\": \"IN\",\n" +
				"\t\t\"matchByLanguage\": {\n" +
				"\t\t\t\"available\": true,\n" +
				"\t\t\t\"results\": [{\n" +
				"\t\t\t\t\"locale\": \"en_IN\",\n" +
				"\t\t\t\t\"configs\": [{\n" +
				"\t\t\t\t\t\"micrositeId\": \"77001\",\n" +
				"\t\t\t\t\t\"urls\": {\n" +
				"\t\t\t\t\t\t\"appinfra.testing.service\": \"https://dev.appinfra.testing.service/en_IN/B2C/77001\",\n" +
				"\t\t\t\t\t\t\"appinfra.testing.identity.service\": \"https://dev.appinfra.testing.identity.service/en_IN/B2C/77001\",\n" +
				"\t\t\t\t\t\t\"appinfra.testing.configuration.service\": \"https://dev.appinfra.testing.configuration.service/en_IN/B2C/77001\",\n" +
				"\t\t\t\t\t\t\"appinfra.testing.discovery.service\": \"https://www.philips.com/api/v1/discovery/b2c/77001?locale=en_IN&country=IN&testappstate=apps%2b%2benv%2bdev\"\n" +
				"\t\t\t\t\t},\n" +
				"\t\t\t\t\t\"tags\": [{\n" +
				"\t\t\t\t\t\t\"id\": \"apps:env/dev\",\n" +
				"\t\t\t\t\t\t\"name\": \"dev\",\n" +
				"\t\t\t\t\t\t\"key\": \"apps++env+dev\"\n" +
				"\t\t\t\t\t}]\n" +
				"\t\t\t\t}]\n" +
				"\t\t\t}]\n" +
				"\t\t},\n" +
				"\t\t\"matchByCountry\": {\n" +
				"\t\t\t\"available\": true,\n" +
				"\t\t\t\"results\": [{\n" +
				"\t\t\t\t\"locale\": \"en_IN\",\n" +
				"\t\t\t\t\"configs\": [{\n" +
				"\t\t\t\t\t\"micrositeId\": \"77001\",\n" +
				"\t\t\t\t\t\"urls\": {\n" +
				"\t\t\t\t\t\t\"appinfra.testing.service\": \"https://dev.appinfra.testing.service/en_IN/B2C/77001\",\n" +
				"\t\t\t\t\t\t\"appinfra.testing.identity.service\": \"https://dev.appinfra.testing.identity.service/en_IN/B2C/77001\",\n" +
				"\t\t\t\t\t\t\"appinfra.testing.configuration.service\": \"https://dev.appinfra.testing.configuration.service/en_IN/B2C/77001\",\n" +
				"\t\t\t\t\t\t\"appinfra.testing.discovery.service\": \"https://www.philips.com/api/v1/discovery/b2c/77001?locale=en_IN&country=IN&testappstate=apps%2b%2benv%2bdev\"\n" +
				"\t\t\t\t\t},\n" +
				"\t\t\t\t\t\"tags\": [{\n" +
				"\t\t\t\t\t\t\"id\": \"apps:env/dev\",\n" +
				"\t\t\t\t\t\t\"name\": \"dev\",\n" +
				"\t\t\t\t\t\t\"key\": \"apps++env+dev\"\n" +
				"\t\t\t\t\t}]\n" +
				"\t\t\t\t}]\n" +
				"\t\t\t}]\n" +
				"\t\t}\n" +
				"\t},\n" +
				"\t\"httpStatus\": \"OK\"\n" +
				"}";
		return sdUrlJson;
	}

	public static String getsdUrlPlatformjson() {

		String sdUrlPlatformjson = "{\n" +
				"\t\"success\": true,\n" +
				"\t\"payload\": {\n" +
				"\t\t\"country\": \"IN\",\n" +
				"\t\t\"matchByLanguage\": {\n" +
				"\t\t\t\"available\": true,\n" +
				"\t\t\t\"results\": [{\n" +
				"\t\t\t\t\"locale\": \"en_IN\",\n" +
				"\t\t\t\t\"configs\": [{\n" +
				"\t\t\t\t\t\"micrositeId\": \"77000\",\n" +
				"\t\t\t\t\t\"urls\": {\n" +
				"\t\t\t\t\t\t\"userreg.janrain.cdn\": \"https://d1lqe9temigv1p.cloudfront.net\",\n" +
				"\t\t\t\t\t\t\"userreg.janrain.api\": \"https://philips.dev.janraincapture.com\",\n" +
				"\t\t\t\t\t\t\"userreg.landing.emailverif\": \"https://10.128.41.111:4503/content/B2C/en_IN/verify-account.html\",\n" +
				"\t\t\t\t\t\t\"userreg.landing.resetpass\": \"https://10.128.41.111:4503/content/B2C/en_IN/myphilips/reset-password.html?cl=mob.html\",\n" +
				"\t\t\t\t\t\t\"userreg.hsdp.userserv\": \"https://user-registration-assembly-testing.us-east.philips-healthsuite.com\",\n" +
				"\t\t\t\t\t\t\"appinfra.testing.service\": \"https://dev.appinfra.testing.service/en_IN/B2C/77000\",\n" +
				"\t\t\t\t\t\t\"prxclient.assets\": \"https://tst.philips.com/prx/product/%sector%/en_IN/%catalog%/products/%ctn%.assets\"\n" +
				"\n" +
				"\t\t\t\t\t},\n" +
				"\t\t\t\t\t\"tags\": [{\n" +
				"\t\t\t\t\t\t\"id\": \"apps:env/dev\",\n" +
				"\t\t\t\t\t\t\"name\": \"dev\",\n" +
				"\t\t\t\t\t\t\"key\": \"apps++env+dev\"\n" +
				"\t\t\t\t\t}]\n" +
				"\t\t\t\t}]\n" +
				"\t\t\t}]\n" +
				"\t\t},\n" +
				"\t\t\"matchByCountry\": {\n" +
				"\t\t\t\"available\": true,\n" +
				"\t\t\t\"results\": [{\n" +
				"\t\t\t\t\"locale\": \"en_IN\",\n" +
				"\t\t\t\t\"configs\": [{\n" +
				"\t\t\t\t\t\"micrositeId\": \"77000\",\n" +
				"\t\t\t\t\t\"urls\": {\n" +
				"\t\t\t\t\t\t\"userreg.janrain.cdn\": \"https://d1lqe9temigv1p.cloudfront.net\",\n" +
				"\t\t\t\t\t\t\"userreg.janrain.api\": \"https://philips.dev.janraincapture.com\",\n" +
				"\t\t\t\t\t\t\"userreg.landing.emailverif\": \"https://10.128.41.111:4503/content/B2C/en_IN/verify-account.html\",\n" +
				"\t\t\t\t\t\t\"userreg.landing.resetpass\": \"https://10.128.41.111:4503/content/B2C/en_IN/myphilips/reset-password.html?cl=mob.html\",\n" +
				"\t\t\t\t\t\t\"userreg.hsdp.userserv\": \"https://user-registration-assembly-testing.us-east.philips-healthsuite.com\",\n" +
				"\t\t\t\t\t\t\"appinfra.testing.service\": \"https://dev.appinfra.testing.service/en_IN/B2C/77000\",\n" +
				"\t\t\t\t\t\t\"prxclient.assets\": \"https://tst.philips.com/prx/product/%sector%/en_IN/%catalog%/products/%ctn%.assets\"\n" +
				"\n" +
				"\t\t\t\t\t},\n" +
				"\t\t\t\t\t\"tags\": [{\n" +
				"\t\t\t\t\t\t\"id\": \"apps:env/dev\",\n" +
				"\t\t\t\t\t\t\"name\": \"dev\",\n" +
				"\t\t\t\t\t\t\"key\": \"apps++env+dev\"\n" +
				"\t\t\t\t\t}]\n" +
				"\t\t\t\t}]\n" +
				"\t\t\t}]\n" +
				"\t\t}\n" +
				"\t},\n" +
				"\t\"httpStatus\": \"OK\"\n" +
				"}";
		return sdUrlPlatformjson;

	}

}
