package com.excogit.suvo.pesatracker.model;

/**
 * Created by suvo on 3/23/2017.
 */

public class MainPageMonthly {

    private String chargesAmt,receivedAmt,sentAmt,month;

    public MainPageMonthly(String chargesAmt, String receivedAmt, String sentAmt,String month) {
        this.chargesAmt = chargesAmt;
        this.receivedAmt = receivedAmt;
        this.sentAmt = sentAmt;
        this.month = month;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getChargesAmt() {
        return chargesAmt;
    }

    public void setChargesAmt(String chargesAmt) {
        this.chargesAmt = chargesAmt;
    }

    public String getReceivedAmt() {
        return receivedAmt;
    }

    public void setReceivedAmt(String receivedAmt) {
        this.receivedAmt = receivedAmt;
    }

    public String getSentAmt() {
        return sentAmt;
    }

    public void setSentAmt(String sentAmt) {
        this.sentAmt = sentAmt;
    }
}
