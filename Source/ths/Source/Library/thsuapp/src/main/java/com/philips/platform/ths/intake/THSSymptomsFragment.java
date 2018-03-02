/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake;

import android.Manifest;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.americanwell.sdk.entity.consumer.DocumentRecord;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.entity.visit.Topic;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.intake.selectimage.THSImageRecyclerViewAdapter;
import com.philips.platform.ths.intake.selectimage.THSOnDismissSelectedImageFragmentCallback;
import com.philips.platform.ths.intake.selectimage.THSSelectedImageCallback;
import com.philips.platform.ths.intake.selectimage.THSSelectedImageFragment;
import com.philips.platform.ths.intake.selectimage.THSSelectedImagePojo;
import com.philips.platform.ths.providerslist.THSOnDemandSpeciality;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSFileUtils;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSSharedPreferenceUtility;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.CheckBox;
import com.philips.platform.uid.view.widget.EditText;
import com.philips.platform.uid.view.widget.ImageButton;
import com.philips.platform.uid.view.widget.Label;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.philips.platform.ths.utility.THSConstants.THS_ANDROID_CAMERA;
import static com.philips.platform.ths.utility.THSConstants.THS_ANDROID_GALLERY;
import static com.philips.platform.ths.utility.THSConstants.THS_SEND_DATA;
import static com.philips.platform.ths.utility.THSConstants.THS_SPECIAL_EVENT;
import static com.philips.platform.ths.utility.THSConstants.THS_SYMPTOMS_PAGE;

