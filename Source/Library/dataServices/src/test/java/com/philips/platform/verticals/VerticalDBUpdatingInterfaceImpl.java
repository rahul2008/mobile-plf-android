package com.philips.platform.verticals;

import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.listeners.DBRequestListener;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by 310218660 on 1/2/2017.
 */

public class VerticalDBUpdatingInterfaceImpl implements DBUpdatingInterface {

    @Override
    public void updateMoment(final Moment ormMoment, final DBRequestListener dbRequestListener) throws SQLException {

    }

    @Override
    public boolean updateConsent(Consent consent, DBRequestListener dbRequestListener) throws SQLException {
        return false;
    }

    @Override
    public void updateFailed(Exception e, DBRequestListener dbRequestListener) {

    }

    @Override
    public boolean updateCharacteristics(List<Characteristics> userCharacteristics, DBRequestListener dbRequestListener) throws SQLException {
        return false;
    }

    @Override
    public void updateSettings(Settings settings, DBRequestListener dbRequestListener) throws SQLException {

    }

    @Override
    public boolean updateSyncBit(int tableID, boolean isSynced) throws SQLException {
        return false;
    }

}
