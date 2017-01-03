package com.nutlabs.restaurant;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by mdimran on 4/3/2016.
 */
public class Communicator  {
    private static Communicator instance = null;
    private static String serverUrl;
    private static String serverPort;
    private static String serverBaseUrl;
    private static String macAddr;
    private static Context ctx;
    private static String name;
    private static String emailId;
    private static String phoneNo;

    public static SharedPreferences pref;
    private static SharedPreferences.Editor editor;

    //make the constructor private so that this class cannot be instantiated
    private Communicator(){

    }

    public static Communicator getInstance(Context context){
        if(instance==null){
            instance=new Communicator();
            ctx=context;
            init(ctx);
            return instance;
        }
        else{
            init(context);
            return instance;
        }

    }

    public static void init(Context context){
        WifiManager manager = (WifiManager)ctx.getSystemService(context .WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        macAddr = info.getMacAddress();
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        editor=pref.edit();
        name=pref.getString(SharedConstants.PREF_NAME,"");
        emailId=pref.getString(SharedConstants.PREF_EMAIL,"");
        phoneNo=pref.getString(SharedConstants.PREF_PHONE,"");
        serverUrl=pref.getString(SharedConstants.PREF_SERVER_URL,"");
        //serverUrl="192.168.1.5";
        //serverUrl="192.168.116.10";
        serverPort=SharedConstants.DEFAULT_SERVER_PORT_PYTHON;
        serverBaseUrl="http://"+serverUrl+":"+serverPort+SharedConstants.SERVER_APP_PATH;
        Log.d(SharedConstants.TAG,"ServerBaseUrl="+serverBaseUrl);
    }

    public int connectionInfo() {
        try {
            //Log.d(TAG, "attempting to login now=");
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(serverBaseUrl +SharedConstants.API_CONNECTION_INFO);
            Log.d(SharedConstants.TAG,"Connection info url="+serverBaseUrl +SharedConstants.API_CONNECTION_INFO);
            JSONObject jsonConn = new JSONObject();

            try {
                jsonConn.put(SharedConstants.JSON_FIELD_CLIENT_MAC, macAddr);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            httppost.addHeader("Content-Type",
                    "application/json");
            StringEntity se = new StringEntity( jsonConn.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httppost.setEntity(se);

            //Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            int status=response.getStatusLine().getStatusCode();
            Log.d(SharedConstants.TAG,"http status value return is "+ status);
            if ( status==HttpStatus.SC_OK){
                Log.d(SharedConstants.TAG,"API call connection info succcessfull");
            }
            return status;

        } catch (ClientProtocolException e) {
            Log.d(SharedConstants.TAG,"found1 exception in API connectioninfo", e);
            return -1;
        } catch (IOException e) {
            Log.d(SharedConstants.TAG,"found2 exception in API connectioninfo", e);
            return -1;
        }
    }

    public int viewDetails(String data , String category, int viewLength, int dataLength) {
        try {
            //Log.d(TAG, "attempting to login now=");
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(serverBaseUrl+SharedConstants.API_VIEW_DETAILS);
            Log.d(SharedConstants.TAG,"View details url="+serverBaseUrl +SharedConstants.API_VIEW_DETAILS);
            JSONObject jsonViewDetails = new JSONObject();

            try {
                jsonViewDetails.put(SharedConstants.JSON_FIELD_CLIENT_MAC, macAddr);
                jsonViewDetails.put(SharedConstants.JSON_FIELD_EMAIL_ID, emailId);
                jsonViewDetails.put(SharedConstants.JSON_FIELD_PHONE, phoneNo);
                jsonViewDetails.put(SharedConstants.JSON_FIELD_DATA, data );
                jsonViewDetails.put(SharedConstants.JSON_FIELD_CATEGORY, category);
                jsonViewDetails.put(SharedConstants.JSON_FIELD_DATA_LENGTH, dataLength);
                jsonViewDetails.put(SharedConstants.JSON_FIELD_VIEW_LENGTH, viewLength);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(SharedConstants.TAG,jsonViewDetails.toString());
            httppost.addHeader("Content-Type",
                    "application/json");
            StringEntity se = new StringEntity( jsonViewDetails.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httppost.setEntity(se);

            //Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            int status=response.getStatusLine().getStatusCode();
            Log.d(SharedConstants.TAG,"http status value return is "+ status);
            if ( status==HttpStatus.SC_OK){
                Log.d(SharedConstants.TAG,"API call view details info succcessfull");
            }
            return status;

        } catch (ClientProtocolException e) {
            Log.d(SharedConstants.TAG,"found1 exception in API view details", e);
            return -1;
        } catch (IOException e) {
            Log.d(SharedConstants.TAG,"found2 exception in API view details", e);
            return -1;
        }
    }

    public int userDetails() {
        try {
            //Log.d(TAG, "attempting to login now=");
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(serverBaseUrl+SharedConstants.API_USER_DETAILS);
            Log.d(SharedConstants.TAG,"User details="+serverBaseUrl +SharedConstants.API_USER_DETAILS);
            JSONObject jsonLogin = new JSONObject();

            try {
                jsonLogin.put(SharedConstants.JSON_FIELD_CLIENT_MAC, macAddr);
                jsonLogin.put(SharedConstants.JSON_FIELD_NAME, name);
                jsonLogin.put(SharedConstants.JSON_FIELD_EMAIL_ID, emailId);
                jsonLogin.put(SharedConstants.JSON_FIELD_PHONE, phoneNo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            httppost.addHeader("Content-Type",
                    "application/json");
            StringEntity se = new StringEntity( jsonLogin.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httppost.setEntity(se);

            //Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            int status=response.getStatusLine().getStatusCode();
            Log.d(SharedConstants.TAG,"http status value return is "+ status);
            if ( status==HttpStatus.SC_OK){
                Log.d(SharedConstants.TAG,"API call user details info succcessfull");
             }
            return status;

        } catch (ClientProtocolException e) {
            Log.d(SharedConstants.TAG,"found1 exception in API user details", e);
            return -1;
        } catch (IOException e) {
            Log.d(SharedConstants.TAG,"found2 exception in API user details", e);
            return -1;
        }
    }


    public int feeadback(int rideRating, int pinutRating,String comment) {
        try {
            //Log.d(TAG, "attempting to login now=");
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(serverBaseUrl+SharedConstants.API_FEEDBACK);
            Log.d(SharedConstants.TAG,"Feedback url="+serverBaseUrl +SharedConstants.API_FEEDBACK);
            JSONObject jsonFeedback = new JSONObject();

            try {
                jsonFeedback.put(SharedConstants.JSON_FIELD_CLIENT_MAC, macAddr);
                jsonFeedback.put(SharedConstants.JSON_FIELD_NAME, name);
                jsonFeedback.put(SharedConstants.JSON_FIELD_EMAIL_ID, emailId);
                jsonFeedback.put(SharedConstants.JSON_FIELD_PHONE, phoneNo);
                jsonFeedback.put(SharedConstants.JSON_FIELD_RIDE_EXPRNC, rideRating);
                jsonFeedback.put(SharedConstants.JSON_FIELD_PINUT_EXPRNC, pinutRating);
                jsonFeedback.put(SharedConstants.JSON_FIELD_USER_COMMENT, comment);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            httppost.addHeader("Content-Type",
                    "application/json");
            StringEntity se = new StringEntity( jsonFeedback.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httppost.setEntity(se);

            //Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            int status=response.getStatusLine().getStatusCode();
            Log.d(SharedConstants.TAG,"http status value return is "+ status);
            if ( status==HttpStatus.SC_OK){
                Log.d(SharedConstants.TAG,"API call user details info succcessfull");
            }
            return status;

        } catch (ClientProtocolException e) {
            Log.d(SharedConstants.TAG,"found1 exception in API user details", e);
            return -1;
        } catch (IOException e) {
            Log.d(SharedConstants.TAG,"found2 exception in API user details", e);
            return -1;
        }
    }
    public int mcqresult(String i, int attempts){
        try
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(serverBaseUrl+SharedConstants.API_ASSESMENT);
            Log.d(SharedConstants.TAG,"User details="+serverBaseUrl +SharedConstants.API_ASSESMENT);
            JSONObject jsonFeedback = new JSONObject();
            try {
                jsonFeedback.put(SharedConstants.JSON_FIELD_CLIENT_MAC, macAddr);
                jsonFeedback.put(SharedConstants.JSON_FIELD_NAME, name);
                jsonFeedback.put(SharedConstants.JSON_FIELD_EMAIL_ID, emailId);
                jsonFeedback.put(SharedConstants.JSON_FIELD_PHONE, phoneNo);
                jsonFeedback.put(SharedConstants.JSON_COUNT, attempts);

              //  jsonFeedback.put(SharedConstants.JSON_COUNT, count);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            httppost.addHeader("Content-Type",
                    "application/json");
            StringEntity se = new StringEntity( jsonFeedback.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httppost.setEntity(se);
            HttpResponse response = httpclient.execute(httppost);

            int status=response.getStatusLine().getStatusCode();
        Log.d(SharedConstants.TAG,"http status value return is "+ status);
        if ( status==HttpStatus.SC_OK){
            Log.d(SharedConstants.TAG,"API call user details info succcessfull");
        }
        return status;

    } catch (ClientProtocolException e) {
        Log.d(SharedConstants.TAG,"found1 exception in API user details", e);
        return -1;
    } catch (IOException e) {
        Log.d(SharedConstants.TAG,"found2 exception in API user details", e);
        return -1;
    }
    }

}
