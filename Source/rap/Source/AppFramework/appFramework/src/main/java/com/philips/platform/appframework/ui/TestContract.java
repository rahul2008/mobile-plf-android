/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.ui;


import com.philips.platform.appframework.models.Chapter;

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
