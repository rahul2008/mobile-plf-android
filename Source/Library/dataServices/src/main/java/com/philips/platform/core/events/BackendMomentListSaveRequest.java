/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.core.events;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.listeners.DBChangeListener;

import java.util.List;

public class BackendMomentListSaveRequest extends ListEvent<Moment> {

    private final DBChangeListener dbChangeListener;
    private final List<Moment> mMomentList;

    public BackendMomentListSaveRequest(@NonNull final List<Moment> dataList, DBChangeListener dbChangeListener) {
        super(dataList);
        this.dbChangeListener = dbChangeListener;
        mMomentList = dataList;
    }

    public DBChangeListener getDbChangeListener() {
        return dbChangeListener;
    }

    public List<Moment> getMomentList() {
        return mMomentList;
    }
}
