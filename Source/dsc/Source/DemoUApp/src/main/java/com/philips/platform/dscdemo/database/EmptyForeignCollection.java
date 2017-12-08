/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.dscdemo.database;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.CloseableWrappedIterable;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class EmptyForeignCollection<T> extends ArrayList<T> implements ForeignCollection<T> {
    static final long serialVersionUID = 11L;
    @Override
    public CloseableIterator<T> iterator(final int flags) {
        return null;
    }

    @Override
    public CloseableIterator<T> closeableIterator(final int flags) {
        return null;
    }

    @Override
    public CloseableIterator<T> iteratorThrow() throws SQLException {
        return null;
    }

    @Override
    public CloseableIterator<T> iteratorThrow(final int flags) throws SQLException {
        return null;
    }

    @Override
    public CloseableWrappedIterable<T> getWrappedIterable() {
        return null;
    }

    @Override
    public CloseableWrappedIterable<T> getWrappedIterable(final int flags) {
        return null;
    }

    @Override
    public void closeLastIterator() {

    }

    @Override
    public boolean isEager() {
        return false;
    }

    @Override
    public int update(final T obj) throws SQLException {
        return 0;
    }

    @Override
    public int updateAll() throws SQLException {
        return 0;
    }

    @Override
    public int refresh(final T obj) throws SQLException {
        return 0;
    }

    @Override
    public int refreshAll() throws SQLException {
        return 0;
    }

    @Override
    public int refreshCollection() throws SQLException {
        return 0;
    }

    @Override
    public Dao<T, ?> getDao() {
        return null;
    }

    @Override
    public CloseableIterator<T> closeableIterator() {
        return null;
    }
}
