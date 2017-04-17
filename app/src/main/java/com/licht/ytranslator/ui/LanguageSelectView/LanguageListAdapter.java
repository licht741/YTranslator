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

class LanguageListAdapter extends RecyclerView.Adapter<LanguageListAdapter.LanguageViewHolder>
        implements Filterable{

    private final ISelectLanguageView view;

    private final List<String> mLanguages = new ArrayList<>();
    private final List<String> mFilteredItems = new ArrayList<>();

    private final String currentSelectedLanguage;
    private Context context;

    private Filter filter;

    LanguageListAdapter(ISelectLanguageView view, List<String> languages, String currentSelectedLanguage) {
        this.view = view;
        mLanguages.addAll(languages);
        mFilteredItems.addAll(languages);

        this.currentSelectedLanguage = currentSelectedLanguage;
    }

    @Override
    public LanguageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_language_for_select, parent, false);

        return new LanguageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LanguageViewHolder holder, int position) {
        final String lang = mFilteredItems.get(position);

        holder.mLanguage.setText(lang);

        // Отдельно помечаем текущий язык
        if (lang.equals(currentSelectedLanguage)) {
            holder.mIsSelected.setVisibility(View.VISIBLE);
            holder.mIsSelected.setImageResource(R.drawable.ic_selected);
            holder.itemView.setBackground(ContextCompat.getDrawable(context, R.color.colorPrimaryLight));
        } else {
            holder.mIsSelected.setVisibility(View.GONE);
            holder.itemView.setBackground(ContextCompat.getDrawable(context, android.R.color.background_light));
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

    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new LanguageFilter(this, mLanguages);

        return filter;
    }

    private static class LanguageFilter extends Filter {
        private final LanguageListAdapter adapter;
        private final List<String> originalList;
        private final List<String> filteredList;

        LanguageFilter(LanguageListAdapter adapter,
                              List<String> originalList) {
            this.adapter = adapter;
            this.originalList = originalList;
            this.filteredList = new ArrayList<>();
        }


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (constraint.length() > 0) {
                final String pattern = constraint.toString().toLowerCase().trim();
                for (final String language : originalList)
                    if (language.toLowerCase().contains(pattern))
                        filteredList.add(language);

            } else
                filteredList.addAll(originalList);

            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.mFilteredItems.clear();
            adapter.mFilteredItems.addAll((List<String>) results.values);
            adapter.notifyDataSetChanged();
        }
    }
}
