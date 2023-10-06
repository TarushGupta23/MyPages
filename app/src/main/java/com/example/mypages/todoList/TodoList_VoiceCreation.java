package com.example.mypages.todoList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypages.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TodoList_VoiceCreation extends AppCompatActivity {

    CardView micBtn;
    ListView listView;
    String result;
    Button save, cancel;
    TextView selected;
    DatabaseReference databaseReference;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list_voice_creation);

        micBtn = findViewById(R.id.todoListVoice_mic);
        listView = findViewById(R.id.todoListVoice_recyclerView);
        save = findViewById(R.id.todoListVoice_save);
        cancel = findViewById(R.id.todoListVoice_discard);
        selected = findViewById(R.id.textView26);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid()).child("todo_list_folder").child("active");

        micBtn.setOnClickListener(view -> {
            Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en-US");
            try {
                startActivityForResult(i, 1);
            } catch (Exception e) {
                Toast.makeText(this, "Unable to recognise", Toast.LENGTH_SHORT).show();
            }
        });

        cancel.setOnClickListener(view -> {finish();});
        save.setOnClickListener(view -> {
            if (result.equals("")) {
                Toast.makeText(this, "Nothing to save", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Model_todoList newItem = new Model_todoList(this);
                newItem.setTitle(result);
                newItem.setCompressed(true);
                databaseReference.push().setValue(newItem);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String [] arr = text.toArray(new String[0]);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            this,
                            android.R.layout.simple_list_item_1,
                            android.R.id.text1,
                            arr
                    );
                    listView.setAdapter(adapter);
                    result = arr[0];
                    selected.setText(result);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            result = arr[position];
                            selected.setText(result);
                        }
                    });
                }
                break;
        }
    }
}