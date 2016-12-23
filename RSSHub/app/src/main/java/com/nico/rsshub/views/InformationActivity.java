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
import android.widget.ListView;

import com.nico.rsshub.R;
import com.nico.rsshub.controllers.Controller;
import com.nico.rsshub.modeles.Category;
import com.nico.rsshub.modeles.Feed;
import com.nico.rsshub.modeles.Information;

import java.util.List;
import java.util.Vector;

public class InformationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SubMenu favoritesMenu;

    private SubMenu categoriesMenu;

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
        this.categoriesMenu  = m.addSubMenu(R.string.categories);
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


        Vector<View> pages = new Vector<>();
        Vector<String> titles = new Vector<>();
        final Vector<Button> buttons = new Vector<>();
        pages.add(listViewNews);
        titles.add(getString(R.string.news));
        newsButton = (Button)findViewById(R.id.news_button);
        newsButton.setText(getString(R.string.news));
        this.initButtonClickListener(newsButton,0);
        buttons.add(newsButton);
        newsButton.setTextColor(getColor(R.color.orange));


        pages.add(listViewNewsFavorites);
        titles.add(getString(R.string.favorites));
        newsFavoritesButton = (Button)findViewById(R.id.news_favorites_button);
        newsFavoritesButton.setText(getString(R.string.favorites));
        this.initButtonClickListener(newsFavoritesButton,1);
        buttons.add(newsFavoritesButton);

        pages.add(listViewChrono);
        titles.add(getString(R.string.chrono));
        chronoButton = (Button)findViewById(R.id.chrono_button);
        chronoButton.setText(getString(R.string.chrono));
        this.initButtonClickListener(chronoButton,2);
        buttons.add(chronoButton);

        pages.add(listViewChronoFavorites);
        titles.add(getString(R.string.chrono_favorites));
        chronoFavoritesButton = (Button)findViewById(R.id.chrono_favorites_button);
        chronoFavoritesButton.setText(getString(R.string.chrono_favorites));
        this.initButtonClickListener(chronoFavoritesButton,3);
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
                buttons.get(position).setTextColor(getColor(R.color.orange));
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

        adapterNews.notifyDataSetChanged();
    }

    public void refreshListViews() {
        ((BaseAdapter) listViewNews.getAdapter()).notifyDataSetChanged();
        ((BaseAdapter) listViewChrono.getAdapter()).notifyDataSetChanged();
        ((BaseAdapter) listViewNewsFavorites.getAdapter()).notifyDataSetChanged();
        ((BaseAdapter) listViewChronoFavorites.getAdapter()).notifyDataSetChanged();
    }

    public void initButtonClickListener(Button button, final int index) {
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
                viewPager.setCurrentItem(index, true);
            }
        });
    }

    public void refreshNavigationMenu(List<Feed> feeds) {
        this.favoritesMenu.clear();
        this.categoriesMenu.clear();
        this.feedsMenu.clear();

        for(Feed feed : feeds) {
            if(feed.isFavorite()) {
                this.favoritesMenu.add(feed.getTitle());
            }
            this.feedsMenu.add(feed.getTitle());
        }

        for(Category category : Category.values()) {
            this.categoriesMenu.add(category.getValue());
        }

    }

}
