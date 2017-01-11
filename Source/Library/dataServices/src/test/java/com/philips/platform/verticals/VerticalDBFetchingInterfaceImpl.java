package com.philips.platform.verticals;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.listeners.DBRequestListener;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by 310218660 on 1/2/2017.
 */

public class VerticalDBFetchingInterfaceImpl implements DBFetchingInterface{
    @Override
    public void fetchMoments(DBRequestListener dbRequestListener) throws SQLException {

    }

    @Override
    public void fetchMoments(@NonNull String type,DBRequestListener dbRequestListener) throws SQLException {

    }

    @Override
    public void fetchMoments(DBRequestListener dbRequestListener,@NonNull Object... types) throws SQLException {

    }

    @Override
    public void fetchLastMoment(String type,DBRequestListener dbRequestListener) throws SQLException {

    }

    @Override
    public Object fetchMomentByGuid(@NonNull String guid) throws SQLException {
        return null;
    }

    @Override
    public List<?> fetchNonSynchronizedMoments() throws SQLException {
        return null;
    }

    @Override
    public Object fetchMomentById(int id, DBRequestListener dbRequestListener) throws SQLException {
        return null;
    }

    @Override
    public Map<Class, List<?>> putConsentForSync(Map<Class, List<?>> dataToSync) throws SQLException {
        return null;
    }

    @Override
    public void fetchConsents(DBRequestListener dbRequestListener) throws SQLException {

    }

    @Override
    public Consent fetchConsent(DBRequestListener dbRequestListener) throws SQLException {
        return null;
    }

    @Override
    public void postError(Exception e, DBRequestListener dbRequestListener) {

    }
}
