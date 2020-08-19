package xtrem.wellworthhawkeye;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by XPC05 on 25-04-2015.
 */
public class MyReport extends Activity implements NetProcess,View.OnClickListener {
    Context context;
    ArrayList<String>reportName,reportDetails;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myreport);
        context = MyReport.this;

        connect();


    }

    public void displayData(){
        try {
            if (reportName != null) {
                LinearLayout data = (LinearLayout) findViewById(R.id.data);
                data.removeAllViews();

                for (int i=0;i<reportName.size();i++) {

                    LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v = layoutInflater.inflate(R.layout.myreportdetail, null);
                    v.setTag(i);
                    v.setOnClickListener(this);

                    TextView reportTitle = (TextView) v.findViewById(R.id.reportname);
                    reportTitle.setText(reportName.get(i));

                    data.addView(v);

                }
            }
        }catch (Exception e){
            e.printStackTrace();

        }

    }


    public void connect(){
        //new ReadTaskGet(context,this,main).execute(xModuleClass.strConnectString + "/LiveRMSSegmentDetail?clientCode="+ xModuleClass.Username.trim()+"&Tag="+1+"&selectedSegment="+selectedSegment.replaceAll(" ","%20"));
        new ReadTaskGet(context,this).execute(xModuleClass.strConnectString+"/ceodb/MyReportServlet?tag=0");
    }

    @Override
    public void process(String result, int http_response_code) {

        try {
            System.err.println("http_response_code:" + http_response_code);
            if (http_response_code == 200) {
                JSONArray jsonArray = new JSONArray(result);

                if(!jsonArray.equals("")){
                    reportName = new ArrayList<String>();
                    reportDetails = new ArrayList<String>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        System.err.println("object:" + object);
                        reportName.add(object.get("reportname").toString().trim());
                        reportDetails.add(object.get("reporttabledetails").toString().trim());

                    }
                }
                displayData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
        if(view instanceof LinearLayout){

            LinearLayout layout = (LinearLayout) view;

            int selecTionIndex = -1;

            selecTionIndex = Integer.parseInt(layout.getTag().toString());
            if(selecTionIndex != -1){

                //String[] rowData = segmentValue.get(selecTionIndex);
                String selectedReportname = null;
                try {
                    selectedReportname = reportDetails.get(selecTionIndex);
                    Toast.makeText(context,selectedReportname,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this , MyReportDetails.class);
                    intent.putExtra("selectedReportName",selectedReportname);
                    startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }


    }
}