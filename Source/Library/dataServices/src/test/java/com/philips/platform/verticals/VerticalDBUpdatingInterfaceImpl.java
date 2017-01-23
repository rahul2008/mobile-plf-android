package com.philips.platform.verticals;

import com.philips.platform.core.datatypes.UserCharacteristics;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.listeners.DBRequestListener;

import java.sql.SQLException;

/**
 * Created by 310218660 on 1/2/2017.
 */

public class VerticalDBUpdatingInterfaceImpl implements DBUpdatingInterface {


    @Override
    public void updateMoment(Moment ormMoment, DBRequestListener dbRequestListener) throws SQLException {

    }

    @Override
    public boolean updateConsent(Consent consent, DBRequestListener dbRequestListener) throws SQLException {
        return false;
    }

    @Override
    public void updateFailed(Exception e, DBRequestListener dbRequestListener) {

    }

    @Override
    public boolean updateCharacteristics(UserCharacteristics userCharacteristics, DBRequestListener dbRequestListener) throws SQLException {
        return false;
    }

    @Override
    public boolean setSynced(String creatorID, boolean isSynced) throws SQLException {
        return false;
    }
//
//    @Override
//    public void processCharacteristicsReceivedFromDataCore(UserCharacteristics userCharacteristics, DBRequestListener dbRequestListener) throws SQLException {
//
//    }
}
