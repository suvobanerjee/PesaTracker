package com.excogit.suvo.pesatracker.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.excogit.suvo.pesatracker.R;
import com.excogit.suvo.pesatracker.activities.MainPage;
import com.excogit.suvo.pesatracker.constants.StringConstants;
import com.excogit.suvo.pesatracker.model.RegisterRespModel;
import com.excogit.suvo.pesatracker.rest.ApiClient;
import com.excogit.suvo.pesatracker.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by suvo on 3/16/2017.
 */

public class LoginFrag extends Fragment {

    OnLoginFragListener mCallback;

    private String phoneNum;

    private TextView phoneTexView,newRegistrationLink,fogotPinLink;
    private AppCompatButton logInBtn;
    private EditText pinText;

    private String pin = null;

    public interface OnLoginFragListener
    {
        void changeFrag();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login_frag,container,false);
        phoneTexView = (TextView) view.findViewById(R.id.login_phone);
        newRegistrationLink = (TextView) view.findViewById(R.id.link_signup);
        logInBtn = (AppCompatButton) view.findViewById(R.id.btn_login);
        pinText = (EditText) view.findViewById(R.id.login_pin);
        fogotPinLink = (TextView) view.findViewById(R.id.loginpage_forgetPinLink);




        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences(getString(R.string.pref_name),Context.MODE_PRIVATE);
        Boolean registeredFlag = pref.getBoolean(getString(R.string.register_flag),false);

        if(registeredFlag)
        {
            phoneNum = pref.getString(getString(R.string.phone_string),null);
            phoneTexView.setText(phoneNum);
            newRegistrationLink.setVisibility(View.INVISIBLE);
        }

        newRegistrationLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.changeFrag();
            }
        });



        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // LoginAsyncTask logInAsync = new LoginAsyncTask();
                pin = pinText.getText().toString();
                logInBtn.setEnabled(false);
                login(pin);
                //logInAsync.execute(pin);

            }
        });


        fogotPinLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPin();
            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof  OnLoginFragListener)
        {
            mCallback = (OnLoginFragListener) context;
        }
        else
        {
            throw new ClassCastException(context.toString()
                    + " must implemenet Fragment interface");
        }
    }



    public void login(String pin)
    {
        Log.i("LoginFrag",pin);
        Log.i("LoginFrag",phoneNum);
        //calling the webservice /in.php
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<RegisterRespModel> call = apiInterface.postLogin(phoneNum,Integer.parseInt(pin),getString(R.string.login_constant));
        call.enqueue(new Callback<RegisterRespModel>() {
            @Override
            public void onResponse(Call<RegisterRespModel> call, Response<RegisterRespModel> response) {

                Log.i("LoginFragCode",String.valueOf(response.code()));
                Log.i("LoginFragBody",response.body().toString());
                Log.i("LoginFragBody",response.body().getStatus());
                Log.i("LoginFragBody",response.body().getMessage());
                Log.i("LoginFragBody",response.body().getCode());
                Log.i("LoginFragBody",response.body().getProgram());

                if(response.code() == 200)
                {
                    if(response.body().getStatus().equals(getString(R.string.resp_constant_success)) &&
                            response.body().getProgram().equals(getString(R.string.resp_constant_program_login)))
                    {
                        Intent loginIntent = new Intent(getActivity(),MainPage.class);
                        startActivity(loginIntent);
                    }
                    else if (response.body().getStatus().equals(StringConstants.LOGIN_FAILURE_STATUS_FAIL) &&
                            response.body().getMessage().equals(StringConstants.LOGIN_FAILURE_WRONG_PIN))
                    {
                        Toast.makeText(getActivity(),getString(R.string.login_failure_wrong_pin_msg),Toast.LENGTH_SHORT).show();
                        logInBtn.setEnabled(true);
                    }
                    else
                    {
                        Toast.makeText(getActivity(),getString(R.string.login_failure_msg),Toast.LENGTH_SHORT).show();
                        logInBtn.setEnabled(true);
                    }
                }

            }

            @Override
            public void onFailure(Call<RegisterRespModel> call, Throwable t) {

                Log.e("LoginFrag" ,"Failure ###");
                t.printStackTrace();

                Toast.makeText(getActivity(),getString(R.string.universal_failure_msg),Toast.LENGTH_SHORT).show();
                logInBtn.setEnabled(true);
            }
        });

    }

    public void forgotPin()
    {
        //call /reset.php
        Log.i("LoginFragForgot",phoneNum);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<RegisterRespModel> call = apiInterface.postRetrivePin(phoneNum,getString(R.string.resp_constant_retrivepin));
        call.enqueue(new Callback<RegisterRespModel>() {
            @Override
            public void onResponse(Call<RegisterRespModel> call, Response<RegisterRespModel> response) {

                Log.i("LoginFragForgot",String.valueOf(response.code()));
                Log.i("LoginFragForgot",response.body().toString());
                Log.i("LoginFragForgot",response.body().getStatus());
                Log.i("LoginFragForgot",response.body().getMessage());
                Log.i("LoginFragForgot",response.body().getCode());
                Log.i("LoginFragForgot",response.body().getProgram());

                if(response.code() == 200)
                {
                    if(response.body().getStatus().equals(getString(R.string.resp_constant_success)) &&
                            response.body().getMessage().equals(StringConstants.RETRIEVE_PIN_SUCCESS_CONSTANT))
                    {
                        Toast.makeText(getActivity(),getString(R.string.retrive_pin_success_msg),Toast.LENGTH_LONG).show();
                    }
                    else if (response.body().getStatus().equals(StringConstants.RETRIEVE_PIN_FAILURE_STATUS_FAIL) &&
                            response.body().getMessage().equals(StringConstants.RETRIEVE_PIN_FAILURE_MSG))
                    {
                        Toast.makeText(getActivity(),getString(R.string.resp_constant_resend_pin_user_not_exist),Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getActivity(),getString(R.string.universal_failure_msg),Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(),getString(R.string.universal_failure_msg),Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<RegisterRespModel> call, Throwable t) {

            }
        });


    }


    private class LoginAsyncTask extends AsyncTask<String,String,String>
    {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        String resp;


        @Override
        protected String doInBackground(String... params) {

            int pinInt = Integer.parseInt(pin);
            Log.i("LoginFrag",phoneNum);
            Log.i("LoginFrag",String.valueOf(pin));

            //calling the webservice /in.php
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<RegisterRespModel> call = apiInterface.postLogin(phoneNum,pinInt,getString(R.string.login_constant));
            call.enqueue(new Callback<RegisterRespModel>() {
                @Override
                public void onResponse(Call<RegisterRespModel> call, Response<RegisterRespModel> response) {

                    Log.i("LoginFrag",String.valueOf(response.code()));
                    Log.i("LoginFrag",response.body().toString());

                }

                @Override
                public void onFailure(Call<RegisterRespModel> call, Throwable t) {

                    Log.e("LoginFrag" ,"Failure ###");
                    t.printStackTrace();
                }
            });


            return null;
        }

        @Override
        protected void onPreExecute() {

            progressDialog.setMessage("Registering...");
            progressDialog.show();

            super.onPreExecute();
        }




        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            Intent i = new Intent(getActivity(), MainPage.class);
            startActivity(i);
            super.onPostExecute(s);
        }
    }

}
