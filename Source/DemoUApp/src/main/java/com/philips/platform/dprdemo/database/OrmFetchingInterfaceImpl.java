/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.dprdemo.database;

import android.support.annotation.NonNull;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.DCSync;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.datatypes.SynchronisationData;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.dprdemo.database.datatypes.MomentType;
import com.philips.platform.dprdemo.database.table.OrmConsentDetail;
import com.philips.platform.dprdemo.database.table.OrmDCSync;
import com.philips.platform.dprdemo.utils.NotifyDBRequestListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class OrmFetchingInterfaceImpl implements DBFetchingInterface {

    static final String SYNCED_FIELD = "synced";


    NotifyDBRequestListener notifyDBRequestListener;

    private final Dao<OrmConsentDetail, Integer> consentDetailsDao;
    private final Dao<OrmDCSync, Integer> ormDCSyncDao;

    public OrmFetchingInterfaceImpl(Dao<OrmConsentDetail, Integer> consentDetailsDao,  Dao<OrmDCSync, Integer> ormDCSyncDao) {

        this.consentDetailsDao = consentDetailsDao;
        this.ormDCSyncDao = ormDCSyncDao;
        notifyDBRequestListener = new NotifyDBRequestListener();
    }

    @Override
    public List<? extends Moment> fetchMoments(DBFetchRequestListner<Moment> dbFetchRequestListner) throws SQLException {
       return null;
    }

    @Override
    public void fetchConsentDetails(DBFetchRequestListner<ConsentDetail> dbFetchRequestListner) throws SQLException {

        QueryBuilder<OrmConsentDetail, Integer> queryBuilder = consentDetailsDao.queryBuilder();
        ArrayList<OrmConsentDetail> ormConsents = (ArrayList<OrmConsentDetail>) consentDetailsDao.query(queryBuilder.prepare());
        if (ormConsents != null && !ormConsents.isEmpty()) {
            notifyDBRequestListener.notifyConsentFetchSuccess(dbFetchRequestListner, ormConsents);
        }
    }

    @Override
    public List<?> fetchNonSyncConsentDetails() throws SQLException {

        List<OrmConsentDetail> ormConsents = new ArrayList<>();
        if (!isSynced(SyncType.CONSENT.getId())) {
            QueryBuilder<OrmConsentDetail, Integer> queryBuilder = consentDetailsDao.queryBuilder();
            ormConsents = consentDetailsDao.query(queryBuilder.prepare());
            return ormConsents;
        }
        return ormConsents;
    }

    @Override
    public List<?> fetchConsentDetails() throws SQLException {
        QueryBuilder<OrmConsentDetail, Integer> queryBuilder = consentDetailsDao.queryBuilder();
        List<OrmConsentDetail> ormConsents = consentDetailsDao.query(queryBuilder.prepare());
        return ormConsents;
    }

    @Override
    public void fetchUserCharacteristics(DBFetchRequestListner<Characteristics> dbFetchRequestListner) throws SQLException {

    }

    @Override
    public void fetchCharacteristics(DBFetchRequestListner<Characteristics> dbFetchRequestListner) throws SQLException {

    }

    @Override
    public void postError(Exception e, DBFetchRequestListner dbFetchRequestListner) {
        dbFetchRequestListner.onFetchFailure(e);
    }

    @Override
    public void fetchMoments(@NonNull final String type, DBFetchRequestListner<Moment> dbFetchRequestListner) throws SQLException {

    }

    @Override
    public void fetchMoments(DBFetchRequestListner<Moment> dbFetchRequestListner, @NonNull final Object... types) throws SQLException {

    }

    @Override
    public void fetchLastMoment(final String type, DBFetchRequestListner<Moment> dbFetchRequestListner) throws SQLException {

    }

    @Override
    public Object fetchMomentByGuid(@NonNull final String guid) throws SQLException {
        return null;
    }

    @NonNull
    private QueryBuilder<SynchronisationData, Integer> getSyncQueryBuilderWithGuidFilter(final @NonNull String guid) throws SQLException {
        return null;
    }

    @Override
    public List<?> fetchNonSynchronizedMoments() throws SQLException {
        return null;
    }

    @Override
    public Object fetchMomentById(final int id, DBFetchRequestListner<Moment> dbFetchRequestListner) throws SQLException {
        return null;
    }

    @Override
    public Map<Class, List<?>> putUserCharacteristicsForSync(Map<Class, List<?>> dataToSync) throws SQLException {
        return null;
    }

    private List<? extends Characteristics> fetchNonSynchronizedCharacteristics() throws SQLException {

        return null;
    }

    @Override
    public Settings fetchSettings(DBFetchRequestListner<Settings> dbFetchRequestListner) throws SQLException {
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
    public boolean isSynced(int tableID) throws SQLException {
        QueryBuilder<OrmDCSync, Integer> lDCSyncQueryBuilder = ormDCSyncDao.queryBuilder();
        lDCSyncQueryBuilder.where().eq("tableID", tableID);
        OrmDCSync ormDCSync = lDCSyncQueryBuilder.queryForFirst();
        if (ormDCSync == null)
            return true;
        return ormDCSync.isSynced();
    }

    @Override
    public DCSync fetchDCSyncData(SyncType syncType) throws SQLException {
        QueryBuilder<OrmDCSync, Integer> lDCSyncQueryBuilder = ormDCSyncDao.queryBuilder();
        lDCSyncQueryBuilder.where().eq("tableID", syncType.getId());
        OrmDCSync ormDCSync = lDCSyncQueryBuilder.queryForFirst();
        return ormDCSync;
    }

    //Insights
    @Override
    public List<? extends Insight> fetchActiveInsights(DBFetchRequestListner<Insight> dbFetchRequestListner) throws SQLException {
        return null;
    }

    @Override
    public Insight fetchInsightByGuid(@NonNull String guid) throws SQLException {
        return null;
    }

    @Override
    public Insight fetchInsightById(int id, DBFetchRequestListner<Insight> dbFetchRequestListner) throws SQLException {
        return null;
    }

    @Override
    public List<?> fetchNonSynchronizedInsights() throws SQLException {
        return null;
    }
}
