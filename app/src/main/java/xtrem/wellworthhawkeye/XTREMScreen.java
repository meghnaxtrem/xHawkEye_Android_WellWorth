package xtrem.wellworthhawkeye;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Admin on 2/11/14.
 */public class XTREMScreen extends Activity {
    WebView myWebView;
    ProgressDialog pd;

    Context context;
    private boolean firstClick_call = true;
    private boolean firstClick_mail = true;
    private boolean firstClick_web = true;
    private LinearLayout animLayout;
    private TextView textView_anim;
    private RelativeLayout relativeLayout;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.webviewlayout);
        try{
            context = XTREMScreen.this;
            animLayout = ((LinearLayout) findViewById(R.id.animLayout));
            textView_anim = ((TextView) findViewById(R.id.textView_anim));
            animLayout.removeAllViews();
            textView_anim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (textView_anim.getText().toString().contains("+91")) {    //Call
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:+912229201324"));
                        startActivity(callIntent);
                        startActivity(callIntent);
                    } else if (textView_anim.getText().toString().contains("@")) { //Mail
                        Intent intentEmail = new Intent(Intent.ACTION_SEND);
                        intentEmail.setData(Uri.parse("mailto:"));
                        intentEmail.setType("text/plain");
                        intentEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{"mobilitysolutions@xtremsoftindia.com"});
                        intentEmail.putExtra(Intent.EXTRA_SUBJECT, "Xtrem TOUCH Mobile App.");
                        startActivity(Intent.createChooser(intentEmail, "Choose an email provider :"));
                    } else if (textView_anim.getText().toString().contains("www")) {   //Website
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.xtremsoftindia.com"));
                        startActivity(browserIntent);
                    }
                }
            });

            //MainActivity.overrideFonts(findViewById(R.id.xtremScreenLL));
        }catch (Exception ex){
            Log.e("FONT", "WAS NOT CREATED");
        }
    }

   /* @Override
    public void onResume() {
        ((TextView)(findViewById(R.id.aboutus))).setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.about_us_settings,0,0);
        ((TextView)(findViewById(R.id.mktWatch))).setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.trading_circ,0,0);
        ((TextView)(findViewById(R.id.homeScreen))).setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.home_circ,0,0);
        ((TextView)(findViewById(R.id.rms))).setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.rms_circ,0,0);
        ((TextView)(findViewById(R.id.bo))).setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.backoffice_icon_circ,0,0);
        ((TextView)(findViewById(R.id.call_us))).setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.call_us_circ,0,0);
        ((TextView)(findViewById(R.id.position))).setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.news_circ,0,0);
    }*/
    /*public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try {
            view = inflater.inflate(R.layout.activity_xtremscreen, container, false);
            context = MainActivity.mainContext;
            MainActivity.overrideFonts(view);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }*/

    public void slide_out_animation(final String text){
        PlayAnim(R.id.animLayout, (Context) this, R.anim.slide_left_out, 0);
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                animLayout.removeAllViews();
                animLayout.addView(textView_anim);
                textView_anim.setText("");
                textView_anim.setText(text);
                PlayAnim(R.id.animLayout,context,R.anim.slide_right_in, 0);
            }
        },700);
    }

    public void call_click(View v) {
        if(v instanceof ImageView){
            if(firstClick_call) {
                if(!firstClick_mail || !firstClick_web)
                    slide_out_animation("+912229201324");
                else {
                    animLayout.removeAllViews();
                    animLayout.addView(textView_anim);
                    textView_anim.setText("");
                    textView_anim.setText("+912229201324");
                    PlayAnim(R.id.animLayout, (Context) this, R.anim.slide_right_in, 0);
                }
                firstClick_call = false;
                firstClick_mail = true;
                firstClick_web = true;
            }
            else {
                firstClick_call = true;
                PlayAnim(R.id.animLayout, (Context) this, R.anim.slide_left_out, 0);
                Handler handle = new Handler();
                handle.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        animLayout.removeAllViews();
                    }
                },500);
            }

        }

    }



    public void email_click(View v) {
        if(v instanceof ImageView) {
            if (firstClick_mail) {
                if(!firstClick_web || !firstClick_call)
                    slide_out_animation("mobilitysolutions@xtremsoftindia.com");
                else {
                    animLayout.removeAllViews();
                    animLayout.addView(textView_anim);
                    textView_anim.setText("");
                    textView_anim.setText("mobilitysolutions@xtremsoftindia.com");
                    PlayAnim(R.id.animLayout, (Context) this, R.anim.slide_right_in, 0);
                }
                firstClick_mail = false;
                firstClick_call = true;
                firstClick_web = true;
            } else {
                firstClick_mail = true;
                PlayAnim(R.id.animLayout, (Context) this, R.anim.slide_left_out, 0);
                Handler handle = new Handler();
                handle.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        animLayout.removeAllViews();
                    }
                }, 500);
            }

        }

    }
    public void website_click(View v) {
        if(v instanceof ImageView) {
            if (firstClick_web) {
                if(!firstClick_mail || !firstClick_call)
                    slide_out_animation("http://www.xtremsoftindia.com");
                else {
                    animLayout.removeAllViews();
                    animLayout.addView(textView_anim);
                    textView_anim.setText("");
                    textView_anim.setText("http://www.xtremsoftindia.com");
                    PlayAnim(R.id.animLayout, (Context) this, R.anim.slide_right_in, 0);
                }
                firstClick_web = false;
                firstClick_mail = true;
                firstClick_call = true;
            } else {
                firstClick_web = true;
                PlayAnim(R.id.animLayout, (Context) this, R.anim.slide_left_out, 0);
                Handler handle = new Handler();
                handle.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        animLayout.removeAllViews();
                    }
                }, 500);
            }
        }
    }

    public Animation PlayAnim( int viewid, Context Con, int animationid, int StartOffset )
    {
        View v = findViewById(viewid);

        if( v != null )
        {
            Animation animation = AnimationUtils.loadAnimation(Con, animationid);
            animation.setStartOffset(StartOffset);
            v.startAnimation(animation);
            return animation;
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.DONUT)
    public void social_click(View v) {
        if(v instanceof ImageView){
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(v.getContentDescription().toString()));
            startActivity(i);
        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){

            case KeyEvent.KEYCODE_BACK:
                XTREMScreen.this.finish();
                return true;

        }
        return super.onKeyDown(keyCode, event);
    }
}