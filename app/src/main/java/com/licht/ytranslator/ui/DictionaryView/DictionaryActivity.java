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
import com.licht.ytranslator.utils.Utils;

import javax.inject.Inject;

/**
 * Экран, на котором отображаются данные о слове, которые были получены через API Яндекс Словаря
 * В приложении яндекс переводчика эти данные отображаются на основном экране перевода,
 * но мне кажется более удобным, отображать их на отдельном экране, разбивая на смысловые вкладки
 */
public class DictionaryActivity extends AppCompatActivity {

    // На этом экране достаточно простая логика,
    // поэтому нарушается принцип MVP, и обращение к данным идёт напрямую, без презентера
    @Inject
    DataManager dataManager;

    private DictionaryObject mDictionaryObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        YTransApp.getAppComponent().inject(this);

        // При открытии этой активити, должны быть переданы данные о том, какой перевод
        // используется
        String word = getIntent().getStringExtra("WORD");
        String dirs = getIntent().getStringExtra("DIRECTION");
        mDictionaryObject = dataManager.getCachedWord(word, dirs);

        initUI();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.dictionary));

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }


    private void setupViewPager(ViewPager viewPager) {
        // Для каждого возможного толкования слова заводим отдельную вкладку
        // Для названия вкладки используем часть речи, к которой относится это толкование
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        final int dictCount = mDictionaryObject.getDictionaries().size();
        for (WordObject wordObject : mDictionaryObject.getDictionaries()) {
            final String wordType = wordObject.getType();

            // Если нам нужно сделать больше, чем 2 таба, то надо сократить их названия
            // В противном случае будут некрасивые переносы
            final String title = dictCount < 3 ? wordType : Utils.cutWord(wordType);
            adapter.addFragment(DictionaryFragment.newInstance(wordObject.getId()), title);
        }
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        finish();
    }


}
