/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake.selectimage;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.americanwell.sdk.entity.consumer.DocumentRecord;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.uid.view.widget.Button;

import java.util.List;

public class THSSelectedImageFragment extends DialogFragment implements View.OnClickListener,THSSelectedImageFragmentViewCallback {

    public static String TAG = THSSelectedImageFragment.class.getSimpleName();
    public List<THSSelectedImagePojo> selectedImagePojoList;
    private int selectedPosition;
    private CustomPagerAdapter pagerAdapter;
    private ViewPager imageViewPager;
    private Button deleteImageButton;
    private THSOnDismissSelectedImageFragmentCallback onDismissSelectedImageFragmentCallback;
    private THSSelectedImageFragmentPresenter thsSelectedImageFragmentPresenter;
    private List<DocumentRecord> documentRecordList;
    private ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ths_selected_image_dialog_fragment, container, false);
        progressDialog = new ProgressDialog(getFragmentActivity());
        thsSelectedImageFragmentPresenter = new THSSelectedImageFragmentPresenter(this);
        pagerAdapter = new CustomPagerAdapter(getActivity());
        deleteImageButton = (Button) view.findViewById(R.id.ths_delete_selected_image_button);
        deleteImageButton.setOnClickListener(this);
        imageViewPager = (ViewPager) view.findViewById(R.id.selected_image_pager);
        imageViewPager.setAdapter(pagerAdapter);
        imageViewPager.setCurrentItem(selectedPosition);
        return view;
    }

    public void setSelectedImage(int selectedPosition, List<THSSelectedImagePojo> selectedImagePojoList, List<DocumentRecord> documentRecordList) {
        this.selectedImagePojoList = selectedImagePojoList;
        this.selectedPosition = selectedPosition;
        this.documentRecordList = documentRecordList;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ths_delete_selected_image_button) {
            deletePhoto();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        onDismissSelectedImageFragmentCallback.dismissSelectedImageFragment(selectedImagePojoList);
    }

    private void deletePhoto() {
        int deletedItemPosition = updateViewAndDeleteList();

        if (deletedItemPosition == selectedImagePojoList.size() - 1) {
            imageViewPager.setCurrentItem(deletedItemPosition - 1);
        } else {
            imageViewPager.setCurrentItem(deletedItemPosition + 1);
        }


    }

    private int updateViewAndDeleteList() {
        int deletedItemPosition = imageViewPager.getCurrentItem();
        thsSelectedImageFragmentPresenter.deleteDocument(documentRecordList.get(deletedItemPosition));
        selectedImagePojoList.remove(deletedItemPosition);
        pagerAdapter.notifyDataSetChanged();
        return deletedItemPosition;
    }

    public void setSelectedImageFragmentCallback(THSOnDismissSelectedImageFragmentCallback onDismissSelectedImageFragmentCallback) {
        this.onDismissSelectedImageFragmentCallback = onDismissSelectedImageFragmentCallback;
    }

    @Override
    public void finishActivityAffinity() {

    }

    @Override
    public FragmentActivity getFragmentActivity() {
        return getActivity();
    }

    @Override
    public int getContainerID() {
        return 0;
    }

    @Override
    public void addFragment(THSBaseFragment fragment, String fragmentTag, Bundle bundle) {

    }

    @Override
    public void updateProgreeDialog(boolean show) {
        if(show){
            progressDialog.setMessage("Deleting photo");
            progressDialog.show();
        }
        else {
            progressDialog.dismiss();
            if (selectedImagePojoList.size() <= 0) {
                onDismissSelectedImageFragmentCallback.dismissSelectedImageFragment(selectedImagePojoList);
            }
        }
    }

    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            if(null != selectedImagePojoList && selectedImagePojoList.size() > 0){
                return selectedImagePojoList.size();
            }else {
                return 0;
            }

        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.ths_selected_image_pager_layout, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.ths_selected_image);
            int width = BitmapFactory.decodeFile(selectedImagePojoList.get(position).getPath()).getWidth();
            imageView.setMaxHeight(width);
            imageView.setImageBitmap(BitmapFactory.decodeFile(selectedImagePojoList.get(position).getPath()));

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }
}
