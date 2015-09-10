package com.philips.cdp.digitalcare.rateandreview.parser;

import com.philips.cdp.digitalcare.ConsumerProductInfo;
import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.RequestData;
import com.philips.cdp.digitalcare.ResponseCallback;
import com.philips.cdp.digitalcare.localematch.LocaleMatchHandler;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is responsible for parsing the ProductPage Url from the PRX server.
 * <p> Notable point is, The Product URL fetching is parameterized wrt Locale Specific using the
 * LocaleMatch code snippet</p>
 *
 * @author naveen@philips.com
 * @since 20/August/2015
 */
public class ProductPageParser implements ResponseCallback {

    public static String PRX_PRODUCT_URL = null;
    private final String TAG = ProductPageParser.class.getSimpleName();
    private final String PRX_JSON_DATA = "data";
    private final String PRX_JSON_SUCCESS = "success";
    private final String PRX_JSON_PRODUCTURL = "productURL";
    private final String PRX_URL = "http://%s/prx/product/%s/%s/CONSUMER/products/%s.summary";
    // private ProductPageListener mProductPageListener = null;


    public ProductPageParser() {
        DigiCareLogger.i(TAG, "ProductPageParser Constructor");
        //this.mProductPageListener = listener;
    }


    public void execute() {
        String url = getUrl();
        if (url != null) {
            new RequestData(url, this).execute();
        }
    }

    protected String getUrl() {
        String language = DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithCountryFallBack()
                .getLanguage().toLowerCase();

        String country = DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithCountryFallBack()
                .getCountry().toUpperCase();

        String locale = language + "_" + country;
        ConsumerProductInfo consumerProductInfo = DigitalCareConfigManager.getInstance().getConsumerProductInfo();


        return String.format(PRX_URL, LocaleMatchHandler.getPRXUrl(locale), consumerProductInfo.getSector(), locale,
                consumerProductInfo.getCtn());

    }


    @Override
    public void onResponseReceived(String response) {
        DigiCareLogger.i(TAG, "Json Parsed ? : " + getProductUrl(response));
        //if (mProductPageListener != null)
        PRX_PRODUCT_URL = getProductUrl(response);
        //  mProductPageListener.onPRXProductPageReceived(getProductUrl(response));
    }

    private String getProductUrl(String response) {
        if (response == null)
            return null;
        else {

            String mUrl = null;
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.has(PRX_JSON_DATA) && jsonObject.has(PRX_JSON_SUCCESS)) {
                    JSONObject mDataKey = jsonObject.getJSONObject(PRX_JSON_DATA);
                    String mProductPagePath = (mDataKey.has(PRX_JSON_PRODUCTURL)) ? mDataKey.getString(PRX_JSON_PRODUCTURL) : null;
                    mUrl = mProductPagePath;
                } else
                    mUrl = null;
            } catch (JSONException exception) {
                DigiCareLogger.e(TAG, "PRX Json parsing Exception : " + exception);
            } finally {
                return mUrl;
            }
        }
    }

}
