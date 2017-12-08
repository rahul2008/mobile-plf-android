/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/

package com.philips.platform.dscdemo.database;

import android.support.annotation.NonNull;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.DCSync;
import com.philips.platform.core.datatypes.DSPagination;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.dscdemo.database.datatypes.MomentType;
import com.philips.platform.dscdemo.database.table.OrmCharacteristics;
import com.philips.platform.dscdemo.database.table.OrmConsentDetail;
import com.philips.platform.dscdemo.database.table.OrmDCSync;
import com.philips.platform.dscdemo.database.table.OrmInsight;
import com.philips.platform.dscdemo.database.table.OrmMoment;
import com.philips.platform.dscdemo.database.table.OrmSettings;
import com.philips.platform.dscdemo.database.table.OrmSynchronisationData;
import com.philips.platform.dscdemo.utility.NotifyDBRequestListener;

import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
public class OrmFetchingInterfaceImpl implements DBFetchingInterface {
    private static final String SYNCED_FIELD = "synced";
    @NonNull
    private Dao<OrmMoment, Integer> momentDao;
    @NonNull
    private Dao<OrmSynchronisationData, Integer> synchronisationDataDao;
    private final Dao<OrmConsentDetail, Integer> consentDetailsDao;
    private final Dao<OrmSettings, Integer> settingsDao;
    private final Dao<OrmCharacteristics, Integer> characteristicsDao;
    private final Dao<OrmDCSync, Integer> ormDCSyncDao;
    private final Dao<OrmInsight, Integer> ormInsightDao;

    private NotifyDBRequestListener notifyDBRequestListener;


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

    //Moments
    @Override
    public List<? extends Moment> fetchMoments(DBFetchRequestListner<Moment> dbFetchRequestListener) throws SQLException {
        QueryBuilder<OrmMoment, Integer> queryBuilder = momentDao.queryBuilder();
        List<OrmMoment> activeOrmMoments = getActiveMoments(momentDao.query(queryBuilder.prepare()));
        notifyDBRequestListener.notifyMomentFetchSuccess(activeOrmMoments, dbFetchRequestListener);
        return activeOrmMoments;
    }

    @Override
    public void fetchMoments(@NonNull final String type, DBFetchRequestListner<Moment> dbFetchRequestListener) throws SQLException {
        final QueryBuilder<OrmMoment, Integer> queryBuilder = momentDao.queryBuilder();
        queryBuilder.orderBy("dateTime", true);
        List<OrmMoment> activeOrmMoments = getActiveMoments(momentDao.queryForEq("type_id", type));
        notifyDBRequestListener.notifyMomentFetchSuccess(activeOrmMoments, dbFetchRequestListener);
    }

