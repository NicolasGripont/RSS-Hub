package com.nico.rsshub.views;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nico.rsshub.R;
import com.nico.rsshub.controllers.Controller;
import com.nico.rsshub.modeles.Category;
import com.nico.rsshub.modeles.Feed;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class AddFeedActivity extends AppCompatActivity {

    private LinearLayout linearLayoutTags = null;

    private TextView feedSourceTextView = null;

    private TextView feedTitleTextView = null;

    private TextView feedUrlTextView = null;

    private TextView feedTagsTextView = null;

    private EditText feedSourceEditText = null;

    private EditText feedTitleEditText = null;

    private EditText feedUrlEditText = null;

    private EditText feedTagsEditText = null;

    private FloatingActionButton floatingActionButton = null;

    private AlertDialog loadingDialog = null;

    private Vector<CheckBox> checkBoxes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feed);

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

        this.floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        this.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Controller.getInstance().onAddButtonClicked();
            }
        });

        this.feedSourceTextView = (TextView) findViewById(R.id.feed_source_textView);
        this.feedSourceTextView.setText(R.string.source);
        this.feedTitleTextView = (TextView) findViewById(R.id.feed_title_textView);
        this.feedTitleTextView.setText(R.string.title);
        this.feedUrlTextView = (TextView) findViewById(R.id.feed_url_textView);
        this.feedUrlTextView.setText(R.string.url);
        this.feedTagsTextView = (TextView) findViewById(R.id.feed_tags_textView);
        this.feedTagsTextView.setText(R.string.tags);

        this.feedSourceEditText = (EditText) findViewById(R.id.feed_source_editText);
        this.feedTitleEditText = (EditText) findViewById(R.id.feed_title_editText);
        this.feedUrlEditText = (EditText) findViewById(R.id.feed_url_editText);
        this.feedTagsEditText = (EditText) findViewById(R.id.feed_tags_editText);


        this.linearLayoutTags = (LinearLayout) findViewById(R.id.feed_categories_linearLayout);
        this.checkBoxes = new Vector<>();
        for(String tag : Controller.getInstance().getTags()) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(tag);
            checkBox.setTextSize(18);
            checkBox.setTextColor(this.feedTitleTextView.getTextColors().getDefaultColor());
            checkBox.setPadding(0,0,0,20);

            this.linearLayoutTags.addView(checkBox);
            this.linearLayoutTags.setPadding(0,0,0,300);
            this.checkBoxes.add(checkBox);
        }


        findViewById(R.id.linearLayout).requestFocus();

        Controller.getInstance().setCurrentActivity(this);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Controller.getInstance().onBackClicked();
    }

    public String getFeedSource() {
        return this.feedSourceEditText.getText().toString();
    }

    public String getFeedTitle() {
        return this.feedTitleEditText.getText().toString();
    }

    public String getFeedUrl() {
        return this.feedUrlEditText.getText().toString();
    }

    public List<String> getFeedTags() {
        List<String> tags = new ArrayList<>();

        String[] inputTags = this.feedTagsEditText.getText().toString().trim().replaceAll("[^A-Z^a-z^0-9^#]", "").split("#");

        for(String inputTag : inputTags) {
            if (!inputTag.equals("")) {
                String tag = "#" + inputTag.replaceAll("[^A-Z^a-z^0-9]", "");
                if(!tags.contains(tag)) {
                    tags.add(tag);
                }
            }
        }

        for(CheckBox checkBox : checkBoxes) {
            if (checkBox.isChecked()) {
                List<String> tmpTags = new ArrayList<>();
                if (!tags.contains(checkBox.getText().toString())) {
                    tmpTags.add(checkBox.getText().toString());
                }
                tags.addAll(tmpTags);
            }
        }

        return tags;
    }

    public boolean areInputsEdited() {
        if(!getFeedSource().equals("") && !getFeedTitle().equals("") && !getFeedUrl().equals("")) {
            return true;
        }
        return false;
    }

    public void showLoadFeedAlertDialog() {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.findViewById(R.id.linearLayout).getWindowToken(), 0);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddFeedActivity.this);

        alertDialogBuilder.setTitle(R.string.add_feed_please_wait);

        alertDialogBuilder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Controller.getInstance().cancelLoadFeed();
                    }
                }
        );

        this.loadingDialog = alertDialogBuilder.create();

        this.loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                loadingDialog.show();
            }
        });

        this.loadingDialog.show();
    }

    public void showConfirmationAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddFeedActivity.this);

        alertDialogBuilder.setTitle(R.string.add_this_feed_question);

        alertDialogBuilder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Controller.getInstance().cancelLoadFeed();
                    }
                }
        );

        alertDialogBuilder.setPositiveButton(R.string.add,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Controller.getInstance().addNewFeed();
                        dialog.dismiss();
                        onBackPressed();
                    }
                }
        );

        final AlertDialog alertDialog = alertDialogBuilder.create();


        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                alertDialog.show();
            }
        });

        alertDialog.show();
    }

    public void showErrorAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddFeedActivity.this);

        alertDialogBuilder.setTitle(R.string.an_error_occured);

        alertDialogBuilder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }
        );

        final AlertDialog alertDialog = alertDialogBuilder.create();


        alertDialog.show();
    }

    public void dismissLoadingDialog() {
        if(this.loadingDialog != null) {
            this.loadingDialog.dismiss();
            this.loadingDialog = null;
        }
    }


}
