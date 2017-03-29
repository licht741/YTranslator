package com.licht.ytranslator.ui.DictionaryView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.licht.ytranslator.R;
import com.licht.ytranslator.data.model.WordObject;
import com.licht.ytranslator.data.model.ExampleObject;
import com.licht.ytranslator.data.model.StringWrapper;
import com.licht.ytranslator.data.model.WordMeaningObject;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {
    private WordObject wordObject;

    public WordAdapter(WordObject wordObject) {
        this.wordObject = wordObject;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
        WordMeaningObject wordMeaningObject = wordObject.getWordMeaningObjects().get(position);

        StringBuilder result = new StringBuilder();
        for (StringWrapper stringWrapper : wordMeaningObject.getMeanings())
            result.append(stringWrapper.getContent() + ",");
        if (result.length() > 0)
            result.deleteCharAt(result.length() - 1);

        return result.toString();
    }

    private String getSynonimes(int position) {
        WordMeaningObject wordMeaningObject = wordObject.getWordMeaningObjects().get(position);

        final StringBuilder result = new StringBuilder();
        result.append(wordMeaningObject.getText()).append(", ");
        for (StringWrapper stringWrapper : wordMeaningObject.getSynonimes())
            result.append(stringWrapper.getContent() + ", ");

        if (result.length() > 1)
            result.delete(result.length() - 2, result.length());

        return result.toString();
    }

    private String getExamples(int position) {
        final WordMeaningObject wordMeaningObject = wordObject.getWordMeaningObjects().get(position);

        final StringBuilder stringBuilder = new StringBuilder();
        for (ExampleObject exampleObject : wordMeaningObject.getExampleObjects()) {
            stringBuilder.append(exampleObject.getPhrase().getContent()).append("\u2014");
            for (StringWrapper s : exampleObject.getTranslates())
                stringBuilder.append(s.getContent()).append(";");
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    @Override
    public int getItemCount() {
        return wordObject.getWordMeaningObjects().size();
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
