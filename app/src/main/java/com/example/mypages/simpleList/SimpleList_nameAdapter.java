package com.example.mypages.simpleList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypages.MainActivity;
import com.example.mypages.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SimpleList_nameAdapter extends RecyclerView.Adapter<SimpleList_nameAdapter.ViewHolder>{
    List<SimpleListViewActivity.DataHolder> lists;
    Context context;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid()).child("simpleList_folder");

    public SimpleList_nameAdapter(List<SimpleListViewActivity.DataHolder> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chart_name_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(lists.get(position));
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        CardView body;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.chartItemCard_name);
            body = itemView.findViewById(R.id.chartItemCard_body);
        }

        void onBind(SimpleListViewActivity.DataHolder item) {
            name.setText(item.name);
            body.setOnClickListener(view -> {
                Intent intent = new Intent(context, SimpleListEditActivity.class);
                intent.putExtra("listId", item.key);
                context.startActivity(intent);
            });
            body.setOnLongClickListener(view -> {
                showDeleteDialog(item);
                return true;
            });
        }
    }

    public void showDeleteDialog(SimpleListViewActivity.DataHolder item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Do you want to delete list").setTitle("Delete List")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    reference.child(item.key).setValue(null);
                    dialogInterface.dismiss();
                }).setNegativeButton("No", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                }).create().show();
    }
}
