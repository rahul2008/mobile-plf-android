package com.philips.cdp.registration.configuration;

import com.philips.cdp.registration.ui.utils.RLog;

import java.util.HashMap;

public class ClientIDConfiguration {

    public static final String DEV_CAPTURE_DOMAIN = "https://philips.dev.janraincapture.com";

    public static final String TEST_CAPTURE_DOMAIN = "https://philips-test.dev.janraincapture.com";

    public static final String EVAL_CAPTURE_DOMAIN = "https://philips.eval.janraincapture.com";

    public static final String PROD_CAPTURE_DOMAIN = "https://philips.janraincapture.com";

    public static final String DEV_CAPTURE_DOMAIN_CHINA = "https://philips-cn-dev.capture.cn.janrain.com";

    public static final String DEV_CAPTURE_DOMAIN_CHINA_EU = "https://philips-china-eu.eu-dev.janraincapture.com";

    public static final String TEST_CAPTURE_DOMAIN_CHINA = "https://philips-cn-test.capture.cn.janrain.com";

    public static final String TEST_CAPTURE_DOMAIN_CHINA_EU = "https://philips-china-test.eu-dev.janraincapture.com";

    public static final String EVAL_CAPTURE_DOMAIN_CHINA = "https://philips-cn-staging.capture.cn.janrain.com";

    public static final String PROD_CAPTURE_DOMAIN_CHINA = "https://philips-cn.capture.cn.janrain.com";

    private static final String DEV_CAPTURE_DOMAIN_CAPTUE_ID = "eupac7ugz25x8dwahvrbpmndf8";
    private static final String TEST_CAPTURE_DOMAIN_CAPTUE_ID = "x7nftvwfz8e8vcutz49p8eknqp";
    private static final String EVAL_CAPTURE_DOMAIN_CAPTUE_ID = "nt5dqhp6uck5mcu57snuy8uk6c";
    private static final String PROD_CAPTURE_DOMAIN_CAPTUE_ID = "hffxcm638rna8wrxxggx2gykhc";
    private static final String DEV_CAPTURE_DOMAIN_CHINA_CAPTUE_ID = "7629q5uxm2jyrbk7ehuwryj7a4";
    private static final String DEV_CAPTURE_DOMAIN_CHINA_EU_CAPTUE_ID = "euwkgsf83m56hqknjxgnranezh";
    private static final String TEST_CAPTURE_DOMAIN_CHINA_CAPTUE_ID = "hqmhwxu7jtdcye758vvxux4ryb";
    private static final String TEST_CAPTURE_DOMAIN_CHINA_EU_CAPTUE_ID = "vdgkb3z57jpv93mxub34x73mqu";
    private static final String EVAL_CAPTURE_DOMAIN_CHINA_CAPTUE_ID = "czwfzs7xh23ukmpf4fzhnksjmd";
    private static final String PROD_CAPTURE_DOMAIN_CHINA_CAPTUE_ID = "zkr6yg4mdsnt7f8mvucx7qkja3";


    private static final String DEV_CAPTURE_DOMAIN_ENAGAGE_ID = "bdbppnbjfcibijknnfkk";
    private static final String TEST_CAPTURE_DOMAIN_ENAGAGE_ID = "fhbmobeahciagddgfidm";
    private static final String EVAL_CAPTURE_DOMAIN_ENAGAGE_ID = "jgehpoggnhbagolnihge";
    private static final String PROD_CAPTURE_DOMAIN_ENAGAGE_ID = "ddjbpmgpeifijdlibdio";
    private static final String DEV_CAPTURE_DOMAIN_CHINA_ENAGAGE_ID = "ruaheighoryuoxxdwyfs";
    private static final String DEV_CAPTURE_DOMAIN_CHINA_EU_ENAGAGE_ID = "bdbppnbjfcibijknnfkk";
    private static final String TEST_CAPTURE_DOMAIN_CHINA_ENAGAGE_ID = "jndphelwbhuevcmovqtn";
    private static final String TEST_CAPTURE_DOMAIN_CHINA_EU_ENAGAGE_ID = "fhbmobeahciagddgfidm";
    private static final String EVAL_CAPTURE_DOMAIN_CHINA_ENAGAGE_ID = "uyfpympodtnesxejzuic";
    private static final String PROD_CAPTURE_DOMAIN_CHINA_ENAGAGE_ID = "cfwaqwuwcwzlcozyyjpa";


