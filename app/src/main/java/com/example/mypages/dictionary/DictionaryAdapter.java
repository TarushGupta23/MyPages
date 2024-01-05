package com.example.mypages.dictionary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypages.R;

import java.util.ArrayList;

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.DictionaryViewHolder> {
    ArrayList<Meaning> meanings;

    public DictionaryAdapter(ArrayList<Meaning> meanings) {this.meanings = meanings;}

    @NonNull
    @Override
    public DictionaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dictionary_meaning_card, parent, false);
        return new DictionaryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DictionaryViewHolder holder, int position) {
        holder.bind(meanings.get(position));
    }

    @Override
    public int getItemCount() {
        return meanings.size();
    }

    public static class DictionaryViewHolder extends RecyclerView.ViewHolder{
        TextView partOfSpeech, definition, synonyms, antonyms;

        public DictionaryViewHolder(@NonNull View itemView) {
            super(itemView);
            partOfSpeech = itemView.findViewById(R.id.textView_dictionaryMeaning_partOfSpeech);
            definition = itemView.findViewById(R.id.textView_dictionaryMeaning_definition);
            synonyms = itemView.findViewById(R.id.textView_dictionaryMeaning_synonyms);
            antonyms = itemView.findViewById(R.id.textView_dictionaryMeaning_antonyms);
        }

        public void bind(Meaning meaning) {
            partOfSpeech.setText(meaning.partOfSpeech);
            String def = meaning.definitions.get(0).definition;
            definition.setText(def);

            if (meaning.synonyms == null ) {
                meaning.synonyms = new ArrayList<>();
            }
            if (meaning.antonyms == null) {
                meaning.antonyms = new ArrayList<>();
            }
            StringBuilder sb = new StringBuilder();
            for (String s: meaning.synonyms) {
                sb.append(s);
                sb.append(",  ");
            }
            synonyms.setText(sb.toString());

            sb = new StringBuilder();
            for (String s: meaning.antonyms) {
                sb.append(s);
                sb.append(",  ");
            }
            antonyms.setText(sb.toString());
        }
    }
}
