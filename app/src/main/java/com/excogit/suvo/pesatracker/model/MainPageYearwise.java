package com.excogit.suvo.pesatracker.model;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.List;

/**
 * Created by suvo on 3/23/2017.
 */

public class MainPageYearwise implements Parent<MainPageMonthly> {


    private String year,chargesAmt,receivedAmt,sentAmt;
    private List<MainPageMonthly> mChildList;


    public MainPageYearwise(String year, String chargesAmt, String receivedAmt, String sentAmt, List<MainPageMonthly> mChildList) {
        this.year = year;
        this.chargesAmt = chargesAmt;
        this.receivedAmt = receivedAmt;
        this.sentAmt = sentAmt;
        this.mChildList = mChildList;
    }



    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
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

    public List<MainPageMonthly> getmChildList() {
        return mChildList;
    }

    public void setmChildList(List<MainPageMonthly> mChildList) {
        this.mChildList = mChildList;
    }

    @Override
    public List<MainPageMonthly> getChildList() {
        return mChildList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
