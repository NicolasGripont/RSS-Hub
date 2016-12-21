package com.nico.rsshub.views;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nico.rsshub.R;
import com.nico.rsshub.controllers.Controller;

import java.util.List;

public class ManageFeedsActivity extends AppCompatActivity {

    private ListView listViewFeeds = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_feeds);
        setTitle(R.string.manage_feeds);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Controller.getInstance().onPlusButtonClicked();
            }
        });


        this.listViewFeeds = (ListView) findViewById(R.id.listView_feeds);

        Controller.getInstance().setCurrentActivity(this);

        final FeedAdapter adapter = new FeedAdapter(this, Controller.getInstance().getFeedsList());
        listViewFeeds.setAdapter(adapter);

        listViewFeeds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
//                Controller.getInstance().onInformationClick(adapter,position);
            }
        });
        listViewFeeds.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(adapter.getFeeds().get(position));
                return false;
            }
        });
    }

    public void refreshListViewFeeds() {
        ((FeedAdapter) listViewFeeds.getAdapter()).notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Controller.getInstance().onBackClicked();
    }

}
