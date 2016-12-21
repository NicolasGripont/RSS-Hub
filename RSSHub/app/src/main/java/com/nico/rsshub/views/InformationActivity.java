package com.nico.rsshub.views;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;

import com.nico.rsshub.R;
import com.nico.rsshub.controllers.Controller;
import com.nico.rsshub.modeles.Information;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Vector;

public class InformationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SubMenu favoritesMenu;

    private SubMenu categoriesMenu;

    private SubMenu feedsMenu;

    private Context mContext;

    private ListView listViewNews;

    private ListView listViewNewsFavorites;

    private ListView listViewChrono;

    private ListView listViewChronoFavorites;

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
        mContext = this;
        this.listViewNews = new ListView(mContext);
        this.listViewNewsFavorites = new ListView(mContext);
        this.listViewChrono = new ListView(mContext);
        this.listViewChronoFavorites = new ListView(mContext);

        Vector<View> pages = new Vector<>();
        Vector<String> titles = new Vector<>();
        pages.add(listViewNews);
        titles.add(getString(R.string.news));
        pages.add(listViewNewsFavorites);
        titles.add(getString(R.string.favorites));
        pages.add(listViewChrono);
        titles.add(getString(R.string.chrono));
        pages.add(listViewChronoFavorites);
        titles.add(getString(R.string.chrono_favorites));

        ViewPager vp = (ViewPager) findViewById(R.id.viewpager);
        CustomPagerAdapter adapter = new CustomPagerAdapter(mContext,pages,titles);
        vp.setAdapter(adapter);
        vp.setOffscreenPageLimit(4);

        String[] prenoms = new String[]{
                "Antoine", "Benoit", "Cyril", "David", "Eloise", "Florent",
                "Gerard", "Hugo", "Ingrid", "Jonathan", "Kevin", "Logan",
                "Mathieu", "Noemie", "Olivia", "Philippe", "Quentin", "Romain",
                "Sophie", "Tristan", "Ulric", "Vincent", "Willy", "Xavier",
                "Yann", "Zoé"
        };

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_manage_feeds) {
            Controller.getInstance().onMannageFeedsClicked();
        }

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
