package net.simplifiedcoding.navigationdrawerexample;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Heenali on 20/2/2019.
 */

public class Activity_Login extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    TextView txt_forgotpass,txt_regi;
    Button btn_login;
    EditText txt_pass,txt_email;
    ConnectionDetector cd;
    KProgressHUD hud;
    SessionManager sm;
    private CallbackManager callbackManager;
    private LinearLayout ll_btn_googleSignIn, ll_btn_facebookSignIn;
    private TextView ll_btn_googleSignIn_tx, ll_btn_facebookSignIn_tx;

    private GoogleApiClient mGoogleApiClient;
    private int SIGN_IN = 007;
    private GoogleSignInOptions gso;
    String semail,sname,sname2,sid,stype;
    private ProgressDialog mProgressDialog;
    TextView txt_errormess;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(Activity_Login.this, R.color.appcolor2));
        }
        sm = new SessionManager(Activity_Login.this);
        cd = new ConnectionDetector(Activity_Login.this);
        txt_email=(EditText) findViewById(R.id.txt_email);
        txt_errormess=(TextView) findViewById(R.id.txt_errormess);
        txt_pass=(EditText) findViewById(R.id.txt_pass);
        txt_forgotpass=(TextView)findViewById(R.id.txt_forgotpass);
        txt_regi=(TextView)findViewById(R.id.txt_regi);
        btn_login=(Button) findViewById(R.id.btn_login);
        ll_btn_googleSignIn = (LinearLayout) findViewById(R.id.ll_btn_googleSignIn);
        ll_btn_facebookSignIn = (LinearLayout) findViewById(R.id.ll_btn_facebookSignIn);
        ll_btn_googleSignIn_tx = (TextView) findViewById(R.id.ll_btn_googleSignIn_tx);
        ll_btn_facebookSignIn_tx = (TextView) findViewById(R.id.ll_btn_facebookSignIn_tx);

        txt_forgotpass.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i=new Intent(getApplicationContext(),Activity_SendeMail.class);
                startActivity(i);
            }
        });

        txt_regi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i=new Intent(getApplicationContext(),Activity_regi.class);
                startActivity(i);
            }
        });



        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
                //login();
            }
        });

        //initGoogleClient();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        initFaceboock();
        ll_btn_googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, SIGN_IN);
            }
        });
        ll_btn_facebookSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                faceBookSetting();
            }
        });
        ll_btn_googleSignIn_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, SIGN_IN);
            }
        });
        ll_btn_facebookSignIn_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                faceBookSetting();
            }
        });

    }
    private void revokeAccess() {

        /* Disconnect accounts : It is highly recommended that you provide users that signed in with Google the ability
        to disconnect their Google account from your app. If the user deletes their account,
        you must delete the information that your app obtained from the Google APIs.

        Note. clear user records from all storage places, i haven't stored any user records so i am just updating the UI.
        */

        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {

                    }
                });
    }
    private void signOut()
    {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...

                    }
                });
    }
    @Override
    public void onStart()
    {
        super.onStart();
        try
        {
            signOut();
            revokeAccess();
            OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
            if (opr.isDone()) {
                // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
                // and the GoogleSignInResult will be available instantly.
                Log.d("", "Got cached sign-in");
                GoogleSignInResult result = opr.get();
                handleSignInResult(result);
            } else {
                // If the user has not previously signed in on this device or the sign-in has expired,
                // this asynchronous branch will attempt to sign in the user silently.  Cross-device
                // single sign-on will occur in this branch.
                showProgressDialog();
                opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                    @Override
                    public void onResult(GoogleSignInResult googleSignInResult) {
                        hideProgressDialog();
                        handleSignInResult(googleSignInResult);
                    }
                });
            }
        }
        catch (Exception e)
        {

        }

    }



    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.app_name));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void initFaceboock() {

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        AppEventsLogger.activateApp(this);
    }
    public void login()
    {
        Log.d("mess", "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        btn_login.setEnabled(false);

        // TODO: Implement your own authentication logic here.

        onLoginSuccess();
    }
    public void onLoginFailed()
    {
        //mess("Login failed");
        btn_login.setEnabled(true);
    }
    public  void mess(String mess)
    {
        Snackbar snackbar = Snackbar.make(btn_login, mess, Snackbar.LENGTH_LONG).setAction("Action", null);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(Color.parseColor("#FA8072"));
        snackbar.show();
    }
    public void onLoginSuccess()
    {
        btn_login.setEnabled(true);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(btn_login.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        if (cd.isConnectingToInternet())
        {
            login_call();
        }
        else
        {

            mess("Check your internet connection." );
        }

    }
    public boolean validate() {
        boolean valid = true;

        String mobile_number = txt_email.getText().toString();
        String password = txt_pass.getText().toString();


        if (mobile_number.isEmpty()) {
            mess("Please Enter Valid Email Address");
            // txt_email.setError("Please enter valid Email Id");
            txt_errormess.setText("Please Enter Valid Email Address");
            valid = false;

        }
        else if (password.isEmpty())
        {
            mess("Please Enter Valid Password");
            txt_errormess.setText("Please Enter Valid Password");
            //txt_pass.setError("Please enter valid password");
            valid = false;
        }



        return valid;
    }
    public void login_call()
    {
        hud = KProgressHUD.create(Activity_Login.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setWindowColor(getResources().getColor(R.color.appcolor1))
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        RequestQueue queue = Volley.newRequestQueue(Activity_Login.this);

        String url = "http://templateapp.talenhosting.com/api/user/login";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        // _response.setText(response);
                        hud.dismiss();
                        txt_errormess.setText("");
                        try
                        {
                            JSONObject jobj = new JSONObject(response);
                            String status = jobj.getString("status");
                            if(status.equalsIgnoreCase("true"))
                            {
                                JSONObject object = jobj.getJSONObject("data");
                                String attr1 = object.getString("token");
                                JSONObject object1 = object.getJSONObject("user");
                                String name = object1.getString("firstname");
                                String name1 = object1.getString("lastname");
                                String name2 = object1.getString("email");
                                sm.createLoginSession(attr1);
                                sm.setUserId(attr1, name, name1, name2, attr1, attr1, attr1, attr1, attr1);

                                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                            else
                            {
                                txt_errormess.setText("Invalid Username or password");
                                mess("Invalid Username or password");
                            }
                        }
                        catch (Exception e)
                        {
                            txt_errormess.setText("Something Wrong");
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
                params.put("email", txt_email.getText().toString());
                params.put("password", txt_pass.getText().toString());
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    private void initGoogleClient() {


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PROFILE))
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestProfile()
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API
        // and the options specified by gGoogleSignInOptions.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(Activity_Login.this /* FragmentActivity */,Activity_Login.this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .build();

    }
    private void faceBookSetting() {

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {

                                        Log.e("", "FacebookLogin--->" + response);

                                        String name = response.getJSONObject().optString("name");
                                        String userID = response.getJSONObject().optString("id");
                                        String email = response.getJSONObject().optString("email");
                                        String gender = response.getJSONObject().optString("gender");
                                        String profileUrl = "https://graph.facebook.com/" + userID + "/picture?type=large";
                                        String[] nameFull = name.split(" ");
                                        String gen;
                                        if (gender.equals("male")) {
                                            gen = "M";
                                        } else if (gender.equals("female")) {
                                            gen = "F";
                                        } else {
                                            gen = "U";
                                        }

                                        JSONObject jsonObject = new JSONObject();
                                        try {
                                            jsonObject.put("first_name", name);
                                            jsonObject.put("socialid", userID);
                                            jsonObject.put("email", email);
                                            jsonObject.put("profile_pic", profileUrl);
                                            jsonObject.put("gender", gen);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        getSocialLogin(jsonObject.toString(),
                                                email,
                                                userID,
                                                "FACEBOOK",name);

                                        Log.v("LoginActivity", response.toString());
                                    }
                                });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    /**
                     * Called when the dialog finishes with an error.
                     *
                     * @param error The error that occurred
                     */
                    @Override
                    public void onError(FacebookException error) {
                        Log.e("", error.toString());
                        //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }

                });

    }
    private void getSocialLogin(final String socialData,
                                final String email,
                                final String socilId,
                                final String socialTypeFrom,final String fname)
    {

       // Toast.makeText(getApplicationContext(), email, Toast.LENGTH_SHORT).show();
       sm.createLoginSession(socilId);
       sm.setUserId(socilId, fname, "", email, "", "", "", "", "");


        semail=email;
        sname=fname;
        sname2=fname;
        stype="facebook";
        sid=socilId;

        login_call_sociale();
       // Intent i=new Intent(getApplicationContext(),MainActivity.class);
        //startActivity(i);
        //finish();


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 007)
        {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        }
        else
        {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess())
        {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e("", "display name: " + acct.getDisplayName());
            Log.e("", "display name: " + acct.getEmail());

           sm.createLoginSession(acct.getId());
            sm.setUserId(acct.getId(), acct.getDisplayName()+"", "", acct.getEmail()+"", "", "", "", "", "");
            semail=acct.getEmail();
            sname=acct.getDisplayName();
            sname2=acct.getDisplayName();
            stype="google";
            sid=acct.getId();
            login_call_sociale();

           // Intent i=new Intent(getApplicationContext(),MainActivity.class);
           // startActivity(i);
           // finish();
        }
        else
            {
            // Signed out, show unauthenticated UI.
          //Toast.makeText(getApplicationContext(),"Login faill",Toast.LENGTH_SHORT).show();
        }
    }
    public void login_call_sociale()
    {
        hud = KProgressHUD.create(Activity_Login.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setWindowColor(getResources().getColor(R.color.appcolor1))
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        RequestQueue queue = Volley.newRequestQueue(Activity_Login.this);

        String url = "http://templateapp.talenhosting.com/api/user/sm/login";

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
                                String attr1 = object.getString("jwtid");
                                JSONObject object1 = object.getJSONObject("user");
                                String name = object1.getString("firstname");
                                String name1 = object1.getString("lastname");
                                String name2 = object1.getString("email");
                              //  sm.createLoginSession(attr1);
                               // sm.setUserId(attr1, name, name1, name2, attr1, attr1, attr1, attr1, attr1);

                                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                            else
                            {
                                mess("Invalid Username or password");
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
                params.put("email", semail);
                params.put("type", stype);
                params.put("sm_id",sid);
                params.put("firstname", sname);
                params.put("lastname", sname2);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