@SuppressWarnings("serial")
public class THSSymptomsFragment extends THSBaseFragment implements View.OnClickListener,
        THSSelectedImageCallback, THSOnDismissSelectedImageFragmentCallback, View.OnTouchListener, THSSymptomsFragmentViewInterface {
    public static final String TAG = THSSymptomsFragment.class.getSimpleName();
    protected THSSymptomsPresenter thsSymptomsPresenter;
    private THSProviderInfo mThsProviderInfo;
    protected THSOnDemandSpeciality thsOnDemandSpeciality;
    protected LinearLayout topicLayout;
    private ImageButton camera_button;
    protected Button mContinue;
    private RelativeLayout mRelativeLayout, ths_symptoms_relative_layout, ths_camera_image_list_layout;
    protected THSVisitContext mThsVisitContext;
    private String userChosenTask;
    private RecyclerView imageListView;
    private THSImageRecyclerViewAdapter thsImageRecyclerViewAdapter;
    private ArrayList<THSSelectedImagePojo> selectedImagePojoList;
    public static final int REQUEST_READ_EXTERNAL_STORAGE_AN_CAMERA = 123;
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE = 124;
    protected Dialog dialog;
    private Uri mCapturedImageURI;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private THSConsumerWrapper thsConsumerWrapper;
    private THSSelectedImageFragment thsSelectedImageFragment;
    private ArrayList<DocumentRecord> documentRecordList;
    private THSFileUtils thsFileUtils;
    private EditText additional_comments_edittext;
    private String UPLOAD_DOC_IMAGE_LIST = "UPLOAD_DOC_IMAGE_LIST";
    private Provider mProvider;
    protected String tagActions = "";
    public static long visitStartTime;
    private Label mLabelPatientName;
    protected static int NumberOfConditionSelected = 0;
    private final String TAG_SYMPTOMS_CHECKED="SymptomsChecked";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_intake_symptoms, container, false);
        visitStartTime = THSTagUtils.getCurrentTime();

        if(THSManager.getInstance().getThsTagging()!=null) {
            THSManager.getInstance().getThsTagging().trackTimedActionStart("totalPreparationTimePreVisit");
            THSTagUtils.doTrackActionWithInfo("totalPrepartationTimeStart", null, null);
        }

        thsFileUtils = new THSFileUtils();
        documentRecordList = new ArrayList<>();
        selectedImagePojoList = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            mThsProviderInfo = bundle.getParcelable(THSConstants.THS_PROVIDER_INFO);
            mProvider = bundle.getParcelable(THSConstants.THS_PROVIDER);
            thsOnDemandSpeciality = bundle.getParcelable(THSConstants.THS_ON_DEMAND);
        }


        String jsonValue = THSSharedPreferenceUtility.getString(getContext(), THSConstants.THS_SAVE_UPLOAD_IMAGE_KEY, null);
        if (null != jsonValue && jsonValue.length() > 0) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<THSSelectedImagePojo>>() {
            }.getType();
            selectedImagePojoList = gson.fromJson(jsonValue, type);
        }

        /**
         * Observed the following exception while saving and restoring the documentRecordList using the above method.
         * So saving in THSManager as a singleton.
         * java.lang.RuntimeException:
         * Unable to invoke no-args constructor for interface  Register an InstanceCreator with Gson for this type may fix this problem
         */
        if (null != THSManager.getInstance().getTHSDocumentList()) {
            documentRecordList = THSManager.getInstance().getTHSDocumentList();
        }


        ths_camera_image_list_layout = (RelativeLayout) view.findViewById(R.id.ths_camera_image_list_layout);
        ths_symptoms_relative_layout = (RelativeLayout) view.findViewById(R.id.ths_symptoms_relative_layout);
        ths_symptoms_relative_layout.setVisibility(View.INVISIBLE);
        additional_comments_edittext = (EditText) view.findViewById(R.id.additional_comments_edittext);
        additional_comments_edittext.setOnTouchListener(this);
        imageListView = (RecyclerView) view.findViewById(R.id.imagelist);


        mLabelPatientName = (Label) view.findViewById(R.id.ths_symptoms_patient_name);
        String name = getString(R.string.ths_dependent_name, THSManager.getInstance().getThsConsumer(getContext()).getFirstName());
        mLabelPatientName.setText(name);

        imageListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        thsImageRecyclerViewAdapter = new THSImageRecyclerViewAdapter(selectedImagePojoList, this);
        thsImageRecyclerViewAdapter.isClickable = true;
        imageListView.setAdapter(thsImageRecyclerViewAdapter);
        topicLayout = (LinearLayout) view.findViewById(R.id.checkbox_container);
        camera_button = (ImageButton) view.findViewById(R.id.camera_click_button);
        camera_button.setOnClickListener(this);
        mContinue = (Button) view.findViewById(R.id.continue_btn);
        mContinue.setOnClickListener(this);
        mRelativeLayout = (RelativeLayout) view.findViewById(R.id.symptoms_container);
        thsSymptomsPresenter = new THSSymptomsPresenter(this);
        thsSymptomsPresenter = new THSSymptomsPresenter(this, mThsProviderInfo);
        if (null != getActionBarListener()) {
            getActionBarListener().updateActionBar(getString(R.string.ths_prepare_your_visit), true);
        }
        getVisitContext();
        return view;
    }

    public void setConsumerObject(THSConsumerWrapper thsConsumerWrapper) {
        this.thsConsumerWrapper = thsConsumerWrapper;
    }

    @Override
    public boolean handleBackEvent() {
        THSManager.getInstance().setVisitContext(null);
        return false;
    }

    protected void getVisitContext() {
        if (mThsVisitContext == null) {
            createCustomProgressBar(mRelativeLayout, BIG);
            mContinue.setEnabled(false);
            if (mThsProviderInfo != null || mProvider != null) {
                thsSymptomsPresenter.getVisitContext();
            } else {
                try {
                    thsSymptomsPresenter.getfirstAvailableProvider(thsOnDemandSpeciality);
                } catch (AWSDKInstantiationException e) {

                }
            }
        } else {
            mContinue.setEnabled(true);
            addTopicsToView(THSManager.getInstance().getPthVisitContext());
        }
    }

    public void addTopicsToView(THSVisitContext visitContext) {
        ths_symptoms_relative_layout.setVisibility(View.VISIBLE);
        mThsVisitContext = visitContext;
        Typeface typeface = getTypeface();
        if (getContext() != null) {
            List<Topic> topics = visitContext.getTopics();
            for (final Topic topic : topics) {
                CheckBox checkBox = new CheckBox(getContext());
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                checkBox.setLayoutParams(layoutParams);
                checkBox.setEnabled(true);
                checkBox.setTypeface(typeface);
                checkBox.setText(topic.getTitle());
                if (topic.isSelected()) {
                    checkBox.setChecked(true);
                }
                topicLayout.addView(checkBox);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        topic.setSelected(isChecked);
                        if (isChecked) {
                            NumberOfConditionSelected++;
                        } else {
                            NumberOfConditionSelected--;
                        }
                    }
                });
            }
        }
        mContinue.setEnabled(true);
    }

    public Typeface getTypeface() {
        return Typeface.createFromAsset(getActivity().getAssets(), "fonts/centralesansbook.ttf");
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.continue_btn) {
            //THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA, THS_FLOATING_BUTTON, "symptomContinue");
            thsSymptomsPresenter.onEvent(R.id.continue_btn);
        } else if (i == R.id.camera_click_button) {
            selectImage();
        } else if (i == R.id.cancel_dialog) {
            dialog.dismiss();
        } else if (i == R.id.select_from_gallery) {
            userChosenTask = getString(R.string.ths_intake_symptoms_choose_from_library);
            dialog.dismiss();
            requestWritePermission();
        } else if (i == R.id.camera_image) {
            userChosenTask = getString(R.string.ths_intake_symptoms_take_photo);
            dialog.dismiss();
            requestPermission();
        }
    }

    protected void updateOtherTopic() {
        if (isOtherTopicValid()) {
            mThsVisitContext.setOtherTopic(additional_comments_edittext.getText().toString());

            THSManager.getInstance().setVisitContext(mThsVisitContext);
        }
    }

    protected void addTags() {
        if (NumberOfConditionSelected > 0) {
            tagActions = THSTagUtils.addActions(tagActions, TAG_SYMPTOMS_CHECKED);
        } else {
            tagActions = tagActions.replace(TAG_SYMPTOMS_CHECKED, "");
        }
        if(null!=selectedImagePojoList && selectedImagePojoList.size()>0){
            tagActions = THSTagUtils.addActions(tagActions, "pictureAdded");
        }
        if(!additional_comments_edittext.getText().toString().isEmpty()){
            tagActions = THSTagUtils.addActions(tagActions, "commentAdded");
        }
        if (!(tagActions.isEmpty())) {
            THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA, "step1SymptomsForVisit", tagActions);
            if(tagActions.contains(TAG_SYMPTOMS_CHECKED))
                THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA, THS_SPECIAL_EVENT, "step1SymptomsAdded");
        }
    }

    private boolean isOtherTopicValid() {
        boolean otherTopicEnabled = false;
        try {
            otherTopicEnabled = THSManager.getInstance().getAwsdk(getContext()).getConfiguration().otherTopicEnabled();
        } catch (AWSDKInstantiationException e) {

        }
        if (otherTopicEnabled) {
            return mThsVisitContext != null && additional_comments_edittext != null && !additional_comments_edittext.getText().toString().isEmpty();
        } else {
            return false;
        }
    }

    /**
     * Pick galley photos code below
     */

    private void selectImage() {

        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.ths_choose_image_dialog);
        dialog.setCancelable(false);
        dialog.setTitle(getString(R.string.ths_intake_symptoms_add_photo));
        Button cancelDialogButton = (Button) dialog.findViewById(R.id.cancel_dialog);
        cancelDialogButton.setText(getString(R.string.ths_cancel));
        cancelDialogButton.setOnClickListener(this);
        Button selectFromGalleryButton = (Button) dialog.findViewById(R.id.select_from_gallery);
        selectFromGalleryButton.setOnClickListener(this);
        selectFromGalleryButton.setText(getString(R.string.ths_intake_symptoms_choose_from_library));
        Button selectFromCameraButton = (Button) dialog.findViewById(R.id.camera_image);
        selectFromCameraButton.setOnClickListener(this);
        selectFromCameraButton.setText(getString(R.string.ths_intake_symptoms_take_photo));
        dialog.show();
    }

    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA},
                    REQUEST_READ_EXTERNAL_STORAGE_AN_CAMERA);
        } else {
            chooseUserTask();
        }
    }

    private void chooseUserTask() {
        if (userChosenTask.equals(getString(R.string.ths_intake_symptoms_take_photo))) {
            cameraIntent();
        } else if (userChosenTask.equals(getString(R.string.ths_intake_symptoms_choose_from_library))) {
            galleryIntent();
        }
    }

    private void requestWritePermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            chooseUserTask();
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ComponentName componentName = intent.resolveActivity(getActivity().getPackageManager());
        if (componentName != null) {
            startActivityForResult(intent, RESULT_LOAD_IMAGE);
            THSTagUtils.doTrackPageWithInfo(THS_ANDROID_GALLERY,null,null);
        } else {
            showError(getString(R.string.ths_add_photo_no_app_to_handle));
        }
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
            THSTagUtils.doTrackPageWithInfo(THS_ANDROID_CAMERA,null,null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String picturePath = null;
        switch (requestCode) {
            case RESULT_LOAD_IMAGE:
                if (requestCode == RESULT_LOAD_IMAGE &&
                        resultCode == RESULT_OK && null != data && null != data.getData()) {
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
                            getActivity().getContentResolver().query(mCapturedImageURI, projection, null,
                                    null, null);
                    int column_index_data = cursor.getColumnIndexOrThrow(
                            MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    picturePath = cursor.getString(column_index_data);
                    cursor.close();
                }
                break;
        }

        if (null != picturePath) {
            compressImage(mCapturedImageURI, picturePath);
        }


    }

    private void compressImage(Uri imageUri, String picturePath) {
        InputStream imageStream = null;
        try {
            imageStream = getActivity().getContentResolver().openInputStream(imageUri);
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            Uri compressedImageUri = getImageUri(getActivity(), selectedImage, picturePath);
            String path = getRealPathFromURI(compressedImageUri);
            updateDocumentsToUpload(path, compressedImageUri);
            thsSymptomsPresenter.uploadDocuments(compressedImageUri);

        } catch (FileNotFoundException e) {

        }

    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage, String picturePath) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String fileName = picturePath.substring(picturePath.lastIndexOf("/") + 1, picturePath.lastIndexOf("."));
        String path = null;
        path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, fileName, null);
        return Uri.parse(path);
    }

    public void updateDocumentsToUpload(String picturePath, Uri compressedImageUri) {
        THSSelectedImagePojo image = new THSSelectedImagePojo();
        String filename = null;
        filename = thsFileUtils.getFileName(getContext(), compressedImageUri);
        image.setTitle(filename);
        image.setDatetime(System.currentTimeMillis());
        image.setPath(picturePath);
        image.setIsUploaded(false);
        selectedImagePojoList.add(image);
        thsImageRecyclerViewAdapter.notifyDataSetChanged();
        imageListView.setAdapter(thsImageRecyclerViewAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_EXTERNAL_STORAGE_AN_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    chooseUserTask();
                } else {
                    showError(getString(R.string.ths_intake_symptoms_image_selection_permission_denied));
                }
                break;
            case REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    chooseUserTask();
                } else {
                    showError(getString(R.string.ths_intake_symptoms_image_selection_permission_denied));
                }
        }

    }

    public void updateDocumentRecordList(DocumentRecord documentRecord) {
        if (null != documentRecordList) {
            documentRecordList.add(documentRecord);
        }
        int position = 0;
        for (THSSelectedImagePojo thsSelectedImagePojo : selectedImagePojoList) {
            if (documentRecord.getName().contains(thsSelectedImagePojo.getTitle().substring(0, thsSelectedImagePojo.getTitle().indexOf(".")))) {
                thsSelectedImagePojo.setIsUploaded(true);
                selectedImagePojoList.set(position, thsSelectedImagePojo);
            }
            position++;
        }
        thsImageRecyclerViewAdapter.notifyDataSetChanged();
        imageListView.setAdapter(thsImageRecyclerViewAdapter);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (null != selectedImagePojoList && selectedImagePojoList.size() > 0) {
            Gson gson = new Gson();
            String json = gson.toJson(selectedImagePojoList);

            THSSharedPreferenceUtility.setString(getContext(), THSConstants.THS_SAVE_UPLOAD_IMAGE_KEY, json);
        } else {
            THSSharedPreferenceUtility.setString(getContext(), THSConstants.THS_SAVE_UPLOAD_IMAGE_KEY, null);
        }
        if (null != documentRecordList) {
            THSManager.getInstance().setTHSDocumentList(documentRecordList);
        } else {
            THSManager.getInstance().setTHSDocumentList(null);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        THSSharedPreferenceUtility.setString(getContext(), THSConstants.THS_SAVE_UPLOAD_IMAGE_KEY, null);
        THSManager.getInstance().setTHSDocumentList(null);
    }

    @Override
    public void onImageClicked(int position) {
        thsSelectedImageFragment = new THSSelectedImageFragment();
        thsSelectedImageFragment.setSelectedImage(position, selectedImagePojoList, documentRecordList);
        thsSelectedImageFragment.setSelectedImageFragmentCallback(this);
        thsSelectedImageFragment.show(getActivity().getSupportFragmentManager(), "");
    }

    @Override
    public void dismissSelectedImageFragment(ArrayList<THSSelectedImagePojo> selectedImagePojoList) {
        this.selectedImagePojoList = selectedImagePojoList;
        thsImageRecyclerViewAdapter.notifyDataSetChanged();
        imageListView.setAdapter(thsImageRecyclerViewAdapter);
        thsSelectedImageFragment.dismiss();

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.additional_comments_edittext) {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_UP:
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    break;
            }
        }
        return false;
    }

    protected Provider getProvider() {
        return mProvider;
    }

    @Override
    public void onResume() {
        super.onResume();
        THSTagUtils.doTrackPageWithInfo(THS_SYMPTOMS_PAGE, null, null);

    }

    @Override
    public void setContinueButtonState(boolean enable) {
        thsImageRecyclerViewAdapter.isClickable = enable;
        ths_camera_image_list_layout.setEnabled(enable);
        imageListView.setEnabled(enable);
        camera_button.setEnabled(enable);
        mContinue.setEnabled(enable);
    }


}
