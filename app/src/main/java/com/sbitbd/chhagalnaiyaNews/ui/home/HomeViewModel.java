package com.sbitbd.chhagalnaiyaNews.ui.home;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sbitbd.chhagalnaiyaNews.config.DoConfig;
import com.sbitbd.chhagalnaiyaNews.config.multiSectionAdapter;
import com.sbitbd.chhagalnaiyaNews.config.multinews_adapter;
import com.sbitbd.chhagalnaiyaNews.config.multisection_model;
import com.sbitbd.chhagalnaiyaNews.config.newsAdapter;
import com.sbitbd.chhagalnaiyaNews.config.news_model;
import com.sbitbd.chhagalnaiyaNews.config.sectionMenuAdapter;
import com.sbitbd.chhagalnaiyaNews.config.sectionModel;
import com.sbitbd.chhagalnaiyaNews.config.tab_adapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private DoConfig config = new DoConfig();
    private List<news_model> news_models = new ArrayList<>();
    private List<sectionModel> sectionModels = new ArrayList<>();

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public List<news_model> showProJSON(String response, news_model pro_model, newsAdapter product_adapter, Context context, ImageView imageView, TextView textView) {
        String title, subtitle, image, id;
        List<news_model> news_models1 = new ArrayList<>();
        id = "";
        title = "";
        subtitle = "";
        image = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                try {
                    news_models.clear();
                    JSONObject collegeData = result.getJSONObject(i);
                    id = collegeData.getString(config.ID);
                    title = collegeData.getString(config.TITLE);
//                    subtitle = collegeData.getString(config.SUBTITLE);
                    image = collegeData.getString(config.IMAGE);
                    pro_model = new news_model(id, title, subtitle, image);
                    news_models.add(pro_model);
                    news_models1.add(pro_model);
                    if (i != 0) {
                        product_adapter.adduser(pro_model);
                    }
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {e.printStackTrace();
        }
        return news_models1;
    }

    public void showProJSON(String response, news_model pro_model, newsAdapter product_adapter, Context context) {
        String title, subtitle, image, id;
        id = "";
        title = "";
        subtitle = "";
        image = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                try {
                    JSONObject collegeData = result.getJSONObject(i);
                    id = collegeData.getString(config.ID);
                    title = collegeData.getString(config.TITLE);
//                    subtitle = collegeData.getString(config.SUBTITLE);
                    image = collegeData.getString(config.IMAGE);
                    pro_model = new news_model(id, title, subtitle, image);
                    product_adapter.adduser(pro_model);

                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
    }

    public void showSecNewsJSON(String response, multisection_model pro_model, multiSectionAdapter product_adapter, Context context) {
        String title, image, id;
        id = "";
        title = "";
        image = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                try {
                    JSONObject collegeData = result.getJSONObject(i);
                    id = collegeData.getString(config.ID);
                    title = collegeData.getString(config.TITLE);
                    image = collegeData.getString(config.IMAGE);
                    pro_model = new multisection_model(id, title, image);
                    product_adapter.adduser(pro_model);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
    }

    public void showMenuJSON(String response, sectionModel pro_model, sectionMenuAdapter product_adapter, Context context) {
        String title, id;
        id = "";
        title = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                try {
                    JSONObject collegeData = result.getJSONObject(i);
                    id = collegeData.getString(config.ID);
                    title = collegeData.getString(config.TITLE);
                    pro_model = new sectionModel(id, title);
                    product_adapter.adduser(pro_model);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
    }

    public void showMultiMenuJSON(String response, sectionModel pro_model, multinews_adapter product_adapter, Context context) {
        String title, id;
        id = "";
        title = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                try {
                    JSONObject collegeData = result.getJSONObject(i);
                    id = collegeData.getString(config.ID);
                    title = collegeData.getString(config.TITLE);
                    pro_model = new sectionModel(id, title);
                    product_adapter.adduser(pro_model);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
    }

    public List<sectionModel> showSubMenuJSON(String response, sectionModel pro_model, tab_adapter product_adapter, Context context) {
        String title, id;
        id = "";
        title = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                try {
                    JSONObject collegeData = result.getJSONObject(i);
                    id = collegeData.getString(config.ID);
                    title = collegeData.getString(config.TITLE);
                    pro_model = new sectionModel(id, title);
                    product_adapter.adduser(pro_model);
                    sectionModels.add(pro_model);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
        return sectionModels;
    }
}