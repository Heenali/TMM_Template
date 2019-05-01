package net.simplifiedcoding.navigationdrawerexample;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Heenali on 15/4/2019.
 */

public class Activity_help extends AppCompatActivity {

    ImageView back_btn;
    KProgressHUD hud;
    WebView web;
    private String strEmail;
    Button btn_email;
    TextView tv_about;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(Activity_help.this, R.color.appcolor2));
        }
        back_btn=(ImageView)findViewById(R.id.back_btn);
        btn_email=(Button) findViewById(R.id.btn_email);
        web=(WebView) findViewById(R.id.web);
        tv_about=(TextView) findViewById(R.id.tv_web);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
       // initWebView();

       // reg_call();
        btn_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    hideKeyboard();
                    UIUtils.sendMail(getApplicationContext(), "Need Help?", "", strEmail);

            }
        });
        tv_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://talenalexander.com/"));
                startActivity(browserIntent);

            }
        });




    }
    protected void hideKeyboard() {
        // Check if no view has focus:
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    private void initWebView() {
        web.setWebChromeClient(new MyWebChromeClient(this));
        web.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                /**
                 * Check for the url, if the url is from same domain
                 * open the url in the same activity as new intent
                 * else pass the url to browser activity
                 * */

                openInAppBrowser(url);


                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // progressBar.setVisibility(View.GONE);
            }
        });
        web.clearCache(true);
        web.clearHistory();
        web.getSettings().setJavaScriptEnabled(true);
        web.setHorizontalScrollBarEnabled(false);

    }
    private void openInAppBrowser(String url)
    {
        try
        {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        }
        catch (Exception e)
        {


        }

    }
    private class MyWebChromeClient extends WebChromeClient {
        Context context;

        public MyWebChromeClient(Context context) {
            super();
            this.context = context;
        }


    }
    public  void reg_call()
    {
        hud = KProgressHUD.create(Activity_help.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setWindowColor(getResources().getColor(R.color.appcolor1))
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        RequestQueue queue = Volley.newRequestQueue(Activity_help.this);

        String url = "http://templateapp.talenhosting.com/api/setting";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        // _response.setText(response);
                        hud.dismiss();
                        try
                        {
                            JSONObject jobj = new JSONObject(response);
                            String status = jobj.getString("status");


                            if(status.equalsIgnoreCase("true"))
                            {
                                JSONArray contacts = jobj.getJSONArray("data");
                                JSONObject c = contacts.getJSONObject(3);
                                String mess = c.getString("value");
                                web.loadDataWithBaseURL("", mess, "text/html", "UTF-8", "");

                                // finish();

                            }
                            else
                            {



                            }
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(getApplicationContext(),"Something Wrong",Toast.LENGTH_LONG).show();

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                //_response.setText("That didn't work!");
                hud.dismiss();
                Toast.makeText(getApplicationContext(),"That didn't work!",Toast.LENGTH_LONG).show();

            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();


                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}

