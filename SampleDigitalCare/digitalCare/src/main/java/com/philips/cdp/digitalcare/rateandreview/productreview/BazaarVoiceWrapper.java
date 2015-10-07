package com.philips.cdp.digitalcare.rateandreview.productreview;


import com.bazaarvoice.BazaarEnvironment;
import com.bazaarvoice.BazaarRequest;
import com.bazaarvoice.OnBazaarResponse;
import com.bazaarvoice.SubmissionParams;
import com.bazaarvoice.types.Action;
import com.bazaarvoice.types.ApiVersion;
import com.bazaarvoice.types.RequestType;
import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.rateandreview.productreview.model.BazaarReviewModel;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * BazaarFunctions.java <br>
 * ReviewSubmissionExample<br>
 * <p/>
 * BazaarVoiceWrapper class is used as utility class. This is interface between BazaarVoice library and
 * GUI.
 * <p/>
 * This is a suite of functions that leverage the BazaarvoiceSDK. This class
 * consolidates the usage of these functions for easier understanding of how to
 * use the SDK.
 * <p/>
 *
 * @author : Ritesh.jha@philips.com
 * @since : 11 Sep 2015
 */

public class BazaarVoiceWrapper {

    private static final String TAG = BazaarVoiceWrapper.class.getSimpleName();
    private static final String API_URL_STAGING = "stg.api.bazaarvoice.com"; //Staging server
    private static String CLIENT_URL = API_URL_STAGING;
    private static final String API_URL_PRODCUTION = "api.bazaarvoice.com"; //Production Server
    private static final String API_KEY_TEST = "2cpdrhohmgmwfz8vqyo48f52g";
    private static String API_KEY = API_KEY_TEST;
    private static final ApiVersion API_VERSION = ApiVersion.FIVE_FOUR;
    private static BazaarEnvironment ENVIRONMENT = BazaarEnvironment.staging;
    private static HashMap<String, String> mKeys = new HashMap<>();

    public static void initializeKeys(BazaarEnvironment env) {
        switch (env) {
            case staging:
                initializeBazaarVoiceStagingKeys();
                break;

            case production:
                initializeBazaarVoiceProductionKeys();
                break;
        }
    }

