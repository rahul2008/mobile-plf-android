package com.philips.cdp.prodreg.error;

import android.content.Context;

import com.philips.cdp.prodreg.constants.ProdRegError;
import com.philips.cdp.prodreg.constants.RegistrationState;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.register.UserWithProducts;
import com.philips.cdp.product_registration_lib.R;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
public class ErrorHandlerTest extends TestCase {

    ErrorHandler errorHandler;
    private UserWithProducts userWithProductsMock;
    private Context context;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        userWithProductsMock = mock(UserWithProducts.class);
        errorHandler = new ErrorHandler();
        context = mock(Context.class);
    }

    @Test
    public void testErrorHandle() {

        RegisteredProduct product = new RegisteredProduct("ctn", null, null);
        errorHandler.handleError(userWithProductsMock, product, ProdRegError.INVALID_CTN.getCode());
        verify(userWithProductsMock).updateLocaleCache(product, ProdRegError.INVALID_CTN, RegistrationState.FAILED);

        errorHandler.handleError(userWithProductsMock, product, ProdRegError.INVALID_SERIALNUMBER.getCode());
        verify(userWithProductsMock).updateLocaleCache(product, ProdRegError.INVALID_SERIALNUMBER, RegistrationState.FAILED);

        errorHandler.handleError(userWithProductsMock, product, ProdRegError.INVALID_VALIDATION.getCode());
        verify(userWithProductsMock).updateLocaleCache(product, ProdRegError.INVALID_VALIDATION, RegistrationState.FAILED);

        errorHandler.handleError(userWithProductsMock, product, ProdRegError.NO_INTERNET_AVAILABLE.getCode());
        verify(userWithProductsMock).updateLocaleCache(product, ProdRegError.NO_INTERNET_AVAILABLE, RegistrationState.FAILED);

        errorHandler.handleError(userWithProductsMock, product, ProdRegError.INTERNAL_SERVER_ERROR.getCode());
        verify(userWithProductsMock).updateLocaleCache(product, ProdRegError.INTERNAL_SERVER_ERROR, RegistrationState.FAILED);

        errorHandler.handleError(userWithProductsMock, product, ProdRegError.TIME_OUT.getCode());
        verify(userWithProductsMock).updateLocaleCache(product, ProdRegError.TIME_OUT, RegistrationState.FAILED);

        errorHandler.handleError(userWithProductsMock, product, ProdRegError.ACCESS_TOKEN_INVALID.getCode());
        verify(userWithProductsMock).onAccessTokenExpire(product);

        errorHandler.handleError(userWithProductsMock, product, 600);
        verify(userWithProductsMock).updateLocaleCache(product, ProdRegError.UNKNOWN, RegistrationState.FAILED);

        errorHandler.handleError(userWithProductsMock, product, 511);
        verify(userWithProductsMock).updateLocaleCache(product, ProdRegError.NETWORK_ERROR, RegistrationState.FAILED);

        errorHandler.handleError(userWithProductsMock, product, 1);
        verify(userWithProductsMock).updateLocaleCache(product, ProdRegError.PARSE_ERROR, RegistrationState.FAILED);

        errorHandler.handleError(userWithProductsMock, product, 507);
        verify(userWithProductsMock).updateLocaleCache(product, ProdRegError.INVALID_SERIAL_NUMBER_AND_PURCHASE_DATE, RegistrationState.FAILED);
    }

    @Test
    public void testGetError() {
        ProdRegErrorMap prodRegErrorMap = errorHandler.getError(context, ProdRegError.INVALID_DATE.getCode());
        assertEquals(prodRegErrorMap.getDescription(), context.getString(R.string.PPR_Enter_Purchase_Date_ErrMsg));
        assertEquals(prodRegErrorMap.getTitle(), context.getString(R.string.PPR_Req_Purchase_Date_Title));

        prodRegErrorMap = errorHandler.getError(context, ProdRegError.INVALID_CTN.getCode());
        assertEquals(prodRegErrorMap.getDescription(), context.getString(R.string.PPR_Product_Not_Found_ErrMsg));
        assertEquals(prodRegErrorMap.getTitle(), context.getString(R.string.PPR_Product_Not_Found_Title));

        prodRegErrorMap = errorHandler.getError(context, ProdRegError.PRODUCT_ALREADY_REGISTERED.getCode());
        assertEquals(prodRegErrorMap.getDescription(), context.getString(R.string.PPR_Already_Registered_ErrMsg));
        assertEquals(prodRegErrorMap.getTitle(), context.getString(R.string.PPR_Already_Registered_title));

        prodRegErrorMap = errorHandler.getError(context, ProdRegError.ACCESS_TOKEN_INVALID.getCode());
        assertEquals(prodRegErrorMap.getDescription(), context.getString(R.string.PPR_Authentication_ErrMsg));
        assertEquals(prodRegErrorMap.getTitle(), context.getString(R.string.PPR_Authentication_Fail_Title));

        prodRegErrorMap = errorHandler.getError(context, ProdRegError.NO_INTERNET_AVAILABLE.getCode());
        assertEquals(prodRegErrorMap.getDescription(), context.getString(R.string.PPR_No_Internet_ErrMsg));
        assertEquals(prodRegErrorMap.getTitle(), context.getString(R.string.PPR_No_Internet_Title));

        prodRegErrorMap = errorHandler.getError(context, ProdRegError.INTERNAL_SERVER_ERROR.getCode());
        assertEquals(prodRegErrorMap.getDescription(), context.getString(R.string.PPR_Unable_Connect_Server_ErrMsg));
        assertEquals(prodRegErrorMap.getTitle(), context.getString(R.string.PPR_Communication_Err_Title));

        prodRegErrorMap = errorHandler.getError(context, ProdRegError.TIME_OUT.getCode());
        assertEquals(prodRegErrorMap.getDescription(), context.getString(R.string.PPR_Unable_Connect_Server_ErrMsg));
        assertEquals(prodRegErrorMap.getTitle(), context.getString(R.string.PPR_Communication_Err_Title));

        prodRegErrorMap = errorHandler.getError(context, ProdRegError.NETWORK_ERROR.getCode());
        assertEquals(prodRegErrorMap.getDescription(), context.getString(R.string.PPR_Network_ErrMsg));
        assertEquals(prodRegErrorMap.getTitle(), context.getString(R.string.PPR_Network_Err_Title));

        prodRegErrorMap = errorHandler.getError(context, ProdRegError.INVALID_SERIALNUMBER.getCode());
        assertEquals(prodRegErrorMap.getDescription(), context.getString(R.string.PPR_SerialNum_Format_ErrMsg));
        assertEquals(prodRegErrorMap.getTitle(), context.getString(R.string.PPR_Invalid_SerialNum_Title));

        prodRegErrorMap = errorHandler.getError(context, ProdRegError.UNKNOWN.getCode());
        assertEquals(prodRegErrorMap.getDescription(), context.getString(R.string.PPR_Unknow_ErrMsg));
        assertEquals(prodRegErrorMap.getTitle(), context.getString(R.string.PPR_Unknow_ErrMsg));

        prodRegErrorMap = errorHandler.getError(context, ProdRegError.MISSING_CTN.getCode());
        assertEquals(prodRegErrorMap.getDescription(), context.getString(R.string.PPR_Missing_Ctn_ErrMsg));
        assertEquals(prodRegErrorMap.getTitle(), context.getString(R.string.PPR_Missing_Ctn_Title));
    }
}