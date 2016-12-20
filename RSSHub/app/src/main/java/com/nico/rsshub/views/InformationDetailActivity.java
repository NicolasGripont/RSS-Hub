package com.nico.rsshub.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
