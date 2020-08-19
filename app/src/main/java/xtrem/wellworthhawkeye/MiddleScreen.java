package xtrem.wellworthhawkeye;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MiddleScreen extends Activity implements View.OnClickListener {
	private LinearLayout mainLayout;
	private LinearLayout middleScreen;
	private ImageView imgMainReport;
	private ImageView imgMyReport;
	private TextView txtFooter;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.middle_screen);
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();

		LinearLayout mainReportLayout = (LinearLayout) findViewById(R.id.general_report_layout);
		mainReportLayout.setOnClickListener(this);
		LinearLayout myReportLayout = (LinearLayout) findViewById(R.id.my_report_layout);
		myReportLayout.setOnClickListener(this);

		txtFooter = (TextView) findViewById(R.id.footer_txt);
		txtFooter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(MiddleScreen.this,
						XTREMScreen.class);
				MiddleScreen.this.startActivity(myIntent);
			}
		});
		int reports=Integer.parseInt(LoginScreen.reports);
		if(reports==1){
			mainReportLayout.setVisibility(View.VISIBLE);
			myReportLayout.setVisibility(View.GONE);
		}else if(reports==2){
			mainReportLayout.setVisibility(View.GONE);
			myReportLayout.setVisibility(View.VISIBLE);
		}else{
			mainReportLayout.setVisibility(View.VISIBLE);
			myReportLayout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.general_report_layout:
				new GetTask(this, "mainReport").execute();
				break;
			case R.id.my_report_layout:
				new GetTask(this, "myReport").execute();
				break;
		}


		
	}


    @Override
    public void finish()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you really want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.dismiss();
                        MiddleScreen.super.finish();
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
@TargetApi(Build.VERSION_CODES.CUPCAKE)
class GetTask extends AsyncTask<Object, Void, String> {
	Context context;

	ProgressDialog mDialog;
	boolean isRCConnet = false;
	String strMsg = "";
    String img="";
	// GetTask(Context context,String userid)
	GetTask(Context context, String selectedImage) {
		this.context = context;
		this.img = selectedImage;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		mDialog = new ProgressDialog(context);

		mDialog.setMessage("Please wait...");
		mDialog.show();
	}
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		mDialog.dismiss();
	}
	@Override
	protected String doInBackground(Object... params) {
		if(img.equalsIgnoreCase("mainReport")) {
		Intent myIntent = new Intent(MiddleScreen.this,
				CEODBScreen.class);
		myIntent.putExtra("strConnect", xModuleClass.strConnectString);
		MiddleScreen.this.startActivity(myIntent);
		}
		else if(img.equalsIgnoreCase("myReport")) {
		Intent myIntent = new Intent(MiddleScreen.this,
				MyReportScreen.class);
		myIntent.putExtra("strConnect", xModuleClass.strConnectString);
		MiddleScreen.this.startActivity(myIntent);
		}
		return null;
	}
}
}