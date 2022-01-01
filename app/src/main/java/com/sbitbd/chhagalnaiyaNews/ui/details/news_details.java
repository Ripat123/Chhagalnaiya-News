package com.sbitbd.chhagalnaiyaNews.ui.details;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.sbitbd.chhagalnaiyaNews.R;
import com.sbitbd.chhagalnaiyaNews.config.DoConfig;

import java.util.HashMap;
import java.util.Map;

public class news_details extends AppCompatActivity {
    private Button backbtn;
    private WebView webView,webView1;
    private DoConfig config = new DoConfig();
    private ProgressBar progressIndicator;
    private TextView title,sbit;
    private ImageView imageView;
    private Typeface typeface;
    private ImageView share;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private String details;
    private String data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_news_details);
        initView(savedInstanceState);
    }
    private void initView(Bundle savedInstanceState){
        backbtn = findViewById(R.id.pro_des_back);
        webView = findViewById(R.id.terms_webview);
        webView1 = findViewById(R.id.foot_webview);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);


        progressIndicator = findViewById(R.id.progress);
        title = findViewById(R.id.tittle_id);
        imageView = findViewById(R.id.title_img);
        sbit = findViewById(R.id.sbit);
        share = findViewById(R.id.sharebtn);

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        String id = getIntent().getStringExtra("id");
        String title_t = getIntent().getStringExtra("title");
        String img = getIntent().getStringExtra("img");
        typeface = Typeface.createFromAsset(getAssets(),"font/hindi.ttf");
        title.setTypeface(typeface);
        title.setText(title_t);
        Glide.with(news_details.this)
                .load(config.IMAGE_URL+id+"."+img)
                .fitCenter().error(R.drawable.logo_water).placeholder(R.drawable.logo_water)
                .into(imageView);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(config.NEWS_LINK+ id))
                        .setQuote(title_t)
                        .build();

                if (ShareDialog.canShow(ShareLinkContent.class)){

                    shareDialog.show(shareLinkContent, ShareDialog.Mode.AUTOMATIC);
                }
            }
        });
        sbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.sbit.com.bd"));
                startActivity(browserIntent);
            }
        });
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webView.setLongClickable(false);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (savedInstanceState != null) {
            progressIndicator.setVisibility(View.GONE);
            data = savedInstanceState.getString("data");
            webView.loadData("<html><body>" + data + "</body></html>",
                    "text/html; charset=utf-8", "UTF-8");
        }
        else
            loadData(id);
        loadData();
    }

    private void loadData(String id){
        try {
            String sql = "SELECT `description` AS 'id' FROM `publish_news` WHERE `id` = '"+id+"'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressIndicator.setVisibility(View.GONE);
                            if(!response.equals("1")){
                                webView.loadData("<html><body>"+response+"</body></html>",
                                        "text/html; charset=utf-8", "UTF-8");
                                data = response;
                            details = Html.fromHtml(response).toString();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressIndicator.setVisibility(View.GONE);
                    Toast.makeText(news_details.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(news_details.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }catch (Exception e){
        }
    }
    private void loadData(){
        try {

            String sql = "SELECT `contact_us`.`description` AS 'id' FROM `contact_us`";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(!response.equals("1")){
                                webView1.loadData("<html><body>"+response+"</body></html>",
                                        "text/html; charset=utf-8", "UTF-8");
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(news_details.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(news_details.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }catch (Exception e){
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("data",data);
    }
}