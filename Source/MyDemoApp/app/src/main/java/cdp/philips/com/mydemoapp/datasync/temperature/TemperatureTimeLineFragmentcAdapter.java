package cdp.philips.com.mydemoapp.datasync.temperature;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.events.MomentDeleteRequest;
import com.philips.platform.core.events.MomentUpdateRequest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cdp.philips.com.mydemoapp.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class TemperatureTimeLineFragmentcAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<? extends Moment> mData;
    Context mContext;
    Eventing mEventing;

    public TemperatureTimeLineFragmentcAdapter(final Context context,Eventing eventing, final ArrayList<? extends Moment> data) {
        mData = data;
        mContext = context;
        mEventing = eventing;
    }

    @Override
    public TemperatureTimeLineFragmentcAdapter.DataSyncViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.af_timeline_item, parent, false);
        return new DataSyncViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof DataSyncViewHolder) {
            DataSyncViewHolder mSyncViewHolder = (DataSyncViewHolder) holder;

            TemperatureMomentHelper helper = new TemperatureMomentHelper();
            Moment moment = mData.get(position);
            mSyncViewHolder.mNotes.setText(helper.getNotes(moment));
            mSyncViewHolder.mMomentData.setText(String.valueOf(helper.getTemperature(moment)));

            mSyncViewHolder.mBtnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    removeMoment(position);
                }
            });
            mSyncViewHolder.mBtnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                 updateMoment(position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(final ArrayList<? extends Moment> data) {
        this.mData = data;
    }

    public class DataSyncViewHolder extends RecyclerView.ViewHolder {
        public TextView mMomentData;
        public Button mBtnUpdate;
        public Button mBtnDelete;
        public  TextView mNotes;

        public DataSyncViewHolder(final View itemView) {
            super(itemView);
            mMomentData = (TextView) itemView.findViewById(R.id.time_line_data);
            mBtnDelete = (Button) itemView.findViewById(R.id.delete);
            mBtnUpdate = (Button) itemView.findViewById(R.id.update);
            mNotes = (TextView) itemView.findViewById(R.id.time_line_temperature);
        }
    }

    private void removeMoment(int adapterPosition) {
        try {
            mEventing.post((new MomentDeleteRequest(mData.get(adapterPosition))));
            mData.remove(adapterPosition);
            notifyItemRemoved(adapterPosition);
            notifyDataSetChanged();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    /*@Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.delete:
                removeMoment();
                break;
            case R.id.update:
                updateMoment();
                break;
        }
    }*/

    private void updateMoment(final int position) {

        final Moment moment = mData.get(position);
        TemperatureMomentHelper helper = new TemperatureMomentHelper();

        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.af_datasync_create_moment_pop_up);
        dialog.setTitle("Create A Moment");

        final EditText temperature = (EditText) dialog.findViewById(R.id.temperature_detail);
        final EditText location = (EditText) dialog.findViewById(R.id.location_detail);
        final Button dialogButton = (Button) dialog.findViewById(R.id.save);
        dialogButton.setEnabled(false);

        temperature.setText(String.valueOf(helper.getTemperature(moment)));
        location.setText(helper.getNotes(moment));

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                updateAndSaveMoment(mydate, temperature.getText().toString(),
                        location.getText().toString(), position);
                dialog.dismiss();
            }
        });

        temperature.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                if(temperature.getText().toString()!=null && !temperature.getText().toString().isEmpty() && location.getText().toString()!=null && !location.getText().toString().isEmpty()){
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
                if(temperature.getText().toString()!=null && !temperature.getText().toString().isEmpty() && location.getText().toString()!=null && !location.getText().toString().isEmpty()){
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

    private void updateAndSaveMoment(final String phaseInput, final String temperatureInput, final String locationInput, int position) {

        try {
            final Moment moment;
            TemperatureMomentHelper helper = new TemperatureMomentHelper();
            moment = helper.updateMoment(mData.get(position), phaseInput, temperatureInput, locationInput);
            mEventing.post((new MomentUpdateRequest(moment)));
        } catch (Exception ArrayIndexOutOfBoundsException) {

        }
    }

}