    @Override
    public void fetchMoments(DBFetchRequestListner<Moment> dbFetchRequestListener, @NonNull final Object... types) throws SQLException {
        List<Integer> ids = new ArrayList<>();
        for (Object object : types) {
            if (object instanceof Integer) {
                ids.add((Integer) object);
            } else if (object instanceof String) {
                int idFromDescription = MomentType.getIDFromDescription((String) object);
                ids.add(idFromDescription);
            }
        }

        final QueryBuilder<OrmMoment, Integer> queryBuilder = momentDao.queryBuilder();
        queryBuilder.where().in("type_id", ids);
        queryBuilder.orderBy("dateTime", true);
        List<OrmMoment> activeOrmMoments = getActiveMoments(momentDao.query(queryBuilder.prepare()));
        notifyDBRequestListener.notifyMomentFetchSuccess(activeOrmMoments, dbFetchRequestListener);
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
    public void fetchLatestMomentByType(String type, DBFetchRequestListner<Moment> dbFetchRequestListener) throws SQLException {
        QueryBuilder<OrmMoment, Integer> builder = momentDao.queryBuilder();
        builder.where().eq("type_id", MomentType.getIDFromDescription(type));
        builder.orderBy("dateTime", false);

        List<OrmMoment> activeOrmMoments = getActiveMoments(momentDao.query(builder.prepare()));
        ArrayList<OrmMoment> moments = new ArrayList<>();

        if (activeOrmMoments.size() > 0) {
            moments.add(activeOrmMoments.get(0));
        }

        dbFetchRequestListener.onFetchSuccess((List) moments);
    }

    @Override
    public void fetchMomentsWithTimeLine(Date startDate, Date endDate, DSPagination paginationModel,
                                             DBFetchRequestListner<Moment> dbFetchRequestListener) throws SQLException {
        QueryBuilder<OrmMoment, Integer> builder = momentDao.queryBuilder();
        builder.where().between("dateTime", new DateTime(startDate), new DateTime(endDate));
        if (paginationModel.getOrdering().equals(DSPagination.DSPaginationOrdering.ASCENDING))
            builder.orderBy("dateTime", true);
        else
            builder.orderBy(paginationModel.getOrderBy(), false);

         builder.offset((long) paginationModel.getPageNumber()).limit((long) paginationModel.getPageLimit());
        List<OrmMoment> ormMoments = getActiveMoments(momentDao.query(builder.prepare()));
        dbFetchRequestListener.onFetchSuccess((List) ormMoments);
    }

    @Override
    public void fetchMomentsWithTypeAndTimeLine(String momentType, Date startDate, Date endDate, DSPagination paginationModel,
                                             DBFetchRequestListner<Moment> dbFetchRequestListener) throws SQLException {
        QueryBuilder<OrmMoment, Integer> builder = momentDao.queryBuilder();

        Where where = builder.where();
        where.and(where.eq("type_id", MomentType.getIDFromDescription(momentType)),
                where.between("dateTime", new DateTime(startDate), new DateTime(endDate)));
        if (paginationModel.getOrdering().equals(DSPagination.DSPaginationOrdering.ASCENDING))
            builder.orderBy(paginationModel.getOrderBy(), true);
        else
            builder.orderBy(paginationModel.getOrderBy(), false);
        builder.offset((long) paginationModel.getPageNumber()).limit((long) paginationModel.getPageLimit());
        List<OrmMoment> ormMoments = getActiveMoments(momentDao.query(builder.prepare()));
        dbFetchRequestListener.onFetchSuccess((List) ormMoments);
    }

    @Override
    public Object fetchMomentByGuid(@NonNull final String guid) throws SQLException {
        QueryBuilder<OrmSynchronisationData, Integer> syncDataQueryBuilder = getSyncQueryBuilderWithGuidFilter(guid);
        QueryBuilder<OrmMoment, Integer> momentQueryBuilder = momentDao.queryBuilder();
        return momentQueryBuilder.join(syncDataQueryBuilder).queryForFirst();
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

    @Override
    public List<?> fetchNonSynchronizedMoments() throws SQLException {
        QueryBuilder<OrmMoment, Integer> momentQueryBuilder = momentDao.queryBuilder();
        momentQueryBuilder.where().eq(SYNCED_FIELD, false);
        return momentQueryBuilder.query();
    }

    private List<OrmMoment> getActiveMoments(final List<?> ormMoments) {
        List<OrmMoment> activeOrmMoments = new ArrayList<>();
        if (ormMoments != null) {
            for (OrmMoment ormMoment : (List<OrmMoment>) ormMoments) {
                if (ormMoment.getSynchronisationData() == null || !ormMoment.getSynchronisationData().isInactive()) {
                    activeOrmMoments.add(ormMoment);
                }
            }
        }
        return activeOrmMoments;
    }

    //Consents
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
        return consentDetailsDao.query(queryBuilder.prepare());
    }

    //User Characteristics
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
    public Map<Class, List<?>> putUserCharacteristicsForSync(Map<Class, List<?>> dataToSync) throws SQLException {
        List<? extends Characteristics> characteristicses = fetchNonSynchronizedCharacteristics();
        dataToSync.put(Characteristics.class, characteristicses);
        return dataToSync;
    }

    private List<? extends Characteristics> fetchNonSynchronizedCharacteristics() throws SQLException {
        List<OrmCharacteristics> query;
        if (!isSynced(SyncType.CHARACTERISTICS.getId())) {
            QueryBuilder<OrmCharacteristics, Integer> characteristicsIntegerQueryBuilder = characteristicsDao.queryBuilder();
            query = characteristicsIntegerQueryBuilder.query();
            return query;
        }
        return null;
    }

   /* public OrmCharacteristics fetchUCByCreatorId(@NonNull final String creatorId) throws SQLException {
        QueryBuilder<OrmCharacteristics, Integer> lUCQueryBuilder = characteristicsDao.queryBuilder();
        lUCQueryBuilder.where().eq("creatorId", creatorId);
        if (lUCQueryBuilder.query().isEmpty()) {
            return null;
        }
        return lUCQueryBuilder.query().get(lUCQueryBuilder.query().size() - 1); //equivalent to query for last
    }*/

    //Settings
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
        return settingsQueryBuilder.queryForFirst();
    }

    @Override
    public List<?> fetchNonSyncSettings() throws SQLException {
        List<OrmSettings> ormSettingsList = new ArrayList<>();
        if (isSynced(SyncType.SETTINGS.getId())) {
            return ormSettingsList;
        } else {
            QueryBuilder<OrmSettings, Integer> settingsQueryBuilder = settingsDao.queryBuilder();
            ormSettingsList = settingsQueryBuilder.query();
            return ormSettingsList;
        }
    }

    //Sync
    @NonNull
    private QueryBuilder<OrmSynchronisationData, Integer> getSyncQueryBuilderWithGuidFilter(final @NonNull String guid) throws SQLException {
        QueryBuilder<OrmSynchronisationData, Integer> syncDataQueryBuilder = synchronisationDataDao.queryBuilder();
        syncDataQueryBuilder.where().eq("guid", guid);
        return syncDataQueryBuilder;
    }

    @Override
    public boolean isSynced(int tableID) throws SQLException {
        QueryBuilder<OrmDCSync, Integer> lDCSyncQueryBuilder = ormDCSyncDao.queryBuilder();
        lDCSyncQueryBuilder.where().eq("tableID", tableID);
        OrmDCSync ormDCSync = lDCSyncQueryBuilder.queryForFirst();
        return ormDCSync == null || ormDCSync.isSynced();
    }

    @Override
    public DCSync fetchDCSyncData(SyncType syncType) throws SQLException {
        QueryBuilder<OrmDCSync, Integer> lDCSyncQueryBuilder = ormDCSyncDao.queryBuilder();
        lDCSyncQueryBuilder.where().eq("tableID", syncType.getId());
        return lDCSyncQueryBuilder.queryForFirst();
    }

    //Insights
    @Override
    public List<? extends Insight> fetchActiveInsights(DBFetchRequestListner<Insight> dbFetchRequestListner) throws SQLException {
        QueryBuilder<OrmInsight, Integer> queryBuilder = ormInsightDao.queryBuilder();
        return getActiveInsights(ormInsightDao.query(queryBuilder.prepare()), dbFetchRequestListner);
    }

    private List<OrmInsight> getActiveInsights(final List<?> ormInsights, DBFetchRequestListner<Insight> dbFetchRequestListner) {
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

    //Post Error
    @Override
    public void postError(Exception e, DBFetchRequestListner dbFetchRequestListner) {
        dbFetchRequestListner.onFetchFailure(e);
    }
}
