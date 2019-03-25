package com.example.newsapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsapp.R;
import com.example.newsapp.model.NewsData;
import com.example.newsapp.ui.NewsActivity;
import com.example.newsapp.ui.NewsDetailActivity;
import com.squareup.picasso.Picasso;

public class NewsAdapter extends BaseAdapter {
    private Context context;
    private NewsData newsData;

    public NewsAdapter(Context context, NewsData newsData) {
        this.context = context;
        this.newsData = newsData;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return newsData.getArticles().size();
    }

    @Override
    public Object getItem(int position) {
        return newsData.getArticles().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_news, null, false);

            holder.iv = convertView.findViewById(R.id.newsImage);
            holder.title = convertView.findViewById(R.id.newsTitle);
            holder.date = convertView.findViewById(R.id.newsDate);
            holder.readLater = convertView.findViewById(R.id.readLaterButton);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Picasso.get().load(newsData.getArticles().get(position).getUrlToImage()).into(holder.iv);
        holder.title.setText(newsData.getArticles().get(position).getTitle());
        holder.date.setText(newsData.getArticles().get(position).getPublishedAt());

        holder.readLater.setOnClickListener(v -> {
            if (!holder.readLater.isSelected())
                holder.readLater.setSelected(true);
            else
                holder.readLater.setSelected(false);
            Toast.makeText(context, "Read later", Toast.LENGTH_SHORT).show();
        });

        convertView.setOnClickListener(v -> {
            Intent detailIntent = new Intent(context, NewsDetailActivity.class);
            detailIntent.putExtra("article", newsData.getArticles().get(position));
            context.startActivity(detailIntent);
        });
        return convertView;
    }

    private class ViewHolder {
        private TextView title, date;
        private ImageView iv;
        private ImageButton readLater;
    }
}
