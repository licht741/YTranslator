package com.licht.ytranslator.ui.HistoryView;


import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

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

public class HistoryListFragment extends Fragment implements IHistoryView, SearchView.OnQueryTextListener {

    private Unbinder unbinder;

    @Inject
    HistoryPresenter presenter;

    @BindView(R.id.rv_history_list) RecyclerView recyclerView;
    @BindView(R.id.view_no_content) RelativeLayout noContentView;

    private SearchView searchView;

    private final HistoryAdapter adapter = new HistoryAdapter(this);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        YTransApp.getAppComponent().inject(this);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_history_list, container, false);
        unbinder = ButterKnife.bind(this, root);
        presenter.bindView(this);
        initUI(root);

        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("search", searchView.getQuery().toString());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null || !savedInstanceState.containsKey("search"))
            return;

        final String search = savedInstanceState.getString("search");
        if ("".equals(search))
            return;

        searchView.post(() -> {
            searchView.onActionViewExpanded();
            searchView.setQuery(search, false);
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_history, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(this);

        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
        searchEditText.setHintTextColor(ContextCompat.getColor(getContext(), android.R.color.white));

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.history_title);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return false;
    }

    private void initUI(View root) {
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(getContext(), android.R.color.white));

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);

        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if (searchView != null)
                    searchView.onActionViewCollapsed();
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.requestData(false);
    }

    @Override
    public void onItemSelected(String word, String direction) {
        ((MainActivity) getActivity()).setFragment(TranslateFragment.newInstance(word, direction));
    }

    @Override
    public void onStarredChanged(String word, String direction, boolean newStarredCurrent) {
        presenter.setWordStarredState(word, direction);
    }

    @Override
    public void setData(List<HistoryObject> items) {
        if (items.size() == 0) {
            noContentView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noContentView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        adapter.setData(items);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_history_clear)
            showClearHistoryDialog();

        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.unbindView();
    }

    private void showClearHistoryDialog() {
        if (adapter.getItemCount() > 0)
            new AlertDialog
                    .Builder(getActivity()).setMessage(R.string.history_clear)
                    .setPositiveButton(R.string.yes, ((dialog, which) ->
                            presenter.clearHistory(false)))
                    .setNegativeButton(R.string.no, null).create()
                    .show();

    }

}
