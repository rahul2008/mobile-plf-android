/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package cdp.philips.com.mydemoapp.temperature;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.philips.cdp.uikit.customviews.UIKitListPopupWindow;
import com.philips.cdp.uikit.utils.RowItem;
import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementDetail;
import com.philips.platform.core.datatypes.MeasurementGroup;
import com.philips.platform.core.datatypes.MeasurementGroupDetail;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.UuidGenerator;

import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cdp.philips.com.mydemoapp.R;
import cdp.philips.com.mydemoapp.database.DatabaseHelper;
import cdp.philips.com.mydemoapp.database.datatypes.MeasurementDetailType;
import cdp.philips.com.mydemoapp.database.datatypes.MeasurementGroupDetailType;
import cdp.philips.com.mydemoapp.database.datatypes.MeasurementType;
import cdp.philips.com.mydemoapp.database.datatypes.MomentDetailType;
import cdp.philips.com.mydemoapp.database.table.OrmMeasurement;
import cdp.philips.com.mydemoapp.database.table.OrmMeasurementDetail;
import cdp.philips.com.mydemoapp.database.table.OrmMeasurementGroup;
import cdp.philips.com.mydemoapp.database.table.OrmMoment;
import cdp.philips.com.mydemoapp.database.table.OrmMomentDetail;

public class TemperaturePresenter {
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

    TemperaturePresenter(Context context, String momentType) {
        mDataServices = DataServicesManager.getInstance();
        mMomentType = momentType;
        mContext = context;
    }

    private Moment createMoment(String momemtDetail, String measurement, String measurementDetail) {
        Log.i("***CREATE***", "In Create Moment");
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
        MeasurementDetail measurementDetail = mDataServices.createMeasurementDetail(MeasurementDetailType.LOCATION, mMeasurement);
        measurementDetail.setValue(value);
        mMeasurement.addMeasurementDetail(measurementDetail);
    }

    private void createMeasurement(MeasurementGroup group, String value) {
        mMeasurement = mDataServices.createMeasurement(MeasurementType.TEMPERATURE, group);
        mMeasurement.setValue(value);
        mMeasurement.setDateTime(DateTime.now());
        mMeasurement.setUnit("celsius");
    }

    private void createMomentDetail(String value, Moment moment) {
        MomentDetail momentDetail = mDataServices.
                createMomentDetail(MomentDetailType.PHASE, moment);
        momentDetail.setValue(value);
    }

    private void createMeasurementGroupDetail(String value) {
        MeasurementGroupDetail measurementGroupDetail = mDataServices.
                createMeasurementGroupDetail(MeasurementGroupDetailType.TEMP_OF_DAY, mMeasurementGroup);
        measurementGroupDetail.setValue(value);
        mMeasurementGroup.addMeasurementGroupDetail(measurementGroupDetail);
    }

    private void createMeasurementGroup(Moment moment) {
        mMeasurementGroup = mDataServices.
                createMeasurementGroup(moment);
    }

    void fetchData() {
        mDataServices.fetchAllData();
    }

    private void saveRequest(Moment moment) {
        if (moment.getCreatorId() == null || moment.getSubjectId() == null) {
            Toast.makeText(mContext, "Please Login again", Toast.LENGTH_SHORT).show();
        } else {
            mDataServices.save(moment);
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

        final String delete = mContext.getResources().getString(R.string.delete);
        final String update = mContext.getResources().getString(R.string.update);
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
                        addOrUpdateMoment(UPDATE, data.get(selectedItem));
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
            mDataServices.deleteMoment(data.get(adapterPosition));
            data.remove(adapterPosition);
            adapter.notifyItemRemoved(adapterPosition);
            adapter.notifyDataSetChanged();
        } catch (ArrayIndexOutOfBoundsException e) {
            if (e.getMessage() != null) {
                Log.i("***SPO***", "e = " + e.getMessage());
            }
        }
    }

    private void updateMoment(OrmMoment old) throws SQLException {
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext, new UuidGenerator());

        Dao<OrmMoment, Integer> momentDao = databaseHelper.getMomentDao();
        Dao<OrmMomentDetail, Integer> momentDetailDao = databaseHelper.getMomentDetailDao();
        Dao<OrmMeasurement, Integer> measurementDao = databaseHelper.getMeasurementDao();
        Dao<OrmMeasurementDetail, Integer> measurementDetailDao = databaseHelper.getMeasurementDetailDao();
        Dao<OrmMeasurementGroup, Integer> measurementGroup = databaseHelper.getMeasurementGroupDao();

        String momentDetail = mPhase.getText().toString();
        String meausrementValue = mTemperature.getText().toString();
        String measurementDetailValue = mLocation.getText().toString();

        Collection<? extends MomentDetail> momentDetails = old.getMomentDetails();


        for (MomentDetail momentDetail1 : momentDetails) {
            momentDetail1.setValue(momentDetail);
            momentDetailDao.createOrUpdate((OrmMomentDetail) momentDetail1);
            momentDetailDao.refresh((OrmMomentDetail) momentDetail1);
        }

        Collection<? extends MeasurementGroup> measurementGroups = old.getMeasurementGroups();
        for (MeasurementGroup next : measurementGroups) {
            Collection<? extends MeasurementGroup> measurementGroupsInside = next.getMeasurementGroups();
            for (MeasurementGroup next1 : measurementGroupsInside) {
                Collection<? extends Measurement> measurements = next1.getMeasurements();
                for (Measurement next2 : measurements) {
                    next2.setValue(meausrementValue);

                    Collection<? extends MeasurementDetail> measurementDetails = next2.getMeasurementDetails();
                    for (MeasurementDetail next3 : measurementDetails) {
                        next3.setValue(measurementDetailValue);
                        measurementDetailDao.createOrUpdate((OrmMeasurementDetail) next3);
                        measurementDetailDao.refresh((OrmMeasurementDetail) next3);
                    }
                    measurementDao.createOrUpdate((OrmMeasurement) next2);
                    measurementDao.refresh((OrmMeasurement) next2);
                }
                measurementGroup.createOrUpdate((OrmMeasurementGroup) next1);
                measurementGroup.refresh((OrmMeasurementGroup) next1);
            }
            measurementGroup.createOrUpdate((OrmMeasurementGroup) next);
            measurementGroup.refresh((OrmMeasurementGroup) next);
        }
        old.setDateTime(DateTime.now());
        old.setSynced(false);
        momentDao.createOrUpdate(old);
        momentDao.refresh(old);
        mDataServices.update(old);
    }

    void addOrUpdateMoment(final int addOrUpdate, final Moment moment) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.af_datasync_create_moment_pop_up);
        dialog.setTitle(mContext.getResources().getString(R.string.create_moment));

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
                        createAndSaveMoment();
                        break;
                    case UPDATE:
                        try {
                            updateMoment((OrmMoment) moment);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                dialog.dismiss();
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
        //validate temperature
        try {
            Double.valueOf(temperature);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


}
