package com.philips.platform.appframework.testmicroappfw.ui;


import com.philips.platform.appframework.testmicroappfw.models.Chapter;

import java.util.ArrayList;

/**
 * Created by philips on 10/02/17.
 */

public interface TestContract {
    interface View {
        void displayChapterList(ArrayList<Chapter> chaptersList);

        void showCoCoList(Chapter chapter);
    }

    interface UserActionsListener {
        void loadChapterList();
    }
}
