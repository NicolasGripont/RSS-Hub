package com.nico.rsshub.views;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nico.rsshub.R;
import com.nico.rsshub.controllers.Controller;
import com.nico.rsshub.modeles.Category;

import java.util.Vector;

public class AddFeedActivity extends AppCompatActivity {

    private LinearLayout linearLayoutCategories = null;

    private TextView feedTitleTextView = null;

    private TextView feedUrlTextView = null;

    private TextView feedCategoryTextView = null;

    private EditText feedTitleEditText = null;

    private EditText feedUrlEditText = null;

    private Vector<CategoryRadioButton> categoryRadioButtons = null;

    private FloatingActionButton floatingActionButton = null;

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

        this.feedTitleTextView = (TextView) findViewById(R.id.feed_title_textView);
        this.feedTitleTextView.setText(R.string.title);
        this.feedUrlTextView = (TextView) findViewById(R.id.feed_url_textView);
        this.feedUrlTextView.setText(R.string.url);
        this.feedCategoryTextView = (TextView) findViewById(R.id.feed_category_textView);
        this.feedCategoryTextView.setText(R.string.category);
        this.feedTitleEditText = (EditText) findViewById(R.id.feed_title_editText);
        this.feedUrlEditText = (EditText) findViewById(R.id.feed_url_editText);

        this.linearLayoutCategories = (LinearLayout) findViewById(R.id.feed_categories_linearLayout);
        this.categoryRadioButtons = new Vector<>();
        for(Category category : Category.values()) {
            CategoryRadioButton radioButton = new CategoryRadioButton(this,category);
            radioButton.setTextSize(24);
            radioButton.setTextColor(this.feedTitleTextView.getTextColors().getDefaultColor());
            radioButton.setPadding(0,0,0,20);
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RadioButton radioButton = (RadioButton) v;
                    if(radioButton.isChecked()) {
                        for(CategoryRadioButton r : categoryRadioButtons) {
                            if(r != radioButton) {
                                r.setChecked(false);
                            }
                        }
                    }

                }
            });
            this.linearLayoutCategories.addView(radioButton);
            this.linearLayoutCategories.setPadding(0,0,0,300);
            this.categoryRadioButtons.add(radioButton);
        }

        findViewById(R.id.linearLayout).requestFocus();

        Controller.getInstance().setCurrentActivity(this);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Controller.getInstance().onBackClicked();
    }

    public void showAddAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddFeedActivity.this);

        alertDialogBuilder.setTitle(R.string.add_feed_please_wait);

        alertDialogBuilder.setNegativeButton(R.string.cancel,
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

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

    public String getFeedTitle() {
        return this.feedTitleEditText.getText().toString();
    }

    public String getFeedUrl() {
        return this.feedUrlEditText.getText().toString();
    }

    public Category getFeedCategory() {
        Category category = null;
        for(CategoryRadioButton radioButton : categoryRadioButtons) {
            if(radioButton.isChecked()) {
                category = radioButton.getCategory();
            }
        }
        return category;
    }

    public boolean areInputsEdited() {
        if(!getFeedTitle().equals("") && !getFeedUrl().equals("") && getFeedCategory() != null) {
            return true;
        }
        return false;
    }
}
