package horizontal.cdp.prxcomponent;

import android.util.Log;

import com.cdp.prx.assets.AssetModel;
import com.cdp.prx.databuilder.ProductAssetBuilder;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by 310190678 on 02-Nov-15.
 */
public class ResponseData {

    private static final String TAG = ResponseData.class.getSimpleName();

    private PrxDataBuilder mPrxDataBuilder = null;
    private Object mModelClass = null;
    private JSONObject mJsonObject = null;
    private Gson mGson = null;
    private AssetModel mAssetModel = null;

    public ResponseData(JSONObject jsonObject, PrxDataBuilder dataBuilder) {
        this.mJsonObject = jsonObject;
        this.mPrxDataBuilder = dataBuilder;
        mGson = new Gson();
    }


    public void init()
    {
        getAssetModel();
    }

    public AssetModel getAssetModel() {
        if (mPrxDataBuilder instanceof ProductAssetBuilder) {
            Log.d(TAG, "It is Product Asset");
            mAssetModel = mGson.fromJson(mJsonObject.toString(), AssetModel.class);
            return (AssetModel) mModelClass;
        }
        return null;
    }

}
