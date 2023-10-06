package com.example.mypages.todoList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.Menu;

import com.example.mypages.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class TodoListMainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list_main);
        getSupportActionBar().setTitle("Todo List");

        tabLayout = findViewById(R.id.todoList_tabLayout);
        viewPager2 = findViewById(R.id.todoList_viewPager);

        TodoList_ViewPagerAdapter viewPagerAdapter = new TodoList_ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager2.setAdapter(viewPagerAdapter);
        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, viewPager2, true, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Active Tasks");
                        break;
                    case 1:
                        tab.setText("Completed");
                        break;
                    case 2:
                        tab.setText("Pending");
                        break;
                }
            }
        });
        mediator.attach();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_plus_mic_icon, menu);
        return true;
    }
}