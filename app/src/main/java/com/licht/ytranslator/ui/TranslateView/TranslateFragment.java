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
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.licht.ytranslator.R;
import com.licht.ytranslator.YTransApp;
import com.licht.ytranslator.presenters.TranslatePresenter;
import com.licht.ytranslator.ui.DictionaryView.DictionaryActivity;
import com.licht.ytranslator.ui.LanguageSelectView.SelectLanguageActivity;
import com.licht.ytranslator.utils.ExtendedEditText.ExtendedEditText;
import com.licht.ytranslator.utils.ExtendedEditText.ExtendedEditTextListener;

import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Subscription;

public class TranslateFragment extends Fragment implements ITranslateView, ExtendedEditTextListener {
    @Inject
    TranslatePresenter presenter;

    private Unbinder unbinder;

    @BindView(R.id.edit_input_text)
    ExtendedEditText inputText;

    @BindView(R.id.tv_translated_text)
    TextView tvTranslatedText;

    @BindView(R.id.tv_selected_source_lang)
    TextView tvSelectedSourceLang;

    @BindView(R.id.tv_selected_dest_lang)
    TextView tvSelectedDestLang;

    @BindView(R.id.tv_show_details_label)
    TextView tvShowDetailsLabel;

    ImageView ivIsStarred;

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

        ivIsStarred = (ImageView)root.findViewById(R.id.iv_is_starred);
        ivIsStarred.setOnClickListener(v -> presenter.onStarredClick());

        editTextSub = RxTextView.textChanges(inputText)
                .filter(seq -> seq != null)
                .subscribe(charSequence -> {
                    mCurrentWord = charSequence.toString();
                    presenter.onTextInput(mCurrentWord);
                });

        inputText.setOnEditTextImeBackListener(this);
    }

    @Override
    public void isStarVisible(boolean isVisible) {
        ivIsStarred.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onImeBack(ExtendedEditText ctrl, String text) {
        presenter.onKeyboardHide();
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
    public void setInputText(String text) {
        inputText.setText(text);
    }

    @Override
    public void setTranslatedText(String text) {
        tvTranslatedText.setText(text);
    }

    @Override
    public void setIsStarredView(boolean isStarred) {
        if (isStarred)
            ivIsStarred.setImageResource(R.drawable.ic_star);
        else
            ivIsStarred.setImageResource(R.drawable.ic_bookmark);
    }

//    @OnClick(R.id.iv_is_starred)
//    public void onStarredClick() {
//        presenter.onStarredClick();
//    }

    @OnClick(R.id.iv_swap_language)
    public void swapLanguages() {
        presenter.onSwapLanguages();
    }

    @OnClick(R.id.iv_clear_input)
    public void onClearInput() {
        presenter.onClearInput();
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

    @Override
    public void detailsAreAvailable(boolean isVisible) {
        if (isVisible) {
            tvShowDetailsLabel.setVisibility(View.VISIBLE);
            tvTranslatedText.setOnClickListener(v -> presenter.onOpenDictionaryClick());
        }
        else {
            tvShowDetailsLabel.setVisibility(View.INVISIBLE);
            tvTranslatedText.setOnClickListener(null);
        }
    }

//    @OnClick(R.id.tv_translated_text)
//    public void onTranslatedTextClick() {
//        presenter.onOpenDictionaryClick();
//    }

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
