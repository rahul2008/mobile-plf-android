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
public class ResponseData {

    private static final String TAG = ResponseData.class.getSimpleName();

    private PrxDataBuilder mPrxDataBuilder = null;
    private ResponseListener mListener = null;
    private JSONObject mJsonObject = null;
    private Gson mGson = null;
    private AssetModel mAssetModel = null;
    private SummaryModel mSummaryModel = null;

    public ResponseData(JSONObject jsonObject, PrxDataBuilder dataBuilder, ResponseListener listener) {
        this.mJsonObject = jsonObject;
        this.mPrxDataBuilder = dataBuilder;
        this.mListener = listener;
        mGson = new Gson();
    }


    public void init()
    {
        getDataModel();
        mListener.onResponseSuccess(this);
    }

    public Object getDataModel() {
        if (mPrxDataBuilder instanceof ProductAssetBuilder) {
            Log.d(TAG, "It is Product Asset");
            mAssetModel = mGson.fromJson(mJsonObject.toString(), AssetModel.class);
            return mAssetModel;
        }else if(mPrxDataBuilder instanceof ProductSummaryBuilder)
        {
            mSummaryModel = mGson.fromJson(mJsonObject.toString(), SummaryModel.class);
            return  mSummaryModel;
        }
        return null;
    }

}
