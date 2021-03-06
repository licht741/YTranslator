package com.licht.ytranslator.ui.LanguageSelectView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.EditText;

import com.licht.ytranslator.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Экран выбор языка (языка перевода или языка, на который осуществляется перевод)
 */
public class SelectLanguageActivity extends AppCompatActivity
        implements ISelectLanguageView, SearchView.OnQueryTextListener {
    public static final String AVAILABLE_LANGUAGE_LIST = "LANGUAGE_LIST";
    public static final String SELECTED_LANGUAGE = "SELECTED_LANGUAGE";
    public static final String RECENTLY_LANGUAGE_LIST = "RECENTLY_LANGUAGE_LIST";
    public static final String RESULT_LANGUAGE = "RESULT_LANGUAGE";

    @BindView(R.id.rv_languages_list) RecyclerView mRecyclerView;
    private Unbinder unbinder;

    private LanguageListAdapter mAdapter = null;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);
        unbinder = ButterKnife.bind(this);

        initUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSupportActionBar().setTitle(getString(R.string.select_language));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select_language, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);

        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        searchEditText.setHintTextColor(ContextCompat.getColor(this, android.R.color.white));

        if (previousSearchRequest != null) {
            searchView.post(() -> {
                searchView.onActionViewExpanded();
                searchView.setQuery(previousSearchRequest, false);
                previousSearchRequest = null;
            });
        }

        return true;
    }

    @Override
    public void sendResult(String result) {
        Intent intent = new Intent();
        intent.putExtra(RESULT_LANGUAGE, result);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.getFilter().filter(newText);
        return false;
    }

    private void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));

        Bundle b = this.getIntent().getExtras();
        ArrayList<String> languagesList = b.getStringArrayList(AVAILABLE_LANGUAGE_LIST);
        String currentLanguage = b.getString(SELECTED_LANGUAGE);

        ArrayList<String> recentlyUsedLanguages = b.getStringArrayList(RECENTLY_LANGUAGE_LIST);

        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new LanguageListAdapter(this, languagesList, recentlyUsedLanguages, currentLanguage);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("search", searchView.getQuery().toString());
    }

    private String previousSearchRequest = null;
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Восстанавливаем введенный поиск (если он был)
        if (savedInstanceState == null || !savedInstanceState.containsKey("search"))
            return;

        previousSearchRequest = savedInstanceState.getString("search");

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
