package com.excogit.suvo.pesatracker.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.bignerdranch.expandablerecyclerview.model.Parent;
import com.excogit.suvo.pesatracker.R;
import com.excogit.suvo.pesatracker.activities.DetailsActivity;
import com.excogit.suvo.pesatracker.constants.StringConstants;
import com.excogit.suvo.pesatracker.model.MainPageMonthly;
import com.excogit.suvo.pesatracker.model.MainPageYearwise;
import com.excogit.suvo.pesatracker.view_holders.MainPageMonthlyChild;
import com.excogit.suvo.pesatracker.view_holders.MainPageYearlyParentViewHolder;

import java.util.List;

/**
 * Created by suvo on 3/23/2017.
 */

public class MainPageYearlyParentAdapter extends ExpandableRecyclerAdapter<MainPageYearwise, MainPageMonthly, MainPageYearlyParentViewHolder, MainPageMonthlyChild>
{


    private LayoutInflater mInflater;
    private Context mContext;
    private List<MainPageYearwise> parentList;

    public MainPageYearlyParentAdapter(Context context, @NonNull List<MainPageYearwise> parentList) {
        super(parentList);

        this.parentList = parentList;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MainPageYearlyParentViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {

        View yearWiseView = mInflater.inflate(R.layout.list_item_year,parentViewGroup,false);

        return new MainPageYearlyParentViewHolder(yearWiseView);
    }


    @Override
    public MainPageMonthlyChild onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {

        View monthlyView = mInflater.inflate(R.layout.list_item_month,childViewGroup,false);

        return new MainPageMonthlyChild(monthlyView);
    }

    @Override
    public void onBindParentViewHolder(@NonNull MainPageYearlyParentViewHolder parentViewHolder, int parentPosition, @NonNull MainPageYearwise parent) {

        MainPageYearwise yearwise = parent;
        parentViewHolder.yearTextView.setText(yearwise.getYear());
        parentViewHolder.chargesTextView.setText(yearwise.getChargesAmt());
        parentViewHolder.sentTextView.setText(yearwise.getSentAmt());
        parentViewHolder.receivedTextView.setText(yearwise.getReceivedAmt());

    }

    @Override
    public void onBindChildViewHolder(@NonNull MainPageMonthlyChild childViewHolder, final int parentPosition, final int childPosition, @NonNull final MainPageMonthly child) {

        MainPageMonthly monthly = child;

        childViewHolder.monthTextView.setText(monthly.getMonth());
        childViewHolder.monthlychargesTextView.setText(monthly.getChargesAmt());
        childViewHolder.monthlysentTextView.setText(monthly.getSentAmt());
        childViewHolder.monthlyreceivedTextView.setText(monthly.getReceivedAmt());

        childViewHolder.monthTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext,child.getMonth()+" "+parentList.get(parentPosition).getYear(),Toast.LENGTH_SHORT).show();
                activityStarter(child.getMonth(),parentList.get(parentPosition).getYear());
            }
        });

    }


    private void activityStarter(String month,String year)
    {
        Intent childIntetnt = new Intent(mContext, DetailsActivity.class);
        childIntetnt.putExtra(StringConstants.YEAR_CONSTANT,year);
        childIntetnt.putExtra(StringConstants.MONTH_CONSTANT,month);
        mContext.startActivity(childIntetnt);
    }
}
