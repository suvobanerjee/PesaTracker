package com.excogit.suvo.pesatracker.view_holders;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.excogit.suvo.pesatracker.R;

/**
 * Created by suvo on 4/6/2017.
 */

public class SummaryChildViewHolder extends ChildViewHolder {

    public TextView trxDate,trxUser,trxAmt;

    public SummaryChildViewHolder(@NonNull View itemView) {
        super(itemView);

        trxDate = (TextView) itemView.findViewById(R.id.list_item_summary_details_time);
        trxUser = (TextView) itemView.findViewById(R.id.list_item_summary_dtls_trx_user);
        trxAmt = (TextView) itemView.findViewById(R.id.list_item_summary_dtls_trx_value);

    }
}
