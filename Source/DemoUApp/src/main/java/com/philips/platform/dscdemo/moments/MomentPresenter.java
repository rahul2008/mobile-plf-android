/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.dscdemo.moments;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.ListPopupWindow;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.philips.platform.core.datatypes.DSPagination;
import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementDetail;
import com.philips.platform.core.datatypes.MeasurementGroup;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.listeners.SynchronisationCompleteListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.dscdemo.DemoAppManager;
import com.philips.platform.dscdemo.R;
import com.philips.platform.dscdemo.database.DatabaseHelper;
import com.philips.platform.dscdemo.database.EmptyForeignCollection;
import com.philips.platform.dscdemo.database.datatypes.MeasurementDetailType;
import com.philips.platform.dscdemo.database.datatypes.MeasurementGroupDetailType;
import com.philips.platform.dscdemo.database.datatypes.MeasurementType;
import com.philips.platform.dscdemo.database.datatypes.MomentDetailType;
import com.philips.platform.dscdemo.database.datatypes.MomentType;
import com.philips.platform.dscdemo.database.table.OrmMoment;
import com.philips.platform.dscdemo.database.table.OrmSynchronisationData;

import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

class MomentPresenter {
    private Context mContext;

    private static final int DELETE = 0;
    private static final int UPDATE = 1;
    static final int ADD = 2;

    private final DBRequestListener<Moment> dbRequestListener;
    private DataServicesManager mDataServices;
    private DatabaseHelper databaseHelper;

    private Measurement mMeasurement;
    private MeasurementGroup mMeasurementGroup;
    private MeasurementGroup mMeasurementGroupInside;

    private EditText mEtMomentType;
    private EditText mTemperature;
    private EditText mLocation;
    private EditText mPhase;
    private Button mDialogButton;

    MomentPresenter(Context context, DBRequestListener<Moment> dbRequestListener) {
        mDataServices = DataServicesManager.getInstance();
        databaseHelper = DemoAppManager.getInstance().getDatabaseHelper();
        mContext = context;
        this.dbRequestListener = dbRequestListener;
    }

    private Moment createMoment(String type, String momemtDetail, String measurement, String measurementDetail) {
        Moment moment = mDataServices.createMoment(type);
        createMomentDetail(momemtDetail, moment);

        createMeasurementGroup(moment);
        createMeasurementGroupDetail(measurementDetail);

        createMeaurementGroupInsideMeasurementGroup(measurement, measurementDetail);
        mMeasurementGroupInside.addMeasurement(mMeasurement);
        mMeasurementGroup.addMeasurementGroup(mMeasurementGroupInside);
        moment.addMeasurementGroup(mMeasurementGroup);
        return moment;
    }

    private void createMeaurementGroupInsideMeasurementGroup(String measurement, String measurementDetail) {
        mMeasurementGroupInside = mDataServices.
                createMeasurementGroup(mMeasurementGroup);
        createMeasurement(mMeasurementGroupInside, measurement);
        createMeasurementDetail(measurementDetail);
    }

    private void createMeasurementDetail(String value) {
        mDataServices.createMeasurementDetail(MeasurementDetailType.LOCATION, value, mMeasurement);
    }

    private void createMeasurement(MeasurementGroup group, String value) {
        mMeasurement = mDataServices.createMeasurement(MeasurementType.TEMPERATURE, value, "celsius", group);
    }

    private void createMomentDetail(String value, Moment moment) {
        mDataServices.
                createMomentDetail(MomentDetailType.PHASE, value, moment);
    }

    private void createMeasurementGroupDetail(String value) {
        mDataServices.
                createMeasurementGroupDetail(MeasurementGroupDetailType.TEMP_OF_DAY, value, mMeasurementGroup);
    }

    private void createMeasurementGroup(Moment moment) {
        mMeasurementGroup = mDataServices.
                createMeasurementGroup(moment);
    }

    void fetchData(DBFetchRequestListner<Moment> dbFetchRequestListner) {
        mDataServices.fetchAllMoment(dbFetchRequestListner);
    }

