package com.excogit.suvo.pesatracker.view_holders;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.excogit.suvo.pesatracker.R;

/**
 * Created by suvo on 4/1/2017.
 */

public class DailyVieHolderChild extends ChildViewHolder {

    public TextView trxTime,trxType,trxTo,trxAmt;

    public DailyVieHolderChild(@NonNull View itemView) {
        super(itemView);

        trxTime = (TextView) itemView.findViewById(R.id.list_item_trxn_dtls_time);
        trxType = (TextView) itemView.findViewById(R.id.list_item_trxn_dtls_type_trx);
        trxTo = (TextView) itemView.findViewById(R.id.list_item_trxn_dtls_to_trx);
        trxAmt = (TextView) itemView.findViewById(R.id.list_item_trxn_dtls_trx_value);

    }
}
