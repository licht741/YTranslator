package com.licht.ytranslator.ui.DictionaryView;

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
import com.licht.ytranslator.data.model.Dictionary;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DictionaryFragment extends Fragment {

    private static final String DICTIONARY_ID = "DICTIONARY";
    private long mDictionaryID;

    private Dictionary mDictionary;

    @Inject
    DataManager dataManager;

    @BindView(R.id.tv_dictionary_word)
    TextView dictionaryWord;

    @BindView(R.id.tv_dictionary_transcr)
    TextView dictionaryTranscription;

    @BindView(R.id.rv_dictionary)
    RecyclerView rvDictionary;

    private Unbinder unbinder;

    @Inject
    DataManager manager;


    public DictionaryFragment() {
        YTransApp.getAppComponent().inject(this);
    }

    public static DictionaryFragment newInstance(long id) {
        DictionaryFragment fragment = new DictionaryFragment();
        Bundle args = new Bundle();
        args.putLong(DICTIONARY_ID, id);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDictionaryID = getArguments().getLong(DICTIONARY_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dictionary, container, false);

        unbinder = ButterKnife.bind(this, root);

        mDictionary = dataManager.getCachedDictionary(mDictionaryID);

        dictionaryWord.setText(mDictionary.getText());
        dictionaryTranscription.setText(String.format("[%s]", mDictionary.getTrans()));

        rvDictionary.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvDictionary.setLayoutManager(mLayoutManager);

        WordAdapter mAdapter = new WordAdapter(dataManager.getCachedDictionary(mDictionaryID));
        rvDictionary.setAdapter(mAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}