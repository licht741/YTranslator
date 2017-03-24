package com.licht.ytranslator.ui;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.licht.ytranslator.R;
import com.licht.ytranslator.ui.TranslateView.TranslateFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        final View activityRootView = findViewById(R.id.drawer_layout);
//        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
//            int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
//
//            if (heightDiff > 100) {
//                Toast.makeText(getApplicationContext(), "true", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(getApplicationContext(), "false", Toast.LENGTH_SHORT).show();
//            }
//        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setFragment(false, new TranslateFragment());
    }

    public void setFragment(boolean addToBackStack, Fragment fragment) {
        if (getCurrentFragmentClass() != null && getCurrentFragmentClass() == fragment.getClass())
            return;

        if (addToBackStack)
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit();
            else
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
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
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_translate)
            setFragment(false, new TranslateFragment());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Nullable
    private Class getCurrentFragmentClass() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);

        return fragment == null ? null : fragment.getClass();
    }
}
