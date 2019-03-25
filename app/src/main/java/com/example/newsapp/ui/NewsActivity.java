package com.example.newsapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsapp.BuildConfig;
import com.example.newsapp.R;
import com.example.newsapp.adapter.NewsAdapter;
import com.example.newsapp.model.Article;
import com.example.newsapp.model.NewsData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {
    private static final String TAG = NewsActivity.class.getSimpleName();
    private static final String API_KEY = BuildConfig.API_KEY;
    private static final String BASE_URL = "https://newsapi.org/v2/top-headlines?country=us";

    private ListView newsList;
    private ProgressBar progressBar;
    private NewsData newsData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Log.d(TAG, API_KEY);
        setViews();
        loadData();
    }

    private void setViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        newsList = findViewById(R.id.newsList);
        progressBar = findViewById(R.id.progressBar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
        }
    }

    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, String.format("%s&apiKey=%s", BASE_URL, API_KEY),
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.optString("status").equals("ok")) {

                            newsData = new NewsData();
                            ArrayList<Article> articles = new ArrayList<>();
                            JSONArray dataArray = obj.getJSONArray("articles");

                            for (int i = 0; i < dataArray.length(); i++) {
                                Article article = new Article();
                                JSONObject dataobj = dataArray.getJSONObject(i);

                                article.setTitle(dataobj.getString("title"));
                                article.setPublishedAt(dataobj.getString("publishedAt"));
                                article.setUrlToImage(dataobj.getString("urlToImage"));
                                article.setDescription(dataobj.getString("description"));
                                article.setUrl(dataobj.getString("url"));
                                article.setAuthor(dataobj.getString("author"));
                                article.setContent(dataobj.getString("content"));

                                articles.add(article);
                            }
                            newsData.setArticles(articles);
                            setupListView();
                        } else {
                            JSONObject errorMessage = obj.getJSONObject("message");
                            Toast.makeText(this, errorMessage.toString(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);
    }

    private void setupListView() {
        progressBar.setVisibility(View.GONE);
        NewsAdapter adapter = new NewsAdapter(this, newsData);
        newsList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_liked, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_liked) {
            Intent intent = new Intent(this, NewsLikedActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
