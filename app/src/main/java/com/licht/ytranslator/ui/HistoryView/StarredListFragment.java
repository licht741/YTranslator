package com.licht.ytranslator.ui.HistoryView;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.licht.ytranslator.R;
import com.licht.ytranslator.YTransApp;
import com.licht.ytranslator.data.model.HistoryObject;
import com.licht.ytranslator.presenters.HistoryPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StarredListFragment extends Fragment implements IHistoryView {

    Unbinder unbinder;

    @Inject
    HistoryPresenter presenter;

    @BindView(R.id.rv_history_list)
    RecyclerView recyclerView;

    private final HistoryAdapter adapter = new HistoryAdapter();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        YTransApp.getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_starred_list, container, false);
        unbinder = ButterKnife.bind(this, root);
        presenter.bindView(this);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.requestData(true);
    }

    @Override
    public void setData(List<HistoryObject> items) {
        adapter.setData(items);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.unbindView();
    }
}
