package com.nutlabs.restaurant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import org.apache.http.HttpStatus;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class VideoViewActivity extends Activity {

    // Declare variables
    ProgressDialog pDialog;
    VideoView videoview;
    String dataName;
    String dataType;
    Communicator mCommunicator;
    int savedLocation;
    int totalDuration;
    ViewDetailsTask mViewDetailsTask;
    private int currentApiVersion = android.os.Build.VERSION.SDK_INT;
    long startTime;
    long stopTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layout = getWindow().getAttributes();
        layout.screenBrightness = 1F;
        getWindow().setAttributes(layout);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Window w = this.getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //w.setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        // Get the layout from video_main.xml
        setContentView(R.layout.activity_video_view);

        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString("url");
        Log.d(SharedConstants.TAG,"Url="+url);
        //String credentials=SharedConstants.SERVER_DEFAULT_USERNAME+":"+SharedConstants.SERVER_DEFAULT_PASSWORD+"@";
        //url = new StringBuilder(url).insert(7, credentials).toString();
        dataName=bundle.getString(SharedConstants.DATA_NAME);
        dataType=bundle.getString(SharedConstants.DATA_CATEGORY_TYPE);

        mCommunicator=Communicator.getInstance(this);

        // Find your VideoView in your video_main.xml layout
        videoview = (VideoView) findViewById(R.id.VideoView);
        savedLocation = 0;
        totalDuration = 0;
        // Execute StreamVideo AsyncTask

        // Create a progressbar
        pDialog = new ProgressDialog(VideoViewActivity.this);
        // Set progressbar title
        pDialog.setTitle(dataName);
        // Set progressbar message
        pDialog.setMessage("Buffering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        // Show progressbar
        pDialog.show();
        startTime= System.currentTimeMillis();

        try {
            // Start the MediaController
            MediaController mediacontroller = new MediaController(
                    VideoViewActivity.this);
            mediacontroller.setAnchorView(videoview);
                      // Get the URL from String VideoURL
            Uri video = Uri.parse(url);
            Map<String, String> params = new HashMap<String, String>(1);
            final String cred = SharedConstants.SERVER_DEFAULT_USERNAME  + ":" + SharedConstants.SERVER_DEFAULT_PASSWORD;
            //final String auth = "Basic " + Base64.encodeBytes(cred.getBytes("UTF-8"));
            final String auth = "Basic " + Base64.encodeToString(cred.getBytes(), Base64.DEFAULT);
            params.put("Authorization", auth);

            videoview.setMediaController(mediacontroller);
            //videoview.setVideoURI(video);
            //videoview.setVideoURI(video,params);


            Method setVideoURIMethod = videoview.getClass().getMethod("setVideoURI", Uri.class, Map.class);
            setVideoURIMethod.invoke(videoview, video, params);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoview.requestFocus();
        videoview.setOnPreparedListener(new OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                videoview.start();
            }
        });

        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
              onDestroy();
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this,"onBackPressed", Toast.LENGTH_LONG);
        //totalDuration = videoview.getDuration();
        //savedLocation = videoview.getCurrentPosition();
        //Log.i(SharedConstants.TAG, "onBackPressed at " + savedLocation);
        //Log.i(SharedConstants.TAG, "onBackPressed Length is" + totalDuration);
        //communicator.viewDetails(dataName,dataType,totalDuration,savedLocation);
        //mViewDetailsTask = new ViewDetailsTask();
        //mViewDetailsTask.execute((Void) null);

    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
            this.finish();

        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onResume() {
        super.onResume();
        videoview.seekTo(savedLocation);

    }

    @Override
    public void onPause() {
        super.onPause();
        Toast.makeText(this,"onPausePressed", Toast.LENGTH_LONG);
        savedLocation = videoview.getCurrentPosition();
        totalDuration=videoview.getDuration();
        stopTime= System.currentTimeMillis();
        Log.i(SharedConstants.TAG, "onPause at" + savedLocation);
        Log.i(SharedConstants.TAG, "onPause Length is"+totalDuration);
        mViewDetailsTask = new ViewDetailsTask();
        mViewDetailsTask.execute((Void) null);
        if (videoview.isPlaying()) {
            videoview.pause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        //totalDuration = videoview.getDuration();
        //savedLocation = videoview.getCurrentPosition();
        //Log.i(SharedConstants.TAG, "onStop at" + savedLocation);
        //Log.i(SharedConstants.TAG, "onStop Length is" + totalDuration);
        if (videoview.isPlaying()) {
            videoview.stopPlayback();
        }
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
    /**
     * Represents an asynchronous view details api call
     */
    public class ViewDetailsTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Log.d(SharedConstants.TAG, "doinbackground is called"+dataName +", "+dataType +", "+(int)((stopTime-startTime)/(1000))+", "+totalDuration/(1000));
                int retVal = mCommunicator.viewDetails(dataName,dataType,(int)((stopTime-startTime)/(1000)),totalDuration/(1000));
                if (retVal == HttpStatus.SC_OK) {
                    Log.d(SharedConstants.TAG, "View details API successfull");
                } else {
                    Log.d(SharedConstants.TAG, "View details API call failed");
                }

                return true;
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {

        }

        @Override
        protected void onCancelled() {

        }
    }

}