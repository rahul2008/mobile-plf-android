package com.philips.platform.mya.uappadaptor;

/**
 * Created by philips on 11/16/17.
 */

public class ConsentDataInterface extends DataInterface {

    ConsentDataModel consentDataModel;
    @Override
    public DataModel getData(DataModelType dataModelType) {
        if(dataModelType==DataModelType.CONSENT){
            if(consentDataModel==null){
                consentDataModel=new ConsentDataModel();
            }
        }
        return consentDataModel;
    }
}
