package com.philips.platform.ths.providerdetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.philips.platform.ths.R;
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

    public THSAppointmentGridAdapter(Context context, List<Date> cardList) {
        super(context, 0, cardList);
        this.mContext = context;
        this.cardList = (ArrayList<Date>) cardList;
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

        Date gridData = cardList.get(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);




            convertView = layoutInflater.inflate(R.layout.cell, null);



            Button thumbnail = (Button) convertView.findViewById(R.id.date);


            thumbnail.setText(getFormatedTime(gridData));

            ViewHolder viewHolder = new ViewHolder(thumbnail);
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
