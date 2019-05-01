package net.simplifiedcoding.navigationdrawerexample;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Button;
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
 * Created by Heenali on 26/3/2019.
 */

public class Activity_OTP extends AppCompatActivity {

    ImageView back_btn;
    Button btn_submit;

    KProgressHUD hud;
    String otp_no="";
    ConnectionDetector cd;
    EditText input_otp1;
    String email="";
    TextView txt_errormess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(Activity_OTP.this, R.color.appcolor2));
        }
        cd= new ConnectionDetector(Activity_OTP.this);
        input_otp1=(EditText)findViewById(R.id.txt_otp);


        Intent i = getIntent();

        email= i.getStringExtra("email");

        txt_errormess=(TextView) findViewById(R.id.txt_errormess);
        txt_errormess.setText("ss");
        back_btn=(ImageView)findViewById(R.id.back_btn);
        btn_submit=(Button) findViewById(R.id.btn_submit);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (validate())
                {

                    otp_no=input_otp1.getText().toString().trim();

                    if(input_otp1.getText().length()>0)
                    {
                        if(cd.isConnectingToInternet())
                        {
                            reg_call();
                        }
                        else {
                            mess("Check Your Internet Connection.");
                        }
                    }
                    else
                    {
                        Toast.makeText(Activity_OTP.this,"OTP Number Is Wrong",Toast.LENGTH_LONG).show();
                    }


                }
            }
        });


    }
    public boolean validate() {
        boolean valid = true;

        String otp1 = input_otp1.getText().toString().trim();


//        || otp.length() < 4
        if (otp1.isEmpty())
        {
             mess("Enter OTP");
            txt_errormess.setText("Enter OTP");
            valid = false;
        }
        else
        {
            txt_errormess.setText("");
            input_otp1.setError(null);
        }

        return valid;
    }

    public  void mess(String mess)
    {
        Snackbar snackbar = Snackbar.make(btn_submit, mess, Snackbar.LENGTH_LONG)
                .setAction("Action", null);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(Color.parseColor("#FA8072"));
        snackbar.show();
    }
    public  void reg_call()
    {
        hud = KProgressHUD.create(Activity_OTP.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setWindowColor(getResources().getColor(R.color.appcolor1))
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        RequestQueue queue = Volley.newRequestQueue(Activity_OTP.this);

        String url = "http://templateapp.talenhosting.com/api/user/verify";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        // _response.setText(response);
                        hud.dismiss();
                        try
                        {
                            txt_errormess.setText("");
                            JSONObject jobj = new JSONObject(response);
                            String status = jobj.getString("status");


                            if(status.equalsIgnoreCase("true"))
                            {
                                JSONObject object = jobj.getJSONObject("data");
                                String mess = object.getString("message");
                                mess(mess);
                                Toast.makeText(getApplicationContext(),mess,Toast.LENGTH_LONG).show();
                                finish();

                            }
                            else
                            {
                                JSONObject object = jobj.getJSONObject("data");
                                try
                                {

                                    String messd=object.getString("message");
                                    mess(messd);
                                    txt_errormess.setText(messd);
                                }
                                catch (Exception e)
                                {

                                }
                                try
                                {
                                    JSONObject object1 = object.getJSONObject("message");
                                    JSONArray subArray = object1.getJSONArray("email");
                                    String messd=subArray.toString().replace("[\""," ");
                                    messd=messd.replace("\"]"," ");
                                    mess(messd);
                                }
                                catch (Exception e)
                                {

                                }
                                try
                                {
                                    JSONObject object1 = object.getJSONObject("message");
                                    JSONArray subArray = object1.getJSONArray("code");
                                    String messd=subArray.toString().replace("[\""," ");
                                    messd=messd.replace("\"]"," ");
                                    mess(messd);
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
                params.put("email", email);
                params.put("code", input_otp1.getText().toString());

                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}

