/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/

package com.philips.platform.core.datatypes;


import java.io.Serializable;

/** Interface to filter Moments based on offset **/
public interface DSPagination extends Serializable {

	int getPageNumber();

	int getPageLimit();

	String getOrderBy();

	void setPageNumber(int pageNumber);

	void setPageLimit(int pageLimit);

	void setOrderBy(String orderBy);

	enum DSPaginationOrdering {ASCENDING, DESCENDING}

	DSPaginationOrdering getOrdering();

	void setOrdering(DSPaginationOrdering paginationOrdering);

}
