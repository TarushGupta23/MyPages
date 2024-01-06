package com.example.mypages.chartsRes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypages.R;
import com.example.mypages.pieChart.PieChartsEditActivity;
import com.example.mypages.selectCreateActivity;

import java.util.ArrayList;
import java.util.List;

public class ChartNameAdapter extends RecyclerView.Adapter<ChartNameAdapter.ChartViewHolder> {
    private ArrayList<ModelChart> charts;
    private Context context;

    public ChartNameAdapter(ArrayList<ModelChart> charts, Context context) {
        this.charts = charts;
        this.context = context;
    }

    @NonNull
    @Override
    public ChartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chart_name_card, parent, false);
        return new ChartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChartViewHolder holder, int position) {
        holder.bind(charts.get(position), context);
    }

    @Override
    public int getItemCount() {
        return charts.size();
    }

    static class ChartViewHolder extends RecyclerView.ViewHolder {
        TextView chartName;
        CardView chartBody;

        public ChartViewHolder(@NonNull View itemView) {
            super(itemView);
            chartName = itemView.findViewById(R.id.chartItemCard_name);
            chartBody = itemView.findViewById(R.id.chartItemCard_body);
        }

        void bind(ModelChart chartItem, Context context) {
            chartName.setText(chartItem.getName());
            chartBody.setOnClickListener(view -> {
                Intent intent = new Intent(context, getChartActivity(chartItem));
                intent.putExtra("chartId", chartItem.getKey());
                context.startActivity(intent);
            });
        }

        public Class getChartActivity(ModelChart chart) {
            if (chart.getChartType() == ModelChart.PIE) {
                return PieChartsEditActivity.class;
            } else {
                return selectCreateActivity.class;
            }
        }
    }
}
