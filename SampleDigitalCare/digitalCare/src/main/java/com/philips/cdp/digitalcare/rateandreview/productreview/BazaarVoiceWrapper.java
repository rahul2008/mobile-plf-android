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
import java.util.Locale;
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
    private static final String API_URL_PRODCUTION = "api.bazaarvoice.com"; //Production Server
    private static String CLIENT_URL = API_URL_STAGING;
    private static String API_KEY;
    private static final ApiVersion API_VERSION = ApiVersion.FIVE_FOUR;
    private static BazaarEnvironment ENVIRONMENT = BazaarEnvironment.staging;
    private static HashMap<String, String> mBazaarVoiceApiKeysHashMap = new HashMap<String, String>();

    public void initializeKeys() {
       if(DigitalCareConfigManager.getInstance().isProductionEnvironment()) {
           initializeBazaarVoiceProductionKeys();
       }else{
           initializeBazaarVoiceStagingKeys();
        }
    }

    /*
     * Initialize all staging keys.
     */
    private void initializeBazaarVoiceStagingKeys() {
        DigiCareLogger.d(TAG, "Initializing Staging Keys");
        mBazaarVoiceApiKeysHashMap.clear();
        mBazaarVoiceApiKeysHashMap.put("bg_BG", "7k27vg945grwmujvqyybmx4p");
        mBazaarVoiceApiKeysHashMap.put("cs_CZ", "txay2cps8g9zprq5g3ndebuk");
        mBazaarVoiceApiKeysHashMap.put("da_DK", "286cqehc83zzzs98dunf7qcm");
        mBazaarVoiceApiKeysHashMap.put("de_AT", "km5nr8gd7976uh2eqehay2qy");
        mBazaarVoiceApiKeysHashMap.put("de_CH", "zm75cj82t2ev37py9tq3uajn");
        mBazaarVoiceApiKeysHashMap.put("de_DE", "efzg87rtc9stkqcfeb86y4mj");
        mBazaarVoiceApiKeysHashMap.put("en_AU", "7zyj2zm83dz2vhhwyvn97yjq");
        mBazaarVoiceApiKeysHashMap.put("en_CA", "4f693d9duf9efebt6kxav7vc");
        mBazaarVoiceApiKeysHashMap.put("es_CO", "nnupawvnbdj3hkhuunf35re2");
        mBazaarVoiceApiKeysHashMap.put("en_GB", "wx9etnumcwc98jkk5cegymas");
        mBazaarVoiceApiKeysHashMap.put("en_ID", "tbfrpgm4arwwn6ujd6p4yjug");
        mBazaarVoiceApiKeysHashMap.put("en_IN", "7zenwkp83bznukam73ut6vta");
        mBazaarVoiceApiKeysHashMap.put("en_MY", "m2jjg69ce3tx4q5re5shfw6k");
        mBazaarVoiceApiKeysHashMap.put("en_NZ", "84zgwaa3tj9javm4c7nmgcjj");
        mBazaarVoiceApiKeysHashMap.put("en_PH", "kmu625uscnjux6w9drwn22q3");
        mBazaarVoiceApiKeysHashMap.put("en_SG", "6bnux276rrjzvg3wrf9ju455");
        mBazaarVoiceApiKeysHashMap.put("en_US", "szdfpyru9ux2bshb5w9eemy3");
        mBazaarVoiceApiKeysHashMap.put("es_AR", "6tppnwstaeyr2takczmffyuu");
        mBazaarVoiceApiKeysHashMap.put("es_CL", "ccte7gsucemsxg4mybnrxbhj");
        mBazaarVoiceApiKeysHashMap.put("es_ES", "zfasz6c38dn28wbtk69v58vj");
        mBazaarVoiceApiKeysHashMap.put("es_PA", "899xu2ka4j8krff6pps4jtbt");
        mBazaarVoiceApiKeysHashMap.put("es_PE", "h2ncp3fxzjfzshjedq4rfdqc");
        mBazaarVoiceApiKeysHashMap.put("et_EE", "a9kfbz76dtpt6q53ehpbjgmh");
        mBazaarVoiceApiKeysHashMap.put("fi_FI", "e6c27szspu8ebuyhzwfknzw5");
        mBazaarVoiceApiKeysHashMap.put("fr_FR", "w8b6ms8ftmefspdx2y5a6rvy");
        mBazaarVoiceApiKeysHashMap.put("fr_BE", "gkghshm29hyb2denqn5wcc3t");
        mBazaarVoiceApiKeysHashMap.put("fr_CA", "uwxdz4buxgaqfs7k56exdf25");
        mBazaarVoiceApiKeysHashMap.put("fr_CH", "8s7djmq7s2bmcfz7yv4zf44b");
        mBazaarVoiceApiKeysHashMap.put("it_IT", "ht4mg5ffpm2rxhtt6hxrht29");
        mBazaarVoiceApiKeysHashMap.put("ko_KR", "ayd5f28f2fzvhfgw4qtumqkh");
        mBazaarVoiceApiKeysHashMap.put("lt_LT", "szgd7y55r5cyx4akhnf9ha5q");
        mBazaarVoiceApiKeysHashMap.put("lv_LV", "84qce5kxvxpxhy6eepavm9rz");
        mBazaarVoiceApiKeysHashMap.put("nl_BE", "zefcusv9yx8saprw2a9gdga6");
        mBazaarVoiceApiKeysHashMap.put("nl_NL", "6paazzqbsby4s82rhsg8uhd4");
        mBazaarVoiceApiKeysHashMap.put("no-NO", "dvtpu7ambsz3uqrnuxwvu3um");
        mBazaarVoiceApiKeysHashMap.put("pl_PL", "64dxsqseunxchwphtmr4xfcr");
        mBazaarVoiceApiKeysHashMap.put("pt_BR", "98ezjn684geax54cs4rgrgxd");
        mBazaarVoiceApiKeysHashMap.put("ro_RO", "swe53pwxhkdeq6gbq69mjjec");
        mBazaarVoiceApiKeysHashMap.put("ru_RU", "w65h8u24nnut7hnmfdeaxkzj");
        mBazaarVoiceApiKeysHashMap.put("sv_SE", "r4udhywuqw3asdxhzbkszkae");
        mBazaarVoiceApiKeysHashMap.put("th_TH", "xcdwx7nmm5dhzmztv2g27myv");
        mBazaarVoiceApiKeysHashMap.put("tr_TR", "jctmhj58yng9dfrazvdvzdjk");

    }

    /*
     * Initialize all staging keys.
     */
    private void initializeBazaarVoiceProductionKeys() {
        mBazaarVoiceApiKeysHashMap.clear();
        DigiCareLogger.d(TAG, "Initializing Production Keys");
        mBazaarVoiceApiKeysHashMap.put("bg_BG", "");
        mBazaarVoiceApiKeysHashMap.put("cs_CZ", "");
        mBazaarVoiceApiKeysHashMap.put("da_DK", "");
        mBazaarVoiceApiKeysHashMap.put("de_AT", "");
        mBazaarVoiceApiKeysHashMap.put("de_CH", "");
        mBazaarVoiceApiKeysHashMap.put("de_DE", "");
        mBazaarVoiceApiKeysHashMap.put("en_AU", "");
        mBazaarVoiceApiKeysHashMap.put("en_CA", "");
        mBazaarVoiceApiKeysHashMap.put("es_CO", "");
        mBazaarVoiceApiKeysHashMap.put("en_GB", "");
        mBazaarVoiceApiKeysHashMap.put("en_ID", "");
        mBazaarVoiceApiKeysHashMap.put("en_IN", "");
        mBazaarVoiceApiKeysHashMap.put("en_MY", "");
        mBazaarVoiceApiKeysHashMap.put("en_NZ", "");
        mBazaarVoiceApiKeysHashMap.put("en_PH", "");
        mBazaarVoiceApiKeysHashMap.put("en_SG", "");
        mBazaarVoiceApiKeysHashMap.put("en_US", "");
        mBazaarVoiceApiKeysHashMap.put("es_AR", "");
        mBazaarVoiceApiKeysHashMap.put("es_CL", "");
        mBazaarVoiceApiKeysHashMap.put("es_ES", "");
        mBazaarVoiceApiKeysHashMap.put("es_PA", "");
        mBazaarVoiceApiKeysHashMap.put("es_PE", "");
        mBazaarVoiceApiKeysHashMap.put("et_EE", "");
        mBazaarVoiceApiKeysHashMap.put("fi_FI", "");
        mBazaarVoiceApiKeysHashMap.put("fr_FR", "");
        mBazaarVoiceApiKeysHashMap.put("fr_BE", "");
        mBazaarVoiceApiKeysHashMap.put("fr_CA", "");
        mBazaarVoiceApiKeysHashMap.put("fr_CH", "");
        mBazaarVoiceApiKeysHashMap.put("it_IT", "");
        mBazaarVoiceApiKeysHashMap.put("ko_KR", "");
        mBazaarVoiceApiKeysHashMap.put("lt_LT", "");
        mBazaarVoiceApiKeysHashMap.put("lv_LV", "");
        mBazaarVoiceApiKeysHashMap.put("nl_BE", "");
        mBazaarVoiceApiKeysHashMap.put("nl_NL", "");
        mBazaarVoiceApiKeysHashMap.put("no-NO", "");
        mBazaarVoiceApiKeysHashMap.put("pl_PL", "");
        mBazaarVoiceApiKeysHashMap.put("pt_BR", "");
        mBazaarVoiceApiKeysHashMap.put("ro_RO", "");
        mBazaarVoiceApiKeysHashMap.put("ru_RU", "");
        mBazaarVoiceApiKeysHashMap.put("sv_SE", "");
        mBazaarVoiceApiKeysHashMap.put("th_TH", "");
        mBazaarVoiceApiKeysHashMap.put("tr_TR", "");

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
    public void submitReview(String prodId, BazaarReviewModel review,
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
    private void reviewAction(String prodId, BazaarReviewModel review,
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
        params.setAgreedToTermsAndConditions(review.getTermsAndConditionStatus());
        params.setUserId(review.getEmail());

        boolean isProduction = DigitalCareConfigManager.getInstance().isProductionEnvironment();

        if (isProduction && getBazaarVoiceKey() != null) {
            DigiCareLogger.d(TAG, "Environment is production and key is available");
            ENVIRONMENT = BazaarEnvironment.production;
            CLIENT_URL = API_URL_PRODCUTION;
        }
        API_KEY = getBazaarVoiceKey();

        BazaarRequest submission = new BazaarRequest(CLIENT_URL, API_KEY, ENVIRONMENT, API_VERSION);
        submission.postSubmission(RequestType.REVIEWS, params, listener);
    }

    public boolean isValidEmail(String email) {
        if (email == null)
            return false;
        if (email.length() == 0)
            return false;
        String emailPattern = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public String getBazaarVoiceKey(){
        Locale locale = DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithCountryFallBack();
        if(locale != null) {
            String localeValue = locale.toString();
            boolean keyAvailable = mBazaarVoiceApiKeysHashMap.containsKey(localeValue);
            if(keyAvailable){
                return mBazaarVoiceApiKeysHashMap.get(localeValue);
            }
        }
        return null;
    }
}
