package com.nico.rsshub.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nico.rsshub.R;
import com.nico.rsshub.modeles.Information;
import com.nico.rsshub.controlers.LoadFeedsTask;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final int READ_BLOCK_SIZE = 100;

    private ListView listView = null;

    //TODO : ask storage permissions
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);

        LoadFeedsTask loadFeedsTask = new LoadFeedsTask(this);
        loadFeedsTask.execute("http://www.lequipe.fr/rss/actu_rss.xml");
    }


    public void updateInformations(List<Information> informations) {
        String[] titles;
        if(informations != null) {
            titles = new String[informations.size()];
            for(int i = 0; i < informations.size(); i++) {
                titles[i] = informations.get(i).getTitle();
            }
        } else {
            titles = new String[]{"Error !"};
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, titles);
        listView.setAdapter(adapter);
    }
}
