package com.philips.platform.dscdemo.database;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class OrmDatabaseHelper implements DatabaseTable {
    private ConnectionSource connectionSource;

    public OrmDatabaseHelper(ConnectionSource connectionSource) {

        this.connectionSource = connectionSource;
    }

    @Override
    public void createTable(final ConnectionSource connectionSource, final Class<?> dataClass) throws SQLException {
        TableUtils.createTable(connectionSource, dataClass);

    }
}