    private static final String DEV_CAPTURE_DOMAIN_RESET_PASS_CLIENT_ID = "rj95w5ghxqthxxy8jpug5a63wrbeykzk";
    private static final String TEST_CAPTURE_DOMAIN_RESET_PASS_CLIENT_ID = "suxgtg52ej3srf683t7u5gqzw4824avg";
    private static final String EVAL_CAPTURE_DOMAIN_RESET_PASS_CLIENT_ID = "h27n93rjva8xuvzgpeb7jf9jxq6dnnzr";
    private static final String PROD_CAPTURE_DOMAIN_RESET_PASS_CLIENT_ID = "h27n93rjva8xuvzgpeb7jf9jxq6dnnzr";
    private static final String DEV_CAPTURE_DOMAIN_CHINA_RESET_PASS_CLIENT_ID = "xhrue999syb8g2csggp9acs6x87q8q3d";
    private static final String DEV_CAPTURE_DOMAIN_CHINA_EU_RESET_PASS_CLIENT_ID = "4c5tqzbneykdw2md7mkp75uycp23x3qz";
    private static final String TEST_CAPTURE_DOMAIN_CHINA_RESET_PASS_CLIENT_ID = "v2s8qajf9ncfzsyy6ghjpqvsrju9xgvt";
    private static final String TEST_CAPTURE_DOMAIN_CHINA_EU_RESET_PASS_CLIENT_ID = "fh5mfvjqzwhn5t9gdwqqjnbcw9atd7mv";
    private static final String EVAL_CAPTURE_DOMAIN_CHINA_RESET_PASS_CLIENT_ID = "mfvjprjmgbrhfbtn6cq6q2yupzhxn977";
    private static final String PROD_CAPTURE_DOMAIN_CHINA_RESET_PASS_CLIENT_ID = "65dzkyh48ux4vcguhvwsgvtk4bzyh2va";


    public String getCaptureId(String domain) {
        HashMap<String, String> map = new HashMap<>();
        map.put(DEV_CAPTURE_DOMAIN, DEV_CAPTURE_DOMAIN_CAPTUE_ID);
        map.put(TEST_CAPTURE_DOMAIN, TEST_CAPTURE_DOMAIN_CAPTUE_ID);
        map.put(EVAL_CAPTURE_DOMAIN, EVAL_CAPTURE_DOMAIN_CAPTUE_ID);
        map.put(PROD_CAPTURE_DOMAIN, PROD_CAPTURE_DOMAIN_CAPTUE_ID);
        map.put(DEV_CAPTURE_DOMAIN_CHINA, DEV_CAPTURE_DOMAIN_CHINA_CAPTUE_ID);
        map.put(DEV_CAPTURE_DOMAIN_CHINA_EU, DEV_CAPTURE_DOMAIN_CHINA_EU_CAPTUE_ID);
        map.put(TEST_CAPTURE_DOMAIN_CHINA, TEST_CAPTURE_DOMAIN_CHINA_CAPTUE_ID);
        map.put(TEST_CAPTURE_DOMAIN_CHINA_EU, TEST_CAPTURE_DOMAIN_CHINA_EU_CAPTUE_ID);
        map.put(EVAL_CAPTURE_DOMAIN_CHINA, EVAL_CAPTURE_DOMAIN_CHINA_CAPTUE_ID);
        map.put(PROD_CAPTURE_DOMAIN_CHINA, PROD_CAPTURE_DOMAIN_CHINA_CAPTUE_ID);
        RLog.d(RLog.SERVICE_DISCOVERY, "Capture Domain : " + domain);
        RLog.d(RLog.SERVICE_DISCOVERY, "Capture Domain Map : " + map.get(domain));
        return map.get(domain);
    }

