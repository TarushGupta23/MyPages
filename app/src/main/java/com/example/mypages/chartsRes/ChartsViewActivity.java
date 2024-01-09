package com.example.mypages.chartsRes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.enums.HoverMode;
import com.anychart.graphics.vector.Stroke;
import com.anychart.core.cartesian.series.Column;
import com.example.mypages.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.enums.Position;
import com.anychart.enums.Anchor;
import com.anychart.data.Set;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChartsViewActivity extends AppCompatActivity {
    DatabaseReference reference;
    FirebaseAuth auth;
    ModelChart chart;
    AnyChartView anyChartView;
    TextView chartName;
    List<DataEntry> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts_view);

        Intent i = getIntent();
        String chartId = i.getStringExtra("chartId");
        String path = i.getStringExtra("chartPath");

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid()).child(path).child(chartId);

        data = new ArrayList<>();
        data.add(new ValueDataEntry("No data", 1));

        anyChartView = (AnyChartView) findViewById(R.id.anyChartView);
        chartName = findViewById(R.id.chartEditView_title);
        if (path.equals("pieChart_folder")) {
            showPieChart();
        } else {
            showBarChart();
        }
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
//            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void showPieChart() {
        final Pie pie = AnyChart.pie();
        pie.data(data);
        anyChartView.setChart(pie);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chart = snapshot.getValue(ModelChart.class);
                if (chart.getData() == null) {
                    chart.setData(new HashMap<>());
                }
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

    void showBarChart() {
        // update ho ra h but history bhi show ho rhi h
//        final Cartesian[] cartesian = {AnyChart.column()};
//        final Column[] column = {cartesian[0].column(data)};
//
//        column[0].tooltip()
//                .position(Position.CENTER_BOTTOM)
//                .anchor(Anchor.CENTER_BOTTOM)
//                .offsetX(0d)
//                .offsetY(5d)
//                .format("{%Value}{groupsSeparator: }")
//                .titleFormat("{%X}");
//
//        cartesian[0].animation(true);
//        cartesian[0].title("");
//
//        cartesian[0].yScale().minimum(0d);
//
//        cartesian[0].yAxis(0).labels().format("{%Value}{groupsSeparator: }");
//
//        cartesian[0].tooltip().positionMode(TooltipPositionMode.POINT);
//        cartesian[0].interactivity().hoverMode(HoverMode.BY_X);
//
//        cartesian[0].xAxis(0).title("Field");
//        cartesian[0].yAxis(0).title("Value");

        final Cartesian cartesian = AnyChart.column();
        final Column column = cartesian.column(data);

        column.tooltip()
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }")
                .titleFormat("{%X}");

        cartesian.animation(true);
        cartesian.title("");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Field");
        cartesian.yAxis(0).title("Value");

        anyChartView.setChart(cartesian);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chart = snapshot.getValue(ModelChart.class);
                if (chart.getData() == null) {
                    chart.setData(new HashMap<>());
                }
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

                final Cartesian cartesian2 = AnyChart.column();
                final Column column2 = cartesian2.column(updatedData);

                column2.tooltip()
                        .position(Position.CENTER_BOTTOM)
                        .anchor(Anchor.CENTER_BOTTOM)
                        .offsetX(0d)
                        .offsetY(5d)
                        .format("{%Value}{groupsSeparator: }")
                        .titleFormat("{%X}");

                cartesian2.animation(true);
                cartesian2.title("");

                cartesian2.yScale().minimum(0d);

                cartesian2.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

                cartesian2.tooltip().positionMode(TooltipPositionMode.POINT);
                cartesian2.interactivity().hoverMode(HoverMode.BY_X);

                cartesian2.xAxis(0).title("Field");
                cartesian2.yAxis(0).title("Value");

                anyChartView.setChart(cartesian2);
                Toast.makeText(ChartsViewActivity.this, "Cartisian set on anychart", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    void showLineChart() {
//        final Cartesian cartesian = AnyChart.line();
//        cartesian.animation(true);
//        cartesian.padding(10d, 20d, 5d, 20d);
//
//        cartesian.crosshair().enabled(true);
//        cartesian.crosshair()
//                .yLabel(true)
//                .yStroke((Stroke) null, null, null, (String) null, (String) null);
//
//        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
//        Set set = Set.instantiate();
//        set.data(data);
//
    }
}