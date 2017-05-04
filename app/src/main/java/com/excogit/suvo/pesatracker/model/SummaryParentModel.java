package com.excogit.suvo.pesatracker.model;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.List;

/**
 * Created by suvo on 4/6/2017.
 */

public class SummaryParentModel implements Parent<SummaryChildModel> {

    String summaryTrxType,summaryTrxAmt;

    private List<SummaryChildModel> mChildList;

    public SummaryParentModel(String summaryTrxType, String summaryTrxAmt, List<SummaryChildModel> mChildList) {
        this.summaryTrxType = summaryTrxType;
        this.summaryTrxAmt = summaryTrxAmt;
        this.mChildList = mChildList;
    }

    public String getSummaryTrxType() {
        return summaryTrxType;
    }

    public void setSummaryTrxType(String summaryTrxType) {
        this.summaryTrxType = summaryTrxType;
    }

    public String getSummaryTrxAmt() {
        return summaryTrxAmt;
    }

    public void setSummaryTrxAmt(String summaryTrxAmt) {
        this.summaryTrxAmt = summaryTrxAmt;
    }

    @Override
    public List<SummaryChildModel> getChildList() {
        return mChildList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
