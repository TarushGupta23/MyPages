package com.example.mypages.todoList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypages.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Adapter_subtaskInfo extends RecyclerView.Adapter<Adapter_subtaskInfo.ViewHolder>{
    ArrayList<Model_todoList.SubTask> taskList;
    long creation;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    Adapter_subtaskInfo(ArrayList<Model_todoList.SubTask> taskList, long creation) {
        this.taskList = taskList;
        this.creation = creation;
    }

    @NonNull
    @Override
    public Adapter_subtaskInfo.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_list_subtask_item_info, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_subtaskInfo.ViewHolder holder, int position) {
        Model_todoList.SubTask task = taskList.get(position);
        holder.name.setText(task.getSubTaskName());
        if (task.isCompleted()) {
            holder.date.setText(dateFormat.format(task.getCompletionDate()));
            long timeDifferenceMillis = task.getCompletionDate().getTime() - creation;
            long millisecondsInADay = 24 * 60 * 60 * 1000;
            long daysDifference = timeDifferenceMillis / millisecondsInADay;
            long hoursDifference = (timeDifferenceMillis % millisecondsInADay) / (60 * 60 * 1000);
            holder.time.setText(Long.toString(daysDifference) + " D " + Long.toString(hoursDifference) + " H");
        } else {
            holder.date.setText("Pending");
            holder.time.setText("Pending");
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, date, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.todoListInfo_subtaskName);
            date = itemView.findViewById(R.id.todoListInfo_subtaskDate);
            time = itemView.findViewById(R.id.todoListInfo_subtaskTime);
        }
    }
}
