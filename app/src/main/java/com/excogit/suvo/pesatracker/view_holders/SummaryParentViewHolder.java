package com.excogit.suvo.pesatracker.view_holders;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.excogit.suvo.pesatracker.R;

/**
 * Created by suvo on 4/6/2017.
 */

public class SummaryParentViewHolder extends ParentViewHolder {


    public TextView trxType,trxAmt;

    public SummaryParentViewHolder(@NonNull View itemView) {
        super(itemView);

        trxType = (TextView) itemView.findViewById(R.id.list_item_summary_trx_type);
        trxAmt = (TextView) itemView.findViewById(R.id.list_item_summary_trx_amt);

    }
}
