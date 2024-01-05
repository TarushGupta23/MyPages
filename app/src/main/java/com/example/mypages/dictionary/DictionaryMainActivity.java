package com.example.mypages.dictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypages.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DictionaryMainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    EditText searchField;
    CardView searchBtn, phoneticsBtn;
    TextView textViewPhonetics;
    String audioLink = "";
    private final MediaPlayer mediaPlayer = new MediaPlayer();
    private AudioAttributes audioAttributes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_main);

        recyclerView = findViewById(R.id.dictionaryRecyclerView);
        searchField = findViewById(R.id.editTextDictionarySearchField);
        searchBtn = findViewById(R.id.dictionarySearchButton);
        phoneticsBtn = findViewById(R.id.dictionaryPhoneticsBtn);
        textViewPhonetics = findViewById(R.id.textViewDictionaryPhonetics);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new DictionaryAdapter(new ArrayList<>()));

        searchBtn.setOnClickListener(view -> {
            String word = searchField.getText().toString();
            if (word.equals("")) {
                Toast.makeText(this, "Enter a word to search", Toast.LENGTH_SHORT).show();
            } else {
                searchMeaning(word);
            }
        });

        audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        mediaPlayer.setAudioAttributes(audioAttributes);

        phoneticsBtn.setOnClickListener(view -> {
            if (audioLink.equals("")) {
                Toast.makeText(this, "Audio not available", Toast.LENGTH_SHORT).show();
            } else {
                mediaPlayer.reset();
                try {
                    mediaPlayer.setDataSource(audioLink);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void searchMeaning(String word) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.dictionaryapi.dev/api/v2/entries/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitApi api = retrofit.create(RetrofitApi.class);
        Call<List<Root>> call = api.getData(word);

        call.enqueue(new Callback<List<Root>>() {
            @Override
            public void onResponse(Call<List<Root>> call, Response<List<Root>> response) {
                if (response.isSuccessful()) {
                    Root data = response.body().get(0);
                    if (data == null) {
                        Toast.makeText(DictionaryMainActivity.this, "Data is null", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String phoneticText = "";
                    ArrayList<Phonetic> phonetics = data.phonetics;
                    if (phonetics != null && !phonetics.isEmpty()) {
                        if (phonetics.size() > 1) {
                            phoneticText = phonetics.get(1).text;
                            audioLink = phonetics.get(1).audio;
                        } else {
                            phoneticText = phonetics.get(0).text;
                            audioLink = phonetics.get(0).audio;
                        }
                    }
                    textViewPhonetics.setText(phoneticText);

                    recyclerView.setAdapter(new DictionaryAdapter(data.meanings));
                } else {
                    Toast.makeText(DictionaryMainActivity.this, "Meaning not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Root>> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}