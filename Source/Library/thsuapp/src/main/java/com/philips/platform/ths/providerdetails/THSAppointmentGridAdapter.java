/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.providerdetails;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.philips.platform.ths.R;
import com.philips.platform.ths.appointment.THSGridItemOnClickListener;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.uid.thememanager.ThemeUtils;
import com.philips.platform.uid.view.widget.Button;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class THSAppointmentGridAdapter extends ArrayAdapter<Date> {

    private ArrayList<Date> gridItemTimeList = new ArrayList();
    private Context mContext;
    public final String TIME_FORMATTER = "HH:mm";
    private THSBaseFragment thsBaseFragment;
    private THSProviderInfo thsProviderInfo;
    private int selectedPosition = -1;
    private THSGridItemOnClickListener thsGridItemOnClickListener;
    private final ColorStateList colorStateList;

    public THSAppointmentGridAdapter(Context context, List<Date> cardList, THSBaseFragment thsBaseFragment, THSProviderInfo thsProviderInfo,THSGridItemOnClickListener thsGridItemOnClickListener) {
        super(context, 0, cardList);
        this.mContext = context;
        this.gridItemTimeList = (ArrayList<Date>) cardList;
        this.thsBaseFragment = thsBaseFragment;
        this.thsProviderInfo = thsProviderInfo;
        this.thsGridItemOnClickListener = thsGridItemOnClickListener;
        colorStateList = ThemeUtils.buildColorStateList(getContext(), R.color.segment_text_color);
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
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
        if (gridItemTimeList == null) {
            return 0;
        }
        return gridItemTimeList.size();
    }

    @Override
    public Date getItem(int position) {
        return gridItemTimeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Date gridData = gridItemTimeList.get(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);

            convertView = layoutInflater.inflate(R.layout.ths_cell, null);

            Button timeslot = (Button) convertView.findViewById(R.id.date);
            timeslot.setTextColor(colorStateList);

            if(position == selectedPosition){
                timeslot.setPressed(true);
            }
            timeslot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(THSConstants.THS_PROVIDER_INFO, thsProviderInfo);
                    bundle.putSerializable(THSConstants.THS_DATE, gridData);
                    thsGridItemOnClickListener.onGridItemClicked(position);
                }
            });

            timeslot.setText(getFormatedTime(gridData));

            ViewHolder viewHolder = new ViewHolder(timeslot);
            convertView.setTag(viewHolder);
        }


        return convertView;
    }

    public void updateGrid(ArrayList<Date> newList) {
        this.gridItemTimeList.clear();
        this.gridItemTimeList = new ArrayList<>(newList);
        this.notifyDataSetChanged();
    }

    static class ViewHolder {
        Button buttonDate;

        public ViewHolder(Button dateButton) {
            this.buttonDate = dateButton;
        }
    }

    public String getFormatedTime(Date date) {
        return new SimpleDateFormat(TIME_FORMATTER, Locale.getDefault()).format(date);
    }
}
