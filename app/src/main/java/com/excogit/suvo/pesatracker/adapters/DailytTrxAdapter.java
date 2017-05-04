package com.excogit.suvo.pesatracker.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.excogit.suvo.pesatracker.R;
import com.excogit.suvo.pesatracker.model.DailyDataChild;
import com.excogit.suvo.pesatracker.model.DayWiseDataParent;
import com.excogit.suvo.pesatracker.view_holders.DailyVieHolderChild;
import com.excogit.suvo.pesatracker.view_holders.DayWiseDataViewHolderParent;

import java.util.List;

/**
 * Created by suvo on 4/1/2017.
 */

public class DailytTrxAdapter extends ExpandableRecyclerAdapter<DayWiseDataParent,DailyDataChild,DayWiseDataViewHolderParent,DailyVieHolderChild> {



    private LayoutInflater mInflater;
    private Context mContext;

    public DailytTrxAdapter(Context context, @NonNull List<DayWiseDataParent> parentList) {
        super(parentList);

        Log.i("Yello@@!Listlen",String.valueOf(parentList.size()));
        mContext = context;
        mInflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public DayWiseDataViewHolderParent onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {

        View dayWiseView = mInflater.inflate(R.layout.list_item_day,parentViewGroup,false);

        return new DayWiseDataViewHolderParent(dayWiseView);
    }


    @Override
    public DailyVieHolderChild onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {

        View dailyView = mInflater.inflate(R.layout.list_item_trxn_dtls,childViewGroup,false);

        return new DailyVieHolderChild(dailyView);
    }

    @Override
    public void onBindParentViewHolder(@NonNull DayWiseDataViewHolderParent parentViewHolder, int parentPosition, @NonNull DayWiseDataParent parent) {

        Log.i("Yello#$",parent.getDay());
        parentViewHolder.dayTextView.setText(parent.getDay());
        parentViewHolder.chargesTextView.setText(parent.getTotCharge());
        parentViewHolder.sentTextView.setText(parent.getTotSent());
        parentViewHolder.receivedTextView.setText(parent.getTotReceived());
    }

    @Override
    public void onBindChildViewHolder(@NonNull DailyVieHolderChild childViewHolder, int parentPosition, int childPosition, @NonNull DailyDataChild child) {

        childViewHolder.trxTime.setText(child.getTime());
        childViewHolder.trxType.setText(child.getTypeOfTrx());
        childViewHolder.trxTo.setText(child.getTrxToPerson());
        childViewHolder.trxAmt.setText(child.getValueofTrxn());

    }
}
