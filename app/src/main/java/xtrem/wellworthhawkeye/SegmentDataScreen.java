/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xtrem.wellworthhawkeye;
/**
 * @author Administrator
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.method.SingleLineTransformationMethod;
import android.view.View.OnClickListener;
import android.view.*;
import android.widget.*;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class SegmentDataScreen extends Activity implements OnClickListener {
    private static int g_segallowed;

    private final static String[] segments = {"NSE-CM", "BSE-CM", "NSE-F&O", "NSE-SX", "MCX-COM", "MCX-SX", "NCDEX-COM", "ICEX-COM",
            "BSE-F&O", "ACE-COM", "NSEL", "NCME", "UCE"};
    private final static String[] segmentIDs = {"TN", "TB", "TD", "TU", "TM", "TX", "TE", "", "", "", "", "", ""};

    private final static String[] dataTypes = {"String", "double", "int"};

    private final static int[] colors = {Color.WHITE, Color.rgb(255, 165, 0), Color.YELLOW, Color.rgb(211, 211, 211), Color.RED, Color.RED, Color.GREEN};

    private Button btnHome, btnSegment, btnClose;

    private EditText lblSegment;

    @SuppressWarnings("UseOfObsoleteCollectionType")
    private Vector mainTabVector;

    private MainTitleEditText lblMainTabs[];

    //private GridFieldManager tableMarginUtil, tableTopBranches, tableTopBranchesGL, tableTopClients, tableTopClientsGL, tableTopScripts;
    private TableLayout dataTable[];

    private DataEditText lblDataLabel[][][];

    private TitleEditText lblTitleLabel[][];
    private int lblDataType[][];
    private int lblColors[][];

    private String labelData[][][];

    private int count = 5, tabCount = 0, maxColumnCount = 0, columnCounts[], tableCount[];
    private int recordCount = 0, fieldCount = 0;

    private static int iterate;

    private boolean isLoaded[];

    //IO Objects
    private DataInputStream in = null;

    private String segmentSelection;

    private TableLayout segmentDataForm;

    //Timer Object
    private Timer timer;

    //Bitmaps
    Bitmap blackButton, blackGradientBitmap, blackGradientBitmap2;
    Bitmap homeBitmap, homeBitmap2, segmentBitmap, segmentBitmap2, closeBitmap, closeBitmap2;
    Bitmap backgroundBitmap, segmentButtonBitmap;

    int blackGradientImgSrc, blackGradientImgSrc2, segmentButtonImgSrc;

    ScrollView scrollView;
    RelativeLayout main;
    MenuItem mnuHome, mnuSegment, mnuClose;

    static String selectedSegment = "TN";

    private String strConnect;

    private String closingParameter = "false";
    //int width;
    int[] intWidth3Array;
    int[] intWidth53Array;
    int[] intWidth4Array;

    //NEW ADDED
    private int width = 0, height = 0;
    private  TableRow trImageVentura;

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @SuppressWarnings({"UseOfObsoleteCollectionType", "CallToThreadDumpStack"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RelativeLayout mainLayout = new RelativeLayout(this);

        LinearLayout topLayout = new LinearLayout(this);
        topLayout.setId(1);
        topLayout.setOrientation(LinearLayout.VERTICAL);


        Intent intent = getIntent();
        this.segmentSelection = intent.getStringExtra("selectedSegment"); //if it's a string you stored.
        this.strConnect = intent.getStringExtra("strConnect"); //if it's a string you stored.

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        //display.getSize(size);
        //int width = size.x;
        width = display.getWidth();
        height = display.getHeight();
        //int height = size.y;
        intWidth3Array = new int[]{width / 3, (width / 3) + (width / 8), (width / 3) - ((width / 8) + 10)};
        intWidth4Array = new int[]{width / 4, (width / 4) + (width / 8), (width / 4) - (width / 16), (width / 4) - ((width / 16) + 10)};
        intWidth53Array = new int[]{(width / 3) + (width / 6), (width / 3) - (width / 12), (width / 3) - ((width / 12) + 10)};
        //intWidth6Array = new int[]{width/7,width/8,width/10,width/10,width/7,width/10,width/10,width/7};
        int iLogoBackColor = xModuleClass.logoBackColor;//Color.rgb(16, 110, 136);
       /* ImageView imgTitle = new ImageView(this);
        int imageResource = xModuleClass.bannerImg(this);*/

        TableLayout mainTable = new TableLayout(this);


        ImageView imgTitle = new ImageView(this);
        int imageResource = getResources().getIdentifier("@drawable/SELECT CLIENTOCODE ,ISINCODE,COMPCODE,GIVQTY,RECQTY,DTOFTRAN FROM Ldbo.DematMain  \n" +
                "WHERE DPCODE='00133359' \n" +
                " AND FIRMNUMBER IN ('SRS-000001')", null, getPackageName());
        Bitmap bitmap3 = BitmapFactory.decodeResource(getResources(), imageResource);
        imgTitle.setImageBitmap(bitmap3);
        imgTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(SegmentDataScreen.this, XTREMScreen.class);
                SegmentDataScreen.this.startActivity(myIntent);
            }
        });

        ImageView moreOpt = new ImageView(this);
        moreOpt.setPadding(0, 5, 25, 0);
        moreOpt.setImageResource(R.drawable.morebutton);
        moreOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOptionsMenu();
            }
        });

        trImageVentura = new TableRow(this);
        trImageVentura.setPadding(5, 2, 5, 2);
        trImageVentura.setGravity(Gravity.CENTER_HORIZONTAL);
        //trImageVentura.setBackgroundColor(iLogoBackColor);
        trImageVentura.addView(imgTitle);

        //topLayout.addView(trImageVentura);
        //logo.setGravity(Gravity.CENTER);

        //trImageVentura.addView(logo);

        /*TableRow trImageVentura = new TableRow(this);
        trImageVentura.setPadding(5, 2, 5, 2);
        //trImageVentura.setGravity(Gravity.CENTER_HORIZONTAL);
        trImageVentura.setBackgroundColor(iLogoBackColor);
        RelativeLayout logo = new RelativeLayout(this);
        RelativeLayout.LayoutParams param1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT );
        param1.addRule(RelativeLayout.CENTER_IN_PARENT);
        RelativeLayout.LayoutParams param2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT );
        param2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        //logo.setGravity(Gravity.CENTER);
        logo.addView(imgTitle,param1);
        logo.addView(moreOpt,param2);
        logo.setBackgroundColor(iLogoBackColor);
        trImageVentura.addView(logo);*/

        //mainTable.addView(trImageVentura);
        //topLayout.addView(trImageVentura);

        g_segallowed = 7;
        new GetData().execute();
    }

    private void setSegmentDatas() {
        try {

            lblSegment = new EditText(this);

            //connect("TN");
            lblSegment.setText(this.segmentSelection);
            lblSegment.setTextColor(Color.WHITE);
            lblSegment.setEnabled(false);
            lblSegment.setClickable(true);
            lblSegment.setGravity(0x11);
            int index = 0;
            int i = 0;
            while (i < segments.length) {
                if (segments[i].equals(this.segmentSelection)) {
                    index = i;
                }
                i++;
            }
            //segmentSelection = segmentIDs[index];

            //int size = lblSegment.getPreferredHeight();
            int blackButtonimgSrc = getResources().getIdentifier("@drawable/black_button", null, getPackageName());
            blackButton = BitmapFactory.decodeResource(getResources(), blackButtonimgSrc);
            //lblSegment.setBackground(new BitmapDrawable(getResources(), blackButton));
            lblSegment.setBackgroundResource(blackButtonimgSrc);
            lblSegment.setPadding((int) (width + height) / 120, (int) (width + height) / 168, (int) (width + height) / 120, (int) (width + height) / 56);

            blackGradientImgSrc = getResources().getIdentifier("@drawable/black_gradient", null, getPackageName());
            blackGradientBitmap = BitmapFactory.decodeResource(getResources(), blackGradientImgSrc);

            mainTabVector = new Vector();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setMainTabsData(){
        try { //mainTable.addView(lblSegment);
            segmentDataForm = new TableLayout(this);
            segmentDataForm.setId(2);
            segmentDataForm.addView(trImageVentura);
            segmentDataForm.addView(lblSegment);

            for (int i = 0; i < tabCount; i++) {
                segmentDataForm.addView(lblMainTabs[i]);
            }

        /*labelData = new String [tabCount] [count] [maxColumnCount];
        lblDataLabel = new DataEditText [tabCount] [count] [maxColumnCount];*/
            lblTitleLabel = new TitleEditText[tabCount][maxColumnCount];
            lblDataType = new int[tabCount][maxColumnCount];
            lblColors = new int[tabCount][maxColumnCount];

            //mgrFooter = new HorizontalFieldManager(USE_ALL_WIDTH);
            //mgrFooter.setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
            //mgrFooter.setPadding(0,5,0,5);

            //btnRefresh = new ButtonField();

            int homeBitmapImgSrc = getResources().getIdentifier("@drawable/home_normal", null, getPackageName());
            homeBitmap = BitmapFactory.decodeResource(getResources(), homeBitmapImgSrc);

            int homeBitmapImgSrc2 = getResources().getIdentifier("@drawable/home_normal2", null, getPackageName());
            homeBitmap2 = BitmapFactory.decodeResource(getResources(), homeBitmapImgSrc2);

            btnHome = new Button(this);
            btnHome.setWidth(width / 3);
            btnHome.setHeight((int) (width + height) / 26);
            btnHome.setBackgroundResource(homeBitmapImgSrc);

            int segmentBitmapImgSrc = getResources().getIdentifier("@drawable/segment_normal", null, getPackageName());
            segmentBitmap = BitmapFactory.decodeResource(getResources(), segmentBitmapImgSrc);

            int segmentBitmapImgSrc2 = getResources().getIdentifier("@drawable/segment_normal2", null, getPackageName());
            segmentBitmap2 = BitmapFactory.decodeResource(getResources(), segmentBitmapImgSrc2);

            btnSegment = new Button(this);
            btnSegment.setWidth(width / 3);
            btnSegment.setHeight((int) (width + height) / 26);
            btnSegment.setBackgroundResource(segmentButtonImgSrc);

            int closeBitmapImgSrc = getResources().getIdentifier("@drawable/close_normal", null, getPackageName());
            closeBitmap = BitmapFactory.decodeResource(getResources(), closeBitmapImgSrc);

            int closeBitmapImgSrc2 = getResources().getIdentifier("@drawable/close_normal2", null, getPackageName());
            closeBitmap2 = BitmapFactory.decodeResource(getResources(), closeBitmapImgSrc2);

            btnClose = new Button(this);
            btnClose.setWidth(width / 3);
            btnClose.setHeight((int) (width + height) / 26);
            btnClose.setBackgroundResource(segmentButtonImgSrc);

            //mgrFooter.add(btnHome);
            //mgrFooter.add(btnSegment);
            //mgrFooter.add(btnClose);

            timer = new Timer();

            this.timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    try {
                        for (int i = 0; i < tabCount; i++) {
                            isLoaded[i] = false;
                        }
                    } catch (Exception e) {
                        //Dialog.alert(e.toString());
                    }
                }
            }, 0000, 30000);

            //busyCursor = new BusyCursorScreen(6000);
            //UiApplication.getUiApplication().pushModalScreen(busyCursor);
            //connect(segmentIDs[index]);
            //showData();
            //display();

            //this.getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
            int backgroundBitmapImgSrc = getResources().getIdentifier("@drawable/gradient", null, getPackageName());
            backgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundBitmapImgSrc);

        /*try
        {
            this.setWallpaper(backgroundBitmap);
        }
        catch(IOException ie)
        {
            ie.printStackTrace();
        }*/
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            scrollView = new ScrollView(this);

            scrollView.setId(2);
            scrollView.setHorizontalScrollBarEnabled(true);
            scrollView.setVerticalScrollBarEnabled(true);
            scrollView.addView(segmentDataForm);

        /*RelativeLayout.LayoutParams param_tab = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param_tab.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
        topLayout.addView(trImageVentura,param_tab);*/


            //mainTable.addView(scrollView);
            RelativeLayout relative = new RelativeLayout(this);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

            Button footer = new Button(this);
            footer.setText("Powered By : Xtremsoft Technologies Mumbai");
            footer.setEnabled(true);
            footer.setId(3);
            footer.setTypeface(Typeface.DEFAULT_BOLD);
            footer.setBackgroundResource(0);
            footer.setTextColor(xModuleClass.logoBackColor);
            footer.setTextSize(12);
            footer.setGravity(Gravity.CENTER);
            footer.setTransformationMethod(SingleLineTransformationMethod.getInstance());
            footer.setWidth(width);
            footer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(SegmentDataScreen.this, XTREMScreen.class);
                    SegmentDataScreen.this.startActivity(myIntent);
                }
            });

            relative.addView(footer, params);

            segmentDataForm.addView(relative, new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        /*main = new RelativeLayout(this);
        //main.setBackgroundResource(imageBackCool);
        RelativeLayout.LayoutParams params_tab = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        params_tab.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        main.addView(topLayout, params_tab);

        RelativeLayout.LayoutParams params_tab0 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        params_tab0.addRule(RelativeLayout.BELOW, topLayout.getId());
        params_tab0.addRule(RelativeLayout.ABOVE, footer.getId());
        main.addView(scrollView, params_tab0);

        RelativeLayout.LayoutParams params_tab1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        params_tab1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params_tab1.addRule(RelativeLayout.CENTER_HORIZONTAL);
        main.addView(footer, params_tab1);*/

            super.setContentView(scrollView);
            //super.setContentView(main);

            //this.setStatus(mgrFooter);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private class GetData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            getSegmentAllowed();
            runOnUiThread(() -> setSegmentDatas());
            getMainTabs();
            runOnUiThread(() -> setMainTabsData());
            return null;
        }
    }


    @SuppressWarnings("CallToThreadDumpStack")
    public final void getSegmentAllowed() {
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(xModuleClass.strConnectString + "/ceodb/runSegAllowedServlet");
            httpget.addHeader("User-Agent", "Profile/MIDP-2.0 Confirguration/CLDC-1.0");
            httpget.addHeader("Accept", "text/plain");
            HttpResponse response = httpclient.execute(httpget);
            in = new DataInputStream(response.getEntity().getContent());

            g_segallowed = Integer.parseInt(in.readUTF());

            in.close();
            in = null;
        } catch (EOFException e) {
            //Dialog.alert("Data Loaded...!!!");
        } catch (IOException e) {
            //Dialog.alert("IOException in connect() > Message : " + e.getMessage() + " : " + e.toString());
            System.out.println("Caught IOException: " + e.toString());
            System.err.println("Caught IOException: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            //Dialog.alert("Exception in connect() > Message : " + e.getMessage() + " : " + e.toString());
            System.out.println("Caught Exception: " + e.toString());
            System.err.println("Caught Exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException ignore) {
                }
        }
    }

    @SuppressWarnings("CallToThreadDumpStack")
    public final void getMainTabs() {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(xModuleClass.strConnectString + "/ceodb/getMainTabs");
            httpget.addHeader("User-Agent", "Profile/MIDP-2.0 Confirguration/CLDC-1.0");
            httpget.addHeader("Accept", "text/plain");
            httpget.addHeader("IMEI", getDeviceID(telephonyManager));
            HttpResponse response = httpclient.execute(httpget);
            in = new DataInputStream(response.getEntity().getContent());

            tabCount = Integer.parseInt(in.readUTF());

            lblMainTabs = new MainTitleEditText[tabCount];
            dataTable = new TableLayout[tabCount];
            isLoaded = new boolean[tabCount];
            columnCounts = new int[tabCount];
            tableCount = new int[tabCount];
            maxColumnCount = 0;

            for (iterate = 0; iterate < tabCount; iterate++) {
                lblMainTabs[iterate] = new MainTitleEditText(this, in.readUTF());
                lblMainTabs[iterate].setOnClickListener(this);
                mainTabVector.addElement(lblMainTabs[iterate]);
                tableCount[iterate] = Integer.parseInt(in.readUTF());
                int rowCount = Integer.parseInt(in.readUTF());
                columnCounts[iterate] = Integer.parseInt(in.readUTF());
                dataTable[iterate] = new TableLayout(this);
                //dataTable [iterate].setRowCount(rowCount + 1);
                //dataTable [iterate].setColumnCount(columnCounts [iterate]);
                //rowCount + 1, columnCounts [iterate], USE_ALL_WIDTH);
                if (columnCounts[iterate] > maxColumnCount)
                    maxColumnCount = columnCounts[iterate];
                isLoaded[iterate] = false;
            }

            in.close();
            in = null;
        } catch (EOFException e) {
            //Dialog.alert("Data Loaded...!!!");
        } catch (IOException e) {
            //Dialog.alert("IOException in connect() > Message : " + e.getMessage() + " : " + e.toString());
            System.out.println("Caught IOException: " + e.toString());
            System.err.println("Caught IOException: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            //Dialog.alert("Exception in connect() > Message : " + e.getMessage() + " : " + e.toString());
            System.out.println("Caught Exception: " + e.toString());
            System.err.println("Caught Exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException ignore) {
                }
        }
    }

    @SuppressWarnings("CallToThreadDumpStack")
    public void connect(String selectedSegment, int tabNumber) {
        try {
            //retrieve a reference to an instance of TelephonyManager
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(xModuleClass.strConnectString + "/ceodb/runSegmentServlet");
            //HttpGet httpget = new HttpGet("http://219.64.39.25:5054/ceodb/runSegmentServlet");
            //HttpGet httpget = new HttpGet("http://61.12.61.163:5054/ceodb/runSegmentServlet");
            httpget.addHeader("User-Agent", "Profile/MIDP-2.0 Confirguration/CLDC-1.0");
            httpget.addHeader("Accept", "text/plain");
            httpget.addHeader("IMEI", getDeviceID(telephonyManager));
            httpget.addHeader("Ph-No", telephonyManager.getLine1Number());
            httpget.addHeader("Segment", selectedSegment);
            httpget.addHeader("Tab-Number", "" + tabNumber);
            HttpResponse response = httpclient.execute(httpget);
            in = new DataInputStream(response.getEntity().getContent());

            fieldCount = Integer.parseInt(in.readUTF());
            recordCount = Integer.parseInt(in.readUTF());
            int tblCount = Integer.parseInt(in.readUTF());

            labelData = new String[tabCount][recordCount][maxColumnCount];
            lblDataLabel = new DataEditText[tabCount][recordCount][maxColumnCount];

            //dataTable [tabNumber].removeAllViews();

            runOnUiThread(() -> {
                try {
                    dataTable[tabNumber].removeAllViewsInLayout();
                }catch (Exception e){
                    e.printStackTrace();
                }
            });
            TableRow tr = new TableRow(this);
            for (int i = 0; i < fieldCount; i++) {
                String title = in.readUTF();
                lblColors[tabNumber][i] = Integer.parseInt(title.substring(title.lastIndexOf('_') + 1));
                title = title.substring(0, title.lastIndexOf('_'));
                lblDataType[tabNumber][i] = Integer.parseInt(title.substring(title.indexOf("_") + 1));
                title = title.substring(0, title.indexOf("_"));
                if (Character.isDigit(title.charAt(title.length() - 1)))
                    title = title.substring(0, title.length() - 1);
                lblTitleLabel[tabNumber][i] = new TitleEditText(this, title);
                if (lblDataType[tabNumber][i] != 0) {
                    //lblDataLabel [tabNumber] [i] [j].setGravity(0x10 | 0x05 | 0x80 | 0x08);
                    lblTitleLabel[tabNumber][i].setGravity(Gravity.RIGHT);
                } else {
                    lblTitleLabel[tabNumber][i].setGravity(Gravity.LEFT);
                }
                final int j = i;
                runOnUiThread(() -> {
                    try {
                        if (tblCount == 1) {
                            tr.addView(lblTitleLabel[tabNumber][j]);
                        } else if (tblCount == 2) {
                            if (j < fieldCount / 2) {
                                tr.addView(lblTitleLabel[tabNumber][j]);
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
            }
            runOnUiThread(() -> {
                try {
                    dataTable[tabNumber].addView(tr);
                }catch (Exception e){
                    e.printStackTrace();
                }
            });

            String str;
            for (int i = 0; i < recordCount; i++) {
                for (int j = 0; j < fieldCount; j++) {
                    str = in.readUTF();
                    if (str.equals("NA")) {
                        break;
                    }
                    labelData[tabNumber][i][j] = str;
                    //System.out.println("=========" + labelData [0] [i] [j]);
                }
            }
            in.close();
            in = null;
            isLoaded[tabNumber] = true;
        }
        /*catch (EOFException e)
        {-
            //Dialog.alert("Data Loaded...!!!");
        }
        catch (IOException e)
        {
            //Dialog.alert("IOException in connect() > Message : " + e.getMessage() + " : " + e.toString());
            System.out.println("Caught IOException: " + e.toString());
            System.err.println("Caught IOException: " + e.getMessage());
            e.printStackTrace();
        }*/ catch (Exception e) {
            //Dialog.alert("Exception in connect() > Message : " + e.getMessage() + " : " + e.toString());
            System.out.println("Caught Exception: " + e.toString());
            System.err.println("Caught Exception: " + e.getMessage());
            e.printStackTrace();
        }
      /*  finally
        {
            if (in != null)
                try { in.close(); } catch (IOException ignore) {}
        }*/
    }

    @SuppressWarnings("CallToThreadDumpStack")
    public void display(int tabNumber) {
        int bkgndColor;
        //dataTable [tabNumber] = null;
        //dataTable [tabNumber] = new GridFieldManager(recordCount + 1, fieldCount, USE_ALL_WIDTH);
        /*try
        {
            dataTable [tabNumber].deleteRange(fieldCount, fieldCount * recordCount);
        }
        catch(Exception e) { }*/
        try {
            if (tableCount[tabNumber] == 1) {
                //Formatter formatter = new Formatter();
                for (int i = 0; i < recordCount; i++) {
                    TableRow tr = new TableRow(this);
                    boolean showRow = false;
                    for (int j = 0; j < fieldCount; j++) {
                        lblDataLabel[tabNumber][i][j] = new DataEditText(this, labelData[tabNumber][i][j]);
                        if (((lblTitleLabel[tabNumber][0]).getText().toString().contains("Code")) && (((lblTitleLabel[tabNumber][0]).getText().toString().startsWith("Cl")))) {
                            lblDataLabel[tabNumber][i][j].setOnClickListener(this);
                        } else {
                            lblDataLabel[tabNumber][i][j].setClickable(false);
                        }
                        if (lblDataType[tabNumber][j] != 0) {
                            //lblDataLabel [tabNumber] [i] [j].setGravity(0x10 | 0x05 | 0x80 | 0x08);
                            lblDataLabel[tabNumber][i][j].setGravity(Gravity.RIGHT);
                        } else {
                            lblDataLabel[tabNumber][i][j].setGravity(Gravity.LEFT);
                        }

                        if (lblColors[tabNumber][j] != 7 && lblColors[tabNumber][j] != 1) {
                            lblDataLabel[tabNumber][i][j].setTextColor(colors[lblColors[tabNumber][j]]);
                        } else {
                            double data = Double.parseDouble(labelData[tabNumber][i][j]);
                            if (data > 0)
                                lblDataLabel[tabNumber][i][j].setTextColor(colors[6]);
                            else if (data < 0)
                                lblDataLabel[tabNumber][i][j].setTextColor(colors[5]);
                            else
                                lblDataLabel[tabNumber][i][j].setTextColor(colors[0]);
                        }
                        if (tabNumber == 5) {
                            lblDataLabel[tabNumber][i][j].setWidth(intWidth53Array[j]);
                        } else {
                            if (fieldCount <= 3) {
                                lblDataLabel[tabNumber][i][j].setWidth(intWidth3Array[j]);
                            } else if (fieldCount <= 4) {
                                lblDataLabel[tabNumber][i][j].setWidth(intWidth4Array[j]);
                            }
                        }
                        if (!labelData[tabNumber][i][j].trim().contains("null")) {
                            showRow = true;
                        }
                        if (showRow) {
                            tr.addView(lblDataLabel[tabNumber][i][j]);
                            //tr.setTag(labelData[tabNumber][i][1]);
                            //tr.setOnClickListener(this);
                        }
                        //tr.addView(lblDataLabel [tabNumber] [i] [j]);
                        //dataTable [tabNumber].addView(lblDataLabel [tabNumber] [i] [j]);
                        //lblDataLabel [i] = new DataEditText(formatter.formatNumber(MUMarginUtil [i], 2), EditText.RIGHT);
                    }
                    dataTable[tabNumber].addView(tr);
                }
            } else if (tableCount[tabNumber] == 2) {
                int countI = 0;
                for (int i = 0; i < recordCount; i++) {
                    boolean showRow = false;
                    TableRow tr = new TableRow(this);
                    for (int j = 0; j < fieldCount / 2; j++) {
                        lblDataLabel[tabNumber][i][j] = new DataEditText(this, labelData[tabNumber][i][j]);
                        /*if(lblDataType [tabNumber] [j] != 0)
                        {    //lblDataLabel [tabNumber] [i] [j].setGravity(0x10 | 0x05 | 0x80 | 0x08);
                            lblDataLabel[tabNumber][i][j].setGravity(Gravity.LEFT);
                        }*/
                        if (lblDataType[tabNumber][j] != 0) {
                            //lblDataLabel [tabNumber] [i] [j].setGravity(0x10 | 0x05 | 0x80 | 0x08);
                            lblDataLabel[tabNumber][i][j].setGravity(Gravity.RIGHT);
                        } else {
                            lblDataLabel[tabNumber][i][j].setGravity(Gravity.LEFT);
                        }


                        if (lblColors[tabNumber][j] != 7 && lblColors[tabNumber][j] != 1) {
                            lblDataLabel[tabNumber][i][j].setTextColor(colors[lblColors[tabNumber][j]]);
                        } else {
                            double data = Double.parseDouble(labelData[tabNumber][i][j]);
                            if (data > 0)
                                lblDataLabel[tabNumber][i][j].setTextColor(colors[6]);
                            else if (data < 0)
                                lblDataLabel[tabNumber][i][j].setTextColor(colors[5]);
                            else
                                lblDataLabel[tabNumber][i][j].setTextColor(colors[0]);
                        }
                        if ((fieldCount / 2) <= 3) {
                            lblDataLabel[tabNumber][i][j].setWidth(intWidth3Array[j]);
                        } else if ((fieldCount / 2) <= 4) {
                            lblDataLabel[tabNumber][i][j].setWidth(intWidth4Array[j]);
                        }

                        if (!labelData[tabNumber][i][j].trim().contains("null")) {
                            showRow = true;
                        }
                        if (showRow) {
                            tr.addView(lblDataLabel[tabNumber][i][j]);
                        }
                        //tr.addView(lblDataLabel [tabNumber] [i] [j]);
                    }
                    dataTable[tabNumber].addView(tr);
                }
                TableRow tr = new TableRow(this);

                for (int j = fieldCount / 2; j < fieldCount; j++) {

                    tr.addView(lblTitleLabel[tabNumber][j]);

                }
                dataTable[tabNumber].addView(tr);
                for (int i = 0; i < recordCount; i++) {
                    boolean showRow = false;
                    tr = new TableRow(this);
                    for (int j = fieldCount / 2; j < fieldCount; j++) {
                        lblDataLabel[tabNumber][i][j] = new DataEditText(this, labelData[tabNumber][i][j]);


                        //if(lblDataType [tabNumber] [j] != 0)
                        //  lblDataLabel [tabNumber] [i] [j].setGravity(0x10 | 0x05 | 0x80 | 0x08);
                        if (lblDataType[tabNumber][j] != 0) {
                            //lblDataLabel [tabNumber] [i] [j].setGravity(0x10 | 0x05 | 0x80 | 0x08);
                            lblDataLabel[tabNumber][i][j].setGravity(Gravity.RIGHT);
                        } else {
                            lblDataLabel[tabNumber][i][j].setGravity(Gravity.LEFT);
                        }
                        if (lblColors[tabNumber][j] != 7 && lblColors[tabNumber][j] != 1) {
                            lblDataLabel[tabNumber][i][j].setTextColor(colors[lblColors[tabNumber][j]]);
                        } else {
                            double data = Double.parseDouble(labelData[tabNumber][i][j]);
                            if (data > 0)
                                lblDataLabel[tabNumber][i][j].setTextColor(colors[6]);
                            else if (data < 0)
                                lblDataLabel[tabNumber][i][j].setTextColor(colors[5]);
                            else
                                lblDataLabel[tabNumber][i][j].setTextColor(colors[0]);
                        }
                        if ((fieldCount / 2) <= 3) {
                            lblDataLabel[tabNumber][i][j].setWidth(intWidth3Array[j - (fieldCount / 2)]);
                        } else if ((fieldCount / 2) <= 4) {
                            lblDataLabel[tabNumber][i][j].setWidth(intWidth4Array[j - (fieldCount / 2)]);
                        }
                        if (!labelData[tabNumber][i][j].trim().contains("null")) {
                            showRow = true;
                        }
                        if (showRow) {
                            tr.addView(lblDataLabel[tabNumber][i][j]);
                        }
                        //tr.addView(lblDataLabel [tabNumber] [i] [j]);
                    }
                    dataTable[tabNumber].addView(tr);
                }
            }
        } catch (Exception e) {
            //Dialog.alert("Exception in display() > Message : " + e.getMessage() + " : " + e.toString());
            e.printStackTrace();
            System.out.print(e.getMessage());
            System.out.println(" : " + e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //menu.add("Refresh");

        mnuHome = menu.add(Menu.NONE, 1, Menu.NONE, "Home");
        //set the shortcut
        mnuHome.setShortcut('1', 'H');

        mnuSegment = menu.add(Menu.NONE, 2, Menu.NONE, "Segment");
        mnuSegment.setShortcut('1', 'S');

        mnuClose = menu.add(Menu.NONE, 3, Menu.NONE, "Close");
        mnuClose.setShortcut('1', 'C');

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //check selected menu item
        if (item.getItemId() == 1) {
            /*Intent data = new Intent();
            data.putExtra("isClose", "false");
            if (SegmentDataScreen.this.getParent() == null)
            {
                this.setResult(100, data);
                //SegmentDataScreen.this.setResult(Activity.RESULT_OK, data);
            }
            else
            {
                this.getParent().setResult(100, data);
                //SegmentDataScreen.this.getParent().setResult(Activity.RESULT_OK, data);
            }*/
            closingParameter = "false";

            this.finish();
        }
        if (item.getItemId() == 2) {
            segmentSelection = showSegmentDialog(segmentSelection);

            for (int i = 0; i < tabCount; i++) {
                if (dataTable[i].isShown() == true) {
                    segmentDataForm.removeView(dataTable[i]);
                }
            }

            for (int i = 0; i < tabCount; i++) {
                isLoaded[i] = false;
            }

            //lblSegment.setText(segmentSelection);
        }
        if (item.getItemId() == 3) {
            this.close();
        }
        return false;
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("isClose", closingParameter);
        if (SegmentDataScreen.this.getParent() == null) {
            this.setResult(100, data);
            //SegmentDataScreen.this.setResult(Activity.RESULT_OK, data);
        } else {
            this.getParent().setResult(100, data);
            //SegmentDataScreen.this.getParent().setResult(Activity.RESULT_OK, data);
        }
        super.finish();
    }

    String getDeviceID(TelephonyManager phonyManager) {
        String id = phonyManager.getDeviceId();
        if (id == null) {
            id = "not available";
        }
        int phoneType = phonyManager.getPhoneType();
        switch (phoneType) {
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

    void toggleTabs(MainTitleEditText label) {
        int tabNumber = mainTabVector.indexOf(label);
        if (dataTable[tabNumber].isShown() == true) {
            segmentDataForm.removeView(dataTable[tabNumber]);
        } else {
            for (int i = 0; i < tabCount; i++) {
                if (i != tabNumber) {
                    if (dataTable[i].isShown() == true) {
                        segmentDataForm.removeView(dataTable[i]);
                    }
                }
            }
            if (isLoaded[tabNumber] == false) {
                int index = 0;
                int i = 0;
                while (i < segments.length) {
                    if (segments[i].equals(this.lblSegment.getText().toString())) {
                        index = i;
                    }
                    i++;
                }
                segmentSelection = segmentIDs[index];
                new ConnectTask(tabNumber).execute();
            }
            //segmentDataForm.addView(dataTable [tabNumber]);
            segmentDataForm.addView(dataTable[tabNumber], tabNumber + 3);
        }

        super.setContentView(scrollView);
        //super.setContentView(main);
        //this.doLayout();
        //this.doPaint();
    }
    /*public Bitmap ImgResizer(Bitmap bitmap , int width , int height)
    {
        Bitmap temp = new Bitmap(width,height);
        Bitmap resized_Bitmap = bitmap;
        temp.createAlpha(Bitmap.HOURGLASS);
        resized_Bitmap.scaleInto(temp, Bitmap.FILTER_LANCZOS, Bitmap.SCALE_STRETCH);
        return temp;
    }*/


    private class ConnectTask extends AsyncTask<Void,Void,Void>{
        private int tabNumber;

        public ConnectTask(int tabNumber){
            this.tabNumber = tabNumber;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            connect(segmentSelection, tabNumber);
            runOnUiThread(() -> {
                display(tabNumber);
                isLoaded[tabNumber] = true;
                setContentView(scrollView);
            });
            return null;
        }
    }



    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public void onClick(View v) {
        if (v instanceof MainTitleEditText) {
            toggleTabs((MainTitleEditText) v);
        } else if (v instanceof DataEditText) {
            DataEditText clientText = (DataEditText) v;
            new GetTask(this, selectedSegment, clientText.getText().toString().trim()).execute();
        }
    }

    public void close() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you really want to exit???")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                        closingParameter = "true";
                        
                        /*Intent data = new Intent();
                        data.putExtra("isClose", "true");
                        if (SegmentDataScreen.this.getParent() == null)
                        {
                            SegmentDataScreen.this.setResult(100, data);
                            //SegmentDataScreen.this.setResult(Activity.RESULT_OK, data);
                        }
                        else
                        {
                            SegmentDataScreen.this.getParent().setResult(100, data);
                            //SegmentDataScreen.this.getParent().setResult(Activity.RESULT_OK, data);
                        }*/
                        SegmentDataScreen.this.finish();
                        
                        /*Intent intent = SegmentDataScreen.this.getIntent();
                        intent.putExtra("isClose", "true");
                        SegmentDataScreen.this.setResult(100, intent);
                        SegmentDataScreen.this.finish();
                        
                        System.exit(0);*/
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    String showSegmentDialog(String visibleSegment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setMessage("Select a segment from the following...");
        //builder.setCancelable(true);

        int segmentCount = 0;

        String binarySegment = Integer.toBinaryString(g_segallowed);
        binarySegment = invert(binarySegment);
        for (int i = 0; i < binarySegment.length(); i++) {
            if (binarySegment.charAt(i) == '1' && visibleSegment.equals(segments[i]) == false) {
                segmentCount++;
            }
        }

        final String segmentsShown[] = new String[segmentCount];

        int j = 0;

        for (int i = 0; i < binarySegment.length(); i++) {
            if (binarySegment.charAt(i) == '1' && visibleSegment.equals(segments[i]) == false) {
                segmentsShown[j] = segments[i];
                j++;
            }
        }

        //builder.setItems(segmentsShown, null);
        //builder.setPositiveButton("OK", null);

        final CharSequence[] items = {"Red", "Green", "Blue"};

        builder.setTitle("Select a segment from the following...");
        builder.setItems(segmentsShown, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(getApplicationContext(), segmentsShown[item], Toast.LENGTH_SHORT).show();
                selectedSegment = segmentsShown[item];
                lblSegment.setText(segmentsShown[item]);
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

        return selectedSegment;
    }

    public String invert(String s) {
        String temp = "";
        for (int i = s.length() - 1; i >= 0; i--)
            temp += s.charAt(i);
        return temp;
    }

    class MainTitleEditText extends Button {
        MainTitleEditText(Context context, String text) {
            super(context);
            this.setText(text + " ");
            this.setPadding(45, 5, 5, 5);
            this.setTextColor(Color.rgb(255, 255, 255));

//            this.setTextColor(Color.rgb(160, 160, 160));
            this.setGravity(0x03 | 0x10);

            //this.setEnabled(false);
            //this.setClickable(true);

            blackGradientImgSrc2 = getResources().getIdentifier("@drawable/gradient_blue1", null, getPackageName());

            this.setBackgroundResource(blackGradientImgSrc2);
        }
    }

    class SegmentEditText extends EditText {
        SegmentEditText(Context context, String text) {
            super(context);
            this.setText(text + " ");
            this.setPadding(2, 2, 2, 2);
            this.setTextColor(Color.rgb(160, 160, 160));
            this.setEnabled(false);
            this.setClickable(true);

            segmentButtonImgSrc = getResources().getIdentifier("@drawable/segment_button", null, getPackageName());

            this.setBackgroundResource(segmentButtonImgSrc);
        }
    }

    class TitleEditText extends EditText {
        TitleEditText(Context context, String text) {
            super(context);
            this.setText(text + " ");
            this.setPadding(2, 1, 2, 1);
            this.setEnabled(false);
            this.setClickable(true);
            this.setBackgroundResource(0);
            this.setTextSize(14);
            this.setTextColor(Color.WHITE);
        }
    }

    class DataEditText extends EditText {
        DataEditText(Context context, String text) {
            super(context);
            this.setText(text + "  ");
            this.setPadding(2, 1, 2, 1);
            this.setEnabled(false);
            this.setClickable(true);
            this.setBackgroundResource(0);
            this.setTextSize(14);
        }
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    class GetTask extends AsyncTask<Object, Void, String> {
        Context context;

        ProgressDialog mDialog;
        boolean isRCConnet = false;
        String segment = "";
        String clientName = "";

        // GetTask(Context context,String userid)
        GetTask(Context context, String selectedImage, String clientName) {
            this.context = context;
            this.segment = selectedImage;
            this.clientName = clientName;
        }

        @TargetApi(Build.VERSION_CODES.CUPCAKE)
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mDialog = new ProgressDialog(context);

            mDialog.setMessage("Please wait...");
            mDialog.show();
        }

        @TargetApi(Build.VERSION_CODES.CUPCAKE)
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mDialog.dismiss();
        }

        @Override
        protected String doInBackground(Object... params) {
            Intent myIntent = new Intent(SegmentDataScreen.this, ClientOpenPosition.class);
            myIntent.putExtra("segment", this.segment);
            myIntent.putExtra("clientname", this.clientName);
            SegmentDataScreen.this.startActivity(myIntent);

            return null;
        }
    }

}
