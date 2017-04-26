/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.baseapp.screens.dataservices.database;

import android.support.annotation.NonNull;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.philips.platform.baseapp.screens.dataservices.database.datatypes.MomentType;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmCharacteristics;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmConsentDetail;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmDCSync;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmInsight;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMoment;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmSettings;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmSynchronisationData;
import com.philips.platform.baseapp.screens.dataservices.temperature.TemperatureMomentHelper;
import com.philips.platform.baseapp.screens.dataservices.utility.NotifyDBRequestListener;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.DCSync;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.utils.DSLog;

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


    @NonNull
    private Dao<OrmMoment, Integer> momentDao;

    NotifyDBRequestListener notifyDBRequestListener;


    @NonNull
    private Dao<OrmSynchronisationData, Integer> synchronisationDataDao;
    private final Dao<OrmConsentDetail, Integer> consentDetailsDao;
    private final Dao<OrmSettings, Integer> settingsDao;

    private final Dao<OrmCharacteristics, Integer> characteristicsDao;

    private final Dao<OrmDCSync, Integer> ormDCSyncDao;

    private final Dao<OrmInsight, Integer> ormInsightDao;


    private TemperatureMomentHelper mTemperatureMomentHelper;


    public OrmFetchingInterfaceImpl(final @NonNull Dao<OrmMoment, Integer> momentDao,
                                    final @NonNull Dao<OrmSynchronisationData, Integer> synchronisationDataDao,
                                    Dao<OrmConsentDetail, Integer> consentDetailsDao, Dao<OrmCharacteristics, Integer> characteristicsDao,
                                    Dao<OrmSettings, Integer> settingsDao, Dao<OrmDCSync, Integer> ormDCSyncDao, Dao<OrmInsight, Integer> ormInsightDao) {

        this.momentDao = momentDao;
        this.synchronisationDataDao = synchronisationDataDao;
        this.characteristicsDao = characteristicsDao;
        this.consentDetailsDao = consentDetailsDao;
        this.settingsDao = settingsDao;
        this.ormDCSyncDao = ormDCSyncDao;
        this.ormInsightDao = ormInsightDao;

        notifyDBRequestListener = new NotifyDBRequestListener();
    }

    @Override
    public List<? extends Moment> fetchMoments(DBFetchRequestListner<Moment> dbFetchRequestListner) throws SQLException {
        QueryBuilder<OrmMoment, Integer> queryBuilder = momentDao.queryBuilder();
        return getActiveMoments(momentDao.query(queryBuilder.prepare()), dbFetchRequestListner);
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
        QueryBuilder<OrmCharacteristics, Integer> queryBuilder = characteristicsDao.queryBuilder();
        List<OrmCharacteristics> ormCharacteristicsList = characteristicsDao.query(queryBuilder.prepare());
        if (ormCharacteristicsList.size() != 0) {
            List list = new ArrayList();
            list.add(ormCharacteristicsList.get(0));
            dbFetchRequestListner.onFetchSuccess(list);
        } else {
            dbFetchRequestListner.onFetchSuccess(null);
        }

    }

    @Override
    public void fetchCharacteristics(DBFetchRequestListner<Characteristics> dbFetchRequestListner) throws SQLException {
        QueryBuilder<OrmCharacteristics, Integer> queryBuilder = characteristicsDao.queryBuilder();
        queryBuilder.where().in("parent", 0);
        List<OrmCharacteristics> ormCharacteristicsList = characteristicsDao.query(queryBuilder.prepare());
        if (ormCharacteristicsList.size() != 0) {
            dbFetchRequestListner.onFetchSuccess(ormCharacteristicsList);
        } else {
            dbFetchRequestListner.onFetchSuccess(null);
        }
    }

    @Override
    public void postError(Exception e, DBFetchRequestListner dbFetchRequestListner) {
        dbFetchRequestListner.onFetchFailure(e);
    }

    @Override
    public void fetchMoments(@NonNull final String type, DBFetchRequestListner<Moment> dbFetchRequestListner) throws SQLException {
        DSLog.i(DSLog.LOG, "In fetchMoments - OrmFetchingInterfaceImpl");
        final QueryBuilder<OrmMoment, Integer> queryBuilder = momentDao.queryBuilder();
        queryBuilder.orderBy("dateTime", true);
        getActiveMoments(momentDao.queryForEq("type_id", type), dbFetchRequestListner);
    }

    @Override
    public void fetchMoments(DBFetchRequestListner<Moment> dbFetchRequestListner, @NonNull final Object... types) throws SQLException {
        List<Integer> ids = new ArrayList<>();
        final int i = 0;
        for (Object object : types) {
            if (object instanceof Integer) {
                ids.add((Integer) object);
            }else if(object instanceof String){
                int idFromDescription = MomentType.getIDFromDescription((String) object);
                ids.add(idFromDescription);
            }
        }
        final QueryBuilder<OrmMoment, Integer> queryBuilder = momentDao.queryBuilder();
        queryBuilder.where().in("type_id", ids);
        queryBuilder.orderBy("dateTime", true);
        getActiveMoments(momentDao.query(queryBuilder.prepare()), dbFetchRequestListner);
    }

    @Override
    public void fetchLastMoment(final String type, DBFetchRequestListner<Moment> dbFetchRequestListner) throws SQLException {
        QueryBuilder<OrmMoment, Integer> builder = momentDao.queryBuilder();
        Where<OrmMoment, Integer> where = builder.where();
        where.eq("type_id", type);
        builder.setWhere(where);
        builder.orderBy("dateTime", false);

        OrmMoment ormMoments = momentDao.queryForFirst(builder.prepare());
        ArrayList<OrmMoment> moments = new ArrayList<>();
        moments.add(ormMoments);

        dbFetchRequestListner.onFetchSuccess((List) ormMoments);
    }

    @Override
    public Object fetchMomentByGuid(@NonNull final String guid) throws SQLException {
        QueryBuilder<OrmSynchronisationData, Integer> syncDataQueryBuilder = getSyncQueryBuilderWithGuidFilter(guid);

        QueryBuilder<OrmMoment, Integer> momentQueryBuilder = momentDao.queryBuilder();
        return momentQueryBuilder.join(syncDataQueryBuilder).queryForFirst();
    }

    @NonNull
    private QueryBuilder<OrmSynchronisationData, Integer> getSyncQueryBuilderWithGuidFilter(final @NonNull String guid) throws SQLException {
        QueryBuilder<OrmSynchronisationData, Integer> syncDataQueryBuilder = synchronisationDataDao.queryBuilder();

        syncDataQueryBuilder.where().eq("guid", guid);
        return syncDataQueryBuilder;
    }

    @Override
    public List<?> fetchNonSynchronizedMoments() throws SQLException {
        DSLog.i(DSLog.LOG, "In OrmFetchingInterfaceImpl fetchNonSynchronizedMoments");
        QueryBuilder<OrmMoment, Integer> momentQueryBuilder = momentDao.queryBuilder();
        DSLog.i(DSLog.LOG, "In OrmFetchingInterfaceImpl after query builder");
        momentQueryBuilder.where().eq(SYNCED_FIELD, false);
        DSLog.i(DSLog.LOG, "In OrmFetchingInterfaceImpl after where and before query");
        return momentQueryBuilder.query();
    }

    @Override
    public Object fetchMomentById(final int id, DBFetchRequestListner<Moment> dbFetchRequestListner) throws SQLException {
        QueryBuilder<OrmMoment, Integer> momentQueryBuilder = momentDao.queryBuilder();
        momentQueryBuilder.where().eq("id", id);
        Object object = momentQueryBuilder.queryForFirst();
        List list = new ArrayList();
        list.add(object);

        if (dbFetchRequestListner != null)
            dbFetchRequestListner.onFetchSuccess(list);
        return object;
    }

    public List<OrmMoment> getActiveMoments(final List<?> ormMoments, DBFetchRequestListner<Moment> dbFetchRequestListner) {
        DSLog.i(DSLog.LOG, "pabitra In getActiveMoments - OrmFetchingInterfaceImpl");
        List<OrmMoment> activeOrmMoments = new ArrayList<>();
        if (ormMoments != null) {
            for (OrmMoment ormMoment : (List<OrmMoment>) ormMoments) {
                if (ormMoment.getSynchronisationData() == null || !ormMoment.getSynchronisationData().isInactive()) {
                    activeOrmMoments.add(ormMoment);
                }
            }
        }
        DSLog.i(DSLog.LOG, "pabitra In getActiveMoments - OrmFetchingInterfaceImpl and ormMoments = " + ormMoments);
        notifyDBRequestListener.notifyMomentFetchSuccess(activeOrmMoments, dbFetchRequestListner);
        return activeOrmMoments;
    }

    @Override
    public Map<Class, List<?>> putUserCharacteristicsForSync(Map<Class, List<?>> dataToSync) throws SQLException {
        List<? extends Characteristics> characteristicses = fetchNonSynchronizedCharacteristics();
        dataToSync.put(Characteristics.class, characteristicses);
        return dataToSync;
    }

    private List<? extends Characteristics> fetchNonSynchronizedCharacteristics() throws SQLException {

        List<OrmCharacteristics> query = new ArrayList<>();
        if (!isSynced(SyncType.CHARACTERISTICS.getId())) {
            QueryBuilder<OrmCharacteristics, Integer> characteristicsIntegerQueryBuilder = characteristicsDao.queryBuilder();
            query = characteristicsIntegerQueryBuilder.query();
            return query;
        }
        return null;
    }

    @Override
    public OrmSettings fetchSettings(DBFetchRequestListner<Settings> dbFetchRequestListner) throws SQLException {
        QueryBuilder<OrmSettings, Integer> settingsQueryBuilder = settingsDao.queryBuilder();

        OrmSettings ormSettings = settingsQueryBuilder.queryForFirst();
        List list = new ArrayList();
        list.add(ormSettings);
        dbFetchRequestListner.onFetchSuccess(list);
        return ormSettings;
    }

    @Override
    public Settings fetchSettings() throws SQLException {
        QueryBuilder<OrmSettings, Integer> settingsQueryBuilder = settingsDao.queryBuilder();

        OrmSettings ormSettings = settingsQueryBuilder.queryForFirst();

        return ormSettings;
    }

    @Override
    public List<?> fetchNonSyncSettings() throws SQLException {

        List<OrmSettings> ormSettingsList = new ArrayList<>();

        if (isSynced(SyncType.SETTINGS.getId()) == true) {
            return ormSettingsList;
        } else {
            QueryBuilder<OrmSettings, Integer> settingsQueryBuilder = settingsDao.queryBuilder();

            ormSettingsList = settingsQueryBuilder.query();
            return ormSettingsList;
        }
    }


    public OrmCharacteristics fetchUCByCreatorId(@NonNull final String creatorId) throws SQLException {
        QueryBuilder<OrmCharacteristics, Integer> lUCQueryBuilder = characteristicsDao.queryBuilder();
        lUCQueryBuilder.where().eq("creatorId", creatorId);
        if (lUCQueryBuilder.query().isEmpty()) {
            return null;
        }
        return lUCQueryBuilder.query().get(lUCQueryBuilder.query().size() - 1); //equivalent to query for last
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
        QueryBuilder<OrmInsight, Integer> queryBuilder = ormInsightDao.queryBuilder();
        return getActiveInsights(ormInsightDao.query(queryBuilder.prepare()), dbFetchRequestListner);
    }

    public List<OrmInsight> getActiveInsights(final List<?> ormInsights, DBFetchRequestListner<Insight> dbFetchRequestListner) {
        List<OrmInsight> activeOrmInsights = new ArrayList<>();
        if (ormInsights != null) {
            for (OrmInsight ormInsight : (List<OrmInsight>) ormInsights) {
                if (ormInsight.getSynchronisationData() == null || !ormInsight.getSynchronisationData().isInactive()) {
                    activeOrmInsights.add(ormInsight);
                }
            }
        }
        notifyDBRequestListener.notifyInsightFetchSuccess(activeOrmInsights, dbFetchRequestListner);
        return activeOrmInsights;
    }

    @Override
    public Insight fetchInsightByGuid(@NonNull String guid) throws SQLException {
        QueryBuilder<OrmInsight, Integer> insightQueryBuilder = ormInsightDao.queryBuilder();
        insightQueryBuilder.where().eq("guid", guid);
        return insightQueryBuilder.queryForFirst();
    }

    @Override
    public Insight fetchInsightById(int id, DBFetchRequestListner<Insight> dbFetchRequestListner) throws SQLException {
        QueryBuilder<OrmInsight, Integer> insightQueryBuilder = ormInsightDao.queryBuilder();
        insightQueryBuilder.where().eq("id", id);
        return insightQueryBuilder.queryForFirst();
    }

    @Override
    public List<?> fetchNonSynchronizedInsights() throws SQLException {
        QueryBuilder<OrmInsight, Integer> insightQueryBuilder = ormInsightDao.queryBuilder();
        insightQueryBuilder.where().eq(SYNCED_FIELD, false);
        return insightQueryBuilder.query();
    }
}
