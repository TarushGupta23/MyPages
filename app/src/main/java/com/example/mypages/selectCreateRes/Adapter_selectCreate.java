package com.example.mypages.selectCreateRes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypages.R;

import java.util.ArrayList;

public class Adapter_selectCreate extends RecyclerView.Adapter<Adapter_selectCreate.CardViewHolder> {
    private ArrayList<Model_selectCreate> modelList;
    private Context context;

    public Adapter_selectCreate(ArrayList<Model_selectCreate> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_layout, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Model_selectCreate model = modelList.get(position);
        holder.imageView.setImageResource(model.getId());
        holder.textView.setText(model.getFieldName());
        holder.cardView.setOnClickListener(view -> {

            if (model.getTargetActivity() != null) {
                Intent i;
                i = new Intent(context, model.getTargetActivity());
                if (!model.getIntentData().equals("")) {
                    i.putExtra("selectCreateData", model.getIntentData());
                }
                context.startActivity(i);
            } else {
                Toast.makeText(context, model.getFieldName() + " was clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView;
        private CardView cardView;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.selectCreate_cardImage);
            textView = itemView.findViewById(R.id.selectCreate_cardText);
            cardView = itemView.findViewById(R.id.selectCreate_cardView);
        }
    }
}
