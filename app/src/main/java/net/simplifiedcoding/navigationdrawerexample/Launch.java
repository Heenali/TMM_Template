package net.simplifiedcoding.navigationdrawerexample;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Heenali on 25/3/2019.
 */

public class Launch extends AppCompatActivity {

    private Timer timer = new Timer();
    private long delayTime = 3500;
    SessionManager sm;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);


        sm = new SessionManager(Launch.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(Launch.this, R.color.appcolor2));
        }
        timer.schedule(new TimerTask()
        {
        public void run()
        {

            if(sm.isLoggedIn())
            {
                Intent intent;
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
            else
            {
                Intent intent;
                intent = new Intent(getApplicationContext(), Activity_Login.class);
                startActivity(intent);
            }

            finish();
        }
    }, delayTime);
    }
}
