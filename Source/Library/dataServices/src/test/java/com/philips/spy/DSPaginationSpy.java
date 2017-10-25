/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.spy;

import com.philips.platform.core.datatypes.DSPagination;


public class DSPaginationSpy implements DSPagination {

	private int mPageNumber;
	private int mPageLimit;
	private DSPaginationOrdering mSortOrder;
	private String mOrderBy;

	@Override
	public int getPageNumber() {
		return mPageNumber;
	}

	@Override
	public int getPageLimit() {
		return mPageLimit;
	}

	@Override
	public String getOrderBy() {
		return mOrderBy;
	}

	@Override
	public void setPageNumber(int pageNumber) {
		this.mPageNumber = pageNumber;
	}

	@Override
	public void setPageLimit(int pageLimit) {
		this.mPageLimit = pageLimit;
	}

	@Override
	public void setOrderBy(String orderBy) {
		this.mOrderBy = orderBy;
	}

	@Override
	public DSPaginationOrdering getOrdering() {
		return mSortOrder;
	}

	@Override
	public void setOrdering(DSPaginationOrdering paginationOrdering) {
		mSortOrder = paginationOrdering;
	}
}
