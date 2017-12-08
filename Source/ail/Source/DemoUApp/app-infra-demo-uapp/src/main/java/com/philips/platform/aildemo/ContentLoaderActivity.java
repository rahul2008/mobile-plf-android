/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.aildemo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.philips.platform.appinfra.contentloader.ContentInterface;
import com.philips.platform.appinfra.contentloader.ContentLoader;
import com.philips.platform.appinfra.contentloader.ContentLoaderInterface;
import com.philips.platform.appinfra.contentloader.model.ContentArticle;
import com.philips.platform.appinfra.demo.R;

import java.util.List;

/**
 * Created by 310238114 on 11/16/2016.
 */
public class ContentLoaderActivity extends AppCompatActivity {
    Spinner APIspinner;
    ContentLoader mContentLoader;
    Button buttonTriggerApi;
    TextView input;
    TextView textViewResponse;
    ListView listView;
    private final String[] APIlist = {"Check Status","Refresh", "Get All Content", "Get Content by ID", "Get Content by IDs", "Get Content by TAG", "Get Content by TAGs - OR", "Get Content by TAGs - AND", "Delete All"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        int contentloaderIndex;
        if (extras != null) {
            contentloaderIndex = extras.getInt("ContentLoaderIndex");
            mContentLoader = ContentLoaderCreateActivity.ContentLoaderList.get(contentloaderIndex);
        }

        setContentView(R.layout.content_loader_detail);
        listView = (ListView) findViewById(R.id.listView);

        APIspinner = (Spinner) findViewById(R.id.spinnerAPIs);
        ArrayAdapter<String> input_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, APIlist);
        APIspinner.setAdapter(input_adapter);

        textViewResponse = (TextView) findViewById(R.id.textViewResponseAPI);
        input = (TextView) findViewById(R.id.editTextContentOrTagIds);
        buttonTriggerApi = (Button) findViewById(R.id.buttoncallAPI);

