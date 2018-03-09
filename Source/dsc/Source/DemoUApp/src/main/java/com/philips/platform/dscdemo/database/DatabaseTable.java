package com.philips.platform.dscdemo.database;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

interface DatabaseTable {
    void createTable(ConnectionSource connectionSource, Class<?> dataClass) throws SQLException;
}
