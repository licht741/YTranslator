package com.licht.ytranslator.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.licht.ytranslator.R;
import com.licht.ytranslator.ui.HistoryView.HistoryListFragment;
import com.licht.ytranslator.ui.HistoryView.StarredListFragment;
import com.licht.ytranslator.ui.TranslateView.TranslateFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = "Fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Если никакой фрагмент не восстанавливается системой, то запускаем новый
        Fragment fmt = getSupportFragmentManager().findFragmentByTag(TAG);
        if (fmt == null)
            setFragment(new TranslateFragment());
    }

    public void setFragment(Fragment fragment) {
        // Если мы хотим перейти на экран, который уже открыт, ничего не делаем
        if (getCurrentFragmentClass() != null && getCurrentFragmentClass() == fragment.getClass())
            return;

        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.container, fragment, TAG)
            .commit();
    }

    @Override
    public void onBackPressed() {
        // По нажатию на кнопку возврата, сначала закрываем боковое меню (если оно было открыто)
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Обрабатываем выбор экрана в боковом меню
        // C точки зрения логики приложения, все экраны, на которые можно перейти из бокового меню "равноценные"
        // То есть нажатие на кнопку "Назад" на каждом из этих экранов приводит к закрытию приложения
        int id = item.getItemId();
        if (id == R.id.nav_translate)
            setFragment(new TranslateFragment());
        else if (id == R.id.nav_history)
            setFragment(new HistoryListFragment());
        else if (id == R.id.nav_starred)
            setFragment(new StarredListFragment());

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