        buttonTriggerApi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (APIspinner.getSelectedItem().toString().trim()) {
                    case "Check Status":
                        textViewResponse.setText(null);
                        listView.setAdapter(null);
                        textViewResponse.setText(mContentLoader.getStatus().toString());
                        break;
                    case "Refresh":
                        textViewResponse.setText(null);
                        listView.setAdapter(null);
                        mContentLoader.refresh(new ContentLoaderInterface.OnRefreshListener() {
                            @Override
                            public void onError(ContentLoaderInterface.ERROR error, String message) {
                                Log.i("CL onError:", "" + error);
                                textViewResponse.setText(error.toString() + "\n" + message);
                            }

                            @Override
                            public void onSuccess(REFRESH_RESULT result) {
                                Log.i("CL onSuccess:", "" + result);
                                textViewResponse.setText(result.toString() + "\nPlease see response data with tag 'CL REFRSH RESP' in console log");
                            }
                        });
                        break;
                    case "Get All Content":
                        textViewResponse.setText(null);
                        listView.setAdapter(null);
                        mContentLoader.getAllContent(new ContentLoaderInterface.OnResultListener<String>() {
                            @Override
                            public void onError(ContentLoaderInterface.ERROR error, String message) {
                                textViewResponse.setText(error.toString() + "\n" + message);
                            }

                            @Override
                            public void onSuccess(List<String> contents) {
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ContentLoaderActivity.this,
                                        android.R.layout.simple_list_item_1, contents);
                                listView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                listView.setOnItemClickListener(null);
                                // textViewResponse.setText(contents.toString());
                            }
                        });
                        break;
                    case "Get Content by ID":
                        textViewResponse.setText(null);
                        listView.setAdapter(null);
                        String[] ids = new String[1];
                        ids[0] = input.getText().toString().trim();
                        mContentLoader.getContentById(ids, new ContentLoaderInterface.OnResultListener() {
                            @Override
                            public void onError(ContentLoaderInterface.ERROR error, String message) {
                                textViewResponse.setText(error.toString() + "\n" + message);
                            }

                            @Override
                            public void onSuccess(List contents) {
                                List<ContentInterface> contentList = contents;
                                ContentListAdapter adapter = new ContentListAdapter(ContentLoaderActivity.this, contentList);
                                listView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                listView.setOnItemClickListener(null);
                            }
                        });
                        break;
                    case "Get Content by IDs":
                        listView.setAdapter(null);
                        textViewResponse.setText(null);
                        String IDsString = input.getText().toString().trim();
                        String[] iDs = IDsString.split(",");
                        String[] iDsTrimmed = new String[iDs.length];
                        for (int i = 0; i < iDs.length; i++) {
                            iDsTrimmed[i] = iDs[i].trim();
                        }
                        mContentLoader.getContentById(iDsTrimmed, new ContentLoaderInterface.OnResultListener() {
                            @Override
                            public void onError(ContentLoaderInterface.ERROR error, String message) {
                                textViewResponse.setText(error.toString() + "\n" + message);
                            }

                            @Override
                            public void onSuccess(List contents) {

                                List<ContentInterface> contentArticle = contents;
                                ContentListAdapter adapter = new ContentListAdapter(ContentLoaderActivity.this, contentArticle);
                                listView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                listView.setOnItemClickListener(null);
                            }
                        });
                        break;
                    case "Get Content by TAG":
                        listView.setAdapter(null);
                        textViewResponse.setText(null);
                        String tagString = input.getText().toString().trim();
                        mContentLoader.getContentByTag(tagString, new ContentLoaderInterface.OnResultListener() {
                            @Override
                            public void onError(ContentLoaderInterface.ERROR error, String message) {
                                textViewResponse.setText(error.toString() + "\n" + message);
                            }

                            @Override
                            public void onSuccess(List contents) {
                                final List<ContentInterface> contentArticle = contents;
                                ContentListAdapter adapter = new ContentListAdapter(ContentLoaderActivity.this, contentArticle);
                                listView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                                        ContentInterface details = contentArticle.get(position);
                                        List<String> tag = details.getTags();
                                        showAlertDialog("ID" + " " + details.getId(), "Tagname: " + tag.toString() + "\r\n");

                                     //   for (Tag t : tag) {

                                           // showAlertDialog("ID" + " " + details.getId(), "Tagname: " + t.name + "\r\n" + " " + "TagId: " + t.getId());
                                      //  }

                                    }
                                });
                            }
                        });
                        break;
                    case "Get Content by TAGs - OR":
                        listView.setAdapter(null);
                        textViewResponse.setText(null);
                        String tagsStringOr = input.getText().toString().trim();
                        String[] TagsOr = tagsStringOr.split(",");
                        String[] TagsORTrimmed = new String[TagsOr.length];
                        for (int i = 0; i < TagsOr.length; i++) {
                            TagsORTrimmed[i] = TagsOr[i].trim();
                        }

                        mContentLoader.getContentByTag(TagsORTrimmed, ContentLoaderInterface.OPERATOR.OR, new ContentLoaderInterface.OnResultListener() {
                            @Override
                            public void onError(ContentLoaderInterface.ERROR error, String message) {
                                textViewResponse.setText(error.toString() + "\n" + message);
                            }

                            @Override
                            public void onSuccess(List contents) {
                                final List<ContentInterface> contentArticle = contents;
                               /* textViewResponse.setVisibility(View.GONE);
                                listView.setVisibility(View.VISIBLE);*/
                                //for(ContentArticle content : contentArticle) {
                                ContentListAdapter adapter = new ContentListAdapter(ContentLoaderActivity.this, contentArticle);
//                                    ArrayAdapter<ContentArticle> itemsAdapter =
//                                            new ArrayAdapter<ContentArticle>(this, android.R.layout.simple_list_item_1, content);
                                //  }
                                listView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                        ContentInterface details = contentArticle.get(position);
                                        List<String> tag = details.getTags();
                                        showAlertDialog("ID" + " " + details.getId(), "Tagname: " + tag.toString() + "\r\n");

//                                        for (Tag t : tag) {
//                                            showAlertDialog("ID" + " " + details.getId(), "Tagname: " + t.name + "\r\n" + " " + "TagId: " + t.getId());
//                                        }
                                    }
                                });
                            }
                        });
                        break;
                    case "Get Content by TAGs - AND":
                        textViewResponse.setText(null);
                        listView.setAdapter(null);
                        String tagStringAnd = input.getText().toString().trim();
                        String[] TagsAnd = tagStringAnd.split(",");
                        String[] TagsAndTrimmed = new String[TagsAnd.length];
                        for (int i = 0; i < TagsAnd.length; i++) {
                            TagsAndTrimmed[i] = TagsAnd[i].trim();
                        }
                        mContentLoader.getContentByTag(TagsAndTrimmed, ContentLoaderInterface.OPERATOR.AND, new ContentLoaderInterface.OnResultListener() {
                            @Override
                            public void onError(ContentLoaderInterface.ERROR error, String message) {
                                textViewResponse.setText(error.toString() + "\n" + message);
                            }

                            @Override
                            public void onSuccess(List contents) {
                                final List<ContentInterface> contentArticle = contents;
                                //for(ContentArticle content : contentArticle) {
                                ContentListAdapter adapter = new ContentListAdapter(ContentLoaderActivity.this, contentArticle);
//                                    ArrayAdapter<ContentArticle> itemsAdapter =
//                                            new ArrayAdapter<ContentArticle>(this, android.R.layout.simple_list_item_1, content);
                                //  }
                                listView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                                        ContentInterface details = contentArticle.get(position);
                                        List<String> tag = details.getTags();
                                        showAlertDialog("ID" + " " + details.getId(), "Tagname: " + tag.toString() + "\r\n");

//                                        for (Tag t : tag) {
//                                            showAlertDialog("ID" + " " + details.getId(), "Tagname: " + t.name + "\r\n" + " " + "TagId: " + t.getId());
//                                        }
                                    }
                                });
                            }
                        });
                        break;

                    case "Delete All":
                        textViewResponse.setText(null);
                        listView.setAdapter(null);
                        mContentLoader.clearCache();
                        textViewResponse.setText("Deleted Successfully");
                        break;

                }

            }
        });


    }

    private String showContents(List contents) {
        String result = "";
        if (null != contents && contents.size() > 0) {

            for (int contentCount = 0; contentCount < contents.size(); contentCount++) {
                if (contents.get(contentCount) instanceof ContentArticle) {
                    ContentArticle ca = ((ContentArticle) contents.get(contentCount));
                    result += "\n\n[ID: " + ca.getId() + "] [Version: " + ca.getVersion() + "] [Tag(s): " + getTagsString(ca.getTags()) + "]";
                }
            }
        }
        return result;
    }

    private String getTagsString(List<String> tagList) {
        String tags = "";
        if (null != tagList && tagList.size() > 0) {
            for (String tagId : tagList) {
                tags += tagId+ ",";
            }
            tags = tags.substring(0, tags.length() - 1);// remove last comma
        }
        return tags;
    }

    void showAlertDialog(String title, String msg) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ContentLoaderActivity.this);
        builder1.setTitle(title);
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        //builder1.setNegativeButton(null);

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}
