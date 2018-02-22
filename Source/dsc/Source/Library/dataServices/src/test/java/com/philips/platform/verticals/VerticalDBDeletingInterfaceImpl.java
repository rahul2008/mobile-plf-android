package com.philips.platform.verticals;

import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.listeners.DBRequestListener;

import java.sql.SQLException;
import java.util.List;

public class VerticalDBDeletingInterfaceImpl implements DBDeletingInterface {
    @Override
    public void deleteAll(DBRequestListener dbRequestListener) {

    }

    @Override
    public boolean deleteMoments(List<Moment> moments, DBRequestListener<Moment> dbRequestListener) throws SQLException {
        return false;
    }


    @Override
    public void markAsInActive(Moment moment, DBRequestListener dbRequestListener) {

    }

    @Override
    public void deleteMoment(Moment moment, DBRequestListener dbRequestListener) {

    }

    @Override
    public void deleteMomentDetail(Moment moment, DBRequestListener dbRequestListener) throws SQLException {

    }

    @Override
    public void deleteMeasurementGroup(Moment moment, DBRequestListener dbRequestListener) throws SQLException {

    }

    @Override
    public void deleteFailed(Exception e, DBRequestListener dbRequestListener) {

    }

    @Override
    public void deleteSyncedMoments(DBRequestListener<Moment> dbRequestListener) throws SQLException {

    }

    @Override
    public void deleteAllExpiredMoments(DBRequestListener<Integer> dbRequestListener) {

    }

    @Override
    public void markMomentsAsInActive(List<Moment> moment, DBRequestListener<Moment> dbRequestListener) throws SQLException {

    }

    @Override
    public boolean markInsightsAsInActive(List<Insight> insights, DBRequestListener<Insight> dbRequestListener) throws SQLException {
        return false;
    }

    @Override
    public boolean deleteInsights(List<Insight> insights, DBRequestListener<Insight> dbRequestListener) throws SQLException {
        return false;
    }

    @Override
    public int deleteSyncBit(SyncType syncType) throws SQLException {
        return 0;
    }

    @Override
    public void deleteUserCharacteristics() throws SQLException {

    }

    @Override
    public void deleteAllMoments(DBRequestListener dbRequestListener) throws SQLException {

    }

    @Override
    public void deleteInsight(Insight insight, DBRequestListener dbRequestListener) throws SQLException {

    }
}
