

package com.philips.cdp.digitalcare.prx.subcategorymodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Data {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("domain")
    @Expose
    private String domain;
    @SerializedName("seoURL")
    @Expose
    private String seoURL;
    @SerializedName("pagePath")
    @Expose
    private String pagePath;
    @SerializedName("parentCode")
    @Expose
    private String parentCode;
    @SerializedName("parentName")
    @Expose
    private String parentName;
    @SerializedName("seoName")
    @Expose
    private String seoName;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("lowestPrice")
    @Expose
    private String lowestPrice;
    @SerializedName("noOfProducts")
    @Expose
    private String noOfProducts;
    @SerializedName("rank")
    @Expose
    private String rank;
    @SerializedName("imageURL")
    @Expose
    private String imageURL;
    @SerializedName("ctn")
    @Expose
    private String ctn;
    @SerializedName("dtn")
    @Expose
    private String dtn;
    @SerializedName("productURL")
    @Expose
    private String productURL;
    @SerializedName("productPagePath")
    @Expose
    private String productPagePath;
    @SerializedName("versions")
    @Expose
    private List<String> versions = new ArrayList<String>();
    @SerializedName("price")
    @Expose
    private Price price;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("isDeleted")
    @Expose
    private String isDeleted;

    /**
     * @return The code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code The code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * @param domain The domain
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * @return The seoURL
     */
    public String getSeoURL() {
        return seoURL;
    }

    /**
     * @param seoURL The seoURL
     */
    public void setSeoURL(String seoURL) {
        this.seoURL = seoURL;
    }

    /**
     * @return The pagePath
     */
    public String getPagePath() {
        return pagePath;
    }

    /**
     * @param pagePath The pagePath
     */
    public void setPagePath(String pagePath) {
        this.pagePath = pagePath;
    }

    /**
     * @return The parentCode
     */
    public String getParentCode() {
        return parentCode;
    }

    /**
     * @param parentCode The parentCode
     */
    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    /**
     * @return The parentName
     */
    public String getParentName() {
        return parentName;
    }

    /**
     * @param parentName The parentName
     */
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    /**
     * @return The seoName
     */
    public String getSeoName() {
        return seoName;
    }

    /**
     * @param seoName The seoName
     */
    public void setSeoName(String seoName) {
        this.seoName = seoName;
    }

    /**
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return The lowestPrice
     */
    public String getLowestPrice() {
        return lowestPrice;
    }

    /**
     * @param lowestPrice The lowestPrice
     */
    public void setLowestPrice(String lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    /**
     * @return The noOfProducts
     */
    public String getNoOfProducts() {
        return noOfProducts;
    }

    /**
     * @param noOfProducts The noOfProducts
     */
    public void setNoOfProducts(String noOfProducts) {
        this.noOfProducts = noOfProducts;
    }

    /**
     * @return The rank
     */
    public String getRank() {
        return rank;
    }

    /**
     * @param rank The rank
     */
    public void setRank(String rank) {
        this.rank = rank;
    }

    /**
     * @return The imageURL
     */
    public String getImageURL() {
        return imageURL;
    }

    /**
     * @param imageURL The imageURL
     */
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    /**
     * @return The ctn
     */
    public String getCtn() {
        return ctn;
    }

    /**
     * @param ctn The ctn
     */
    public void setCtn(String ctn) {
        this.ctn = ctn;
    }

    /**
     * @return The dtn
     */
    public String getDtn() {
        return dtn;
    }

    /**
     * @param dtn The dtn
     */
    public void setDtn(String dtn) {
        this.dtn = dtn;
    }

    /**
     * @return The productURL
     */
    public String getProductURL() {
        return productURL;
    }

    /**
     * @param productURL The productURL
     */
    public void setProductURL(String productURL) {
        this.productURL = productURL;
    }

    /**
     * @return The productPagePath
     */
    public String getProductPagePath() {
        return productPagePath;
    }

    /**
     * @param productPagePath The productPagePath
     */
    public void setProductPagePath(String productPagePath) {
        this.productPagePath = productPagePath;
    }

    /**
     * @return The versions
     */
    public List<String> getVersions() {
        return versions;
    }

    /**
     * @param versions The versions
     */
    public void setVersions(List<String> versions) {
        this.versions = versions;
    }

    /**
     * @return The price
     */
    public Price getPrice() {
        return price;
    }

    /**
     * @param price The price
     */
    public void setPrice(Price price) {
        this.price = price;
    }

    /**
     * @return The type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return The createdDate
     */
    public String getCreatedDate() {
        return createdDate;
    }

    /**
     * @param createdDate The createdDate
     */
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return The isDeleted
     */
    public String getIsDeleted() {
        return isDeleted;
    }

    /**
     * @param isDeleted The isDeleted
     */
    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

}