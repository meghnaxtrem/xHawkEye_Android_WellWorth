package xtrem.wellworthhawkeye;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by Admin on 2/11/14.
 */
public class xModuleClass {

    static String serverIP = "110.173.187.169";
    static String port = "51527";
    public static String strConnectString = "http://" + serverIP + ":" + port;
    //public static String strConnectString = "";
    public static String footerText = "";
    //public static int logoBackColor = Color.rgb(16, 110, 136);
    public static int logoBackColor = Color.rgb(255,255,255);


    public static boolean isInternetAvailable(Context context)
    {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();

            if (info != null)
            {
                for (int i = 0; i < info.length; i++)
                {
                    Log.i("Class", info[i].getState().toString());
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                   Log.e("","");
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String stringToDoubleString(String str){
        return valueToString(Double.parseDouble(str));
    }
    private static NumberFormat nfComma2 = null;
    public  static NumberFormat getDecimalFormatFraction2WithComma(){
        if(nfComma2 != null){
            return nfComma2;
        }
        else{
            nfComma2 = new DecimalFormat("##,##,##,##,##0.00");//("#########0.00");//
            return nfComma2;
        }
    }

    public static String rsin = "Rs.";
    /*private  static NumberFormat nf = null;
    public  static  NumberFormat getDecimalFormat(){

        if(nf != null){
            return nf;
        }
        else{
            nf = new DecimalFormat("##,##,##,##,##,###");
            return nf;
        }
    }*/
    public static String valueToString(double value){
        NumberFormat formatter = null;
        double fValue = 0.00;
        if(rsin.equals("Rs.lacs")){
            formatter = getDecimalFormatFraction2WithComma();
            fValue = (double)((double)value / 100000);
        }
        else if(rsin.equals("Rs.cr")){
            formatter = getDecimalFormatFraction2WithComma();
            fValue = (double)((double)value / 10000000);
        }
        else {
            formatter = getDecimalFormatFraction2WithComma();
            fValue = (double)value;
        }
        String str = formatter.format(fValue);
        return str;
    }

    public static int bannerImg(Context context) {
        return context.getResources().getIdentifier("@drawable/banner_img",null, context.getPackageName());
    }
}
