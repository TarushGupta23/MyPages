package com.example.mypages.chartsRes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypages.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChartEditDialogRecyclerView extends RecyclerView.Adapter<ChartEditDialogRecyclerView.ViewHolder> {
    DatabaseReference reference;
    List<Map.Entry<String, Double>> entryList = new ArrayList<>();
    Context context;

    public ChartEditDialogRecyclerView(DatabaseReference reference, Context context) {
        this.reference = reference.child("data");
        this.context = context;

        this.reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                entryList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    entryList.add(new Map.Entry<String, Double>() {
                        @Override
                        public String getKey() {
                            return dataSnapshot.getKey();
                        }

                        @Override
                        public Double getValue() {
                            return Double.parseDouble(dataSnapshot.getValue().toString());
                        }

                        @Override
                        public Double setValue(Double value) {
                            return value;
                        }
                    });
                }
                notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chart_edit_dialog_field_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView deleteBtn, saveBtn;
        EditText fieldName, fieldValue;

        public ViewHolder(@NonNull View itemView) { super(itemView); }

        void bind(int position) {
            deleteBtn = itemView.findViewById(R.id.chartDataEditDialogCard_deleteBtn);
            fieldName = itemView.findViewById(R.id.chartDataEditDialogCard_fieldName);
            fieldValue = itemView.findViewById(R.id.chartDataEditDialogCard_fieldValue);
            saveBtn = itemView.findViewById(R.id.chartDataEditDialogCard_confirmBtn);

            String name = entryList.get(position).getKey();
            Double value = entryList.get(position).getValue();

            fieldName.setText(name);
            fieldValue.setText(value + "");


            deleteBtn.setOnClickListener(view -> {
                reference.child(name).setValue(null);
            });

            saveBtn.setOnClickListener(view -> {
                String finalName = fieldName.getText().toString();
                Double finalVal;
                if (fieldValue.getText().toString().equals("")) { finalVal = 0d; }
                else { finalVal = Double.parseDouble(fieldValue.getText().toString()); }

                if (finalName.equals("")) {
                    Toast.makeText(context, "removing data", Toast.LENGTH_SHORT).show();
                    reference.child(name).setValue(null);
                } else {
                    reference.child(name).setValue(null);
                    reference.child(finalName).setValue(finalVal);
                }
            });
        }
    }
}
