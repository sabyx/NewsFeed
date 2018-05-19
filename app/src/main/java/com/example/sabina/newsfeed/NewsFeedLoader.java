package com.example.sabina.newsfeed;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.widget.ListView;

import java.util.List;

class NewsFeedLoader extends AsyncTaskLoader<List<News>> {

    private String url;

    public NewsFeedLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (url == null) {
            return null;
        }
        return QueryUtils.fetchNewsData(url);
    }
}
