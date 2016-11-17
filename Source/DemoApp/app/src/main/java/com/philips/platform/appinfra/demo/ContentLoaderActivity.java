package com.philips.platform.appinfra.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.philips.platform.appinfra.contentloader.ContentLoader;
import com.philips.platform.appinfra.contentloader.ContentLoaderInterface;

import java.util.List;

/**
 * Created by 310238114 on 11/16/2016.
 */
public class ContentLoaderActivity  extends AppCompatActivity {
    Spinner APIspinner;
    ContentLoader mContentLoader;
    Button buttonTriggerApi;
    TextView input;
    TextView textViewResponse;
    private final String[] APIlist ={"Refresh","Get All Content","Get Content by ID","Get Content by IDs","Get Content by TAG","Get Content by TAGs - OR","Get Content by TAGs - AND"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        int contentloaderIndex;
        if (extras != null) {
            contentloaderIndex = extras.getInt("ContentLoaderIndex");
            mContentLoader=ContentLoaderCreateActivity.ContentLoaderList.get(contentloaderIndex);
        }

        setContentView(R.layout.content_loader_detail);

        APIspinner =(Spinner)findViewById(R.id.spinnerAPIs);
        ArrayAdapter<String> input_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, APIlist);
        APIspinner.setAdapter(input_adapter);

        textViewResponse= (TextView)findViewById(R.id.textViewResponseAPI);
        input=(TextView)findViewById(R.id.editTextContentOrTagIds);
        buttonTriggerApi =  (Button)findViewById(R.id.buttoncallAPI);

        buttonTriggerApi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            switch(APIspinner.getSelectedItem().toString().trim()){
                case "Refresh":
                    textViewResponse.setText(null);
                    mContentLoader.refresh(new ContentLoaderInterface.OnRefreshListener() {
                        @Override
                        public void onError(ContentLoaderInterface.ERROR error, String message) {
                            Log.i("CL onError:", "" + error);
                            textViewResponse.setText(error.toString()+"\n"+message);
                        }
                        @Override
                        public void onSuccess(ContentLoaderInterface.OnRefreshListener.REFRESH_RESULT result) {
                            Log.i("CL onSuccess:", "" + result);
                            textViewResponse.setText(result.toString() +"\nPlease see response data with tag 'CL REFRSH RESP' in console log");
                        }
                    });
                    break;
                case "Get All Content":
                    textViewResponse.setText(null);
                    mContentLoader.getAllContent(new ContentLoaderInterface.OnResultListener<String>() {
                        @Override
                        public void onError(ContentLoaderInterface.ERROR error, String message) {
                            textViewResponse.setText(error.toString()+"\n"+message);
                        }
                        @Override
                        public void onSuccess(List<String> contents) {
                            textViewResponse.setText(contents.toString());
                        }
                    });
                    break;
                case "Get Content by ID":
                    textViewResponse.setText(null);
                    String[] ids= new  String[1];
                    ids[0]=input.getText().toString().trim();
                    mContentLoader.getContentById(ids, new ContentLoaderInterface.OnResultListener() {
                        @Override
                        public void onError(ContentLoaderInterface.ERROR error, String message) {
                            textViewResponse.setText(error.toString()+"\n"+message);
                        }
                        @Override
                        public void onSuccess(List contents) {
                            textViewResponse.setText(contents.toString());
                        }
                    });
                    break;
                case "Get Content by IDs":
                    textViewResponse.setText(null);
                    String IDsString=input.getText().toString().trim();
                    String[] iDs= IDsString.split(",");
                    String[] iDsTrimmed = new String[iDs.length];
                    for(int i=0;i<iDs.length;i++){
                        iDsTrimmed[i]=iDs[i].trim();
                    }
                    mContentLoader.getContentById(iDsTrimmed, new ContentLoaderInterface.OnResultListener() {
                        @Override
                        public void onError(ContentLoaderInterface.ERROR error, String message) {
                            textViewResponse.setText(error.toString()+"\n"+message);
                        }
                        @Override
                        public void onSuccess(List contents) {
                            textViewResponse.setText(contents.toString());
                        }
                    });
                    break;
                case "Get Content by TAG":
                    textViewResponse.setText(null);
                    String tagString=input.getText().toString().trim();
                    mContentLoader.getContentByTag(tagString, new ContentLoaderInterface.OnResultListener() {
                        @Override
                        public void onError(ContentLoaderInterface.ERROR error, String message) {
                            textViewResponse.setText(error.toString()+"\n"+message);
                        }
                        @Override
                        public void onSuccess(List contents) {
                            textViewResponse.setText(contents.toString());
                        }
                    });
                    break;
                case "Get Content by TAGs - OR":
                    textViewResponse.setText(null);
                    String tagsStringOr=input.getText().toString().trim();
                    String[] TagsOr= tagsStringOr.split(",");
                    String[] TagsORTrimmed = new String[TagsOr.length];
                    for(int i=0;i<TagsOr.length;i++){
                        TagsORTrimmed[i]=TagsOr[i].trim();
                    }

                    mContentLoader.getContentByTag(TagsORTrimmed,ContentLoaderInterface.OPERATOR.OR,new ContentLoaderInterface.OnResultListener() {
                        @Override
                        public void onError(ContentLoaderInterface.ERROR error, String message) {
                            textViewResponse.setText(error.toString()+"\n"+message);
                        }
                        @Override
                        public void onSuccess(List contents) {
                            textViewResponse.setText(contents.toString());
                        }
                    });
                    break;
                case "Get Content by TAGs - AND":
                    textViewResponse.setText(null);
                    String tagStringAnd=input.getText().toString().trim();
                    String[] TagsAnd= tagStringAnd.split(",");
                    String[] TagsAndTrimmed = new String[TagsAnd.length];
                    for(int i=0;i<TagsAnd.length;i++){
                        TagsAndTrimmed[i]=TagsAnd[i].trim();
                    }
                    mContentLoader.getContentByTag(TagsAndTrimmed,ContentLoaderInterface.OPERATOR.AND,new ContentLoaderInterface.OnResultListener() {
                        @Override
                        public void onError(ContentLoaderInterface.ERROR error, String message) {
                            textViewResponse.setText(error.toString()+"\n"+message);
                        }
                        @Override
                        public void onSuccess(List contents) {
                            textViewResponse.setText(contents.toString());
                        }
                    });
                    break;

            }

            }
        });


    }
}
