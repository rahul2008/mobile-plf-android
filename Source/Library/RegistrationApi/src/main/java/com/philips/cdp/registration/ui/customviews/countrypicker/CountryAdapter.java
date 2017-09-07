package com.philips.cdp.registration.ui.customviews.countrypicker;

import android.content.Context;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.settings.RegistrationHelper;

import java.util.List;

class CountryAdapter extends BaseAdapter {
    private List<Country> countries;
    private LayoutInflater inflater;

    CountryAdapter(Context context, List<Country> countries) {
        super();
        this.countries = countries;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            cellView = inflater.inflate(R.layout.country_selection_item, null);
            cell.textView = (TextView) cellView.findViewById(R.id.usr_countrySelection_countryName);
            cell.imageView = (ImageView) cellView.findViewById(R.id.usr_countrySelection_countrySelector);
            cell.imageView.setVisibility(View.GONE);
            cellView.setTag(cell);
        } else {
            cell = (Cell) cellView.getTag();
        }
        cell.textView.setText(country.getName());
        Log.i("COUNTRY", " Selected country : " + RegistrationHelper.getInstance().getCountryCode() + " current Country " + country.getCode());
        Log.i("COUNTRY", " Are they equal :: " + (RegistrationHelper.getInstance().getCountryCode().equals(country.getCode())));
        if(position == 0) {
            cell.imageView.setVisibility(View.VISIBLE);
        }
        return cellView;
    }

    private static class Cell {
        TextView textView;
        ImageView imageView;
    }
}