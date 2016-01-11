package com.deamon;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.pojo.Datum;
import com.pojo.Overview;

public class Tester {

	public static void main(String[] args) {

		Gson mGson = null;
		String mMultiCTNPath = "C:\\Users\\310190678\\Desktop\\PRXJSONS\\testfile.json";
		List<Datum> mList = null;
		Datum mData = null;

		if (mGson == null)
			mGson = new Gson();

		try {

			BufferedReader mBufferedReader = new BufferedReader(new FileReader(mMultiCTNPath));

			Overview overview = mGson.fromJson(mBufferedReader, Overview.class);

			if (overview.isSuccess()) {
				mList = overview.getData();
				mData = mList.get(0);
			}

			System.out.println("Printing the Overview Object : " + mData.getFamilyName());
			System.out.println("Printing the Overview Object : " + mData.getCode12NC());
			System.out.println("Printing the Overview Object : " + mData.getImageURL());
			System.out.println("Printing the Overview Object : " + mData.getLocale());
			System.out.println("Printing the Overview Object : " + mData.getProductTitle());

		} catch (IOException exception) {
			System.out.println("IO Exception : " + exception);
		}

	}

}
