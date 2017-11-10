package com.example.android.newsfeed;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<List<NewsFeed>> {

    private static final int LOADER_ID = 1;
    private NewsFeedAdapter mAdapter;
    private EditText mSearch;
    private ImageView mSearchButton;
    private String search_key;
    private TextView mEmptyStateTextView;

    /**
     * API to be parsed.
     */
    private static final String NEWS_REQUEST_API =
            "https://content.guardianapis.com/search?q=";

    private static final String NEWS_KEY = "&api-key=test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearch = (EditText) findViewById(R.id.edit_text);
        mSearchButton = (ImageView) findViewById(R.id.search_button);
        search_key = mSearch.getText().toString().trim();

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    getLoaderManager().restartLoader(LOADER_ID, null, MainActivity.this);
                } else {
                    mEmptyStateTextView.setText(R.string.no_internet_connection);
                    Toast.makeText(MainActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                }

                if (search_key.length() == 0) {
                    if (netInfo == null) {
                        mEmptyStateTextView.setText(R.string.no_internet_connection);
                        Toast.makeText(MainActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                    }
                    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                        Toast.makeText(MainActivity.this, "Enter keyword to search", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        mAdapter = new NewsFeedAdapter(this, new ArrayList<NewsFeed>());
        ListView newsListView = (ListView) findViewById(R.id.list);
        newsListView.setAdapter(mAdapter);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                NewsFeed currentNews = mAdapter.getItem(i);
                Uri NewsUri = Uri.parse(currentNews.getNewsLink());
                Intent intent = new Intent(Intent.ACTION_VIEW, NewsUri);
                startActivity(intent);
            }
        });

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

            android.app.LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<NewsFeed>> onCreateLoader(int i, Bundle bundle) {
        search_key = mSearch.getText().toString().trim();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String type = sharedPrefs.getString(
                getString(R.string.settings_type_key),
                getString(R.string.settings_type_default));

        Uri baseUri = Uri.parse(NEWS_REQUEST_API + search_key + NEWS_KEY);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("type", type);

        return new NewsFeedLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsFeed>> loader, List<NewsFeed> newsFeeds) {

        View loadingIndicator = (View) findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mAdapter.clear();

        mEmptyStateTextView.setText(R.string.no_news_available);

        if (newsFeeds != null) {
            mAdapter.addAll(newsFeeds);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsFeed>> loader) {

        mAdapter.clear();
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
            Intent settingIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
