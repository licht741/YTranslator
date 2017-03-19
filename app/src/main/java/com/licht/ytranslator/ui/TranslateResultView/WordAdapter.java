package com.licht.ytranslator.ui.TranslateResultView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.licht.ytranslator.R;
import com.licht.ytranslator.data.model.Dictionary;
import com.licht.ytranslator.data.model.Word;

import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {
    private Context context;
    private Word mWord;

    public WordAdapter(Word word) {
        mWord = word;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_word_in_dictionary, parent, false);

        return new WordViewHolder(v);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        holder.tvCardNumber.setText(String.valueOf(position));
//        holder.tvTranslating.setText(mWord.getDictionaries().get(position));
    }

    @Override
    public int getItemCount() {
        return mWord.getDictionaries().size();
    }

    private String buildStringList(Dictionary dictionary) {
        final StringBuilder stringBuilder = new StringBuilder();

//        for (String word: dictionary.)
//            stringBuilder.append(String.format("- %s\n", word));

        return stringBuilder.toString();
    }

    class WordViewHolder extends RecyclerView.ViewHolder {
        TextView tvCardNumber;
        TextView tvTranslating;
        TextView tvMeaning;

        WordViewHolder(View itemView) {
            super(itemView);

            tvCardNumber = (TextView) itemView.findViewById(R.id.item_word_num);
            tvTranslating = (TextView) itemView.findViewById(R.id.tv_dictionary_translating);
            tvMeaning = (TextView) itemView.findViewById(R.id.tv_dictionary_meaning);
        }

    }
}
