/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.ecs.model.products;

import com.philips.cdp.di.ecs.model.summary.ECSProductSummary;

import java.util.List;

public class ECSProducts {
    private String type;
    private CurrentQueryEntity currentQuery;
    private String freeTextSearch;
    private PaginationEntity pagination;
    private List<ECSProduct> products;
    private List<SortsEntity> sorts;
    private ECSProductSummary ecsProductSummary;

    public String getType() {
        return type;
    }

    public CurrentQueryEntity getCurrentQuery() {
        return currentQuery;
    }

    public String getFreeTextSearch() {
        return freeTextSearch;
    }

    public PaginationEntity getPagination() {
        return pagination;
    }

    public List<ECSProduct> getProducts() {
        return products;
    }

    public void setProducts(List<ECSProduct> products) {
        this.products = products;
    }

    public List<SortsEntity> getSorts() {
        return sorts;
    }

    public ECSProductSummary getEcsProductSummary() {
        return ecsProductSummary;
    }

    public void setEcsProductSummary(ECSProductSummary ecsProductSummary) {
        this.ecsProductSummary = ecsProductSummary;
    }
}