package com.example.mypages.rssFeed;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypages.R;

import java.util.List;

public class Adapter_Rss extends RecyclerView.Adapter<Adapter_Rss.ViewHolder> {

    private Context context;
    private List<RssFeed> rssList;

    public Adapter_Rss(Context context, List<RssFeed> rssList) {
        this.context = context;
        this.rssList = rssList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rss_feed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RssFeed item = rssList.get(position);
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
        holder.pubDate.setText(item.getPubDate());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getLink()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return rssList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, pubDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textView_rssTitle);
            description = itemView.findViewById(R.id.textView_rssDescription);
            pubDate = itemView.findViewById(R.id.textView_rssPubDate);
        }
    }
}
