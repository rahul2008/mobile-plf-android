package com.philips.platform.dscdemo.database;

import com.j256.ormlite.dao.Dao;
import com.philips.platform.securedblibrary.SecureDbOrmLiteSqliteOpenHelper;

import java.sql.SQLException;

public class OrmDaoProvider implements DaoProvider {
    SecureDbOrmLiteSqliteOpenHelper helper;

    OrmDaoProvider(SecureDbOrmLiteSqliteOpenHelper helper) {
        this.helper = helper;
    }

    @Override
    public Dao getOrmDao(final Class<?> clazz) throws SQLException {
        return helper.getDao(clazz);
    }
}
