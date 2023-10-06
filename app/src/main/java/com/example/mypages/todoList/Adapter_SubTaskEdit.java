package com.example.mypages.todoList;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypages.R;

import java.util.ArrayList;

public class Adapter_SubTaskEdit extends RecyclerView.Adapter<Adapter_SubTaskEdit.ViewHolder> {
    ArrayList<Model_todoList.SubTask> list;

    public Adapter_SubTaskEdit(ArrayList<Model_todoList.SubTask> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Adapter_SubTaskEdit.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_list_subtask_edit_card, parent, false);
        return new Adapter_SubTaskEdit.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_SubTaskEdit.ViewHolder holder, int position) {
        holder.name.setText(list.get(position).getSubTaskName());

        holder.delete.setOnClickListener(view -> {
            list.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        Button delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.todoListSubtask_editName);
            delete = itemView.findViewById(R.id.todoListSubtask_editDelete);
        }
    }
}
