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
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.method.SingleLineTransformationMethod;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

/**
 * Created by Admin on 2/11/14.
 */
public class ClientOpenPosition extends Activity {

    private TableLayout myReportScreen,table,mainTable;
    private DataInputStream in = null;
    private String strConnect;
    private boolean doCreateSession;
    private Vector rowData;

    private String selectedSegment;
    private String clientCode;

    int xwidth;
    int columnCount;
    int fontSize;
    String[] strColumnData;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        this.strConnect = xModuleClass.strConnectString;//intent.getStringExtra("strConnect");
        doCreateSession = true;
        this.selectedSegment = intent.getStringExtra("segment");
        this.clientCode = intent.getStringExtra("clientcode");
        Display display = getWindowManager().getDefaultDisplay();

        xwidth = display.getWidth();
        //int height = display.getHeight();
        rowData = new Vector();
        fontSize = 12;
        int iLogoBackColor = Color.rgb(16, 110, 136);

        ImageView imgTitle = new ImageView(this);
        int imageResource = getResources().getIdentifier("@drawable/logo", null, getPackageName());
        Bitmap bitmap3 = BitmapFactory.decodeResource(getResources(), imageResource);
        imgTitle.setImageBitmap(bitmap3);
        imgTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(ClientOpenPosition.this, XTREMScreen.class);
                ClientOpenPosition.this.startActivity(myIntent);
            }
        });

        TableRow trImageVentura = new TableRow(this);
        trImageVentura.setPadding(5, 2, 5, 2);
        trImageVentura.setGravity(Gravity.CENTER_HORIZONTAL);
        //trImageVentura.setBackgroundColor(iLogoBackColor);
        trImageVentura.addView(imgTitle);

        mainTable = new TableLayout(this);

        mainTable.addView(trImageVentura);
        // intWidthArray = new int[]{width/7,width/8,width/10,width/10,width/7,width/10,width/10,width/7};
