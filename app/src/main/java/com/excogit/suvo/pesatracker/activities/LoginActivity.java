package com.excogit.suvo.pesatracker.activities;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;


import com.excogit.suvo.pesatracker.R;
import com.excogit.suvo.pesatracker.fragments.LoginFrag;
import com.excogit.suvo.pesatracker.fragments.RegisterFrag;


public class LoginActivity extends AppCompatActivity  implements LoginFrag.OnLoginFragListener, RegisterFrag.OnRegisterFrag {


    private static  int REQ_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        SharedPreferences pref = getSharedPreferences(getString(R.string.pref_name),MODE_PRIVATE);
        boolean isRegistred = pref.getBoolean(getString(R.string.register_flag),false);


        if (findViewById(R.id.loginPage_fragContainer) != null)
        {
            if(savedInstanceState != null)
            {
                return;
            }

            if(isRegistred) {
                LoginFrag loginFrag = new LoginFrag();

                getSupportFragmentManager().beginTransaction().add(R.id.loginPage_fragContainer, loginFrag).commit();
            }
            else
            {
                RegisterFrag registerFrag = new RegisterFrag();
                getSupportFragmentManager().beginTransaction().add(R.id.loginPage_fragContainer,registerFrag).commit();
            }
        }


        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_SMS)
            != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_SMS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS},REQ_CODE);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        if(requestCode == REQ_CODE)
        {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_LONG).show();

            }
            else
            {
                Toast.makeText(this,"Permission NOT Granted",Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void changeFrag() {

            getSupportFragmentManager().beginTransaction().replace(R.id.loginPage_fragContainer,new RegisterFrag())
                    .addToBackStack(null)
                    .commit();
    }

    @Override
    public void changeFragRegister()
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.loginPage_fragContainer,new LoginFrag())
                .addToBackStack(null)
                .commit();
    }


    /*@Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }*/
}

