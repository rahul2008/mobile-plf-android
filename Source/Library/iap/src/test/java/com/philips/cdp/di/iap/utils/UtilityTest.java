package com.philips.cdp.di.iap.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;

import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.response.addresses.Country;
import com.philips.cdp.di.iap.response.addresses.Region;
import com.philips.platform.uid.view.widget.AlertDialogFragment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.philips.cdp.di.iap.utils.NetworkUtility.ALERT_DIALOG_TAG;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by philips on 9/26/17.
 */
public class UtilityTest {

    @Mock
    Log mockLog;

    com.philips.cdp.di.iap.utils.Utility utility;

    @Mock
    Activity activity;

    @Mock
    Context contextMock;

    @Mock
    View viewMock;

    @Mock
    IBinder iBinderMock;

    @Mock
    TypedArray typedArrayMock;

    @Mock
    Resources.Theme themeMock;

    @Mock
    SharedPreferences.Editor editorMock;

    @Mock
    SharedPreferences sharedPreferencesMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = NullPointerException.class)
    public void shouldHideKeyboard() throws Exception {

        when(viewMock.getWindowToken()).thenReturn(iBinderMock);
        when(activity.getCurrentFocus()).thenReturn(viewMock);
        utility.hideKeypad(activity);
    }

    @Test
    public void shouldFormatAddress() throws Exception {
        utility.formatAddress(anyString());
    }

    @Test
    public void shouldReturnNull_WhenNullIsPassedToFormatAddress() throws Exception {
        Assert.assertEquals(null, utility.formatAddress(null));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowException_WhenGetFormattedDateIsPassedWithWrongFormatString() throws Exception {
        utility.getFormattedDate(anyString());
    }

//    @Test(expected = NullPointerException.class)
//    public void shouldReturnThemeCalor_fromContext() throws Exception {
//        when(themeMock.obtainStyledAttributes(new int[]{R.attr.uikit_baseColor})).thenReturn(typedArrayMock);
//        when(contextMock.getTheme()).thenReturn(themeMock);
//        when(typedArrayMock.getColor(0, ContextCompat.getColor(activity, R.color.uikit_philips_blue))).thenReturn(anyInt());
//        utility.getThemeColor(contextMock);
//    }

    @Test
    public void shouldAddCountryPrefrence() throws Exception {
        when(sharedPreferencesMock.edit()).thenReturn(editorMock);
        utility.addCountryInPreference(sharedPreferencesMock, "hi", "Hi");
    }

    @Test
    public void shouldAppendAddressWithNewLineIfNotNull() throws Exception {

        StringBuilder stringBuilder = new StringBuilder("abcd");
        utility.appendAddressWithNewLineIfNotNull(stringBuilder, "code");

    }

    @Mock
    AddressFields addressFieldsMock;

    @Test
    public void shouldGetAddressToDisplay() throws Exception {
        utility.getAddressToDisplay(addressFieldsMock);
    }

    @Test
    public void shouldReturnIsNotNullNorEmpty() throws Exception {
        Assert.assertEquals(true, utility.isNotNullNorEmpty("abcd"));
    }

    @Mock
    Addresses addressesMock;

    @Mock
    Country countryMock;

    @Mock
    Region regionMock;

    @Test
    public void shouldPrepareAddressFields() throws Exception {
        when(countryMock.getIsocode()).thenReturn("091");
        when(regionMock.getIsocodeShort()).thenReturn("01");
        when(addressesMock.getRegion()).thenReturn(regionMock);
        when(addressesMock.getCountry()).thenReturn(countryMock);
        when(addressesMock.getTitleCode()).thenReturn("abcd");
        utility.prepareAddressFields(addressesMock, "abc@gmail.com");

    }

    @Mock
    FragmentManager fragmentManagerMock;

    @Mock
    AlertListener alertListenerMock;

    @Test(expected = NullPointerException.class)
    public void shouldShowActionDialog() throws Exception {
        utility.showActionDialog(contextMock,"HI","HI","HI","HI",fragmentManagerMock,alertListenerMock);

    }

    @Mock
    AlertDialogFragment alertDialogFragmentMock;

    @Test
    public void shouldDismissAlertDialog() throws Exception {
        utility.dismissAlertFragmentDialog(alertDialogFragmentMock,fragmentManagerMock);
    }

    @Test
    public void shouldDismissAlertDialogWhenPassedNull() throws Exception {
        when(fragmentManagerMock.findFragmentByTag(ALERT_DIALOG_TAG)).thenReturn(alertDialogFragmentMock);
        utility.dismissAlertFragmentDialog(null,fragmentManagerMock);
    }

    @Test
    public void shouldReturnIndexOfSubString() throws Exception {
        CharSequence hello="hello";
        CharSequence world="world";
        utility.indexOfSubString(true,hello,world);

    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnImageArray() throws Exception {
        utility.getImageArrow(contextMock);
    }
}