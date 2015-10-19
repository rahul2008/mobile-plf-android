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
        mBazaarVoiceApiKeysHashMap.put("bg_BG", "79kyglphnlxmmfb0k62xsgms1");
        mBazaarVoiceApiKeysHashMap.put("cs_CZ", "3eostk96e0y81z894gavtbm3r");
        mBazaarVoiceApiKeysHashMap.put("da_DK", "2uz77cf939urqvi1ia3b8b4x6");
        mBazaarVoiceApiKeysHashMap.put("de_AT", "akwjl5bu5gun21267e2tk8qyn");
        mBazaarVoiceApiKeysHashMap.put("de_CH", "78g0603hk1q9qnhjsl1wp1cid");
        mBazaarVoiceApiKeysHashMap.put("de_DE", "6i4a0j1u2l4ov63mdauydfltn");
        mBazaarVoiceApiKeysHashMap.put("en_AU", "7bxjh53yegkw8nb122jt899m6");
        mBazaarVoiceApiKeysHashMap.put("en_CA", "35uakw46zjt1kpz0ljnru83lm");
        mBazaarVoiceApiKeysHashMap.put("es_CO", "ap5kaq1ino9vkroxp396dq96n");
        mBazaarVoiceApiKeysHashMap.put("en_GB", "ik3dq1v3om4jgi8p86wo8tvce");
        mBazaarVoiceApiKeysHashMap.put("en_ID", "tjooqwbsibnpqutnwb83ejpvp");
        mBazaarVoiceApiKeysHashMap.put("en_IN", "75s787hudjw08pz8usvnh2x2l");
        mBazaarVoiceApiKeysHashMap.put("en_MY", "lsdk7a4oglwbk83466kltqyhm");
        mBazaarVoiceApiKeysHashMap.put("en_NZ", "d9qaydvu9a7dn73c3otn4qxbb");
        mBazaarVoiceApiKeysHashMap.put("en_PH", "77b4b1fj9a6r0btajgh3urxkj");
        mBazaarVoiceApiKeysHashMap.put("en_SG", "qdm1wscox3j7g9os2bizfu6bn");
        mBazaarVoiceApiKeysHashMap.put("en_US", "dqhezzbxtoyqhwhwklresdx2d");
        mBazaarVoiceApiKeysHashMap.put("es_AR", "perlcjm8ivoxqb2qkir7jkec4");
        mBazaarVoiceApiKeysHashMap.put("es_CL", "p1j4ug2e205gb2wdo75rdptls");
        mBazaarVoiceApiKeysHashMap.put("es_ES", "6acq9jab62kaopsver7xvk7b6");
        mBazaarVoiceApiKeysHashMap.put("es_PA", "oukplejlf4lc2hvnp1fpfbwrw");
        mBazaarVoiceApiKeysHashMap.put("es_PE", "u0ueovoeywq7r5fdj4p3wmzbs");
        mBazaarVoiceApiKeysHashMap.put("et_EE", "m4qnctyecx124v36464ridpim");
        mBazaarVoiceApiKeysHashMap.put("fi_FI", "pvi4lh9tsg6t1jxu9opge1oz2");
        mBazaarVoiceApiKeysHashMap.put("fr_FR", "pibolowazo6xuu8c0dxovofgu");
        mBazaarVoiceApiKeysHashMap.put("fr_BE", "sregyv0ys4x1cu9flx1th1sfm");
        mBazaarVoiceApiKeysHashMap.put("fr_CA", "trm136kc36ubpsg80e6okyd0a");
        mBazaarVoiceApiKeysHashMap.put("fr_CH", "toer2gu2qdryzbfj78yffmv0l");
        mBazaarVoiceApiKeysHashMap.put("it_IT", "ted5hn0ym69d0x9skw59sksc3");
        mBazaarVoiceApiKeysHashMap.put("ko_KR", "b5jqz2k64mbb23uqjeu1cadfc");
        mBazaarVoiceApiKeysHashMap.put("lt_LT", "72wuo0w4g4mpzh1th90omk3xp");
        mBazaarVoiceApiKeysHashMap.put("lv_LV", "onlsi2ejnaria9353apb10kge");
        mBazaarVoiceApiKeysHashMap.put("nl_BE", "dctijol3zzei69bmrlbdhcq6g");
        mBazaarVoiceApiKeysHashMap.put("nl_NL", "dgsjw6vam4ug27pcfb0ocp3l2");
        mBazaarVoiceApiKeysHashMap.put("no-NO", "72panbv25dzjuqn3jb8hkkldw");
        mBazaarVoiceApiKeysHashMap.put("pl_PL", "3ec776varb3lzvtkcv2cpmbh2");
        mBazaarVoiceApiKeysHashMap.put("pt_BR", "orl7xi3y20y1dprqnn7yhnnlq");
        mBazaarVoiceApiKeysHashMap.put("ro_RO", "texm9bkb32pdehtx459k2cc9j");
        mBazaarVoiceApiKeysHashMap.put("ru_RU", "lq91u0evljhubp2ctzk076yv5");
        mBazaarVoiceApiKeysHashMap.put("sv_SE", "38uhs2w5xfr7mx1mf89ub7ebc");
        mBazaarVoiceApiKeysHashMap.put("th_TH", "ao9ugx1wdjkqf1jhuiw333ore");
        mBazaarVoiceApiKeysHashMap.put("tr_TR", "e0nui2ukl14qjeh90es6q39lu");
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
