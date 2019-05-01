package net.simplifiedcoding.navigationdrawerexample;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;
import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Heenali on 16/3/2019.
 */

public class Activity_Myaccount extends AppCompatActivity {

    ImageView back_btn;
    KProgressHUD hud;
    EditText txt_email,txt_fname,txt_lname;
    Button btn_regi;
    ConnectionDetector cd;
    SessionManager sm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);
        init();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(Activity_Myaccount.this, R.color.appcolor2));
        }
        back_btn=(ImageView)findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
       // refresh();
        //sendRequest();
        txt_email=(EditText)findViewById(R.id.txt_email);
        txt_fname=(EditText)findViewById(R.id.txt_fname);
        txt_lname=(EditText)findViewById(R.id.txt_about);
        // txt_email.setEnabled(false);
        //txt_email.setText(sm.getUserName2());

        //txt_fname.setText(sm.getUserName()+" "+sm.getUserName1());
        txt_lname.setText("I have used TextInputLayout on many apps. This Widget is the weapon of choice for most input fields like login screens, forms etc. ... ");


    }
    public void regi() {
        Log.d("mess", "regi");

        if (!validate())
        {
            onLoginFailed();
            return;
        }

        btn_regi.setEnabled(false);
        Success();
    }
    public void Success()
    {


        btn_regi.setEnabled(true);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(btn_regi.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        if (cd.isConnectingToInternet())
        {
            // reg_call();
        }
        else
        {

            mess("Check your internet connection.");
        }



    }
    public boolean validate() {
        boolean valid = true;

        String mobile_number = txt_email.getText().toString();

        String fname = txt_fname.getText().toString();
        String lname = txt_lname.getText().toString();

        if (fname.isEmpty())
        {
            mess("Please enter valid Firstname");
            // txt_email.setError("Please enter valid Email Id");
            valid = false;

        }
        else if (lname.isEmpty())
        {
            mess("Please enter valid Lastname");
            // txt_email.setError("Please enter valid Email Id");
            valid = false;

        }

        else  if (mobile_number.isEmpty())
        {
            mess("Please enter valid Email Id");
            // txt_email.setError("Please enter valid Email Id");
            valid = false;

        }
        else if (!mobile_number.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {

            mess("Invalid Email Address");
            valid = false;

        }


        return valid;
    }
    public void onLoginFailed()
    {
        //mess("Login failed");
        btn_regi.setEnabled(true);
    }
    public  void init()
    {
        sm = new SessionManager(Activity_Myaccount.this);
        cd = new ConnectionDetector(Activity_Myaccount.this);
        txt_email=(EditText)findViewById(R.id.txt_email);

        //  btn_regi=(Button) findViewById(R.id.btn_regi);
        //  txt_lname=(EditText)findViewById(R.id.txt_lname);
        //  txt_fname=(EditText)findViewById(R.id.txt_fname);

        // txt_email.setText(sm.getUserName2());
        // txt_lname.setText("");
        // txt_fname.setText(sm.getUserName()+" "+sm.getUserName1());


    }
    private void sendRequest()
    {
        String url = "http://templateapp.talenhosting.com/api/setting/"+sm.getDevice_Id();
        Log.e("ddd",url);
        StringRequest stringRequest = new StringRequest(url,new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Activity_Myaccount.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void showJSON(String json)
    {
Toast.makeText(getApplicationContext(),json+"",Toast.LENGTH_LONG).show();
    }
    public  void refresh()
    {
        hud = KProgressHUD.create(Activity_Myaccount.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setWindowColor(getResources().getColor(R.color.appcolor1))
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        RequestQueue queue = Volley.newRequestQueue(Activity_Myaccount.this);

        String url = "http://templateapp.talenhosting.com/api/user/refresh/";

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
                                JSONObject object = jobj.getJSONObject("data");
                                String mess = object.getString("token");
                                mess(mess);
                                // finish();

                            }
                            else
                            {
                                JSONObject object = jobj.getJSONObject("data");


                                try
                                {
                                    String mess = object.getString("message");
                                    mess(mess);
                                }
                                catch (Exception e)
                                {

                                }



                            }
                        }
                        catch (Exception e)
                        {

                            mess("Something Wrong");
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                //_response.setText("That didn't work!");
                hud.dismiss();
                mess("That didn't work!");
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("Bearer", sm.getDevice_Id());
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    public  void mess(String mess)
    {
        Snackbar snackbar = Snackbar.make(btn_regi, mess, Snackbar.LENGTH_LONG)
                .setAction("Action", null);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(Color.parseColor("#FA8072"));
        snackbar.show();
    }
}
