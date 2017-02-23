package com.philips.cdp.registration.settings.ClientIDConfiguration;

import com.philips.cdp.registration.ui.utils.RLog;

import java.util.HashMap;

/**
 * Created by 310243576 on 2/23/2017.
 */

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

    public String getCaptureId(String domain) {
        HashMap<String, String> map = new HashMap<>();
        map.put(DEV_CAPTURE_DOMAIN, "eupac7ugz25x8dwahvrbpmndf8");
        map.put(TEST_CAPTURE_DOMAIN, "x7nftvwfz8e8vcutz49p8eknqp");
        map.put(EVAL_CAPTURE_DOMAIN, "nt5dqhp6uck5mcu57snuy8uk6c");
        map.put(PROD_CAPTURE_DOMAIN, "hffxcm638rna8wrxxggx2gykhc");
        map.put(DEV_CAPTURE_DOMAIN_CHINA, "7629q5uxm2jyrbk7ehuwryj7a4");
        map.put(DEV_CAPTURE_DOMAIN_CHINA_EU, "euwkgsf83m56hqknjxgnranezh");
        map.put(TEST_CAPTURE_DOMAIN_CHINA, "hqmhwxu7jtdcye758vvxux4ryb");
        map.put(TEST_CAPTURE_DOMAIN_CHINA_EU, "vdgkb3z57jpv93mxub34x73mqu");
        map.put(EVAL_CAPTURE_DOMAIN_CHINA, "czwfzs7xh23ukmpf4fzhnksjmd");
        map.put(PROD_CAPTURE_DOMAIN_CHINA, "zkr6yg4mdsnt7f8mvucx7qkja3");
        RLog.d(RLog.SERVICE_DISCOVERY, "Capture Domain : " + domain);
        RLog.d(RLog.SERVICE_DISCOVERY, "Capture Domain Map : " + map.get(domain));
        return map.get(domain);
    }

    public String getEngageId(String domain) {
        HashMap<String, String> map = new HashMap<>();
        map.put(DEV_CAPTURE_DOMAIN, "bdbppnbjfcibijknnfkk");
        map.put(TEST_CAPTURE_DOMAIN, "fhbmobeahciagddgfidm");
        map.put(EVAL_CAPTURE_DOMAIN, "jgehpoggnhbagolnihge");
        map.put(PROD_CAPTURE_DOMAIN, "ddjbpmgpeifijdlibdio");
        map.put(DEV_CAPTURE_DOMAIN_CHINA, "ruaheighoryuoxxdwyfs");
        map.put(DEV_CAPTURE_DOMAIN_CHINA_EU, "bdbppnbjfcibijknnfkk");
        map.put(TEST_CAPTURE_DOMAIN_CHINA, "jndphelwbhuevcmovqtn");
        map.put(TEST_CAPTURE_DOMAIN_CHINA_EU, "fhbmobeahciagddgfidm");
        map.put(EVAL_CAPTURE_DOMAIN_CHINA, "uyfpympodtnesxejzuic");
        map.put(PROD_CAPTURE_DOMAIN_CHINA, "cfwaqwuwcwzlcozyyjpa");
        RLog.d(RLog.SERVICE_DISCOVERY, "Engagedi Domain : " + domain);
        RLog.d(RLog.SERVICE_DISCOVERY, "Engagedi Domain Map :" + map.get(domain));

        return map.get(domain);
    }

    public String getResetPasswordClientId(String domain) {
        HashMap<String, String> map = new HashMap<>();
        map.put(DEV_CAPTURE_DOMAIN, "rj95w5ghxqthxxy8jpug5a63wrbeykzk");
        map.put(TEST_CAPTURE_DOMAIN, "suxgtg52ej3srf683t7u5gqzw4824avg");
        map.put(EVAL_CAPTURE_DOMAIN, "h27n93rjva8xuvzgpeb7jf9jxq6dnnzr");
        map.put(PROD_CAPTURE_DOMAIN, "h27n93rjva8xuvzgpeb7jf9jxq6dnnzr");
        map.put(DEV_CAPTURE_DOMAIN_CHINA, "xhrue999syb8g2csggp9acs6x87q8q3d");
        map.put(DEV_CAPTURE_DOMAIN_CHINA_EU, "4c5tqzbneykdw2md7mkp75uycp23x3qz");
        map.put(TEST_CAPTURE_DOMAIN_CHINA, "v2s8qajf9ncfzsyy6ghjpqvsrju9xgvt");
        map.put(TEST_CAPTURE_DOMAIN_CHINA_EU, "fh5mfvjqzwhn5t9gdwqqjnbcw9atd7mv");
        map.put(EVAL_CAPTURE_DOMAIN_CHINA, "mfvjprjmgbrhfbtn6cq6q2yupzhxn977");
        map.put(PROD_CAPTURE_DOMAIN_CHINA, "65dzkyh48ux4vcguhvwsgvtk4bzyh2va");
        RLog.d(RLog.SERVICE_DISCOVERY, "Engagedi Domain : " + domain);
        RLog.d(RLog.SERVICE_DISCOVERY, "Engagedi Domain Map :" + map.get(domain));
        return map.get(domain);
    }

}
