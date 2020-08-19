/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xtrem.wellworthhawkeye;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.*;

import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


/**
 * 
 * @author Administrator
 */
public class LoginScreen extends Activity implements View.OnClickListener {
	// TableLayout
	private TableLayout loginScreen;

	// IO Objects
	private DataInputStream in = null;

	private EditText lblLoginTitle, lblUserName, lblPassword;
	private EditText txtUserName, txtPassword;

	private Button btnLogin, btnExit;
	private CheckBox chkRememberDetails;

	private String isValid;
	public String strConnect;
	public static String reports="1";

	/** Called when the activity is first created. */
	@TargetApi(Build.VERSION_CODES.CUPCAKE)
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();

		//int iLogoBackColor = Color.rgb(16, 110, 136);
		int iLogoBackColor = xModuleClass.logoBackColor;//Color.rgb(16, 110, 136);
		ImageView imgTitle = new ImageView(this);
		int imageResource = xModuleClass.bannerImg(this);//getResources().getIdentifier("@drawable/jm_logo_300_70",null, getPackageName());
		Bitmap bitmap3 = BitmapFactory.decodeResource(getResources(),
				imageResource);

		imgTitle.setImageBitmap(bitmap3);
		imgTitle.setBackgroundColor(getResources().getColor(R.color.text_green));
		imgTitle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent myIntent = new Intent(LoginScreen.this,
						XTREMScreen.class);
				LoginScreen.this.startActivity(myIntent);
			}
		});

		TableRow trImageVentura = new TableRow(this);
		trImageVentura.setPadding(5, 2, 5, 2);
		trImageVentura.setGravity(Gravity.CENTER_HORIZONTAL);
		trImageVentura.setBackgroundColor(iLogoBackColor);
		trImageVentura.addView(imgTitle);

		loginScreen = new TableLayout(this);
		TableLayout userPassLayout = new TableLayout(this);
		loginScreen.addView(trImageVentura);
		TableRow row1 = new TableRow(this);
		row1.setGravity(Gravity.CENTER);
		row1.setPadding(1, 10, 1, 4);

		int[] colorBack = { Color.rgb(250, 250, 250), Color.rgb(240, 240, 240) };
		GradientDrawable gradientBack = new GradientDrawable(
				GradientDrawable.Orientation.TOP_BOTTOM, colorBack);
		gradientBack.setCornerRadius(10);

		GradientDrawable gradientLoginBack = new GradientDrawable();
		gradientLoginBack.setColor(Color.rgb(16, 110, 136));
		gradientLoginBack.setCornerRadius(10);

		txtUserName = new EditText(this);
		txtUserName.setHint("Username");
		txtUserName.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		txtUserName.setHintTextColor(Color.DKGRAY);
		txtUserName.setTextColor(Color.rgb(0, 0, 255));
		txtUserName.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
		txtUserName.setTextSize(13);
		txtUserName.setBackgroundDrawable(gradientBack);
		txtUserName.setSingleLine(true);
		txtUserName.setWidth(width / 2);
		row1.addView(txtUserName);

		TableRow row2 = new TableRow(this);
		row2.setGravity(Gravity.CENTER);
		row2.setPadding(1, 4, 1, 4);

		txtPassword = new EditText(this);
		txtPassword.setHint("Password");
		txtPassword.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		txtPassword.setHintTextColor(Color.DKGRAY);
		txtPassword.setTextColor(Color.rgb(0, 0, 255));
		txtPassword.setSingleLine(true);
		txtPassword.setBackgroundDrawable(gradientBack);
		txtPassword.setTextSize(13);
		txtPassword.setTransformationMethod(new PasswordTransformationMethod());
		txtPassword.setWidth(width / 2);
		row2.addView(txtPassword);

		String txtData = readTextFile();
		if (!txtData.trim().equals("")) {
			String[] userPass = txtData.split("#");
		    if(userPass.length<=2){
			    txtUserName.setText(userPass[0]);
			    txtPassword.setText(userPass[1]);
            }
            else{
                txtUserName.setText(userPass[0]);
                txtPassword.setText(userPass[1]);
                if(userPass[2].contains("\n")){
                    xModuleClass.rsin = userPass[2].replace("\n","");
                }
                else{
                    xModuleClass.rsin = userPass[2].trim();
                }
            }
		}
		userPassLayout.addView(row1);
		userPassLayout.addView(row2);

		TableLayout loginLayout = new TableLayout(this);
		TableRow rowL = new TableRow(this);
		rowL.setGravity(Gravity.CENTER);
		rowL.setPadding(0, 2, 0, 2);

		btnLogin = new Button(this);
		btnLogin.setText("  Login  ");
		btnLogin.setOnClickListener(this);
		btnLogin.setTypeface(Typeface.DEFAULT_BOLD);
		btnLogin.setBackgroundDrawable(gradientLoginBack);
		btnLogin.setTextSize(13);
		btnLogin.setWidth(width / 4);

		rowL.addView(btnLogin);
		// rowL.addView(btnExit);
		loginLayout.addView(rowL);

		TableRow rowRem1 = new TableRow(this);
		rowRem1.setGravity(Gravity.CENTER);
		rowRem1.setPadding(0, 2, 0, 2);

		chkRememberDetails = new CheckBox(this);
		chkRememberDetails.setText("Remember details");
		chkRememberDetails.setTextSize(13);
		chkRememberDetails.setChecked(true);
		chkRememberDetails.setWidth(width / 2);
		rowRem1.addView(chkRememberDetails);
		userPassLayout.addView(rowRem1);

		RelativeLayout relative = new RelativeLayout(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

		Button footer = new Button(this);
		footer.setText("Powered By : Xtremsoft Technologies Mumbai");
		footer.setEnabled(true);
		footer.setTypeface(Typeface.DEFAULT_BOLD);
		footer.setBackgroundResource(0);
		footer.setTextColor(iLogoBackColor);
		footer.setTextSize(12);
		footer.setGravity(Gravity.CENTER);
		footer.setTransformationMethod(SingleLineTransformationMethod
				.getInstance());
		footer.setWidth(width);
		footer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent myIntent = new Intent(LoginScreen.this,
						XTREMScreen.class);
				LoginScreen.this.startActivity(myIntent);
			}
		});
		relative.addView(footer, params);

		loginScreen.addView(userPassLayout);
		loginScreen.addView(loginLayout);
		loginScreen.addView(relative, new TableLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		this.setContentView(loginScreen);

	}

	public interface OnPermissionAccepted {
		void OnPermission(String tag);
	}

	private String _permissionTag = "";
	private OnPermissionAccepted _onpermission;
	public final int REQUEST_CODE_PERMISSION = 10000;

	public void checkPermission(String[] permissions, OnPermissionAccepted _onpermission) {
		checkPermission(permissions, "", _onpermission);
	}

	public void checkPermission(String[] permissions, String _permissionTag, OnPermissionAccepted _onpermission) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkPermission(permissions)) {
			this._permissionTag = _permissionTag;
			this._onpermission = _onpermission;
			requestPermission(permissions);
		} else {
			_onpermission.OnPermission(_permissionTag);
		}
	}

	public boolean checkPermission(String[] permissions) {
		boolean permissionGranted = true;
		for (String _permissionStr : permissions) {
			int _permissionInt = ContextCompat.checkSelfPermission(this, _permissionStr);
			if (_permissionInt != PackageManager.PERMISSION_GRANTED) {
				permissionGranted = false;
				break;
			}
		}
		return permissionGranted;
	}

	private void requestPermission(String[] permissions) {
		ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_PERMISSION);
	}


	@TargetApi(Build.VERSION_CODES.M)
	@RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
		switch (requestCode) {
			case REQUEST_CODE_PERMISSION:
				if (grantResults.length > 0) {
					boolean _isAllPermissionGranted = checkPermission(permissions);
					if (_isAllPermissionGranted) {
						if (_onpermission != null) _onpermission.OnPermission(_permissionTag);
					} else {
						for (String _permissionStr : permissions) {
							if (shouldShowRequestPermissionRationale(_permissionStr)) {
								new AlertDialog.Builder(this)
										.setTitle("Permission Denied!")
										.setMessage("You need to allow all the permissions to access Features")
										.setPositiveButton("OK", (dialog, which) -> requestPermission(permissions))
										.setNegativeButton("Cancel", null)
										.create()
										.show();
								break;
							}
						}
					}
					break;
				}
		}
	}
	@TargetApi(Build.VERSION_CODES.CUPCAKE)
    public void onClick(View v) {
		Button btn = (Button) v;
		if (btn == btnLogin) {

            if (!xModuleClass.isInternetAvailable(LoginScreen.this)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginScreen.this);
                builder.setMessage("Please check your Internet Connection")
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.dismiss();
                                        //System.exit(0);
                                        //HospitalList.this.finish();
                                    }
                                }
                        );
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                String[] _permissions = {READ_PHONE_STATE,WRITE_EXTERNAL_STORAGE};
                checkPermission(_permissions, tag -> new GetTask(this).execute());
            }

           /* if(!xModuleClass.isInternetAvailable(LoginScreen.this)){
                new GetTask(this).execute();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginScreen.this);
                builder.setMessage("Please check your Internet Connection")
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.dismiss();
                                        //System.exit(0);
                                        //LoginScreen.this.finish();
                                    }
                                }
                        );
                AlertDialog alert = builder.create();
                alert.show();
            }*/


			/*
			 * try { File sdcard = Environment.getExternalStorageDirectory();
			 * 
			 * //Get the text file File file = new
			 * File(sdcard,"/download/info.zip");
			 * 
			 * //Read text from file StringBuilder text = new StringBuilder();
			 * 
			 * try { BufferedReader br = new BufferedReader(new
			 * FileReader(file)); String line;
			 * 
			 * while ((line = br.readLine()) != null) { text.append(line);
			 * text.append('\n'); } } catch (IOException e) { //You'll need to
			 * add proper error handling here }
			 * 
			 * String serverIP = text.substring(0,text.indexOf("#")); String
			 * temp = text.substring(text.indexOf("#")+1);
			 * 
			 * String port = temp.substring(0, temp.indexOf("#")); String image
			 * = temp.substring(temp.indexOf("#")+1);
			 * 
			 * strConnect = "http://" + serverIP + ":" + port;
			 * 
			 * authenticate(); if(isValid.equals("true")) { this.finish();
			 * loadCEODBScreen(); } else if(isValid.equals("false")) {
			 * AlertDialog.Builder builder = new AlertDialog.Builder(this);
			 * builder
			 * .setMessage("Incorrect User ID or Password... Please try again..."
			 * ) .setCancelable(false) .setPositiveButton("OK", new
			 * DialogInterface.OnClickListener() { public void
			 * onClick(DialogInterface dialog, int id) { dialog.dismiss(); } })
			 * .setNegativeButton("Cancel", new
			 * DialogInterface.OnClickListener() { public void
			 * onClick(DialogInterface dialog, int id) { dialog.dismiss(); } });
			 * AlertDialog alert = builder.create(); alert.show(); } }
			 * catch(Exception e) { System.out.println(e.toString());
			 * 
			 * AlertDialog.Builder builder = new AlertDialog.Builder(this);
			 * builder.setMessage(e.toString()) .setCancelable(false)
			 * .setPositiveButton("OK", new DialogInterface.OnClickListener() {
			 * public void onClick(DialogInterface dialog, int id) {
			 * dialog.dismiss(); } }); AlertDialog alert = builder.create();
			 * alert.show(); }
			 */
		} else if (btn == btnExit) {
			System.exit(0);
		}
	}

	public void loadMiddleScreen() {
		Intent myIntent = new Intent(LoginScreen.this, MiddleScreen.class);
		// myIntent.putExtra("strConnect", strConnect);
		LoginScreen.this.startActivity(myIntent);
		LoginScreen.this.finish();
	}

	/*
	 * public void loadCEODBScreen() { Intent myIntent = new
	 * Intent(LoginScreen.this, CEODBScreen.class);
	 * myIntent.putExtra("strConnect", strConnect);
	 * LoginScreen.this.startActivity(myIntent); LoginScreen.this.finish(); }
	 */

	public void authenticate() {
		try {
			// retrieve a reference to an instance of TelephonyManager
			TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(xModuleClass.strConnectString + "/ceodb/runLoginServlet");
			// HttpGet httpget = new
			// HttpGet("http://61.12.61.163:5054/ceodb/runLoginServlet");
			// HttpGet httpget = new
			// HttpGet("http://172.168.10.16:8080/ceodbNew/runLoginServlet");

			httpget.addHeader("User-Agent",
					"Profile/MIDP-2.0 Confirguration/CLDC-1.0");
			httpget.addHeader("Accept", "text/plain");
			httpget.addHeader("IMEI", getDeviceID(telephonyManager));
			httpget.addHeader("UserName", txtUserName.getText().toString()
					.trim());
			httpget.addHeader("Password", txtPassword.getText().toString()
					.trim());
			HttpResponse response = httpclient.execute(httpget);
			in = new DataInputStream(response.getEntity().getContent());

			isValid = in.readUTF();
			reports=in.readUTF();

			in.close();
			in = null;
		} catch (EOFException e) {
			// Dialog.alert("Data Loaded...!!!");
		} catch (IOException e) {
			System.out.println("Caught IOException: " + e.toString());
			System.err.println("Caught IOException: " + e.getMessage());
			isValid = "error";
		} catch (Exception e) {
			System.out.println("Caught Exception: " + e.toString());
			System.err.println("Caught Exception: " + e.getMessage());
			isValid = "error";
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException ignore) {
				}
		}
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

	@TargetApi(Build.VERSION_CODES.CUPCAKE)
	class GetTask extends AsyncTask<Object, Void, String> {
		Context context;

		ProgressDialog mDialog;
		boolean isRCConnet = false;
		String strMsg = "";

		// GetTask(Context context,String userid)
		GetTask(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			mDialog = new ProgressDialog(context);

			mDialog.setMessage("Logging in... Please wait...");
			mDialog.show();
		}

		@Override
		protected String doInBackground(Object... params) {

			try {
				/*File sdcard = Environment.getExternalStorageDirectory();

				// Get the text file
				File file = new File(sdcard, "/download/info.zip");
				// Read text from file
				StringBuilder text = new StringBuilder();

				try {
					BufferedReader br = new BufferedReader(new FileReader(file));
					String line;

					while ((line = br.readLine()) != null) {
						text.append(line);
						text.append('\n');
					}
				} catch (IOException e) {
					// You'll need to add proper error handling here
				}

				String serverIP = text.substring(0, text.indexOf("#"));
				String temp = text.substring(text.indexOf("#") + 1);

				String port = temp.substring(0, temp.indexOf("#"));
				String image = temp.substring(temp.indexOf("#") + 1);

				strConnect = "http://" + serverIP + ":" + port;
                xModuleClass.strConnectString = strConnect;*/

                /*if(xModuleClass.isInternetAvailable(LoginScreen.this)){
                    //new GetTask(this).execute();
                    authenticate();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginScreen.this);
                    builder.setMessage("Please check your Internet Connection")
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {
                                            dialog.dismiss();
                                            //System.exit(0);
                                            //LoginScreen.this.finish();
                                        }
                                    }
                            );
                    AlertDialog alert = builder.create();
                    alert.show();
                }*/
				authenticate();


				if (isValid.equals("true")) {
					if (chkRememberDetails.isChecked()) {
						writeTextFile(txtUserName.getText() + "#"+ txtPassword.getText()+"#"+xModuleClass.rsin);
					} else {
						writeTextFile("");
					}
					loadMiddleScreen();
					// loadCEODBScreen();
				} else if (isValid.equals("false")) {
					strMsg = "Incorrect User ID or Password... Please try again..";
				}
                else{
                    strMsg = isValid;
                }
			} catch (Exception e) {
				System.out.println(e.toString());
				strMsg = e.toString();
				/*
				 * AlertDialog.Builder builder = new AlertDialog.Builder(this);
				 * builder.setMessage(e.toString()) .setCancelable(false)
				 * .setPositiveButton("OK", new
				 * DialogInterface.OnClickListener() { public void
				 * onClick(DialogInterface dialog, int id) { dialog.dismiss(); }
				 * }); AlertDialog alert = builder.create(); alert.show();
				 */
			}

			return "";
			// here you can get the details from db or web and fetch it..
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			mDialog.dismiss();

			try {
				if (isValid.equals("false")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            LoginScreen.this);
                    builder.setMessage(strMsg)
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                int id) {
                                            dialog.dismiss();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else if (!strMsg.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            LoginScreen.this);
                    builder.setMessage(strMsg)
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                int id) {
                                            dialog.dismiss();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	String getDeviceID(TelephonyManager phonyManager) {
		String id = phonyManager.getDeviceId();
		if (id == null) {
			id = "not available";
		}
		int phoneType = phonyManager.getPhoneType();
		switch (phoneType) {
		case TelephonyManager.PHONE_TYPE_NONE:
			// return "NONE: " + id;
			return id;

		case TelephonyManager.PHONE_TYPE_GSM:
			// return "GSM: IMEI=" + id;
			return id;

		case TelephonyManager.PHONE_TYPE_CDMA:
			// return "CDMA: MEID/ESN=" + id;
			return id;
		default:
			return "UNKNOWN: ID=" + id;
		}
	}
}
