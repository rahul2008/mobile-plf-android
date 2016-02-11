package com.philips.productselection.prx;

/**
 * @author naveen@philips.com on 01-Feb-16.
 */
public class ProductData {

    private String mImage = null;
    private String mProductName = null;
    private String mProductVariant = null;
    private String mExtra1;
    private String mExtra2;
    private String mExtra3;


    public String getImage() {
        return mImage;
    }

    protected void setImage(String image) {
        this.mImage = image;
    }

    public String getProductName() {
        return mProductName;
    }

    protected void setProductName(String productName) {
        this.mProductName = productName;
    }

    public String getProductVariant() {
        return mProductVariant;
    }

    protected void setProduuctVariant(String produuctVariant) {
        this.mProductVariant = produuctVariant;
    }

    public String getExtra1() {
        return mExtra1;
    }

    protected void setExtra1(String extra1) {
        this.mExtra1 = extra1;
    }

    public String getExtra2() {
        return mExtra2;
    }

    protected void setExtra2(String extra2) {
        this.mExtra2 = extra2;
    }

    public String getExtra3() {
        return mExtra3;
    }

    protected void setExtra3(String extra3) {
        this.mExtra3 = extra3;
    }
}
