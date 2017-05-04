package com.excogit.suvo.pesatracker.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.excogit.suvo.pesatracker.R;
import com.excogit.suvo.pesatracker.model.SummaryChildModel;
import com.excogit.suvo.pesatracker.model.SummaryParentModel;
import com.excogit.suvo.pesatracker.view_holders.SummaryChildViewHolder;
import com.excogit.suvo.pesatracker.view_holders.SummaryParentViewHolder;

import java.util.List;

/**
 * Created by suvo on 4/6/2017.
 */

public class SummaryAdapter extends ExpandableRecyclerAdapter<SummaryParentModel,SummaryChildModel,SummaryParentViewHolder,SummaryChildViewHolder> {

    private LayoutInflater mInflater;
    private Context mContext;


    public SummaryAdapter(@NonNull List<SummaryParentModel> parentList,Context context) {
        super(parentList);

        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public SummaryParentViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {

        View summaryParentView = mInflater.inflate(R.layout.list_item_summary_parent,parentViewGroup,false);

        return new SummaryParentViewHolder(summaryParentView);
    }

    @NonNull
    @Override
    public SummaryChildViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {

        View summaryChildView = mInflater.inflate(R.layout.list_item_summary_details_child,childViewGroup,false);

        return new SummaryChildViewHolder(summaryChildView);
    }

    @Override
    public void onBindParentViewHolder(@NonNull SummaryParentViewHolder parentViewHolder, int parentPosition, @NonNull SummaryParentModel parent) {

        parentViewHolder.trxType.setText(parent.getSummaryTrxType());
        parentViewHolder.trxAmt.setText(parent.getSummaryTrxAmt());
    }

    @Override
    public void onBindChildViewHolder(@NonNull SummaryChildViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull SummaryChildModel child) {

        Log.i("Summary###",child.getTrxAmt());

        childViewHolder.trxDate.setText(child.getDate());
        childViewHolder.trxUser.setText(child.getTrxUser());
        childViewHolder.trxAmt.setText(child.getTrxAmt());
    }
}
