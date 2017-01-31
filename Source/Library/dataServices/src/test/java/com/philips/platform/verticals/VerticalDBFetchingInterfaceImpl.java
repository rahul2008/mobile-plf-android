package com.philips.platform.verticals;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.datatypes.UserCharacteristics;
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
    public void fetchMoments(@NonNull String type, DBRequestListener dbRequestListener) throws SQLException {

    }

    @Override
    public void fetchMoments(DBRequestListener dbRequestListener, @NonNull Object... types) throws SQLException {

    }

    @Override
    public void fetchLastMoment(String type, DBRequestListener dbRequestListener) throws SQLException {

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
    public void fetchConsents(DBRequestListener dbRequestListener) throws SQLException {

    }

    @Override
    public Map<Class, List<?>> putUserCharacteristicsForSync(Map<Class, List<?>> dataToSync) throws SQLException {
        return null;
    }

    @Override
    public Consent fetchConsent(DBRequestListener dbRequestListener) throws SQLException {
        return null;
    }

    @Override
    public void postError(Exception e, DBRequestListener dbRequestListener) {

    }

    @Override
    public void fetchUserCharacteristics(DBRequestListener dbRequestListener) throws SQLException {

    }

    @Override
    public List<?> fetchNonSyncConsentDetails() throws SQLException {
        return null;
    }

    @Override
    public UserCharacteristics fetchUCByCreatorId(@NonNull String creatorId) throws SQLException {
        return null;
    }

    @Override
    public Settings fetchSettings(DBRequestListener dbRequestListener) throws SQLException {
        return null;
    }

    @Override
    public Settings fetchSettings() throws SQLException {
        return null;
    }

    @Override
    public List<?> fetchNonSyncSettings() throws SQLException {
        return null;
    }

    @Override
    public List<?> fetchNonSyncConsents() throws SQLException {
        return null;
    }

    @Override
    public boolean isSynced(int tableID) throws SQLException {
        return false;
    }

    @Override
    public void fetchCharacteristics(DBRequestListener dbRequestListener) throws SQLException {

    }
}
