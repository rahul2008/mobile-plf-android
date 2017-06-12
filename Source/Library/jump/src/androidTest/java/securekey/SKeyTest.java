//package securekey;
//
//import android.content.Context;
//import android.support.multidex.MultiDex;
//import static android.support.test.InstrumentationRegistry.getInstrumentation;
//import static junit.framework.Assert.assertNotNull;
//
//import com.philips.securekey.SKey;
//
//import org.junit.Test;
//
//
//public class SKeyTest {
//    SKey mSecureStorage ;
//
//    Context context;
//
//
//
//    @Test
//    public void testGetErrorCode() throws Exception {
//        MultiDex.install(getInstrumentation().getTargetContext());
//
//        System.setProperty("dexmaker.dexcache", getInstrumentation()
//                .getTargetContext().getCacheDir().getPath());
//        mSecureStorage = new SKey();
//        assertNotNull(mSecureStorage.generateSecretKey());
//    }
//}