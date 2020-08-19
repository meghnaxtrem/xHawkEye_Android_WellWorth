package xtrem.wellworthhawkeye;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by XPC05 on 27-04-2015.
 */
public class MyReportDetails extends Activity implements NetProcess {
    Context context;
    String name;
    ArrayList<JSONObject> jsonObject;
    ArrayList<String> column1,column2,column3;
    int columnCount = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myreportdetails);

        Intent intent = getIntent();
        name = intent.getStringExtra("selectedReportName");

        context = MyReportDetails.this;
        connect();

    }
    public void connect(){
        //new ReadTaskGet(context,this,main).execute(xModuleClass.strConnectString + "/LiveRMSSegmentDetail?clientCode="+ xModuleClass.Username.trim()+"&Tag="+1+"&selectedSegment="+selectedSegment.replaceAll(" ","%20"));
        new ReadTaskGet(context,this).execute(xModuleClass.strConnectString+"/ceodb/MyReportServlet?tag=1&reporttablename="+name);
    }

    public void displayData(){
        try {
            LinearLayout title = (LinearLayout) findViewById(R.id.title);
            title.removeAllViews();
            LinearLayout data = (LinearLayout) findViewById(R.id.data);
            data.removeAllViews();

            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,1);

            columnCount = jsonObject.get(0).getInt("columncount");

            for(int i=0;i<columnCount;i++){
                String lblHeader = jsonObject.get(1).getString("lbl"+(i+1)).toString().trim();
                Reportlbl lbl = new Reportlbl(context,lblHeader, Gravity.CENTER,Color.WHITE,14,"");
                lbl.setLayoutParams(param);
                title.addView(lbl);
            }

            System.err.println("Child count in title:"+title.getChildCount());

            for(int i=2;i<jsonObject.size();i++){
                LinearLayout txtlayout = new LinearLayout(this);
                txtlayout.setOrientation(LinearLayout.HORIZONTAL);
                txtlayout.setLayoutParams(param);

                for(int j=0;j<columnCount;j++){
                    String txt = jsonObject.get(i).getString("reportcolumn"+(j+1)).toString().trim();
                    Reportlbl lbl = new Reportlbl(context,txt, Gravity.CENTER,Color.WHITE,14,"");
                    lbl.setSingleLine(false);
                    lbl.setLayoutParams(param);
                    txtlayout.addView(lbl);
                }
                data.addView(txtlayout);
            }
        }catch (Exception e){
            e.printStackTrace();

        }
    }



    @Override
    public void process(String result, int http_response_code) {

        try {
            System.err.println("http_response_code:" + http_response_code);
            if (http_response_code == 200) {
                JSONArray jsonArray = new JSONArray(result);

                if(!jsonArray.equals("")){
                    jsonObject = new ArrayList<JSONObject>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        //columnCount = object.getInt("columncount");
                        System.err.println("object:" + object);
                        jsonObject.add(object);


                    }
                    System.err.println("JSON Object:"+jsonObject);
                }
                displayData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class Reportlbl extends TextView {
        //private int textSize = 14;

        public Reportlbl(Context context, String text, int gravity, int textColor,int textSize,String style) {
            super(context);

            this.setTextSize(textSize);

            this.setText(text);
            this.setTextColor(textColor);
            this.setGravity(gravity);
            this.setLongClickable(true);
            this.setSingleLine(true);

        }


    }
}