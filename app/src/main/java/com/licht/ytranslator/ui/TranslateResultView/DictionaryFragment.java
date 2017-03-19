package com.licht.ytranslator.ui.TranslateResultView;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.licht.ytranslator.R;
import com.licht.ytranslator.YTransApp;
import com.licht.ytranslator.data.DataManager;
import com.licht.ytranslator.data.model.Word;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DictionaryFragment extends Fragment {

    private static final String ARG_WORD = "WORD";
    private static final String ARG_DIR = "DIRECTION";

    private String mWord;
    private String mDirection;

    @Inject
    DataManager dataManager;

    @BindView(R.id.tv_dictionary_word)
    TextView dictionaryWord;

    @BindView(R.id.tv_dictionary_transcr)
    TextView dictionaryTranscription;

    @BindView(R.id.rv_dictionary)
    RecyclerView rvDictionary;

    private Unbinder unbinder;

    public DictionaryFragment() {
        YTransApp.getAppComponent().inject(this);
    }

    public static DictionaryFragment newInstance(String word, String dir) {
        DictionaryFragment fragment = new DictionaryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_WORD, word);
        args.putString(ARG_DIR, dir);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mWord = getArguments().getString(ARG_WORD);
            mDirection = getArguments().getString(ARG_DIR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dictionary, container, false);

        unbinder = ButterKnife.bind(this, root);

        Word w = dataManager.getCachedWord(mWord, mDirection);

        dictionaryWord.setText(w.getWord());
        dictionaryTranscription.setText(w.getDictionaries().get(0).getTrans());

        int x = 0;

        rvDictionary.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvDictionary.setLayoutManager(mLayoutManager);

        WordAdapter mAdapter = new WordAdapter(w);
        rvDictionary.setAdapter(mAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
