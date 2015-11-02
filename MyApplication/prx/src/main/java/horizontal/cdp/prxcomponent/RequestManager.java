package horizontal.cdp.prxcomponent;

import android.content.Context;

/**
 * Created by 310190678 on 02-Nov-15.
 */
public class RequestManager {

    private Context mContext = null;

    public void init(Context applicationContext)
    {
        mContext = applicationContext;
    }

    public void executeRequest(PrxDataBuilder prxDataBuilder, ResponseListener responseListener)
    {

    }


    public void cancelRequest(String requestTag)
    {

    }
}
