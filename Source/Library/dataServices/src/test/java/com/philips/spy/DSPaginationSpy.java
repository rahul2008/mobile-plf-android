package com.philips.spy;

import com.philips.platform.core.datatypes.DSPagination;


public class DSPaginationSpy implements DSPagination {

	private String orderBy;
	private int pageNumber;
	private int pageLimit;
	private DSPaginationOrdering sortOrder;

	@Override
	public int getPageNumber() {
		return pageNumber;
	}

	@Override
	public int getPageLimit() {
		return pageLimit;
	}

	@Override
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	@Override
	public void setPageLimit(int pageLimit) {
		this.pageLimit = pageLimit;
	}

	@Override
	public DSPaginationOrdering getOrdering() {
		return sortOrder;
	}

	@Override
	public void setOrdering(DSPaginationOrdering paginationOrdering) {
		sortOrder = paginationOrdering;
	}
}
