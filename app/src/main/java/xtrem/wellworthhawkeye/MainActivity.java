package xtrem.wellworthhawkeye;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        this.finish();
        
        //Intent myIntent = new Intent(this, SplashScreen.class);
        //myIntent.putExtra("selectedSegment", selectedSegment);
        //this.startActivity(myIntent);
        loadLoginScreen();
        //myIntent = new Intent(this, LoginScreen.class);
        //this.startActivity(myIntent);
        
        //setContentView(LoginScreen);
    }

    public void loadLoginScreen()
    {
        Intent myIntent = new Intent(this, LoginScreen.class);
        this.startActivity(myIntent);
    }
}
