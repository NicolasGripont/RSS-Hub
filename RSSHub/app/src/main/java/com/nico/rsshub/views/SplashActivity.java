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
//        saveFeeds();
//        loadFeeds();
    }


//    private void saveFeeds(){
//        FileOutputStream output = null;
//        try {
//            System.out.println(getCacheDir().getAbsolutePath()+"/feed.txt");
//            File file = new File(getCacheDir().getAbsolutePath()+"/feed.txt");
//            file.getParentFile().mkdir();
//            file.createNewFile();
//            output = new FileOutputStream(getCacheDir().getAbsolutePath()+"/feed.txt", false); // true will be same as Context.MODE_APPEND
//            String data = "Je suis bien enregistré";
//            output.write(data.getBytes());
//            if(output != null)
//                output.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void loadFeeds(){
//        String data = "Je suis bien enregistré";
//        File file = new File(getCacheDir().getAbsolutePath()+"/feed.txt");
//        if(file.exists()){
//            System.out.println("---------existe");
//        } else {
//            System.out.println("------existe pas");
//        }
//    }
}
