package com.licht.ytranslator.ui.LanguageSelectView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.licht.ytranslator.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SelectLanguageActivity extends AppCompatActivity implements ISelectLanguageView {
    public static final String AVAILABLE_LANGUAGE_LIST = "LANGUAGE_LIST";
    public static final String SELECTED_LANGUAGE = "SELECTED_LANGUAGE";
    public static final String RESULT_LANGUAGE = "RESULT_LANGUAGE";

    @BindView(R.id.rv_languages_list)
    RecyclerView mRecyclerView;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);
        unbinder = ButterKnife.bind(this);

        Bundle b = this.getIntent().getExtras();
        ArrayList<String> languagesList = b.getStringArrayList(AVAILABLE_LANGUAGE_LIST);
        String currentLanguage = b.getString(SELECTED_LANGUAGE);

        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        LanguageListAdapter mAdapter = new LanguageListAdapter(this, languagesList, currentLanguage);
        mRecyclerView.setAdapter(mAdapter);
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
}
