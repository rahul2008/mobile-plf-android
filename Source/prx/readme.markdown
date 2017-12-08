## Latest Version 3.0.0

###changes

> Removed :
>>- public String getServerInfo()
>>- public String getRequestUrl()
>>- public String getLocaleMatchResult()
>>- public void setLocaleMatchResult(String localeMatch)


>Added :

>>- public void getRequestUrlFromAppInfra(AppInfraInterface appinfra , OnUrlReceived listener );
<br>-- This method returns the base url of prx from service discovery based on country. Replacement method for getRequestUrl 
>>- public interface OnUrlReceived extends ServiceDiscoveryInterface.OnErrorListener {     
		void onSuccess(String url);
    }
<br>-- call  back onUrlReceived
>>-   String getCtn();
>>- Added constructors in base class:
<br>-- PrxRequest(String ctn, String serviceId)
<br>-- PrxRequest(String ctn, String serviceID, Sector sector, Catalog catalog)


>1) AppInfra is no longer depends on PRXClient ,so if anyone consuming PRXClient should add it directly to dependencies in gradle <br>
>2) Added AppInfra dependency in PRX.<br>
>3) We are taking Locale from ServiceDiscovery , Hence setting LocaleMatch in Prx is not required . We are using LocaleMatch only to get the Sector and CTN enum definitions. This we are planning to bring to PRXClient so the we can remove PILocaleMatch completely <br>
>4) Created PRXDependency class in PRX to inject AppInfra . Example usage <br>
>5) If you are subclassing PRXRequest, public String getRequestUrl() method is deprecated. You should use getRequestUrlFromAppInfra which already have the default implementation 
```
```
Created PRXDependency class in PRX to inject AppInfra . Example usage in Demo App :

PRXDependencies  prxDependencies = new PRXDependencies(context , mAppInfra); // use existing appinfra instance
RequestManager mRequestManager = new RequestManager();
mRequestManager.init(prxDependencies); // pass prxdependency.

```
deprecated <br>
class ->  PRXLocaleMatch

## Synopsis

The main functionality of this library is to download any data related to product present on PRX. It can be used by consumer care, registration and different applications. This library can be reused by other projects with minimal development changes as a generic network component as well.
PRX client library exposes classes and APIs to clients to send a request and get a response. Library also helps clients to customise the requests.

1	Client
It can be an application, consumer care component or registration component. <br>
2	RequestManager 
It provides set of public APIs for placing requests from client and also talks to Network wrapper class for performing network operations. <br>
3	Responsehandler
Handles response like invoking respective builders to build the response. It also invokes listener/blocks.<br>
4	Product/ProductSummary/ProductAssets
Model data for each request type.<br>


## Installation

* Add prx to dependencies in gradle file
compile(group: 'com.philips.cdp', name: 'prx', version: '3.0.0', ext: 'aar')

## Code Example - Quick integration



* Create Any request

```
  ProductSummaryRequest mProductSummeryBuilder = new ProductSummaryRequest(selectedCtn, mRequestTag);
						 
```

* Create Request Manager and call execute API

```
    //Dont create new instance of Appinfra instead inject the existing instance
	PRXDependencies  prxDependencies = new PRXDependencies(context , mAppInfra); 
	RequestManager mRequestManager = new RequestManager();
	mRequestManager.init(prxDependencies); // pass prxdependency.

	mRequestManager.executeRequest(prxRequest, new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {
                String str = responseData.getClass().toString();
                
            }

            @Override
            public void onResponseError(PrxError prxError) {
                Log.d(TAG, "Response Error Message PRX: " + prxError.getDescription());
                
            }
        });   


```
## PRXRequest Subclass

```
public class ProductSummaryRequest extends PrxRequest {

    private static final String PRXSummaryDataServiceID = "prxclient.summary";
    private String mRequestTag = null;


    public ProductSummaryRequest(String ctn, String requestTag) {
        super(ctn, PRXSummaryDataServiceID);
        this.mRequestTag = requestTag;
    }

    public ProductSummaryRequest(String ctn, Sector sector, Catalog catalog, String requestTag) {
        super(ctn, PRXSummaryDataServiceID, sector, catalog);
        this.mRequestTag = requestTag;
    }
	
	@Override
    public ResponseData getResponseData(JSONObject jsonObject) {

        return new SummaryModel().parseJsonResponseData(jsonObject);
    }
}


//Implement following functions if needed
-  public int getRequestType() 
-  public Map<String, String> getHeaders();
-  public Map<String, String> getParams();
-  public int getRequestTimeOut();
-  public void setRequestTimeOut(final int requestTimeOut);
-  public abstract ResponseData getResponseData(JSONObject jsonObject);
-  public void getRequestUrlFromAppInfra(final AppInfraInterface appInfra, final OnUrlReceived listener)

@end

```
*note : getRequestUrlFromAppInfra have default implementation which will return serviceUrl from service discovery based on the serviceID. It will also replace placeholders in serviceURl like sector, catalog and ctn
## <a name="team-members"></a>Team Members
 * Raymond Kloprogge - <raymond.kloprogge@philips.com> - Architecture
 * G Kavya - <kavya.g.kurpad@philips.com> - Developer
 * Anurag Gautam - <anurag.gautam@philips.com> - Developer
 * Adarsh Shetty - <adarsha.shetty@philips.com> - Developer

## License
 * Copyright (c) Koninklijke Philips N.V., 2017 All rights are reserved. Reproduction or dissemination in whole or in part is prohibited without the prior written consent of the copyright holder.*

## <a name="api-references"></a>API References
 * see external folder 
