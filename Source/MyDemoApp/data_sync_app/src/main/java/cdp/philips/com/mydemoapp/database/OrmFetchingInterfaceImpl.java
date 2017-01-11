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
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.utils.DSLog;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cdp.philips.com.mydemoapp.database.table.OrmConsent;
import cdp.philips.com.mydemoapp.database.table.OrmConsentDetail;
import cdp.philips.com.mydemoapp.database.table.OrmMoment;
import cdp.philips.com.mydemoapp.database.table.OrmSynchronisationData;
import cdp.philips.com.mydemoapp.temperature.TemperatureMomentHelper;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class OrmFetchingInterfaceImpl implements DBFetchingInterface {

    static final String SYNCED_FIELD = "synced";


    @NonNull
    private Dao<OrmMoment, Integer> momentDao;

    @NonNull
    private Dao<OrmSynchronisationData, Integer> synchronisationDataDao;
    private final Dao<OrmConsent, Integer> consentDao;
    private final Dao<OrmConsentDetail, Integer> consentDetailsDao;


    private TemperatureMomentHelper mTemperatureMomentHelper;


    public OrmFetchingInterfaceImpl(final @NonNull Dao<OrmMoment, Integer> momentDao,
                                    final @NonNull Dao<OrmSynchronisationData, Integer> synchronisationDataDao, Dao<OrmConsent, Integer> consentDao, Dao<OrmConsentDetail, Integer> consentDetailsDao) {
        this.momentDao = momentDao;
        this.synchronisationDataDao = synchronisationDataDao;
        mTemperatureMomentHelper = new TemperatureMomentHelper();

        this.consentDao = consentDao;
        this.consentDetailsDao = consentDetailsDao;
    }

    @Override
    public void fetchMoments(DBRequestListener dbRequestListener) throws SQLException {
        QueryBuilder<OrmMoment, Integer> queryBuilder = momentDao.queryBuilder();
        getActiveMoments(momentDao.query(queryBuilder.prepare()), dbRequestListener);
    }

    @Override
    public void fetchConsents(DBRequestListener dbRequestListener) throws SQLException {
        QueryBuilder<OrmConsent, Integer> queryBuilder = consentDao.queryBuilder();
        ArrayList<OrmConsent> ormConsents = (ArrayList<OrmConsent>) consentDao.query(queryBuilder.prepare());
        if (ormConsents.size() != 0) {
            dbRequestListener.onSuccess(ormConsents.get(0));
        } else {
            dbRequestListener.onSuccess(null);
        }
    }

    @Override
    public Consent fetchConsent(DBRequestListener dbRequestListener) throws SQLException {
        QueryBuilder<OrmConsent, Integer> queryBuilder = consentDao.queryBuilder();
        ArrayList<OrmConsent> ormConsents = (ArrayList<OrmConsent>) consentDao.query(queryBuilder.prepare());
        if (ormConsents != null && !ormConsents.isEmpty()) {
            return ormConsents.get(ormConsents.size() - 1);
        } else {
            return null;
        }
    }

    @Override
    public void postError(Exception e, DBRequestListener dbRequestListener) {
        dbRequestListener.onFailure(e);
    }

    @Override
    public void fetchMoments(@NonNull final String type,DBRequestListener dbRequestListener) throws SQLException {
        DSLog.i("***SPO***", "In fetchMoments - OrmFetchingInterfaceImpl");
        final QueryBuilder<OrmMoment, Integer> queryBuilder = momentDao.queryBuilder();
        queryBuilder.orderBy("dateTime", true);
        getActiveMoments(momentDao.queryForEq("type_id", type), dbRequestListener);
    }

    @Override
    public void fetchMoments(DBRequestListener dbRequestListener,@NonNull final Object... types) throws SQLException {
        List<OrmMoment> ormMoments = new ArrayList<OrmMoment>();
        List<Integer> ids = new ArrayList<>();
        final int i = 0;
        for (Object object : types) {
            if (object instanceof Integer) {
                ids.add((Integer) object);
            }
        }
        final QueryBuilder<OrmMoment, Integer> queryBuilder = momentDao.queryBuilder();
        queryBuilder.where().in("type_id", ids);
        queryBuilder.orderBy("dateTime", true);
        getActiveMoments(momentDao.query(queryBuilder.prepare()), dbRequestListener);
    }

    @Override
    public void fetchLastMoment(final String type,DBRequestListener dbRequestListener) throws SQLException {
        QueryBuilder<OrmMoment, Integer> builder = momentDao.queryBuilder();
        Where<OrmMoment, Integer> where = builder.where();
        where.eq("type_id", type);
        builder.setWhere(where);
        builder.orderBy("dateTime", false);

        OrmMoment ormMoments = momentDao.queryForFirst(builder.prepare());
        ArrayList<OrmMoment> moments = new ArrayList<>();
        moments.add(ormMoments);

        dbRequestListener.onSuccess(ormMoments);
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
        DSLog.i("***SPO***","In getActiveMoments - OrmFetchingInterfaceImpl and ormMoments = " + ormMoments);
        dbRequestListener.onSuccess((ArrayList<? extends Object>)ormMoments);
       // mTemperatureMomentHelper.notifySuccessToAll((ArrayList<? extends Object>) activeOrmMoments,dbRequestListener);
    }

    @Override
    public Map<Class, List<?>> putMomentsForSync(final Map<Class, List<?>> dataToSync) throws SQLException {
        DSLog.i("***SPO***", "In OrmFetchingInterfaceImpl before fetchNonSynchronizedMoments");
        List<? extends Moment> ormMomentList = (List<? extends Moment>) fetchNonSynchronizedMoments();
        DSLog.i("***SPO***", "In OrmFetchingInterfaceImpl dataToSync.put");
        dataToSync.put(Moment.class, ormMomentList);
        return dataToSync;
    }

    @Override
    public Map<Class, List<?>> putConsentForSync(Map<Class, List<?>> dataToSync) throws SQLException {
        List<? extends Consent> consentList = fetchConsentsWithNonSynchronizedConsentDetails();
        dataToSync.put(Consent.class, consentList);
        return dataToSync;
    }

    public List<OrmConsent> fetchConsentsWithNonSynchronizedConsentDetails() throws SQLException {
        QueryBuilder<OrmConsent, Integer> consentQueryBuilder = consentDao.queryBuilder();
        final List<OrmConsent> query = consentQueryBuilder.query();
        for (OrmConsent ormConsent : query) {

            boolean isNonSyncConsentDetailExist = false;
            for (OrmConsentDetail ormConsentDetail : ormConsent.getConsentDetails()) {
                if (!ormConsentDetail.getBackEndSynchronized()) {
                    isNonSyncConsentDetailExist = true;
                }
            }

            if (!isNonSyncConsentDetailExist) {
                query.remove(ormConsent);
            }
        }
        //consentQueryBuilder.where().eq("beSynchronized", false);

        return query;
    }

    public List<OrmConsentDetail> fetchNonSynchronizedConsentDetails() throws SQLException {
        QueryBuilder<OrmConsentDetail, Integer> consentQueryBuilder = consentDetailsDao.queryBuilder();
        consentQueryBuilder.where().eq("beSynchronized", false);

        return consentQueryBuilder.query();
    }

    public OrmConsent fetchConsentByCreatorId(@NonNull final String creatorId) throws SQLException {
        QueryBuilder<OrmConsent, Integer> consentQueryBuilder = consentDao.queryBuilder();
        consentQueryBuilder.where().eq("creatorId", creatorId);
        if (consentQueryBuilder.query().isEmpty()) {
            return null;
        }
        return consentQueryBuilder.query().get(consentQueryBuilder.query().size() - 1); //equivalent to query for last
    }

    public List<OrmConsent> fetchAllConsent() throws SQLException {
        QueryBuilder<OrmConsent, Integer> consentQueryBuilder = consentDao.queryBuilder();
        return consentQueryBuilder.query();
    }


    public List<OrmConsent> fetchAllConsentByCreatorId(@NonNull final String creatorId) throws SQLException {
        QueryBuilder<OrmConsent, Integer> consentQueryBuilder = consentDao.queryBuilder();
        consentQueryBuilder.where().eq("creatorId", creatorId);

        return consentQueryBuilder.query();
    }
}
