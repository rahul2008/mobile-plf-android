/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.ui;

import com.philips.platform.appframework.models.Chapter;
import com.philips.platform.appframework.models.CommonComponent;

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
