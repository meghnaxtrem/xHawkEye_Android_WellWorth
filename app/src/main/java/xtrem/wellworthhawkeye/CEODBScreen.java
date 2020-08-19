/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xtrem.wellworthhawkeye;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.method.SingleLineTransformationMethod;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author Administrator
 */
public class CEODBScreen extends Activity implements View.OnClickListener
{
    //String Variables
    private String segment [];

    //Image View Objects
    private ImageView ceoDBTitle;

    //int variables
    private int count = 0;
    private int trade [], activeClients [], pro [], client [];
    //private int totalTrade, totalActiveClients, totalPro, totalClient;
    private int fontSize, fontSizeTitle;

    //IO Objects
    private DataInputStream in = null;

    //double variables
    private double turnover [], longData [], shortData [], M2M [], payInOut [], mktTO [];
    //private double totalTurnover, totalLongData, totalShortData, totalM2M, totalPayInOut, totalMktTO;

    //Label Objects
    private TextView lblSegment [],lblTrades [], lblClients [], lblM2M [], lblTurnover [], lblLongShort [];
    private TextView lblProClient [], lblPayInOut [], lblMktTO [];
    private TextView lblTradesTitle, lblClientsTitle, lblM2MTitle, lblSegmentTitle, lblTurnoverTitle;
    private TextView lblLongShortTitle, lblProClientTitle, lblPayInOutTitle, lblMktTOTitle;

    //Table Layout object
    private TableLayout ceodbForm,mainTable;

    //Menu Items
    private MenuItem mnuRefresh;
    private MenuItem mnuExit;

    private boolean doCreateSession;

    private String g_version;

    private String isValid;
    private String strConnect;

    //Scroll View Object
    private ScrollView scrollView;
    int[] intWidthArray;

    static final int TIME_DIALOG_ID = 1111;
    private TextView output;

    private int hour,minute,width;
    private Calendar c;
    TableRow tr1;

