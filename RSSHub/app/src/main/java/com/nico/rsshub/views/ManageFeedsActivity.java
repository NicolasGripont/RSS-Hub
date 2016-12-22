package com.nico.rsshub.views;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nico.rsshub.R;
import com.nico.rsshub.controllers.Controller;

public class ManageFeedsActivity extends AppCompatActivity {

    private ListView listViewFeeds = null;

    private Toolbar toolbar = null;

    private FeedAdapter feedAdapter;

    private ActionMode actionMode;

    private MenuItem deletingItem;

    private MenuItem modifyingItem;

    private MenuItem selectAllItem;

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
        this.listViewFeeds.setDividerHeight(30);


        Controller.getInstance().setCurrentActivity(this);

        this.feedAdapter = new FeedAdapter(this, Controller.getInstance().getFeedsList(),Controller.getInstance().getSelectedFeeds());
        listViewFeeds.setAdapter(feedAdapter);

        listViewFeeds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
//                Controller.getInstance().onInformationClick(adapter,position);
            }
        });
        listViewFeeds.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Controller.getInstance().setManageFeedsMode(true);
//                Controller.getInstance().selectFeed(position);
                return false;
            }
        });

        listViewFeeds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Controller.getInstance().selectFeed(position);
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



    public void showActionMode() {
        this.actionMode = startActionMode(new android.view.ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.manage_feeds, menu);
                deletingItem = menu.findItem(R.id.action_delete);
                modifyingItem = menu.findItem(R.id.action_modify);
                selectAllItem = menu.findItem(R.id.action_select_all);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {

                return false;
            }

            @Override
            public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_delete:
                        Controller.getInstance().onDeleteButtonClicked();
                        return true;
                    case R.id.action_modify:

                        return true;
                    case R.id.action_select_all:
                        Controller.getInstance().selectAllFeeds();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(android.view.ActionMode mode) {
                Controller.getInstance().setManageFeedsMode(false);
            }

        });
    }

    public void setManageFeedsMode(boolean isManageFeedsMode) {
        this.feedAdapter.setManageFeedsMode(isManageFeedsMode);
    }

    public void updateListView() {
        this.feedAdapter.notifyDataSetChanged();
    }

    public void setModifyingItemVisibility(boolean visible) {
        this.modifyingItem.setVisible(visible);
    }

    public void setDeletingItemVisibility(boolean visible) {
        this.deletingItem.setVisible(visible);
    }

    public void setSelectAllItemItemVisibility(boolean visible) {
        this.selectAllItem.setVisible(visible);
    }

    public void setSelectAllItemIcon(boolean isAllSelected) {
        if(isAllSelected) {
            this.selectAllItem.setIcon(R.mipmap.uncheck);
        } else {
            this.selectAllItem.setIcon(R.mipmap.check);
        }
    }

    public void finishActionMode(){
        this.actionMode.finish();
    }

    public void showDeleteAlertDialog(int nbFeeds) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ManageFeedsActivity.this);
        alertDialogBuilder.setTitle("Suppression");
        if(nbFeeds == 1) {
            alertDialogBuilder.setMessage("Supprimer ce feed ?");
        } else {
            alertDialogBuilder.setMessage("Supprimer ces feeds ?");
        }
        alertDialogBuilder.setPositiveButton("Annuler",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alertDialogBuilder.setNegativeButton("Supprimer",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Controller.getInstance().deleteSelectedFeeds();
                    }
                });
        alertDialogBuilder.show();
    }
}
