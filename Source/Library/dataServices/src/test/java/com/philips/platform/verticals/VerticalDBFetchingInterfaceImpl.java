package com.philips.platform.verticals;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.DCSync;
import com.philips.platform.core.datatypes.DSPagination;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.listeners.DBFetchRequestListner;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class VerticalDBFetchingInterfaceImpl implements DBFetchingInterface {

    @Override
    public List<? extends Moment> fetchMoments(DBFetchRequestListner dbFetchRequestListner) throws SQLException {
        return null;
    }

    @Override
    public void fetchMoments(@NonNull String type, DBFetchRequestListner dbFetchRequestListner) throws SQLException {

    }

    @Override
    public void fetchMoments(DBFetchRequestListner dbFetchRequestListner, @NonNull Object... types) throws SQLException {

    }

    @Override
    public void fetchLastMoment(String type, DBFetchRequestListner dbFetchRequestListner) throws SQLException {

    }

    @Override
    public void fetchLatestMomentByType(String type, DBFetchRequestListner<Moment> dbFetchRequestListener) throws SQLException {

    }

    @Override
    public void fetchMomentsWithTimeLine(Date startDate, Date endDate, DSPagination paginationModel, DBFetchRequestListner<Moment> dbFetchRequestListener) throws SQLException {

    }

    @Override
    public void fetchMomentsWithTypeAndTimeLine(String momentType, Date startDate, Date endDate, DSPagination paginationModel, DBFetchRequestListner<Moment> dbFetchRequestListener) throws SQLException {

    }

    @Override
    public Object fetchMomentByGuid(@NonNull String guid) throws SQLException {
        return null;
    }

    @Override
    public List<?> fetchNonSynchronizedMoments() throws SQLException {
        return null;
    }

    @Override
    public Object fetchMomentById(int id, DBFetchRequestListner dbFetchRequestListner) throws SQLException {
        return null;
    }

    @Override
    public void fetchConsentDetails(DBFetchRequestListner dbFetchRequestListner) throws SQLException {

    }

    @Override
    public Map<Class, List<?>> putUserCharacteristicsForSync(Map<Class, List<?>> dataToSync) throws SQLException {
        return null;
    }


    @Override
    public void postError(Exception e, DBFetchRequestListner dbFetchRequestListner) {

    }

    @Override
    public void fetchUserCharacteristics(DBFetchRequestListner dbFetchRequestListner) throws SQLException {

    }


    @Override
    public Settings fetchSettings(DBFetchRequestListner dbFetchRequestListner) throws SQLException {
        return null;
    }

    @Override
    public Settings fetchSettings() throws SQLException {
        return null;
    }

    @Override
    public List<?> fetchNonSyncSettings() throws SQLException {
        return null;
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
    public boolean isSynced(int tableID) throws SQLException {
        return false;
    }

    @Override
    public DCSync fetchDCSyncData(SyncType syncType) throws SQLException {
        return null;
    }

    @Override
    public List<? extends Insight> fetchActiveInsights(DBFetchRequestListner dbFetchRequestListner) throws SQLException {
        return null;
    }

    @Override
    public Insight fetchInsightByGuid(@NonNull String guid) throws SQLException {
        return null;
    }

    @Override
    public Insight fetchInsightById(int id, DBFetchRequestListner dbFetchRequestListner) throws SQLException {
        return null;
    }

    @Override
    public List<?> fetchNonSynchronizedInsights() throws SQLException {
        return null;
    }

    @Override
    public void fetchCharacteristics(DBFetchRequestListner dbFetchRequestListner) throws SQLException {

    }
}
