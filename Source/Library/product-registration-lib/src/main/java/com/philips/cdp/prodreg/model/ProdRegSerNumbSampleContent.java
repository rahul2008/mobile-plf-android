package com.philips.cdp.prodreg.model;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegSerNumbSampleContent {
    private String title;

    private String asset;

    private String snExample;

    private String snFormat;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public String getSnExample() {
        return snExample;
    }

    public void setSnExample(String snExample) {
        this.snExample = snExample;
    }

    public String getSnFormat() {
        return snFormat;
    }

    public void setSnFormat(String snFormat) {
        this.snFormat = snFormat;
    }

    @Override
    public String toString() {
        return "ClassPojo [title = " + title + ", asset = " + asset + ", snExample = " + snExample + ", snFormat = " + snFormat + "]";
    }
}
