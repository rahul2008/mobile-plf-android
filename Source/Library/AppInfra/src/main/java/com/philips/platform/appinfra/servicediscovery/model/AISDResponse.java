package com.philips.platform.appinfra.servicediscovery.model;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.logging.LoggingInterface;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The model class for AISDResponse.
 */

public class AISDResponse {

	public enum AISDPreference {AISDLanguagePreference, AISDCountryPreference}

	private ServiceDiscovery platformURLs = null;
	private ServiceDiscovery propositionURLs = null;
	private final String SDEmptyURL = "https://delete.delete";
	private final AppInfra mAppInfra;


	public AISDResponse(AppInfra appInfra) {
		this.mAppInfra = appInfra;
	}

	public ServiceDiscovery getPlatformURLs() {
		return platformURLs;
	}

	public void setPlatformURLs(ServiceDiscovery platformURLs) {
		this.platformURLs = platformURLs;
	}

	public ServiceDiscovery getPropositionURLs() {
		return propositionURLs;
	}

	public void setPropositionURLs(ServiceDiscovery propositionURLs) {
		this.propositionURLs = propositionURLs;
	}

	public URL getServiceURL(String serviceId, AISDPreference preference,
	                         Map<String, String> replacement) {
		URL propositionUrl = null, platformUrl = null;
		if (getPropositionURLs() != null) {
			propositionUrl = getPropositionURLs().getServiceURLWithServiceID(serviceId, preference, replacement);
		}
		if (getPlatformURLs() != null) {
			platformUrl = getPlatformURLs().getServiceURLWithServiceID(serviceId, preference, replacement);
		}

		if (propositionUrl != null && platformUrl != null) {
			mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, AppInfraLogEventID.AI_SERVICE_DISCOVERY,"Service Discovery Platform URL is overriden by proposition URL ");
		}

		if (propositionUrl != null) {
			if (propositionUrl.toString().equalsIgnoreCase(SDEmptyURL)) {
				mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO,AppInfraLogEventID.AI_SERVICE_DISCOVERY, "Service Discovery Proposition has empty URL , So ignoring platform URL");
				return null;
			}
			return propositionUrl;

		}

		return platformUrl;
	}

	public HashMap<String, ServiceDiscoveryService> getServicesUrl(ArrayList<String> serviceIds,
	                                                               AISDResponse.AISDPreference preference,
	                                                               Map<String, String> replacement) {
		final HashMap<String, ServiceDiscoveryService> response = new HashMap<>();
		HashMap<String, ServiceDiscoveryService> propositionResponse = null, platformResponse = null;
		ServiceDiscoveryService propositionService = null;
		ServiceDiscoveryService platformService = null;

		if (getPropositionURLs() != null) {
			propositionResponse = getPropositionURLs().getServicesWithServiceID(serviceIds, preference, replacement);
		}

		if (getPlatformURLs() != null) {
			platformResponse = getPlatformURLs().getServicesWithServiceID(serviceIds, preference, replacement);
		}

		for (final String serviceId : serviceIds) {

			if(propositionResponse != null) {
				propositionService = propositionResponse.get(serviceId);
			}

			if(platformResponse != null) {
				platformService = platformResponse.get(serviceId);
			}

			if (propositionService != null && platformService != null) {
				if (propositionService.getConfigUrls() != null && platformService.getConfigUrls() != null) {
					mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG,AppInfraLogEventID.AI_SERVICE_DISCOVERY, "Service Discovery Platform URL is overridden by proposition URL for serviceId" + " " + serviceId);
				}
			}

			if (propositionService != null && propositionService.getConfigUrls() != null) {
				if (propositionService.getConfigUrls().equalsIgnoreCase(SDEmptyURL)) {
					propositionService.setConfigUrl(null);
					propositionService.setmError("ServiceDiscovery cannot find the URL for serviceID" + serviceId);
					mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG,AppInfraLogEventID.AI_SERVICE_DISCOVERY, "Service Discovery Proposition has empty URL , So ignoring platform URL for serviceId" + " " + serviceId);
				}
				response.put(serviceId, propositionService);
			} else {
				if (platformService != null) {
					response.put(serviceId, platformService);
				}
			}
		}
		return response;
	}

	public String getLocaleWithPreference(AISDPreference preference) {
		String locale;

		if (getPropositionURLs() != null) {
			locale = getPropositionURLs().getLocaleWithPreference(preference);
			if (locale != null) {
				mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG,AppInfraLogEventID.AI_SERVICE_DISCOVERY, "Service Discovery get Locale With Preference"+locale);
				return locale;
			}
		}

		if (getPlatformURLs() != null) {
			locale = getPlatformURLs().getLocaleWithPreference(preference);
			if (locale != null) {
				mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG,AppInfraLogEventID.AI_SERVICE_DISCOVERY, "Service Discovery get Locale With Preference"+locale);
				return locale;
			}
		}
		return null;
	}

	public String getCountryCode() {
		String country;
		if (getPropositionURLs() != null) {
			country = getPropositionURLs().getCountry();
			if (country != null) {
				mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG,AppInfraLogEventID.AI_SERVICE_DISCOVERY, "Service Discovery get Country Code"+country);
				return country;
			}
		}
		if (getPlatformURLs() != null) {
			country = getPlatformURLs().getCountry();
			if (country != null) {
				mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG,AppInfraLogEventID.AI_SERVICE_DISCOVERY, "Service Discovery get Country Code"+country);
				return country;
			}
		}
		return null;
	}

	public ServiceDiscovery.Error getError() {
		if (getPropositionURLs() != null) {
			return getPropositionURLs().getError();
		}
		if (getPlatformURLs() != null) {
			return getPlatformURLs().getError();
		}
		return null;
	}

	public boolean isSuccess() {

		if (getPropositionURLs() != null) {
			return getPropositionURLs().isSuccess();
		}
		return getPlatformURLs() != null && getPlatformURLs().isSuccess();
	}
}
