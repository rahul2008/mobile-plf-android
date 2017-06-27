package com.philips.platform.appframework.testmicroappfw.ui;


import com.philips.platform.appframework.testmicroappfw.models.Chapter;
import com.philips.platform.appframework.testmicroappfw.models.CommonComponent;

import java.util.ArrayList;

/**
 * Created by philips on 10/02/17.
 */

public class TestContract {
    public interface View{
        void displayChapterList(ArrayList<Chapter> chaptersList);

        void showCoCoList(Chapter chapter);
    }
    public interface UserActionsListener{
        void loadChapterList();
    }
}
