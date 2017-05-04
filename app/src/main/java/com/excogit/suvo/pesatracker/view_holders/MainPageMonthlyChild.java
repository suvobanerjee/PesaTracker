package com.excogit.suvo.pesatracker.view_holders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.excogit.suvo.pesatracker.R;

/**
 * Created by suvo on 3/23/2017.
 */

public class MainPageMonthlyChild extends ChildViewHolder {

    public ImageButton monthlyDropDownBtn;
    public TextView monthTextView, monthlychargesTextView, monthlyreceivedTextView, monthlysentTextView;

    public MainPageMonthlyChild(View itemView)
    {
        super(itemView);

        //monthlyDropDownBtn = (ImageButton) itemView.findViewById(R.id.list_item_month_expand_arrow);
        monthTextView = (TextView) itemView.findViewById(R.id.list_item_month_month_text);
        monthlychargesTextView = (TextView) itemView.findViewById(R.id.list_item_month_charges_text);
        monthlyreceivedTextView = (TextView) itemView.findViewById(R.id.list_item_month_received_text);
        monthlysentTextView = (TextView) itemView.findViewById(R.id.list_item_month_sent_text);

    }
}
