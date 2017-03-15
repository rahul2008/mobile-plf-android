package com.philips.platform.appframework.testmicroappfw.ui;

import android.content.Context;

import com.philips.platform.appframework.testmicroappfw.models.Chapter;
import com.philips.platform.appframework.testmicroappfw.models.CommonComponent;

import java.util.ArrayList;

/**
 * Created by philips on 13/02/17.
 */

public class COCOListContract {
    public interface View{
        void displayCoCoList(ArrayList<CommonComponent> commonComponentsList);

    }

    public interface UserActionsListener{
        void loadCoCoList(Chapter chapter);
    }

}
