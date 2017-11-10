package com.example.android.newsfeed;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by SahilKapoor on 23-09-2017.
 */

public class NewsFeedAdapter extends ArrayAdapter<NewsFeed> {

    /**
     * constructor
     *
     * @param context --activity context
     * @param news    --news list
     */
    public NewsFeedAdapter(Activity context, ArrayList<NewsFeed> news) {
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        NewsFeed currentItem = getItem(position);

        TextView newsTitle = (TextView) listItemView.findViewById(R.id.news_title);
        newsTitle.setText(currentItem.getNewsTitle());

        TextView newsDescription = (TextView) listItemView.findViewById(R.id.news_description);
        newsDescription.setText(currentItem.getNewsDescription());

        TextView newsType = (TextView) listItemView.findViewById(R.id.news_type);
        newsType.setText(currentItem.getNewsType());

        TextView newsDate = (TextView) listItemView.findViewById(R.id.news_date);
        newsDate.setText(currentItem.getNewsDate());

        return listItemView;
    }
}
