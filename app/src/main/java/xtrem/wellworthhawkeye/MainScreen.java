package xtrem.wellworthhawkeye;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.method.SingleLineTransformationMethod;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;

/**
 * Created by Administrator on 1/28/14.
 */
public class MainScreen extends Activity {

    private ScrollView scrollView;
    int[] intWidthArray;
    TableLayout mainTable,ceodbForm;
    private String strConnect;
    private boolean doCreateSession;
    //IO Objects
    private DataInputStream in = null;
    private String[] mainArray;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        this.strConnect = intent.getStringExtra("strConnect"); //if it's a string you stored.
        doCreateSession = true;
        int iLogoBackColor = Color.rgb(16, 110, 136);

        ImageView imgTitle = new ImageView(this);
        int imageResource = getResources().getIdentifier("@drawable/logo", null, getPackageName());
        Bitmap bitmap3 = BitmapFactory.decodeResource(getResources(), imageResource);
        imgTitle.setImageBitmap(bitmap3);
        imgTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainScreen.this, XTREMScreen.class);
                MainScreen.this.startActivity(myIntent);
            }
        });

        TableRow trImageVentura = new TableRow(this);
        trImageVentura.setPadding(5, 2, 5, 2);
        trImageVentura.setGravity(Gravity.CENTER_HORIZONTAL);
        //trImageVentura.setBackgroundColor(iLogoBackColor);
        trImageVentura.addView(imgTitle);

        mainTable = new TableLayout(this);

        mainTable.addView(trImageVentura);
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        intWidthArray = new int[]{width/7,width/8,width/10,width/10,width/7,width/10,width/10,width/7};
/*
        ImageView img = new ImageView(this);
        int imageCeo = getResources().getIdentifier("@drawable/ceodbtitle", null, getPackageName());
        Bitmap bit12 = BitmapFactory.decodeResource(getResources(), imageCeo);
        img.setImageBitmap(bit12);
*/
        TextView title = new TextView(this);
        title.setText("CEO Dashboard");
        title.setEnabled(false);
        title.setBackgroundResource(0);
        title.setTextColor(iLogoBackColor);//(Color.rgb(0, 200, 200));
        title.setTextSize(20);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        title.setWidth(width);

        TableRow imageRow = new TableRow(this);
        imageRow.setPadding(1, 0, 1, 0);
        imageRow.setBackgroundColor(Color.BLACK);
        imageRow.setGravity(Gravity.CENTER_HORIZONTAL);
        imageRow.addView(title);

        mainTable.addView(imageRow);

        ceodbForm = new TableLayout (this);

        connect();
        //display();

        scrollView = new ScrollView(this);
        scrollView.setHorizontalScrollBarEnabled(true);
        scrollView.setVerticalScrollBarEnabled(true);
        scrollView.addView(ceodbForm);

        mainTable.addView(scrollView);
        RelativeLayout relative = new RelativeLayout(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT );
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        Button footer = new Button(this);
        footer.setText("Powered By : Xtremsoft Technologies Mumbai");
        footer.setEnabled(true);
        footer.setTypeface(Typeface.DEFAULT_BOLD);
        footer.setBackgroundResource(0);
        footer.setTextColor(iLogoBackColor);
        footer.setTextSize(12);
        footer.setGravity(Gravity.CENTER);
        footer.setTransformationMethod(SingleLineTransformationMethod.getInstance());
        footer.setWidth(width);
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainScreen.this, XTREMScreen.class);
                MainScreen.this.startActivity(myIntent);
            }
        });
        relative.addView(footer,params);

        mainTable.addView(relative,new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        super.setContentView(mainTable);
    }
    @SuppressWarnings("CallToThreadDumpStack")
    public void connect()
    {
        try
        {
            //retrieve a reference to an instance of TelephonyManager
            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(xModuleClass.strConnectString + "/ceodb/CDMyReportServlet");
            //HttpGet httpget = new HttpGet("http://61.12.61.163:5054/ceodb/runServlet");
            //HttpGet httpget = new HttpGet("http://172.168.10.16:8080/ceodbNew/runServlet");
            httpget.addHeader("User-Agent","Profile/MIDP-2.0 Confirguration/CLDC-1.0");
            httpget.addHeader("Accept","text/plain");
            httpget.addHeader("IMEI", getDeviceID(telephonyManager));
            httpget.addHeader("Ph-No", telephonyManager.getLine1Number());
            httpget.addHeader("DoCreateSession", doCreateSession+"");
            HttpResponse response = httpclient.execute(httpget);
            in = new DataInputStream(response.getEntity().getContent());

            int i = 0;
            String str;
            str = in.readUTF();
            //Dialog.alert(str);
            if(str.equals("NR") == false)
            {
                while(str!=null)
                {
                   /* //System.out.println("Str : " + str);
                    segment [i] = str;
                    System.out.println("=========" + segment [i]);
                    turnover [i] = Double.parseDouble(in.readUTF());
                    //totalTurnover += turnover [i];
                    //System.out.println("=========" + turnover [i]);
                    trade [i] = Integer.parseInt(in.readUTF());
                    //totalTrade += trade [i];
                    //System.out.println("=========" + trade [i]);
                    activeClients [i] = Integer.parseInt(in.readUTF());
                    //totalActiveClients += activeClients [i];
                    //System.out.println("=========" + activeClients [i]);
                    pro [i] = Integer.parseInt(in.readUTF());
                    //totalPro += pro [i];
                    //System.out.println("=========" + pro [i]);
                    client [i] = Integer.parseInt(in.readUTF());
                    //totalClient += client [i];
                    //System.out.println("=========" + client [i]);
                    longData [i] = Double.parseDouble(in.readUTF());
                    //totalLongData += longData [i];
                    //System.out.println("=========" + longData [i]);
                    shortData [i] = Double.parseDouble(in.readUTF());
                    //totalShortData += shortData [i];
                    //System.out.println("=========" + shortData [i]);
                    M2M [i] = Double.parseDouble(in.readUTF());
                    //totalM2M += M2M [i];
                    //System.out.println("=========" + M2M [i]);
                    payInOut [i] = Double.parseDouble(in.readUTF());
                    //totalPayInOut += payInOut [i];
                    //System.out.println("=========" + payInOut [i]);
                    mktTO [i] = Double.parseDouble(in.readUTF());
                    //totalMktTO += mktTO [i];
                    //System.out.println("=========" + mktTO [i]);
                    i++;
                    str = in.readUTF();
                    if(str==null)
                    {
                        break;
                    }
                    count++;
                    //System.out.println("=========" + count);*/
                }
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Your device is not registered with xHawkEye...\nPlease register your device...")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.dismiss();
                                System.exit(0);
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
            in.close();
            in = null;
            //httpConn.close();
            //httpConn = null;
        }
        catch(EOFException e)
        {
            //System.out.println("EOF Exception in connect()");
            //System.out.println(e.getMessage());
            //System.out.println(e.toString());
            //e.printStackTrace();
        }
        catch (IOException ie)
        {
            System.out.println("IO Exception in connect()");

            System.out.println(ie.getMessage());
            System.out.println(ie.toString());
            ie.printStackTrace();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(ie.toString())
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        catch(Exception e)
        {
            System.out.println(e.toString());

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(e.toString())
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

        finally
        {
            //if (in != null)
            //try { in.close(); } catch (IOException ignore) {} catch (Exception ignore) {}
        }
    }

    String getDeviceID(TelephonyManager phonyManager)
    {
        String id = phonyManager.getDeviceId();
        if (id == null)
        {
            id = "not available";
        }
        int phoneType = phonyManager.getPhoneType();
        switch(phoneType)
        {
            case TelephonyManager.PHONE_TYPE_NONE:
                //return "NONE: " + id;
                return id;

            case TelephonyManager.PHONE_TYPE_GSM:
                //return "GSM: IMEI=" + id;
                return id;

            case TelephonyManager.PHONE_TYPE_CDMA:
                //return "CDMA: MEID/ESN=" + id;
                return id;

            default:
                return "UNKNOWN: ID=" + id;
        }
    }
}