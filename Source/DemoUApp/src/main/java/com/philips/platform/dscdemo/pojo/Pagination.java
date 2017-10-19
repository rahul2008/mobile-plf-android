package com.philips.platform.dscdemo.pojo;

import com.philips.platform.core.datatypes.DSPagination;

/**
 * Created by gkavya on 10/19/17.
 */

public class Pagination implements DSPagination {

	private String orderBy;
	private int pageNumber;
	private int pageLimit;
	private DSPaginationOrdering sortOrder;

	@Override
	public String getOrderBy() {
		return null;
	}

	@Override
	public int getPageNumber() {
		return 0;
	}

	@Override
	public int getPageLimit() {
		return 0;
	}

	@Override
	public void setOrderBy(String orderBy) {

	}

	@Override
	public void setPageNumber(int pageNumber) {

	}

	@Override
	public void setPageLimit(int pageLimit) {

	}

	@Override
	public DSPaginationOrdering getOrdering() {
		return null;
	}

	@Override
	public void setOrdering(DSPaginationOrdering paginationOrdering) {

	}
}
