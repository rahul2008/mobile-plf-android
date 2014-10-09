package com.philips.cl.di.dev.pa.util;

import java.util.ArrayList;
import java.util.Arrays;

public class UnicodeSpecialCharacter {
	public static final int UNICODE38 = 38;
	public static final int UNICODE47 = 47;
	public static final int UNICODE92 = 92;
	public static final int UNICODE60 = 60;
	public static final int UNICODE62 = 62;
	public static final int UNICODE171 = 171;
	public static final int UNICODE187 = 187;
	public static final int UNICODE706 = 706;
	public static final int UNICODE5168 = 5168;
	public static final int UNICODE5193 = 5193;
	public static final int UNICODE8249 = 8249;
	public static final int UNICODE8250 = 8250;
	public static final int UNICODE8804 = 8804;
	public static final int UNICODE8833 = 8833;
	public static final int UNICODE9065 = 9065;
	public static final int UNICODE10873 = 10873;
	public static final int UNICODE10940 = 10940;
	public static final int UNICODE10999 = 10999;
	public static final int UNICODE11002 = 11002;
	public static final int UNICODE65286 = 65286;
	public static final int UNICODE65308 = 65308;
	public static final int UNICODE65310 = 65310;
	public static final int UNICODE65120 = 65120;
	public static final int UNICODE65124 = 65124;
	public static final int UNICODE65125 = 65125;
	public static final int UNICODE12296 = 12296;
	public static final int UNICODE12299 = 12299;
	public static final int UNICODE11622 = 11622;

	private static final Integer[] unicodeArr = { UNICODE38, UNICODE47,
			UNICODE92, UNICODE60, UNICODE62, UNICODE171, UNICODE187,
			UNICODE706, UNICODE8249, UNICODE8250, UNICODE9065, UNICODE65286,
			UNICODE65308, UNICODE65120, UNICODE65124, UNICODE65125,
			UNICODE11622 };

	public static ArrayList<Integer> getSpecialCharaterUnicodes() {
		ArrayList<Integer> unicodes = new ArrayList<Integer>();
		unicodes.addAll(Arrays.asList(unicodeArr));
		addUnicodes(UNICODE5168, UNICODE5193, unicodes);
		addUnicodes(UNICODE8804, UNICODE8833, unicodes);
		addUnicodes(UNICODE10873, UNICODE10940, unicodes);
		addUnicodes(UNICODE10999, UNICODE11002, unicodes);
		addUnicodes(UNICODE12296, UNICODE12299, unicodes);
//		ALog.i(ALog.EWS, "Unicodes: " + unicodes);
		return unicodes;
	}

	private static void addUnicodes(int start, int end,
			ArrayList<Integer> unicodes) {
		for (int i = start; i <= end; i++) {
			unicodes.add(i);
		}
	}
}
