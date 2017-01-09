package com.philips.platform.verticals;

import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by 310218660 on 1/2/2017.
 */

public class VerticalDBUpdatingInterfaceImpl implements DBUpdatingInterface{
    @Override
    public int processMoment(int updatedCount, Moment moment) {
        return 0;
    }

    @Override
    public void updateOrSaveMomentInDatabase(Moment ormMoment) {

    }

    @Override
    public Moment getOrmMoment(Moment moment) {
        return null;
    }

    @Override
    public int processMomentsReceivedFromBackend(List<? extends Moment> moments) {
        return 0;
    }

    @Override
    public void processCreatedMoment(List<? extends Moment> moments) {

    }

    @Override
    public void updateFailed(Exception e) {

    }

    @Override
    public boolean updateConsent(Consent consent) throws SQLException {
        return false;
    }

    @Override
    public void updateCharacteristics(Characteristics characteristics) throws SQLException {

    }

    @Override
    public void processCharacteristicsReceivedFromDataCore(Characteristics characteristics) throws SQLException {

    }
}
