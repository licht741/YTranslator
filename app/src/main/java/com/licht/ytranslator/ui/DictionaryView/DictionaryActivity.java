package com.licht.ytranslator.ui.DictionaryView;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.licht.ytranslator.R;
import com.licht.ytranslator.YTransApp;
import com.licht.ytranslator.data.DataManager;
import com.licht.ytranslator.data.model.WordObject;
import com.licht.ytranslator.data.model.DictionaryObject;

import javax.inject.Inject;

public class DictionaryActivity extends AppCompatActivity {

    // На этом экране достаточно простая логика,
    // поэтому используем источник данных напрямую, без презентера
    @Inject
    DataManager dataManager;

    private DictionaryObject mDictionaryObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        YTransApp.getAppComponent().inject(this);

        String word = getIntent().getStringExtra("WORD");
        String dirs = getIntent().getStringExtra("DIRECTION");

        mDictionaryObject = dataManager.getCachedWord(word, dirs);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mDictionaryObject.getWord().toUpperCase());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void setupViewPager(ViewPager viewPager) {
        // Для каждого возможного толкования слова заводим отдельную вкладку
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (WordObject wordObject : mDictionaryObject.getDictionaries()) {
            final String title = wordObject.getType();
            adapter.addFragment(DictionaryFragment.newInstance(wordObject.getId()), title);
        }
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        finish();
    }


}
