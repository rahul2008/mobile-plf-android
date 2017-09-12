package com.philips.platform.appframework.testmicroappfw.ui;

import com.philips.platform.appframework.testmicroappfw.models.Chapter;
import com.philips.platform.appframework.testmicroappfw.models.CommonComponent;

import java.util.ArrayList;

/**
 * Created by philips on 13/02/17.
 */

public interface COCOListContract {
     interface View{
        void displayCoCoList(ArrayList<CommonComponent> commonComponentsList);

    }

     interface UserActionsListener{
        void loadCoCoList(Chapter chapter);
    }

}
