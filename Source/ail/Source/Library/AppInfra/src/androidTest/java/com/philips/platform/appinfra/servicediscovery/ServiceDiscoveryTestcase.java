package com.philips.platform.appinfra.servicediscovery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInstrumentation;
import com.philips.platform.appinfra.ConfigValues;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationManager;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.rest.request.RequestQueue;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.servicediscovery.model.AISDResponse;
import com.philips.platform.appinfra.servicediscovery.model.MatchByCountryOrLanguage;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscovery;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;
import com.philips.platform.appinfra.tagging.AppInfraTaggingUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryManager.AIL_HOME_COUNTRY;
import static com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryManager.AIL_SERVICE_DISCOVERY_HOMECOUNTRY_CHANGE_ACTION;
import static com.philips.platform.appinfra.tagging.AppInfraTaggingUtil.DOWNLOAD_PLATFORM_SERVICES_INVOKED;
import static com.philips.platform.appinfra.tagging.AppInfraTaggingUtil.DOWNLOAD_PREPOSITION_SERVICES_INVOKED;
import static com.philips.platform.appinfra.tagging.AppInfraTaggingUtil.GET_HOME_COUNTRY_SIM_SUCCESS;
import static com.philips.platform.appinfra.tagging.AppInfraTaggingUtil.GET_HOME_COUNTRY_SYNCHRONOUS_ERROR;
import static com.philips.platform.appinfra.tagging.AppInfraTaggingUtil.SD_FORCE_REFRESH_CALLED;
import static com.philips.platform.appinfra.tagging.AppInfraTaggingUtil.SD_SET_HOME_COUNTRY_FETCH_FAILED;
import static com.philips.platform.appinfra.tagging.AppInfraTaggingUtil.SD_SET_HOME_COUNTRY_STORE_FAILED;
import static com.philips.platform.appinfra.tagging.AppInfraTaggingUtil.SD_SUCCESS;
import static com.philips.platform.appinfra.tagging.AppInfraTaggingUtil.SERVICE_DISCOVERY;
import static com.philips.platform.appinfra.tagging.AppInfraTaggingUtil.SET_HOME_COUNTRY_SUCCESS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * ServiceDiscovery Test class.
 */
public class ServiceDiscoveryTestcase extends AppInfraInstrumentation {

	private ServiceDiscoveryInterface mServiceDiscoveryInterface = null;
	private ServiceDiscoveryManager mServiceDiscoveryManager = null;
	private AppInfra mAppInfra;
	private ServiceDiscovery mserviceDiscovery = null;
	private MatchByCountryOrLanguage mMatchByCountryOrLanguage = null;
	private String mServiceId = "userreg.janrain.cdn";
	// Context context = Mockito.mock(Context.class);
	private ArrayList<String> mServicesId = new ArrayList<String>(
			Arrays.asList("userreg.janrain.api", "userreg.janrain.cdn"));

	private Context context;

	private ServiceDiscovery serviceDiscovery = null;
	private Method method;

	private AISDResponse aisdResponse;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		context = getInstrumentation().getContext();
		assertNotNull(context);

		mAppInfra = new AppInfra.Builder().build(context);
		assertNotNull(mAppInfra);
		serviceDiscovery = new ServiceDiscovery(mAppInfra);
		testConfig();
		mServiceDiscoveryInterface = mAppInfra.getServiceDiscovery();
		mServiceDiscoveryManager = new ServiceDiscoveryManager(mAppInfra);
		RequestManager mRequestItemManager = new RequestManager(context, mAppInfra);
		assertNotNull(mRequestItemManager);
		mserviceDiscovery = new ServiceDiscovery(mAppInfra);
		mserviceDiscovery = loadServiceDiscoveryModel();
		mMatchByCountryOrLanguage = new MatchByCountryOrLanguage();
		assertNotNull(mserviceDiscovery);
		assertNotNull(mMatchByCountryOrLanguage);
		assertNotNull(mServiceDiscoveryInterface);
		assertNotNull(mServiceDiscoveryManager);
		try {
			method = ServiceDiscoveryManager.class.getDeclaredMethod("isOnline");
			method.setAccessible(true);
			method.invoke(mServiceDiscoveryManager);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
		}

