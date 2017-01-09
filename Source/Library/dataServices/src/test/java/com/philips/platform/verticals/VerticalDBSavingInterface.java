package com.philips.platform.verticals;

import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;

import java.sql.SQLException;

/**
 * Created by 310218660 on 1/2/2017.
 */

public class VerticalDBSavingInterface implements DBSavingInterface{
    @Override
    public boolean saveMoment(Moment moment) throws SQLException {
        return true;
    }

    @Override
    public boolean saveConsent(Consent consent) throws SQLException {
        return true;
    }

    @Override
    public boolean saveUserCharacteristics(Characteristics characteristics) throws SQLException {
        return true;
    }

    @Override
    public void postError(Exception e) {

    }
}
