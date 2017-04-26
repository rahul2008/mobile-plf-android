package com.philips.platform.verticals;

import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;
import com.philips.platform.core.listeners.DBRequestListener;

import java.sql.SQLException;
import java.util.List;

public class VerticalDBSavingInterface implements DBSavingInterface {


    @Override
    public boolean saveMoment(Moment moment, DBRequestListener dbRequestListener) throws SQLException {
        return false;
    }

    @Override
    public boolean saveMoments(List<Moment> moments, DBRequestListener<Moment> dbRequestListener) throws SQLException {
        return false;
    }

    @Override
    public boolean saveConsentDetails(List<ConsentDetail> consentDetails, DBRequestListener<ConsentDetail> dbRequestListener) throws SQLException {
        return false;
    }


    @Override
    public void postError(Exception e, DBRequestListener dbRequestListener) {

    }

    @Override
    public boolean saveUserCharacteristics(List<Characteristics> userCharacteristics, DBRequestListener<Characteristics> dbRequestListener) throws SQLException {
        return false;
    }

    @Override
    public boolean saveInsights(List<Insight> insights, DBRequestListener<Insight> dbRequestListener) throws SQLException {
        return false;
    }

    @Override
    public boolean saveSyncBit(SyncType type, boolean isSynced) throws SQLException {
        return false;
    }

    @Override
    public boolean saveSettings(Settings settings, DBRequestListener dbRequestListener) throws SQLException {
        return false;
    }

}
