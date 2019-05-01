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

public class Activity_SendeMail extends AppCompatActivity {

    ImageView back_btn;
    Button btn_submit;
    EditText txt_emailid;
    KProgressHUD hud;

    TextView txt_errormess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendemail);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(Activity_SendeMail.this, R.color.appcolor2));
        }
        txt_errormess=(TextView) findViewById(R.id.txt_errormess);
        back_btn=(ImageView)findViewById(R.id.back_btn);
        btn_submit=(Button) findViewById(R.id.btn_submit);
        txt_emailid=(EditText) findViewById(R.id.txt_emailid);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(txt_emailid.getText().toString().equalsIgnoreCase(""))
                {
                    mess("Enter Valid Email Address");
                    txt_errormess.setText("Enter Valid Email Address");
                }
                else
                {
                    /*Intent intent;
                    intent = new Intent(getApplicationContext(), Activity_passcreate.class);
                    startActivity(intent);
                    finish();*/
                    reg_call();
                }

            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        hud = KProgressHUD.create(Activity_SendeMail.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setWindowColor(getResources().getColor(R.color.appcolor1))
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        RequestQueue queue = Volley.newRequestQueue(Activity_SendeMail.this);

        String url = "http://templateapp.talenhosting.com/api/password/email";

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
                                Toast.makeText(getApplicationContext(),mess,Toast.LENGTH_SHORT).show();
                               // txt_emailid.setText("");
                               // mess(mess);
                                 finish();

                            }
                            else
                            {
                                JSONObject object = jobj.getJSONObject("data");
                                String mess = object.getString("message");
                                mess(mess);
                                txt_errormess.setText(mess);
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
                params.put("email", txt_emailid.getText().toString());

                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}

