package com.excogit.suvo.pesatracker.view_holders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.excogit.suvo.pesatracker.R;

/**
 * Created by suvo on 3/23/2017.
 */

public class MainPageYearlyParentViewHolder extends ParentViewHolder {

    public ImageButton mDropDownBtn;
    public TextView yearTextView;
    public TextView chargesTextView;
    public TextView receivedTextView;
    public TextView sentTextView;

    public MainPageYearlyParentViewHolder(View itemView)
    {
        super(itemView);

        //mDropDownBtn = (ImageButton) itemView.findViewById(R.id.list_item_year_expand_arrow);
        yearTextView = (TextView) itemView.findViewById(R.id.list_item_year_year_text);
        chargesTextView = (TextView) itemView.findViewById(R.id.list_item_year_charges_text);
        receivedTextView = (TextView) itemView.findViewById(R.id.list_item_year_received_text);
        sentTextView = (TextView) itemView.findViewById(R.id.list_item_year_sent_text);
    }

}
