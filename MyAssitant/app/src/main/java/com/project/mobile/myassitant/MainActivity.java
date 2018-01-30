package com.project.mobile.myassitant;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.project.mobile.fragment.AdviceFragment;
import com.project.mobile.fragment.ShareEventsFragment;
import com.project.mobile.fragment.ToDoFragment;
import com.project.mobile.helper.ConnectHelper;

public class MainActivity extends AppCompatActivity {
    private ImageView imgBackArrow;

    private DrawerLayout mDrawwer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addControls();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        ToDoFragment toDoFragment = new ToDoFragment();
        fragmentTransaction.replace(R.id.main_container, toDoFragment).commit();

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                doOnbackStackChange();
            }
        });
    }

    private void doOnbackStackChange(){
        if (getSupportFragmentManager().getBackStackEntryCount() > 0){
            drawerToggle.setDrawerIndicatorEnabled(false);
        }else{
            drawerToggle.setDrawerIndicatorEnabled(true);
        }
    }

    private void addControls() {
        mDrawwer = (DrawerLayout) findViewById(R.id.draw_layout);
        drawerToggle = setDrawerToggle();
        nvDrawer = (NavigationView) findViewById(R.id.nv_main);
        setupDrawerContent(nvDrawer);
        drawerToggle.syncState();
        View header = nvDrawer.getHeaderView(0);
        imgBackArrow = (ImageView) header.findViewById(R.id.img_arrow_back);
        imgBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawwer.closeDrawer(Gravity.START);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        if (getSupportFragmentManager().getBackStackEntryCount() == 0){
            if (drawerToggle.onOptionsItemSelected(item)) {
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private ActionBarDrawerToggle setDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawwer, toolbar, R.string.open_navigation, R.string.close_navigation);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawer(item);
                return true;
            }
        });
    }

    private void selectDrawer(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.mnu_advice:
                if (!ConnectHelper.isNetWorkAvalable(this)){
                    Toast.makeText(this, "Internet not available", Toast.LENGTH_LONG).show();
                }else{
                    mDrawwer.closeDrawer(Gravity.START);
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    AdviceFragment adviceFragment = new AdviceFragment();
                    fragmentTransaction.replace(R.id.main_container, adviceFragment).commit();
                }
                break;

            case R.id.mnu_share_evnets:
                ShareEventsFragment shareEventsFragment = new ShareEventsFragment();
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_container, shareEventsFragment).commit();
                mDrawwer.closeDrawer(Gravity.START);
                break;

            case R.id.mnu_to_do:
                mDrawwer.closeDrawer(Gravity.START);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                ToDoFragment toDoFragment = new ToDoFragment();
                fragmentTransaction.replace(R.id.main_container, toDoFragment).commit();
                break;

        }
    }
}
