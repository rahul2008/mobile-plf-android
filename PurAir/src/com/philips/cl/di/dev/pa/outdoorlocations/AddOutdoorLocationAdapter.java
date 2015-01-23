package com.philips.cl.di.dev.pa.outdoorlocations;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.dashboard.OutdoorCityInfo;
import com.philips.cl.di.dev.pa.util.LanguageUtils;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class AddOutdoorLocationAdapter extends ArrayAdapter<OutdoorCityInfo> {
	
	private List<OutdoorCityInfo> outdoorCityInfoList;
	private Context context;

	public AddOutdoorLocationAdapter(Context context,
			int textViewResourceId, List<OutdoorCityInfo> outdoorCityInfoList) {
		super(context, textViewResourceId, outdoorCityInfoList);
		this.context = context;
		this.outdoorCityInfoList = outdoorCityInfoList;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.simple_list_item, null);
		
		ImageView deleteSign = (ImageView) view.findViewById(R.id.list_item_delete);
		
		deleteSign.setVisibility(View.GONE);
		FontTextView tvName = (FontTextView) view.findViewById(R.id.list_item_name);
		if (outdoorCityInfoList.get(position) != null) {
			String city = outdoorCityInfoList.get(position).getCityName();
			
			if(LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANS")) {
				city = outdoorCityInfoList.get(position).getCityNameCN();
			} else if(LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANT")) {
				city = outdoorCityInfoList.get(position).getCityNameTW();
			}
		
		
			//Replace first latter Capital and append US Embassy
			if( outdoorCityInfoList.get(position).getDataProvider() == OutdoorDataProvider.US_EMBASSY.ordinal()) {
				StringBuilder builder = new StringBuilder(AddOutdoorLocationHelper.getFirstWordCapitalInSentence(city)) ;				
				builder.append(" (").append(context.getString(R.string.us_embassy)).append(" )") ;				
				city = builder.toString() ;
			}
			tvName.setText(city);
		}
		
		return view;
	}

}
