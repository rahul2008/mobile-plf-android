package com.philips.cl.di.dev.pa.outdoorlocations;

import java.util.List;
import java.util.Locale;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.dashboard.OutdoorCity;
import com.philips.cl.di.dev.pa.dashboard.OutdoorCityInfo;
import com.philips.cl.di.dev.pa.dashboard.OutdoorManager;
import com.philips.cl.di.dev.pa.util.LanguageUtils;
import com.philips.cl.di.dev.pa.view.FontTextView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class UserSelectedCitiesAdapter extends ArrayAdapter<String> {

	private Context context;
	private List<String> userCities;
	
	public UserSelectedCitiesAdapter(Context context, int resource, List<String> userCities) {
		super(context, resource, userCities);
		this.context = context;
		this.userCities = userCities;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.simple_list_item, null);

		ImageView deleteSign = (ImageView) view.findViewById(R.id.list_item_delete);
		deleteSign.setVisibility(View.VISIBLE);
		FontTextView tvName = (FontTextView) view.findViewById(R.id.list_item_name);

		String areaId = userCities.get(position);

		OutdoorCity city = OutdoorManager.getInstance().getCityData(areaId);
		OutdoorCityInfo info = city.getOutdoorCityInfo();
		
		String cityName = info.getCityName();

		if(LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANS")) {
			cityName = info.getCityNameCN();
		} else if(LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANT")) {
			cityName = info.getCityNameTW();
		}

		//Replace first latter Capital and append US Embassy
		if( info.getDataProvider() == OutdoorDataProvider.US_EMBASSY.ordinal()) {
			StringBuilder builder = new StringBuilder() ;
			builder.append(cityName.substring(0,1).toUpperCase()).append(cityName.substring(1)) ;
			builder.append(" (").append(context.getString(R.string.us_embassy)).append(" )") ;

			cityName = builder.toString() ;
		}
		tvName.setText(cityName);

		return view;
	}

}
