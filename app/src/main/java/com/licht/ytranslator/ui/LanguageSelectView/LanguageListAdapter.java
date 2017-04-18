package com.licht.ytranslator.ui.LanguageSelectView;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.licht.ytranslator.R;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

class LanguageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements Filterable {

    private final ISelectLanguageView view;

    private final List<ILanguageAdapterItem> mLanguages = new ArrayList<>();
    private final List<ILanguageAdapterItem> mFilteredItems = new ArrayList<>();

    private final String currentSelectedLanguage;
    private Context context;

    private Filter filter;

    private final int recentlyUsedLanguagesCount;

    LanguageListAdapter(ISelectLanguageView view,
                        List<String> allLanguages,
                        List<String> recentlyUsedLanguages,
                        String currentSelectedLanguage) {
        this.view = view;

        recentlyUsedLanguagesCount = recentlyUsedLanguages.size();

        final List<ILanguageAdapterItem> items =
                getFormattedItems(allLanguages, recentlyUsedLanguages);

        mLanguages.addAll(items);
        mFilteredItems.addAll(items);

        this.currentSelectedLanguage = currentSelectedLanguage;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        if (viewType == ILanguageAdapterItem.LANGUAGE_ITEM_TYPE) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_language_for_select, parent, false);
            return new LanguageViewHolder(v);
        }

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_select_languages_title, parent, false);
        return new TitleViewHolder(v);

    }

    @Override
    public int getItemViewType(int position) {
        return mFilteredItems.get(position).getItemType();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ILanguageAdapterItem.TITLE_ITEM_TYPE) {
            final TitleViewHolder titleViewHolder = (TitleViewHolder)holder;
            titleViewHolder.mTitle
                    .setText(((TitleItem)mFilteredItems.get(position)).getContent());
            return;
        }

        final String lang = ((LanguageItem)mFilteredItems.get(position)).getContent();
        final LanguageViewHolder languageViewHolder = (LanguageViewHolder) holder;
        languageViewHolder.mLanguage.setText(lang);

        // Отдельно помечаем текущий язык
        // Не нужно отдельно отмечать язык, находящийся в недавно использованных
        // Поэтому по индексу проверяем, где находится данный элемент
        // Если он относится к "недавно использованным", то не помечаем
        if (lang.equals(currentSelectedLanguage) && (position > recentlyUsedLanguagesCount + 1)) {
            languageViewHolder.mIsSelected.setVisibility(View.VISIBLE);
            languageViewHolder.mIsSelected.setImageResource(R.drawable.ic_selected);
            languageViewHolder.itemView.setBackground(ContextCompat.getDrawable(context, R.color.colorPrimaryLight));
        } else {
            languageViewHolder.mIsSelected.setVisibility(View.GONE);
            languageViewHolder.itemView
                    .setBackground(ContextCompat.getDrawable(context, android.R.color.background_light));
        }

        holder.itemView.setOnClickListener(v -> view.sendResult(lang));
    }

    @Override
    public int getItemCount() {
        return mFilteredItems.size();
    }

    static class LanguageViewHolder extends RecyclerView.ViewHolder {
        final TextView mLanguage;
        final ImageView mIsSelected;

        LanguageViewHolder(View itemView) {
            super(itemView);
            mLanguage = (TextView) itemView.findViewById(R.id.tv_language);
            mIsSelected = (ImageView) itemView.findViewById(R.id.iv_is_selected);
        }
    }

    static class TitleViewHolder extends RecyclerView.ViewHolder {
        final TextView mTitle;

        TitleViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }

    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new LanguageFilter(this, mLanguages);

        return filter;
    }

    private static class LanguageFilter extends Filter {
        private final LanguageListAdapter adapter;
        private final List<ILanguageAdapterItem> originalList;
        private final List<ILanguageAdapterItem> filteredList;

        LanguageFilter(LanguageListAdapter adapter,
                       List<ILanguageAdapterItem> originalList) {
            this.adapter = adapter;
            this.originalList = originalList;
            this.filteredList = new ArrayList<>();
        }


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();

            // Может возникнуть ситуация, что мы уже добавили язык, который находится в "Недавнем"
            // а теперь встречаем его во второй раз, и пытаемся добавить. Отдельно проверяем этот случай
            final SortedSet<String> addedLanguages = new TreeSet<>();

            if (constraint.length() > 0) {
                final String pattern = constraint.toString().toLowerCase().trim();
                for (final ILanguageAdapterItem language : originalList) {
                    if (language.getItemType() == ILanguageAdapterItem.TITLE_ITEM_TYPE)
                        continue;
                    LanguageItem item = (LanguageItem) language;

                    if (item.getContent().toLowerCase().contains(pattern)
                            && !addedLanguages.contains(item.getContent())) {
                        addedLanguages.add(item.getContent());
                        filteredList.add(language);
                    }

                }
            } else
                filteredList.addAll(originalList);

            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.mFilteredItems.clear();
            adapter.mFilteredItems.addAll((List<ILanguageAdapterItem>) results.values);
            adapter.notifyDataSetChanged();
        }
    }

    private List<ILanguageAdapterItem> getFormattedItems(List<String> allLanguages,
                                                         List<String> recentlyLanguages) {
        List<ILanguageAdapterItem> items = new ArrayList<>();

        if (recentlyLanguages.size() > 0)
            items.add(new TitleItem("Недавно использованные"));

        for (String language : recentlyLanguages)
            items.add(new LanguageItem(language));

        if (recentlyLanguages.size() > 0)
            items.add(new TitleItem("Все языки"));

        for (String language : allLanguages)
            items.add(new LanguageItem(language));

        return items;
    }
}
