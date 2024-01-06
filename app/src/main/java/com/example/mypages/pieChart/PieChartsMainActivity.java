package com.example.mypages.pieChart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mypages.R;
import com.example.mypages.chartsRes.ChartNameAdapter;
import com.example.mypages.chartsRes.ModelChart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class PieChartsMainActivity extends AppCompatActivity {
    ArrayList<ModelChart> chartList;
    RecyclerView recyclerView;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    ChartNameAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_charts_main);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid()).child("pieChart_folder");
        recyclerView = findViewById(R.id.piechartMain_recyclerView);
        chartList = new ArrayList<>();
        adapter = new ChartNameAdapter(chartList, this);
//        TODO: check orientation
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chartList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ModelChart item = dataSnapshot.getValue(ModelChart.class);
                    if (item != null) {
                        chartList.add(item);
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

    void showEditDialog() {
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
            String chartName = nameInput.getText().toString();
            if (chartName.equals("")) {
                Toast.makeText(this, "Please enter chart name", Toast.LENGTH_SHORT).show();
            } else {
                DatabaseReference newSlot = databaseReference.push();

                ModelChart newChart = new ModelChart();
                newChart.setName(chartName);
                newChart.setData(new HashMap<>());
                newChart.setChartType(ModelChart.PIE);
                newChart.setKey(newSlot.getKey());
                newChart.getData().put("Maths", 99.2);
                newChart.getData().put("Eng", 99.2);
                newChart.getData().put("Hindi", 99.2);

                newSlot.setValue(newChart);
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }
}