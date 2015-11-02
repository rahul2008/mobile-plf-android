package horizontal.cdp.prxcomponent;

/**
 * Created by 310190678 on 02-Nov-15.
 */
public abstract class PrxDataBuilder {

    private String mServerInfo = null;
    private String mSectorCode = null;
    private String mLocale = null;
    private String mCatalogCode = null;
    private String mCtnCode = null;

    public String getServerInfo() {
        return mServerInfo;
    }

    public void setmServerInfo(String mServerInfo) {
        this.mServerInfo = mServerInfo;
    }

    public String getSectorCode() {
        return mSectorCode;
    }

    public void setmSectorCode(String mSectorCode) {
        this.mSectorCode = mSectorCode;
    }

    public String getLocale() {
        return mLocale;
    }

    public void setmLocale(String mLocale) {
        this.mLocale = mLocale;
    }

    public String getCatalogCode() {
        return mCatalogCode;
    }

    public void setmCatalogCode(String mCatalogCode) {
        this.mCatalogCode = mCatalogCode;
    }

    public String getCtnCode() {
        return mCtnCode;
    }

    public void setmCtnCode(String mCtnCode) {
        this.mCtnCode = mCtnCode;
    }

    public abstract ResponseData getResponseData();

    public abstract String getRequestUrl();
}
