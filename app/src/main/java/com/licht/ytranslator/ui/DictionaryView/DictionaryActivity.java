package com.licht.ytranslator.ui.DictionaryView;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.licht.ytranslator.R;
import com.licht.ytranslator.YTransApp;
import com.licht.ytranslator.data.DataManager;
import com.licht.ytranslator.data.model.Dictionary;
import com.licht.ytranslator.data.model.Word;

import javax.inject.Inject;

public class DictionaryActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Inject
    DataManager dataManager;

    private Word mWord;
    private String word;
    private String dirs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        YTransApp.getAppComponent().inject(this);

        word = getIntent().getStringExtra("WORD");
        dirs = getIntent().getStringExtra("DIRECTION");

        mWord = dataManager.getCachedWord(word, dirs);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mWord.getWord().toUpperCase());

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (Dictionary dictionary : mWord.getDictionaries()) {
            final String title = dictionary.getType();
            adapter.addFragment(DictionaryFragment.newInstance(dictionary.getId()), title);
        }
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        finish();
    }


}