    void fetchLatestMoment(String type, DBFetchRequestListner<Moment> dbFetchRequestListener) {
        mDataServices.fetchLatestMomentByType(type, dbFetchRequestListener);
    }

    void fetchMomentByDateRange(Date startDate, Date endDate, DSPagination paginationModel, DBFetchRequestListner<Moment> dbFetchRequestListener) {
        mDataServices.fetchMomentsWithTimeLine(startDate, endDate,paginationModel, dbFetchRequestListener);
    }

    void fetchMomentByDateRangeAndType(String momentType, Date startDate, Date endDate,DSPagination paginationModel,DBFetchRequestListner<Moment> dbFetchRequestListener){
        mDataServices.fetchMomentsWithTypeAndTimeLine(momentType,startDate,endDate,paginationModel,dbFetchRequestListener);
    }



    private void saveRequest(Moment moment) {
        if (moment.getCreatorId() == null || moment.getSubjectId() == null) {
            Toast.makeText(mContext, "Please Login again", Toast.LENGTH_SHORT).show();
        } else {
            List<Moment> moments = new ArrayList<>();
            moments.add(moment);
            mDataServices.saveMoments(moments, dbRequestListener);
        }
    }

    private void createAndSaveMoment(String type) {
        Moment moment;
        moment = createMoment(type, mPhase.getText().toString(),
                mTemperature.getText().toString(), mLocation.getText().toString());
        saveRequest(moment);
    }

    void bindDeleteOrUpdatePopUp(final List<? extends Moment> data, final View view,
                                 final int selectedItem) {

        String delete = mContext.getResources().getString(R.string.delete);
        String update = mContext.getResources().getString(R.string.update);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, android.R.id.text1);
        arrayAdapter.add(delete);
        arrayAdapter.add(update);

