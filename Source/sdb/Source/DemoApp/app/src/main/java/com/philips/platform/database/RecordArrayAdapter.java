package com.philips.platform.database;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;

@SuppressWarnings("rawtypes")
public class RecordArrayAdapter extends ArrayAdapter<String>{

		private LayoutInflater inflater;
		
		// This would hold the database objects. It could be TeacherDetails or AddressBook objects
		private List records;
		

		public RecordArrayAdapter(Context context, int resource, List objects) {
			super(context, resource, objects);
			
			this.records = objects;

			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			//Reuse the view to make the scroll effect smooth
			if(convertView == null)
				convertView = inflater.inflate(R.layout.list_item, parent, false);
			
			// If the ListView needs to display the records of AddressBook objects
			if(records.get(position).getClass().isInstance(new AddressBook())){
				final AddressBook addressBookDetails = (AddressBook) records.get(position);
				((TextView)convertView.findViewById(R.id.txtAddressId)).setText(Integer.toString(addressBookDetails.addressId));
				((TextView)convertView.findViewById(R.id.txtFirstName)).setText(addressBookDetails.firstName);
				((TextView)convertView.findViewById(R.id.txtLastName)).setText(addressBookDetails.lastName);
				((TextView)convertView.findViewById(R.id.txtAddress)).setText(addressBookDetails.address);
				((TextView)convertView.findViewById(R.id.txtContact)).setText(addressBookDetails.contactNumber);

			}

			return convertView;
		}
	
}
