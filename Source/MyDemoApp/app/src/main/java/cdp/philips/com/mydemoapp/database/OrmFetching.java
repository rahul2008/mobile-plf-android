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
import com.philips.platform.core.datatypes.MomentType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cdp.philips.com.mydemoapp.database.table.OrmMoment;
import cdp.philips.com.mydemoapp.database.table.OrmSynchronisationData;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class OrmFetching {

    static final String SYNCED_FIELD = "synced";


    @NonNull
    private Dao<OrmMoment, Integer> momentDao;

    @NonNull
    private Dao<OrmSynchronisationData, Integer> synchronisationDataDao;



    public OrmFetching(final @NonNull Dao<OrmMoment, Integer> momentDao,
                       final @NonNull Dao<OrmSynchronisationData, Integer> synchronisationDataDao) {
        this.momentDao = momentDao;
        this.synchronisationDataDao = synchronisationDataDao;

    }

    @NonNull
    public List<OrmMoment> fetchMoments() throws SQLException {
        QueryBuilder<OrmMoment, Integer> queryBuilder = momentDao.queryBuilder();
//        queryBuilder.orderBy("dateTime", false);
//        queryBuilder.where().eq(SYNCED_FIELD, true);
        return momentDao.query(queryBuilder.prepare());

//        QueryBuilder<OrmMoment, Integer> queryBuilder = momentDao.queryBuilder();
//        Where<OrmMoment, Integer> where = queryBuilder.where();
//        where.ne("type_id", MomentType.TREATMENT.getId());
//        where.ne("type_id", MomentType.TREATMENT.getId());
//        where.and(2);
//        queryBuilder.orderBy("dateTime", false);
////        CloseableIterator<OrmMoment> ormMomentCloseableIterator =
////                  momentDao.closeableIterator();
////        while (ormMomentCloseableIterator.hasNext())
//        return momentDao.query(queryBuilder.prepare());
    }



    @NonNull
    public List<OrmMoment> fetchMoments(final @NonNull MomentType type) throws SQLException {
        final QueryBuilder<OrmMoment, Integer> queryBuilder = momentDao.queryBuilder();
        return momentDao.queryForEq("type_id", type.getId());
    }

    @NonNull
    public List<OrmMoment> fetchMoments(final @NonNull MomentType... types) throws SQLException {
        List<OrmMoment> ormMoments = new ArrayList<OrmMoment>();
        List<Integer> ids = new ArrayList<>(types.length);
        final int i = 0;
        for (MomentType momentType : types) {
            ids.add(momentType.getId());
        }
        final QueryBuilder<OrmMoment, Integer> queryBuilder = momentDao.queryBuilder();
        queryBuilder.where().in("type_id", ids);

        return momentDao.query(queryBuilder.prepare());
    }

    public OrmMoment fetchLastMoment(final MomentType type) throws SQLException {
        QueryBuilder<OrmMoment, Integer> builder = momentDao.queryBuilder();
        Where<OrmMoment, Integer> where = builder.where();
        where.eq("type_id", type.getId());
        builder.setWhere(where);
        builder.orderBy("dateTime", false);
        return momentDao.queryForFirst(builder.prepare());
    }

    public OrmMoment fetchMomentByGuid(@NonNull final String guid) throws SQLException {
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


    public List<OrmMoment> fetchNonSynchronizedMoments() throws SQLException {
        QueryBuilder<OrmMoment, Integer> momentQueryBuilder = momentDao.queryBuilder();
        momentQueryBuilder.where().eq(SYNCED_FIELD, false);

        return momentQueryBuilder.query();
    }



    public OrmMoment fetchMomentById(final int id) throws SQLException {
        QueryBuilder<OrmMoment, Integer> momentQueryBuilder = momentDao.queryBuilder();
        momentQueryBuilder.where().eq("id", id);

        return momentQueryBuilder.queryForFirst();
    }


}
