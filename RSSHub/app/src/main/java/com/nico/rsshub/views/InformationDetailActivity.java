package com.nico.rsshub.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import com.nico.rsshub.R;
import com.nico.rsshub.controllers.Controller;
import com.nico.rsshub.modeles.Information;

public class InformationDetailActivity extends AppCompatActivity {

    private WebView webView = null;

    private Information information = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_detail);
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

        this.webView = (WebView) findViewById(R.id.webView);

        this.information = (Information) getIntent().getSerializableExtra("information");

        this.showLoading();

        Controller.getInstance().setCurrentActivity(this);
    }

    public void showLoading() {
        String summary = "<html><body><h1>Chargement...</h1></body></html>";
        this.webView.loadData(summary, "text/html; charset=utf-8", "utf-8");
    }

    public void loadUrl(String url) {
        this.webView.loadUrl(url);
    }

    @Override
    public void finish() {
        super.finish();
        Controller.getInstance().backToInformationActivity();
    }
}
