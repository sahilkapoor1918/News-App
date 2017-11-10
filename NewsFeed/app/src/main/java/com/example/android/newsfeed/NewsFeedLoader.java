package com.example.android.newsfeed;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by SahilKapoor on 23-09-2017.
 */

public class NewsFeedLoader extends AsyncTaskLoader<List<NewsFeed>> {

    private String mUrl;

    public NewsFeedLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NewsFeed> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        List<NewsFeed> newslist = QueryUtils.fetchNewsData(mUrl);
        return newslist;
    }
}
