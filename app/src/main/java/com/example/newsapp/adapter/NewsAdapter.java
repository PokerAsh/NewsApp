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

import com.example.newsapp.R;
import com.example.newsapp.connection.DBNewsAdapter;
import com.example.newsapp.model.Article;
import com.example.newsapp.model.NewsData;
import com.example.newsapp.ui.NewsDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Article> articleList;

    public NewsAdapter(Context context, NewsData newsData) {
        this.context = context;
        this.articleList = newsData.getArticles();
    }

    public NewsAdapter(Context context, ArrayList<Article> articles) {
        this.context = context;
        this.articleList = articles;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return articleList.size();
    }

    @Override
    public Object getItem(int position) {
        return articleList.get(position);
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
            holder.liked = convertView.findViewById(R.id.likeButton);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Picasso.get().load(articleList.get(position).getUrlToImage()).into(holder.iv);
        holder.title.setText(articleList.get(position).getTitle());
        holder.date.setText(articleList.get(position).getPublishedAt());

        DBNewsAdapter adapter = DBNewsAdapter.getInstance(context);
        adapter.open();
        if (adapter.articleExists(articleList.get(position))) {
            holder.liked.setSelected(true);
        } else {
            holder.liked.setSelected(false);
        }
        adapter.close();

        holder.liked.setOnClickListener(v -> {
            if (!holder.liked.isSelected()) {
                adapter.open();
                adapter.writeArticleToDatabase(articleList.get(position));
                adapter.close();
                holder.liked.setSelected(true);
            } else {
                adapter.open();
                adapter.removeFromDb(articleList.get(position).getTitle());
                adapter.close();
                holder.liked.setSelected(false);
            }
        });

        convertView.setOnClickListener(v -> {
            Intent detailIntent = new Intent(context, NewsDetailActivity.class);
            detailIntent.putExtra("article", articleList.get(position));
            context.startActivity(detailIntent);
        });
        return convertView;
    }

    private class ViewHolder {
        private TextView title, date;
        private ImageView iv;
        private ImageButton liked;
    }
}
