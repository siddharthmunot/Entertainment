package com.nutlabs.restaurant;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.nutlabs.restaurant.kore.ui.BaseActivity;

import org.apache.http.HttpStatus;


public class FeedbackActivity extends BaseActivity {
    RatingBar rbRideExprnc;
    RatingBar rbPinutExprnc;
    EditText etComment;
    Button btnSubmit;
    FeedbackTask mFeedbackTask;
    Communicator mCommunicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        mCommunicator=Communicator.getInstance(this);
        rbRideExprnc=(RatingBar)findViewById(R.id.ratingBarRide);
        rbPinutExprnc=(RatingBar)findViewById(R.id.ratingBarPinut);
        etComment=(EditText)findViewById(R.id.etxtComments);
        addListenerOnButtonClick();
        setupActionBar();
    }

    public void addListenerOnButtonClick(){

        btnSubmit=(Button)findViewById(R.id.btnSubmit);
        //Performing action on Button Click
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                mFeedbackTask=new FeedbackTask();
                mFeedbackTask.execute((Void) null);
                finish();

                //Getting the rating and displaying it on the toast
                //String rating = String.valueOf(ratingbar1.getRating());
                //Toast.makeText(getApplicationContext(), rating, Toast.LENGTH_LONG).show();
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_feedback, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
          //  return true;
        //}

        //return super.onOptionsItemSelected(item);
        return true;
    }

    /**
     * Represents an asynchronous user connection api call
     */
    public class FeedbackTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                String comment="";
                if (etComment.getText().toString()!=null || etComment.getText().toString().length()>0)
                    comment=etComment.getText().toString();
                Log.d(SharedConstants.TAG, "doinbackground is called for feedback api");
                int retVal = mCommunicator.feeadback((int)rbRideExprnc.getRating(),(int)rbPinutExprnc.getRating(),comment);
                if (retVal == HttpStatus.SC_OK) {
                    Log.d(SharedConstants.TAG, "Feedback API successfull");

                } else {
                    Log.d(SharedConstants.TAG, "Feedback API call failed");

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

    private void setupActionBar() {
        //Toolbar toolbar = (Toolbar) findViewById(R.id.default_toolbar);
        //setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle("Feedback");
    }
}
