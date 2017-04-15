package com.licht.ytranslator.ui.LanguageSelectView;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.licht.ytranslator.R;

import java.util.ArrayList;
import java.util.List;

class LanguageListAdapter extends RecyclerView.Adapter<LanguageListAdapter.LanguageViewHolder> {

    private final ISelectLanguageView view;
    private final List<String> mLanguages;
    private final String currentSelectedLanguage;
    private Context context;

    LanguageListAdapter(ISelectLanguageView view, List<String> languages, String currentSelectedLanguage) {
        this.view = view;
        this.mLanguages = languages;
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
        final String lang = mLanguages.get(position);

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
        return mLanguages.size();
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
}
