package com.nutlabs.restaurant;

import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.database.DataSetObserver;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.nutlabs.restaurant.kore.utils.Utils;

//import com.afollestad.materialdialogs.MaterialDialog;
//import com.dd.CircularProgressButton;
//import org.telegram.messenger.AndroidUtilities;
//import org.telegram.messenger.LocaleController;

public class Intro extends Activity {
    private ViewPager viewPager;
    private ImageView topImage1;
    private ImageView topImage2;
    private ViewGroup bottomPages;
    private int lastPage = 0;
    private boolean justCreated = false;
   // private boolean startPressed = false;
    private int[] icons;
    private int[] titles;
    private int[] messages;
    private static final String TAG = "wiflick.intro";
    public static float density = 1;
    private boolean canProceed = true;
    //private EditText MobileNumber, CountryCode;
    private View CustomeDialogView;
    private View verifyOTPDialogView;
    public static SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Request transitions on lollipop
        if (Utils.isLollipopOrLater()) {
           // getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }

        super.onCreate(savedInstanceState);


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.intro_layout);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();
        String number = "";
        boolean bIsFirstLaunchDone = pref.contains("FirstLaunchDone");
        if (bIsFirstLaunchDone==true){
            Intent intent2;
            if (pref.getBoolean(SharedConstants.PREF_USER_DATA_SAVED,false)==false){
                intent2 = new Intent(Intro.this, RegisterationActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent2);
            }
            else {
                intent2 = new Intent(Intro.this, ServerDiscoveryActivity.class);
                //intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent2);

            }
        }
        else
        {
            editor = pref.edit();
            editor.putBoolean("FirstLaunchDone", true);
            try {
                PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                int versionCode = pinfo.versionCode;
                editor.putInt("VersionCode",versionCode);
            }
             catch (Exception e){
            }


            editor.commit();
        }

