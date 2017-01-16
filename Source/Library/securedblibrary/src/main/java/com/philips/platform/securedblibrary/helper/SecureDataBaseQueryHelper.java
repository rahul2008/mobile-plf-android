package com.philips.platform.securedblibrary.helper;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import net.sqlcipher.database.SQLiteDatabase;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;


/**
 * Created by 310273508 on 1/12/2017.
 */

public class SecureDataBaseQueryHelper<T> {

    public SecureDbOrmLiteSqliteOpenHelper secureDataBaseHelper;
    private Context context;
     SQLiteDatabase db ;
    private String  dataBasePassword;

    public SecureDataBaseQueryHelper(Context context, SecureDbOrmLiteSqliteOpenHelper secureDataBaseHelper) {
        this.context = context;
        this.secureDataBaseHelper = secureDataBaseHelper;
        db = getWriteDbPermission();

    }

    public SecureDataBaseQueryHelper(Context context, SecureDbOrmLiteSqliteOpenHelper secureDataBaseHelper, String dataBasePassword) {
        this.context = context;
        this.secureDataBaseHelper = secureDataBaseHelper;
        this.dataBasePassword=dataBasePassword;
        db = getWriteDbPermission();

    }

    private SecureDbOrmLiteSqliteOpenHelper getHelper() {
        return secureDataBaseHelper;
    }

    public SQLiteDatabase getWriteDbPermission() {

        return getHelper().getWritableDatabase(dataBasePassword);
    }



    public void beginTransaction() {
        db.beginTransaction();
    }

    public void endTransaction() {
        db.endTransaction();
    }

    public void setTransactionSuccessful() {
        db.setTransactionSuccessful();

    }

    public boolean inTransaction() {
        return db.inTransaction();


    }


    public void createOrInsert(Class clazz, T obj) {

        create(clazz, obj);
    }

    public int deleteAll(Class clazz) {
        return deleteAllRecords(clazz);
    }

    public int deleteById(Class clazz, T obj) {
        return deleteRecordsById(clazz, obj);
    }


    public int updateAllRecords(Class clazz, String columnToBeUpdate, T valueToBeSet) {
        return updateAll(clazz, columnToBeUpdate, valueToBeSet);
    }

    public int updateRecordByWhere(Class clazz, T obj, String whereCauseColumnName, T whereCauseValue, String columnToBeUpdate) {
        return updateByWhere(clazz, obj, whereCauseColumnName, whereCauseValue, columnToBeUpdate);
    }

    public List retrieveByQuery(Class clazz, String columnName, T obj) {
        return queryBywhere(clazz, columnName, obj);
    }

    public T retrieveById(Class clazz, T obj) {
        return queryById(clazz, obj);
    }

    public List retrieveAll(Class<T> clazz) {
        return queryForAll(clazz);
    }


    private List queryForAll(Class clazz) {
        beginTransaction();

        try {
            Dao<T, ?> dao = getHelper().getDao(clazz);
            List<T> list=dao.queryForAll();
            db.setTransactionSuccessful();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }finally {
            db.endTransaction();
        }
    }


    private T queryById(Class clazz, T id) {
        beginTransaction();
        try {
            Dao<T, Object> dao = getHelper().getDao(clazz);
            T t=dao.queryForId(id);
            db.setTransactionSuccessful();
            return t;
        } catch (SQLException e) {
            e.printStackTrace();
            Log.i("", "null in byid");
            return null;
        }finally {
            db.endTransaction();
        }
    }

    private List<T> queryBywhere(Class clazz, String columnName, T whereValue) {
        beginTransaction();
        try {
            List<T> list;
            Dao<T, Object> dao = getHelper().getDao(clazz);
            QueryBuilder queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq(columnName, whereValue);
            list = queryBuilder.query();
            db.setTransactionSuccessful();
            return list;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }finally {
            db.endTransaction();
        }
    }

    private int create(Class clazz, T obj) {
        beginTransaction();
        try {
            Dao<T, ?> dao = (Dao<T, ?>) getHelper().getDao(clazz);
            int createReturnValue=dao.create(obj);
            db.setTransactionSuccessful();
            return createReturnValue;

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        finally {
            db.endTransaction();
        }
    }


    private int deleteRecordsById(Class clazz, T obj) {
        beginTransaction();
        try {
            Dao<T, Object> dao = getHelper().getDao(clazz);
            int deleteCount=dao.deleteById(obj);
            db.setTransactionSuccessful();
            return deleteCount;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }finally {
            db.endTransaction();
        }
    }

    private int deleteByWhere(Class clazz, String columnName, T obj) {
        beginTransaction();
        try {
            Dao<T, Object> dao = getHelper().getDao(clazz);
            DeleteBuilder deleteBuilder = dao.deleteBuilder();
            deleteBuilder.where().eq(columnName, obj);
            deleteBuilder.delete();
            int deleteCount= dao.delete(deleteBuilder.prepare());
            db.setTransactionSuccessful();
            return deleteCount;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }finally {
            db.endTransaction();
        }
    }

    private int deleteAllRecords(Class clazz) {
        beginTransaction();
        try {
            Dao<T, Object> dao = getHelper().getDao(clazz);

            List<T> list = dao.queryForAll();
            int deleteCount= dao.delete(list);
            db.setTransactionSuccessful();
            return deleteCount;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
        return 0;
    }

    private int updateByWhere(Class clazz, T obj, String whereCauseColumnName, T whereCuaseValue, String columnToBeUpdate) {
        beginTransaction();
        try {

            Dao<T, Object> dao = getHelper().getDao(clazz);
            UpdateBuilder updateBuilder = dao.updateBuilder();
            updateBuilder.updateColumnValue(columnToBeUpdate, obj);
            updateBuilder.where().eq(whereCauseColumnName, whereCuaseValue);
            int row = dao.update(updateBuilder.prepare());
            db.setTransactionSuccessful();
            return row;

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
        return 0;
    }

    private int updateAll(Class clazz, String columnToBeUpdate, T valueToBeSet) {
        beginTransaction();
        try {
            Dao<T, Object> dao = getHelper().getDao(clazz);

            UpdateBuilder updateBuilder = dao.updateBuilder();
            updateBuilder.updateColumnValue(columnToBeUpdate, valueToBeSet);
            int row = dao.update(updateBuilder.prepare());
            db.setTransactionSuccessful();
            return row;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
        return 0;
    }

}
