package com.philips.platform.database;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.philips.platform.securedblibrary.SecureDbOrmLiteSqliteOpenHelper;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;


/**
 * Created by 310273508 on 1/12/2017.
 */

public class SecureDataBaseQueryHelper<T> {

    private static SimpleDateFormat df;
    public SecureDbOrmLiteSqliteOpenHelper secureDataBaseHelper;
    String insertStartTime = null;
    private Context context;
    private String dataBasePassword;

    public SecureDataBaseQueryHelper(Context context, SecureDbOrmLiteSqliteOpenHelper secureDataBaseHelper) {
        this.context = context;
        this.secureDataBaseHelper = secureDataBaseHelper;
        df = new SimpleDateFormat("HH:mm:ss.SSS a", Locale.ENGLISH);

    }

    public SecureDataBaseQueryHelper(Context context, SecureDbOrmLiteSqliteOpenHelper secureDataBaseHelper, String dataBasePassword) {
        this.context = context;
        this.secureDataBaseHelper = secureDataBaseHelper;
        this.dataBasePassword = dataBasePassword;
        df = new SimpleDateFormat("HH:mm:ss.SSS a", Locale.ENGLISH);
        try {
            getHelper().getWriteDbPermission();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private SecureDbOrmLiteSqliteOpenHelper getHelper() {
        return secureDataBaseHelper;
    }



    public void beginTransaction() {
        try {
            getHelper().getWriteDbPermission().beginTransaction();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void endTransaction() {
        try {
            getHelper().getWriteDbPermission().endTransaction();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setTransactionSuccessful() {
        try {
            getHelper().getWriteDbPermission().setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean inTransaction() {
        try {
            return getHelper().getWriteDbPermission().inTransaction();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }


    public void createOrInsert(Class clazz, T obj) {
        try {
            getHelper().getWriteDbPermission();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        create(clazz, obj);
    }

    public int deleteAll(Class clazz) {
        try {
            getHelper().getWriteDbPermission();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deleteAllRecords(clazz);

    }

    public int deleteById(Class clazz, T obj) {
        try {
            getHelper().getWriteDbPermission();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deleteRecordsById(clazz, obj);
    }


    public int updateAllRecords(Class clazz, String columnToBeUpdate, T valueToBeSet) {
        try {
            getHelper().getWriteDbPermission();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updateAll(clazz, columnToBeUpdate, valueToBeSet);
    }

    public int updateRecordByWhere(Class clazz, T obj, String whereCauseColumnName, T whereCauseValue, String columnToBeUpdate) {
        try {
            getHelper().getWriteDbPermission();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updateByWhere(clazz, obj, whereCauseColumnName, whereCauseValue, columnToBeUpdate);
    }

    public List retrieveByQuery(Class clazz, String columnName, T obj) {
        try {
            getHelper().getWriteDbPermission();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return queryBywhere(clazz, columnName, obj);
    }

    public T retrieveById(Class clazz, T obj) {
        try {
            getHelper().getWriteDbPermission();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return queryById(clazz, obj);
    }

    public List retrieveAll(Class<T> clazz) {
        try {
            getHelper().getWriteDbPermission();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return queryForAll(clazz);
    }

    public void close() {
        getHelper().close();
    }

    private List queryForAll(Class clazz) {
        String readStartTime = getLocalTimestamp();
        Log.d("Read Start Time: ", "Reading .." + getLocalTimestamp());
        beginTransaction();

        try {
            Dao<T, ?> dao = getHelper().getDao(clazz);
            List<T> list = dao.queryForAll();
            setTransactionSuccessful();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        } finally {
            endTransaction();
            Log.d("Read End Time: ", "Read End .." + getLocalTimestamp());
            String readEndTime = getLocalTimestamp();
            getFinalTime(readStartTime, readEndTime, "Read Time");
        }
    }


    private T queryById(Class clazz, T id) {
        String readStartTime = getLocalTimestamp();
        Log.d("Read Start Time: ", "Reading .." + getLocalTimestamp());
        beginTransaction();
        try {
            Dao<T, Object> dao = getHelper().getDao(clazz);
            T t = dao.queryForId(id);
            setTransactionSuccessful();
            return t;
        } catch (SQLException e) {
            e.printStackTrace();
            Log.i("", "null in byid");
            return null;
        } finally {
            endTransaction();
            Log.d("Read End Time: ", "Read End .." + getLocalTimestamp());
            String readEndTime = getLocalTimestamp();
            getFinalTime(readStartTime, readEndTime, "Read Time");
        }
    }

    private List<T> queryBywhere(Class clazz, String columnName, T whereValue) {
        String readStartTime = getLocalTimestamp();
        Log.d("Read Start Time: ", "Reading .." + getLocalTimestamp());
        beginTransaction();
        try {
            List<T> list;
            Dao<T, Object> dao = getHelper().getDao(clazz);
            QueryBuilder queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq(columnName, whereValue);
            list = queryBuilder.query();
            setTransactionSuccessful();
            return list;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            endTransaction();
            Log.d("Read End Time: ", "Read End .." + getLocalTimestamp());
            String readEndTime = getLocalTimestamp();
            getFinalTime(readStartTime, readEndTime, "Read Time");
        }
    }

    private int create(Class clazz, T obj) {
        String insertStartTime = getLocalTimestamp();
        Log.d("Insert Start Time: ", "Inserting .." + getLocalTimestamp());
        beginTransaction();
        try {
            Dao<T, ?> dao = (Dao<T, ?>) getHelper().getDao(clazz);
            int createReturnValue = dao.create(obj);
            setTransactionSuccessful();
            return createReturnValue;

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            endTransaction();
            Log.d("Insert End Time: ", "Insert End .." + getLocalTimestamp());
            String insertEndTime = getLocalTimestamp();
            getFinalTime(insertStartTime, insertEndTime, "InsertTime");
        }
    }


    public int bulkInsert() {


        try {
            final Dao<T, ?> dao = (Dao<T, ?>) getHelper().getDao(AddressBook.class);

            TransactionManager.callInTransaction(dao.getConnectionSource(),
                    new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {

                            List<AddressBook> addressBooksList = new ArrayList<>();
                            for (int i = 0; i < 1000; i++) {
                                addressBooksList.add(new AddressBook("A", "AZ", "BANGALORE", "98484848488"));
                            }

                            insertStartTime = getLocalTimestamp();
                            Log.d("Insert Start Time: ", "Inserting .." + getLocalTimestamp());
                            for (AddressBook c : addressBooksList) {
                                beginTransaction();
                                dao.createOrUpdate((T) c);
                            }
                            return null;
                        }
                    });


        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            endTransaction();
            Log.d("Insert End Time: ", "Insert End .." + getLocalTimestamp());
            String insertEndTime = getLocalTimestamp();
            getFinalTime(insertStartTime, insertEndTime, "InsertTime");
        }
        return 0;
    }


    private int deleteRecordsById(Class clazz, T obj) {
        String deleteStartTime = getLocalTimestamp();
        Log.d("Delete Start Time: ", "Deleting  .." + getLocalTimestamp());
        beginTransaction();
        try {
            Dao<T, Object> dao = getHelper().getDao(clazz);
            int deleteCount = dao.deleteById(obj);
            setTransactionSuccessful();
            return deleteCount;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            endTransaction();
            Log.d("Delete End Time: ", "Delete End .." + getLocalTimestamp());
            String deleteEndTime = getLocalTimestamp();
            getFinalTime(deleteStartTime, deleteEndTime, "DeleteTime");
        }
    }

    private int deleteByWhere(Class clazz, String columnName, T obj) {
        String deleteStartTime = getLocalTimestamp();
        Log.d("Delete Start Time: ", "Deleting .." + getLocalTimestamp());
        beginTransaction();
        try {
            Dao<T, Object> dao = getHelper().getDao(clazz);
            DeleteBuilder deleteBuilder = dao.deleteBuilder();
            deleteBuilder.where().eq(columnName, obj);
            deleteBuilder.delete();
            int deleteCount = dao.delete(deleteBuilder.prepare());
            setTransactionSuccessful();
            return deleteCount;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            endTransaction();
            Log.d("Delete End Time: ", "Delete End .." + getLocalTimestamp());
            String deleteEndTime = getLocalTimestamp();
            getFinalTime(deleteStartTime, deleteEndTime, "DeleteTime");
        }
    }

    private int deleteAllRecords(Class clazz) {
        String deleteStartTime = getLocalTimestamp();
        Log.d("Delete Start Time: ", "Deleting .." + getLocalTimestamp());
        beginTransaction();
        try {
            Dao<T, Object> dao = getHelper().getDao(clazz);

            List<T> list = dao.queryForAll();
            int deleteCount = dao.delete(list);
            setTransactionSuccessful();
            return deleteCount;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            endTransaction();
            Log.d("Delete End Time: ", "Delete End .." + getLocalTimestamp());
            String deleteEndTime = getLocalTimestamp();
            getFinalTime(deleteStartTime, deleteEndTime, "DeleteTime");
        }
        return 0;
    }

    private int updateByWhere(Class clazz, T obj, String whereCauseColumnName, T whereCuaseValue, String columnToBeUpdate) {
        String updateStartTime = getLocalTimestamp();
        Log.d("Update Start Time: ", "Updating .." + getLocalTimestamp());
        beginTransaction();
        try {

            Dao<T, Object> dao = getHelper().getDao(clazz);
            UpdateBuilder updateBuilder = dao.updateBuilder();
            updateBuilder.updateColumnValue(columnToBeUpdate, obj);
            updateBuilder.where().eq(whereCauseColumnName, whereCuaseValue);
            int row = dao.update(updateBuilder.prepare());
            setTransactionSuccessful();
            return row;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            endTransaction();
            Log.d("Update End Time: ", "Update End .." + getLocalTimestamp());
            String updateEndTime = getLocalTimestamp();
            getFinalTime(updateStartTime, updateEndTime, "UpdateTime");
        }
        return 0;
    }

    private int updateAll(Class clazz, String columnToBeUpdate, T valueToBeSet) {
        String updateStartTime = getLocalTimestamp();
        Log.d("Updating Start Time: ", "Updating .." + getLocalTimestamp());
        beginTransaction();
        try {
            Dao<T, Object> dao = getHelper().getDao(clazz);

            UpdateBuilder updateBuilder = dao.updateBuilder();
            updateBuilder.updateColumnValue(columnToBeUpdate, valueToBeSet);
            int row = dao.update(updateBuilder.prepare());
            setTransactionSuccessful();
            return row;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            endTransaction();
            Log.d("Update End Time: ", "Update End .." + getLocalTimestamp());
            String updateEndTime = getLocalTimestamp();
            getFinalTime(updateStartTime, updateEndTime, "UpdateTime");
        }
        return 0;
    }

    private String getLocalTimestamp() {

        String mLocalTimestamp;
        Calendar c = Calendar.getInstance();
        mLocalTimestamp = df.format(c.getTime());
        return mLocalTimestamp;
    }

    private void getFinalTime(String start, String end, String TAG) {
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = df.parse(start);
            endDate = df.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diffInMs = endDate.getTime() - startDate.getTime();

        long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMs);

        Log.i(TAG, "  Time taken in InMs: " + diffInMs);
        Log.i(TAG, "  Time taken in ISec: " + diffInSec);
    }

}
