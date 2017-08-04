package com.philips.cdp.registration.ui.customviews.countrypicker;

import android.content.Context;
import android.view.*;
import android.widget.*;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.R.drawable;
import com.philips.cdp.registration.ui.utils.RLog;

import java.lang.reflect.Field;
import java.util.*;

class CountryAdapter extends BaseAdapter {
    private List<Country> countries;
    private LayoutInflater inflater;

    private int getResId(String drawableName) {
        try {
            Class<drawable> res = R.drawable.class;
            Field field = res.getField(drawableName);
            return field.getInt(null);
        } catch (Exception e) {
            RLog.e("COUNTRYPICKER", "Failure to get drawable id."+ e);
        }
        return -1;
    }

    CountryAdapter(Context context, List<Country> countries) {
        super();
        this.countries = countries;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return countries.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cellView = convertView;
        Cell cell;
        Country country = countries.get(position);

        if (convertView == null) {
            cell = new Cell();
            cellView = inflater.inflate(R.layout.reg_country_selection_row, null);
            cell.textView = (TextView) cellView.findViewById(R.id.reg_row_title);
            cell.imageView = (ImageView) cellView.findViewById(R.id.reg_row_icon);
            cellView.setTag(cell);
        } else {
            cell = (Cell) cellView.getTag();
        }
        cell.textView.setText(country.getName());
        String drawableName = "reg_"
                + country.getCode().toLowerCase(Locale.ENGLISH);
        cell.imageView.setImageResource(getResId(drawableName));
        return cellView;
    }

    private static class Cell {
        TextView textView;
        ImageView imageView;
    }
}