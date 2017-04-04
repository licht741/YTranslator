package com.licht.ytranslator.ui.TranslateView;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.licht.ytranslator.utils.LocalizationUtils;
import com.licht.ytranslator.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

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
    ExtendedEditText tvInputText;
    @BindView(R.id.tv_translated_text)
    TextView tvTranslatedText;
    @BindView(R.id.tv_selected_source_lang)
    TextView tvSelectedSourceLang;
    @BindView(R.id.tv_selected_dest_lang)
    TextView tvSelectedDestLang;
    @BindView(R.id.tv_show_details_label)
    TextView tvShowDetailsLabel;
    @BindView(R.id.iv_is_starred)
    ImageView ivIsStarred;

    Subscription editTextSub;

    private String mCurrentWord;

    private static final int REQ_CODE_SOURCE_LANGUAGE = 100;
    private static final int REQ_CODE_DESTINATION_LANGUAGE = 200;
    private static final int REQ_CODE_SPEECH_INPUT = 300;

    private static final String ARG_WORD = "arg_word";
    private static final String ARG_DIRECTION = "arg_direction";

    public static TranslateFragment newInstance(String word, String direction) {
        TranslateFragment fragment = new TranslateFragment();

        Bundle args = new Bundle();
        args.putString(ARG_WORD, word);
        args.putString(ARG_DIRECTION, direction);
        fragment.setArguments(args);

        return fragment;
    }

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
        initUI(root);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Bundle args = getArguments();
        if (args == null) {
            presenter.requestData();
        } else {
            final String word = args.getString(ARG_WORD);
            final String direction = args.getString(ARG_DIRECTION);

            if (word != null && direction != null)
                presenter.initializeData(word, direction);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Переводчик");
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
        tvInputText.setText(text);
    }

    @Override
    public void setTranslatedText(String inputText, String outputText) {

        // Могла возникнуть такая ситуация, что для текста, который ввели раньше, получили ответ позже,
        // чем от текста, который ввели позже
        // Поэтому, перед выводом результата проверяем, что выводим перевод именно того текста,
        // который сейчас введён.
        if (tvInputText.getText().toString().equals(inputText))
            tvTranslatedText.setText(outputText);
    }

    @Override
    public void setIsStarredView(boolean isStarred) {
        if (isStarred)
            ivIsStarred.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_star));
        else
            ivIsStarred.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_bookmark));
    }

    @Override
    public void detailsAreAvailable(boolean isVisible) {
        if (isVisible) {
            tvShowDetailsLabel.setVisibility(View.VISIBLE);
            tvTranslatedText.setOnClickListener(v -> presenter.onOpenDictionaryClick());
        } else {
            tvShowDetailsLabel.setVisibility(View.INVISIBLE);
            tvTranslatedText.setOnClickListener(null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT:
                final String input = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
                setInputText(input);
                onTextInput(input);
                break;

            case REQ_CODE_SOURCE_LANGUAGE:
                final String resultLanguage = data.getStringExtra(SelectLanguageActivity.RESULT_LANGUAGE);
                presenter.onUpdateSourceLanguage(resultLanguage); // todo
                break;

            case REQ_CODE_DESTINATION_LANGUAGE:
                final String destLanguage = data.getStringExtra(SelectLanguageActivity.RESULT_LANGUAGE);
                presenter.onUpdateDestinationLanguage(destLanguage); // todo
                break;
        }
    }

    private void onTextInput(String text) {
        if (text == null || "".equals(text))
            setIsStarredView(false);
        presenter.onTextInput(text);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.unbindView();
        editTextSub.unsubscribe();
    }


    @OnClick(R.id.iv_microphone)
    public void onMicrophoneClick() {
        startAudio();
    }


    @OnClick(R.id.iv_swap_language)
    public void swapLanguages() {
        presenter.onSwapLanguages();
    }

    @OnClick(R.id.iv_clear_input)
    public void onClearInput() {
        isStarVisible(false);
        detailsAreAvailable(false);
        setInputText("");
    }

    @OnClick(R.id.tv_selected_source_lang)
    public void onSelectedSourceClick() {
        final String selectedLanguage = presenter.getSourceLanguage();
        final ArrayList<String> languages = presenter.getLanguagesList();
        Collections.sort(languages);

        Intent intent = new Intent(getContext(), SelectLanguageActivity.class);
        Bundle b = new Bundle();
        b.putString(SelectLanguageActivity.SELECTED_LANGUAGE, selectedLanguage);
        b.putStringArrayList(SelectLanguageActivity.AVAILABLE_LANGUAGE_LIST, languages);
        intent.putExtras(b);
        startActivityForResult(intent, REQ_CODE_SOURCE_LANGUAGE);

    }

    @OnClick(R.id.tv_selected_dest_lang)
    public void onSelectedDestinationClick() {
        final String selectedLanguage = presenter.getDestinationLanguage();
        final ArrayList<String> languages = presenter.getLanguagesList();

        Intent intent = new Intent(getContext(), SelectLanguageActivity.class);
        Bundle b = new Bundle();
        b.putString(SelectLanguageActivity.SELECTED_LANGUAGE, selectedLanguage);
        b.putStringArrayList(SelectLanguageActivity.AVAILABLE_LANGUAGE_LIST, languages);
        intent.putExtras(b);
        startActivityForResult(intent, REQ_CODE_DESTINATION_LANGUAGE);
    }


    private void startAudio() { // // TODO: 30.03.2017
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        final Locale locale = new Locale(LocalizationUtils.getCurrentLocalizationSymbol());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, locale);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    private void initUI(View root) {
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(getContext(),
                                  android.R.color.white));

        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();

        ivIsStarred.setOnClickListener(v -> presenter.onStarredClick());

        editTextSub = RxTextView.textChanges(tvInputText)
                .filter(seq -> seq != null)
                .subscribe(charSequence -> {
                    mCurrentWord = charSequence.toString();
                    onTextInput(mCurrentWord);
                });

        tvInputText.setOnEditTextImeBackListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        Utils.hideKeyboard(getActivity());
    }


    @Override
    public void onLanguageChanges() {
        onTextInput(mCurrentWord);
    }

    @Override
    public void onLanguagesSwapped() {
        mCurrentWord = tvTranslatedText.getText().toString();
        tvInputText.setText(mCurrentWord);
    }
}