/*
        ImageView img = new ImageView(this);
        int imageCeo = getResources().getIdentifier("@drawable/ceodbtitle", null, getPackageName());
        Bitmap bit12 = BitmapFactory.decodeResource(getResources(), imageCeo);
        img.setImageBitmap(bit12);
*/
        TextView title = new TextView(this);
        title.setText("Open Position of " + clientCode);
        title.setEnabled(false);
        title.setBackgroundResource(0);
        //title.setTextColor(iLogoBackColor);//(Color.rgb(0, 200, 200));
        title.setTextSize(20);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        title.setWidth(xwidth);

        TableRow imageRow = new TableRow(this);
        imageRow.setPadding(1, 0, 1, 0);
        imageRow.setBackgroundColor(Color.BLACK);
        imageRow.setGravity(Gravity.CENTER_HORIZONTAL);
        imageRow.addView(title);

        mainTable.addView(imageRow);
        table = new TableLayout(this);
        mainTable.addView(table);
        connect();
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
        footer.setWidth(xwidth);
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(ClientOpenPosition.this, XTREMScreen.class);
                ClientOpenPosition.this.startActivity(myIntent);
            }
        });
        relative.addView(footer,params);

        mainTable.addView(relative,new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        super.setContentView(mainTable);

    }
    public void connect()
    {
        try
        {
            //retrieve a reference to an instance of TelephonyManager
            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(xModuleClass.strConnectString + "/ceodb/CDMyReportServlet");
            //showDialog("HTTP Get: "+strConnect+"/ceodb_old/CDMyReportServlet");
            httpget.addHeader("User-Agent","Profile/MIDP-2.0 Confirguration/CLDC-1.0");
            httpget.addHeader("Accept","text/plain");
            httpget.addHeader("Segment", selectedSegment);
            httpget.addHeader("IMEI", getDeviceID(telephonyManager));
            httpget.addHeader("Tab-Number", "0");
            httpget.addHeader("ClientCode", clientCode);

            HttpResponse response = httpclient.execute(httpget);
            in = new DataInputStream(response.getEntity().getContent());

            columnCount = Integer.parseInt(in.readUTF());
            System.out.println(columnCount);
            //showDialog(""+columnCount);
            String cols = in.readUTF();
            System.out.println(cols);
            strColumnData = null;
            strColumnData = cols.split("#");
            rowData = new Vector();
            while(true){
                String strRow = in.readUTF();
                if(strRow.equalsIgnoreCase("NA") || strRow == null){
                    break;
                }
                else{
                    String[] strRowArray = strRow.split("#");
                    rowData.add(strRowArray);
                }
            }
            in.close();
            in = null;
            addTitle();
            addRow();

        }
        catch(EOFException e)
        {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("IOException : "+e.toString())
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
        catch (IOException ie)
        {
            System.out.println("IO Exception in connect()");

            System.out.println(ie.getMessage());
            System.out.println(ie.toString());
            ie.printStackTrace();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("IOException : "+ie.toString())
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
            builder.setMessage("Exception : "+e.getMessage())
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

    private  void addTitle(){

        try{
            TableLayout table1 = new TableLayout(this);
            table1.setStretchAllColumns(true);
            table1.setShrinkAllColumns(true);

            int[] colorBack = {Color.rgb(5, 42, 69),Color.rgb(10,88,124),Color.rgb(5, 42, 69)};
            GradientDrawable gradientBack = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT ,colorBack);
            gradientBack.setCornerRadius(5);



            TableRow rowDayLabels0 = new TableRow(this);
            //int blackGradientImgSrc2 = getResources().getIdentifier("@drawable/gradient_blue", null, getPackageName());
            //rowDayLabels0.setBackgroundResource(blackGradientImgSrc2);
            rowDayLabels0.setPadding(1, 1, 1, 1);

            TableRow rowDayLabels1 = new TableRow(this);
            //rowDayLabels1.setBackgroundResource(blackGradientImgSrc2);
            rowDayLabels1.setPadding(1, 1, 1, 1);
        /*TableRow.LayoutParams rowParams = new TableRow.LayoutParams(xwidth,20);
        rowDayLabels0.setLayoutParams(rowParams);
        rowDayLabels1.setLayoutParams(rowParams);
        */
            int iLogoBackColor = Color.rgb(16, 110, 136);

            rowDayLabels0.setBackgroundColor(iLogoBackColor);
            rowDayLabels1.setBackgroundColor(iLogoBackColor);
            //rowDayLabels0.setBackground(gradientBack);
            //rowDayLabels1.setBackground(gradientBack);
            rowDayLabels0.setGravity(Gravity.CENTER);
            rowDayLabels1.setGravity(Gravity.CENTER);
            if(strColumnData != null){
                for(int i=0;i<strColumnData.length;i++){

                    TextView r1c2 = new TextView(this);
                    r1c2.setText(" " +strColumnData[i].substring(0, strColumnData[i].indexOf("_"))+" ");
                    r1c2.setTypeface(Typeface.DEFAULT_BOLD);
                    r1c2.setTextColor(Color.WHITE);
                    r1c2.setTextSize(fontSize+2);

                    if(i==0){
                        r1c2.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
                        //r1c2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    }
                    else{
                        r1c2.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
                        //r1c2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    }
                    r1c2.setWidth(xwidth/(columnCount/2));
                    if((i+1)%(columnCount/2) == 0){
                        r1c2.setPadding(0, 0, 5, 0);
                    }
                    if(i<(columnCount/2)){
                        rowDayLabels0.addView(r1c2);
                    }
                    else{
                        rowDayLabels1.addView(r1c2);
                    }
                }
                table1.addView(rowDayLabels0);
                table1.addView(rowDayLabels1);
                table.addView(table1);
            }
        }
        catch (Exception e){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Exception : "+e.getMessage())
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

    private void addRow(){

        try{
            TableLayout table2 = new TableLayout(this);
            table2.setStretchAllColumns(true);
            table2.setShrinkAllColumns(true);

            for(int i=0;i<rowData.size();i++){
                String[] rowStrData = (String[])rowData.get(i);
                if(rowStrData!= null){
                    TableRow rowHighs0 = new TableRow(this);
                    rowHighs0.setPadding(1, 1, 1, 1);
                    //int blackGradientImgSrc2 = getResources().getIdentifier("@drawable/black_gradient", null, getPackageName());
                    //rowHighs0.setBackgroundResource(blackGradientImgSrc2);

                    TableRow rowHighs1 = new TableRow(this);
                    rowHighs1.setPadding(1, 1, 1, 1);

                    rowHighs0.setGravity(Gravity.CENTER);
                    rowHighs1.setGravity(Gravity.CENTER);
                    //rowHighs1.setBackgroundResource(blackGradientImgSrc2);
                    if((i%2)==0){
                        rowHighs0.setBackgroundColor(Color.DKGRAY);
                        rowHighs1.setBackgroundColor(Color.DKGRAY);
                    }
                    else{
                        rowHighs0.setBackgroundColor(Color.BLACK);
                        rowHighs1.setBackgroundColor(Color.BLACK);
                    }
                    for(int j=0;j<rowStrData.length;j++){
                        TextView day1High = new TextView(this);
                        day1High.setTypeface(Typeface.DEFAULT);

                        if(j==0){
                            day1High.setTextColor(Color.WHITE);
                            day1High.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
                            day1High.setText(" " + rowStrData[j]);
                        }
                        else{
                            day1High.setTextColor(Color.WHITE);
                            day1High.setGravity(Gravity.CENTER_VERTICAL |Gravity.RIGHT);
                            day1High.setText(xModuleClass.stringToDoubleString(rowStrData[j])+" ");
                        }

                        day1High.setTextSize(fontSize);
                        day1High.setWidth(xwidth/(columnCount/2));
                        if((j+1)%(columnCount/2) == 0){
                            day1High.setPadding(0, 0, 5, 0);
                        }
                        else{

                        }
                        if(j<(columnCount/2)){
                            rowHighs0.addView(day1High);
                        }
                        else{
                            rowHighs1.addView(day1High);
                        }
                    }
                    table2.addView(rowHighs0);
                    table2.addView(rowHighs1);
                }
                // day2 column
            }

            ScrollView scrollView = new ScrollView(this);
            scrollView.setHorizontalScrollBarEnabled(false);
            scrollView.setVerticalScrollBarEnabled(true);
            scrollView.addView(table2);
            table.addView(scrollView);
        }
        catch (Exception e){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Exception : "+e.getMessage())
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //mnuRefresh = menu.add(Menu.NONE,2,Menu.NONE,"Refresh");

        menu.add(Menu.NONE,1,Menu.NONE,"Refresh");
        menu.add(Menu.NONE,2,Menu.NONE,"Amount in Rs.");
        menu.add(Menu.NONE,3,Menu.NONE,"Amount in Rs.lacs");
        menu.add(Menu.NONE,4,Menu.NONE,"Amount in Rs.cr");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == 1)
        {
            refreshRate();

        }
        else if(item.getItemId() == 2){
            xModuleClass.rsin = "Rs.";
            table.removeAllViews();
            addTitle();
            addRow();
            String strFileData = readTextFile();
            String[] strArray = strFileData.split("#");
            if(strArray.length>=2){
                writeTextFile(strArray[0] + "#"+ strArray[1]+"#"+xModuleClass.rsin);
            }
        }
        else if(item.getItemId() == 3){
            xModuleClass.rsin = "Rs.lacs";
            table.removeAllViews();
            addTitle();
            addRow();

            String strFileData = readTextFile();
            String[] strArray = strFileData.split("#");
            if(strArray.length>=2){
                writeTextFile(strArray[0] + "#"+ strArray[1]+"#"+xModuleClass.rsin);
            }
        }
        else if(item.getItemId() == 4){
            xModuleClass.rsin = "Rs.cr";
            table.removeAllViews();
            addTitle();
            addRow();

            String strFileData = readTextFile();
            String[] strArray = strFileData.split("#");
            if(strArray.length>=2){
                writeTextFile(strArray[0] + "#"+ strArray[1]+"#"+xModuleClass.rsin);
            }
        }
        return false;
    }
    public void refreshRate(){
        //new GetTask(this).execute();
        table.removeAllViews();
        connect();
        //addTitle();
        //addRow();
    }
    public String readTextFile() {
        String result = null;
        BufferedReader br = null;

        try {
            String baseDir = Environment.getExternalStorageDirectory()
                    .getAbsolutePath();

            String path = baseDir + File.separator + "xHinfo.zip";
            File file = new File(path);

            // System.out.println("db is Exit : " +dbFile.exists());
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            // Read text from file
            StringBuilder text = new StringBuilder();

            try {
                br = new BufferedReader(new FileReader(file));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }
            } catch (IOException e) {
                // You'll need to add proper error handling here
            } finally {
                try {
                    if (null != br)
                        br.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }

            result = text.toString();
        } catch (Exception e) {
            // Dialog.alert("Config file not found... Please download config file...");
            System.out.println(e.getMessage());
            // System.exit(0);
        }
        return result;
    }

    public void writeTextFile(String text) {
        BufferedWriter bw = null;

        try {

            File sdcard = Environment.getExternalStorageDirectory();
            String baseDir = Environment.getExternalStorageDirectory()
                    .getAbsolutePath();

            String path = baseDir + File.separator + "xHinfo.zip";
            File file = new File(path);

            // System.out.println("db is Exit : " +dbFile.exists());
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            try {
                bw = new BufferedWriter(new FileWriter(file));

                bw.write(text);
                bw.flush();
            } catch (IOException e) {
                // You'll need to add proper error handling here
            } finally {
                try {
                    if (null != bw)
                        bw.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            // Dialog.alert("Config file not found... Please download config file...");
            System.out.println(e.getMessage());
            // System.exit(0);
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