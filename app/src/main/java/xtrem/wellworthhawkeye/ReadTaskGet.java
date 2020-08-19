package xtrem.wellworthhawkeye;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewGroup;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.zip.GZIPInputStream;

/**
 * <h1>ReadTaskGet</h1>
 * ReadTaskGet Is a Class That Provides Ability To Read Data From Internet and provide ResultCode and Result it will not throw and exception
 * TODO: Test for Very Large Date Set ie More Than 100Kb it has been tested to read Data UP to 100Kb of data
 * after creating the object we pass URL to execute Method as it Extends AsyncTask
 * This Class Passes Result To NetProcess Object With Response Code
 * To pass URL follow the example<b>new ReadTaskGet(context,process).execute(URL);</b> to pass url use execute method
 * <i>Note: Belongs to AsyncTask and ReadTaskGet extends AsyncTask And All web activities are done in AsyncTask or else it will give an  exception</i>
 *
 * @author Azim Charaniya
 * @version 1.0
 * @since 2014-10-31
 *
 * @author Azim Charaniya
 * @version 1.1
 * @since 2014-11-05
 * GZIP support for web site to load data ie content type is GZIP then it is decompressed
 */
public class ReadTaskGet extends AsyncTask<String, Integer, String> {
    private Context context;
    private ProgressDialog mDialog;

    private NetProcess process;
    private int HTTP_RESULT = HttpURLConnection.HTTP_OK;
    ViewGroup mainView;
    //private CustomProgress customProgressBar;

    /**
     * @param process
     * @see NetProcess
     * There is not ProgressDialog showing Please wait... is shown web data download completely done in background
     */
    public ReadTaskGet(NetProcess process) {
        this(null, process);
    }

    /**
     * @deprecated practically useless since no data is stored or used to after retriving
     */
    public ReadTaskGet() {
        this(null, null);
    }

    /**
     * @param context it can be Object or Activity or something else;
     * @param process Object of NetProcess
     *                this shows an ProgressDialog showing Please wait... if you don't want this then use ReadTaskGet(NetProcess process)
     */

    public ReadTaskGet(Context context, NetProcess process) {
        this.context = context;
        this.process = process;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (null != this.context) {
            /*customProgressBar=new CustomProgress(context);
            customProgressBar.addInParentView(mainView);*/

            mDialog = new ProgressDialog(context);
            mDialog.setMessage("Please wait...");
            mDialog.show();

            // mDialog.s();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        Log.v("URL", params[0]);
        return getResponseFromUrl(params[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (null != this.context) {
            mDialog.dismiss();
            //customProgressBar.removeFromParentView(mainView);

        }
        if (null != this.process) {
            this.process.process(result, HTTP_RESULT);
        }

    }


    private String getResponseFromUrl(String url) {
        String resultString = "";
        HttpResponse httpResponse = null;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            httpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(5, true));
            URL oracle = new URL(url);
            HttpGet httpGet = new HttpGet(oracle.toURI());
            httpGet.addHeader("User-Agent", "Profile/MIDP-2.0 Confirguration/CLDC-1.0");
            httpGet.addHeader("Accept-Encoding", "gzip");
            httpGet.addHeader("Accept", "text/plain");
            //httpGet.addHeader("IMEI", xModuleClass.IMEI);
            //Add Header for System
            httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            Header contentEncoding =  httpResponse.getFirstHeader("Content-Encoding");
            InputStream gzipStream = httpEntity.getContent();
            if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {//checks if it is gzip encoding
                gzipStream = new GZIPInputStream(gzipStream);
            }
            resultString = convertStreamToString(gzipStream);
            //resultString = EntityUtils.toString(httpEntity);
            Log.v("URL_RESULT", resultString);
            Log.v("URL_RESULT", "download Length " + httpEntity.getContentLength() + " Unzip Length " + resultString.length());
            gzipStream.close();
        } catch (UnsupportedEncodingException e) {
            this.HTTP_RESULT = HttpURLConnection.HTTP_BAD_GATEWAY;
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            this.HTTP_RESULT = HttpURLConnection.HTTP_INTERNAL_ERROR;
            e.printStackTrace();
        } catch (IOException e) {
            if (httpResponse != null)
                HTTP_RESULT = httpResponse.getStatusLine().getStatusCode();
            e.printStackTrace();
        } catch (URISyntaxException e) {
            HTTP_RESULT = HttpURLConnection.HTTP_BAD_REQUEST;
            e.printStackTrace();
        }
        return resultString;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString().trim();
    }
}

