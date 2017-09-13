/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.providerdetails;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.philips.platform.ths.R;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.uid.view.widget.Button;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class THSAppointmentGridAdapter extends ArrayAdapter<Date> {

    private ArrayList<Date> gridItemTimeList = new ArrayList<>();
    private Context mContext;

    THSAppointmentGridAdapter(Context context, List<Date> cardList) {
        super(context, 0, cardList);
        this.mContext = context;
        this.gridItemTimeList = (ArrayList<Date>) cardList;
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

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        View view;

        final Date gridData = gridItemTimeList.get(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(R.layout.ths_cell, null);
            final ViewHolder viewHolder = new ViewHolder();

            viewHolder.buttonDate = (Button) view.findViewById(R.id.date);
            view.setTag(viewHolder);
            viewHolder.buttonDate.setTag(gridItemTimeList.get(position));

        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).buttonDate.setTag(gridItemTimeList.get(position));
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.buttonDate.setText(getFormatedTime(gridData));

        return view;
    }

    static class ViewHolder {
        protected Button buttonDate;
    }

    public String getFormatedTime(Date date) {
        return new SimpleDateFormat(THSConstants.TIME_FORMATTER, Locale.getDefault()).format(date);
    }
}
