/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package cdp.philips.com.mydemoapp.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentType;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cdp.philips.com.mydemoapp.database.table.OrmMoment;
import cdp.philips.com.mydemoapp.database.table.OrmSynchronisationData;
import cdp.philips.com.mydemoapp.listener.DBChangeListener;
import cdp.philips.com.mydemoapp.listener.EventHelper;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class OrmFetchingInterfaceImpl implements DBFetchingInterface{

    static final String SYNCED_FIELD = "synced";


    @NonNull
    private Dao<OrmMoment, Integer> momentDao;

    @NonNull
    private Dao<OrmSynchronisationData, Integer> synchronisationDataDao;



    public OrmFetchingInterfaceImpl(final @NonNull Dao<OrmMoment, Integer> momentDao,
                                    final @NonNull Dao<OrmSynchronisationData, Integer> synchronisationDataDao) {
        this.momentDao = momentDao;
        this.synchronisationDataDao = synchronisationDataDao;

    }

    @Override
    public void fetchMoments() throws SQLException {
        QueryBuilder<OrmMoment, Integer> queryBuilder = momentDao.queryBuilder();
            getActiveMoments(momentDao.query(queryBuilder.prepare()));
        }


    @Override
    public void fetchMoments(@NonNull final MomentType type) throws SQLException {
        final QueryBuilder<OrmMoment, Integer> queryBuilder = momentDao.queryBuilder();
        queryBuilder.orderBy("dateTime", true);
        getActiveMoments(momentDao.queryForEq("type_id", type.getId()));
    }

    @Override
    public void fetchMoments(@NonNull final MomentType... types) throws SQLException{
        List<OrmMoment> ormMoments = new ArrayList<OrmMoment>();
        List<Integer> ids = new ArrayList<>(types.length);
        final int i = 0;
        for (MomentType momentType : types) {
            ids.add(momentType.getId());
        }
        final QueryBuilder<OrmMoment, Integer> queryBuilder = momentDao.queryBuilder();
        queryBuilder.where().in("type_id", ids);
        queryBuilder.orderBy("dateTime", true);
        getActiveMoments(momentDao.query(queryBuilder.prepare()));
    }

    @Override
    public void fetchLastMoment(final MomentType type) throws SQLException{
        QueryBuilder<OrmMoment, Integer> builder = momentDao.queryBuilder();
        Where<OrmMoment, Integer> where = builder.where();
        where.eq("type_id", type.getId());
        builder.setWhere(where);
        builder.orderBy("dateTime", false);

        OrmMoment ormMoments = momentDao.queryForFirst(builder.prepare());
        ArrayList<OrmMoment> moments = new ArrayList<>();
        moments.add(ormMoments);

        notifySuccessToAll(moments);
    }

    @Override
    public Object fetchMomentByGuid(@NonNull final String guid) throws SQLException{
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
    public List<?> fetchNonSynchronizedMoments() throws SQLException{
        Log.i("***SPO***","In OrmFetchingInterfaceImpl fetchNonSynchronizedMoments");
        QueryBuilder<OrmMoment, Integer> momentQueryBuilder = momentDao.queryBuilder();
        momentQueryBuilder.where().eq(SYNCED_FIELD, false);

        return momentQueryBuilder.query();
    }

    @Override
    public Object fetchMomentById(final int id) throws SQLException{
        QueryBuilder<OrmMoment, Integer> momentQueryBuilder = momentDao.queryBuilder();
        momentQueryBuilder.where().eq("id", id);

        return momentQueryBuilder.queryForFirst();
    }

    public void getActiveMoments(final List<?> ormMoments) {
        List<OrmMoment> activeOrmMoments = new ArrayList<>();
        if (ormMoments != null) {
            for (OrmMoment ormMoment : (List<OrmMoment>)ormMoments) {
                if (ormMoment.getSynchronisationData() == null || !ormMoment.getSynchronisationData().isInactive()) {
                    activeOrmMoments.add(ormMoment);
                }
            }
        }

        notifySuccessToAll((ArrayList<? extends Object>) ormMoments);
    }

    private void notifySuccessToAll(final ArrayList<? extends Object> ormMoments) {
        Map<Integer, ArrayList<DBChangeListener>> eventMap = EventHelper.getInstance().getEventMap();
        Set<Integer> integers = eventMap.keySet();
        if(integers.contains(EventHelper.MOMENT)) {
            ArrayList<DBChangeListener> dbChangeListeners = EventHelper.getInstance().getEventMap().get(EventHelper.MOMENT);
            for (DBChangeListener listener : dbChangeListeners) {
                listener.onSuccess(ormMoments);
            }
        }
    }

    private void notifyAllFailure(Exception e) {
        Map<Integer, ArrayList<DBChangeListener>> eventMap = EventHelper.getInstance().getEventMap();
        Set<Integer> integers = eventMap.keySet();
        if(integers.contains(EventHelper.MOMENT)){
            ArrayList<DBChangeListener> dbChangeListeners = EventHelper.getInstance().getEventMap().get(EventHelper.MOMENT);
            for (DBChangeListener listener : dbChangeListeners) {
                listener.onFailure(e);
            }
        }
    }

    @Override
    public Map<Class, List<?>> putMomentsForSync(final Map<Class, List<?>> dataToSync) throws SQLException {
        Log.i("***SPO***","In OrmFetchingInterfaceImpl before fetchNonSynchronizedMoments");
        List<? extends Moment> ormMomentList = (List<? extends Moment>)fetchNonSynchronizedMoments();
        Log.i("***SPO***","In OrmFetchingInterfaceImpl dataToSync.put");
        dataToSync.put(Moment.class, ormMomentList);
        return dataToSync;
    }
}
