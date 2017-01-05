package com.philips.platform.verticals;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by 310218660 on 1/2/2017.
 */

public class VerticalDBFetchingInterfaceImpl implements DBFetchingInterface {
    @Override
    public void fetchMoments() throws SQLException {

    }

    @Override
    public void fetchMoments(@NonNull String type) throws SQLException {

    }

    @Override
    public void fetchMoments(@NonNull Object... types) throws SQLException {

    }

    @Override
    public void fetchLastMoment(String type) throws SQLException {

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
    public Object fetchMomentById(int id) throws SQLException {
        return null;
    }

    @Override
    public Map<Class, List<?>> putMomentsForSync(Map<Class, List<?>> dataToSync) throws SQLException {
        return null;
    }

    @Override
    public Map<Class, List<?>> putConsentForSync(Map<Class, List<?>> dataToSync) throws SQLException {
        return null;
    }

    @Override
    public void fetchConsents() throws SQLException {

    }

    @Override
    public Consent fetchConsent() throws SQLException {
        return null;
    }

    @Override
    public void fetchCharacteristics() throws SQLException {

    }


    @Override
    public void postError(Exception e) {

    }
}
