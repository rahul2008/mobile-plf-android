/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.prxrequest;

import android.net.Uri;
import android.util.Log;
import com.philips.cdp.prodreg.launcher.PRUiHelper;
import com.philips.cdp.prodreg.logging.ProdRegLogger;
import com.philips.cdp.prodreg.model.summary.ProductSummaryResponse;
import com.philips.cdp.prxclient.PrxConstants;
import com.philips.cdp.prxclient.request.PrxRequest;
import com.philips.cdp.prxclient.request.RequestType;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProductSummaryRequest extends PrxRequest {

    public ProductSummaryRequest(String ctn, String serviceID, PrxConstants.Sector sector, PrxConstants.Catalog catalog) {
        super(ctn, serviceID, sector, catalog);
    }

    @Override
    public ResponseData getResponseData(final JSONObject jsonObject) {
        return new ProductSummaryResponse().parseJsonResponseData(jsonObject);
    }

    @Override
    public int getRequestType() {
        return RequestType.GET.getValue();
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }

    @Override
    public Map<String, String> getParams() {
        return null;
    }

    @Override
    public int getRequestTimeOut() {
        return 30000;
    }
}
