package com.philips.cl.di.dev.pa.util;

import java.util.HashMap;

import com.philips.cl.di.dev.pa.newpurifier.AirPurifier;

public interface DashboardUpdateListener {
	void onUpdate(AirPurifier purifier, HashMap<String, Boolean> selectedItems);
    void onItemClickGoToPage(int position);
    void onItemClickGoToAddPurifier();
}
