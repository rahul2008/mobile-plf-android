package com.philips.cl.di.digitalcare.productregistration.test;

import static org.mockito.Mockito.mock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import com.philips.cl.di.digitalcare.productregistration.ProductRegistrationFragment;
import com.philips.cl.di.kitchenappliances.airfryer.LaunchDigitalCare;

public class ProductRegistrationFragmentTest extends
ActivityInstrumentationTestCase2<LaunchDigitalCare> {

public ProductRegistrationFragmentTest() {
super(LaunchDigitalCare.class);
}

private ProductRegistrationFragment mProductRegisteration = null;


@Override
protected void setUp() throws Exception {
super.setUp();
System.setProperty("dexmaker.dexcache", getInstrumentation()
		.getTargetContext().getCacheDir().getPath());
mProductRegisteration = mock(ProductRegistrationFragment.class);
}

@SmallTest
public void testIsProductRegisterationScreenIsMocked() {
boolean validate = false;
String received = mProductRegisteration.getClass().getSimpleName();
if (received.equalsIgnoreCase("ProductRegistrationFragment_Proxy"))
	validate = true;
assertTrue(validate);
}

}
