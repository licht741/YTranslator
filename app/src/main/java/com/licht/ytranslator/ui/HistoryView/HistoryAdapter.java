package com.licht.ytranslator.ui.HistoryView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.licht.ytranslator.R;
import com.licht.ytranslator.data.model.HistoryObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Адаптер для истории переводов (и списка избранных переводов) с поддержкой поиска по списку
 */
class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.WordViewHolder>
        implements Filterable {

    private final List<HistoryObject> items = new ArrayList<>();
    private final List<HistoryObject> filteredItems = new ArrayList<>();

    private TranslateFilter filter;

    private final IHistoryView view;

    HistoryAdapter(IHistoryView view) {
        super();
        this.view = view;
    }

    public void setData(List<HistoryObject> items) {
        this.items.clear();
        this.items.addAll(items);

        this.filteredItems.clear();
        this.filteredItems.addAll(items);

        notifyDataSetChanged();
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word_in_history, parent, false);
        return new WordViewHolder(v);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        final HistoryObject item = filteredItems.get(position);

        holder.tvPhrase.setText(item.getWord());
        holder.tvTrans.setText(item.getTranslate());
        holder.tvDirection.setText(item.getDirection());

        holder.ivIcon.setImageDrawable(null);

        holder.setIcon(item.isFavorites());

        holder.itemView.setOnClickListener(v ->
                view.onItemSelected(item.getWord(), item.getDirection()));

        holder.ivIcon.setOnClickListener(v -> {
            final boolean newStarredState = !items.get(position).isFavorites();

            holder.setIcon(newStarredState);
            view.onStarredChanged(item.getWord(), item.getDirection(), newStarredState);
            items.get(position).setFavorites(newStarredState);
        });
    }

    @Override
    public int getItemCount() {
        return filteredItems.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new TranslateFilter(this, items);

        return filter;
    }

    static class WordViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivIcon;
        final TextView tvPhrase;
        final TextView tvTrans;
        final TextView tvDirection;

        WordViewHolder(View itemView) {
            super(itemView);

            ivIcon = (ImageView) itemView.findViewById(R.id.iv_history_word_icon);
            tvPhrase = (TextView) itemView.findViewById(R.id.tv_history_word_phrase);
            tvTrans = (TextView) itemView.findViewById(R.id.tv_history_word_translate);
            tvDirection = (TextView) itemView.findViewById(R.id.tv_history_word_dir);
        }

        void setIcon(boolean isStarred) {
            if (isStarred)
                ivIcon.setImageResource(R.drawable.ic_star);
            else
                ivIcon.setImageResource(R.drawable.ic_bookmark);
        }
    }

    private static class TranslateFilter extends Filter {
        private final HistoryAdapter adapter;
        private final List<HistoryObject> originalList;
        private final List<HistoryObject> filteredList;

        private TranslateFilter(HistoryAdapter adapter, List<HistoryObject> items) {
            super();
            this.adapter = adapter;
            this.originalList = items;
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (constraint.length() > 0) {
                final String pattern = constraint.toString().toLowerCase().trim();

                // Выбираем переводы, где введённый текст встречается в тексте, или в переводе
                for (final HistoryObject historyObject : originalList)
                    if (historyObject.getWord().toLowerCase().contains(pattern) ||
                            historyObject.getTranslate().toLowerCase().contains(pattern))
                        filteredList.add(historyObject);
            } else
                filteredList.addAll(originalList);

            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.filteredItems.clear();
            adapter.filteredItems.addAll((List<HistoryObject>) results.values);
            adapter.notifyDataSetChanged();
        }
    }

}
