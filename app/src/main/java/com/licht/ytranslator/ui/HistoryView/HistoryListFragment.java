package com.licht.ytranslator.ui.HistoryView;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.licht.ytranslator.R;
import com.licht.ytranslator.YTransApp;
import com.licht.ytranslator.data.model.HistoryObject;
import com.licht.ytranslator.presenters.HistoryPresenter;
import com.licht.ytranslator.ui.MainActivity;
import com.licht.ytranslator.ui.TranslateView.TranslateFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HistoryListFragment extends Fragment implements IHistoryView {

    Unbinder unbinder;

    @Inject
    HistoryPresenter presenter;

    @BindView(R.id.rv_history_list)
    RecyclerView recyclerView;

    private final HistoryAdapter adapter = new HistoryAdapter(this);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        YTransApp.getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_history_list, container, false);
        unbinder = ButterKnife.bind(this, root);
        presenter.bindView(this);

        initUI(root);
        return root;
    }

    private void initUI(View root) {
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);

        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateData();
    }


    @Override
    public void onItemSelected(String word, String direction) {
        ((MainActivity)getActivity()).setFragment(false, TranslateFragment.newInstance(word, direction));
    }

    @Override
    public void onStarredChanged(String word, String direction, boolean newStarredCurrent) {
        presenter.setWordStarredState(word, direction, newStarredCurrent);
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

    @Override
    public void updateData() {
        presenter.requestData(false);
    }
}
