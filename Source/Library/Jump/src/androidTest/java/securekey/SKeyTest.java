package securekey;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.philips.cdp.security.SecureStorage;
import com.philips.securekey.SKey;

import org.junit.Test;

/**
 * Created by 310243576 on 8/20/2016.
 */
public class SKeyTest extends InstrumentationTestCase{
    SKey mSecureStorage ;

    Context context;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
    mSecureStorage = new SKey();
    }

    @Test
    public void testGetErrorCode() throws Exception {
        assertNotNull(mSecureStorage.generateSecretKey());
    }
}