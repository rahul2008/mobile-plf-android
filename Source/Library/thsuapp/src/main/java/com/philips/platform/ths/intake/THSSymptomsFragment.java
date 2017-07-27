package com.philips.platform.ths.intake;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.americanwell.sdk.entity.visit.Topic;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.intake.selectimage.THSImageRecyclerViewAdapter;
import com.philips.platform.ths.intake.selectimage.THSOnDismissSelectedImageFragmentCallback;
import com.philips.platform.ths.intake.selectimage.THSSelectedImageCallback;
import com.philips.platform.ths.intake.selectimage.THSSelectedImageFragment;
import com.philips.platform.ths.intake.selectimage.THSSelectedImagePojo;
import com.philips.platform.ths.providerslist.THSOnDemandSpeciality;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.CheckBox;
import com.philips.platform.uid.view.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class THSSymptomsFragment extends THSBaseFragment implements BackEventListener, View.OnClickListener,
        THSSelectedImageCallback,THSOnDismissSelectedImageFragmentCallback {
    public static final String TAG = THSSymptomsFragment.class.getSimpleName();
    private THSSymptomsPresenter mTHSSymptomsPresenter;
    private THSProviderInfo mThsProviderInfo;
    private THSOnDemandSpeciality thsOnDemandSpeciality;
    private LinearLayout topicLayout;
    private ImageButton camera_button;
    private Button mContinue;
    private RelativeLayout mRelativeLayout;
    private THSVisitContext mThsVisitContext;
    private String userChoosenTask;
    private RecyclerView imageListView;
    private THSImageRecyclerViewAdapter thsImageRecyclerViewAdapter;
    private List<THSSelectedImagePojo> selectedImagePojosList;
    public static final int REQUEST_READ_EXTERNAL_STORAGE_AN_CAMERA = 123;
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE = 124;
    private Dialog dialog;
    private Uri mCapturedImageURI;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private THSConsumer thsConsumer;

    //TODO: Spoorti - check null condition for the bundle Arguments
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_symptoms, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mThsProviderInfo = bundle.getParcelable(THSConstants.THS_PROVIDER_INFO);
            thsOnDemandSpeciality = bundle.getParcelable(THSConstants.THS_ON_DEMAND);
        }
        imageListView = (RecyclerView) view.findViewById(R.id.imagelist);
        imageListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        selectedImagePojosList = new ArrayList<>();
        thsImageRecyclerViewAdapter = new THSImageRecyclerViewAdapter(selectedImagePojosList, this);
        topicLayout = (LinearLayout) view.findViewById(R.id.checkbox_container);
        camera_button = (ImageButton) view.findViewById(R.id.camera_click_button);
        camera_button.setOnClickListener(this);
        mContinue = (Button) view.findViewById(R.id.continue_btn);
        mContinue.setOnClickListener(this);
        mRelativeLayout = (RelativeLayout) view.findViewById(R.id.symptoms_container);
        mTHSSymptomsPresenter = new THSSymptomsPresenter(this);
        requestWritePermission();
        return view;
    }


    public void setConsumerObject(THSConsumer thsConsumer) {
        this.thsConsumer = thsConsumer;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

         mTHSSymptomsPresenter = new THSSymptomsPresenter(this, mThsProviderInfo);
        if (null != getActionBarListener()) {
            getActionBarListener().updateActionBar(getString(R.string.pth_prepare_your_visit), true);
        }
         getVisistContext();
    }

    @Override
    public boolean handleBackEvent() {
        THSManager.getInstance().setVisitContext(null);
        return false;
    }

    private void getVisistContext() {
        if (mThsVisitContext == null) {
            createCustomProgressBar(mRelativeLayout, BIG);
            mContinue.setEnabled(false);
            if (mThsProviderInfo != null) {
                mTHSSymptomsPresenter.getVisitContext();
            } else {
                try {
                    mTHSSymptomsPresenter.getfirstAvailableProvider(thsOnDemandSpeciality);
                } catch (AWSDKInstantiationException e) {
                    e.printStackTrace();
                }
            }
        } else {
            mContinue.setEnabled(true);
            addTopicsToView(THSManager.getInstance().getPthVisitContext());
        }
    }

    //TODO: SPOORTI - crashing when back is pressed
    public void addTopicsToView(THSVisitContext visitContext) {
        mThsVisitContext = visitContext;
        if (getContext() != null) {
            List<Topic> topics = visitContext.getTopics();
            for (final Topic topic : topics
                    ) {
                CheckBox checkBox = new CheckBox(getContext());
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                checkBox.setLayoutParams(layoutParams);
                checkBox.setEnabled(true);
                checkBox.setText(topic.getTitle());
                if (topic.isSelected()) {
                    checkBox.setChecked(true);
                }
                topicLayout.addView(checkBox);
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        topic.setSelected(true);
                    }
                });
            }
        }
        mContinue.setEnabled(true);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.continue_btn) {
            mTHSSymptomsPresenter.onEvent(R.id.continue_btn);
        }
        if (i == R.id.camera_click_button) {
            selectImage();
        }
        if (i == R.id.cancel_dialog) {
            dialog.dismiss();
        }
        if (i == R.id.select_from_gallery) {
            userChoosenTask = "Choose from Library";
            dialog.dismiss();
            requestPermission();
        }
        if (i == R.id.camera_image) {
            userChoosenTask = "Take Photo";
            dialog.dismiss();
            requestPermission();
        }
    }

    /**
     * Pick galley photos code below
     */

    private void selectImage() {

        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.ths_choose_image_dialog);
        dialog.setCancelable(false);
        dialog.setTitle("Add Photo!");
        Button cancelDialogButton = (Button) dialog.findViewById(R.id.cancel_dialog);
        cancelDialogButton.setText("Cancel");
        cancelDialogButton.setOnClickListener(this);
        Button selectFromGalleryButton = (Button) dialog.findViewById(R.id.select_from_gallery);
        selectFromGalleryButton.setOnClickListener(this);
        selectFromGalleryButton.setText("Select from gallery");
        Button selectFromCameraButton = (Button) dialog.findViewById(R.id.camera_image);
        selectFromCameraButton.setOnClickListener(this);
        selectFromCameraButton.setText("Take a photo");
        dialog.show();
    }

    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA},
                    REQUEST_READ_EXTERNAL_STORAGE_AN_CAMERA);
        } else {
            if (userChoosenTask.equals("Take Photo")) {
                cameraIntent();
            } else if (userChoosenTask.equals("Choose from Library")) {
                galleryIntent();
            }
        }
    }

    private void requestWritePermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            mTHSSymptomsPresenter.fetchHealthDocuments(thsConsumer);
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    private void cameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            String fileName = "temp.jpg";
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            mCapturedImageURI = getActivity().getContentResolver()
                    .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            values);
            takePictureIntent
                    .putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String picturePath = null;
        switch (requestCode) {
            case RESULT_LOAD_IMAGE:
                if (requestCode == RESULT_LOAD_IMAGE &&
                        resultCode == RESULT_OK && null != data) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver()
                            .query(selectedImage, filePathColumn, null, null,
                                    null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    mCapturedImageURI = data.getData();
                    cursor.close();

                }
                break;
            case REQUEST_IMAGE_CAPTURE:
                if (requestCode == REQUEST_IMAGE_CAPTURE &&
                        resultCode == RESULT_OK) {
                    String[] projection = {MediaStore.Images.Media.DATA};
                    Cursor cursor =
                            getActivity().managedQuery(mCapturedImageURI, projection, null,
                                    null, null);
                    int column_index_data = cursor.getColumnIndexOrThrow(
                            MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    picturePath = cursor.getString(column_index_data);
                    cursor.close();
                }
                break;
        }

        THSSelectedImagePojo image = new THSSelectedImagePojo();
        image.setTitle("SelectedImage");
        image.setDatetime(System.currentTimeMillis());
        image.setPath(picturePath);
        selectedImagePojosList.add(image);
        thsImageRecyclerViewAdapter.notifyDataSetChanged();
        imageListView.setAdapter(thsImageRecyclerViewAdapter);
        mTHSSymptomsPresenter.uploadDocuments(thsConsumer, mCapturedImageURI);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_EXTERNAL_STORAGE_AN_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    showToast("Permission to select image denied");
                }
                break;
            case REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mTHSSymptomsPresenter.fetchHealthDocuments(thsConsumer);
                } else {
                    showToast("Permission to select image denied");
                }
        }

    }

    THSSelectedImageFragment thsSelectedImageFragment;

    @Override
    public void onImageClicked(int position) {
        thsSelectedImageFragment = new THSSelectedImageFragment();
        thsSelectedImageFragment.setSelectedImage(position, selectedImagePojosList);
        thsSelectedImageFragment.setSelectedImageFragmentCallback(this);
        thsSelectedImageFragment.show(getActivity().getSupportFragmentManager(), "");
    }

    @Override
    public void dismissSelectedImageFragment(List<THSSelectedImagePojo> selectedImagePojoList){
        this.selectedImagePojosList = selectedImagePojoList;
        thsImageRecyclerViewAdapter.notifyDataSetChanged();
        imageListView.setAdapter(thsImageRecyclerViewAdapter);
        thsSelectedImageFragment.dismiss();

    }

}