    public String getEngageId(String domain) {
        HashMap<String, String> map = new HashMap<>();
        map.put(DEV_CAPTURE_DOMAIN, DEV_CAPTURE_DOMAIN_ENAGAGE_ID);
        map.put(TEST_CAPTURE_DOMAIN, TEST_CAPTURE_DOMAIN_ENAGAGE_ID);
        map.put(EVAL_CAPTURE_DOMAIN, EVAL_CAPTURE_DOMAIN_ENAGAGE_ID);
        map.put(PROD_CAPTURE_DOMAIN, PROD_CAPTURE_DOMAIN_ENAGAGE_ID);
        map.put(DEV_CAPTURE_DOMAIN_CHINA, DEV_CAPTURE_DOMAIN_CHINA_ENAGAGE_ID);
        map.put(DEV_CAPTURE_DOMAIN_CHINA_EU, DEV_CAPTURE_DOMAIN_CHINA_EU_ENAGAGE_ID);
        map.put(TEST_CAPTURE_DOMAIN_CHINA, TEST_CAPTURE_DOMAIN_CHINA_ENAGAGE_ID);
        map.put(TEST_CAPTURE_DOMAIN_CHINA_EU, TEST_CAPTURE_DOMAIN_CHINA_EU_ENAGAGE_ID);
        map.put(EVAL_CAPTURE_DOMAIN_CHINA, EVAL_CAPTURE_DOMAIN_CHINA_ENAGAGE_ID);
        map.put(PROD_CAPTURE_DOMAIN_CHINA, PROD_CAPTURE_DOMAIN_CHINA_ENAGAGE_ID);
        RLog.d(RLog.SERVICE_DISCOVERY, "Engagedi Domain : " + domain);
        RLog.d(RLog.SERVICE_DISCOVERY, "Engagedi Domain Map :" + map.get(domain));

        return map.get(domain);
    }

    public String getResetPasswordClientId(String domain) {
        HashMap<String, String> map = new HashMap<>();
        map.put(DEV_CAPTURE_DOMAIN, DEV_CAPTURE_DOMAIN_RESET_PASS_CLIENT_ID);
        map.put(TEST_CAPTURE_DOMAIN, TEST_CAPTURE_DOMAIN_RESET_PASS_CLIENT_ID);
        map.put(EVAL_CAPTURE_DOMAIN, EVAL_CAPTURE_DOMAIN_RESET_PASS_CLIENT_ID);
        map.put(PROD_CAPTURE_DOMAIN, PROD_CAPTURE_DOMAIN_RESET_PASS_CLIENT_ID);
        map.put(DEV_CAPTURE_DOMAIN_CHINA, DEV_CAPTURE_DOMAIN_CHINA_RESET_PASS_CLIENT_ID);
        map.put(DEV_CAPTURE_DOMAIN_CHINA_EU, DEV_CAPTURE_DOMAIN_CHINA_EU_RESET_PASS_CLIENT_ID);
        map.put(TEST_CAPTURE_DOMAIN_CHINA, TEST_CAPTURE_DOMAIN_CHINA_RESET_PASS_CLIENT_ID);
        map.put(TEST_CAPTURE_DOMAIN_CHINA_EU, TEST_CAPTURE_DOMAIN_CHINA_EU_RESET_PASS_CLIENT_ID);
        map.put(EVAL_CAPTURE_DOMAIN_CHINA, EVAL_CAPTURE_DOMAIN_CHINA_RESET_PASS_CLIENT_ID);
        map.put(PROD_CAPTURE_DOMAIN_CHINA, PROD_CAPTURE_DOMAIN_CHINA_RESET_PASS_CLIENT_ID);
        RLog.d(RLog.SERVICE_DISCOVERY, "Engagedi Domain : " + domain);
        RLog.d(RLog.SERVICE_DISCOVERY, "Engagedi Domain Map :" + map.get(domain));
        return map.get(domain);
    }

}
