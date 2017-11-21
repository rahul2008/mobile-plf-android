package consentadaptor;

import uappadaptor.ConsentDataInterface;
import uappadaptor.ConsentDataModel;
import uappadaptor.DataModel;
import uappadaptor.DataModelType;

/**
 * Created by philips on 11/17/17.
 */

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