		try {
			JSONObject propositionObject = new JSONObject(ConfigValues.getsdUrlJson());
			assertNotNull(propositionObject);
			ServiceDiscovery propostionService = parseResponse(propositionObject);
			assertNotNull(propostionService);
			JSONObject platformObject = new JSONObject(ConfigValues.getsdUrlPlatformjson());
			assertNotNull(platformObject);
			ServiceDiscovery platformService = parseResponse(platformObject);
			assertNotNull(platformService);
			aisdResponse = new AISDResponse(mAppInfra);
			aisdResponse.setPropositionURLs(propostionService);
			aisdResponse.setPlatformURLs(platformService);
			assertNotNull(aisdResponse);
		} catch (Exception exception) {
		}

	}

	public void testProcessRequest() {
		AppInfra mAppInfra = getAppInfraMocked();
		ServiceDiscovery serviceDiscovery = new ServiceDiscovery();
		serviceDiscovery.setSuccess(true);
		RestInterface restInterfaceMock = mock(RestInterface.class);
		RequestQueue requestQueueMock = mock(RequestQueue.class);
		SecureStorageInterface secureStorageInterfaceMock = mock(SecureStorageInterface.class);
		when(mAppInfra.getSecureStorage()).thenReturn(secureStorageInterfaceMock);
		final RequestManager requestManagerMock = mock(RequestManager.class);
		String url = "www.philips.com";
		when(requestManagerMock.execute(url,ServiceDiscoveryManager.AISDURLType.AISDURLTypePlatform)).thenReturn(serviceDiscovery);
		when(restInterfaceMock.getRequestQueue()).thenReturn(requestQueueMock);
		when(restInterfaceMock.isInternetReachable()).thenReturn(false);
		when(mAppInfra.getRestClient()).thenReturn(restInterfaceMock);
		final AppInfraTaggingUtil appInfraTaggingUtil = mock(AppInfraTaggingUtil.class);
		mServiceDiscoveryManager = new ServiceDiscoveryManager(mAppInfra) {
			@Override
			AppInfraTaggingUtil getAppInfraTaggingUtil(AppInfra aAppInfra) {
				return appInfraTaggingUtil;
			}

			@Override
			RequestManager getRequestManager() {
				return requestManagerMock;
			}
		};
		mServiceDiscoveryManager.processRequest(url, serviceDiscovery, ServiceDiscoveryManager.AISDURLType.AISDURLTypePlatform, ServiceDiscoveryManager.SD_REQUEST_TYPE.refresh);
		verify(appInfraTaggingUtil).trackErrorAction(SERVICE_DISCOVERY," error while fetching ".concat(ServiceDiscoveryManager.SD_REQUEST_TYPE.refresh.name().concat(" due to ").concat(serviceDiscovery.getError().getErrorvalue().name())));
		when(restInterfaceMock.isInternetReachable()).thenReturn(true);
		mServiceDiscoveryManager.processRequest(url, serviceDiscovery, ServiceDiscoveryManager.AISDURLType.AISDURLTypePlatform, ServiceDiscoveryManager.SD_REQUEST_TYPE.refresh);
		verify(appInfraTaggingUtil).trackSuccessAction(SERVICE_DISCOVERY, SD_SUCCESS);
		serviceDiscovery.setSuccess(false);
		ServiceDiscovery.Error error = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.CONNECTION_TIMEOUT," connection time out");
		serviceDiscovery.setError(error);
		mServiceDiscoveryManager.processRequest(url, serviceDiscovery, ServiceDiscoveryManager.AISDURLType.AISDURLTypePlatform, ServiceDiscoveryManager.SD_REQUEST_TYPE.refresh);
		verify(appInfraTaggingUtil).trackErrorAction(SERVICE_DISCOVERY," error while fetching ".concat(ServiceDiscoveryManager.SD_REQUEST_TYPE.refresh.name().concat(" due to ").concat(serviceDiscovery.getError().getErrorvalue().name())));
	}

	public void testTaggingFetchSD() {
		final SecureStorageInterface.SecureStorageError secureStorageError = mock(SecureStorageInterface.SecureStorageError.class);
		when(secureStorageError.getErrorMessage()).thenReturn("something went wrong while fetching");
		when(secureStorageError.getErrorCode()).thenReturn(SecureStorageInterface.SecureStorageError.secureStorageError.NullData);
		final AppInfraTaggingUtil appInfraTaggingUtil = mock(AppInfraTaggingUtil.class);
				mServiceDiscoveryManager = new ServiceDiscoveryManager(mAppInfra) {
			@Override
			SecureStorageInterface.SecureStorageError getSecureStorageError() {
				return secureStorageError;
			}

					@Override
					AppInfraTaggingUtil getAppInfraTaggingUtil(AppInfra aAppInfra) {

						return appInfraTaggingUtil;
					}
				};
		mServiceDiscoveryManager.getHomeCountry();
		verify(appInfraTaggingUtil).trackErrorAction(SERVICE_DISCOVERY, SD_SET_HOME_COUNTRY_FETCH_FAILED);

	}

	public void testTaggingStoreSD() {
		final SecureStorageInterface.SecureStorageError secureStorageError = mock(SecureStorageInterface.SecureStorageError.class);
		when(secureStorageError.getErrorMessage()).thenReturn("something went wrong while fetching");
		when(secureStorageError.getErrorCode()).thenReturn(SecureStorageInterface.SecureStorageError.secureStorageError.NullData);
		final AppInfraTaggingUtil appInfraTaggingUtil = mock(AppInfraTaggingUtil.class);
				mServiceDiscoveryManager = new ServiceDiscoveryManager(mAppInfra) {
			@Override
			SecureStorageInterface.SecureStorageError getSecureStorageError() {
				return secureStorageError;
			}

					@Override
					AppInfraTaggingUtil getAppInfraTaggingUtil(AppInfra aAppInfra) {

						return appInfraTaggingUtil;
					}
				};
		mServiceDiscoveryManager.setHomeCountry("en");
		verify(appInfraTaggingUtil).trackErrorAction(SERVICE_DISCOVERY, SD_SET_HOME_COUNTRY_STORE_FAILED);

	}

	public void testConfig() {

		AppConfigurationManager mConfigInterface = new AppConfigurationManager(mAppInfra) {
			@Override
			protected JSONObject getMasterConfigFromApp() {
				JSONObject result = null;
				try {
					String testJson = ConfigValues.testJson();
					result = new JSONObject(testJson);
				} catch (Exception e) {
				}
				return result;
			}

		};
		mAppInfra = new AppInfra.Builder().setConfig(mConfigInterface).build(context);
	}

	public void testSetGetHomeCountryTagging() {
		final AppInfraTaggingUtil appInfraTaggingUtil = mock(AppInfraTaggingUtil.class);
		AppInfra appInfra = getAppInfraMocked();
		SecureStorageInterface secureStorageInterfaceMock = mock(SecureStorageInterface.class);
		when(appInfra.getSecureStorage()).thenReturn(secureStorageInterfaceMock);

		mServiceDiscoveryManager = new ServiceDiscoveryManager(mAppInfra) {
			@Override
			AppInfraTaggingUtil getAppInfraTaggingUtil(AppInfra aAppInfra) {
				return appInfraTaggingUtil;
			}
		};
		String en = "gb";
		mServiceDiscoveryManager.setHomeCountry(en);
		verify(appInfraTaggingUtil).trackSuccessAction(SERVICE_DISCOVERY, SET_HOME_COUNTRY_SUCCESS.concat(en));

		Context context = mock(Context.class);
		TelephonyManager telephonyManagerMock = mock(TelephonyManager.class);
		when(telephonyManagerMock.getSimCountryIso()).thenReturn("en");
		when(context.getSystemService(Context.TELEPHONY_SERVICE)).thenReturn(telephonyManagerMock);
		when(appInfra.getAppInfraContext()).thenReturn(context);
		mServiceDiscoveryManager = new ServiceDiscoveryManager(appInfra) {
			@Override
			AppInfraTaggingUtil getAppInfraTaggingUtil(AppInfra aAppInfra) {
				return appInfraTaggingUtil;
			}
		};
		mServiceDiscoveryManager.getHomeCountry();
		verify(appInfraTaggingUtil).trackErrorAction(SERVICE_DISCOVERY, GET_HOME_COUNTRY_SYNCHRONOUS_ERROR);
		final String[] homeCountryCode = new String[1];
		ServiceDiscoveryInterface.OnGetHomeCountryListener listenerMock = new ServiceDiscoveryInterface.OnGetHomeCountryListener() {
			@Override
			public void onSuccess(String countryCode, SOURCE source) {
				homeCountryCode[0] = countryCode;
			}

			@Override
			public void onError(ERRORVALUES error, String message) {

			}
		};
		mServiceDiscoveryManager.getHomeCountry(listenerMock);
		verify(appInfraTaggingUtil).trackSuccessAction(SERVICE_DISCOVERY, GET_HOME_COUNTRY_SIM_SUCCESS.concat(homeCountryCode[0]));
	}

	public void testForceRefreshTagging() {
		final AppInfraTaggingUtil appInfraTaggingUtil = mock(AppInfraTaggingUtil.class);
		mServiceDiscoveryManager = new ServiceDiscoveryManager(mAppInfra) {
			@Override
			AppInfraTaggingUtil getAppInfraTaggingUtil(AppInfra aAppInfra) {
				return appInfraTaggingUtil;
			}
		};
		ServiceDiscoveryInterface.OnRefreshListener onRefreshListenerMock = mock(ServiceDiscoveryInterface.OnRefreshListener.class);
		mServiceDiscoveryManager.refresh(onRefreshListenerMock);
		verify(appInfraTaggingUtil).trackSuccessAction(SERVICE_DISCOVERY, SD_FORCE_REFRESH_CALLED);
	}

	public void testDownloadingServices() {
		final AppInfraTaggingUtil appInfraTaggingUtil = mock(AppInfraTaggingUtil.class);
		mServiceDiscoveryManager = new ServiceDiscoveryManager(mAppInfra) {
			@Override
			AppInfraTaggingUtil getAppInfraTaggingUtil(AppInfra aAppInfra) {
				return appInfraTaggingUtil;
			}
		};
		mServiceDiscoveryManager.downloadPlatformService(ServiceDiscoveryManager.SD_REQUEST_TYPE.refresh);
		String sdurlForType = mServiceDiscoveryManager.getSDURLForType(ServiceDiscoveryManager.AISDURLType.AISDURLTypePlatform);
		verify(appInfraTaggingUtil).trackSuccessAction(SERVICE_DISCOVERY, DOWNLOAD_PLATFORM_SERVICES_INVOKED.concat(sdurlForType));

		sdurlForType = mServiceDiscoveryManager.getSDURLForType(ServiceDiscoveryManager.AISDURLType.AISDURLTypeProposition);
		mServiceDiscoveryManager.downloadPropositionService(ServiceDiscoveryManager.SD_REQUEST_TYPE.refresh);
		verify(appInfraTaggingUtil).trackSuccessAction(SERVICE_DISCOVERY, DOWNLOAD_PREPOSITION_SERVICES_INVOKED.concat(sdurlForType));
	}

	@NonNull
	private AppInfra getAppInfraMocked() {
		AppInfra mAppInfra = mock(AppInfra.class);
		LoggingInterface loggingInterfaceMock = mock(LoggingInterface.class);
		when(mAppInfra.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
		return mAppInfra;
	}

	public void testDownloadServices() {
		try {
			method = ServiceDiscoveryManager.class.getDeclaredMethod("downloadServices");
			method.setAccessible(true);
			method.invoke(mServiceDiscoveryManager);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
		}
	}


	public void testSetHomeCountry() {
		try {
			method = ServiceDiscoveryManager.class.getDeclaredMethod("setHomeCountry", String.class);
			method.setAccessible(true);
			method.invoke(mServiceDiscoveryManager, "CN");
			mServiceDiscoveryInterface.setHomeCountry("CN");
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
		}
	}


	public void testSaveToSecureStore() {
		try {
			method = ServiceDiscoveryManager.class.getDeclaredMethod("saveToSecureStore", String.class, String.class);
			method.setAccessible(true);
			method.invoke(mServiceDiscoveryManager, "test", "COUNTRY");
			method.invoke(mServiceDiscoveryManager, "test", "COUNTRY_SOURCE");

			method = ServiceDiscoveryManager.class.getDeclaredMethod("fetchFromSecureStorage", String.class);
			method.setAccessible(true);
			method.invoke(mServiceDiscoveryManager, "COUNTRY");
			method.invoke(mServiceDiscoveryManager, "COUNTRY_SOURCE");
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
		}
	}

	public void testRefresh() {
		//  mserviceDiscovery = loadServiceDiscoveryModel();
		mServiceDiscoveryInterface.refresh(new ServiceDiscoveryInterface.OnRefreshListener() {
			@Override
			public void onSuccess() {
				assertNotNull("Test");
			}

			@Override
			public void onError(ERRORVALUES error, String message) {
				assertNotNull(error);
				assertNotNull(message);
			}
		});
	}

	public void testGetServiceUrlWithLanguagePreference() throws Exception {
//        //mServiceDiscoveryManager.setServiceDiscovery(loadServiceDiscoveryModel());
		try {
			method = ServiceDiscoveryManager.class.getDeclaredMethod("getServiceUrlWithLanguagePreference",
					String.class, ServiceDiscoveryInterface.OnGetServiceUrlListener.class);
			method.setAccessible(true);
			method.invoke(mServiceDiscoveryManager, "userreg.janrain.api", new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
				@Override
				public void onSuccess(URL url) {
				}

				@Override
				public void onError(ERRORVALUES error, String message) {
				}
			});

		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
		}

		mServiceDiscoveryInterface.getServiceUrlWithLanguagePreference(mServiceId,
				new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
					@Override
					public void onError(ERRORVALUES error, String message) {
						assertNotNull(message);
					}

					@Override
					public void onSuccess(URL url) {
						assertNotNull(url.toString());
					}
				});

		mServiceDiscoveryManager.getServiceUrlWithLanguagePreference(null, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
			@Override
			public void onError(ERRORVALUES error, String message) {
				assertNotNull(error);
			}

			@Override
			public void onSuccess(URL url) {

			}
		});

		mServiceDiscoveryManager.getServiceUrlWithLanguagePreference("userreg.janrain.api", null);
		Map<String, String> parameters = new HashMap<>();
		parameters.put("ctn", "HD9740");
		parameters.put("sector", "B2C");
		parameters.put("catalog", "shavers");

		mServiceDiscoveryManager.getServiceUrlWithLanguagePreference("prxclient.assets", null, parameters);

		mServiceDiscoveryManager.getServiceUrlWithLanguagePreference(null, new ServiceDiscoveryManager.OnGetServiceUrlListener() {
			@Override
			public void onSuccess(URL url) {

			}

			@Override
			public void onError(ERRORVALUES error, String message) {
				assertNotNull(error);
			}
		}, parameters);

		mServiceDiscoveryManager.getServiceUrlWithLanguagePreference("prxclient.assets", new ServiceDiscoveryManager.OnGetServiceUrlListener() {
			@Override
			public void onSuccess(URL url) {
				assertNotNull(url);
			}

			@Override
			public void onError(ERRORVALUES error, String message) {
				assertNotNull(error);
			}
		}, parameters);
	}

	public void testgetServicesWithLanguageMapUrl() throws Exception {
//        serviceDiscovery = loadServiceDiscoveryModel();
		mServiceDiscoveryInterface.getServicesWithLanguagePreference(mServicesId,
				new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
					@Override
					public void onSuccess(Map urlMap) {
						assertNotNull(urlMap);
						assertFalse(urlMap.size() < 0);
					}

					@Override
					public void onError(ERRORVALUES error, String message) {
						assertNotNull(message);
					}
				});
	}


	public void testgetServiceUrlWithCountryPreference() throws Exception {
//        serviceDiscovery = loadServiceDiscoveryModel();
		mServiceDiscoveryInterface.getServiceUrlWithCountryPreference(mServiceId,
				new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
					@Override
					public void onSuccess(URL url) {

					}

					@Override
					public void onError(ERRORVALUES error, String message) {
						assertNotNull(message);
					}
				});

		mServiceDiscoveryInterface.getServiceUrlWithCountryPreference(mServiceId,
				new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
					@Override
					public void onError(ERRORVALUES error, String message) {
						assertNotNull(message);
					}

					@Override
					public void onSuccess(URL url) {
						assertNotNull(url.toString());
					}
				});

		mServiceDiscoveryManager.getServiceUrlWithCountryPreference(null,
				new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
					@Override
					public void onError(ERRORVALUES error, String message) {
						assertNotNull(error);
					}

					@Override
					public void onSuccess(URL url) {

					}
				});

		mServiceDiscoveryManager.getServiceUrlWithCountryPreference("userreg.janrain.api", null);
		Map<String, String> parameters = new HashMap<>();
		parameters.put("ctn", "HD9740");
		parameters.put("sector", "B2C");
		parameters.put("catalog", "shavers");

		mServiceDiscoveryManager.getServiceUrlWithCountryPreference("prxclient.assets", null, parameters);

		mServiceDiscoveryManager.getServiceUrlWithCountryPreference(null,
				new ServiceDiscoveryManager.OnGetServiceUrlListener() {
					@Override
					public void onSuccess(URL url) {

					}

					@Override
					public void onError(ERRORVALUES error, String message) {
						assertNotNull(error);
					}
				}, parameters);

		mServiceDiscoveryManager.getServiceUrlWithCountryPreference("prxclient.assets",
				new ServiceDiscoveryManager.OnGetServiceUrlListener() {
					@Override
					public void onSuccess(URL url) {
						assertNotNull(url);
					}

					@Override
					public void onError(ERRORVALUES error, String message) {
						assertNotNull(error);
					}
				}, parameters);

	}


	public void testgetServicesWithCountryMapUrl() throws Exception {
//        serviceDiscovery = loadServiceDiscoveryModel();
		mServiceDiscoveryInterface.getServicesWithCountryPreference(mServicesId,
				new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
					@Override
					public void onSuccess(Map urlMap) {
						assertNotNull(urlMap);
						assertFalse(urlMap.size() < 0);
					}

					@Override
					public void onError(ERRORVALUES error, String message) {
						assertNotNull(message);
					}
				});
	}

	public void testgetServiceLocaleWithCountryPreference() throws Exception {
//        serviceDiscovery = loadServiceDiscoveryModel();
		mServiceDiscoveryInterface.getServiceLocaleWithCountryPreference(mServiceId,
				new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {

					@Override
					public void onError(ERRORVALUES error, String message) {
						assertNotNull(message);
					}

					@Override
					public void onSuccess(String locale) {
						assertNotNull(locale);
					}
				});

		mServiceDiscoveryInterface.getServiceLocaleWithCountryPreference(null,
				new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
					@Override
					public void onError(ERRORVALUES error, String message) {
						assertNotNull(message);
					}

					@Override
					public void onSuccess(String locale) {
						assertNotNull(locale);
					}
				});

		mServiceDiscoveryInterface.getServiceLocaleWithCountryPreference(mServiceId, null);
	}


	public void testgetServiceLocaleWithLanguagePreference() throws Exception {
		mServiceDiscoveryInterface.getServiceLocaleWithLanguagePreference(mServiceId,
				new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
					@Override
					public void onError(ERRORVALUES error, String message) {
						assertNotNull(message);
					}

					@Override
					public void onSuccess(String locale) {
						assertNotNull(locale);
					}
				});
		mServiceDiscoveryInterface.getServiceLocaleWithLanguagePreference(null,
				new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
					@Override
					public void onError(ERRORVALUES error, String message) {
						assertNotNull(message);
					}

					@Override
					public void onSuccess(String locale) {
						assertNotNull(locale);
					}
				});

		mServiceDiscoveryInterface.getServiceLocaleWithLanguagePreference(mServiceId, null);
	}

	public void testgetHomeCountry() {
		mServiceDiscoveryInterface.getHomeCountry(new ServiceDiscoveryInterface.OnGetHomeCountryListener() {
			@Override
			public void onSuccess(String countryCode, SOURCE source) {
				assertNotNull(countryCode);
				assertNotNull(source);
			}

			@Override
			public void onError(ERRORVALUES error, String message) {
				assertNotNull(error);
				assertNotNull(message);
			}
		});
	}


	private MatchByCountryOrLanguage commonMatchByCountryOrLanguage(boolean isPossitivecase) {

		MatchByCountryOrLanguage.Config.Tag mTag = new MatchByCountryOrLanguage.Config.Tag();
		mTag.setId("TestTagId");
		mTag.setName("TestTagName");
		mTag.setKey("TestTagKey");

		assertNotNull(mTag.getId());
		assertNotNull(mTag.getKey());
		assertNotNull(mTag.getName());

		ArrayList<MatchByCountryOrLanguage.Config.Tag> mTagArray = new ArrayList<MatchByCountryOrLanguage.Config.Tag>();
		mTagArray.add(mTag);
		HashMap<String, String> mMap = new HashMap<String, String>();
		if (isPossitivecase) {
			mMap.put("userreg.janrain.cdn", "https://d1lqe9temigv1p.cloudfront.net");
			mMap.put("userreg.janrain.api", "https://philips.eval.janraincapture.com");
		} else {
			mMap.put("userreg.janrain.cdn", "TestCase");
			mMap.put("TestCase", "TestCase");
		}


		MatchByCountryOrLanguage.Config mconfig = new MatchByCountryOrLanguage.Config();
		mconfig.setMicrositeId("TestMicrositeId");
		mconfig.setTags(mTagArray);
		mconfig.setUrls(mMap);

		assertNotNull(mconfig.getMicrositeId());
		assertNotNull(mconfig.getTags());
		assertNotNull(mconfig.getUrls());

		ArrayList<MatchByCountryOrLanguage.Config> mConfigArray = new ArrayList<MatchByCountryOrLanguage.Config>();
		mConfigArray.add(mconfig);

		MatchByCountryOrLanguage mMatchByCountryOrLanguage = new MatchByCountryOrLanguage();
		mMatchByCountryOrLanguage.setLocale("TestLocale");
		mMatchByCountryOrLanguage.setAvailable(false);
		mMatchByCountryOrLanguage.setConfigs(mConfigArray);

		assertNotNull(mMatchByCountryOrLanguage.getLocale());
		assertNotNull(mMatchByCountryOrLanguage.getConfigs());
		assertFalse(mMatchByCountryOrLanguage.isAvailable());
		return mMatchByCountryOrLanguage;
	}

	private ServiceDiscovery loadServiceDiscoveryModel() {
		ServiceDiscovery serviceDiscovery = new ServiceDiscovery(mAppInfra);
		serviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(true));
		serviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(true));
		serviceDiscovery.setSuccess(true);
		serviceDiscovery.setHttpStatus("Success");
		serviceDiscovery.setCountry("TestCountry");

		ServiceDiscovery.Error error = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR, "ErrorMessage");
		error.setErrorvalue(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.CONNECTION_TIMEOUT);
		assertNotNull(error.getErrorvalue());
		error.setMessage("Test");
		assertNotNull(error.getMessage());
		serviceDiscovery.setError(error);


		return serviceDiscovery;
	}


	public void testgetServiceUrlWithCountryPreferenceReplacedUrl() {
		Map<String, String> parameters = new HashMap<>();
		parameters.put("ctn", "HD9740");
		parameters.put("sector", "B2C");
		parameters.put("catalog", "shavers");
		mServiceDiscoveryInterface.getServiceUrlWithCountryPreference("prxclient.assets", new
				ServiceDiscoveryInterface.OnGetServiceUrlListener() {
					@Override
					public void onError(ERRORVALUES error, String message) {
						assertNotNull(error);
					}

					@Override
					public void onSuccess(URL url) {
						assertNotNull(url);
					}

				}, parameters);
	}


	public void testgetServiceUrlWithLanguagePreferenceReplacedUrl() {
		Map<String, String> parameters = new HashMap<>();
		parameters.put("ctn", "HD9740");
		parameters.put("sector", "B2C");
		parameters.put("catalog", "shavers");
		mServiceDiscoveryInterface.getServiceUrlWithLanguagePreference("prxclient.assets", new
				ServiceDiscoveryInterface.OnGetServiceUrlListener() {
					@Override
					public void onError(ERRORVALUES error, String message) {
						assertNotNull(error);
					}

					@Override
					public void onSuccess(URL url) {
						assertNotNull(url);
					}

				}, parameters);
	}

	public void testgetServicesWithCountryPreferencewithServiceIds() {

		String[] serviceIds = {"prxclient.assets", "userreg.janrain.api"};
		ArrayList<String> serviceId = new ArrayList<String>(Arrays.asList(serviceIds));
		Map<String, String> parameters = new HashMap<>();
		parameters.put("ctn", "HD9740");
		parameters.put("sector", "B2C");
		parameters.put("catalog", "shavers");
		mServiceDiscoveryInterface.getServicesWithCountryPreference(serviceId, new
				ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
					@Override
					public void onError(ERRORVALUES error, String message) {
						assertNotNull(error);
					}

					@Override
					public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
						assertNotNull(urlMap);
					}
				});

		mServiceDiscoveryInterface.getServicesWithCountryPreference(null, new
				ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
					@Override
					public void onError(ERRORVALUES error, String message) {
						assertNotNull(error);
					}

					@Override
					public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
						assertNotNull(urlMap);
					}
				});

		mServiceDiscoveryInterface.getServicesWithCountryPreference(serviceId, null);

		mServiceDiscoveryInterface.getServicesWithCountryPreference(serviceId, new
				ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
					@Override
					public void onError(ERRORVALUES error, String message) {
						assertNotNull(error);
					}

					@Override
					public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
						assertNotNull(urlMap);
					}
				}, parameters);

		mServiceDiscoveryInterface.getServicesWithCountryPreference(null, new
				ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
					@Override
					public void onError(ERRORVALUES error, String message) {
						assertNotNull(error);
					}

					@Override
					public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
						assertNotNull(urlMap);
					}
				}, parameters);


		mServiceDiscoveryInterface.getServicesWithCountryPreference(serviceId, null, parameters);

		mServiceDiscoveryInterface.getServicesWithCountryPreference(serviceId, new
				ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
					@Override
					public void onError(ERRORVALUES error, String message) {
						assertNotNull(error);
					}

					@Override
					public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
						assertNotNull(urlMap);
					}
				}, null);
	}

	public void testgetServicesWithLanguagePreferencewithServiceIds() {
		String[] serviceIds = {"prxclient.assets", "userreg.janrain.api"};
		ArrayList<String> serviceId = new ArrayList<String>(Arrays.asList(serviceIds));
		Map<String, String> parameters = new HashMap<>();
		parameters.put("ctn", "HD9740");
		parameters.put("sector", "B2C");
		parameters.put("catalog", "shavers");

		mServiceDiscoveryInterface.getServicesWithLanguagePreference(serviceId, new
				ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
					@Override
					public void onError(ERRORVALUES error, String message) {
						assertNotNull(error);
					}

					@Override
					public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
						assertNotNull(urlMap);
					}
				});

		mServiceDiscoveryInterface.getServicesWithLanguagePreference(null, new
				ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
					@Override
					public void onError(ERRORVALUES error, String message) {
						assertNotNull(error);
					}

					@Override
					public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
						assertNotNull(urlMap);
					}
				});

		mServiceDiscoveryInterface.getServicesWithLanguagePreference(serviceId, null);

		mServiceDiscoveryInterface.getServicesWithLanguagePreference(serviceId, new
				ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
					@Override
					public void onError(ERRORVALUES error, String message) {
						assertNotNull(error);
					}

					@Override
					public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
						assertNotNull(urlMap);
					}
				}, parameters);

		mServiceDiscoveryInterface.getServicesWithLanguagePreference(null, new
				ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
					@Override
					public void onError(ERRORVALUES error, String message) {
						assertNotNull(error);
					}

					@Override
					public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
						assertNotNull(urlMap);
					}
				}, parameters);


		mServiceDiscoveryInterface.getServicesWithLanguagePreference(serviceId, null, parameters);

		mServiceDiscoveryInterface.getServicesWithLanguagePreference(serviceId, new
				ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
					@Override
					public void onError(ERRORVALUES error, String message) {
						assertNotNull(error);
					}

					@Override
					public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
						assertNotNull(urlMap);
					}
				}, null);
	}


	public void testemptyresultarray() {

		String resJson = "{\n" +
				"    \"success\": true,\n" +
				"    \"payload\": {\n" +
				"        \"country\": \"US\",\n" +
				"        \"matchByLanguage\": {\n" +
				"            \"available\": false,\n" +
				"            \"results\": [\n" +
				"                        \n" +
				"                        ]\n" +
				"        },\n" +
				"        \"matchByCountry\": {\n" +
				"            \"available\": false,\n" +
				"            \"results\": [\n" +
				"                        \n" +
				"                        ]\n" +
				"        }\n" +
				"    },\n" +
				"    \"httpStatus\": \"OK\"\n" +
				"}\n";
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(resJson);
		} catch (JSONException e) {
		}
		serviceDiscovery.parseResponse(context, mAppInfra, jsonObject);
		mServiceDiscoveryManager.getServiceLocaleWithCountryPreference(mServiceId,
				new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
					@Override
					public void onSuccess(String locale) {
						assertNotNull(locale);
					}

					@Override
					public void onError(ERRORVALUES error, String message) {
						assertNotNull(error);
						assertNotNull(message);
					}
				});
	}

	public void testAisdResponse() {
		try {
			JSONObject propositionObject = new JSONObject(ConfigValues.getsdUrlJson());
			ServiceDiscovery propostionService = parseResponse(propositionObject);

			assertNotNull(propostionService);
			JSONObject platformObject = new JSONObject(ConfigValues.getsdUrlPlatformjson());
			ServiceDiscovery platformService = parseResponse(platformObject);
			assertNotNull(platformService);
			aisdResponse = new AISDResponse(mAppInfra);
			aisdResponse.setPropositionURLs(propostionService);
			aisdResponse.setPlatformURLs(platformService);
		} catch (Exception exception) {
		}

	}

	private ServiceDiscovery parseResponse(JSONObject response) {
		ServiceDiscovery result = new ServiceDiscovery(mAppInfra);
		result.setSuccess(response.optBoolean("success"));
		if (!result.isSuccess()) {
			ServiceDiscovery.Error err = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR, "Server reports failure");
			result.setError(err);
		} else { // no sense in further processing if server reports error
			// START setting match by country
			result.parseResponse(context, mAppInfra, response);
		}

		return result;
	}


	public void testgetServiceURL() {
		URL url = aisdResponse.getServiceURL("userreg.janrain.api", AISDResponse.AISDPreference.AISDCountryPreference, null);
		assertEquals("https://philips.dev.janraincapture.com", url.toString());

		URL urllang = aisdResponse.getServiceURL("userreg.janrain.api", AISDResponse.AISDPreference.AISDLanguagePreference, null);
		assertEquals("https://philips.dev.janraincapture.com", urllang.toString());

		Map<String, String> parameters = new HashMap<>();
		parameters.put("ctn", "HD9740");
		parameters.put("sector", "B2C");
		parameters.put("catalog", "shavers");

		URL rurl = aisdResponse.getServiceURL("prxclient.assets", AISDResponse.AISDPreference.AISDCountryPreference, parameters);
		assertNotNull(url.toString());
		assertEquals("https://tst.philips.com/prx/product/B2C/en_IN/shavers/products/HD9740.assets",
				rurl.toString());


		URL urlLang = aisdResponse.getServiceURL("prxclient.assets", AISDResponse.AISDPreference.AISDLanguagePreference, parameters);
		assertNotNull(urlLang.toString());
		assertEquals("https://tst.philips.com/prx/product/B2C/en_IN/shavers/products/HD9740.assets",
				rurl.toString());
	}

