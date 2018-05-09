package com.philips.platform.dscdemo.database;

import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;

import java.io.IOException;
import java.sql.SQLException;

public class ConnectionSourceStub implements ConnectionSource {
    @Override
    public void close() throws IOException {

    }

    @Override
    public DatabaseConnection getReadOnlyConnection(final String tableName) throws SQLException {
        return null;
    }

    @Override
    public DatabaseConnection getReadWriteConnection(final String tableName) throws SQLException {
        return null;
    }

    @Override
    public void releaseConnection(final DatabaseConnection connection) throws SQLException {

    }

    @Override
    public boolean saveSpecialConnection(final DatabaseConnection connection) throws SQLException {
        return false;
    }

    @Override
    public void clearSpecialConnection(final DatabaseConnection connection) {

    }

    @Override
    public DatabaseConnection getSpecialConnection(final String tableName) {
        return null;
    }

    @Override
    public void closeQuietly() {

    }

    @Override
    public DatabaseType getDatabaseType() {
        return null;
    }

    @Override
    public boolean isOpen(final String tableName) {
        return false;
    }

    @Override
    public boolean isSingleConnection(final String tableName) {
        return false;
    }
}
