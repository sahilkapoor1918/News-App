package com.example.android.newsfeed;

/**
 * Created by SahilKapoor on 23-09-2017.
 */

public class NewsFeed {

    private String mNewsTitle;
    private String mNewsDescription;
    private String mNewsType;
    private String mNewsDate;
    private String mNewsLink;

    /**
     * constructor
     *
     * @param newsTitle       --title of the news
     * @param newsDescription --description of the news
     * @param newsType        --type of news
     * @param newsDate        --date of news
     * @param newsLink        --link of the news
     */
    public NewsFeed(String newsTitle, String newsDescription, String newsType, String newsDate, String newsLink) {

        mNewsTitle = newsTitle;
        mNewsDescription = newsDescription;
        mNewsType = newsType;
        mNewsDate = newsDate;
        mNewsLink = newsLink;
    }

    /**
     * method to get the title of the news
     *
     * @return mNewsTitle
     */
    public String getNewsTitle() {
        return mNewsTitle;
    }

    /**
     * method to get the description of the news
     *
     * @return mNewsDescription
     */
    public String getNewsDescription() {
        return mNewsDescription;
    }

    /**
     * method to get the type of news
     *
     * @return mNewsType
     */
    public String getNewsType() {
        return mNewsType;
    }

    /**
     * method to get the date of the news
     *
     * @return mNewsDate
     */
    public String getNewsDate() {
        return mNewsDate;
    }

    /**
     * method to get the link of the news
     *
     * @return mNewsLink
     */
    public String getNewsLink() {
        return mNewsLink;
    }
}