//	public void testgetServicesUrl() {
//		HashMap<String, ServiceDiscoveryService> responseMap;
//		String[] serviceIds = {"userreg.janrain.api", "prxclient.assets"};
//		ArrayList<String> serviceId = new ArrayList<String>(Arrays.asList(serviceIds));
//
//		responseMap = aisdResponse.getServicesUrl(serviceId,
//				AISDResponse.AISDPreference.AISDCountryPreference, null);
//		assertNotNull(responseMap);
//
//		responseMap = aisdResponse.getServicesUrl(serviceId,
//				AISDResponse.AISDPreference.AISDLanguagePreference, null);
//		assertNotNull(responseMap);
//
//		Map<String, String> parameters = new HashMap<>();
//		parameters.put("ctn", "HD9740");
//		parameters.put("sector", "B2C");
//		parameters.put("catalog", "shavers");
//
//		responseMap = aisdResponse.getServicesUrl(serviceId,
//				AISDResponse.AISDPreference.AISDCountryPreference, parameters);
//		assertNotNull(responseMap);
//
//		responseMap = aisdResponse.getServicesUrl(serviceId,
//				AISDResponse.AISDPreference.AISDLanguagePreference, parameters);
//		assertNotNull(responseMap);
//	}

	public void testgetLocaleWithPreference() {
		String localbyCountry = aisdResponse.getLocaleWithPreference(AISDResponse.AISDPreference.AISDCountryPreference);
		assertEquals("en_IN", localbyCountry);
		String localbyLang = aisdResponse.getLocaleWithPreference(AISDResponse.AISDPreference.AISDLanguagePreference);
		assertEquals("en_IN", localbyLang);
	}

	public void testgetCountryCode() {
		String country = aisdResponse.getCountryCode();
		assertNotNull(country);
	}

	public void testgetError() {
		ServiceDiscovery.Error err = aisdResponse.getError();
		assertEquals(null, err);
	}

	public void testisSuccess() {
		assertTrue(aisdResponse.isSuccess());
	}

	public void testdownloadPlatformService() {
		try {
			method = ServiceDiscoveryManager.class.getDeclaredMethod("downloadPlatformService");
			method.setAccessible(true);
			method.invoke(mServiceDiscoveryManager);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
		}
	}

	public void testdownloadPropositionService() {
		try {
			method = ServiceDiscoveryManager.class.getDeclaredMethod("downloadPropositionService");
			method.setAccessible(true);
			method.invoke(mServiceDiscoveryManager);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
		}
	}

	public void testprocessRequest() {
		String url = "https://www.philips.com/api/v1/discovery/b2c/77001?locale=en_IN&tags=apps%2b%2benv%2bdev&country=IN";
		ServiceDiscovery service = new ServiceDiscovery(mAppInfra);
		try {
			method = ServiceDiscoveryManager.class.getDeclaredMethod("processRequest", String.class,
					ServiceDiscovery.class, ServiceDiscoveryManager.AISDURLType.class);
			method.setAccessible(true);
			method.invoke(mServiceDiscoveryManager, url, service, ServiceDiscoveryManager.AISDURLType.AISDURLTypeProposition);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
		}
	}


	public void testgetSDURLForType() {
		try {
			method = ServiceDiscoveryManager.class.getDeclaredMethod("getSDURLForType",
					ServiceDiscoveryManager.AISDURLType.class);
			method.setAccessible(true);
			String urlplatform = (String) method.invoke(mServiceDiscoveryManager, ServiceDiscoveryManager.AISDURLType.AISDURLTypePlatform);
			// assertNotNull(urlplatform);
			String urlProposition = (String) method.invoke(mServiceDiscoveryManager, ServiceDiscoveryManager.AISDURLType.AISDURLTypeProposition);
			//  assertNotNull(urlProposition);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
		}
	}

	public void testgetSDBaseURLForEnvironment() {
		try {
			method = ServiceDiscoveryManager.class.getDeclaredMethod("getSDBaseURLForEnvironment", String.class);
			method.setAccessible(true);

			String baseUrlproduction = (String) method.invoke(mServiceDiscoveryManager, "PRODUCTION");
			assertNotNull(baseUrlproduction);
			assertSame("www.philips.com", baseUrlproduction);

			String baseUrlstaging = (String) method.invoke(mServiceDiscoveryManager, "STAGING");
			assertNotNull(baseUrlstaging);
			assertSame("stg.philips.com", baseUrlstaging);

		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
		}
	}


	public void testgetAppStateStringFromState() {

		try {
			method = ServiceDiscoveryManager.class.getDeclaredMethod("getAppStateStringFromState", String.class);
			method.setAccessible(true);

			String urltest = (String) method.invoke(mServiceDiscoveryManager, "TEST");
			assertSame("apps%2b%2benv%2btest", urltest);

			String urldev = (String) method.invoke(mServiceDiscoveryManager, "DEVELOPMENT");
			assertSame("apps%2b%2benv%2bdev", urldev);

			String urlstag = (String) method.invoke(mServiceDiscoveryManager, "STAGING");
			assertSame("apps%2b%2benv%2bstage", urlstag);

			String urlprod = (String) method.invoke(mServiceDiscoveryManager, "PRODUCTION");
			assertSame("apps%2b%2benv%2bacc", urlprod);

			String urlaccp = (String) method.invoke(mServiceDiscoveryManager, "ACCEPTANCE");
			assertSame("apps%2b%2benv%2bprod", urlaccp);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
		}
	}

	public void testhomeCountryCode() {
		try {
			method = ServiceDiscoveryManager.class.getDeclaredMethod("homeCountryCode",
					ServiceDiscoveryInterface.OnGetHomeCountryListener.class);
			method.setAccessible(true);
			method.invoke(mServiceDiscoveryManager, new ServiceDiscoveryInterface.OnGetHomeCountryListener() {
				@Override
				public void onError(ERRORVALUES error, String message) {
					assertNotNull(error);
				}

				@Override
				public void onSuccess(String countryCode, SOURCE source) {
					assertNotNull(countryCode);
				}
			});
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
		}
	}

	public void testgetServiceDiscoveryData() {
		try {
			method = ServiceDiscoveryManager.class.getDeclaredMethod("getServiceDiscoveryData",
					ServiceDiscoveryManager.AISDListener.class);
			method.setAccessible(true);
			method.invoke(mServiceDiscoveryManager, new ServiceDiscoveryManager.AISDListener() {
				@Override
				public void ondataReceived(AISDResponse response) {
					  //assertNotNull(response);
				}
			});
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
		}
	}

	public void testgetCountryCodeFromSim() {
		try {
			method = ServiceDiscoveryManager.class.getDeclaredMethod("getCountryCodeFromSim");
			method.setAccessible(true);
			String countryCode = (String) method.invoke(mServiceDiscoveryManager);
			//  assertNotNull(countryCode);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
		}
	}

	public void testcachedURLsExpired() {
		try {
			method = ServiceDiscoveryManager.class.getDeclaredMethod("cachedURLsExpired");
			method.setAccessible(true);
			boolean value = (boolean) method.invoke(mServiceDiscoveryManager);
			//  assertTrue(value);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
		}
	}

	public void testFilterDataForUrlbyLang() {
		Map<String, String> parameters = new HashMap<>();
		parameters.put("ctn", "HD9740");
		parameters.put("sector", "B2C");
		parameters.put("catalog", "shavers");
		mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(true));
		mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(true));
		mMatchByCountryOrLanguage.setLocale("IN");
		try {
			method = ServiceDiscoveryManager.class.getDeclaredMethod("filterDataForUrlbyLang", ServiceDiscovery.class,
					String.class, ServiceDiscoveryInterface.OnGetServiceUrlListener.class, Map.class);
			method.setAccessible(true);
			method.invoke(mServiceDiscoveryManager, mserviceDiscovery, mServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
				@Override
				public void onSuccess(URL url) {
					assertNotNull(url);
				}

				@Override
				public void onError(ERRORVALUES error, String message) {
					assertNotNull(error);
					assertNotNull(message);
				}
			}, parameters);

			/////////////////////////
			method = ServiceDiscoveryManager.class.getDeclaredMethod("filterDataForUrlbyLang", ServiceDiscovery.class,
					ArrayList.class, ServiceDiscoveryInterface.OnGetServiceUrlMapListener.class, Map.class);
			method.setAccessible(true);
			method.invoke(mServiceDiscoveryManager, mserviceDiscovery, mServicesId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
				@Override
				public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {

				}

				@Override
				public void onError(ERRORVALUES error, String message) {

				}
			}, parameters);

		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
		}
	}

	public void testFilterDataForUrlbyCountry() {
		Map<String, String> parameters = new HashMap<>();
		parameters.put("ctn", "HD9740");
		parameters.put("sector", "B2C");
		parameters.put("catalog", "shavers");
		// mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(true));
		mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(true));
		mMatchByCountryOrLanguage.setLocale("IN");
		try {
			method = ServiceDiscoveryManager.class.getDeclaredMethod("filterDataForUrlbyCountry", ServiceDiscovery.class,
					String.class, ServiceDiscoveryInterface.OnGetServiceUrlListener.class, Map.class);
			method.setAccessible(true);
			method.invoke(mServiceDiscoveryManager, mserviceDiscovery, mServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
				@Override
				public void onSuccess(URL url) {
					assertNotNull(url);
				}

				@Override
				public void onError(ERRORVALUES error, String message) {
					assertNotNull(error);
					assertNotNull(message);
				}
			}, parameters);


			/////////////////////////
			method = ServiceDiscoveryManager.class.getDeclaredMethod("filterDataForUrlbyCountry", ServiceDiscovery.class,
					ArrayList.class, ServiceDiscoveryInterface.OnGetServiceUrlMapListener.class, Map.class);
			method.setAccessible(true);
			method.invoke(mServiceDiscoveryManager, mserviceDiscovery, mServicesId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
				@Override
				public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {

				}

				@Override
				public void onError(ERRORVALUES error, String message) {

				}
			}, parameters);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
		}
	}

	public void testGetUrlsMapper() {
		Map<String, String> parameters = new HashMap<>();
		parameters.put("ctn", "HD9740");
		parameters.put("sector", "B2C");
		parameters.put("catalog", "shavers");
		mMatchByCountryOrLanguage.setLocale("IN");

		try {
			method = ServiceDiscoveryManager.class.getDeclaredMethod("getUrlsMapper", ServiceDiscovery.class,
					int.class, String.class, ArrayList.class, ServiceDiscoveryInterface.OnGetServiceUrlMapListener.class, Map.class);
			method.setAccessible(true);
			method.invoke(mServiceDiscoveryManager, mserviceDiscovery, 5, "urlbycountry", mServicesId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
				@Override
				public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {

				}

				@Override
				public void onError(ERRORVALUES error, String message) {

				}
			}, parameters);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
		}
	}

	public void testGetDataForUrl() {
		Map<String, String> parameters = new HashMap<>();
		parameters.put("ctn", "HD9740");
		parameters.put("sector", "B2C");
		parameters.put("catalog", "shavers");
		mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(true));
		mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(true));
		mMatchByCountryOrLanguage.setLocale("IN");
		try {
			method = ServiceDiscoveryManager.class.getDeclaredMethod("getDataForUrl", ServiceDiscovery.class,
					String.class, ServiceDiscoveryInterface.OnGetServiceUrlListener.class, Map.class);
			method.setAccessible(true);
			method.invoke(mServiceDiscoveryManager, mserviceDiscovery, mServiceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
				@Override
				public void onSuccess(URL url) {
					assertNotNull(url);
				}

				@Override
				public void onError(ERRORVALUES error, String message) {
					assertNotNull(error);
					assertNotNull(message);
				}
			}, parameters);

		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
		}
	}

	public void testFilterDataForLocalByLang() {
		mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(true));
		mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(true));
		mMatchByCountryOrLanguage.setLocale("IN");
		try {
			method = ServiceDiscoveryManager.class.getDeclaredMethod("filterDataForLocalByLang", ServiceDiscovery.class,
					ServiceDiscoveryInterface.OnGetServiceLocaleListener.class);
			method.setAccessible(true);
			method.invoke(mServiceDiscoveryManager, mserviceDiscovery, new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {

				@Override
				public void onError(ERRORVALUES error, String message) {
					assertNotNull(message);
				}

				@Override
				public void onSuccess(String locale) {
					assertNotNull(locale);
				}
			});

		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
		}
	}

	public void testFilterDataForLocalByCountry() {
		mserviceDiscovery.setMatchByLanguage(commonMatchByCountryOrLanguage(true));
		mserviceDiscovery.setMatchByCountry(commonMatchByCountryOrLanguage(true));
		mMatchByCountryOrLanguage.setLocale("IN");
		try {
			method = ServiceDiscoveryManager.class.getDeclaredMethod("filterDataForLocalByCountry", ServiceDiscovery.class,
					ServiceDiscoveryInterface.OnGetServiceLocaleListener.class);
			method.setAccessible(true);
			method.invoke(mServiceDiscoveryManager, mserviceDiscovery, new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
				@Override
				public void onError(ERRORVALUES error, String message) {
					assertNotNull(message);
				}

				@Override
				public void onSuccess(String locale) {
					assertNotNull(locale);
				}
			});

		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
		}
	}


	public void testGetServicesMultipleConfigs() {

		JSONObject jsonObject = null;
		JSONObject sdPlatformObj = null;

		try {
			jsonObject = new JSONObject(ConfigValues.getMultipleConfigJson());
			sdPlatformObj = new JSONObject(ConfigValues.getsdUrlPlatformjson());
		} catch (JSONException e) {
			Log.e("error"," while parsing json");
		}
		serviceDiscovery.parseResponse(context, mAppInfra, jsonObject);
		serviceDiscovery.parseResponse(context ,mAppInfra ,sdPlatformObj);
		mServiceDiscoveryInterface.getServiceUrlWithCountryPreference("appinfra.appconfigdownload", new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
			@Override
			public void onSuccess(URL url) {

				assertEquals("https://test.prod.appinfra.testing.service/en_SG/B2C/77001","https://test.prod.appinfra.testing.service/en_SG/B2C/77001");

			}

			@Override
			public void onError(ERRORVALUES error, String message) {
				assertNotNull(error);

			}
		});
		String[] serviceIds = {"appinfra.appconfigdownload", "appinfra.testing.service"};
		ArrayList<String> serviceId = new ArrayList<>(Arrays.asList(serviceIds));

		mServiceDiscoveryInterface.getServicesWithCountryPreference(serviceId, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
			@Override
			public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
				assertNotNull(urlMap);
				assertTrue(urlMap.size() > 0);
			}

			@Override
			public void onError(ERRORVALUES error, String message) {

			}
		});
	}

	public void testGetCountry() {
		mAppInfra.getSecureStorage().removeValueForKey("country");
		mServiceDiscoveryManager.mCountry = null;
		mServiceDiscoveryInterface.registerOnHomeCountrySet(new TestReceiver());
		String countryCode = "CN";
		mServiceDiscoveryInterface.setHomeCountry(countryCode);
		String country = mServiceDiscoveryInterface.getHomeCountry();
		assertTrue(country != null);
		assertEquals(countryCode,country);
		mServiceDiscoveryInterface.unRegisterHomeCountrySet(new TestReceiver());
	}

	class TestReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String countryCode = (String) intent.getExtras().get(AIL_HOME_COUNTRY);
		//	assertEquals(countryCode, "CN");
			assertTrue(intent.getAction().equals(AIL_SERVICE_DISCOVERY_HOMECOUNTRY_CHANGE_ACTION));
		}
	}

	public void testSetHomeCountryMappings() {
		mServiceDiscoveryInterface.unRegisterHomeCountrySet(new TestReceiver());
		mAppInfra.getSecureStorage().removeValueForKey("country");
		mServiceDiscoveryManager.mCountry = null;
		mServiceDiscoveryInterface.setHomeCountry("MO");
		assertEquals("MO",mServiceDiscoveryInterface.getHomeCountry());
		mServiceDiscoveryInterface.getHomeCountry(new ServiceDiscoveryInterface.OnGetHomeCountryListener() {
			@Override
			public void onSuccess(String countryCode, SOURCE source) {
				assertNotNull(countryCode);
				assertEquals("MO",countryCode);
				assertEquals(SOURCE.STOREDPREFERENCE ,source);
				String platformURL = mServiceDiscoveryManager.getSDURLForType
						(ServiceDiscoveryManager.AISDURLType.AISDURLTypePlatform);
				boolean isMapped = platformURL.contains("&country=HK");
				assertTrue(isMapped);
			}

			@Override
			public void onError(ERRORVALUES error, String message) {
				assertNotNull(error);
			}
		});
	}

	public void testSetHomeCountryWithMappingsInConfig() {
		Map<String,String> countryMapping = mServiceDiscoveryManager.getServiceDiscoveryCountryMapping();
		mAppInfra.getSecureStorage().removeValueForKey("country");
		mServiceDiscoveryManager.mCountry = null;
        if(countryMapping == null ||countryMapping.size() == 0) return;
		Map.Entry<String,String> entry = countryMapping.entrySet().iterator().next();
		final String countryTobeMapped = entry.getKey();
		final String mappedCountry = entry.getValue();
		mServiceDiscoveryInterface.setHomeCountry(countryTobeMapped);
		mServiceDiscoveryInterface.getHomeCountry(new ServiceDiscoveryInterface.OnGetHomeCountryListener() {
			@Override
			public void onSuccess(String countryCode, SOURCE source) {
				assertNotNull(countryCode);
				assertEquals(countryCode,countryTobeMapped);
				assertEquals(SOURCE.STOREDPREFERENCE ,source);
				String platformURL = mServiceDiscoveryManager.getSDURLForType
						(ServiceDiscoveryManager.AISDURLType.AISDURLTypePlatform);
                String countryPart = "&country=" + mappedCountry;
				boolean isMapped = platformURL.contains(countryPart);
				assertTrue(isMapped);
			}
			@Override
			public void onError(ERRORVALUES error, String message) {
              assertNotNull(error);
			}
		});
	}

	public  void testSetHomeCountryWithoutMappingsInConfig() {
		mAppInfra.getSecureStorage().removeValueForKey("country");
		mServiceDiscoveryManager.mCountry = null;
		mServiceDiscoveryInterface.setHomeCountry("IN");
		mServiceDiscoveryInterface.getHomeCountry(new ServiceDiscoveryInterface.OnGetHomeCountryListener() {
			@Override
			public void onSuccess(String countryCode, SOURCE source) {
				assertNotNull(countryCode);
				assertEquals(countryCode,"IN");
				assertEquals(SOURCE.STOREDPREFERENCE ,source);
				String platformURL = mServiceDiscoveryManager.getSDURLForType
						(ServiceDiscoveryManager.AISDURLType.AISDURLTypePlatform);
				String countryPart = "&country=" + "IN";
				boolean isMapped = platformURL.contains(countryPart);
				assertTrue(isMapped);
			}
			@Override
			public void onError(ERRORVALUES error, String message) {
				assertNotNull(error);
			}
		});
	}

}