            icons = new int[] {
                    R.drawable.travel1,
                    R.drawable.wifi_connect1,
                    R.drawable.multimedia_page,

            };
            titles = new int[] {
                    R.string.Page1Title,
                    R.string.Page2Title,
                    R.string.Page3Title,

            };
            messages = new int[] {
                    R.string.Page1Message,
                    R.string.Page2Message,
                    R.string.Page3Message,

            };
        viewPager = (ViewPager)findViewById(R.id.intro_view_pager);
        TextView skipIntroButton = (TextView) findViewById(R.id.skipIntro);
        skipIntroButton.setText("Skip Intro".toUpperCase());
        //ImageButton goButton = (ImageButton) findViewById(R.id.Go);
       // MobileNumber = (EditText) findViewById(R.id.phoneNumber);
        //CountryCode = (EditText) findViewById(R.id.code);
        //startMessagingButton.setText("Go".toUpperCase());
        if (Build.VERSION.SDK_INT >= 21) {
            StateListAnimator animator = new StateListAnimator();
            animator.addState(new int[] {android.R.attr.state_pressed}, ObjectAnimator.ofFloat(skipIntroButton, "translationZ", dp(2), dp(4)).setDuration(200));
            animator.addState(new int[] {}, ObjectAnimator.ofFloat(skipIntroButton, "translationZ", dp(4), dp(2)).setDuration(200));
            skipIntroButton.setStateListAnimator(animator);
        }
        topImage1 = (ImageView)findViewById(R.id.icon_image1);
        topImage2 = (ImageView)findViewById(R.id.icon_image2);
        bottomPages = (ViewGroup)findViewById(R.id.bottom_pages);
        topImage2.setVisibility(View.GONE);
        viewPager.setAdapter(new IntroAdapter());
        viewPager.setPageMargin(0);
        viewPager.setOffscreenPageLimit(1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if (i == ViewPager.SCROLL_STATE_IDLE || i == ViewPager.SCROLL_STATE_SETTLING) {
                    if (lastPage != viewPager.getCurrentItem()) {
                        lastPage = viewPager.getCurrentItem();

                        final ImageView fadeoutImage;
                        final ImageView fadeinImage;
                        if (topImage1.getVisibility() == View.VISIBLE) {
                            fadeoutImage = topImage1;
                            fadeinImage = topImage2;

                        } else {
                            fadeoutImage = topImage2;
                            fadeinImage = topImage1;
                        }

                        fadeinImage.bringToFront();
                        fadeinImage.setImageResource(icons[lastPage]);
                        fadeinImage.clearAnimation();
                        fadeoutImage.clearAnimation();


                        Animation outAnimation = AnimationUtils.loadAnimation(Intro.this, R.anim.icon_anim_fade_out);
                        outAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                fadeoutImage.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                        Animation inAnimation = AnimationUtils.loadAnimation(Intro.this, R.anim.icon_anim_fade_in);
                        inAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                fadeinImage.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });


                        fadeoutImage.startAnimation(outAnimation);
                        fadeinImage.startAnimation(inAnimation);
                    }
                }
            }
        });

        skipIntroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (startPressed) {
                //    return;
                //}
                //startPressed = true;

                //Intent intent2 = new Intent(Intro.this, MainActivityNew.class);
                //startActivity(intent2);

                //Intent intent2 = new Intent(Intro.this, ServerDiscoveryActivity.class);
                //intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //startActivity(intent2);

                Intent intent2 = new Intent(Intro.this, RegisterationActivity.class);
                //intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent2);


            }
        });

        //justCreated = true;
    }

    /*private void confirmPhoneNumberDialog(final String mobileNumber) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        CustomeDialogView = inflater
                .inflate(R.layout.layout_dialog_confirm_number, null);
        TextView mobileNumberText = (TextView) CustomeDialogView.findViewById(R.id.mobilenumber);
        mobileNumberText.setText(mobileNumber);
        final AlertDialog alertDialog = new AlertDialog.Builder(Intro.this).create();
        alertDialog.setTitle("Confirm Number");
        alertDialog.setView(CustomeDialogView);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //alertDialog.dismiss();
                verifyOTP(mobileNumber);
                }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "EDIT", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                canProceed = false;
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void moveToMainActivity(String mobileNumber) {
        if(canProceed) {

            SharedPreferences.Editor editor = MainActivity.pref.edit();
            editor.putString("mobileNumber", mobileNumber);
            editor.apply();
            Intent intent2 = new Intent(Intro.this, MainActivity.class);
            intent2.putExtra("fromIntro", true);
            startActivity(intent2);
            finish();
        }
    }

    private void verifyOTP(final String mobilenumber) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        verifyOTPDialogView = inflater
                .inflate(R.layout.layout_dialog_verify_otp, null);
        final EditText otpNum = (EditText) verifyOTPDialogView.findViewById(R.id.otp);
       // mobileNumberText.setText(mobileNumber);
        final AlertDialog alertDialog = new AlertDialog.Builder(Intro.this).create();
        alertDialog.setTitle("Verify OTP");
        alertDialog.setView(verifyOTPDialogView);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Verify", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                final String num = otpNum.getText().toString();
                Log.i(TAG, "OTP=" + num);
                if (num.trim().equals("1234")) {
                    Log.i(TAG, "Inside 1234 = " + num);
                    canProceed = true;
                    moveToMainActivity(mobilenumber);
                } else {
                    Log.i(TAG, "Inside wrong = " + num);
                    Toast.makeText(Intro.this, "Not a valid OTP", Toast.LENGTH_SHORT).show();
                    canProceed = false;
                }

            }
        });
        alertDialog.show();
       } */

    public static int dp(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(density * value);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (justCreated) {
            viewPager.setCurrentItem(0);
            lastPage = 0;
            justCreated = false;
        }
    }

    //@Override
    //protected void onPause() {
      //  super.onPause();

    //}

    private class IntroAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return 3;
        }

       @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(container.getContext(), R.layout.intro_view_layout, null);
            TextView headerTextView = (TextView)view.findViewById(R.id.header_text);
            TextView messageTextView = (TextView)view.findViewById(R.id.message_text);
            container.addView(view, 0);

            headerTextView.setText(getString(titles[position]));
            messageTextView.setText((messages[position]));

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            int count = bottomPages.getChildCount();
            for (int a = 0; a < count; a++) {
                View child = bottomPages.getChildAt(a);
                if (a == position) {
                    child.setBackgroundColor(0xff2ca5e0);
                } else {
                    child.setBackgroundColor(0xffbbbbbb);
                }
            }
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
            if (observer != null) {
                super.unregisterDataSetObserver(observer);
            }
        }
    }
}