/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.core.events;


import com.philips.platform.core.datatypes.DSPagination;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.listeners.DBFetchRequestListner;

import java.util.Date;

public class LoadMomentsByDate extends Event {

	private Date mStartDate;
	private Date mEndDate;
	private String mMomentType;
	private final DBFetchRequestListner<Moment> mDbFetchRequestListener;
	private DSPagination mPaginationModel;

	public LoadMomentsByDate(Date startDate, Date endDate,DSPagination paginationModel, DBFetchRequestListner<Moment> dbFetchRequestListener) {
		this.mStartDate = startDate;
		this.mEndDate = endDate;
		this.mPaginationModel = paginationModel;
		this.mDbFetchRequestListener = dbFetchRequestListener;
	}

	public LoadMomentsByDate(String momentType, Date startDate, Date endDate,DSPagination paginationModel,DBFetchRequestListner<Moment> dbFetchRequestListener) {
		this.mStartDate = startDate;
		this.mEndDate = endDate;
		this.mMomentType = momentType;
		this.mPaginationModel = paginationModel;
		this.mDbFetchRequestListener = dbFetchRequestListener;
	}

	public Date getStartDate() {
		return mStartDate;
	}

	public Date getEndDate() {
		return mEndDate;
	}

	public String getMomentType() {
		return mMomentType;
	}

	public DSPagination getPaginationModel() {
		return mPaginationModel;
	}

	public DBFetchRequestListner<Moment> getDbFetchRequestListner() {
		return mDbFetchRequestListener;
	}
}
