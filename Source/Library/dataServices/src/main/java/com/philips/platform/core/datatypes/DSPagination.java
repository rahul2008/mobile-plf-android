package com.philips.platform.core.datatypes;


import java.io.Serializable;

public interface DSPagination extends Serializable {

	String getOrderBy();

	int getPageNumber();

	int getPageLimit();

	void setOrderBy(String orderBy);

	void setPageNumber(int pageNumber);

	void setPageLimit(int pageLimit);

	enum DSPaginationOrdering {DSASCEDING, DSDESCENDING}

	DSPaginationOrdering getOrdering();

	void setOrdering(DSPaginationOrdering paginationOrdering);

}
