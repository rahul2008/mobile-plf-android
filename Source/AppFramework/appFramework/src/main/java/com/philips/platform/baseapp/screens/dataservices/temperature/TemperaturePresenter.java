/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.platform.baseapp.screens.dataservices.temperature;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.philips.cdp.uikit.customviews.UIKitListPopupWindow;
import com.philips.cdp.uikit.utils.RowItem;
import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.screens.dataservices.database.DatabaseHelper;
import com.philips.platform.baseapp.screens.dataservices.database.EmptyForeignCollection;
import com.philips.platform.baseapp.screens.dataservices.database.datatypes.MeasurementDetailType;
import com.philips.platform.baseapp.screens.dataservices.database.datatypes.MeasurementGroupDetailType;
import com.philips.platform.baseapp.screens.dataservices.database.datatypes.MeasurementType;
import com.philips.platform.baseapp.screens.dataservices.database.datatypes.MomentDetailType;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMoment;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmSynchronisationData;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementDetail;
import com.philips.platform.core.datatypes.MeasurementGroup;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;

import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.philips.platform.baseapp.screens.utility.Constants.SQLITE_EXCEPTION;


public class TemperaturePresenter {
    public static final String TAG = TemperaturePresenter.class.getSimpleName();

    private final DBRequestListener dbRequestListener;
    private DataServicesManager mDataServices;

    private Measurement mMeasurement;
    private String mMomentType;
    private MeasurementGroup mMeasurementGroup;
    private MeasurementGroup mMeasurementGroupInside;
    private Context mContext;
    private static final int DELETE = 0;
    private static final int UPDATE = 1;
    static final int ADD = 2;

    private EditText mTemperature;
    private EditText mLocation;
    private EditText mPhase;
    private Button mDialogButton;

    TemperaturePresenter(Context context, String momentType, DBRequestListener dbRequestListener) {
        mDataServices = DataServicesManager.getInstance();
        mMomentType = momentType;
        mContext = context;
        this.dbRequestListener = dbRequestListener;
    }

    private Moment createMoment(String momemtDetail, String measurement, String measurementDetail) {
        Moment moment = mDataServices.createMoment(mMomentType);
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

    public void createMeasurementDetail(String value) {
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

    void fetchData(DBFetchRequestListner dbFetchRequestListner) {
        mDataServices.fetchAllMoment(dbFetchRequestListner);
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

    private void createAndSaveMoment() {
        Moment moment = createMoment(mPhase.getText().toString(),
                mTemperature.getText().toString(), mLocation.getText().toString());
        saveRequest(moment);
    }

    void bindDeleteOrUpdatePopUp(final TemperatureTimeLineFragmentcAdapter adapter,
                                 final List<? extends Moment> data, final View view,
                                 final int selectedItem) {
        List<RowItem> rowItems = new ArrayList<>();

        final String delete = mContext.getResources().getString(R.string.RA_delete);
        final String update = mContext.getResources().getString(R.string.RA_update);
        final String[] descriptions = new String[]{delete, update};

        rowItems.add(new RowItem(descriptions[0]));
        rowItems.add(new RowItem(descriptions[1]));
        final UIKitListPopupWindow popupWindow = new UIKitListPopupWindow(mContext,
                view, UIKitListPopupWindow.UIKIT_Type.UIKIT_BOTTOMLEFT, rowItems);

        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent,
                                    final View view, final int position, final long id) {
                switch (position) {
                    case DELETE:
                        removeMoment(adapter, data, selectedItem);
                        popupWindow.dismiss();
                        break;
                    case UPDATE:
                        final TemperatureMomentHelper helper = new TemperatureMomentHelper();
                        if(String.valueOf(helper.getTemperature(data.get(selectedItem))).equalsIgnoreCase("default")){
                            Toast.makeText(mContext,
                                    R.string.RA_invalid_temperature, Toast.LENGTH_SHORT).show();
                        }else {
                            addOrUpdateMoment(UPDATE, data.get(selectedItem));
                        }
                        popupWindow.dismiss();
                        break;
                    default:
                }
            }
        });
        popupWindow.show();
    }

    private void removeMoment(TemperatureTimeLineFragmentcAdapter adapter,
                              final List<? extends Moment> data, int adapterPosition) {
        try {
            Moment moment = data.get(adapterPosition);
            Dao<OrmSynchronisationData, Integer> ormSynchronisationDataDao = DatabaseHelper.getInstance(mContext).getSynchronisationDataDao();
            ormSynchronisationDataDao.refresh((OrmSynchronisationData) moment.getSynchronisationData());

            Dao<OrmMoment, Integer> momentDao = DatabaseHelper.getInstance(mContext).getMomentDao();
            momentDao.refresh((OrmMoment) moment);

            mDataServices.deleteMoment(moment, dbRequestListener);

        } catch (ArrayIndexOutOfBoundsException e) {
            if (e.getMessage() != null) {
                DSLog.i(DSLog.LOG, "e = " + e.getMessage());
            }
        } catch (SQLException e) {
            RALog.e(TAG + SQLITE_EXCEPTION,
                    e.getMessage());
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
        old.setDateTime(DateTime.now());
        mDataServices.updateMoment(old, dbRequestListener);
    }

    void addOrUpdateMoment(final int addOrUpdate, final Moment moment) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.af_datasync_create_moment_pop_up);
        dialog.setTitle(mContext.getResources().getString(R.string.RA_create_moment));

        mTemperature = (EditText) dialog.findViewById(R.id.temperature_detail);
        mLocation = (EditText) dialog.findViewById(R.id.location_detail);
        mPhase = (EditText) dialog.findViewById(R.id.phase_detail);
        mDialogButton = (Button) dialog.findViewById(R.id.save);
        mDialogButton.setEnabled(false);

        if (addOrUpdate == UPDATE) {
            final TemperatureMomentHelper helper = new TemperatureMomentHelper();
            mTemperature.setText(String.valueOf(helper.getTemperature(moment)));
            mLocation.setText(helper.getNotes(moment));
            mPhase.setText(helper.getTime(moment));
            mDialogButton.setText(R.string.RA_update);
        }

        mDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                final boolean isValid = validateInputFields();
                if (!isValid) {
                    mTemperature.setText("");
                    Toast.makeText(mContext,
                            R.string.RA_invalid_temperature, Toast.LENGTH_SHORT).show();
                    return;
                }

                switch (addOrUpdate) {
                    case ADD:
                        dialog.dismiss();
                        createAndSaveMoment();
                        break;
                    case UPDATE:
                        dialog.dismiss();

                        try {
                            Dao<OrmSynchronisationData, Integer> ormSynchronisationDataDao = DatabaseHelper.getInstance(mContext).getSynchronisationDataDao();
                            ormSynchronisationDataDao.refresh((OrmSynchronisationData) moment.getSynchronisationData());
                            Dao<OrmMoment, Integer> momentDao = DatabaseHelper.getInstance(mContext).getMomentDao();
                            momentDao.refresh((OrmMoment) moment);
                        } catch (SQLException e) {
                            RALog.e(TAG + SQLITE_EXCEPTION,
                                    e.getMessage());
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
}
