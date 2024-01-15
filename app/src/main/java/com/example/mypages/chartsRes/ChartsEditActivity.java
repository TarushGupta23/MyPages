package com.example.mypages.chartsRes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mypages.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChartsEditActivity extends AppCompatActivity {
    Button save, cancel, addData;
    EditText title;
    RecyclerView recyclerView;
    DatabaseReference reference;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts_edit);

        save = findViewById(R.id.button_chartEditDialog_save);
        cancel = findViewById(R.id.button_chartEditDialog_cancel);
        addData = findViewById(R.id.button_chartEditDialog_addData);
        title = findViewById(R.id.editText_chartEditDialog_ChartName);
        recyclerView = findViewById(R.id.recyclerView_chartEditDialog_recyclerView);

        Intent i = getIntent();
        ModelChart chart = (ModelChart) i.getSerializableExtra("chart");

        String path = ModelChart.getChartPath(chart);
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid()).child(path).child(chart.getKey());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ChartEditDialogRecyclerView(reference, this));
        title.setText(chart.getName());

        save.setOnClickListener(view -> {
            if (title.getText().toString().equals("")) {
                Toast.makeText(this, "Please enter card title", Toast.LENGTH_SHORT).show();
            } else {
                reference.child("name").setValue(title.getText().toString());
//                TODO - try restarting charts activity to refresh
                Intent intent = new Intent(this, ChartsViewActivity.class);
                intent.putExtra("chartPath", path);
                intent.putExtra("chartId", chart.getKey());
                startActivity(intent);
                finish();
            }
        });

        addData.setOnClickListener(view -> {
            reference.child("data").child("FieldName").setValue(0.0);
            recyclerView.setAdapter(new ChartEditDialogRecyclerView(reference, this));
        });

        cancel.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChartsViewActivity.class);
            intent.putExtra("chartPath", path);
            intent.putExtra("chartId", chart.getKey());
            startActivity(intent);
            finish();
        });
    }
}