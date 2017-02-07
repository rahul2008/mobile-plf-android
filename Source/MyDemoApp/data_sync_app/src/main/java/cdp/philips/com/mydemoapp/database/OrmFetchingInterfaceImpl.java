/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package cdp.philips.com.mydemoapp.database;

import android.support.annotation.NonNull;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.OrmTableType;
import com.philips.platform.core.datatypes.OrmTableType;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.utils.DSLog;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cdp.philips.com.mydemoapp.database.datatypes.MomentType;
import cdp.philips.com.mydemoapp.database.table.OrmCharacteristics;
import cdp.philips.com.mydemoapp.database.table.OrmConsentDetail;
import cdp.philips.com.mydemoapp.database.table.OrmDCSync;
import cdp.philips.com.mydemoapp.database.table.OrmMoment;
import cdp.philips.com.mydemoapp.database.table.OrmSettings;
import cdp.philips.com.mydemoapp.database.table.OrmSynchronisationData;
import cdp.philips.com.mydemoapp.temperature.TemperatureMomentHelper;
import cdp.philips.com.mydemoapp.utility.NotifyDBRequestListener;

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


    private TemperatureMomentHelper mTemperatureMomentHelper;


    public OrmFetchingInterfaceImpl(final @NonNull Dao<OrmMoment, Integer> momentDao,
                                    final @NonNull Dao<OrmSynchronisationData, Integer> synchronisationDataDao, Dao<OrmConsentDetail, Integer> consentDetailsDao, Dao<OrmCharacteristics, Integer> characteristicsDao, Dao<OrmSettings, Integer> settingsDao, Dao<OrmDCSync, Integer> ormDCSyncDao) {

        this.momentDao = momentDao;
        this.synchronisationDataDao = synchronisationDataDao;
        this.characteristicsDao = characteristicsDao;
        this.consentDetailsDao = consentDetailsDao;
        this.settingsDao = settingsDao;
        this.ormDCSyncDao = ormDCSyncDao;

        notifyDBRequestListener = new NotifyDBRequestListener();
    }

    @Override
    public void fetchMoments(DBRequestListener dbRequestListener) throws SQLException {
        QueryBuilder<OrmMoment, Integer> queryBuilder = momentDao.queryBuilder();
        getActiveMoments(momentDao.query(queryBuilder.prepare()), dbRequestListener);
    }

    @Override
    public void fetchConsentDetails(DBRequestListener dbRequestListener) throws SQLException {

        QueryBuilder<OrmConsentDetail, Integer> queryBuilder = consentDetailsDao.queryBuilder();
        ArrayList<OrmConsentDetail> ormConsents = (ArrayList<OrmConsentDetail>) consentDetailsDao.query(queryBuilder.prepare());
        if (ormConsents != null && !ormConsents.isEmpty()) {
            notifyDBRequestListener.notifySuccess(dbRequestListener, ormConsents);
        }
    }

    @Override
    public List<?> fetchNonSyncConsentDetails() throws SQLException {

        List<OrmConsentDetail> ormConsents = new ArrayList<>();
        if (!isSynced(OrmTableType.CONSENT.getId())) {
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
    public void fetchUserCharacteristics(DBRequestListener dbRequestListener) throws SQLException {
        QueryBuilder<OrmCharacteristics, Integer> queryBuilder = characteristicsDao.queryBuilder();
        List<OrmCharacteristics> ormCharacteristicsList = characteristicsDao.query(queryBuilder.prepare());
        if (ormCharacteristicsList.size() != 0) {
            dbRequestListener.onSuccess(ormCharacteristicsList.get(0));
        } else {
            dbRequestListener.onSuccess(null);
        }

    }

    @Override
    public void fetchCharacteristics(DBRequestListener dbRequestListener) throws SQLException {
        QueryBuilder<OrmCharacteristics, Integer> queryBuilder = characteristicsDao.queryBuilder();
        queryBuilder.where().in("parent", 0);
        List<OrmCharacteristics> ormCharacteristicsList = characteristicsDao.query(queryBuilder.prepare());
        if (ormCharacteristicsList.size() != 0) {
            dbRequestListener.onSuccess(ormCharacteristicsList);
        } else {
            dbRequestListener.onSuccess(null);
        }
    }

    @Override
    public void postError(Exception e, DBRequestListener dbRequestListener) {
        notifyDBRequestListener.notifyFailure(e, dbRequestListener);
    }

    @Override
    public void fetchMoments(@NonNull final String type, DBRequestListener dbRequestListener) throws SQLException {
        DSLog.i("***SPO***", "In fetchMoments - OrmFetchingInterfaceImpl");
        final QueryBuilder<OrmMoment, Integer> queryBuilder = momentDao.queryBuilder();
        queryBuilder.orderBy("dateTime", true);
        getActiveMoments(momentDao.queryForEq("type_id", type), dbRequestListener);
    }

    @Override
    public void fetchMoments(DBRequestListener dbRequestListener, @NonNull final Object... types) throws SQLException {
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
        getActiveMoments(momentDao.query(queryBuilder.prepare()), dbRequestListener);
    }

    @Override
    public void fetchLastMoment(final String type, DBRequestListener dbRequestListener) throws SQLException {
        QueryBuilder<OrmMoment, Integer> builder = momentDao.queryBuilder();
        Where<OrmMoment, Integer> where = builder.where();
        where.eq("type_id", type);
        builder.setWhere(where);
        builder.orderBy("dateTime", false);

        OrmMoment ormMoments = momentDao.queryForFirst(builder.prepare());
        ArrayList<OrmMoment> moments = new ArrayList<>();
        moments.add(ormMoments);

        notifyDBRequestListener.notifySuccess(dbRequestListener, ormMoments);
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
        DSLog.i("***SPO***", "In OrmFetchingInterfaceImpl fetchNonSynchronizedMoments");
        QueryBuilder<OrmMoment, Integer> momentQueryBuilder = momentDao.queryBuilder();
        DSLog.i("***SPO***", "In OrmFetchingInterfaceImpl after query builder");
        momentQueryBuilder.where().eq(SYNCED_FIELD, false);
        DSLog.i("***SPO***", "In OrmFetchingInterfaceImpl after where and before query");
        return momentQueryBuilder.query();
    }

    @Override
    public Object fetchMomentById(final int id, DBRequestListener dbRequestListener) throws SQLException {
        QueryBuilder<OrmMoment, Integer> momentQueryBuilder = momentDao.queryBuilder();
        momentQueryBuilder.where().eq("id", id);

        return momentQueryBuilder.queryForFirst();
    }

    public void getActiveMoments(final List<?> ormMoments, DBRequestListener dbRequestListener) {
        DSLog.i("***SPO***", "In getActiveMoments - OrmFetchingInterfaceImpl");
        List<OrmMoment> activeOrmMoments = new ArrayList<>();
        if (ormMoments != null) {
            for (OrmMoment ormMoment : (List<OrmMoment>) ormMoments) {
                if (ormMoment.getSynchronisationData() == null || !ormMoment.getSynchronisationData().isInactive()) {
                    activeOrmMoments.add(ormMoment);
                }
            }
        }
        DSLog.i("***SPO***", "In getActiveMoments - OrmFetchingInterfaceImpl and ormMoments = " + ormMoments);
        notifyDBRequestListener.notifySuccess((ArrayList<? extends Object>) activeOrmMoments, dbRequestListener);
    }

    @Override
    public Map<Class, List<?>> putUserCharacteristicsForSync(Map<Class, List<?>> dataToSync) throws SQLException {
        List<? extends Characteristics> characteristicses = fetchNonSynchronizedCharacteristics();
        dataToSync.put(Characteristics.class, characteristicses);
        return dataToSync;
    }

    private List<? extends Characteristics> fetchNonSynchronizedCharacteristics() throws SQLException {

        List<OrmCharacteristics> query = new ArrayList<>();

        if (!isSynced(OrmTableType.CHARACTERISTICS.getId())) {
            return query;
        }
        QueryBuilder<OrmCharacteristics, Integer> characteristicsIntegerQueryBuilder = characteristicsDao.queryBuilder();
        query = characteristicsIntegerQueryBuilder.query();


        return query;
    }

    @Override
    public OrmSettings fetchSettings(DBRequestListener dbRequestListener) throws SQLException {
        QueryBuilder<OrmSettings, Integer> settingsQueryBuilder = settingsDao.queryBuilder();

        OrmSettings ormSettings = settingsQueryBuilder.queryForFirst();
        notifyDBRequestListener.notifySuccess(dbRequestListener, ormSettings);
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

        if (isSynced(OrmTableType.SETTINGS.getId()) == true) {
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
        if (ormDCSync == null) return false;
        return ormDCSync.isSynced();
    }


}
