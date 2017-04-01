package com.licht.ytranslator.ui.HistoryView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.licht.ytranslator.R;
import com.licht.ytranslator.data.model.HistoryObject;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.WordViewHolder> {

    private List<HistoryObject> items = new ArrayList<>();

    private IHistoryView view;

    public HistoryAdapter(IHistoryView view) {
        super();
        this.view = view;
    }

    public void setData(List<HistoryObject> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word_in_history, parent, false);
        return new WordViewHolder(v);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        final HistoryObject item = items.get(position);

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
            view.onStarredChanged(item.getWord(),
                    item.getDirection(),
                    newStarredState);
            items.get(position).setFavorites(newStarredState);
        });
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

        public void setIcon(boolean isStarred) {
            if (isStarred)
                ivIcon.setImageResource(R.drawable.ic_star);
            else
                ivIcon.setImageResource(R.drawable.ic_bookmark);
        }
    }
}
