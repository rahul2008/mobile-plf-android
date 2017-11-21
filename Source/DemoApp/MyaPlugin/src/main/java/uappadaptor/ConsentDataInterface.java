package uappadaptor;

import java.io.Serializable;

/**
 * Created by philips on 11/16/17.
 */

public abstract class ConsentDataInterface implements DataInterface, Serializable {


    @Override
    public abstract DataModel getData(DataModelType dataModelType);
}
