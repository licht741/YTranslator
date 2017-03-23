package com.licht.ytranslator.ui.TranslateView;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.licht.ytranslator.R;
import com.licht.ytranslator.YTransApp;
import com.licht.ytranslator.data.model.Dictionary;
import com.licht.ytranslator.presenters.TranslatePresenter;
import com.licht.ytranslator.ui.DictionaryView.DictionaryActivity;
import com.licht.ytranslator.ui.LanguageSelectView.SelectLanguageActivity;

import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Subscription;

public class TranslateFragment extends Fragment implements ITranslateView {
    @Inject
    TranslatePresenter presenter;

    private Unbinder unbinder;

    @BindView(R.id.edit_input_text)
    EditText inputText;

    @BindView(R.id.tv_translated_text)
    TextView tvTranslatedText;

    @BindView(R.id.tv_selected_source_lang)
    TextView tvSelectedSourceLang;

    @BindView(R.id.tv_selected_dest_lang)
    TextView tvSelectedDestLang;

    Subscription editTextSub;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        YTransApp.getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_translate, container, false);
        presenter.bindView(this);
        unbinder = ButterKnife.bind(this, root);

        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        initUI(root);
        return root;
    }

    private String mCurrentWord;

    private void initUI(View root) {
        presenter.requestData();
        editTextSub = RxTextView.textChanges(inputText)
                .filter(seq -> seq != null && seq.length() > 0)
                .subscribe(charSequence -> {
                    mCurrentWord = charSequence.toString();
                    presenter.translate(mCurrentWord);
                });


    }

    @Override
    public void openDictionary(String word, String direction) {
        Intent intent = new Intent(getActivity(), DictionaryActivity.class);
        intent.putExtra("WORD", word);
        intent.putExtra("DIRECTION", direction);
        getActivity().startActivity(intent);
    }

    @Override
    public void setLanguagePair(String source, String destination) {
        tvSelectedSourceLang.setText(source);
        tvSelectedDestLang.setText(destination);
    }

    @Override
    public void setTranslatedText(String text) {
        tvTranslatedText.setText(text);
    }

    @OnClick(R.id.iv_swap_language)
    public void swapLanguages() {
        presenter.swapLanguages();
    }

    @OnClick(R.id.tv_selected_source_lang)
    public void onSelectedSourceClick() {
        final String selectedLanguage = presenter.getSourceLanguage();
        final ArrayList<String> languages = presenter.getSourceLanguages();
        Collections.sort(languages);

        Intent intent = new Intent(getContext(), SelectLanguageActivity.class);
        Bundle b = new Bundle();
        b.putString(SelectLanguageActivity.SELECTED_LANGUAGE, selectedLanguage);
        b.putStringArrayList(SelectLanguageActivity.AVAILABLE_LANGUAGE_LIST, languages);
        intent.putExtras(b);
        startActivityForResult(intent, 100);

    }

    @OnClick(R.id.tv_selected_dest_lang)
    public void onSelectedDestinationClick() {
        final String selectedLanguage = presenter.getDestinationLanguage();
        final ArrayList<String> languages = presenter.getDestinationLanguages();

        Intent intent = new Intent(getContext(), SelectLanguageActivity.class);
        Bundle b = new Bundle();
        b.putString(SelectLanguageActivity.SELECTED_LANGUAGE, selectedLanguage);
        b.putStringArrayList(SelectLanguageActivity.AVAILABLE_LANGUAGE_LIST, languages);
        intent.putExtras(b);
        startActivityForResult(intent, 200);
    }

    @OnClick(R.id.btn_open_dictionary)
    public void onDictionaryOpen() {
        presenter.dictionaryOper();
//        Intent intent = new Intent(getActivity(), DictionaryActivity.class);
//        intent.putExtra("Word", mCurrentWord);
//
//        getActivity().startActivityForResult(intent, 400);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 400)
            return;

        if (data == null || data.getStringExtra(SelectLanguageActivity.RESULT_LANGUAGE) == null)
            return;
        final String resultLanguage = data.getStringExtra(SelectLanguageActivity.RESULT_LANGUAGE);

        if (requestCode == 100) {
            presenter.updateSourceLanguage(resultLanguage);
        } else if (requestCode == 200) {
            presenter.updateDestinationLanguage(resultLanguage);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.unbindView();
        editTextSub.unsubscribe();
    }
}
