/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xtrem.wellworthhawkeye;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TableLayout;

/**
 *
 * @author Administrator
 */
public class SplashScreen extends Activity
{   
    //Splash Screen
    private ImageView splashScreen;
    private ImageView loadingIcon;
    
    //TableLayout
    TableLayout splashLayout;
    
    //Timer Object
    private CountDownTimer cdTimer;
    private CountDownTimer splashCountDown;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);
        
        try
        {
            splashLayout = new TableLayout(this);
            
            splashScreen = new ImageView(this);
        
            int imageResource = getResources().getIdentifier("@drawable/hawkeyesplash", null, getPackageName());
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageResource);
            splashScreen.setImageBitmap(bitmap);

            loadingIcon = new ImageView(this);

            int imageResource2 = getResources().getIdentifier("@drawable/loading", null, getPackageName());
            Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), imageResource2);
            loadingIcon.setImageBitmap(bitmap2);

            this.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            splashLayout.addView(splashScreen, 0);
            splashLayout.addView(loadingIcon, 1);

            super.setContentView(R.layout.splashscreen);

            splashCountDown = new CountDownTimer(5000, 5000)
            {
                public void onTick(long millisUntilFinished)
                {
                    //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                }

                public void onFinish()
                {
                    SplashScreen.this.finish();
                    loadLoginScreen();
                }
            }.start();     
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        } 
    }
    
    public void loadLoginScreen()
    {
        Intent myIntent = new Intent(this, LoginScreen.class);
        //myIntent.putExtra("selectedSegment", selectedSegment);
        this.startActivity(myIntent);
    }
    
}
