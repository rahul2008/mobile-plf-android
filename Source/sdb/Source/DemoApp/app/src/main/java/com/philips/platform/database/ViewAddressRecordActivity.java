package com.philips.platform.database;


import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;


public class ViewAddressRecordActivity extends Activity {

	private ListView listview;

	private List<AddressBook> addressList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_recd);
		listview = (ListView) findViewById(R.id.listview);

		Intent i = getIntent();
		addressList = (List<AddressBook>) i.getSerializableExtra("ADDREES_BOOK");


		final LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View rowView = inflater.inflate(R.layout.list_item, listview, false);
		listview.addHeaderView(rowView);

		Log.i("","address list"+addressList.size());

		listview.setAdapter(new RecordArrayAdapter(this, R.layout.list_item, addressList));

		populateNoRecordMsg();

	}
	
	private void populateNoRecordMsg()
	{
		if(addressList.size() == 0)
		{
			final TextView tv = new TextView(this);
			tv.setPadding(5, 5, 5, 5);
			tv.setTextSize(15);
			tv.setText("No Record Found !!");
			listview.addFooterView(tv);
		}
	}
	


	
	@Override
	protected void onDestroy() {
		super.onDestroy();

		/*if (databaseHelper != null) {
			OpenHelperManager.releaseHelper();
			databaseHelper = null;
		}*/
	}


	
}
