package com.philips.platform.dscdemo.database;

import java.sql.SQLException;

interface Database {
    void createTable(Class<?> dataClass) throws SQLException;
}
