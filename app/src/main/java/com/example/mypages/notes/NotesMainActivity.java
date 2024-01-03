package com.example.mypages.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mypages.R;
import com.example.mypages.tallycounter.Model_tallyCounter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotesMainActivity extends AppCompatActivity {
    DatabaseReference databaseRef;
    RecyclerView recyclerView;
    private Adapter_notes adapter;
    private List<Model_Note> itemList;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Notes");
        setContentView(R.layout.activity_notes_main);

        auth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid()).child("notes_folder");
        recyclerView = findViewById(R.id.recyclerView_cardMain);

        itemList = new ArrayList<>();
        adapter = new Adapter_notes(itemList, NotesMainActivity.this);
        recyclerView.setAdapter(adapter);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList.clear();
                for (DataSnapshot snap: snapshot.getChildren()) {
                    String key = snap.getKey();
                    Model_Note item = snap.getValue(Model_Note.class);
                    if (item != null) {
                        item.setKey(key);
                        itemList.add(item);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_plus_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_action_add) {
            DatabaseReference newSlot = databaseRef.push();
            Model_Note note = new Model_Note(new Date(), newSlot.getKey());
            newSlot.setValue(note);

            Intent i = new Intent(this, NotesEditActivity.class);
            i.putExtra("note", note);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}