        final ListPopupWindow popupWindow = new ListPopupWindow(mContext);
        popupWindow.setAdapter(arrayAdapter);
        popupWindow.setAnchorView(view);
        popupWindow.setWidth(400);
        popupWindow.setHeight(400);
        popupWindow.setModal(true);

        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent,
                                    final View view, final int position, final long id) {
                switch (position) {
                    case DELETE:
                        removeMoment(data, selectedItem);
                        popupWindow.dismiss();
                        break;
                    case UPDATE:
                        final MomentHelper helper = new MomentHelper();
                        if (String.valueOf(helper.getTemperature(data.get(selectedItem))).equalsIgnoreCase("default")) {
                            Toast.makeText(mContext,
                                    "Invalid", Toast.LENGTH_SHORT).show();
                        } else {
                            addOrUpdateMoment(UPDATE, data.get(selectedItem), false);
                        }
                        popupWindow.dismiss();
                        break;
                    default:
                }
            }
        });
        popupWindow.show();
    }

    private void removeMoment(final List<? extends Moment> data, int adapterPosition) {
        try {
            Moment moment = data.get(adapterPosition);
            Dao<OrmSynchronisationData, Integer> ormSynchronisationDataDao = databaseHelper.getSynchronisationDataDao();
            ormSynchronisationDataDao.refresh((OrmSynchronisationData) moment.getSynchronisationData());

            Dao<OrmMoment, Integer> momentDao = databaseHelper.getMomentDao();
            momentDao.refresh((OrmMoment) moment);

            mDataServices.deleteMoment(moment, dbRequestListener);

        } catch (ArrayIndexOutOfBoundsException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateMoment(OrmMoment old) {
        String momentDetail = mPhase.getText().toString();
        String meausrementValue = mTemperature.getText().toString();
        String measurementDetailValue = mLocation.getText().toString();

        Collection<? extends MomentDetail> momentDetails = old.getMomentDetails();

        for (MomentDetail detail : momentDetails) {
            detail.setValue(momentDetail);
        }

        Collection<? extends MeasurementGroup> measurementGroups = old.getMeasurementGroups();
        for (MeasurementGroup measurementGroup : measurementGroups) {
            Collection<? extends MeasurementGroup> measurementGroupsInside = measurementGroup.getMeasurementGroups();
            Collection<MeasurementGroup> measurementGroupsOutput = new EmptyForeignCollection<>();
            for (MeasurementGroup measurementgroupInside : measurementGroupsInside) {
                Collection<? extends Measurement> measurements = measurementgroupInside.getMeasurements();
                for (Measurement measurement : measurements) {
                    measurement.setValue(meausrementValue);

                    Collection<? extends MeasurementDetail> measurementDetails = measurement.getMeasurementDetails();
                    for (MeasurementDetail measurementDetail : measurementDetails) {
                        measurementDetail.setValue(measurementDetailValue);
                    }
                }
                measurementGroupsOutput.add(measurementgroupInside);
            }
            measurementGroup.setMeasurementGroups(measurementGroupsOutput);
        }
        mDataServices.updateMoment(old, dbRequestListener);
    }

    void addOrUpdateMoment(final int addOrUpdate, final Moment moment, final boolean isTypeAvailable) {
        final Dialog dialog = new Dialog(mContext);

        if (isTypeAvailable) {
            dialog.setContentView(R.layout.create_moment);
            mEtMomentType = (EditText) dialog.findViewById(R.id.et_moment_type);
        } else {
            dialog.setContentView(R.layout.af_datasync_create_moment_pop_up);
        }

        dialog.setTitle(mContext.getResources().getString(R.string.create_moment));

        mTemperature = (EditText) dialog.findViewById(R.id.temperature_detail);
        mLocation = (EditText) dialog.findViewById(R.id.location_detail);
        mPhase = (EditText) dialog.findViewById(R.id.phase_detail);
        mDialogButton = (Button) dialog.findViewById(R.id.save);
        mDialogButton.setEnabled(false);

        if (addOrUpdate == UPDATE) {
            final MomentHelper helper = new MomentHelper();
            mTemperature.setText(String.valueOf(helper.getTemperature(moment)));
            mLocation.setText(helper.getNotes(moment));
            mPhase.setText(helper.getTime(moment));
            mDialogButton.setText(R.string.update);
        }

        mDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final boolean isValid = validateInputFields();
                if (!isValid) {
                    mTemperature.setText("");
                    Toast.makeText(mContext,
                            R.string.invalid_temperature, Toast.LENGTH_SHORT).show();
                    return;
                }

                switch (addOrUpdate) {
                    case ADD:
                        dialog.dismiss();
                        if (isTypeAvailable)
                            createAndSaveMoment(mEtMomentType.getText().toString().trim());
                        else
                            createAndSaveMoment(MomentType.TEMPERATURE);
                        break;
                    case UPDATE:
                        dialog.dismiss();
                        try {
                            Dao<OrmSynchronisationData, Integer> ormSynchronisationDataDao = databaseHelper.getSynchronisationDataDao();
                            ormSynchronisationDataDao.refresh((OrmSynchronisationData) moment.getSynchronisationData());
                            Dao<OrmMoment, Integer> momentDao = databaseHelper.getMomentDao();
                            momentDao.refresh((OrmMoment) moment);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        updateMoment((OrmMoment) moment);
                        break;
                }
            }
        });

        textChageListener(mPhase);
        textChageListener(mTemperature);
        textChageListener(mLocation);

        dialog.show();
    }

    private void textChageListener(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s,
                                          final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s,
                                      final int start, final int before, final int count) {
                if (isDialogButtonEnabled()) {
                    mDialogButton.setEnabled(true);
                } else {
                    mDialogButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(final Editable s) {

            }
        });
    }

    private boolean isDialogButtonEnabled() {
        return !mPhase.getText().toString().isEmpty() && !mTemperature.getText().toString().isEmpty() && !mLocation.getText().toString().isEmpty();
    }

    private boolean validateInputFields() {
        String temperature = mTemperature.getText().toString();
        try {
            Double.valueOf(temperature);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void fetchSyncByDateRange(DateTime startDate, DateTime endDate, SynchronisationCompleteListener syncCompleteListener) {
        mDataServices.synchronizeWithFetchByDateRange(startDate, endDate, syncCompleteListener);
    }
}