    SimpleDateFormat format1 = new SimpleDateFormat("ddMMMyy");
    private String date = "";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);

        c=Calendar.getInstance();

        date = format1.format(c.getTime());
        hour=c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        doCreateSession = true;

        LinearLayout topLayout = new LinearLayout(this);
        topLayout.setId(1);
        topLayout.setOrientation(LinearLayout.VERTICAL);

        Intent intent = getIntent();
        this.strConnect = intent.getStringExtra("strConnect"); //if it's a string you stored.

        // ceoDBTitle = new ImageView(this);

        int iLogoBackColor = xModuleClass.logoBackColor;//Color.rgb(16, 110, 136);
        ImageView imgTitle = new ImageView(this);
        int imageResource = xModuleClass.bannerImg(this);

        imgTitle.setBackgroundColor(getResources().getColor(R.color.text_green));
        Bitmap bitmap3 = BitmapFactory.decodeResource(getResources(), imageResource);
        imgTitle.setImageBitmap(bitmap3);
        imgTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(CEODBScreen.this, XTREMScreen.class);
                CEODBScreen.this.startActivity(myIntent);
            }
        });

        ImageView imgRefresh = new ImageView(this);
        imgRefresh.setPadding(0,10,25,0);
        imgRefresh.setImageResource(R.drawable.refresh);
        imgRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshRate();
            }
        });

        TableRow trImageVentura = new TableRow(this);
        trImageVentura.setPadding(5, 2, 5, 2);
        //trImageVentura.setGravity(Gravity.CENTER_HORIZONTAL);
        trImageVentura.setBackgroundColor(iLogoBackColor);
        RelativeLayout logo=new RelativeLayout(this);
        RelativeLayout.LayoutParams param1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT );
        param1.addRule(RelativeLayout.CENTER_IN_PARENT);
        RelativeLayout.LayoutParams param2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT );
        param2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        //logo.setGravity(Gravity.CENTER);
        logo.addView(imgTitle,param1);
        logo.addView(imgRefresh,param2);
        //logo.setBackgroundColor(iLogoBackColor);
        trImageVentura.addView(logo);
        topLayout.addView(trImageVentura);
        //trImageVentura.addView(imgRefresh);

        mainTable = new TableLayout(this);
        mainTable.setId(2);

        //mainTable.addView(trImageVentura);
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        int height = display.getHeight();

      /*  intWidthArray = new int[]{(width/8)+15,
                (width/8),
                (width/8),
                (width/8)-15,
                (width/8),
                (width/8)-15,
                (width/8),
                (width/8)+15};*/

        intWidthArray = new int[]{(width/8)+15,
                (width/8),
                (width/8),
                (width/8)-15,
                (width/8)-10,
                (width/8)-20,
                (width/8),
                (width/8)+30};
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

        topLayout.addView(imageRow);
        topLayout.addView(mainTable);

        /*try
        {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;

            int vP1C = Integer.parseInt(version.substring(0, version.indexOf(".")));
            String str = version.substring(version.indexOf(".") + 1);
            int vP2C = Integer.parseInt(str.substring(0, str.indexOf(".")));
            str = str.substring(str.indexOf(".") + 1);
            int vP3C = Integer.parseInt(str.substring(0, str.indexOf(".")));
            str = str.substring(str.indexOf(".") + 1);
            int vP4C = Integer.parseInt(str);

            getVersion();
            version = g_version;

            int vP1S = Integer.parseInt(version.substring(0, version.indexOf(".")));
            str = version.substring(version.indexOf(".") + 1);
            int vP2S = Integer.parseInt(str.substring(0, str.indexOf(".")));
            str = str.substring(str.indexOf(".") + 1);
            int vP3S = Integer.parseInt(str.substring(0, str.indexOf(".")));
            str = str.substring(str.indexOf(".") + 1);
            int vP4S = Integer.parseInt(str);

            System.out.println(vP4S+ " > " +vP4C+ " | " +vP3S+ " > " +vP3C+ " | " +vP2S+ " > " +vP2C+ " | " +vP1S+" > " +vP1C);

            if(vP4S > vP4C | vP3S > vP3C | vP2S > vP2C | vP1S > vP1C)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Update available... Do you want to update your application now?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.dismiss();
                                //System.exit(0);

                                //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://61.12.61.163:5054/ceodb/Android/xHawkEye-release.apk"));
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strConnect + "/ceodb/Android/xHawkEye.apk"));
                                startActivity(browserIntent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }

        }
        catch(PackageManager.NameNotFoundException ne)
        {
            System.out.println(ne.toString());
        }*/

        ceodbForm = new TableLayout (this);
        new GetData().execute();

        scrollView = new ScrollView(this);
        scrollView.setHorizontalScrollBarEnabled(true);
        scrollView.setVerticalScrollBarEnabled(true);
        scrollView.addView(ceodbForm);

        LinearLayout footerlayout=new LinearLayout(this);
        footerlayout.setOrientation(LinearLayout.HORIZONTAL);
        footerlayout.setId(3);
        footerlayout.setMinimumWidth(width);

        Button footer = new Button(this);
        footer.setText("Powered By : Xtremsoft Technologies Mumbai");
        footer.setEnabled(true);
        footer.setWidth(2*(width/3));
        //footer.setId(3);
        footer.setTypeface(Typeface.DEFAULT_BOLD);
        footer.setBackgroundResource(0);
        footer.setTextColor(getResources().getColor(R.color.xtrem_greeen));
        footer.setTextSize(12);
        footer.setGravity(Gravity.RIGHT);
        footer.setTransformationMethod(SingleLineTransformationMethod.getInstance());
        //footer.setWidth(width);
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(CEODBScreen.this, XTREMScreen.class);
                CEODBScreen.this.startActivity(myIntent);
            }
        });


        output=new TextView(this);
        updateTime(hour, minute, date);
        //output.setText(hour+":"+minute);
        footerlayout.addView(output);
        footerlayout.addView(footer);

        RelativeLayout main = new RelativeLayout(this);
        RelativeLayout.LayoutParams param_tab = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param_tab.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
        main.addView(topLayout,param_tab);

        RelativeLayout.LayoutParams param_tab1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param_tab1.addRule(RelativeLayout.BELOW,topLayout.getId());
        param_tab1.addRule(RelativeLayout.ABOVE,footerlayout.getId());
        main.addView(scrollView,param_tab1);

        RelativeLayout.LayoutParams param_tab3 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param_tab3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        main.addView(footerlayout,param_tab3);

        super.setContentView(main);
        //super.setContentView(mainTable);
    }

    public final void getVersion()
    {
        try
        {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(xModuleClass.strConnectString + "/ceodb/getVersion");
            //HttpGet httpget = new HttpGet("http://219.64.39.25:5054/ceodb/runSegAllowedServlet");
            //HttpGet httpget = new HttpGet("http://61.12.61.163:5054/ceodb/runSegAllowedServlet");
            httpget.addHeader("User-Agent","Profile/MIDP-2.0 Confirguration/CLDC-1.0");
            httpget.addHeader("Accept","text/plain");
            HttpResponse response = httpclient.execute(httpget);
            in = new DataInputStream(response.getEntity().getContent());

            g_version = in.readUTF();

            in.close();
            in = null;
        }
        catch (EOFException e)
        {
            //Dialog.alert("Data Loaded...!!!");
        }
        catch (IOException e)
        {
            //Dialog.alert("IOException in connect() > Message : " + e.getMessage() + " : " + e.toString());
            System.out.println("Caught IOException: " + e.toString());
            System.err.println("Caught IOException: " + e.getMessage());
        }
        catch (Exception e)
        {
            //Dialog.alert("Exception in connect() > Message : " + e.getMessage() + " : " + e.toString());
            System.out.println("Caught Exception: " + e.toString());
            System.err.println("Caught Exception: " + e.getMessage());
        }
        finally
        {
            if (in != null)
                try { in.close(); } catch (IOException ignore) {}
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        //mnuRefresh = menu.add(Menu.NONE,2,Menu.NONE,"Refresh");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == 2)
        {
            //refreshRate();

        }
        return false;
    }


  private class GetData extends AsyncTask<Void,Void,Void>{

      @Override
      protected Void doInBackground(Void... voids) {
          connectAndGetData();
          runOnUiThread(() -> display());
          return null;
      }
  }


    @SuppressWarnings("CallToThreadDumpStack")
    public void connectAndGetData()
    {
        segment = new String [10];
        turnover = new double [10];
        trade = new int [10];
        activeClients = new int [10];
        longData = new double [10];
        shortData = new double [10];
        pro = new int [10];
        client = new int [10];
        M2M = new double [10];
        payInOut = new double [10];
        mktTO = new double [10];

/*
        totalTurnover = 0;
        totalTrade = 0;
        totalActiveClients = 0;
        totalLongData = 0;
        totalShortData = 0;
        totalPro = 0;
        totalClient = 0;
        totalM2M = 0;
        totalPayInOut = 0;
        totalMktTO = 0;
*/
        count = 0;

        try
        {
            //retrieve a reference to an instance of TelephonyManager
            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(xModuleClass.strConnectString + "/ceodb/runServlet");
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
                    //System.out.println("Str : " + str);
                    segment [i] = str;
                    System.out.println("========="+ segment [i]);
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
                    //System.out.println("=========" + count);
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

    @TargetApi(Build.VERSION_CODES.BASE_1_1)
    @SuppressWarnings("CallToThreadDumpStack")
    public void display()
    {
        TableLayout titleTable = new TableLayout(this);
        mainTable.removeAllViews();
        ceodbForm.removeAllViews();
        int textSize = 12;
        int textColor = Color.WHITE; //Color.rgb(0, 200, 200));
        lblSegmentTitle = new TextView(this);
        lblSegmentTitle.setText("Segment");
        lblSegmentTitle.setPadding(5, 0, 0, 0);
        lblSegmentTitle.setEnabled(false);
        lblSegmentTitle.setBackgroundResource(0);
        lblSegmentTitle.setTextColor(textColor);//Color.rgb(0, 200, 200));
        lblSegmentTitle.setTextSize(textSize);
        lblSegmentTitle.setTypeface(Typeface.DEFAULT_BOLD);
        lblSegmentTitle.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        lblSegmentTitle.setWidth(intWidthArray[0]);

        lblTurnoverTitle = new TextView(this);
        lblTurnoverTitle.setText("TO(Cr)");
        lblTurnoverTitle.setEnabled(false);
        lblTurnoverTitle.setBackgroundResource(0);
        lblTurnoverTitle.setTextColor(textColor);//(Color.rgb(0, 200, 200));
        lblTurnoverTitle.setTextSize(textSize);
        lblTurnoverTitle.setGravity(Gravity.RIGHT);
        lblTurnoverTitle.setTypeface(Typeface.DEFAULT_BOLD);
        lblTurnoverTitle.setWidth(intWidthArray[1]);

        lblTradesTitle = new TextView(this);
        lblTradesTitle.setText("Trades ");
        lblTradesTitle.setEnabled(false);
        lblTradesTitle.setBackgroundResource(0);
        lblTradesTitle.setTextColor(textColor);//(Color.rgb(0, 200, 200));
        lblTradesTitle.setTextSize(textSize);
        lblTradesTitle.setGravity(Gravity.RIGHT);
        lblTradesTitle.setTypeface(Typeface.DEFAULT_BOLD);
        lblTradesTitle.setWidth(intWidthArray[2]);

        lblClientsTitle = new TextView(this);
        lblClientsTitle.setText("Clients ");
        lblClientsTitle.setEnabled(false);
        lblClientsTitle.setBackgroundResource(0);
        lblClientsTitle.setTextColor(textColor);
        lblClientsTitle.setTextSize(textSize);
        lblClientsTitle.setGravity(Gravity.RIGHT);
        lblClientsTitle.setTypeface(Typeface.DEFAULT_BOLD);
        lblClientsTitle.setWidth(intWidthArray[3]);

        lblLongShortTitle = new TextView(this);
        lblLongShortTitle.setText("Lng:Shrt ");
        lblLongShortTitle.setEnabled(false);
        lblLongShortTitle.setBackgroundResource(0);
        lblLongShortTitle.setTextColor(textColor);
        lblLongShortTitle.setTextSize(textSize);
        lblLongShortTitle.setGravity(Gravity.RIGHT);
        lblLongShortTitle.setTypeface(Typeface.DEFAULT_BOLD);
        lblLongShortTitle.setWidth(intWidthArray[4]);
/*
        lblProClientTitle = new TextView(this);
        lblProClientTitle.setText("Pro:Client");
        lblProClientTitle.setEnabled(false);
        lblProClientTitle.setBackgroundResource(0);
        lblProClientTitle.setTextColor(Color.rgb(0, 200, 200));
        lblProClientTitle.setTextSize(textSize);
*/
        lblM2MTitle = new TextView(this);
        lblM2MTitle.setText("M2M\n(Lac)");
        lblM2MTitle.setEnabled(false);
        lblM2MTitle.setBackgroundResource(0);
        lblM2MTitle.setTextColor(textColor);
        lblM2MTitle.setTextSize(textSize);
        lblM2MTitle.setGravity(Gravity.RIGHT);
        lblM2MTitle.setTypeface(Typeface.DEFAULT_BOLD);
        //lblM2MTitle.setTransformationMethod(SingleLineTransformationMethod.getInstance());
        lblM2MTitle.setWidth(intWidthArray[5]);

        lblPayInOutTitle = new TextView(this);
        //lblPayInOutTitle.setText("Pay I/O \n Rs.(crore)");
        lblPayInOutTitle.setText("Pay I/O\n(Cr)");
        lblPayInOutTitle.setEnabled(false);
        lblPayInOutTitle.setBackgroundResource(0);
        lblPayInOutTitle.setTextColor(textColor);
        lblPayInOutTitle.setTextSize(textSize);
        lblPayInOutTitle.setGravity(Gravity.RIGHT);
        lblPayInOutTitle.setTypeface(Typeface.DEFAULT_BOLD);
        lblPayInOutTitle.setWidth(intWidthArray[6]);

        lblMktTOTitle = new TextView(this);
        lblMktTOTitle.setText("Mkt TO\n(Cr)");
        lblMktTOTitle.setEnabled(false);
        lblMktTOTitle.setBackgroundResource(0);
        lblMktTOTitle.setTextColor(textColor);
        lblMktTOTitle.setTextSize(textSize);
        lblMktTOTitle.setGravity(Gravity.RIGHT);
        lblMktTOTitle.setTypeface(Typeface.DEFAULT_BOLD);
        lblMktTOTitle.setWidth(intWidthArray[7]);

        lblSegment = new TextView [count + 1];
        lblTurnover = new TextView [count + 1];
        lblTrades = new TextView [count + 1];
        lblClients = new TextView [count + 1];
        lblLongShort = new TextView [count + 1];
        //lblProClient = new TextView [count + 1];
        lblM2M = new TextView [count + 1];
        lblPayInOut = new TextView [count + 1];
        lblMktTO = new TextView [count + 1];

        TableRow tr = new TableRow(this);
        tr.setBackgroundColor(Color.DKGRAY);
        tr.addView(lblSegmentTitle);
        tr.addView(lblTurnoverTitle);
        tr.addView(lblTradesTitle);
        tr.addView(lblClientsTitle);
        tr.addView(lblLongShortTitle);
        //tr.addView(lblProClientTitle);
        tr.addView(lblM2MTitle);
        tr.addView(lblPayInOutTitle);
        tr.addView(lblMktTOTitle);

        /*String[] str={"","Rs.(crore)","","","","Rs.(lakhs)","Rs.(crore)",""};
        TableRow tr1=new TableRow(this);

        TextView lbl1=new TextView(this);
        lbl1.setText(str[]);
        TextView lbl1=new TextView(this);
        TextView lbl1=new TextView(this);
        TextView lbl1=new TextView(this);
        TextView lbl1=new TextView(this);
        TextView lbl1=new TextView(this);*/


        //ceodbForm.addView(trImage);
        titleTable.addView(tr);
        mainTable.addView(titleTable);
        //int textSize = 12;
        try
        {
            for(int i = 0; i <= count; i++)
            {
                tr1 = new TableRow(this);
                //tr1.setOnClickListener(this);
                tr1.setId(i);
                int blackGradientImgSrc2;
                if(i==count){
                    blackGradientImgSrc2 = getResources().getIdentifier("@drawable/gradient_blue", null, getPackageName());
                }
                else{
                    blackGradientImgSrc2 = getResources().getIdentifier("@drawable/black_gradient", null, getPackageName());
                }
                tr1.setBackgroundResource(blackGradientImgSrc2);

                lblSegment[i] = new TextView(this);
                lblSegment[i].setText(segment [i]);
                lblSegment[i].setPadding(5,0,0,0);
                lblSegment[i].setTag(1);
                if(!segment[i].contains("Totals"))
                {
                    lblSegment[i].setOnClickListener(this);

                }
                //lblSegment[i].setOnClickListener(this);
                lblSegment[i].setBackgroundResource(0);
                lblSegment[i].setTextColor(Color.rgb(255, 255, 255));
                lblSegment[i].setTextSize(textSize);
                //lblSegment[i].setPadding(5, 5, 10, 10);
                lblSegment[i].setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                lblSegment[i].setWidth(intWidthArray[0]);

                tr1.addView(lblSegment[i]);

                lblTurnover[i] = new TextView(this);
                lblTurnover[i].setText(""+convertTurnover(turnover [i])+" ");
                lblTurnover[i].setTag(1);

                if(i!=count)
                {
                    lblTurnover[i].setOnClickListener(this);
                }
                //lblTurnover[i].setEnabled(false);
                lblTurnover[i].setBackgroundResource(0);
                lblTurnover[i].setTextColor(Color.rgb(255, 204, 0));
                lblTurnover[i].setMaxLines(1);
                lblTurnover[i].setTextSize(textSize);
                //lblTurnover[i].setPadding(5, 5, 10, 10);
                lblTurnover[i].setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);//0x10 | 0x05 | 0x80 | 0x08);
                lblTurnover[i].setWidth(intWidthArray[1]);

                tr1.addView(lblTurnover[i]);

                lblTrades[i] = new TextView(this);
                /*if(i==count)
                    lblTrades[i].setText(""+totalTrade+" ");
                else*/
                lblTrades[i].setText(""+trade [i]+" ");
                //lblTrades[i].setEnabled(false);
                lblTrades[i].setBackgroundResource(0);
                lblTrades[i].setTag(1);
                if(i!=count)
                {
                    lblTrades[i].setOnClickListener(this);
                }
                //lblTrades[i].setTextColor(Color.rgb(255, 255, 102));
                lblTrades[i].setTextColor(Color.rgb(255, 255, 0));
                lblTrades[i].setTextSize(textSize);
                lblTrades[i].setMaxLines(1);
                //lblTrades[i].setPadding(5, 5, 10, 10);
                lblTrades[i].setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
                lblTrades[i].setWidth(intWidthArray[2]);

                tr1.addView(lblTrades[i]);

                lblClients[i] = new TextView(this);
                lblClients[i].setText(""+activeClients [i]+" ");
                //lblClients[i].setEnabled(false);
                lblClients[i].setBackgroundResource(0);
                //lblClients[i].setTextColor(Color.rgb(192, 192, 192));
                lblClients[i].setTag(1);
                if(i!=count)
                {
                    lblClients[i].setOnClickListener(this);
                }
                lblClients[i].setTextColor(Color.rgb(255, 255, 255));
                lblClients[i].setTextSize(textSize);
                lblClients[i].setMaxLines(1);
                //lblClients[i].setPadding(5, 5, 10, 10);
                lblClients[i].setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
                lblClients[i].setWidth(intWidthArray[3]);

                tr1.addView(lblClients[i]);

                lblLongShort[i] = new TextView(this);
               /* if(i==count)
                    lblLongShort[i].setText("- ");
                else*/
                lblLongShort[i].setText(""+convertLongShort(longData [i]) + ":" + convertLongShort(shortData [i])+" ");
                //lblLongShort[i].setEnabled(false);
                lblLongShort[i].setBackgroundResource(0);
                lblLongShort[i].setTag(1);
                if(i!=count)
                {
                    lblLongShort[i].setOnClickListener(this);
                }
                //lblLongShort[i].setTextColor(Color.rgb(192, 192, 192));
                lblLongShort[i].setTextColor(Color.rgb(255, 255, 255));
                lblLongShort[i].setTextSize(textSize);
                lblLongShort[i].setMaxLines(1);
                //lblLongShort[i].setPadding(5, 5, 10, 10);
                lblLongShort[i].setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
                lblLongShort[i].setWidth(intWidthArray[4]);

                tr1.addView(lblLongShort[i]);

                //tr.addView(lblProClient[i]);

                lblM2M[i] = new TextView(this);

                lblM2M [i].setText(""+convert(M2M [i])+" ");
                if (M2M [i] < 0)
                {
                    lblM2M [i].setTextColor(Color.RED);
                    lblM2M [i].setText("(" + convert(M2M [i]).substring(1) + ") ");
                }
                else
                {
                    lblM2M [i].setTextColor(Color.GREEN);
                }

                //lblM2M[i].setEnabled(false);
                lblM2M[i].setBackgroundResource(0);
                lblM2M[i].setTag(1);
                if(i!=count)
                {
                    lblM2M[i].setOnClickListener(this);
                }
                lblM2M[i].setTextSize(textSize);
                lblM2M[i].setMaxLines(1);
                //lblM2M[i].setPadding(5, 5, 10, 10);
                lblM2M[i].setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
                lblM2M[i].setWidth(intWidthArray[5]);

                tr1.addView(lblM2M[i]);

                lblPayInOut[i] = new TextView(this);
                lblPayInOut [i].setText(""+convert(payInOut [i])+" ");
                if (payInOut [i] < 0)
                {
                    lblPayInOut [i].setTextColor(Color.RED);
                    lblPayInOut [i].setText("(" + convert(payInOut [i]).substring(1) + ") ");
                }
                else
                {
                    lblPayInOut [i].setTextColor(Color.GREEN);
                }

                //lblPayInOut[i].setEnabled(false);
                lblPayInOut[i].setTag(1);
                if(i!=count)
                {
                    lblPayInOut[i].setOnClickListener(this);
                }
                lblPayInOut[i].setBackgroundResource(0);
                lblPayInOut[i].setTextSize(textSize);
                lblPayInOut[i].setMaxLines(1);
                //lblPayInOut[i].setPadding(5, 5, 10, 10);
                lblPayInOut[i].setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);//0x10 | 0x05 | 0x80 | 0x08);
                lblPayInOut[i].setWidth(intWidthArray[6]);


                tr1.addView(lblPayInOut[i]);

                LinearLayout.LayoutParams childParams = new LinearLayout.LayoutParams(intWidthArray[7],
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                //childParams.setMargins(0, 0, 5, 0);
                lblMktTO[i] = new TextView(this);
                /*if(i==count)
                    lblMktTO[i].setText(""+convert(totalMktTO)+" ");
                else*/
                lblMktTO[i].setText(""+convert(mktTO [i])+"  ");
                //lblMktTO[i].setEnabled(false);
                lblMktTO[i].setBackgroundResource(0);
                // lblMktTO[i].setBackgroundColor(Color.BLUE);
                //lblMktTO[i].setTextColor(Color.rgb(192, 192, 192));
                lblMktTO[i].setTextColor(Color.rgb(255, 255, 255));
                lblMktTO[i].setTag(1);
                if(i!=count)
                {
                    lblMktTO[i].setOnClickListener(this);
                }
                // lblMktTO[i].setPadding(0,0,0,0);
                lblMktTO[i].setMaxLines(3);
                lblMktTO[i].setEllipsize(TextUtils.TruncateAt.END);

                lblMktTO[i].setHorizontallyScrolling(true);
                lblMktTO[i].setTextSize(textSize);
                //lblMktTO[i].setMaxLines(1);
                //lblMktTO[i].setPadding(0,0,5,0);
                //lblMktTO[i].setPadding(5, 5, 10, 10);
                lblMktTO[i].setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);//0x10 | 0x05 | 0x80 | 0x08);
                lblMktTO[i].setWidth(intWidthArray[7]);
                tr1.addView(lblMktTO[i]);

                //lblMktTO[i].setTransformationMethod(SingleLineTransformationMethod.getInstance());

                ceodbForm.addView(tr1);
            }
            doCreateSession = false;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.print(e.getMessage());
            System.out.println(" : " + e.toString());
        }
    }

    public void loadSegmentScreen(String selectedSegment)
    {
        Intent myIntent = new Intent(this, SegmentDataScreen.class);
        myIntent.putExtra("selectedSegment", selectedSegment);
        myIntent.putExtra("strConnect", strConnect);
        this.startActivityForResult(myIntent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 100)
        {
            String result = data.getStringExtra("isClose");
            if(result.equals("true"))
            {
                this.finish();
                System.exit(0);
            }
        }
    }

    public void onClick(View v)
    {

        if(v instanceof TextView){

            TextView txtView = (TextView)v;
            int selecTionIndex = -1;

            selecTionIndex = Integer.parseInt(txtView.getTag().toString());
            if(selecTionIndex != -1){

                //String[] rowData = rowDataVector.get(selecTionIndex);
                //if(int i=0;i<=count;i++)
                TableRow tr = (TableRow)txtView.getParent();

                final String selectSegment =  ((TextView)(tr.getChildAt(0))).getText().toString();
                //lblSegment[].getText().toString().trim();
                final String msg = "Viewing details of segment : \n" +selectSegment+ " Proceed?";
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(msg)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.dismiss();
                                loadSegmentScreen(selectSegment);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
        // }
        /*TableRow tableRow = (TableRow) v;
        int id=tableRow.getId();
        Toast.makeText(getApplicationContext(),"this is id of"+lblSegment[id],Toast.LENGTH_SHORT).show();

        TextView btn=(TextView)v;
        if(btn != lblSegment [count])
        {
            final String msg = "Viewing details of segment : " + btn.getText().toString() + "\n Proceed?";
            final String selSegment = btn.getText().toString();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            dialog.dismiss();
                            loadSegmentScreen(selSegment);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            }*/
    }
        /*TextView btn = (TextView)v;
        if(btn != lblSegment [count])
        {
            final String msg = "Viewing details of segment : " + btn.getText().toString() + "\n Proceed?";
            final String selSegment = btn.getText().toString();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            dialog.dismiss();
                            loadSegmentScreen(selSegment);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }*/

    @SuppressWarnings("CallToThreadDumpStack")
    public void refreshRate()
    {

        //final Calendar c = Calendar.getInstance();
        c=Calendar.getInstance();
        date = format1.format(c.getTime());
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        updateTime(hour,minute,date);
        new GetData().execute();
        try
        {
           /* int i;
            for(i = 0; i <=count ; i++)
            {
                //System.out.println("=========" + count);
                lblTurnover [i].setText("" + convertTurnover(turnover [i]) + "");
                //lblTurnover[i].setWidth(intWidthArray[1]);
                lblTrades [i].setText("" + trade[i]);
                //lblTrades[i].setWidth(intWidthArray[2]);
                lblClients [i].setText("" + activeClients[i]);
                //lblClients[i].setWidth(intWidthArray[3]);
                lblLongShort [i].setText("" + convertLongShort(longData [i]) + ":" + convertLongShort(shortData [i]));
                //lblLongShort[i].setWidth(intWidthArray[4]);
                //lblProClient [i].setText("" + pro [i] + ":" + client [i]);
                if(M2M [i] >= 0)
                {
                    lblM2M  [i].setText(""+convert(M2M [i]));
                    lblM2M [i].setTextColor(Color.GREEN);
                    //lblM2M[i].setWidth(intWidthArray[5]);
                }
                else
                {
                    lblM2M [i].setText("("+convert(M2M [i]).substring(1) + ")");
                    lblM2M [i].setTextColor(Color.RED);
                    //lblM2M[i].setWidth(intWidthArray[5]);
                }

                if(payInOut [i] >= 0)
                {
                    lblPayInOut  [i].setText(""+convert(payInOut [i]));
                    lblPayInOut [i].setTextColor(Color.GREEN);
                    //lblPayInOut [i].setWidth(intWidthArray[6]);
                }
                else
                {
                    lblPayInOut [i].setText("("+convert(payInOut [i]).substring(1) + ")");
                    lblPayInOut [i].setTextColor(Color.RED);
                    //lblPayInOut [i].setWidth(intWidthArray[6]);
                }
                lblMktTO[i].setText(""+convert(mktTO [i])+" ");
                //lblMktTO[i].setWidth(intWidthArray[7]);
            }

            scrollView.refreshDrawableState();
            ceodbForm.refreshDrawableState();*/

        }
        catch(Exception e)
        {
            //Dialog.alert("Exception in refreshRate() > Message : " + e.getMessage() + " : " + e.toString());
            e.printStackTrace();
            System.out.print(e.getMessage());
            System.out.println(" : " + e.toString());

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
    }

    public String convert(double num)
    {
        String numString = ""+num;
        int index = numString.indexOf(".");
        if (index == -1)
        {
            return numString;
        }
        else
        {
            String returnString = numString.substring(0, index + 2);
            //Dialog.alert(returnString);
            try
            {
                int digit = Integer.parseInt(numString.substring(index + 2, index + 3));
                try
                {
                    int digit2 = Integer.parseInt(numString.substring(index + 3, index + 4));
                    if (digit2 > 4)
                    {
                        digit = digit + 1;
                        if(digit > 9)
                        {
                            digit = 0;
                            returnString = numString.substring(0, index + 1);
                            digit2 = Integer.parseInt(numString.substring(index + 1, index + 2));
                            digit2 += 1;
                            returnString += digit2;
                            if(digit2 > 9)
                            {
                                digit = Integer.parseInt(numString.substring(0, index));
                                digit += 1;
                                returnString = digit + ".00";
                                return returnString;
                            }
                        }
                    }
                }
                catch(Exception e) {}
                returnString = returnString + digit;
            }
            catch (Exception e) {}
            return returnString;
        }
    }
    public String convertLongShort(double num)
    {
        String numString = ""+num;
        int index = numString.indexOf(".");
        if (index == -1)
        {
            return numString;
        }
        else
        {
            String returnString = numString.substring(0, index);
            //Dialog.alert(returnString);
            try
            {
                int digit = Integer.parseInt(numString.substring(index + 1, index + 2));
                if (digit > 4)
                {
                    digit = Integer.parseInt(numString.substring(0, index));
                    digit += 1;
                    returnString = digit + "";
                }
            }
            catch (Exception e) {}
            return returnString;
        }
    }

    public String convertTurnover(double num)
    {
        String numString = ""+num;
        int eIndex = numString.indexOf("E");
        try
        {
            int decimal = Integer.parseInt(numString.substring(eIndex + 1, numString.length()));
            numString = numString.substring(0, eIndex);
            int index = numString.indexOf(".");
            String strPart1 = numString.substring(0, index);
            String strPart2 = numString.substring(index + 1, numString.length());
            int decimalcount = decimal + 2 - strPart2.length();
            for (int i = 0; i < decimalcount ; i++)
            {
                strPart2 += "0";
            }
            numString = strPart1 + strPart2;
            try
            {
                strPart1 = numString.substring(0, numString.length() - 9);
            }
            catch(Exception e)
            {
                if (numString.length() == 1)
                {
                    numString = "0.00000000" + numString;
                }
                else if (numString.length() == 2)
                {
                    numString = "0.0000000" + numString;
                }
                else if (numString.length() == 3)
                {
                    numString = "0.000000" + numString;
                }
                else if (numString.length() == 4)
                {
                    numString = "0.00000" + numString;
                }
                else if (numString.length() == 5)
                {
                    numString = "0.0000" + numString;
                }
                else if (numString.length() == 6)
                {
                    numString = "0.000" + numString;
                }
                else if (numString.length() == 7)
                {
                    numString = "0.00" + numString;
                }
                else if (numString.length() == 8)
                {
                    numString = "0.0" + numString;
                }
                else if (numString.length() == 9)
                {
                    numString = "0." + numString;
                }
                return numString;
            }
            try
            {
                strPart2 = numString.substring(numString.length() - 9, numString.length());
            }
            catch(Exception e)
            {
                strPart2 = numString.substring(numString.length() - 2);
            }
            numString = strPart1 + "." + strPart2;

            numString = convert(Double.parseDouble(numString));
        }
        catch(Exception e)
        {
            numString = convert(Double.parseDouble(numString));
        }
        return numString;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                close();
                // MainActivity.super.finish();
                //View v =  (View)(((TableRow)(MainSwitch.Imagegrid.getChildAt(MainSwitch.getRowIndex()))).getChildAt(MainSwitch.getCellIndex()));
                //v.setBackgroundColor(Color.BLACK);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void close()
    {
        CEODBScreen.super.finish();
        /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();
                        CEODBScreen.super.finish();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();*/
    }
    private void updateTime(int hours, int mins, String date) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        output.setText(" Last Refreshed at : "+date+" "+aTime);
        output.setTextSize(10);
        output.setTextColor(Color.GREEN);
        output.setWidth(width/3);
    }

}
