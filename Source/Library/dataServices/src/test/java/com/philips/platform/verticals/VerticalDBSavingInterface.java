package com.philips.platform.verticals;

import com.philips.platform.core.datatypes.UserCharacteristics;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;
import com.philips.platform.core.listeners.DBRequestListener;

import java.sql.SQLException;

/**
 * Created by 310218660 on 1/2/2017.
 */

public class VerticalDBSavingInterface implements DBSavingInterface{


    @Override
    public boolean saveMoment(Moment moment, DBRequestListener dbRequestListener) throws SQLException {
        return false;
    }

    @Override
    public boolean saveConsent(Consent consent, DBRequestListener dbRequestListener) throws SQLException {
        return false;
    }

    @Override
    public void postError(Exception e, DBRequestListener dbRequestListener) {

    }

    @Override
    public boolean saveUserCharacteristics(UserCharacteristics userCharacteristics, DBRequestListener dbRequestListener) throws SQLException {
        return false;
    }
}
