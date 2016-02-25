package com.philips.cdp.digitalcare;

/**
 * <p> It is the abstract Product information configurable class.</p>
 * <p> This is the mandotory class, must used by the integrating application to pass the object of {@link ConsumerProductInfo}
 * object during initialization of the DigitalCare component.</p>
 * <p>Hint:  For reference please glance at the demo sample</p>
 */
public class ConsumerProductInfo {
    private static String mGroup = "PERSONAL_CARE_GR";
    private static String mSector = "B2C";
    private static String mCatalog = "CARE";
    private static String mCategory = "HAIRCARE_CA";
    private static String mSubCategory = "HAIR_STYLERS_SU";
    private static String mCtn = "RQ1250/17";
    private static String mProductTitle = "HairStyler";


    public String getGroup() {
        return mGroup;
    }

    public static void setGroup(String group) {
        mGroup = group;
    }

    public String getSector() {
        return mSector;
    }

    public static void setSector(String sector) {
        mSector = sector;
    }

    public String getCatalog() {
        return mCatalog;
    }

    public static void setCatalog(String catalog) {
        mCatalog = catalog;
    }

    public String getCategory() {
        return mCategory;
    }

    public static void setCategory(String category) {
        mCategory = category;
    }

    public String getSubCategory() {
        return mSubCategory;
    }

    public static void setSubCategory(String subCategory) {
        mSubCategory = subCategory;
    }

    public String getCtn() {
        return mCtn;
    }

    public static void setCtn(String ctn) {
        mCtn = ctn;
    }

    public String getProductTitle() {
        return mProductTitle;
    }

    public static void setProductTitle(String productTitle) {
        mProductTitle = productTitle;
    }
}
