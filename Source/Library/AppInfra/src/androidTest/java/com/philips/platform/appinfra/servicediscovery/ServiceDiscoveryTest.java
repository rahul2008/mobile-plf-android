//package com.philips.platform.appinfra.servicediscovery;
//
//import android.content.Context;
//import android.util.Log;
//
//import com.philips.platform.appinfra.AppInfra;
//import com.philips.platform.appinfra.MockitoTestCase;
//
//import java.net.URL;
//
///**
// * Created by 310238114 on 6/14/2016.
// */
//public class ServiceDiscoveryTest extends MockitoTestCase implements ServiceDiscoveryInterface.OnGetServicesListener, ServiceDiscoveryInterface.OnGetServiceLocaleListener, ServiceDiscoveryInterface.OnGetServiceUrlListener{
//    private Context context;
//    AppInfra appInfra;
//    ServiceDiscoveryInterface mServiceDiscoveryInterface = null;
//    ServiceDiscoveryInterface.OnGetServicesListener onGetServicesListener= this;
//
//    @Override
//    protected void setUp() throws Exception {
//        super.setUp();
//        context = getInstrumentation().getContext();
//        assertNotNull(context);
//        appInfra= new AppInfra(context
//        );
//
//
//    }
//
//    public void  testMethod(){
//        mServiceDiscoveryInterface=new ServiceDiscoveryManager(appInfra);
//        //mServiceDiscoveryInterface.getServicesWithCountryPreference("ugrow.privacy",onGetServicesListener );
//
//        mServiceDiscoveryInterface.refresh(new ServiceDiscoveryInterface.OnRefreshListener() {
//            @Override
//            public void onError(ERRORVALUES error, String message) {
//                Log.i("onError", ""+"refresh  Error");
//            }
//
//            @Override
//            public void onSuccess() {
//                Log.i("onSuccess", ""+"refresh  success");
//               /* mServiceDiscoveryInterface.getServicesWithCountryPreference("ugrow.privacy",mOnGetServicesListener );
//                mServiceDiscoveryInterface.getServicesWithLanguagePreference("ugrow.terms",mOnGetServicesListener );
//                mServiceDiscoveryInterface.getServiceLocaleWithCountryPreference("ugrow.privacy",mOnGetServiceLocaleListener );
//                mServiceDiscoveryInterface.getServiceLocaleWithLanguagePreference("ugrow.terms",mOnGetServiceLocaleListener );
//                mServiceDiscoveryInterface.getServiceUrlWithCountryPreference("ugrow.privacy",mOnGetServiceUrlListener );
//                mServiceDiscoveryInterface.getServiceUrlWithLanguagePreference("ugrow.terms",mOnGetServiceUrlListener );*/
//            }
//        });
//
//    }
//
//    @Override
//    public void onSuccess(URL url) {
//        Log.i("Success", ""+url);
//    }
//
//    @Override
//    public void onSuccess(String services) {
//        Log.i("OnGetServicesListener", ""+services);
//    }
//
//    @Override
//    public void onError(ERRORVALUES error, String message) {
//
//    }
//}
