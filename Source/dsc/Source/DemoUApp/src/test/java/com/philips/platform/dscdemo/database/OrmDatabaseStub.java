package com.philips.platform.dscdemo.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class OrmDatabaseStub implements OrmDatabase {
    public List<Class<?>> dataClasses = new ArrayList<>();
    @Override
    public void createTable(final Class<?> dataClass) throws SQLException {
        dataClasses.add(dataClass);
    }
}
