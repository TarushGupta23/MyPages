package com.example.mypages.todoList;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TodoList_ViewPagerAdapter extends FragmentStateAdapter {
    public TodoList_ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new TodoList_ViewPagerFragment("active");
            case 1:
                return new TodoList_ViewPagerFragment("completed");
            case 2:
                return new TodoList_ViewPagerFragment("pending");
        } //NOTE: change getItemCount() after changing this
        return null;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
