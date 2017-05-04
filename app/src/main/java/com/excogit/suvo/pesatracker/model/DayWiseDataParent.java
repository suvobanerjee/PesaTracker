package com.excogit.suvo.pesatracker.model;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.List;

/**
 * Created by suvo on 4/1/2017.
 */

public class DayWiseDataParent implements Parent<DailyDataChild>{


    String day,totCharge,totSent,totReceived;

    private List<DailyDataChild> mChildList;

    public DayWiseDataParent(String day, String totCharge, String totSent, String totReceived, List<DailyDataChild> mChildList) {
        this.day = day;
        this.totCharge = totCharge;
        this.totSent = totSent;
        this.totReceived = totReceived;
        this.mChildList = mChildList;
    }


    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTotCharge() {
        return totCharge;
    }

    public void setTotCharge(String totCharge) {
        this.totCharge = totCharge;
    }

    public String getTotSent() {
        return totSent;
    }

    public void setTotSent(String totSent) {
        this.totSent = totSent;
    }

    public String getTotReceived() {
        return totReceived;
    }

    public void setTotReceived(String totReceived) {
        this.totReceived = totReceived;
    }

    public List<DailyDataChild> getmChildList() {
        return mChildList;
    }

    public void setmChildList(List<DailyDataChild> mChildList) {
        this.mChildList = mChildList;
    }

    @Override
    public List<DailyDataChild> getChildList() {
        return mChildList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
