package com.nico.rsshub.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.nico.rsshub.R;
import com.nico.rsshub.controllers.Controller;
import com.nico.rsshub.modeles.Feed;
import com.nico.rsshub.modeles.Information;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class InformationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SubMenu favoritesMenu;

    private SubMenu tagsMenu;

    private SubMenu feedsMenu;

    private Context mContext;

    private ViewPager viewPager;

    private ListView listViewNews;

    private ListView listViewNewsFavorites;

    private ListView listViewChrono;

    private ListView listViewChronoFavorites;

    private Button newsButton;

    private Button newsFavoritesButton;

    private Button chronoButton;

    private Button chronoFavoritesButton;

    private NavigationView navigationView;

    private Vector<View> pages;

    private Vector<String> titles;

    private LinearLayout buttonsLayout;

    private Vector<Button> buttons;


    @TargetApi(Build.VERSION_CODES.M)
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

        this.navigationView = (NavigationView) findViewById(R.id.nav_view);
        this.navigationView.setNavigationItemSelectedListener(this);
        Menu m = this.navigationView.getMenu();
        this.favoritesMenu = m.addSubMenu(R.string.favorites);
        this.tagsMenu  = m.addSubMenu(R.string.tags);
        this.feedsMenu  = m.addSubMenu(R.string.feeds);

        //liste
        mContext = this;
        this.listViewNews = new ListView(mContext);
        this.listViewNewsFavorites = new ListView(mContext);
        this.listViewChrono = new ListView(mContext);
        this.listViewChronoFavorites = new ListView(mContext);
        this.listViewNews.setDividerHeight(30);
        this.listViewNews.setDividerHeight(30);
        this.listViewNewsFavorites.setDividerHeight(30);
        this.listViewChrono.setDividerHeight(30);
        this.listViewChronoFavorites.setDividerHeight(30);

        this.buttonsLayout = (LinearLayout) findViewById(R.id.buttonsLayout);

        this.pages = new Vector<>();
        this.titles = new Vector<>();
        this.buttons = new Vector<>();
        pages.add(listViewNews);
        titles.add(getString(R.string.news));
        newsButton = (Button)findViewById(R.id.news_button);
        newsButton.setText(getString(R.string.news));
        this.initButtonClickListener(newsButton,listViewNews);
        buttons.add(newsButton);
        newsButton.setTextColor(getColor(R.color.orange));


        pages.add(listViewNewsFavorites);
        titles.add(getString(R.string.favorites));
        newsFavoritesButton = (Button)findViewById(R.id.news_favorites_button);
        newsFavoritesButton.setText(getString(R.string.favorites));
        this.initButtonClickListener(newsFavoritesButton,listViewNewsFavorites);
        buttons.add(newsFavoritesButton);

        pages.add(listViewChrono);
        titles.add(getString(R.string.chrono));
        chronoButton = (Button)findViewById(R.id.chrono_button);
        chronoButton.setText(getString(R.string.chrono));
        this.initButtonClickListener(chronoButton,listViewChrono);
        buttons.add(chronoButton);

        pages.add(listViewChronoFavorites);
        titles.add(getString(R.string.chrono_favorites));
        chronoFavoritesButton = (Button)findViewById(R.id.chrono_favorites_button);
        chronoFavoritesButton.setText(getString(R.string.chrono_favorites));
        this.initButtonClickListener(chronoFavoritesButton,listViewChronoFavorites);
        buttons.add(chronoFavoritesButton);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        CustomPagerAdapter adapter = new CustomPagerAdapter(mContext,pages,titles);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(pages.size());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onPageSelected(int position) {
                for(Button button :buttons) {
                    button.setTextColor(getColor(R.color.white));
                }
                View page = viewPager.getChildAt(position);
                int index = 0;
                for(int i = 0; i < pages.size(); i++){
                    if(page.equals(pages.get(i))) {
                        index = i;
                        break;
                    }
                }
                buttons.get(index).setTextColor(getColor(R.color.orange));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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
        } else if(id == R.id.nav_home) {
            Controller.getInstance().updateInformations();
            this.setTitle(R.string.app_name);
        } else {
            for(int i = 0; i < feedsMenu.size(); i++) {
                if(feedsMenu.getItem(i).toString().equals(item.toString())) {
                    Controller.getInstance().onNavigationFeedItemSelected(item);
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                }
            }
            for(int i = 0; i < tagsMenu.size(); i++) {
                if(tagsMenu.getItem(i).toString().equals(item.toString())) {
                    Controller.getInstance().onNavigationTagsItemSelected(item);
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                }
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateInformations(final List<Information> all, final List<Information> favorites) {
        Vector<View> pages = new Vector<>();
        Vector<String> titles = new Vector<>();
        this.buttonsLayout.removeAllViews();
        if(favorites == null || favorites.isEmpty()) {
            pages.add(this.pages.get(0));
            pages.add(this.pages.get(2));
            titles.add(this.titles.get(0));
            titles.add(this.titles.get(2));
            this.buttonsLayout.addView(newsButton);
            this.buttonsLayout.addView(chronoButton);
        } else {
            pages.addAll(this.pages);
            titles.addAll(this.titles);
            this.buttonsLayout.addView(newsButton);
            this.buttonsLayout.addView(newsFavoritesButton);
            this.buttonsLayout.addView(chronoButton);
            this.buttonsLayout.addView(chronoFavoritesButton);
        }
        CustomPagerAdapter adapter = new CustomPagerAdapter(mContext,pages,titles);
        viewPager.setAdapter(adapter);

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

        if(favorites != null) {
            InformationAdapter adapterNewsFavorites = new InformationAdapter(this, favorites, true);
            listViewNewsFavorites.setAdapter(adapterNewsFavorites);
            listViewNewsFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
                    Controller.getInstance().onInformationClick(adapter, position);
                }
            });

            InformationAdapter adapterChronoFavorites = new InformationAdapter(this, favorites, false);
            listViewChronoFavorites.setAdapter(adapterChronoFavorites);
            listViewChronoFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
                    Controller.getInstance().onInformationClick(adapter, position);
                }
            });
        }
    }

    public void refreshListViews() {
        ((BaseAdapter) listViewNews.getAdapter()).notifyDataSetChanged();
        ((BaseAdapter) listViewChrono.getAdapter()).notifyDataSetChanged();
        ((BaseAdapter) listViewNewsFavorites.getAdapter()).notifyDataSetChanged();
        ((BaseAdapter) listViewChronoFavorites.getAdapter()).notifyDataSetChanged();
    }

    public void initButtonClickListener(Button button, final View page) {
        button.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                newsButton.setTextColor(getColor(R.color.white));
                newsFavoritesButton.setTextColor(getColor(R.color.white));
                chronoButton.setTextColor(getColor(R.color.white));
                chronoFavoritesButton.setTextColor(getColor(R.color.white));
                button.setTextColor(getColor(R.color.orange));
                int index = 0;
                for(int i = 0; i < pages.size(); i++) {
                    if(page == pages.get(i)) {
                        index = i;
                    }
                }
                viewPager.setCurrentItem(index, true);
            }
        });
    }

    public void refreshNavigationMenu(List<Feed> feeds) {
        this.favoritesMenu.clear();
        this.tagsMenu.clear();
        this.feedsMenu.clear();

        List<String> tags = new ArrayList<>();
        for(Feed feed : feeds) {
            if(feed.isFavorite()) {
                this.favoritesMenu.add(feed.getSource() + " - " + feed.getTitle());
            }
            this.feedsMenu.add(feed.getSource() + " - " + feed.getTitle());
            List<String> tmpTags = new ArrayList<>();
            for(String tag : feed.getTags()) {
                if (!tags.contains(tag)) {
                    tmpTags.add(tag);
                }
            }
            tags.addAll(tmpTags);
        }

        for(String tag : tags) {
            this.tagsMenu.add(tag);
        }
    }

}
