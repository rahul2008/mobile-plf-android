package com.philips.platform.dscdemo.database;

import java.sql.SQLException;

interface OrmDatabase {
    void createTable(Class<?> dataClass) throws SQLException;
}
