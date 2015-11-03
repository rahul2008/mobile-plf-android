package horizontal.cdp.prxcomponent;

import android.content.Context;

import com.cdp.prx.network.NetworkWrapper;

import horizontal.cdp.prxcomponent.listeners.ResponseHandler;
import horizontal.cdp.prxcomponent.listeners.ResponseListener;

/**
 * Description : This is the entry class to start the PRX Request.
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 02-Nov-15.
 */
public class RequestManager {

    private Context mContext = null;
    private ResponseHandler mResponseHandler = null;

    public void init(Context applicationContext) {
        mContext = applicationContext;
    }

    public void executeRequest(PrxDataBuilder prxDataBuilder,ResponseListener responseListener) {
        new NetworkWrapper(mContext, prxDataBuilder, responseListener).executeJsonObjectRequest();
    }

    public void cancelRequest(String requestTag) {}
}
