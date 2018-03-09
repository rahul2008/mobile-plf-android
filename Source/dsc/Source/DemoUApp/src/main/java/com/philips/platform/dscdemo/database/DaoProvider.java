package com.philips.platform.dscdemo.database;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

interface DaoProvider {
    Dao getOrmDao(final Class<?> clazz) throws SQLException;
}
