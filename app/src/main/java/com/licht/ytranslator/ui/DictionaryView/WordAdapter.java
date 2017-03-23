package com.licht.ytranslator.ui.DictionaryView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.licht.ytranslator.R;
import com.licht.ytranslator.data.model.Dictionary;
import com.licht.ytranslator.data.model.Example;
import com.licht.ytranslator.data.model.StringWrapper;
import com.licht.ytranslator.data.model.Translate;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {
    private Context context;
    private Dictionary dictionary;

    public WordAdapter(Dictionary dictionary) {
        this.dictionary = dictionary;
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

        String meaning = getMeanings(position);
        String syn = getSynonimes(position);
        String ex = getExamples(position);

        if (meaning.length() > 0)
            holder.tvMeaning.setText(meaning);
        else
            holder.tvMeaning.setVisibility(View.GONE);

        if (syn.length() > 0)
            holder.tvTranslating.setText(syn);
        else
            holder.tvTranslating.setVisibility(View.GONE);

        if (ex.length() > 0)
            holder.tvExamples.setText(ex);
        else
            holder.tvExamples.setVisibility(View.GONE);
    }

    private String getMeanings(int position) {
        Translate translate = dictionary.getTranslates().get(position);

        StringBuilder result = new StringBuilder();
        for (StringWrapper stringWrapper : translate.getMeanings())
            result.append(stringWrapper.getContent() + ",");
        if (result.length() > 0)
            result.deleteCharAt(result.length() - 1);

        return result.toString();
    }

    private String getSynonimes(int position) {
        Translate translate = dictionary.getTranslates().get(position);

        final StringBuilder result = new StringBuilder();
        result.append(translate.getText()).append(", ");
        for (StringWrapper stringWrapper : translate.getSynonimes())
            result.append(stringWrapper.getContent() + ", ");

        if (result.length() > 1)
            result.delete(result.length() - 2, result.length());

        return result.toString();
    }

    private String getExamples(int position) {
        final Translate translate = dictionary.getTranslates().get(position);

        final StringBuilder stringBuilder = new StringBuilder();
        for (Example example : translate.getExamples()) {
            stringBuilder.append(example.getPhrase().getContent()).append("\u2014");
            for (StringWrapper s : example.getTranslates())
                stringBuilder.append(s.getContent()).append(";");
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    @Override
    public int getItemCount() {
        return dictionary.getTranslates().size();
    }

    class WordViewHolder extends RecyclerView.ViewHolder {
        TextView tvCardNumber;
        TextView tvTranslating;
        TextView tvMeaning;
        TextView tvExamples;

        WordViewHolder(View itemView) {
            super(itemView);

            tvCardNumber = (TextView) itemView.findViewById(R.id.item_word_num);
            tvTranslating = (TextView) itemView.findViewById(R.id.tv_dictionary_translating);
            tvMeaning = (TextView) itemView.findViewById(R.id.tv_dictionary_meaning);
            tvExamples = (TextView) itemView.findViewById(R.id.tv_dictionary_examples);
        }

    }
}
