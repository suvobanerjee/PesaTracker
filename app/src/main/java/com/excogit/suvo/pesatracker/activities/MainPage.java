package com.excogit.suvo.pesatracker.activities;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.excogit.suvo.pesatracker.R;
import com.excogit.suvo.pesatracker.constants.StringConstants;
import com.excogit.suvo.pesatracker.fragments.MainPageYearlyMonthlyDispFrag;
import com.excogit.suvo.pesatracker.fragments.SummaryFrag;
import com.excogit.suvo.pesatracker.services.SMSSaveService;

public class MainPage extends AppCompatActivity {

    MyServiceReceiver serviceReceiver;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher_round);
        setSupportActionBar(toolbar);

        serviceReceiver = new MyServiceReceiver(null);
        progressDialog = new ProgressDialog(this);

        SharedPreferences pref = getSharedPreferences(getString(R.string.pref_name),MODE_PRIVATE);

        boolean isInstallFirst = pref.getBoolean(StringConstants.PREF_FIRST_INSTAL_FLAG,true);
        if (isInstallFirst)
        {

            progressDialog.setMessage(getString(R.string.progressbar_text_setup));
            progressDialog.show();

            Uri smsUri = Uri.parse("content://sms/inbox");
            Cursor cursor = getContentResolver().query(smsUri, new String[]{"_id", "address", "date", "body"}, null, null, null);
            cursor.moveToFirst();
            do {
                Log.i("SMS!!!!", cursor.getString(1));
                Log.i("SMS!!!!", cursor.getString(2));
                Log.i("SMS!!!!", cursor.getString(3));
                 if(cursor.getString(1).equals(StringConstants.SENDER_AIRTEL) ||
                    cursor.getString(1).equals(StringConstants.SENDER_MTN) ||
                    cursor.getString(1).equals(StringConstants.SENDER_AFRICEL))
               //if (cursor.getString(1).equals("9004049239"))
                {

                    Intent smsServiceIntent = new Intent(this, SMSSaveService.class);
                    smsServiceIntent.putExtra(StringConstants.SENDER, cursor.getString(1));
                    smsServiceIntent.putExtra(StringConstants.MSG_BODY, cursor.getString(3));
                    smsServiceIntent.putExtra(StringConstants.TIMESTAMP, Long.parseLong(cursor.getString(2)));
                    smsServiceIntent.putExtra(StringConstants.RESULT_RECEIVER_CONSTANT, serviceReceiver);

                    this.startService(smsServiceIntent);

                }

            } while (cursor.moveToNext());
            //progressDialog.dismiss();
        }
        else
        {
            Log.i("SMS!!!FragElse","Inside on frag load");
            fragmentLoad();
        }


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.main_page_bottom_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment selectedFragment = null;

                switch (item.getItemId())
                {
                    case R.id.menu_action_summary:
                        Toast.makeText(getApplicationContext(),"Summary clicked",Toast.LENGTH_SHORT).show();
                        selectedFragment = new SummaryFrag();
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainPage_fragContainer,selectedFragment).commit();
                        break;

                    case R.id.menu_action_refresh:
                        Toast.makeText(getApplicationContext(),"Refresh clicked",Toast.LENGTH_SHORT).show();
                        //fragmentLoad();
                        selectedFragment = new MainPageYearlyMonthlyDispFrag();
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainPage_fragContainer,selectedFragment).commit();
                        break;

                    /*case R.id.menu_action_search:
                        Toast.makeText(getApplicationContext(),"Search clicked",Toast.LENGTH_SHORT).show();
                        break;
*/
                    case R.id.menu_action_home:
                        selectedFragment = new MainPageYearlyMonthlyDispFrag();
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainPage_fragContainer,selectedFragment).commit();
                        break;

                }

                return true;
            }
        });

    }

   /* private boolean isMyServiceRunning()
    {
        boolean isServiceRunning = false;
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo servvice : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if("com.excogit.suvo.pesatracker.services.SMSSaveService".equals(servvice.service.getClassName()))
            {
                isServiceRunning = true;
            }
        }
        return isServiceRunning;
    }
*/
    private void fragmentLoad()
    {
        MainPageYearlyMonthlyDispFrag yearlyMonthlyDispFrag = new MainPageYearlyMonthlyDispFrag();

        getSupportFragmentManager().beginTransaction().add(R.id.mainPage_fragContainer, yearlyMonthlyDispFrag).commit();
    }

    class UpdateUI implements Runnable
    {
        int result;

        UpdateUI(int result)
        {
            this.result = result;
        }

        @Override
        public void run() {

            Log.i("SMS!!!Run","running on ui");
            if(result == 100) {
                progressDialog.dismiss();
                fragmentLoad();
            }
            else
            {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),getString(R.string.universal_failure_msg),Toast.LENGTH_SHORT).show();
                fragmentLoad();
            }
        }
    }


    class MyServiceReceiver extends ResultReceiver
    {
        public MyServiceReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);

            if(resultCode == 100)
            {
                runOnUiThread(new UpdateUI(resultCode));
            }
        }
    }
}
