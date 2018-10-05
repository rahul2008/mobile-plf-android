package com.philips.cdp.registration.configuration;

import com.philips.cdp.registration.ui.utils.*;
import com.philips.platform.appinfra.logging.*;

import junit.framework.TestCase;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class ClientIDConfigurationTest extends TestCase{

    @Mock
    private LoggingInterface mockLoggingInterface;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        RLog.setMockLogger(mockLoggingInterface);
    }

    @Test
    public void testChinaDevelopResetPassword() {
        assertEquals("xhrue999syb8g2csggp9acs6x87q8q3d", new ClientIDConfiguration().getResetPasswordClientId("https://philips-cn-dev.capture.cn.janrain.com"));
    }

    @Test
    public void testChinaEUDevelopResetPassword() {
        assertEquals("4c5tqzbneykdw2md7mkp75uycp23x3qz", new ClientIDConfiguration().getResetPasswordClientId("https://philips-china-eu.eu-dev.janraincapture.com"));
    }

    @Test
    public void testChinaTestResetPassword() {
        assertEquals("v2s8qajf9ncfzsyy6ghjpqvsrju9xgvt", new ClientIDConfiguration().getResetPasswordClientId("https://philips-cn-test.capture.cn.janrain.com"));
    }

    @Test
    public void testChinaEUTestResetPassword() {
        assertEquals("fh5mfvjqzwhn5t9gdwqqjnbcw9atd7mv", new ClientIDConfiguration().getResetPasswordClientId("https://philips-china-test.eu-dev.janraincapture.com"));
    }

    @Test
    public void testChinaEvalResetPassword() {
        assertEquals("mfvjprjmgbrhfbtn6cq6q2yupzhxn977", new ClientIDConfiguration().getResetPasswordClientId("https://philips-cn-staging.capture.cn.janrain.com"));
    }



    @Test
    public void testChinaProductResetPassword() {
        assertEquals("65dzkyh48ux4vcguhvwsgvtk4bzyh2va", new ClientIDConfiguration().getResetPasswordClientId("https://philips-cn.capture.cn.janrain.com"));
    }




    @Test
    public void testChinaDevelopCaptureId() {
        assertEquals("7629q5uxm2jyrbk7ehuwryj7a4", new ClientIDConfiguration().getCaptureId("https://philips-cn-dev.capture.cn.janrain.com"));
    }

    @Test
    public void testChinaEUDevelopCaptureId() {
        assertEquals("euwkgsf83m56hqknjxgnranezh", new ClientIDConfiguration().getCaptureId("https://philips-china-eu.eu-dev.janraincapture.com"));
    }

    @Test
    public void testChinaTestCaptureId() {
        assertEquals("hqmhwxu7jtdcye758vvxux4ryb", new ClientIDConfiguration().getCaptureId("https://philips-cn-test.capture.cn.janrain.com"));
    }

    @Test
    public void testChinaEUTestCaptureId() {
        assertEquals("vdgkb3z57jpv93mxub34x73mqu", new ClientIDConfiguration().getCaptureId("https://philips-china-test.eu-dev.janraincapture.com"));
    }

    @Test
    public void testChinaEvalCaptureId() {
        assertEquals("czwfzs7xh23ukmpf4fzhnksjmd", new ClientIDConfiguration().getCaptureId("https://philips-cn-staging.capture.cn.janrain.com"));
    }



    @Test
    public void testChinaProductCaptureId() {
        assertEquals("zkr6yg4mdsnt7f8mvucx7qkja3", new ClientIDConfiguration().getCaptureId("https://philips-cn.capture.cn.janrain.com"));
    }

    @Test
    public void testChinaDevelopEngageId() {
        assertEquals("ruaheighoryuoxxdwyfs", new ClientIDConfiguration().getEngageId("https://philips-cn-dev.capture.cn.janrain.com"));
    }

    @Test
    public void testChinaEUDevelopEngageId() {
        assertEquals("bdbppnbjfcibijknnfkk", new ClientIDConfiguration().getEngageId("https://philips-china-eu.eu-dev.janraincapture.com"));
    }

    @Test
    public void testChinaTestEngageId() {
        assertEquals("jndphelwbhuevcmovqtn", new ClientIDConfiguration().getEngageId("https://philips-cn-test.capture.cn.janrain.com"));
    }

    @Test
    public void testChinaEUTestEngageId() {
        assertEquals("fhbmobeahciagddgfidm", new ClientIDConfiguration().getEngageId("https://philips-china-test.eu-dev.janraincapture.com"));
    }

    @Test
    public void testChinaEvalEngageId() {
        assertEquals("uyfpympodtnesxejzuic", new ClientIDConfiguration().getEngageId("https://philips-cn-staging.capture.cn.janrain.com"));
    }



    @Test
    public void testChinaProductEngageId() {
        assertEquals("cfwaqwuwcwzlcozyyjpa", new ClientIDConfiguration().getEngageId("https://philips-cn.capture.cn.janrain.com"));
    }







    @Test
    public void testRussiaDevelopCaptureId() {
        assertEquals("eupac7ugz25x8dwahvrbpmndf8", new ClientIDConfiguration().getCaptureId("https://dev.philips.ru/localstorage"));
    }
    @Test
    public void testRussiaTestCaptureId() {
        assertEquals("x7nftvwfz8e8vcutz49p8eknqp", new ClientIDConfiguration().getCaptureId("https://tst.philips.ru/localstorage"));
    }

    @Test
    public void testRussiaEvalCaptureId() {
        assertEquals("nt5dqhp6uck5mcu57snuy8uk6c", new ClientIDConfiguration().getCaptureId("https://stg.philips.ru/localstorage"));
    }

    @Test
    public void testRussiaProductCaptureId() {
        assertEquals("hffxcm638rna8wrxxggx2gykhc", new ClientIDConfiguration().getCaptureId("https://www.philips.ru/localstorage"));
    }



    @Test
    public void testRussiaDevelopEngageId() {
        assertEquals("bdbppnbjfcibijknnfkk", new ClientIDConfiguration().getEngageId("https://dev.philips.ru/localstorage"));
    }
    @Test
    public void testRussiaTestEngageId() {
        assertEquals("fhbmobeahciagddgfidm", new ClientIDConfiguration().getEngageId("https://tst.philips.ru/localstorage"));
    }

    @Test
    public void testRussiaEvalEngageId() {
        assertEquals("jgehpoggnhbagolnihge", new ClientIDConfiguration().getEngageId("https://stg.philips.ru/localstorage"));
    }

    @Test
    public void testRussiaProductEngageId() {
        assertEquals("ddjbpmgpeifijdlibdio", new ClientIDConfiguration().getEngageId("https://www.philips.ru/localstorage"));
    }


    @Test
    public void testRussialDevelopResetPassword() {
        assertEquals("rj95w5ghxqthxxy8jpug5a63wrbeykzk", new ClientIDConfiguration().getResetPasswordClientId("https://dev.philips.ru/localstorage"));
    }
    @Test
    public void testRussiaTestResetPassword() {
        assertEquals("suxgtg52ej3srf683t7u5gqzw4824avg", new ClientIDConfiguration().getResetPasswordClientId("https://tst.philips.ru/localstorage"));
    }

    @Test
    public void testRussiaEvalResetPassword() {
        assertEquals("h27n93rjva8xuvzgpeb7jf9jxq6dnnzr", new ClientIDConfiguration().getResetPasswordClientId("https://stg.philips.ru/localstorage"));
    }

    @Test
    public void testRussiaProductResetPassword() {
        assertEquals("h27n93rjva8xuvzgpeb7jf9jxq6dnnzr", new ClientIDConfiguration().getResetPasswordClientId("https://www.philips.ru/localstorage"));
    }




    @Test
    public void testGlobalDevelopCaptureId() {
        assertEquals("eupac7ugz25x8dwahvrbpmndf8", new ClientIDConfiguration().getCaptureId("https://philips.dev.janraincapture.com"));
    }
    @Test
    public void testGlobalTestCaptureId() {
        assertEquals("x7nftvwfz8e8vcutz49p8eknqp", new ClientIDConfiguration().getCaptureId("https://philips-test.dev.janraincapture.com"));
    }

    @Test
    public void testGlobalEvalCaptureId() {
        assertEquals("nt5dqhp6uck5mcu57snuy8uk6c", new ClientIDConfiguration().getCaptureId("https://philips.eval.janraincapture.com"));
    }

    @Test
    public void testGlobalProductCaptureId() {
        assertEquals("hffxcm638rna8wrxxggx2gykhc", new ClientIDConfiguration().getCaptureId("https://philips.janraincapture.com"));
    }



    @Test
    public void testGlobalDevelopEngageId() {
        assertEquals("bdbppnbjfcibijknnfkk", new ClientIDConfiguration().getEngageId("https://philips.dev.janraincapture.com"));
    }
    @Test
    public void testGlobalTestEngageId() {
        assertEquals("fhbmobeahciagddgfidm", new ClientIDConfiguration().getEngageId("https://philips-test.dev.janraincapture.com"));
    }

    @Test
    public void testGlobalEvalEngageId() {
        assertEquals("jgehpoggnhbagolnihge", new ClientIDConfiguration().getEngageId("https://philips.eval.janraincapture.com"));
    }

    @Test
    public void testGlobalProductEngageId() {
        assertEquals("ddjbpmgpeifijdlibdio", new ClientIDConfiguration().getEngageId("https://philips.janraincapture.com"));
    }


    @Test
    public void testGlobalDevelopResetPassword() {
        assertEquals("rj95w5ghxqthxxy8jpug5a63wrbeykzk", new ClientIDConfiguration().getResetPasswordClientId("https://philips.dev.janraincapture.com"));
    }
    @Test
    public void testGlobalTestResetPassword() {
        assertEquals("suxgtg52ej3srf683t7u5gqzw4824avg", new ClientIDConfiguration().getResetPasswordClientId("https://philips-test.dev.janraincapture.com"));
    }

    @Test
    public void testGlobalEvalResetPassword() {
        assertEquals("h27n93rjva8xuvzgpeb7jf9jxq6dnnzr", new ClientIDConfiguration().getResetPasswordClientId("https://philips.eval.janraincapture.com"));
    }

    @Test
    public void testGlobalProductResetPassword() {
        assertEquals("h27n93rjva8xuvzgpeb7jf9jxq6dnnzr", new ClientIDConfiguration().getResetPasswordClientId("https://philips.janraincapture.com"));
    }

}