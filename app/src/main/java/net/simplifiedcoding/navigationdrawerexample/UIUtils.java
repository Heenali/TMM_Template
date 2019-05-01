package net.simplifiedcoding.navigationdrawerexample;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Sagar on 08-10-2016.
 */
public class UIUtils {

    public static void sendMail(Context context, String subject, String content, String email){
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
        emailIntent.setType("application/*");
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, content);
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] {email});
        context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }
}
