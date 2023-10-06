package com.example.mypages.tallycounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mypages.ColorPickClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.mypages.R;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class tallyCounter_MainActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    private Adapter_tallyCounterRecyclerView adapter;
    private List<Model_tallyCounter> itemList;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Tally Counter");
        setContentView(R.layout.activity_tally_counter_main);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid()).child("tally_counter_folder");

        itemList = new ArrayList<>();
        adapter = new Adapter_tallyCounterRecyclerView(itemList);

        recyclerView = findViewById(R.id.tallyCounterMain_recyclerView);
        recyclerView.setAdapter(adapter);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        // Set up ValueEventListener to listen for data changes
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String key = dataSnapshot.getKey();
                    Model_tallyCounter item = dataSnapshot.getValue(Model_tallyCounter.class);
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
            showEditDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showEditDialog() {
        Model_tallyCounter counter = new Model_tallyCounter(0, "", false, getApplicationContext());

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(tallyCounter_MainActivity.this);
        LayoutInflater inflater = LayoutInflater.from(tallyCounter_MainActivity.this);
        View dialogView = inflater.inflate(R.layout.edit_tally_counter_dialogue, null);
        EditText name, value;
        Button lock, save, discard;
        CardView colorPrim, colorSecond;

        name = dialogView.findViewById(R.id.tallyCounterDialogue_name);
        value = dialogView.findViewById(R.id.tallyCounterDialogue_count);
        lock = dialogView.findViewById(R.id.tallyCounterDialogue_lockButton);
        save = dialogView.findViewById(R.id.tallyCounterDialogue_saveButton);
        discard = dialogView.findViewById(R.id.tallyCounterDialogue_discardButton);
        colorPrim = dialogView.findViewById(R.id.todoListCreateDialogue_colorPrimary);
        colorSecond = dialogView.findViewById(R.id.todoListCreateDialogue_colorSecondary);

        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();

        save.setOnClickListener(v -> {
            if (name.getText().toString().equals("")) {
                Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show();
            } else if (colorPrim.getCardBackgroundColor().equals(colorSecond.getCardBackgroundColor())) {
                Toast.makeText(this, "Primary and Secondary colors should not be same", Toast.LENGTH_SHORT).show();
            } else {
                counter.setCounterName(name.getText().toString());
                try {
                    counter.setCount(Integer.parseInt(value.getText().toString()));
                } catch (Exception e) {
                    counter.setCount(0);
                }
                counter.setPrimaryColor(colorPrim.getCardBackgroundColor().getDefaultColor());
                counter.setSecondaryColor(colorSecond.getCardBackgroundColor().getDefaultColor());
                alertDialog.dismiss();
                databaseReference.push().setValue(counter);
            }
        });

        discard.setOnClickListener(view -> {
            alertDialog.dismiss();
        });

        lock.setOnClickListener(view -> {
            if (counter.isLocked()) {
                Toast.makeText(tallyCounter_MainActivity.this, "Counter unlocked", Toast.LENGTH_SHORT).show();
                counter.setLocked(false);
            } else {
                Toast.makeText(tallyCounter_MainActivity.this, "Counter locked", Toast.LENGTH_SHORT).show();
                counter.setLocked(true);
            }
        });

        colorPrim.setOnClickListener(view -> {
            ColorPickClass colorPickClass = new ColorPickClass(
                    this, ContextCompat.getColor(getApplicationContext(), R.color.deepBlue3), counter.getPrimaryColor(),
                    new ColorPickClass.OnSaveClickListener() {
                        @Override
                        public void onSaveClicked(int newColor) {
                            counter.setPrimaryColor(newColor);
                            colorPrim.setCardBackgroundColor(newColor);
                        }
                    },
                    new ColorPickClass.OnDefaultClickListener() {
                        @Override
                        public void onDefaultClicked(int color) {
                            counter.setPrimaryColor(color);
                            colorPrim.setCardBackgroundColor(color);
                        }
                    },
                    new ColorPickClass.OnCancelClickListener() {
                        @Override
                        public void onCancelClicked(int color) {
                            counter.setPrimaryColor(color);
                            colorPrim.setCardBackgroundColor(color);
                        }
                    }
            );
            colorPickClass.show();
        });

        colorSecond.setOnClickListener(view -> {
            ColorPickClass colorPickClass = new ColorPickClass(
                    this, ContextCompat.getColor(getApplicationContext(), R.color.ice), counter.getSecondaryColor(),
                    new ColorPickClass.OnSaveClickListener() {
                        @Override
                        public void onSaveClicked(int newColor) {
                            counter.setSecondaryColor(newColor);
                            colorSecond.setCardBackgroundColor(newColor);
                        }
                    },
                    new ColorPickClass.OnDefaultClickListener() {
                        @Override
                        public void onDefaultClicked(int color) {
                            counter.setSecondaryColor(color);
                            colorSecond.setCardBackgroundColor(color);
                        }
                    },
                    new ColorPickClass.OnCancelClickListener() {
                        @Override
                        public void onCancelClicked(int color) {
                            counter.setSecondaryColor(color);
                            colorSecond.setCardBackgroundColor(color);
                        }
                    }
            );
            colorPickClass.show();
        });

        alertDialog.show();
    }

}
