package com.philips.platform.appinfra.appupdate;


public class AIVersion {


	private static int compareVersion(String appVer, String cloudVer) {
		String[] arr1 = appVer.split("\\.");
		String[] arr2 = cloudVer.split("\\.");

		int i = 0;
		while (i < arr1.length || i < arr2.length) {
			if (i < arr1.length && i < arr2.length) {
				if (Integer.parseInt(arr1[i]) < Integer.parseInt(arr2[i])) {
					return -1;
				} else if (Integer.parseInt(arr1[i]) > Integer.parseInt(arr2[i])) {
					return 1;
				}
			} else if (i < arr1.length) {
				if (Integer.parseInt(arr1[i]) != 0) {
					return 1;
				}
			} else if (i < arr2.length) {
				if (Integer.parseInt(arr2[i]) != 0) {
					return -1;
				}
			}

			i++;
		}
		return 0;
	}

	static boolean isverionlessthancloud(String version , String cloudver) {
		if(compareVersion(version,cloudver) == -1) {
			return true;
		}
		return false;
	}

	static boolean isversionGreaterthancloud(String version , String cloudver) {
		if(compareVersion(version,cloudver) == 1) {
			return true;
		}
		return false;
	}

	static boolean isbothversionsame(String version , String cloudver) {
		if(compareVersion(version ,cloudver) == 0) {
			return true;
		}
		return false;
	}

	static boolean isversionlessthanequalsto(String verion , String cloudver) {
		if(compareVersion(verion ,cloudver) == -1 || compareVersion(verion ,cloudver) == 0) {
			return true;
		}
		return false;
	}


}
