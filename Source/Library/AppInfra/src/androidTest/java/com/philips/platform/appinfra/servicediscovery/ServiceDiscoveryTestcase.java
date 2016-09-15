////gpackage com.philips.platform.appinfra.servicediscovery;
//
//import android.content.Context;
//
//import com.philips.platform.appinfra.AppInfra;
//import com.philips.platform.appinfra.MockitoTestCase;
//import com.philips.platform.appinfra.appconfiguration.AppConfigurationManager;
//import com.philips.platform.appinfra.servicediscovery.model.Config;
//import com.philips.platform.appinfra.servicediscovery.model.MatchByCountryOrLanguage;
//import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscovery;
//import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveyService;
//
//import org.json.JSONObject;
//
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Created by 310238655 on 6/28/2016.
// */
//public class ServiceDiscoveryTestcase extends MockitoTestCase {
//
//    ServiceDiscoveryInterface mServiceDiscoveryInterface = null;
//    ServiceDiscoveryManager mServiceDiscoveryManager = null;
//    AppInfra mAppInfra;
//    ServiceDiscovery mserviceDiscovery = null;
//    MatchByCountryOrLanguage mMatchByCountryOrLanguage = null;
//    String mServiceId = "userreg.janrain.cdn";
//    // Context context = Mockito.mock(Context.class);
//    ArrayList<String> mServicesId = new ArrayList<String>(
//            Arrays.asList("userreg.janrain.api", "userreg.janrain.cdn"));
//
//    AppConfigurationManager mConfigInterface;
//    private Context context;
//
//    RequestItemManager mRequestItemManager = null;
//
//    @Override
//    protected void setUp() throws Exception {
//        super.setUp();
//        context = getInstrumentation().getContext();
//        assertNotNull(context);
//        mAppInfra = new AppInfra.Builder().build(context);
//        assertNotNull(mAppInfra);
//
//        mConfigInterface = new AppConfigurationManager(mAppInfra) {
//            @Override
//            protected JSONObject getMasterConfigFromApp() {
//                JSONObject result = null;
//                try {
//                    String testJson = "{\n" +
//                            "  \"UR\": {\n" +
//                            "\n" +
//                            "    \"Development\": \"ad7nn99y2mv5berw5jxewzagazafbyhu\",\n" +
//                            "    \"Testing\": \"xru56jcnu3rpf8q7cgnkr7xtf9sh8pp7\",\n" +
//                            "    \"Evaluation\": \"4r36zdbeycca933nufcknn2hnpsz6gxu\",\n" +
//                            "    \"Staging\": \"f2stykcygm7enbwfw2u9fbg6h6syb8yd\",\n" +
//                            "    \"Production\": \"mz6tg5rqrg4hjj3wfxfd92kjapsrdhy3\"\n" +
//                            "\n" +
//                            "  },\n" +
//                            "  \"AI\": {\n" +
//                            "    \"MicrositeID\": 77001,\n" +
//                            "    \"RegistrationEnvironment\": \"Staging\",\n" +
//                            "    \"NL\": [\"googleplus\", \"facebook\"  ],\n" +
//                            "    \"US\": [\"facebook\",\"googleplus\" ],\n" +
//                            "    \"EE\": [123,234 ]\n" +
//                            "  }, \n" +
//                            " \"appinfra\": { \n" +
//                            "   \"appidentity.micrositeId\" : \"77000\",\n" +
//                            "  \"appidentity.sector\"  : \"B2C\",\n" +
//                            " \"appidentity.appState\"  : \"Staging\",\n" +
//                            "\"appidentity.serviceDiscoveryEnvironment\"  : \"Staging\",\n" +
//                            "\"restclient.cacheSizeInKB\"  : 1024 \n" +
//                            "} \n" + "}";
//                    result = new JSONObject(testJson);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return result;
//            }
//
//        };
//        mAppInfra = new AppInfra.Builder().setConfig(mConfigInterface).build(context);
//
//
//        mServiceDiscoveryInterface = mAppInfra.getServiceDiscovery();
//        mServiceDiscoveryManager = new ServiceDiscoveryManager(mAppInfra);
//        mRequestItemManager = new RequestItemManager(context, mAppInfra);
//        assertNotNull(mRequestItemManager);
//        mserviceDiscovery = new ServiceDiscovery();
//        mserviceDiscovery = loadServiceDiscoveryModel();
//        mMatchByCountryOrLanguage = new MatchByCountryOrLanguage();
//        assertNotNull(mserviceDiscovery);
//        assertNotNull(mMatchByCountryOrLanguage);
//        assertNotNull(mServiceDiscoveryInterface);
//        assertNotNull(mServiceDiscoveryManager);
//
//
//    }
//
//    public void testRefresh() {
//        mServiceDiscoveryManager.serviceDiscovery = loadServiceDiscoveryModel();
//        mServiceDiscoveryInterface.refresh(new ServiceDiscoveryInterface.OnRefreshListener() {
//            @Override
//            public void onSuccess() {
//                assertNotNull("Test");
