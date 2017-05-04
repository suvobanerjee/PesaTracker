package com.excogit.suvo.pesatracker.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import com.excogit.suvo.pesatracker.Receivers.SMSReceiver;
import com.excogit.suvo.pesatracker.constants.StringConstants;
import com.excogit.suvo.pesatracker.lsiteners.SMSListener;
import com.excogit.suvo.pesatracker.model.RegisterRespModel;
import com.excogit.suvo.pesatracker.rest.ApiClient;
import com.excogit.suvo.pesatracker.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by suvo on 3/16/2017.
 */

public class RegisterFrag extends Fragment {

    private static final String TAG = "RegisterFrag";
    String phone;//,pin;

    OnRegisterFrag mCallback;

    ProgressDialog progressDialog;

    AppCompatButton registerBtn;

    //TextView otpNotRecivedLink;

    public interface OnRegisterFrag
    {
        void changeFragRegister();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.register_frag,container,false);

        final EditText phoneText = (EditText) v.findViewById(R.id.registerFrag_phone);
       // final EditText pinText = (EditText) v.findViewById(R.id.registerFrag_pin);
        registerBtn = (AppCompatButton) v.findViewById(R.id.registerFrag_registerBtn);
        //otpNotRecivedLink = (TextView) v.findViewById(R.id.registerFrag_resendOtpLink);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerBtn.setEnabled(false);

                progressDialog = new ProgressDialog(v.getContext());
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Registering...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();

                phone = phoneText.getText().toString();
                //pin = pinText.getText().toString();

