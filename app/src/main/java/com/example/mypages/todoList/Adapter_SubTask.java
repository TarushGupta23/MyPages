package com.example.mypages.todoList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypages.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class Adapter_SubTask extends RecyclerView.Adapter<Adapter_SubTask.ViewHolder> {
    FirebaseAuth auth = FirebaseAuth.getInstance();;
    DatabaseReference databaseReference, generalReference;
    Context context;
    Model_todoList item;

    public Adapter_SubTask(Model_todoList item, Context context, String path) {
        this.item = item;
        this.context = context;
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid()).child("todo_list_folder").child(path).child(item.getKey());
        generalReference = FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid()).child("todo_list_folder");
    }

    @NonNull
    @Override
    public Adapter_SubTask.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_list_subtask_card, parent, false);
        return new Adapter_SubTask.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_SubTask.ViewHolder holder, int position) {
        holder.taskName.setText(item.getSubTaskList().get(position).getSubTaskName());
        if (item.getSubTaskList().get(position).isCompleted()) {
            holder.checkBox.setCardBackgroundColor(ContextCompat.getColor(context, R.color.coral));
        } else {
            holder.checkBox.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }

        holder.body.setOnClickListener(view -> {
            if (item.getSubTaskList().get(position).isCompleted()) {
                item.getSubTaskList().get(position).setCompleted(false);
                item.getSubTaskList().get(position).setCompletionDate(null);
                if (item.isCompleted()) {
                    item.setCompleted(false);
                    item.setDateCompleted(null);
                    databaseReference.setValue(null);
                    if (item.isHasDeadline() && new Date().compareTo(item.getDeadLine()) > 0) {
                        generalReference.child("pending").child(item.getKey()).setValue(item);
                    } else {
                        generalReference.child("active").child(item.getKey()).setValue(item);
                    }
                } else {
                    databaseReference.setValue(item);
                }
            } else {
                item.getSubTaskList().get(position).setCompleted(true);
                item.getSubTaskList().get(position).setCompletionDate(new Date());
                databaseReference.setValue(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return item.getSubTaskList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView taskName;
        CardView checkBox;
        LinearLayout body;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.subTaskCard_text);
            checkBox = itemView.findViewById(R.id.subTaskCard_checkBox);
            body = itemView.findViewById(R.id.subTaskCard_body);
        }
    }
}
