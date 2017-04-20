package com.philips.platform.verticals;

import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.listeners.DBRequestListener;

import java.sql.SQLException;
import java.util.List;

public class VerticalDBUpdatingInterfaceImpl implements DBUpdatingInterface {

    @Override
    public void updateMoment(final Moment ormMoment, final DBRequestListener dbRequestListener) throws SQLException {

    }

    @Override
    public boolean updateMoments(List<Moment> ormMoments, DBRequestListener<Moment> dbRequestListener) throws SQLException {
        return false;
    }

    @Override
    public boolean updateConsent(List<? extends ConsentDetail> consentDetails, DBRequestListener<ConsentDetail> dbRequestListener) throws SQLException {
        return false;
    }

    @Override
    public void updateFailed(Exception e, DBRequestListener dbRequestListener) {

    }

    @Override
    public boolean updateCharacteristics(List<Characteristics> userCharacteristics, DBRequestListener<Characteristics> dbRequestListener) throws SQLException {
        return false;
    }

    @Override
    public void updateSettings(Settings settings, DBRequestListener dbRequestListener) throws SQLException {

    }

    @Override
    public boolean updateSyncBit(int tableID, boolean isSynced) throws SQLException {
        return false;
    }

    @Override
    public boolean updateInsights(List<? extends Insight> insights, DBRequestListener<Insight> dbRequestListener) throws SQLException {
        return false;
    }

}
