package com.philips.platform.dscdemo.database;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class DatabaseTableStub implements DatabaseTable {
    public List<Class<?>> dataClasses = new ArrayList<>();
    @Override
    public void createTable(final ConnectionSource connectionSource, final Class<?> dataClass) throws SQLException {
        dataClasses.add(dataClass);
    }
}
