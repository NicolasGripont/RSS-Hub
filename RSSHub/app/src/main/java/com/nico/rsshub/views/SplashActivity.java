package com.nico.rsshub.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nico.rsshub.controllers.Controller;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Controller.getInstance().setCurrentActivity(this);
        if(Controller.getInstance().getInformationList().size() == 0) {
            Controller.getInstance().loadInformations();
        } else {
            Controller.getInstance().showInformationActivity();
        }
    }


}
