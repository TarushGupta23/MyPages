package com.example.mypages.simpleList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mypages.R;
import com.example.mypages.chartsRes.ModelChart;
import com.example.mypages.notes.Model_Note;
import com.example.mypages.notes.NotesEditActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SimpleListViewActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseAuth auth;
    DatabaseReference reference;
    List<DataHolder> itemList = new ArrayList<>();
    SimpleList_nameAdapter adapter = new SimpleList_nameAdapter(itemList, this);

    static class DataHolder {
        String key, name;
        DataHolder(String key, String name) {
            this.name = name;
            this.key = key;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_list_view);

        recyclerView = findViewById(R.id.simpleListView_recyclerView);
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid()).child("simpleList_folder");

        recyclerView.setAdapter(adapter);
        // TODO : orientation
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    String key = dataSnapshot.getKey();
                    String name = (String) dataSnapshot.child("listTitle").getValue();
                    itemList.add(new DataHolder(key, name));
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_action_add) {
            showDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void showDialog() {
        android.app.AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.text_input, null);

        EditText nameInput = dialogView.findViewById(R.id.dialogueTextInput_editText);
        Button btnOk = dialogView.findViewById(R.id.dialogueTextInput_buttonOk);
        Button btnCancel = dialogView.findViewById(R.id.dialogueTextInput_buttonCancel);

        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();

        btnCancel.setOnClickListener(view -> {
            alertDialog.dismiss();
        });

        btnOk.setOnClickListener(view -> {
            String listName = nameInput.getText().toString();
            if (listName.equals("")) {
                Toast.makeText(this, "Please enter list name", Toast.LENGTH_SHORT).show();
            } else {
                DatabaseReference newSlot = reference.push();
                Model_simpleListItem listItem = new Model_simpleListItem(listName, newSlot.getKey(), new ArrayList<>());
                newSlot.setValue(listItem);

                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }
}