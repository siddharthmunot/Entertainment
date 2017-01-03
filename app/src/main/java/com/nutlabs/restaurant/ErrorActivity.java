package com.nutlabs.restaurant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import com.nutlabs.restaurant.kore.ui.BaseActivity;
import com.nutlabs.restaurant.kore.utils.UIUtils;


public class ErrorActivity extends BaseActivity {

    TextView messageTextView;
    WifiReceiver receiver;
    boolean firstTime;
    boolean bWifiConected=false;
    boolean bDataCaonnetionOn=false;
    int appId;
    SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        firstTime=true;
        receiver=new WifiReceiver();
        Intent intent = getIntent();
        String Error_Msg = intent.getExtras().getString(SharedConstants.ERROR_MSG);
        messageTextView=(TextView)findViewById(R.id.text_error);
        checkWiFiAndDataConnectionStatus();
        SetErrorText();
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(true);
                UIUtils.showRefreshAnimation(swipeLayout);
                //Launch the server discovery activity
                Intent launchIntent = new Intent(getApplicationContext(), ServerDiscoveryActivity.class);
                if (launchIntent != null) {
                    launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    getApplicationContext().startActivity(launchIntent);
                }
            }
        });



        /*if (messageTextView!=null){
            messageTextView.setText(Error_Msg);
        }
        else
        {
            messageTextView.setText(SharedConstants.ERROR_MSG_SERVER_NOT_FOUND);
        }
        */
    }


    @Override
    public void onPause() {
        super.onPause();
        //unregisterReceiver(receiver);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(receiver);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //registerReceiver(receiver, new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_error, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class WifiReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Toast.makeText(context, "onReceive of WiFiConnectionListener", Toast.LENGTH_LONG);
            Log.d("PINUT", "onReceive of WifiReceiver Error Activity");
            if(firstTime || isInitialStickyBroadcast()) {
                firstTime = false;
                return;
            }

            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (networkInfo.isConnected()) {
                final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {

                    //Launch the server discovery activity
                    Intent launchIntent = new Intent(context, ServerDiscoveryActivity.class);
                    if (launchIntent != null) {
                        launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(launchIntent);
                    }
                }
            }
        }
    }

    private boolean checkWiFiAndDataConnectionStatus(){

        ConnectivityManager connManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        //Check the mobile data connection state
        NetworkInfo activeNetwork = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        bDataCaonnetionOn = activeNetwork != null &&
                activeNetwork.isConnected();

        //Check for the wifi state of the device
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                bWifiConected=true;
                String SSID=connectionInfo.getSSID();
                if (SSID.startsWith("\"") && SSID.endsWith("\"")){
                    SSID = SSID.substring(1, SSID.length()-1);
                }

                if (SSID.compareToIgnoreCase(SharedConstants.SSID_PINUT)==0) {
                    appId=SharedConstants.APP_ID_PINUT ;
                    return true;
                }
                else if(SSID.compareToIgnoreCase(SharedConstants.SSID_WOOBUS)==0){
                    appId=SharedConstants.APP_ID_WOOBUS;
                    return true;
                }
                else{
                    appId=SharedConstants.APP_ID_GENERAL ;
                    return true;
                }
            }
            else{
                //Wifi not connected
                bWifiConected=false;
                return false;
            }
        }
        else {
            bWifiConected=false;
            return false;

        }

    }

    private void SetErrorText(){
        if (bWifiConected){
            if(bDataCaonnetionOn){
                messageTextView.setText(SharedConstants.ERROR_MSG_WIFI_CONNECTED_DATA_CONNECTED_PINUT);
                return;
            }
            else{
                messageTextView.setText(SharedConstants.ERROR_MSG_WIFI_CONNECTED_ONLY_PINUT );
                return;
            }
        }else if(bDataCaonnetionOn){
            messageTextView.setText(SharedConstants.ERROR_MSG_DATA_CONNECTED_ONLY_PINUT);
            return;

        }
        else
        {
            messageTextView.setText(SharedConstants.ERROR_MSG_WIFI_NOT_CONNECTED_PINUT);
            return;
        }

    }
}
