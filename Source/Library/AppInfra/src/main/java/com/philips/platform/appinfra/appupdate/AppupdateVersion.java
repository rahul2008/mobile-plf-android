package com.philips.platform.appinfra.appupdate;


public class AppupdateVersion {

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

	static boolean isAppVerionLessthanCloud(String version, String cloudver) {
		if (compareVersion(splitVersion(version), splitVersion(cloudver)) == -1) {
			return true;
		}
		return false;
	}

	static boolean isBothVersionSame(String version, String cloudver) {
		if (compareVersion(splitVersion(version), splitVersion(cloudver)) == 0) {
			return true;
		}
		return false;
	}

	static boolean isAppVersionLessthanEqualsto(String version, String cloudver) {
		if (compareVersion(splitVersion(version), splitVersion(cloudver)) == -1 ||
				compareVersion(splitVersion(version), splitVersion(cloudver)) == 0) {
			return true;
		}
		return false;
	}

	private static String splitVersion(String version) {
		if (version != null) {
			if (!version.matches("[0-9]+\\.[0-9]+\\.[0-9]+([_(-].*)?")) { // application format.
				throw new IllegalArgumentException("Invalid version format-AppUpdate");
			} else {
				String arr[] = version.split("-|_|\\(");  //splitting based on verion format.
				return arr[0].trim();
			}
		}
		return null;
	}

}
