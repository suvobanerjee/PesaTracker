package com.excogit.suvo.pesatracker.model;

/**
 * Created by suvo on 4/6/2017.
 */

public class SummaryChildModel {

    String date,trxUser,trxAmt;

    public SummaryChildModel(String date, String trxUser, String trxAmt) {
        this.date = date;
        this.trxUser = trxUser;
        this.trxAmt = trxAmt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTrxUser() {
        return trxUser;
    }

    public void setTrxUser(String trxUser) {
        this.trxUser = trxUser;
    }

    public String getTrxAmt() {
        return trxAmt;
    }

    public void setTrxAmt(String trxAmt) {
        this.trxAmt = trxAmt;
    }
}
