package com.philips.cdp.registration.configuration;

import com.philips.cdp.registration.ui.utils.*;
import com.philips.platform.appinfra.logging.*;

import junit.framework.TestCase;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class ClientIDConfigurationTest extends TestCase{

    @Mock
    LoggingInterface mockLoggingInterface;

    @Before
    @Override
    public void setUp() throws Exception {
        RLog.setMockLogger(mockLoggingInterface);
    }

    @Test
    public void testChinaDevelopResetPassword() throws Exception {
        assertEquals("xhrue999syb8g2csggp9acs6x87q8q3d", new ClientIDConfiguration().getResetPasswordClientId("https://philips-cn-dev.capture.cn.janrain.com"));
    }

    @Test
    public void testChinaEUDevelopResetPassword() throws Exception {
        assertEquals("4c5tqzbneykdw2md7mkp75uycp23x3qz", new ClientIDConfiguration().getResetPasswordClientId("https://philips-china-eu.eu-dev.janraincapture.com"));
    }

    @Test
    public void testChinaTestResetPassword() throws Exception {
        assertEquals("v2s8qajf9ncfzsyy6ghjpqvsrju9xgvt", new ClientIDConfiguration().getResetPasswordClientId("https://philips-cn-test.capture.cn.janrain.com"));
    }

    @Test
    public void testChinaEUTestResetPassword() throws Exception {
        assertEquals("fh5mfvjqzwhn5t9gdwqqjnbcw9atd7mv", new ClientIDConfiguration().getResetPasswordClientId("https://philips-china-test.eu-dev.janraincapture.com"));
    }

    @Test
    public void testChinaEvalResetPassword() throws Exception {
        assertEquals("mfvjprjmgbrhfbtn6cq6q2yupzhxn977", new ClientIDConfiguration().getResetPasswordClientId("https://philips-cn-staging.capture.cn.janrain.com"));
    }



    @Test
    public void testChinaProductResetPassword() throws Exception {
        assertEquals("65dzkyh48ux4vcguhvwsgvtk4bzyh2va", new ClientIDConfiguration().getResetPasswordClientId("https://philips-cn.capture.cn.janrain.com"));
    }




    @Test
    public void testChinaDevelopCaptureId() throws Exception {
        assertEquals("7629q5uxm2jyrbk7ehuwryj7a4", new ClientIDConfiguration().getCaptureId("https://philips-cn-dev.capture.cn.janrain.com"));
    }

    @Test
    public void testChinaEUDevelopCaptureId() throws Exception {
        assertEquals("euwkgsf83m56hqknjxgnranezh", new ClientIDConfiguration().getCaptureId("https://philips-china-eu.eu-dev.janraincapture.com"));
    }

    @Test
    public void testChinaTestCaptureId() throws Exception {
        assertEquals("hqmhwxu7jtdcye758vvxux4ryb", new ClientIDConfiguration().getCaptureId("https://philips-cn-test.capture.cn.janrain.com"));
    }

    @Test
    public void testChinaEUTestCaptureId() throws Exception {
        assertEquals("vdgkb3z57jpv93mxub34x73mqu", new ClientIDConfiguration().getCaptureId("https://philips-china-test.eu-dev.janraincapture.com"));
    }

    @Test
    public void testChinaEvalCaptureId() throws Exception {
        assertEquals("czwfzs7xh23ukmpf4fzhnksjmd", new ClientIDConfiguration().getCaptureId("https://philips-cn-staging.capture.cn.janrain.com"));
    }



    @Test
    public void testChinaProductCaptureId() throws Exception {
        assertEquals("zkr6yg4mdsnt7f8mvucx7qkja3", new ClientIDConfiguration().getCaptureId("https://philips-cn.capture.cn.janrain.com"));
    }





    @Test
    public void testChinaDevelopEngageId() throws Exception {
        assertEquals("ruaheighoryuoxxdwyfs", new ClientIDConfiguration().getEngageId("https://philips-cn-dev.capture.cn.janrain.com"));
    }

    @Test
    public void testChinaEUDevelopEngageId() throws Exception {
        assertEquals("bdbppnbjfcibijknnfkk", new ClientIDConfiguration().getEngageId("https://philips-china-eu.eu-dev.janraincapture.com"));
    }

    @Test
    public void testChinaTestEngageId() throws Exception {
        assertEquals("jndphelwbhuevcmovqtn", new ClientIDConfiguration().getEngageId("https://philips-cn-test.capture.cn.janrain.com"));
    }

    @Test
    public void testChinaEUTestEngageId() throws Exception {
        assertEquals("fhbmobeahciagddgfidm", new ClientIDConfiguration().getEngageId("https://philips-china-test.eu-dev.janraincapture.com"));
    }

    @Test
    public void testChinaEvalEngageId() throws Exception {
        assertEquals("uyfpympodtnesxejzuic", new ClientIDConfiguration().getEngageId("https://philips-cn-staging.capture.cn.janrain.com"));
    }



    @Test
    public void testChinaProductEngageId() throws Exception {
        assertEquals("cfwaqwuwcwzlcozyyjpa", new ClientIDConfiguration().getEngageId("https://philips-cn.capture.cn.janrain.com"));
    }







    @Test
    public void testRussiaDevelopCaptureId() throws Exception {
        assertEquals("eupac7ugz25x8dwahvrbpmndf8", new ClientIDConfiguration().getCaptureId("https://dev.philips.ru/localstorage"));
    }
    @Test
    public void testRussiaTestCaptureId() throws Exception {
        assertEquals("x7nftvwfz8e8vcutz49p8eknqp", new ClientIDConfiguration().getCaptureId("https://tst.philips.ru/localstorage"));
    }

    @Test
    public void testRussiaEvalCaptureId() throws Exception {
        assertEquals("nt5dqhp6uck5mcu57snuy8uk6c", new ClientIDConfiguration().getCaptureId("https://stg.philips.ru/localstorage"));
    }

    @Test
    public void testRussiaProductCaptureId() throws Exception {
        assertEquals("hffxcm638rna8wrxxggx2gykhc", new ClientIDConfiguration().getCaptureId("https://www.philips.ru/localstorage"));
    }



    @Test
    public void testRussiaDevelopEngageId() throws Exception {
        assertEquals("bdbppnbjfcibijknnfkk", new ClientIDConfiguration().getEngageId("https://dev.philips.ru/localstorage"));
    }
    @Test
    public void testRussiaTestEngageId() throws Exception {
        assertEquals("fhbmobeahciagddgfidm", new ClientIDConfiguration().getEngageId("https://tst.philips.ru/localstorage"));
    }

    @Test
    public void testRussiaEvalEngageId() throws Exception {
        assertEquals("jgehpoggnhbagolnihge", new ClientIDConfiguration().getEngageId("https://stg.philips.ru/localstorage"));
    }

    @Test
    public void testRussiaProductEngageId() throws Exception {
        assertEquals("ddjbpmgpeifijdlibdio", new ClientIDConfiguration().getEngageId("https://www.philips.ru/localstorage"));
    }


    @Test
    public void testRussialDevelopResetPassword() throws Exception {
        assertEquals("rj95w5ghxqthxxy8jpug5a63wrbeykzk", new ClientIDConfiguration().getResetPasswordClientId("https://dev.philips.ru/localstorage"));
    }
    @Test
    public void testRussiaTestResetPassword() throws Exception {
        assertEquals("suxgtg52ej3srf683t7u5gqzw4824avg", new ClientIDConfiguration().getResetPasswordClientId("https://tst.philips.ru/localstorage"));
    }

    @Test
    public void testRussiaEvalResetPassword() throws Exception {
        assertEquals("h27n93rjva8xuvzgpeb7jf9jxq6dnnzr", new ClientIDConfiguration().getResetPasswordClientId("https://stg.philips.ru/localstorage"));
    }

    @Test
    public void testRussiaProductResetPassword() throws Exception {
        assertEquals("h27n93rjva8xuvzgpeb7jf9jxq6dnnzr", new ClientIDConfiguration().getResetPasswordClientId("https://www.philips.ru/localstorage"));
    }




    @Test
    public void testGlobalDevelopCaptureId() throws Exception {
        assertEquals("eupac7ugz25x8dwahvrbpmndf8", new ClientIDConfiguration().getCaptureId("https://philips.dev.janraincapture.com"));
    }
    @Test
    public void testGlobalTestCaptureId() throws Exception {
        assertEquals("x7nftvwfz8e8vcutz49p8eknqp", new ClientIDConfiguration().getCaptureId("https://philips-test.dev.janraincapture.com"));
    }

    @Test
    public void testGlobalEvalCaptureId() throws Exception {
        assertEquals("nt5dqhp6uck5mcu57snuy8uk6c", new ClientIDConfiguration().getCaptureId("https://philips.eval.janraincapture.com"));
    }

    @Test
    public void testGlobalProductCaptureId() throws Exception {
        assertEquals("hffxcm638rna8wrxxggx2gykhc", new ClientIDConfiguration().getCaptureId("https://philips.janraincapture.com"));
    }



    @Test
    public void testGlobalDevelopEngageId() throws Exception {
        assertEquals("bdbppnbjfcibijknnfkk", new ClientIDConfiguration().getEngageId("https://philips.dev.janraincapture.com"));
    }
    @Test
    public void testGlobalTestEngageId() throws Exception {
        assertEquals("fhbmobeahciagddgfidm", new ClientIDConfiguration().getEngageId("https://philips-test.dev.janraincapture.com"));
    }

    @Test
    public void testGlobalEvalEngageId() throws Exception {
        assertEquals("jgehpoggnhbagolnihge", new ClientIDConfiguration().getEngageId("https://philips.eval.janraincapture.com"));
    }

    @Test
    public void testGlobalProductEngageId() throws Exception {
        assertEquals("ddjbpmgpeifijdlibdio", new ClientIDConfiguration().getEngageId("https://philips.janraincapture.com"));
    }


    @Test
    public void testGlobalDevelopResetPassword() throws Exception {
        assertEquals("rj95w5ghxqthxxy8jpug5a63wrbeykzk", new ClientIDConfiguration().getResetPasswordClientId("https://philips.dev.janraincapture.com"));
    }
    @Test
    public void testGlobalTestResetPassword() throws Exception {
        assertEquals("suxgtg52ej3srf683t7u5gqzw4824avg", new ClientIDConfiguration().getResetPasswordClientId("https://philips-test.dev.janraincapture.com"));
    }

    @Test
    public void testGlobalEvalResetPassword() throws Exception {
        assertEquals("h27n93rjva8xuvzgpeb7jf9jxq6dnnzr", new ClientIDConfiguration().getResetPasswordClientId("https://philips.eval.janraincapture.com"));
    }

    @Test
    public void testGlobalProductResetPassword() throws Exception {
        assertEquals("h27n93rjva8xuvzgpeb7jf9jxq6dnnzr", new ClientIDConfiguration().getResetPasswordClientId("https://philips.janraincapture.com"));
    }

}