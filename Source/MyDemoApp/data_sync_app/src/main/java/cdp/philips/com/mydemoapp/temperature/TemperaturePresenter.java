package cdp.philips.com.mydemoapp.temperature;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
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

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class TemperaturePresenter {
   DataServicesManager mDataServices;

    Moment mMoment;
    Measurement mMeasurement;
    MomentType mMomentType;
    Context mContext;
    private static final int DELETE = 0;
    private static final int UPDATE = 1;
    public static final int ADD = 2;


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
        MomentDetail momentDetail = mDataServices.createMomentDetail(MomentDetailType.PHASE, mMoment);
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

    public void createAndSaveMoment(final String phaseInput, final String temperatureInput, final String locationInput) {
        createMoment(phaseInput, temperatureInput, locationInput);
        saveRequest();
    }

    public void bindDeleteOrUpdatePopUP(final TemperatureTimeLineFragmentcAdapter adapter, final List<? extends Moment> mData, final View view, final int selectedItem) {
        List<RowItem> rowItems = new ArrayList<>();

        final String delete =mContext.getResources().getString(R.string.delete);
        String update = mContext.getResources().getString(R.string.update);
        final String[] descriptions = new String[]{delete, update};

        rowItems.add(new RowItem(descriptions[0]));
        rowItems.add(new RowItem(descriptions[1]));
        final UIKitListPopupWindow mPopupWindow = new UIKitListPopupWindow(mContext, view, UIKitListPopupWindow.UIKIT_Type.UIKIT_BOTTOMLEFT, rowItems);

        mPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                switch (position) {
                    case DELETE:
                        removeMoment(adapter,mData,selectedItem);
                        mPopupWindow.dismiss();
                        break;
                    case UPDATE:
                        addOrUpdateMoment(UPDATE,mData.get(selectedItem));
                        mPopupWindow.dismiss();
                        break;
                    default:
                }
            }
        });
        mPopupWindow.show();
    }

    private void removeMoment(TemperatureTimeLineFragmentcAdapter adapter, final List<? extends Moment> mData, int adapterPosition) {
        try {
            mDataServices.deleteMoment(mData.get(adapterPosition));
            mData.remove(adapterPosition);
            adapter.notifyItemRemoved(adapterPosition);
            adapter.notifyDataSetChanged();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }


    private void updateAndSaveMoment(Moment moment, final String phaseInput, final String temperatureInput, final String locationInput) {

        try {
            TemperatureMomentHelper helper = new TemperatureMomentHelper();
            moment = helper.updateMoment(moment, phaseInput, temperatureInput, locationInput);
            mDataServices.update(moment);
        } catch (Exception ArrayIndexOutOfBoundsException) {

        }
    }

    public void addOrUpdateMoment(final int addOrUpdate, final Moment moment) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.af_datasync_create_moment_pop_up);
        dialog.setTitle(mContext.getResources().getString(R.string.create_moment));
        final EditText temperature = (EditText) dialog.findViewById(R.id.temperature_detail);
        final EditText location = (EditText) dialog.findViewById(R.id.location_detail);
        final EditText phase = (EditText) dialog.findViewById(R.id.phase_detail);
        final Button dialogButton = (Button) dialog.findViewById(R.id.save);
        dialogButton.setEnabled(false);

        if(moment!=null){
            TemperatureMomentHelper helper = new TemperatureMomentHelper();
            temperature.setText(String.valueOf(helper.getTemperature(moment)));
            location.setText(helper.getNotes(moment));
            phase.setText(helper.getTime(moment));
        }

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                boolean isValid = validateInputFields(phase.getText().toString(),
                        temperature.getText().toString(), location.getText().toString());

                if (!isValid) {
                    temperature.setText("");
                    Toast.makeText(mContext, R.string.invalid_temperature, Toast.LENGTH_SHORT).show();
                    return;
                }

                switch (addOrUpdate){
                    case ADD:
                        createAndSaveMoment(phase.getText().toString(),
                                temperature.getText().toString(), location.getText().toString());
                        break;
                    case UPDATE:
                        updateAndSaveMoment(moment,phase.getText().toString(), temperature.getText().toString(),
                                location.getText().toString());
                        break;
                }
                dialog.dismiss();
            }
        });

        phase.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                if(phase.getText().toString()!=null && !phase.getText().toString().isEmpty() && temperature.getText().toString()!=null && !temperature.getText().toString().isEmpty() && location.getText().toString()!=null && !location.getText().toString().isEmpty()){
                    dialogButton.setEnabled(true);
                }else{
                    dialogButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(final Editable s) {

            }
        });

        temperature.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                if(phase.getText().toString()!=null && !phase.getText().toString().isEmpty() && temperature.getText().toString()!=null && !temperature.getText().toString().isEmpty() && location.getText().toString()!=null && !location.getText().toString().isEmpty()){
                    dialogButton.setEnabled(true);
                }else{
                    dialogButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(final Editable s) {

            }
        });

        location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                if(phase.getText().toString()!=null && !phase.getText().toString().isEmpty() && temperature.getText().toString()!=null && !temperature.getText().toString().isEmpty() && location.getText().toString()!=null && !location.getText().toString().isEmpty()){
                    dialogButton.setEnabled(true);
                }else{
                    dialogButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(final Editable s) {

            }
        });

        dialog.show();
    }

    private boolean validateInputFields(String phase, String temperature, String locations) {
        //validate temperature
        try {
            Double.valueOf(temperature);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


}
