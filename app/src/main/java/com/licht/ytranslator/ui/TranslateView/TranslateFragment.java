package com.licht.ytranslator.ui.TranslateView;


import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.licht.ytranslator.R;
import com.licht.ytranslator.YTransApp;
import com.licht.ytranslator.presenters.TranslatePresenter;
import com.licht.ytranslator.ui.DictionaryView.DictionaryActivity;
import com.licht.ytranslator.ui.LanguageSelectView.SelectLanguageActivity;
import com.licht.ytranslator.utils.ExtendedEditText.ExtendedEditText;
import com.licht.ytranslator.utils.ExtendedEditText.ExtendedEditTextListener;
import com.licht.ytranslator.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class TranslateFragment extends Fragment implements ITranslateView, ExtendedEditTextListener {
    @Inject
    TranslatePresenter presenter;

    private Unbinder unbinder;

    @BindView(R.id.edit_input_text) ExtendedEditText tvInputText;
    @BindView(R.id.tv_translated_text) TextView tvTranslatedText;
    @BindView(R.id.tv_selected_source_lang) TextView tvSelectedSourceLang;
    @BindView(R.id.tv_selected_dest_lang) TextView tvSelectedDestLang;
    @BindView(R.id.tv_show_details_label) TextView tvShowDetailsLabel;
    @BindView(R.id.iv_is_starred) ImageView ivIsStarred;
    @BindView(R.id.iv_share) ImageView ivShare;
    @BindView(R.id.iv_copy) ImageView ivCopy;
    @BindView(R.id.iv_microphone) ImageView ivMicrophone;
    @BindView(R.id.iv_clear_input) ImageView ivClear;

    private static final int REQ_CODE_SOURCE_LANGUAGE = 100;
    private static final int REQ_CODE_DESTINATION_LANGUAGE = 200;
    private static final int REQ_CODE_SPEECH_INPUT = 300;

    private static final String ARG_WORD = "arg_word";
    private static final String ARG_DIRECTION = "arg_direction";

    // Слово, которое сейчас находится в поле ввода
    private String mCurrentWord;

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

        // Если во фрагмент были явно переданы параметры перевода (переводимый текст и направление перевода),
        // то мы выводим их. Это может произойти при выборе слова из истории или списка избранных
        //
        // Если ничего не было передано, то мы используем параметры,
        // которые использовались при последнем переводе
        final Bundle args = getArguments();
        if (args == null) {
            presenter.requestData();
            return;
        }
        final String word = args.getString(ARG_WORD);
        final String direction = args.getString(ARG_DIRECTION);

        if (word != null && direction != null)
            presenter.initializeData(word, direction);
    }

    @Override
    public void onStart() {
        super.onStart();

        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(R.string.translate_title);
    }

    /**
     * @param text Текстовое содержимое
     */
    @Override
    public void onImeBack(String text) {
        // Отслеживаем состояние клавиатуры для того, чтоб добавлять слова в историю.
        // Более понятно мотивация расписана в методе TranslatePresenter.onKeyboardHide
        presenter.onKeyboardHide();
    }

    /**
     * Открывает детализацию перевода с переданными параметрами
     *
     * @param word      Переводимый текст
     * @param direction Направление перевода
     */
    @Override
    public void openDictionary(String word, String direction) {
        Intent intent = new Intent(getActivity(), DictionaryActivity.class);
        intent.putExtra("WORD", word);
        intent.putExtra("DIRECTION", direction);
        getActivity().startActivity(intent);
    }

    /**
     * @param source      Язык, с которого осуществляется перевод
     * @param destination Язык, на который осуществляется перевод
     */
    @Override
    public void setLanguagePair(String source, String destination) {
        tvSelectedSourceLang.setText(source);
        tvSelectedDestLang.setText(destination);
    }

    /**
     * @param text Текст, устанавливаемый в поле ввода
     */
    @Override
    public void setInputText(String text) {
        tvInputText.setText(text);
    }

    /**
     * @param inputText  Введённый текст
     * @param outputText Полученный перевод
     */
    @Override
    public void setTranslatedText(String inputText, String outputText) {

        // Могла возникнуть такая ситуация, что для текста, который ввели раньше, получили ответ позже,
        // чем от текста, который ввели позже
        // Поэтому, перед выводом результата проверяем, что выводим перевод именно того текста,
        // который сейчас введён.
        if (tvInputText.getText().toString().equals(inputText))
            tvTranslatedText.setText(outputText);
    }

    /**
     * @param isStarred True, если перевод в избранном, иначе False.
     */
    @Override
    public void isStarredText(boolean isStarred) {
        if (isStarred)
            ivIsStarred.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_star));
        else
            ivIsStarred.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_bookmark));
    }

    /**
     * Передаёт возможность открытия детального перевода текста (полученного через API Яндекс Словаря)
     *
     * @param isVisible True, если детальный перевод доступен, иначе False
     */
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

        // Обрабатываем ответы, полученные из других активити
        switch (requestCode) {
            // обрабататываем результат голосового ввода
            case REQ_CODE_SPEECH_INPUT:
                List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (results.size() > 0) {
                    final String input = results.get(0);
                    setInputText(input);
                } else // не удалось распознать текст, голосовой ввода недоступен, или возникла
                    // какая-то другая ошибка
                    Utils.showToast(getContext(), getString(R.string.error_voice_error));
                break;

            // Был выбран новый язык ввода
            case REQ_CODE_SOURCE_LANGUAGE:
                final String resultLanguage = data.getStringExtra(SelectLanguageActivity.RESULT_LANGUAGE);
                // Сообщаем презентеру, что исходный язык изменился
                presenter.onUpdateSourceLanguage(resultLanguage);
                break;

            // Был выбран новый язык назначения
            case REQ_CODE_DESTINATION_LANGUAGE:
                final String destLanguage = data.getStringExtra(SelectLanguageActivity.RESULT_LANGUAGE);
                // Сообщаем презентеру, что результирующий язык изменился
                presenter.onUpdateDestinationLanguage(destLanguage);
                break;
        }
    }

    /**
     * Вызывается при изменении содержимого поля ввода
     *
     * @param text Актуальный текст
     */

    private void onTextInput(String text) {
        isStarredText(false);

        // Сообщаем презентеру, что входной текст изменился
        presenter.onTextInput(text);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.unbindView();
    }

    @OnClick(R.id.iv_swap_language)
    public void swapLanguages() {
        presenter.onSwapLanguages();
    }

    @OnClick(R.id.tv_yandex_translate)
    public void onYandexTranslateLabelClick() {
        final String url = getString(R.string.yandex_translate_url);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @OnClick(R.id.tv_selected_source_lang)
    public void onSelectedSourceClick() {
        startActivityToSelectLanguage(REQ_CODE_SOURCE_LANGUAGE, presenter.getSourceLanguage());

    }

    @OnClick(R.id.tv_selected_dest_lang)
    public void onSelectedDestinationClick() {
        startActivityToSelectLanguage(REQ_CODE_DESTINATION_LANGUAGE, presenter.getDestinationLanguage());
    }

    /**
     * @param inputLanguageSym Язык, на котором осуществляется голосовой ввод
     */
    public void startAudioWithInputLanguage(String inputLanguageSym) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        final Locale locale = new Locale(inputLanguageSym);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, locale);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.audio_prompt));

        // Запускаем интент для голосового ввода
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException e) {
            // На устройстве не поддерживается голосовой ввод, или ещё какая-нибудь ошибка
            Utils.showToast(getContext(), getString(R.string.error_voice_error));
        }

    }

    private void initUI(View root) {
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(getContext(), android.R.color.white));

        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                // закрываем клавиатуру при открытии бокового меню
                super.onDrawerOpened(drawerView);
                if (getActivity() != null) {
                    presenter.onKeyboardHide();
                    Utils.hideKeyboard(getActivity());
                }
            }
        };
        drawer.addDrawerListener(toggle);

        toggle.syncState();

        ivIsStarred.setOnClickListener(v -> {
            if (tvTranslatedText.getText().length() > 0)
                presenter.onStarredClick();
        });

        ivMicrophone.setOnClickListener(v -> {
            showAnimationOnIconClick(v);
            if (tvTranslatedText.getText().length() > 0)
                presenter.onStartAudio();
        });

        ivClear.setOnClickListener(v -> {
            showAnimationOnIconClick(v);
            if ("".equals(tvInputText.getText().toString()))
                return;

            // Очищаем поля, открываем клавиатуру для ввода
            detailsAreAvailable(false);
            setInputText("");

            showKeyboard();
        });

        ivShare.setOnClickListener(v ->  {
            showAnimationOnIconClick(v);

            if (tvTranslatedText.getText().length() > 0)
                presenter.onShareText();
        });

        ivCopy.setOnClickListener(v -> {
            showAnimationOnIconClick(v);
            ClipboardManager clipboard = (ClipboardManager)
                    getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

            if (tvTranslatedText.getText().length() > 0) {
                clipboard.setText(tvTranslatedText.getText());
                Utils.showToast(getContext(), getString(R.string.text_is_copied));
            }

        });


        tvInputText.addTextChangedListener(new TextWatcher() {
            // Первые 2 случая обрабатывать не нужно
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Пользователь ввёл какой-то текст, начинаем его обработку
                mCurrentWord = s.toString();
                onTextInput(mCurrentWord);
            }
        });

        // Если пользователь использует долгий тап, то скорее всего он хочет вставить текст для перевода
        // Открываем клавиатуру, чтоб его можно было сразу отредактировать
        tvInputText.setOnLongClickListener(v -> {
            showKeyboard();
            return false;
        });
        tvInputText.setOnEditTextImeBackListener(this);
        tvTranslatedText.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public void onStop() {
        super.onStop();
        Utils.hideKeyboard(getActivity());
    }

    /**
     * Запускает анимацию при нажатии на иконки
     */
    private void showAnimationOnIconClick(View v) {
        v.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_icon_click));
    }

    /**
     * Вызывается когда один из языков был изменён, и необходимо обновить перевод
     */
    @Override
    public void onLanguageChanges() {
        onTextInput(mCurrentWord);
    }

    /**
     * Вызывается, когда изменилось направление перевода
     */
    @Override
    public void onLanguagesSwapped() {
        mCurrentWord = tvTranslatedText.getText().toString();
        tvInputText.setText(mCurrentWord);
    }

    /**
     * Сообщает о том, что получить перевод не получилось, и необходимо сообщить об этом пользователю
     */
    @Override
    public void onTranslateFailure() {
        Utils.showToast(getContext(), getString(R.string.error_translate_getting));
    }

    /**
     * Ставит фокус на поле ввода текста и открывает клавиатуру.
     */
    private void showKeyboard() {
        tvInputText.requestFocus();

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(tvInputText, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * Использовать механизм шаринга текста
     *
     * @param content Текст для шаринга
     */
    @Override
    public void shareText(String content) {
        final Intent sendIntent = Utils.createIntentToSharing(content);
        if (sendIntent.resolveActivity(getActivity().getPackageManager()) != null)
            getActivity().startActivity(sendIntent);
    }

    /**
     * Запускает активити для выбора языка, участвующего в переводе
     * Можно выбирать как исходный язык, так и результирующий. Чтобы обозначить, какой именно это случай
     * используем requestCode
     *
     * @param requestCode Код, по которому обрабатывается возвращаемый из активити результат
     */
    private void startActivityToSelectLanguage(final int requestCode,
                                               final String selectedLanguage) {
        // Список языков
        final ArrayList<String> languages = presenter.getLanguagesList();

        // Создаём активити для выбор языка и запускаем его
        Intent intent = new Intent(getContext(), SelectLanguageActivity.class);
        Bundle b = new Bundle();
        b.putString(SelectLanguageActivity.SELECTED_LANGUAGE, selectedLanguage);
        b.putStringArrayList(SelectLanguageActivity.AVAILABLE_LANGUAGE_LIST, languages);
        b.putStringArrayList(SelectLanguageActivity.RECENTLY_LANGUAGE_LIST, presenter.getRecentlyUsedLanguages());
        intent.putExtras(b);

        startActivityForResult(intent, requestCode);
    }
}
