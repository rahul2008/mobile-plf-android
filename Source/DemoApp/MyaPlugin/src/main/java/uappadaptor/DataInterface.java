package uappadaptor;

import java.io.Serializable;

/**
 * Created by philips on 11/17/17.
 */

public interface DataInterface extends Serializable {

   DataModel getData(DataModelType dataModelType);



}
