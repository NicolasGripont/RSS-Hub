package com.nico.rsshub.views;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nico.rsshub.R;
import com.nico.rsshub.controllers.Controller;

public class ManageFeedsActivity extends AppCompatActivity {

    private ListView listViewFeeds = null;

    private Toolbar toolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_feeds);
        setTitle(R.string.manage_feeds);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

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

                ActionMode mActionMode = startActionMode(new android.view.ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
//                        toolbar.setVisibility(View.GONE);
                        mode.getMenuInflater().inflate(R.menu.manage_feeds, menu);

                        return true;
                    }

                    @Override
                    public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
                        return false;
                    }

                    @Override
                    public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
                        return false;
                    }

                    @Override
                    public void onDestroyActionMode(android.view.ActionMode mode) {
//                            doneClicked();
//                        toolbar.setVisibility(View.VISIBLE);
                    }
                });


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
