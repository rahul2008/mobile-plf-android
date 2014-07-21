package com.philips.cl.di.dev.pa.util;

import java.util.HashMap;

public interface UpdateListener {
	void onUpdate(String id, HashMap<String, Boolean> selectedItems);
}
