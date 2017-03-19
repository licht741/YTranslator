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

public class LanguageListAdapter extends RecyclerView.Adapter<LanguageListAdapter.LanguageViewHolder> {

    private ISelectLanguageView view;
    private ArrayList<String> mLanguages;
    private String currentSelectedLanguage;
    private Context context;

    public LanguageListAdapter(ISelectLanguageView view, ArrayList<String> mLanguages, String currentSelectedLanguage) {
        this.view = view;
        this.mLanguages = mLanguages;
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
        if (lang.equals(currentSelectedLanguage)) {
            holder.mIsSelected.setImageResource(R.drawable.ic_selected);
            holder.itemView.setBackground(ContextCompat.getDrawable(context, R.color.black_overlay));
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

    class LanguageViewHolder extends RecyclerView.ViewHolder {
        TextView mLanguage;
        ImageView mIsSelected;

        LanguageViewHolder(View itemView) {
            super(itemView);

            mLanguage = (TextView) itemView.findViewById(R.id.tv_language);
            mIsSelected = (ImageView) itemView.findViewById(R.id.iv_is_selected);
        }

    }
}