                register();
            }
        });

        /*otpNotRecivedLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retriveOTP();
            }
        });
*/

        /*SMSReceiver.bindListner(new SMSListener() {
            @Override
            public void SMSReceived(String messageText) {
                Log.d("Text",messageText);

                Toast.makeText(getActivity(),"Message: "+messageText,Toast.LENGTH_LONG).show();
            }
        });
*/

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof LoginFrag.OnLoginFragListener)
        {
            mCallback = (OnRegisterFrag) context;
        }
        else
        {
            throw new ClassCastException(context.toString()
                    + " must implemenet Fragment interface");
        }
    }


  /*  public void retriveOTP()
    {
        Log.i(TAG,"Inside retrieve OTP");
    }
*/
    public void register()
    {
        Log.i(TAG,"Inside regsiter!!!!");
        Log.i(TAG+"Phone@@@@@",String.valueOf(phone));
        //RegisterAsyncTask regAsync = new RegisterAsyncTask();

        // TODO: Implement your own authentication logic here.

        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences(getString(R.string.pref_name), Context.MODE_PRIVATE);
        final SharedPreferences.Editor edit = pref.edit();

        final ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<RegisterRespModel> call = apiInterface.postUserRegister(phone, "reg");//, Integer.parseInt(pin)
        call.enqueue(new Callback<RegisterRespModel>() {
            @Override
            public void onResponse(final Call<RegisterRespModel> call, final Response<RegisterRespModel> response) {
                Log.i(TAG, String.valueOf(response.code()));
                Log.i(TAG, response.body().toString());
                Log.i(TAG, response.body().getStatus());
                Log.i(TAG, response.body().getMessage());
                Log.i(TAG, response.body().getCode());
                Log.i(TAG, response.body().getProgram());
                if (response.code() == 200)
                {
                    if(response.body().getStatus().equals(getString(R.string.resp_constant_success)) &&
                            response.body().getProgram().equals(getString(R.string.resp_constant_program_register)))
                    {

                        edit.putString(getString(R.string.phone_string),phone);
                        //edit.putString(getString(R.string.pin_string),pin);
                        edit.putBoolean(getString(R.string.register_flag),true);
                        edit.putBoolean(StringConstants.PREF_FIRST_INSTAL_FLAG,true);

                        edit.commit();
                        progressDialog.dismiss();
                        mCallback.changeFragRegister();

                        /*SMSReceiver.bindListner(new SMSListener()
                        {
                            @Override
                            public void SMSReceived(String messageText)
                            {
                                Log.d("Text",messageText);

                                Toast.makeText(getActivity(),"Message: "+messageText,Toast.LENGTH_LONG).show();
                                String[] messageSplit=messageText.split(":");
                                String otp = messageSplit[1];
                                Log.i(TAG+"Otp",otp);

                                if(otp.equals(response.body().getOtp()))
                                {
                                    edit.putString(getString(R.string.phone_string),phone);
                                    edit.putString(getString(R.string.pin_string),pin);
                                    edit.putBoolean(getString(R.string.register_flag),true);
                                    edit.putBoolean(StringConstants.PREF_FIRST_INSTAL_FLAG,true);

                                    edit.commit();
                                    progressDialog.dismiss();
                                    mCallback.changeFragRegister();
                                }
                                else
                                {
                                    progressDialog.dismiss();
                                    registerBtn.setEnabled(true);
                                    Toast.makeText(getActivity(),getString(R.string.universal_failure_msg),Toast.LENGTH_SHORT).show();
                                }

                                //calling otpauth
                                *//*Call<RegisterRespModel> authCall = apiInterface.postOtpAuth(phone,Integer.parseInt(otp),getString(R.string.resp_constant_auth_otp));
                                authCall.enqueue(new Callback<RegisterRespModel>()
                                {
                                    @Override
                                    public void onResponse(Call<RegisterRespModel> call, Response<RegisterRespModel> response)
                                    {
                                        Log.i(TAG, String.valueOf(response.code()));
                                        Log.i(TAG, response.body().toString());
                                        Log.i(TAG, response.body().getStatus());
                                        Log.i(TAG, response.body().getMessage());
                                        Log.i(TAG, response.body().getCode());
                                        Log.i(TAG, response.body().getProgram());

                                        if (response.code() == 200)
                                        {
                                            if (response.body().getStatus().equals(getString(R.string.resp_constant_success)))
                                            {
                                                edit.putString(getString(R.string.phone_string),phone);
                                                edit.putString(getString(R.string.pin_string),pin);
                                                edit.putBoolean(getString(R.string.register_flag),true);
                                                edit.putBoolean(StringConstants.PREF_FIRST_INSTAL_FLAG,true);

                                                edit.commit();
                                                progressDialog.dismiss();
                                                mCallback.changeFragRegister();
                                            }
                                            else
                                            {
                                                progressDialog.dismiss();
                                                registerBtn.setEnabled(true);
                                                Toast.makeText(getActivity(),getString(R.string.universal_failure_msg),Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else
                                        {
                                            progressDialog.dismiss();
                                            registerBtn.setEnabled(true);
                                            Toast.makeText(getActivity(),getString(R.string.universal_failure_msg),Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<RegisterRespModel> call, Throwable t) {
                                        progressDialog.dismiss();
                                        registerBtn.setEnabled(true);
                                        Toast.makeText(getActivity(),getString(R.string.universal_failure_msg),Toast.LENGTH_SHORT).show();
                                    }
                                });*//*
                            }
                        });*/

                        /*final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.i(TAG,"Inside Timer");
                                Log.i(TAG,"Closing Dialog");

                                progressDialog.dismiss();
                                registerBtn.setEnabled(true);
                                otpNotRecivedLink.setVisibility(View.VISIBLE);

                                Toast.makeText(getActivity(),getString(R.string.otp_not_recived_msg),Toast.LENGTH_LONG).show();

                                call.cancel();

                            }
                        },200000);*/

                    }
                    else if (response.body().getMessage().equals(StringConstants.REG_FAILURE_MSG_USER_EXIST) &&
                            response.body().getProgram().equals(getString(R.string.resp_constant_program_register)))
                    {
                        progressDialog.dismiss();
                        registerBtn.setEnabled(true);
                        Toast.makeText(getActivity(),getString(R.string.reg_failure_msg_user_exists_string),Toast.LENGTH_SHORT).show();

                    }
                    else if (response.body().getMessage().equals(StringConstants.REG_FAILURE_MSG_INVALID_PARAM) &&
                            response.body().getProgram().equals(getString(R.string.resp_constant_program_register)))
                    {
                        progressDialog.dismiss();
                        registerBtn.setEnabled(true);
                        Toast.makeText(getActivity(),getString(R.string.reg_failure_msg_invalid_param),Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        progressDialog.dismiss();
                        registerBtn.setEnabled(true);
                        Toast.makeText(getActivity(),getString(R.string.universal_failure_msg),Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterRespModel> call, Throwable t) {
                Log.e(TAG ,"Failure ###");
                t.printStackTrace();

                progressDialog.dismiss();
                registerBtn.setEnabled(true);
                Toast.makeText(getActivity(),getString(R.string.universal_failure_msg),Toast.LENGTH_SHORT).show();
            }
        });

/*

        edit.putString(getString(R.string.phone_string),phone);
        edit.putString(getString(R.string.pin_string),pin);
        edit.putBoolean(getString(R.string.register_flag),true);

        edit.commit();
*/



        //regAsync.execute();

    }

   /* private class RegisterAsyncTask extends AsyncTask<Void,String,String>
    {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        String resp;


        @Override
        protected void onPreExecute() {

            progressDialog.setMessage("Registering...");
            progressDialog.show();

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            publishProgress("Registering..."); // Calls onProgressUpdate()
            SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences(getString(R.string.pref_name), Context.MODE_PRIVATE);
            final SharedPreferences.Editor edit = pref.edit();


            //here the login logic will be written
           final ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            Call<RegisterRespModel> call = apiInterface.postUserRegister(phone, Integer.parseInt(pin), getString(R.string.register_duty));
            call.enqueue(new Callback<RegisterRespModel>() {
                @Override
                public void onResponse(Call<RegisterRespModel> call, Response<RegisterRespModel> response) {
                    Log.i(TAG, String.valueOf(response.code()));
                    Log.i(TAG, response.body().toString());
                    Log.i(TAG, response.body().getStatus());
                    Log.i(TAG, response.body().getMessage());
                    Log.i(TAG, response.body().getCode());
                    Log.i(TAG, response.body().getProgram());

                    *//*if (response.code() == 200)
                    {
                        if(response.body().getStatus().equals(getString(R.string.resp_constant_success)) &&
                                response.body().getProgram().equals(getString(R.string.resp_constant_program_register)))
                        {
                            SMSReceiver.bindListner(new SMSListener() {
                                @Override
                                public void SMSReceived(String messageText) {
                                    Log.d("Text",messageText);

                                    Toast.makeText(getActivity(),"Message: "+messageText,Toast.LENGTH_LONG).show();
                                    String[] messageSplit=messageText.split(":");
                                    String otp = messageSplit[1];
                                    Log.i(TAG+"Otp",otp);

                                    //calling otpauth
                                    Call<RegisterRespModel> authCall = apiInterface.postOtpAuth(phone,Integer.parseInt(otp),getString(R.string.resp_constant_auth_otp));
                                    authCall.enqueue(new Callback<RegisterRespModel>() {
                                        @Override
                                        public void onResponse(Call<RegisterRespModel> call, Response<RegisterRespModel> response) {
                                            Log.i(TAG, String.valueOf(response.code()));
                                            Log.i(TAG, response.body().toString());
                                            Log.i(TAG, response.body().getStatus());
                                            Log.i(TAG, response.body().getMessage());
                                            Log.i(TAG, response.body().getCode());
                                            Log.i(TAG, response.body().getProgram());

                                            if (response.code() == 200) {
                                                if (response.body().getStatus().equals(getString(R.string.resp_constant_success)))
                                                {
                                                    edit.putString(getString(R.string.phone_string),phone);
                                                    edit.putString(getString(R.string.pin_string),pin);
                                                    edit.putBoolean(getString(R.string.register_flag),true);

                                                    edit.commit();
                                                    resp = "success";
                                                }
                                                else
                                                {
                                                    resp = "fail";
                                                }
                                            }
                                            else
                                            {
                                                resp = "fail";
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<RegisterRespModel> call, Throwable t) {

                                        }
                                    });
                                }
                            });

                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterRespModel> call, Throwable t) {
                    Log.e(TAG ,"Failure ###");
                    t.printStackTrace();
                }
            });
*//*

            return resp;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            *//*Intent i = new Intent(getActivity(), MainPage.class);
            startActivity(i);*//*

          //  Log.i(TAG+"onpost",s);
            mCallback.changeFragRegister();
            super.onPostExecute(s);
        }



    }
*/
}
