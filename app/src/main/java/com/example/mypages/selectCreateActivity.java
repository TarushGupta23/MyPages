package com.example.mypages;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.example.mypages.calculator.CalculatorMainActivity;
import com.example.mypages.notes.NotesMainActivity;
import com.example.mypages.selectCreateRes.Adapter_selectCreate;
import com.example.mypages.selectCreateRes.Model_selectCreate;
import com.example.mypages.tallycounter.tallyCounter_MainActivity;
import com.example.mypages.todoList.TodoListMainActivity;

import java.util.ArrayList;

public class selectCreateActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private ArrayList<Model_selectCreate> arrayList;
    private Adapter_selectCreate adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Select Category");

        setContentView(R.layout.activity_select_create);
        recyclerView = findViewById(R.id.selectCreate_recyclerView);


        // to change number of columns based on orientation
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }

        // hideShowActionBar();

        Model_selectCreate todoList, notes, pieChart, barGraph, simpleLists, tables, lineGraphs, tallycounter, dictionary, flowChart, ppt, calculator, physics, paint;
        todoList = new Model_selectCreate("Todo List", R.drawable.todo_list_icon, TodoListMainActivity.class);
        notes = new Model_selectCreate("Notes", R.drawable.notes_icon, NotesMainActivity.class);
        pieChart = new Model_selectCreate("Pie Chart", R.drawable.pie_chart_icon);
        barGraph = new Model_selectCreate("Bar Graph", R.drawable.bar_graph_icon);
        simpleLists = new Model_selectCreate("List", R.drawable.list_icon);
        tables = new Model_selectCreate("Table", R.drawable.table_icon);
        lineGraphs = new Model_selectCreate("Line Graph", R.drawable.line_chart_icon);
        tallycounter = new Model_selectCreate("Tally Counter", R.drawable.tallycounter_icon, tallyCounter_MainActivity.class);
        dictionary = new Model_selectCreate("Dictionary", R.drawable.dictionary_icon);
        flowChart = new Model_selectCreate("Flow Chart", R.drawable.flow_chart_icon);
        calculator = new Model_selectCreate("Calculator", R.drawable.calculator_icon, CalculatorMainActivity.class);
        ppt = new Model_selectCreate("PPT", R.drawable.ppt_icon);
        physics = new Model_selectCreate("Science", R.drawable.physics_icon);
        paint = new Model_selectCreate("Free Hand", R.drawable.paint_icon);

        arrayList = new ArrayList<>();
        arrayList.add(todoList);
        arrayList.add(simpleLists);

        arrayList.add(notes);
        arrayList.add(ppt);

        arrayList.add(tables);
        arrayList.add(flowChart);

        arrayList.add(barGraph);
        arrayList.add(lineGraphs);

        arrayList.add(pieChart);
        arrayList.add(tallycounter);

        arrayList.add(paint);
        arrayList.add(physics);

        arrayList.add(calculator);
        arrayList.add(dictionary);

        adapter = new Adapter_selectCreate(arrayList, this);
        recyclerView.setAdapter(adapter);

    }


    public void hideShowActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY > oldScrollY) {
                        // Scrolling down
                        actionBar.hide();
                    } else if (scrollY < oldScrollY) {
                        // Scrolling up
                        actionBar.show();
                    }
                }
            });
        }
    }
}