    /*
     * Initialize all staging keys.
     */
    private static void initializeBazaarVoiceStagingKeys() {
        DigiCareLogger.d(TAG, "Initializing Staging Keys");
        mKeys.clear();
        mKeys.put("bg_BG", "7k27vg945grwmujvqyybmx4p");
        mKeys.put("cs_CZ", "txay2cps8g9zprq5g3ndebuk");
        mKeys.put("da_DK", "286cqehc83zzzs98dunf7qcm");
        mKeys.put("de_AT", "km5nr8gd7976uh2eqehay2qy");
        mKeys.put("de_CH", "zm75cj82t2ev37py9tq3uajn");
        mKeys.put("de_DE", "efzg87rtc9stkqcfeb86y4mj");
        mKeys.put("en_AU", "7zyj2zm83dz2vhhwyvn97yjq");
        mKeys.put("en_CA", "4f693d9duf9efebt6kxav7vc");
        mKeys.put("es_CO", "nnupawvnbdj3hkhuunf35re2");
        mKeys.put("en_GB", "wx9etnumcwc98jkk5cegymas");
        mKeys.put("en_ID", "tbfrpgm4arwwn6ujd6p4yjug");
        mKeys.put("en_IN", "7zenwkp83bznukam73ut6vta");
        mKeys.put("en_MY", "m2jjg69ce3tx4q5re5shfw6k");
        mKeys.put("en_NZ", "84zgwaa3tj9javm4c7nmgcjj");
        mKeys.put("en_PH", "kmu625uscnjux6w9drwn22q3");
        mKeys.put("en_SG", "6bnux276rrjzvg3wrf9ju455");
        mKeys.put("en_US", "szdfpyru9ux2bshb5w9eemy3");
        mKeys.put("es_AR", "6tppnwstaeyr2takczmffyuu");
        mKeys.put("es_CL", "ccte7gsucemsxg4mybnrxbhj");
        mKeys.put("es_ES", "zfasz6c38dn28wbtk69v58vj");
        mKeys.put("es_PA", "899xu2ka4j8krff6pps4jtbt");
        mKeys.put("es_PE", "h2ncp3fxzjfzshjedq4rfdqc");
        mKeys.put("et_EE", "a9kfbz76dtpt6q53ehpbjgmh");
        mKeys.put("fi_FI", "e6c27szspu8ebuyhzwfknzw5");
        mKeys.put("fr_FR", "w8b6ms8ftmefspdx2y5a6rvy");
        mKeys.put("fr_BE", "gkghshm29hyb2denqn5wcc3t");
        mKeys.put("fr_CA", "uwxdz4buxgaqfs7k56exdf25");
        mKeys.put("fr_CH", "8s7djmq7s2bmcfz7yv4zf44b");
        mKeys.put("it_IT", "ht4mg5ffpm2rxhtt6hxrht29");
        mKeys.put("ko_KR", "ayd5f28f2fzvhfgw4qtumqkh");
        mKeys.put("lt_LT", "szgd7y55r5cyx4akhnf9ha5q");
        mKeys.put("lv_LV", "84qce5kxvxpxhy6eepavm9rz");
        mKeys.put("nl_BE", "zefcusv9yx8saprw2a9gdga6");
        mKeys.put("nl_NL", "6paazzqbsby4s82rhsg8uhd4");
        mKeys.put("no-NO", "dvtpu7ambsz3uqrnuxwvu3um");
        mKeys.put("pl_PL", "64dxsqseunxchwphtmr4xfcr");
        mKeys.put("pt_BR", "98ezjn684geax54cs4rgrgxd");
        mKeys.put("ro_RO", "swe53pwxhkdeq6gbq69mjjec");
        mKeys.put("ru_RU", "w65h8u24nnut7hnmfdeaxkzj");
        mKeys.put("sv_SE", "r4udhywuqw3asdxhzbkszkae");
        mKeys.put("th_TH", "xcdwx7nmm5dhzmztv2g27myv");
        mKeys.put("tr_TR", "jctmhj58yng9dfrazvdvzdjk");

        DigitalCareConfigManager.getInstance().setBazaarVoiceAPIKeys(mKeys);
    }

    /*
     * Initialize all staging keys.
     */
    private static void initializeBazaarVoiceProductionKeys() {
        mKeys.clear();
        DigiCareLogger.d(TAG, "Initializing Production Keys");
        mKeys.put("bg_BG", "7k27vg945grwmujvqyybmx4p");
        mKeys.put("cs_CZ", "txay2cps8g9zprq5g3ndebuk");
        mKeys.put("da_DK", "286cqehc83zzzs98dunf7qcm");
        mKeys.put("de_AT", "km5nr8gd7976uh2eqehay2qy");
        mKeys.put("de_CH", "zm75cj82t2ev37py9tq3uajn");
        mKeys.put("de_DE", "efzg87rtc9stkqcfeb86y4mj");
        mKeys.put("en_AU", "7zyj2zm83dz2vhhwyvn97yjq");
        mKeys.put("en_CA", "4f693d9duf9efebt6kxav7vc");
        mKeys.put("es_CO", "nnupawvnbdj3hkhuunf35re2");
        mKeys.put("en_GB", "wx9etnumcwc98jkk5cegymas");
        mKeys.put("en_ID", "tbfrpgm4arwwn6ujd6p4yjug");
        mKeys.put("en_IN", "7zenwkp83bznukam73ut6vta");
        mKeys.put("en_MY", "m2jjg69ce3tx4q5re5shfw6k");
        mKeys.put("en_NZ", "84zgwaa3tj9javm4c7nmgcjj");
        mKeys.put("en_PH", "kmu625uscnjux6w9drwn22q3");
        mKeys.put("en_SG", "6bnux276rrjzvg3wrf9ju455");
        mKeys.put("en_US", "szdfpyru9ux2bshb5w9eemy3");
        mKeys.put("es_AR", "6tppnwstaeyr2takczmffyuu");
        mKeys.put("es_CL", "ccte7gsucemsxg4mybnrxbhj");
        mKeys.put("es_ES", "zfasz6c38dn28wbtk69v58vj");
        mKeys.put("es_PA", "899xu2ka4j8krff6pps4jtbt");
        mKeys.put("es_PE", "h2ncp3fxzjfzshjedq4rfdqc");
        mKeys.put("et_EE", "a9kfbz76dtpt6q53ehpbjgmh");
        mKeys.put("fi_FI", "e6c27szspu8ebuyhzwfknzw5");
        mKeys.put("fr_FR", "w8b6ms8ftmefspdx2y5a6rvy");
        mKeys.put("fr_BE", "gkghshm29hyb2denqn5wcc3t");
        mKeys.put("fr_CA", "uwxdz4buxgaqfs7k56exdf25");
        mKeys.put("fr_CH", "8s7djmq7s2bmcfz7yv4zf44b");
        mKeys.put("it_IT", "ht4mg5ffpm2rxhtt6hxrht29");
        mKeys.put("ko_KR", "ayd5f28f2fzvhfgw4qtumqkh");
        mKeys.put("lt_LT", "szgd7y55r5cyx4akhnf9ha5q");
        mKeys.put("lv_LV", "84qce5kxvxpxhy6eepavm9rz");
        mKeys.put("nl_BE", "zefcusv9yx8saprw2a9gdga6");
        mKeys.put("nl_NL", "6paazzqbsby4s82rhsg8uhd4");
        mKeys.put("no-NO", "dvtpu7ambsz3uqrnuxwvu3um");
        mKeys.put("pl_PL", "64dxsqseunxchwphtmr4xfcr");
        mKeys.put("pt_BR", "98ezjn684geax54cs4rgrgxd");
        mKeys.put("ro_RO", "swe53pwxhkdeq6gbq69mjjec");
        mKeys.put("ru_RU", "w65h8u24nnut7hnmfdeaxkzj");
        mKeys.put("sv_SE", "r4udhywuqw3asdxhzbkszkae");
        mKeys.put("th_TH", "xcdwx7nmm5dhzmztv2g27myv");
        mKeys.put("tr_TR", "jctmhj58yng9dfrazvdvzdjk");

        DigitalCareConfigManager.getInstance().setBazaarVoiceAPIKeys(mKeys);
    }

