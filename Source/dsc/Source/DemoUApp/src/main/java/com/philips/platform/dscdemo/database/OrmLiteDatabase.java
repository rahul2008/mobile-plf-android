package com.philips.platform.dscdemo.database;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class OrmLiteDatabase implements OrmDatabase {
    private ConnectionSource connectionSource;

    public OrmLiteDatabase(ConnectionSource connectionSource) {

        this.connectionSource = connectionSource;
    }

    @Override
    public void createTable(final Class<?> dataClass) throws SQLException {
        TableUtils.createTable(connectionSource, dataClass);

    }
}
