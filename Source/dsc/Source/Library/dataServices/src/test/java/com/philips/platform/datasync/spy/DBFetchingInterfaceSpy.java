package com.philips.platform.datasync.spy;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.DCSync;
import com.philips.platform.core.datatypes.DSPagination;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.listeners.DBFetchRequestListner;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DBFetchingInterfaceSpy implements DBFetchingInterface {
    public List<?> settingsToSync;

    @Override
    public List<? extends Moment> fetchMoments(final DBFetchRequestListner<Moment> dbFetchRequestListner) throws SQLException {
        return null;
    }

    @Override
    public void fetchMoments(@NonNull final String type, final DBFetchRequestListner<Moment> dbFetchRequestListner) throws SQLException {

    }

    @Override
    public void fetchMoments(final DBFetchRequestListner<Moment> dbFetchRequestListner, @NonNull final Object... types) throws SQLException {

    }

    @Override
    public void fetchLastMoment(final String type, final DBFetchRequestListner<Moment> dbFetchRequestListner) throws SQLException {

    }

    @Override
    public void fetchLatestMomentByType(final String type, final DBFetchRequestListner<Moment> dbFetchRequestListener) throws SQLException {

    }

    @Override
    public void fetchMomentsWithTimeLine(final Date startDate, final Date endDate, final DSPagination paginationModel, final DBFetchRequestListner<Moment> dbFetchRequestListener) throws SQLException {

    }

    @Override
    public void fetchMomentsWithTypeAndTimeLine(final String momentType, final Date startDate, final Date endDate, final DSPagination paginationModel, final DBFetchRequestListner<Moment> dbFetchRequestListener) throws SQLException {

    }

    @Override
    public Object fetchMomentByGuid(@NonNull final String guid) throws SQLException {
        return null;
    }

    @Override
    public List<?> fetchNonSynchronizedMoments() throws SQLException {
        return null;
    }

    @Override
    public Object fetchMomentById(final int id, final DBFetchRequestListner<Moment> dbFetchRequestListner) throws SQLException {
        return null;
    }

    @Override
    public void fetchConsentDetails(final DBFetchRequestListner<ConsentDetail> dbFetchRequestListner) throws SQLException {

    }

    @Override
    public List<?> fetchNonSyncConsentDetails() throws SQLException {
        return null;
    }

    @Override
    public List<?> fetchConsentDetails() throws SQLException {
        return null;
    }

    @Override
    public void fetchUserCharacteristics(final DBFetchRequestListner<Characteristics> dbFetchRequestListner) throws SQLException {

    }

    @Override
    public Map<Class, List<?>> putUserCharacteristicsForSync(final Map<Class, List<?>> dataToSync) throws SQLException {
        return null;
    }

    @Override
    public void fetchCharacteristics(final DBFetchRequestListner<Characteristics> dbFetchRequestListner) throws SQLException {

    }

    @Override
    public com.philips.platform.core.datatypes.Settings fetchSettings(final DBFetchRequestListner<com.philips.platform.core.datatypes.Settings> dbFetchRequestListner) throws SQLException {
        return null;
    }

    @Override
    public com.philips.platform.core.datatypes.Settings fetchSettings() throws SQLException {
        return null;
    }

    @Override
    public List<?> fetchNonSyncSettings() throws SQLException {
        return settingsToSync;
    }

    @Override
    public boolean isSynced(final int tableID) throws SQLException {
        return false;
    }

    @Override
    public DCSync fetchDCSyncData(final SyncType syncType) throws SQLException {
        return null;
    }

    @Override
    public List<? extends Insight> fetchActiveInsights(final DBFetchRequestListner<Insight> dbFetchRequestListner) throws SQLException {
        return null;
    }

    @Override
    public Insight fetchInsightByGuid(@NonNull final String guid) throws SQLException {
        return null;
    }

    @Override
    public Insight fetchInsightById(final int id, final DBFetchRequestListner<Insight> dbFetchRequestListner) throws SQLException {
        return null;
    }

    @Override
    public List<?> fetchNonSynchronizedInsights() throws SQLException {
        return null;
    }

    @Override
    public void postError(final Exception e, final DBFetchRequestListner dbFetchRequestListner) {

    }
}
