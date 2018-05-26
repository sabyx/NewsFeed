package com.example.sabina.newsfeed;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsFeedActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final String GUARDIAN_REQUEST_URL = "http://content.guardianapis.com/search";
    private static final int LOADER_ID = 1;

    private ProgressBar progressBar;
    private TextView textView;
    private NewsFeedAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);

        ListView newsList = findViewById(R.id.list);
        textView = findViewById(R.id.empty_text);
        progressBar = findViewById(R.id.loading_spinner);
        newsList.setEmptyView(textView);

        adapter = new NewsFeedAdapter(this, 0, new ArrayList<News>());
        newsList.setAdapter(adapter);

        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News currentNews = adapter.getItem(position);
                Uri newsUri = Uri.parse(currentNews.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(intent);
            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            textView.setText(R.string.no_internet);
            progressBar.setVisibility(View.GONE);
        } else {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(LOADER_ID, null, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String q = sharedPreferences.getString(
                getString(R.string.settings_q_key),
                getString(R.string.settings_q_default)
        );

        String apiKey = sharedPreferences.getString(
                getString(R.string.settings_apikey_key),
                getString(R.string.settings_apikey_default)
        );

        String orderBy = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        String type = sharedPreferences.getString(
                getString(R.string.settings_type_key),
                getString(R.string.settings_type_default)
        );

        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);
        Uri.Builder builder = baseUri.buildUpon();
        builder.appendQueryParameter("q", q);
        builder.appendQueryParameter("api-key", apiKey);
        builder.appendQueryParameter("order-by", orderBy);
        builder.appendQueryParameter("type", type);

        return new NewsFeedLoader(this, builder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        adapter.clear();
        progressBar.setVisibility(View.GONE);
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        } else {
            textView.setText(R.string.text_empty);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        adapter.clear();
    }
}
