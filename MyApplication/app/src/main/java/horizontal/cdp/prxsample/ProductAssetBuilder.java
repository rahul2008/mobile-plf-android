package horizontal.cdp.prxsample;

import horizontal.cdp.prxcomponent.PrxDataBuilder;
import horizontal.cdp.prxcomponent.ResponseData;

/**
 * Created by 310190678 on 02-Nov-15.
 */
public class ProductAssetBuilder extends PrxDataBuilder{

    private String mCtn = null;
    private String mRequestTag = null;

    public ProductAssetBuilder(String ctn, String requestTag)
    {
        this.mCtn = ctn;
        this.mRequestTag = requestTag;
    }

    @Override
    public ResponseData getResponseData() {
        return null;
    }

    @Override
    public String getRequestUrl() {
        return null;
    }
}
