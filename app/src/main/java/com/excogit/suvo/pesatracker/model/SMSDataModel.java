package com.excogit.suvo.pesatracker.model;

/**
 * Created by suvo on 3/27/2017.
 */

public class SMSDataModel {

    private String time,amtReceived,amtCharge,amtSent,trxType,trxUser,smsBody,sender,year,month,day;

    public SMSDataModel() {
    }

    public SMSDataModel(String time, String amtReceived, String amtCharge, String amtSent, String trxType, String trxUser, String smsBody, String sender, String year,
                        String month, String day) {
        this.time = time;
        this.amtReceived = amtReceived;
        this.amtCharge = amtCharge;
        this.amtSent = amtSent;
        this.trxType = trxType;
        this.trxUser = trxUser;
        this.smsBody = smsBody;
        this.sender = sender;
        this.year = year;
        this.month = month;
        this.day = day;
    }


    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAmtReceived() {
        return amtReceived;
    }

    public void setAmtReceived(String amtReceived) {
        this.amtReceived = amtReceived;
    }

    public String getAmtCharge() {
        return amtCharge;
    }

    public void setAmtCharge(String amtCharge) {
        this.amtCharge = amtCharge;
    }

    public String getAmtSent() {
        return amtSent;
    }

    public void setAmtSent(String amtSent) {
        this.amtSent = amtSent;
    }

    public String getTrxType() {
        return trxType;
    }

    public void setTrxType(String trxType) {
        this.trxType = trxType;
    }

    public String getTrxUser() {
        return trxUser;
    }

    public void setTrxUser(String trxUser) {
        this.trxUser = trxUser;
    }

    public String getSmsBody() {
        return smsBody;
    }

    public void setSmsBody(String smsBody) {
        this.smsBody = smsBody;
    }
}
