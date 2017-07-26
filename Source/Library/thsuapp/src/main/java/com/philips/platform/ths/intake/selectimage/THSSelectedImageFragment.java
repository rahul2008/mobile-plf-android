package com.philips.platform.ths.intake.selectimage;

import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.philips.platform.ths.R;
import com.philips.platform.uid.view.widget.Button;

import java.util.List;

public class THSSelectedImageFragment extends DialogFragment implements View.OnClickListener {

    public static String TAG = THSSelectedImageFragment.class.getSimpleName();
    public List<THSSelectedImagePojo> selectedImagePojoList;
    private int selectedPosition;
    private CustomPagerAdapter pagerAdapter;
    private ViewPager imageViewPager;
    private Button deleteImageButton;
    THSOnDismissSelectedImageFragmentCallback onDismissSelectedImageFragmentCallback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ths_selected_image_dialog_fragment,container,false);
        pagerAdapter = new CustomPagerAdapter(getActivity());
        deleteImageButton = (Button) view.findViewById(R.id.ths_delete_selected_image_button);
        deleteImageButton.setOnClickListener(this);
        imageViewPager = (ViewPager) view.findViewById(R.id.selected_image_pager);
        imageViewPager.setAdapter(pagerAdapter);
        imageViewPager.setCurrentItem(selectedPosition);
        return view;
    }

    public void setSelectedImage(int selectedPosition ,List<THSSelectedImagePojo> selectedImagePojoList){
        this.selectedImagePojoList = selectedImagePojoList;
        this.selectedPosition = selectedPosition;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ths_delete_selected_image_button){
            deletePhoto();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        onDismissSelectedImageFragmentCallback.dismissSelectedImageFragment(selectedImagePojoList);
    }

    private void deletePhoto() {

        int deletedItemPosition = imageViewPager.getCurrentItem();

        if(deletedItemPosition == selectedImagePojoList.size() - 1){
            selectedImagePojoList.remove(deletedItemPosition);
            pagerAdapter.notifyDataSetChanged();
            imageViewPager.setCurrentItem(deletedItemPosition-1);
        }else {
            selectedImagePojoList.remove(deletedItemPosition);
            pagerAdapter.notifyDataSetChanged();
            imageViewPager.setCurrentItem(deletedItemPosition+1);
        }
        if(selectedImagePojoList.size() == 0){
            onDismissSelectedImageFragmentCallback.dismissSelectedImageFragment(selectedImagePojoList);
        }

    }

    public void setSelectedImageFragmentCallback(THSOnDismissSelectedImageFragmentCallback onDismissSelectedImageFragmentCallback){
        this.onDismissSelectedImageFragmentCallback = onDismissSelectedImageFragmentCallback;
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
            return selectedImagePojoList.size();
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
