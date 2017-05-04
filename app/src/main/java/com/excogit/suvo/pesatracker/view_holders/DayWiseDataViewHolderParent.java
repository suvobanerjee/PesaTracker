package com.excogit.suvo.pesatracker.view_holders;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.excogit.suvo.pesatracker.R;

/**
 * Created by suvo on 4/1/2017.
 */

public class DayWiseDataViewHolderParent extends ParentViewHolder {

    public TextView dayTextView;
    public TextView chargesTextView;
    public TextView receivedTextView;
    public TextView sentTextView;



    public DayWiseDataViewHolderParent(@NonNull View itemView) {
        super(itemView);

        dayTextView = (TextView) itemView.findViewById(R.id.list_item_day_day_text);
        chargesTextView = (TextView) itemView.findViewById(R.id.list_item_day_charges_text);
        receivedTextView = (TextView) itemView.findViewById(R.id.list_item_day_received_text);
        sentTextView = (TextView) itemView.findViewById(R.id.list_item_day_sent_text);
    }
}
