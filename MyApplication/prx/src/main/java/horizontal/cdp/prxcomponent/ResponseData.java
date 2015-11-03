package horizontal.cdp.prxcomponent;

import android.util.Log;

import com.cdp.prx.assets.AssetModel;
import com.cdp.prx.databuilder.ProductAssetBuilder;
import com.cdp.prx.databuilder.ProductSummaryBuilder;
import com.cdp.prx.summary.SummaryModel;
import com.google.gson.Gson;

import org.json.JSONObject;

import horizontal.cdp.prxcomponent.listeners.ResponseListener;

/**
 * Description : This class provides the URL
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 02-Nov-15.
 */

public abstract class ResponseData {
    public  abstract ResponseData parseJsonResponseData(JSONObject response);
}
