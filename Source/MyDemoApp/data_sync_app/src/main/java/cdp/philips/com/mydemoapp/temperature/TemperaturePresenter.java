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

import com.philips.cdp.uikit.customviews.UIKitListPopupWindow;
import com.philips.cdp.uikit.utils.RowItem;
import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementDetail;
import com.philips.platform.core.datatypes.MeasurementDetailType;
import com.philips.platform.core.datatypes.MeasurementType;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.datatypes.MomentDetailType;
import com.philips.platform.core.datatypes.MomentType;
import com.philips.platform.core.trackers.DataServicesManager;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import cdp.philips.com.mydemoapp.R;

public class TemperaturePresenter {
   private DataServicesManager mDataServices;

    private Moment mMoment;
    private Measurement mMeasurement;
    private MomentType mMomentType;
    private Context mContext;
    private static final int DELETE = 0;
    private static final int UPDATE = 1;
    public static final int ADD = 2;

    EditText mTemperature;
    EditText mLocation;
    EditText mPhase;
    Button mDialogButton;

    TemperaturePresenter(Context context, MomentType momentType){
        mDataServices = DataServicesManager.getInstance();
        mMomentType = momentType;
        mContext = context;
    }

    public void createMoment(String momemtDetail, String measurement, String measurementDetail){
        mMoment= mDataServices.createMoment(mMomentType);
        createMomentDetail(momemtDetail);
        createMeasurement(measurement);
        createMeasurementDetail(measurementDetail);
    }

    public void updateMoment(String momemtDetail, String measurement, String measurementDetail){
        mMoment= mDataServices.createMoment(mMomentType);
        mMoment.setDateTime(DateTime.now());
        createMomentDetail(momemtDetail);
        createMeasurement(measurement);
        createMeasurementDetail(measurementDetail);
    }

    public void createMeasurementDetail(String value){
        MeasurementDetail measurementDetail = mDataServices.createMeasurementDetail(MeasurementDetailType.LOCATION,mMeasurement);
        measurementDetail.setValue(value);
    }

    public void createMeasurement(String value){
        mMeasurement = mDataServices.createMeasurement(MeasurementType.TEMPERATURE, mMoment);
        mMeasurement.setValue(Double.valueOf(value));
        mMeasurement.setDateTime(DateTime.now());
    }

    public void createMomentDetail(String value){
        MomentDetail momentDetail = mDataServices.
                createMomentDetail(MomentDetailType.PHASE, mMoment);
        momentDetail.setValue(value);
    }

    public void fetchData(){
        mDataServices.fetch(MomentType.TEMPERATURE);
    }

    public Moment getMoment(){
        return mMoment;
    }

    public void saveRequest(){
        if(mMoment.getCreatorId()==null || mMoment.getSubjectId()==null){
            Toast.makeText(mContext,"Please Login again", Toast.LENGTH_SHORT).show();
        }else {
            mDataServices.save(mMoment);
        }
    }

    public void createAndSaveMoment() {
        createMoment(mPhase.getText().toString(),
                mTemperature.getText().toString(), mLocation.getText().toString());
        saveRequest();
    }

    public void bindDeleteOrUpdatePopUp(final TemperatureTimeLineFragmentcAdapter adapter,
                                        final List<? extends Moment> data, final View view,
                                        final int selectedItem) {
        List<RowItem> rowItems = new ArrayList<>();

        final String delete =mContext.getResources().getString(R.string.delete);
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
                        removeMoment(adapter,data,selectedItem);
                        popupWindow.dismiss();
                        break;
                    case UPDATE:
                        addOrUpdateMoment(UPDATE,data.get(selectedItem));
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
            if(e.getMessage()!=null){
                Log.i("***SPO***","e = " + e.getMessage());
            }
        }
    }


    private void updateAndSaveMoment(Moment moment) {

        try {
            mDataServices.update(new TemperatureMomentHelper().updateMoment(moment,
                    mPhase.getText().toString(), mTemperature.getText().toString(), mLocation.getText().toString()));
        } catch (Exception ArrayIndexOutOfBoundsException) {
            if(ArrayIndexOutOfBoundsException.getMessage()!=null){
                Log.i("***SPO***","e = " + ArrayIndexOutOfBoundsException.getMessage());
            }
        }
    }

    public void addOrUpdateMoment(final int addOrUpdate, final Moment moment) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.af_datasync_create_moment_pop_up);
        dialog.setTitle(mContext.getResources().getString(R.string.create_moment));

        mTemperature = (EditText) dialog.findViewById(R.id.temperature_detail);
        mLocation = (EditText) dialog.findViewById(R.id.location_detail);
        mPhase = (EditText) dialog.findViewById(R.id.phase_detail);
        mDialogButton = (Button) dialog.findViewById(R.id.save);
        mDialogButton.setEnabled(false);

        if(addOrUpdate == UPDATE){
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

                switch (addOrUpdate){
                    case ADD:
                        createAndSaveMoment();
                        break;
                    case UPDATE:
                        updateAndSaveMoment(moment);
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
                if(isDialogButtonEnabled()){
                    mDialogButton.setEnabled(true);
                }else{
                    mDialogButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(final Editable s) {

            }
        });
    }

    private boolean isDialogButtonEnabled() {
        return mPhase.getText().toString()!=null && !mPhase.getText().toString().isEmpty() &&
                mTemperature.getText().toString()!=null && !mTemperature.getText().toString().isEmpty() &&
                mLocation.getText().toString()!=null && !mLocation.getText().toString().isEmpty();
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
