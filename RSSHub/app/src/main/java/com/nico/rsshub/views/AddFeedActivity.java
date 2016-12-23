package com.nico.rsshub.views;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

    private Vector<RadioButton> radioButtonsCategories = null;

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
        this.radioButtonsCategories = new Vector<>();
        for(Category category : Category.values()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(category.getValue());
            radioButton.setTextSize(24);
            radioButton.setTextColor(this.feedTitleTextView.getTextColors().getDefaultColor());
            radioButton.setPadding(0,0,0,20);
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RadioButton radioButton = (RadioButton) v;
                    if(radioButton.isChecked()) {
                        for(RadioButton r : radioButtonsCategories) {
                            if(r != radioButton) {
                                r.setChecked(false);
                            }
                        }
                    }

                }
            });
            this.linearLayoutCategories.addView(radioButton);
            this.linearLayoutCategories.setPadding(0,0,0,300);
            this.radioButtonsCategories.add(radioButton);
        }

        findViewById(R.id.linearLayout).requestFocus();

        Controller.getInstance().setCurrentActivity(this);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Controller.getInstance().onBackClicked();
    }

}
