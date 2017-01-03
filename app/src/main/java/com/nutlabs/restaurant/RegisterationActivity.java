package com.nutlabs.restaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.nutlabs.restaurant.kore.ui.BaseActivity;
import com.nutlabs.restaurant.kore.utils.Utils;


public class RegisterationActivity extends BaseActivity {
    private EditText name;
    private EditText email;
    private EditText phone;
    private Button btnRegister;

    public static SharedPreferences pref;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Request transitions on lollipop
        if (Utils.isLollipopOrLater()) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_new);

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        name = (EditText) findViewById(R.id.ename);
        email = (EditText) findViewById(R.id.eemail);
        phone = (EditText) findViewById(R.id.ephone);
        btnRegister=(Button)findViewById(R.id.btnregister);
        Typeface sRobotoThin = Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Thin.ttf");
        name.setTypeface(sRobotoThin);
        email.setTypeface(sRobotoThin);
        phone.setTypeface(sRobotoThin);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterUser();
            }
        });

    }

    private void cleanError(EditText text){
        text.setError(null);
    }

  public void RegisterUser(){
      cleanError(name);
      cleanError(email);
      cleanError(phone);

      String nameText = name.getText().toString().trim();

      if (TextUtils.isEmpty(nameText)) {
          name.setError("This field is required");
            name.requestFocus();
          return;
      }



      String emailText = email.getText().toString().trim();
      emailText.replaceAll("\\s","");
      if (TextUtils.isEmpty(emailText)) {
          email.setError("This field is required");
          email.requestFocus();
          return;
      }

      String mobileNumber = phone.getText().toString().trim();
      mobileNumber.replaceAll("\\s","");
      if (TextUtils.isEmpty(mobileNumber)) {
          phone.setError("This field is required");
          phone.requestFocus();
          return;
      }

      if(nameText.length() > 50 ){
          name.setError("Name should not exceed 50 character allowed");
          name.requestFocus();
          return;
      }

      if(mobileNumber.length() != 10 ){
          phone.setError("Enter 10 digit mobile number");
          phone.requestFocus();
          return;
      }

     boolean match=true;
     match = Validator.validateName(nameText);
      if(!match){
          name.setError("Invalid Name");
          name.requestFocus();
          return;
      }

      match = Validator.validateEmail(emailText);
      if(!match){
          email.setError("Invalid Email address");
          email.requestFocus();
          return;
      }

      match = Validator.validatePhone(mobileNumber);
      if(!match){
          phone.setError("Invalid Phone number");
          phone.requestFocus();
          return;
      }

      //write the data in prefrences
      editor=pref.edit();
      editor.putString(SharedConstants.PREF_NAME,nameText);
      editor.putString(SharedConstants.PREF_EMAIL,emailText);
      editor.putString(SharedConstants.PREF_PHONE,mobileNumber);
      editor.putBoolean(SharedConstants.PREF_USER_DATA_SAVED,true);
      editor.putBoolean(SharedConstants.PREF_REGISTRATION_SUCCESSFULL,false);
      editor.commit();

      /*pref.edit();
      Communicator communicator=Communicator.getInstance(this);
      int status=communicator.userDetails();
      if (status== HttpStatus.SC_OK){
         editor.putBoolean(PREF_REGISTRATION_SUCCESSFULL,true);
      }
      else{
          editor.putBoolean(PREF_REGISTRATION_SUCCESSFULL,false);
      }
      editor.commit();
      */

      Intent intent2 = new Intent(RegisterationActivity.this, ServerDiscoveryActivity.class);
      intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
      startActivity(intent2);
  }

  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registeration, menu);
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
}
