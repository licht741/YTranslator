package com.licht.ytranslator.ui.HistoryView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.licht.ytranslator.R;
import com.licht.ytranslator.data.model.HistoryItem;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.WordViewHolder> {

    private final List<HistoryItem> items = new ArrayList<>();

    public HistoryAdapter() {
        super();
    }

    public void setData(List<HistoryItem> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word_in_history, parent, false);
        return new WordViewHolder(v);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        final HistoryItem item = items.get(position);

        holder.tvPhrase.setText(item.getWord());
        holder.tvTrans.setText(item.getTranslate());
        holder.tvDirection.setText(item.getDirection());

        if (item.isFavorites())
            holder.ivIcon.setImageResource(R.drawable.ic_star);
        else
            holder.ivIcon.setImageResource(R.drawable.ic_bookmark);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class WordViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvPhrase;
        TextView tvTrans;
        TextView tvDirection;

        public WordViewHolder(View itemView) {
            super(itemView);

            ivIcon = (ImageView) itemView.findViewById(R.id.iv_history_word_icon);
            tvPhrase = (TextView) itemView.findViewById(R.id.tv_history_word_phrase);
            tvTrans = (TextView) itemView.findViewById(R.id.tv_history_word_translate);
            tvDirection = (TextView) itemView.findViewById(R.id.tv_history_word_dir);
        }
    }
}
