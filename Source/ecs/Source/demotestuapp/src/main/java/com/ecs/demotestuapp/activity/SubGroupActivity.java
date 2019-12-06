
package com.ecs.demotestuapp.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.ecs.demotestuapp.R;
import com.ecs.demotestuapp.adapter.SubGroupAdapter;
import com.ecs.demotestuapp.jsonmodel.GroupItem;
import com.philips.cdp.di.ecs.ECSServices;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;
import com.philips.platform.uid.view.widget.Button;


public class SubGroupActivity extends AppCompatActivity  {

    private Button mRegister;

    private UserDataInterface mUserDataInterface;


    URInterface urInterface;
    private long mLastClickTime = 0;

    private ECSServices ecsServices;

    ExpandableListView expandableListView;

    EditText etPropositionID;
    private GroupItem groupItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.sub_group_layout);

        Bundle bundle = getIntent().getExtras();
        groupItem = (GroupItem) bundle.getSerializable("group");


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SubGroupAdapter(groupItem.getSubgroup(), this));

    }





}
