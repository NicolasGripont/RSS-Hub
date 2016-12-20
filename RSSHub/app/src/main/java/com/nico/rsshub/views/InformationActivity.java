package com.nico.rsshub.views;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;

import com.nico.rsshub.R;
import com.nico.rsshub.controllers.Controller;
import com.nico.rsshub.modeles.Information;

import java.io.FileOutputStream;
import java.util.List;

public class InformationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView listViewNews;

    ListView listViewNewsFavorites;

    ListView listViewChrono;

    ListView listViewChronoFavorites;

    TabHost tabHost;

    LinearLayout tabNews;

    LinearLayout tabNewsFavorites;

    LinearLayout tabChrono;

    LinearLayout tabChronoFavorites;


    SubMenu favoritesMenu;

    SubMenu categoriesMenu;

    SubMenu feedsMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        toolbar.setLogo(R.mipmap.logo_rss_hub);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        int nb = navigationView.getHeaderCount();

        Menu m = navigationView.getMenu();
        this.favoritesMenu = m.addSubMenu(R.string.favorites);
        this.favoritesMenu.add("tmp");

        this.categoriesMenu  = m.addSubMenu(R.string.categories);
        this.categoriesMenu.add("tmp");

        this.feedsMenu  = m.addSubMenu(R.string.feeds);
        this.feedsMenu.add("tmp");

        //liste
        this.listViewNews = (ListView) findViewById(R.id.listView_news);
        this.listViewNewsFavorites = (ListView) findViewById(R.id.listView_news_favorites);
        this.listViewChrono = (ListView) findViewById(R.id.listView_chrono);
        this.listViewChronoFavorites = (ListView) findViewById(R.id.listView_chrono_favorites);

        this.tabHost = (TabHost) findViewById(R.id.tabHost);
        this.tabHost.setup();

        //Tabs

        //Tab News
        TabHost.TabSpec specNews = this.tabHost.newTabSpec(getString(R.string.news));
        specNews.setContent(R.id.tab_news);
        specNews.setIndicator(getString(R.string.news));
        this.tabHost.addTab(specNews);

        //Tab News Favorites
        TabHost.TabSpec specNewsFavorites = this.tabHost.newTabSpec(getString(R.string.favorites));
        specNewsFavorites.setContent(R.id.tab_news_favorites);
        specNewsFavorites.setIndicator(getString(R.string.favorites));
        this.tabHost.addTab(specNewsFavorites);

        //Tab Chrono
        TabHost.TabSpec specChrono = this.tabHost.newTabSpec(getString(R.string.chrono));
        specChrono.setContent(R.id.tab_chrono);
        specChrono.setIndicator(getString(R.string.chrono));
        this.tabHost.addTab(specChrono);

        //Tab Chrono Favorites
        TabHost.TabSpec specChronoFavorites = this.tabHost.newTabSpec(getString(R.string.chrono_favorites));
        specChronoFavorites.setContent(R.id.tab_chrono_favorites);
        specChronoFavorites.setIndicator(getString(R.string.chrono_favorites));
        this.tabHost.addTab(specChronoFavorites);

        //TODO : modifier taille titre tab (et modifier le style)
//        LinearLayout.LayoutParams params0 = (LinearLayout.LayoutParams)tabHost.getTabWidget().getChildAt(0).getLayoutParams();
//        params0.width = 500;
//        tabHost.getTabWidget().getChildAt(0).setLayoutParams(params0);
//
//        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams)tabHost.getTabWidget().getChildAt(1).getLayoutParams();
//        params1.width = 500;
//        tabHost.getTabWidget().getChildAt(1).setLayoutParams(params1);
//
//        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams)tabHost.getTabWidget().getChildAt(2).getLayoutParams();
//        params2.width = 500;
//        tabHost.getTabWidget().getChildAt(2).setLayoutParams(params2);
//
//        LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams)tabHost.getTabWidget().getChildAt(3).getLayoutParams();
//        params3.width = 500;
//        tabHost.getTabWidget().getChildAt(3).setLayoutParams(params3);


        Controller.getInstance().setCurrentActivity(this);
        Controller.getInstance().updateInformations();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.informations, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_manage_feeds) {
            Controller.getInstance().onMannageFeedsClicked();
        }
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public void updateInformations(final List<Information> all, final List<Information> favorites) {
        InformationAdapter adapterNews = new InformationAdapter(this, all, true);
        listViewNews.setAdapter(adapterNews);
        listViewNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
                Controller.getInstance().onInformationClick(adapter,position);
            }
        });

        InformationAdapter adapterChrono = new InformationAdapter(this, all, false);
        listViewChrono.setAdapter(adapterChrono);
        listViewChrono.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
                Controller.getInstance().onInformationClick(adapter,position);
            }
        });

        InformationAdapter adapterNewsFavorites = new InformationAdapter(this, favorites, true);
        listViewNewsFavorites.setAdapter(adapterNewsFavorites);
        listViewNewsFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
                Controller.getInstance().onInformationClick(adapter,position);
            }
        });

        InformationAdapter adapterChronoFavorites = new InformationAdapter(this, favorites, false);
        listViewChronoFavorites.setAdapter(adapterChronoFavorites);
        listViewChronoFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
                Controller.getInstance().onInformationClick(adapter,position);
            }
        });

    }

    public void refreshListViews() {
        ((BaseAdapter) listViewNews.getAdapter()).notifyDataSetChanged();
        ((BaseAdapter) listViewChrono.getAdapter()).notifyDataSetChanged();
        ((BaseAdapter) listViewNewsFavorites.getAdapter()).notifyDataSetChanged();
        ((BaseAdapter) listViewChronoFavorites.getAdapter()).notifyDataSetChanged();
    }
}
