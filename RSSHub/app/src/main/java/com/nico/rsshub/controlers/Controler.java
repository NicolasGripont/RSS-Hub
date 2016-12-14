package com.nico.rsshub.controlers;

import android.app.Activity;

import com.nico.rsshub.modeles.Information;
import com.nico.rsshub.views.InformationActivity;

import java.util.List;

/**
 * Created by Nico on 14/12/2016.
 */

public class Controler {

    InformationActivity informationActivity;
    Activity currentActivity;

    public Controler(InformationActivity informationActivity) {
        this.informationActivity = informationActivity;
        this.currentActivity = informationActivity;
    }

    public InformationActivity getInformationActivity() {
        return informationActivity;
    }

    public void setInformationActivity(InformationActivity informationActivity) {
        this.informationActivity = informationActivity;
    }

    public void loadInformations(){
        LoadFeedsTask loadFeedsTask = new LoadFeedsTask(this);
        loadFeedsTask.execute("http://www.lequipe.fr/rss/actu_rss.xml");
    }


    public void updateInformations(List<Information> informations) {
      if(this.currentActivity == this.informationActivity && this.informationActivity != null) {
          this.informationActivity.updateInformations(informations);
      }
    }
}
