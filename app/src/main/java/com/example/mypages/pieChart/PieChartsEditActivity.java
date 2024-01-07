package com.example.mypages.pieChart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.mypages.R;
import com.example.mypages.chartsRes.ChartEditDialogRecyclerView;
import com.example.mypages.chartsRes.ChartsEditActivity;
import com.example.mypages.chartsRes.ModelChart;
import com.example.mypages.notes.Model_Note;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PieChartsEditActivity extends AppCompatActivity {
    DatabaseReference reference;
    FirebaseAuth auth;
    ModelChart chart;
    AnyChartView anyChartView;
    TextView chartName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_charts_edit);

        final Pie pie = AnyChart.pie();

        Intent i = getIntent();
        String chartId = i.getStringExtra("chartId");

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid()).child("pieChart_folder").child(chartId);

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("No data", 1));

        pie.data(data);
        anyChartView = (AnyChartView) findViewById(R.id.any_chart_view);
        anyChartView.setChart(pie);
        chartName = findViewById(R.id.pieChartEditView_title);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chart = snapshot.getValue(ModelChart.class);
                chartName.setText(chart.getName());
                List<DataEntry> updatedData = new ArrayList<>();
                for (Map.Entry<String, Double> entry : chart.getData().entrySet()) {
                    String key = entry.getKey();
                    double value = entry.getValue();
                    updatedData.add(new ValueDataEntry(key, value));
                }
                if (updatedData.size() == 0) {
                    updatedData.add(new ValueDataEntry("No data", 1));
                }
                pie.data(updatedData);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_plus_icon, menu);
        MenuItem item = menu.findItem(R.id.menu_action_add);
        item.setIcon(R.drawable.baseline_edit_note_24);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_action_add) {
            Intent i = new Intent(this, ChartsEditActivity.class);
            i.putExtra("chart", chart);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}