    /**
     * Submits the given review for the given product as a submission. This
     * means that it will be entered into the system and be ready for display
     * soon.
     *
     * @param prodId   the product ID
     * @param review   the full review
     * @param listener the callback function for handling the response
     */
    public static void submitReview(String prodId, BazaarReviewModel review,
                                    OnBazaarResponse listener) {
        reviewAction(prodId, review, listener, true);
    }

    /**
     * Builds a review request and sends it off as either a preview or a
     * submission.
     *
     * @param prodId   the product ID
     * @param review   the full review
     * @param listener the callback function for handling the response
     * @param submit   true to submit, false to preview
     */
    private static void reviewAction(String prodId, BazaarReviewModel review,
                                     OnBazaarResponse listener, boolean submit) {
        SubmissionParams params = new SubmissionParams();
        if (submit)
            params.setAction(Action.SUBMIT);
        else
            params.setAction(Action.PREVIEW);

        params.setProductId(prodId);
        params.setRating((int) review.getRating());
        params.setTitle(review.getSummary());
        params.setReviewText(review.getReview());
        params.setUserNickname(review.getNickname());
        params.setUserEmail(review.getEmail());

        if (!review.getEmail().equals("null"))
            params.setUserId(review.getEmail());
        else if (!(review.getNickname().equals("null") || "".equals(review.getNickname().trim())))
            params.setUserId(review.getNickname());
        else
            params.setUserId("Anonymous");

        String keyAvailable = DigitalCareConfigManager.getInstance().getBazaarVoiceKey();
        boolean isProduction = DigitalCareConfigManager.getInstance().isProductionEnvironment();

        if (isProduction && keyAvailable != null) {
            DigiCareLogger.d(TAG, "Environment is production and key is available");
            ENVIRONMENT = BazaarEnvironment.production;
            CLIENT_URL = API_URL_PRODCUTION;
        }
        API_KEY = keyAvailable;

        BazaarRequest submission = new BazaarRequest(CLIENT_URL, API_KEY, ENVIRONMENT, API_VERSION);
        submission.postSubmission(RequestType.REVIEWS, params, listener);
    }

    public static boolean isValidEmail(String email) {
        if (email == null)
            return false;
        if (email.length() == 0)
            return false;
        String emailPattern = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
