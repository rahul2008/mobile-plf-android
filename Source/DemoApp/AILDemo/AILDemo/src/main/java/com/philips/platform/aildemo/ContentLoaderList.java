package com.philips.platform.aildemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.philips.platform.appinfra.demo.R;

/**
 * Created by 310238114 on 11/16/2016.
 */
public class ContentLoaderList extends AppCompatActivity {
    ListView contentLoaderListView;
    ContentLoaderListAdapter mContentLoaderListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_loader_list);
        contentLoaderListView = (ListView) findViewById(R.id.listViewContentLoader);
        mContentLoaderListAdapter = new ContentLoaderListAdapter(ContentLoaderList.this, ContentLoaderCreateActivity.ContentLoaderList);
            contentLoaderListView.setAdapter(mContentLoaderListAdapter);
        contentLoaderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //here you can use the position to determine what checkbox to check
                //this assumes that you have an array of your checkboxes as well. called checkbox
               Intent i = new Intent(ContentLoaderList.this,ContentLoaderActivity.class);
                i.putExtra("ContentLoaderIndex",position);
                startActivity(i);
            }
        });

    }
}
