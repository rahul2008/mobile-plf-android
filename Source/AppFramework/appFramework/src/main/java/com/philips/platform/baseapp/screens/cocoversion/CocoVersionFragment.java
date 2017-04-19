package com.philips.platform.baseapp.screens.cocoversion;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.base.AppFrameworkBaseActivity;
import com.philips.platform.baseapp.base.AppFrameworkBaseFragment;
import com.philips.platform.baseapp.screens.settingscreen.SettingListItem;
import com.philips.platform.baseapp.screens.settingscreen.SettingListItemType;
import com.philips.platform.baseapp.screens.settingscreen.SettingsAdapter;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationSettingsState;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationState;

import java.util.ArrayList;

/**
 * Created by philips on 4/18/17.
 */

public class CocoVersionFragment extends AppFrameworkBaseFragment {
    public static final String TAG =CocoVersionFragment.class.getSimpleName();
    private ListView list = null;
    private ArrayList<CocoVersionItem> cocoVersionItemList;

    private CocoVersionAdapter adapter;
    @Override
    public void onResume() {
        super.onResume();
        updateActionBar();
    }

    protected void updateActionBar() {
        ((AppFrameworkBaseActivity)getActivity()).updateActionBarIcon(false);
    }
    private ArrayList<CocoVersionItem> buildCocoVersionScreenList() {
         ArrayList<CocoVersionItem> cocoVersionItemList = new ArrayList<CocoVersionItem>();

        cocoVersionItemList.add(formDataSection(getResourceString(R.string.RA_COCO_APPINFRA), CocoVersionItemType.HEADER));
        cocoVersionItemList.add(formDataSection(getResourceString(R.string.RA_COCO_UR), CocoVersionItemType.HEADER));
        cocoVersionItemList.add(formDataSection(getResourceString(R.string.RA_COCO_PR), CocoVersionItemType.HEADER));
        cocoVersionItemList.add(formDataSection(getResourceString(R.string.RA_COCO_IAP), CocoVersionItemType.HEADER));
        cocoVersionItemList.add(formDataSection(getResourceString(R.string.RA_COCO_Connectivity), CocoVersionItemType.HEADER));
        cocoVersionItemList.add(formDataSection(getResourceString(R.string.RA_COCO_UIKIT), CocoVersionItemType.HEADER));
        return cocoVersionItemList;
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.RA_Coco_Version);
    }
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.af_coco_version_information, container, false);
        list = (ListView) view.findViewById(R.id.listwithouticon);
        buildCocoVersionScreenList();
        //TextView version = (TextView) view.findViewById(R.id.about_version);
        return view;

    }
    private CocoVersionItem formDataSection(String cocoItem, CocoVersionItemType type) {
        CocoVersionItem cocoVersionItem = new CocoVersionItem();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            cocoVersionItem.title = Html.fromHtml(cocoItem, Html.FROM_HTML_MODE_LEGACY);
        }
        else{
            cocoVersionItem.title = Html.fromHtml(cocoItem);
        }

        cocoVersionItem.type = type;
        return cocoVersionItem;
    }
    private String getResourceString(int resId) {
        if (isAdded() && getActivity() != null) {
            return getActivity().getApplicationContext().getString(resId);
        }
        return "";
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cocoVersionItemList=buildCocoVersionScreenList();

        adapter = new CocoVersionAdapter(getActivity(), cocoVersionItemList, fragmentPresenter);
        list.setAdapter(adapter);
    }
}
