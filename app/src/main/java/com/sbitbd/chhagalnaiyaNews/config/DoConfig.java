package com.sbitbd.chhagalnaiyaNews.config;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class DoConfig {
    public static final String NEWS_DATA = "http://www.chhagalnaiya.com/newsApps/getNews.php";
    public static final String NEWS_LINK = "http://www.chhagalnaiya.com/news/";
    public static final String MENU_DATA = "http://www.chhagalnaiya.com/newsApps/getMenu.php";
    public static final String GET_ID = "http://www.chhagalnaiya.com/newsApps/getID.php";
    public static final String IMAGE_URL = "http://www.chhagalnaiya.com/public/PublishNewsImg/";
    public static final String ID = "id";
    public static final String IMAGE = "image";
    public static final String TITLE = "title";
    public static final String DESC = "desc";
    public static final String SUBTITLE = "subtitle";
    public static final String RESULT = "result";
    public static final String QUERY = "query";

    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
}
