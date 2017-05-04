package com.excogit.suvo.pesatracker.rest;

import com.excogit.suvo.pesatracker.model.RegisterRespModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by suvo on 3/17/2017.
 */

public interface ApiInterface {

    //for registering
    @FormUrlEncoded
   @POST("/user.php")
    //Call<RegisterRespModel> postUserRegister(@Field("phone") String phoneNum, @Field("pin") int pin, @Field("duty") String duty);
    Call<RegisterRespModel> postUserRegister(@Field("phone") String phoneNum, @Field("duty") String duty);


    //for login
    @FormUrlEncoded
    @POST("/in.php")
    Call<RegisterRespModel> postLogin(@Field("phone") String phoneNum, @Field("pin") int pin, @Field("log") String log);


    //for retrive pin
    @FormUrlEncoded
    @POST("/reset.php")
    Call<RegisterRespModel> postRetrivePin(@Field("phone") String phoneNum, @Field("reset") String reset);


    /*//for otp auth
    @FormUrlEncoded
    @POST("/auth.php")
    Call<RegisterRespModel> postOtpAuth(@Field("phone") String phoneNum, @Field("otp") int pin, @Field("auth") String auth);
*/
}
