/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake.selectimage;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
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
import com.philips.platform.uid.view.widget.Label;

import java.util.ArrayList;
import java.util.List;

public class THSSelectedImageFragment extends DialogFragment implements View.OnClickListener,THSSelectedImageFragmentViewCallback {

    public static String TAG = THSSelectedImageFragment.class.getSimpleName();
    public ArrayList<THSSelectedImagePojo> selectedImagePojoList;
    private int selectedPosition;
    private CustomPagerAdapter pagerAdapter;
    private ViewPager imageViewPager;
    private Button deleteImageButton;
    private Label ths_selected_image_close_icon,ths_selected_image_scroll_left,ths_selected_image_scroll_right;
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
        ths_selected_image_close_icon = (Label) view.findViewById(R.id.ths_selected_image_close_icon);
        ths_selected_image_scroll_right = (Label) view.findViewById(R.id.ths_selected_image_scroll_right);
        ths_selected_image_scroll_left = (Label) view.findViewById(R.id.ths_selected_image_scroll_left);
        ths_selected_image_scroll_left.setOnClickListener(this);
        ths_selected_image_scroll_right.setOnClickListener(this);
        ths_selected_image_close_icon.setOnClickListener(this);
        deleteImageButton.setOnClickListener(this);
        imageViewPager = (ViewPager) view.findViewById(R.id.selected_image_pager);
        imageViewPager.setAdapter(pagerAdapter);
        imageViewPager.setCurrentItem(selectedPosition);
        return view;
    }

    public void setSelectedImage(int selectedPosition, ArrayList<THSSelectedImagePojo> selectedImagePojoList, List<DocumentRecord> documentRecordList) {
        this.selectedImagePojoList = selectedImagePojoList;
        this.selectedPosition = selectedPosition;
        this.documentRecordList = documentRecordList;
    }

    // Images left navigation


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ths_delete_selected_image_button) {
            updateViewAndDeleteList();
        }
        if(v.getId() == R.id.ths_selected_image_close_icon){
            dismiss();
        }
        if(v.getId() == R.id.ths_selected_image_scroll_left){
            int tab = imageViewPager.getCurrentItem();
            if (tab > 0) {
                tab--;
                imageViewPager.setCurrentItem(tab);
            } else if (tab == 0) {
                imageViewPager.setCurrentItem(tab);
            }
        }
        if(v.getId() == R.id.ths_selected_image_scroll_right){
            int tab = imageViewPager.getCurrentItem();
            tab++;
            imageViewPager.setCurrentItem(tab);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        onDismissSelectedImageFragmentCallback.dismissSelectedImageFragment(selectedImagePojoList);
    }

    private int updateViewAndDeleteList() {
        int deletedItemPosition = imageViewPager.getCurrentItem();
        if(null != documentRecordList && documentRecordList.size() > 0) {
            thsSelectedImageFragmentPresenter.deleteDocument(documentRecordList.get(deletedItemPosition));
            documentRecordList.remove(documentRecordList.get(deletedItemPosition));
            selectedImagePojoList.remove(deletedItemPosition);
            pagerAdapter.notifyDataSetChanged();
            return deletedItemPosition;
        }
        else {
            return -1;
        }
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
    public void addFragment(THSBaseFragment fragment, String fragmentTag, Bundle bundle, boolean isReplace) {

    }

    @Override
    public void popFragmentByTag(String fragmentTag, int flag) {

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

    @Override
    public void showToast(String toastMessange) {

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
            //int width = BitmapFactory.decodeFile(selectedImagePojoList.get(position).getPath()).getWidth()imageView.setMaxHeight(width);
            imageView.setImageBitmap(decodeSampledBitmapFromResource(selectedImagePojoList.get(position).getPath(),200,200));

            container.addView(itemView);

            return itemView;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }

        @Override
        public int getItemPosition(Object object){
            return PagerAdapter.POSITION_NONE;
        }
    }

    public static Bitmap decodeSampledBitmapFromResource(String resourceFilePath,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //BitmapFactory.decodeResource(res, resId, options);
        BitmapFactory.decodeFile(resourceFilePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return  BitmapFactory.decodeFile(resourceFilePath, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
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
