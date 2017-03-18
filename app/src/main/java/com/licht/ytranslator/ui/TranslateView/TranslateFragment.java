package com.licht.ytranslator.ui.TranslateView;


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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.licht.ytranslator.R;
import com.licht.ytranslator.YTransApp;
import com.licht.ytranslator.data.DataManager;
import com.licht.ytranslator.data.Result;
import com.licht.ytranslator.presenters.TranslatePresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscription;

public class TranslateFragment extends Fragment implements ITranslateView {
    @Inject
    TranslatePresenter presenter;

    private Unbinder unbinder;

    @BindView(R.id.edit_input_text)
    EditText inputText;

    @BindView(R.id.tv_translated_text)
    TextView tvTranslatedText;

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

    private void initUI(View root) {
        String[] langs = {"Русский", "Английский", "Немецкий", "Французский"};

        final Spinner spinnerFrom = (Spinner) root.findViewById(R.id.spn_translate_from);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, langs);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFrom.setAdapter(adapter1);

        final Spinner spinnerTo = (Spinner) root.findViewById(R.id.spn_translate_to);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, langs);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerTo.setAdapter(adapter2);

        editTextSub = RxTextView.textChanges(inputText)
                .filter(seq -> seq != null && seq.length() > 0)
                .subscribe(charSequence -> presenter.translate(charSequence.toString()));

    }

    @Override
    public void setTranslatedText(String text) {
        tvTranslatedText.setText(text);
    }

    @OnClick(R.id.iv_swap_language)
    public void swapLanguages() {
        Toast.makeText(getContext(), "It works!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.unbindView();
        editTextSub.unsubscribe();
    }
}
