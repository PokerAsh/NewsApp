package com.example.newsapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.newsapp.R;
import com.example.newsapp.adapter.NewsAdapter;
import com.example.newsapp.connection.DBNewsAdapter;

public class NewsLikedActivity extends AppCompatActivity {
    private ListView newsList;
    private ProgressBar progressBar;
    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        setViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpList();
    }

    private void setViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        newsList = findViewById(R.id.newsList);
        progressBar = findViewById(R.id.progressBar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("LikedNews");
        }
    }

    private void setUpList() {
        progressBar.setVisibility(View.VISIBLE);
        DBNewsAdapter adapter = DBNewsAdapter.getInstance(this);
        adapter.open();
        progressBar.setVisibility(View.GONE);
        newsAdapter = new NewsAdapter(this, adapter.getArticles());
        adapter.close();
        newsList.setAdapter(newsAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
