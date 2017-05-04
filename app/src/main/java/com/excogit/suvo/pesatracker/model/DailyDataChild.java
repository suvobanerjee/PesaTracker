package com.excogit.suvo.pesatracker.model;

/**
 * Created by suvo on 4/1/2017.
 */

public class DailyDataChild {

    String time,typeOfTrx,trxToPerson,valueofTrxn;


    public DailyDataChild(String time, String typeOfTrx, String trxToPerson, String valueofTrxn) {
        this.time = time;
        this.typeOfTrx = typeOfTrx;
        this.trxToPerson = trxToPerson;
        this.valueofTrxn = valueofTrxn;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTypeOfTrx() {
        return typeOfTrx;
    }

    public void setTypeOfTrx(String typeOfTrx) {
        this.typeOfTrx = typeOfTrx;
    }

    public String getTrxToPerson() {
        return trxToPerson;
    }

    public void setTrxToPerson(String trxToPerson) {
        this.trxToPerson = trxToPerson;
    }

    public String getValueofTrxn() {
        return valueofTrxn;
    }

    public void setValueofTrxn(String valueofTrxn) {
        this.valueofTrxn = valueofTrxn;
    }
}
