package com.sbitbd.chhagalnaiyaNews.ui.privacy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sbitbd.chhagalnaiyaNews.R;
import com.sbitbd.chhagalnaiyaNews.config.DoConfig;

import java.util.HashMap;
import java.util.Map;

public class privacy_policy extends AppCompatActivity {
    private WebView webView;
    private DoConfig config = new DoConfig();
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        webView = findViewById(R.id.foot_webview);
        button = findViewById(R.id.pro_des_back);
//        loadDataP();
        webView.loadUrl("https://pages.flycricket.io/chhagalnaiya-com/privacy.html");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String message = "Text I want to share.";
//                String title = "Muktir 71";
//                Intent share = new Intent(Intent.ACTION_SEND);
//                share.setType("text/plain");
//                share.putExtra(android.content.Intent.EXTRA_TITLE, title);
//                share.putExtra(Intent.EXTRA_TEXT, message);
//
//                startActivity(Intent.createChooser(share, "Share"));
                onBackPressed();
                finish();
            }
        });
    }

    private void loadDataP(){
        try {

            String sql = "SELECT `privacy_policy`.`description` AS 'id' FROM `privacy_policy`";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(!response.equals("1")){
                                webView.loadData("<html><body>"+response+"</body></html>",
                                        "text/html; charset=utf-8", "UTF-8");
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(privacy_policy.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(privacy_policy.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }catch (Exception e){
        }
    }
}