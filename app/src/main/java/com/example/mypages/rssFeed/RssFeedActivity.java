package com.example.mypages.rssFeed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Xml;

import com.example.mypages.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RssFeedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Adapter_Rss adapter;
    private List<RssFeed> rssFeedList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss_feed);

        recyclerView = findViewById(R.id.recyclerView_rss);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter_Rss(this, rssFeedList);
        recyclerView.setAdapter(adapter);

        new FetchRssFeedTask().execute("https://timesofindia.indiatimes.com/rssfeedstopstories.cms");
    }

    private class FetchRssFeedTask extends AsyncTask<String, Void, List<RssFeed>> {
        @Override
        protected List<RssFeed> doInBackground(String... strings) {
            String urlString = strings[0];
            List<RssFeed> feeds = new ArrayList<>();
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream inputStream = connection.getInputStream();

                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(inputStream, null);

                int eventType = parser.getEventType();
                RssFeed currentFeed = null;
                boolean insideItem = false;

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String name;
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            name = parser.getName();
                            if (name.equalsIgnoreCase("item")) {
                                insideItem = true;
                                currentFeed = new RssFeed("", "", "", "");
                            } else if (insideItem && currentFeed != null) {
                                if (name.equalsIgnoreCase("title")) {
                                    currentFeed.setTitle(parser.nextText());
                                } else if (name.equalsIgnoreCase("description")) {
                                    currentFeed.setDescription(parser.nextText());
                                } else if (name.equalsIgnoreCase("link")) {
                                    currentFeed.setLink(parser.nextText());
                                } else if (name.equalsIgnoreCase("pubDate")) {
                                    currentFeed.setPubDate(parser.nextText());
                                }
                            }
                            break;

                        case XmlPullParser.END_TAG:
                            name = parser.getName();
                            if (name.equalsIgnoreCase("item") && currentFeed != null) {
                                feeds.add(currentFeed);
                                insideItem = false;
                            }
                            break;
                    }
                    eventType = parser.next();
                }
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
            return feeds;
        }

        @Override
        protected void onPostExecute(List<RssFeed> feeds) {
            rssFeedList.clear();
            rssFeedList.addAll(feeds);
            adapter.notifyDataSetChanged();
        }
    }
}