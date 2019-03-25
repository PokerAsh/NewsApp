package com.example.newsapp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newsapp.R;
import com.example.newsapp.model.Article;
import com.squareup.picasso.Picasso;

public class NewsDetailActivity extends AppCompatActivity {
    private static final String ARTICLE = "article";

    private ImageView newsImage;
    private TextView newsAuthor;
    private TextView newsPublish;
    private TextView newsTitle;
    private TextView newsContent;
    private Article article;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        article = (Article) getIntent().getSerializableExtra(ARTICLE);

        newsImage = findViewById(R.id.newsImage);
        newsAuthor = findViewById(R.id.newsAuthor);
        newsPublish = findViewById(R.id.newsPublish);
        newsTitle = findViewById(R.id.newsTitle);
        newsContent = findViewById(R.id.newsContent);
        setViews();
    }

    private void setViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("NewsDetail");
        }

        Picasso.get().load(article.getUrlToImage()).into(newsImage);
        if (article.getAuthor() != null)
            newsAuthor.setText(article.getAuthor());
        newsPublish.setText(article.getPublishedAt());
        newsTitle.setText(article.getTitle());

        String content = article.getContent();
        if (content.contains("[+")) {
            String readMore = content.replace(content.split("… ")[1], "Read More…");
            SpannableString ss = new SpannableString(readMore);
            ClickableSpan click = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.getUrl()));
                    startActivity(browserIntent);
                }
            };

            ss.setSpan(click, content.length() - 13, content.length() - 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            newsContent.setText(ss);
            newsContent.setMovementMethod(LinkMovementMethod.getInstance());
        }
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
