/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.dprdemo.ui;

import com.philips.platform.datasync.subjectProfile.UCoreSubjectProfile;
import com.philips.platform.dprdemo.pojo.SubjectProfile;

import java.util.List;

interface CreateSubjectProfileViewListener {
    boolean validateViews();

    SubjectProfile getSubjectProfile();

    void showToastMessage();

    void onCreateSubjectProfile(List<UCoreSubjectProfile> list);

    void onError(String message);
}
