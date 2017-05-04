package com.excogit.suvo.pesatracker.model;

import com.google.gson.annotations.SerializedName;

import java.io.StringReader;

/**
 * Created by suvo on 3/17/2017.
 */

public class RegisterRespModel {

    @SerializedName("program")
    private String program;

    @SerializedName("status")
    private String status;

    @SerializedName("code")
    private String code;

    @SerializedName("message")
    private String message;

    @SerializedName("otp")
    private String otp;

    public RegisterRespModel(String program, String status, String code, String message,String otp) {
        this.program = program;
        this.status = status;
        this.code = code;
        this.message = message;
        this.otp = otp;
    }


    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
