package com.philips.platform.ths.providerdetails;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.philips.platform.ths.R;
import com.philips.platform.ths.appointment.THSConfirmAppointmentFragment;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.uid.view.widget.Button;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class THSAppointmentGridAdapter extends ArrayAdapter<Date> {

    private ArrayList<Date> cardList = new ArrayList();
    Context mContext;
    public final String TIME_FORMATTER = "h:mm a";
    THSBaseFragment thsBaseFragment;
    THSProviderInfo thsProviderInfo;

    public THSAppointmentGridAdapter(Context context, List<Date> cardList, THSBaseFragment thsBaseFragment, THSProviderInfo thsProviderInfo) {
        super(context, 0, cardList);
        this.mContext = context;
        this.cardList = (ArrayList<Date>) cardList;
        this.thsBaseFragment = thsBaseFragment;
        this.thsProviderInfo = thsProviderInfo;
    }


    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public int getCount() {
        if (cardList == null) {
            return 0;
        }
        return cardList.size();
    }

    @Override
    public Date getItem(int position) {
        return cardList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Date gridData = cardList.get(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);

            convertView = layoutInflater.inflate(R.layout.cell, null);

            Button timeslot = (Button) convertView.findViewById(R.id.date);

            timeslot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(THSConstants.THS_PROVIDER_INFO,thsProviderInfo);
                    bundle.putSerializable(THSConstants.THS_DATE,gridData);
                    final THSConfirmAppointmentFragment fragment = new THSConfirmAppointmentFragment();
                    fragment.setFragmentLauncher(thsBaseFragment.getFragmentLauncher());
                    thsBaseFragment.addFragment(fragment, THSConfirmAppointmentFragment.TAG,bundle);
                }
            });

            timeslot.setText(getFormatedTime(gridData));

            ViewHolder viewHolder = new ViewHolder(timeslot);
            convertView.setTag(viewHolder);
        }


        return convertView;
    }

    public void updateGrid(ArrayList<Date> newList) {
        this.cardList.clear();
        this.cardList = new ArrayList<>(newList);
        this.notifyDataSetChanged();
    }

    static class ViewHolder {
       Button buttonDate;

        public ViewHolder(Button dateButton) {
            this.buttonDate = dateButton;
        }
    }

    public String getFormatedTime(Date date){
        return new SimpleDateFormat(TIME_FORMATTER, Locale.getDefault()).format(date);
    }
}
