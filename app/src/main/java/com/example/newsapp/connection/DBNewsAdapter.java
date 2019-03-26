package com.example.newsapp.connection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.newsapp.model.Article;

import java.util.ArrayList;

public class DBNewsAdapter {
    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private static DBNewsAdapter dataSourceSingletone;
    private final String table = "news";

    public static DBNewsAdapter getInstance(Context context) {
        if (dataSourceSingletone == null) {
            dataSourceSingletone = new DBNewsAdapter(context);
        }
        return dataSourceSingletone;
    }

    private DBNewsAdapter(Context context) {
        dbHelper = new DBHelper(context);
        close();
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public ArrayList<Article> getArticleObjects() {
        ArrayList<Article> articles = new ArrayList<>();
        Cursor cursor = database.query(table, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            articles.add(getArticles(cursor));
        }
        cursor.close();
        return articles;
    }

    public ArrayList<Article> getArticles() {
        ArrayList<Article> passengers = new ArrayList<>();
        Cursor cursor = database.query(table, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            passengers.add(getArticles(cursor));
        }
        cursor.close();
        return passengers;
    }

    private Article getArticles(Cursor c) {
        int author = c.getColumnIndex("author");
        int title = c.getColumnIndex("title");
        int description = c.getColumnIndex("description");
        int url = c.getColumnIndex("url");
        int urlToImage = c.getColumnIndex("urlToImage");
        int publishedAt = c.getColumnIndex("publishedAt");
        int content = c.getColumnIndex("content");

        return new Article(c.getString(author), c.getString(title), c.getString(description),
                c.getString(url), c.getString(urlToImage), c.getString(publishedAt),
                c.getString(content));
    }

    public boolean articleExists(Article article1) {
        for (Article article : getArticles()) {
            if (article.getTitle().equals(article1.getTitle())) {
                return true;
            }
        }
        return false;
    }

    public void writeArticleToDatabase(Article article) {
        ContentValues cv = generateContentValues(article);
        database.insert(table, null, cv);
    }

    private ContentValues generateContentValues(Article article) {
        ContentValues cv = new ContentValues();
        cv.put("author", article.getAuthor());
        cv.put("title", article.getTitle());
        cv.put("description", article.getDescription());
        cv.put("url", article.getUrl());
        cv.put("urlToImage", article.getUrlToImage());
        cv.put("publishedAt", article.getPublishedAtISO());
        cv.put("content", article.getContent());
        return cv;
    }

    public void removeFromDb(String title) {
        if (title.length() > 1) {
            title = title.replace("'", "");
            title = title.replace("\"", "");
            title = title.trim();
        }
        String columns = "title LIKE '" + title + "%'";
        Cursor mCursor = database.query(table, null, columns, null, null, null, null);
        if (mCursor.moveToFirst()) {
            do {
                Integer id = mCursor.getInt(mCursor.getColumnIndex("id"));
                String whereClause = "id" + "=?";
                String[] whereArgs = new String[]{id.toString()};
                database.delete(table, whereClause, whereArgs);
            } while (mCursor.moveToNext());
        }
        mCursor.close();
    }
}
