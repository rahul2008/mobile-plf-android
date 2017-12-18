package com.philips.platform.myaplugin.consentadaptor;

import com.philips.platform.myaplugin.uappadaptor.ConsentDataInterface;
import com.philips.platform.myaplugin.uappadaptor.ConsentDataModel;
import com.philips.platform.myaplugin.uappadaptor.DataModel;
import com.philips.platform.myaplugin.uappadaptor.DataModelType;

public class ConsentDataModelProvider extends ConsentDataInterface {
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
