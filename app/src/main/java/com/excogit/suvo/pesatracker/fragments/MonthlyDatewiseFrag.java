package com.excogit.suvo.pesatracker.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.excogit.suvo.pesatracker.R;
import com.excogit.suvo.pesatracker.adapters.DailytTrxAdapter;
import com.excogit.suvo.pesatracker.constants.StringConstants;
import com.excogit.suvo.pesatracker.db.DbHandler;
import com.excogit.suvo.pesatracker.model.DailyDataChild;
import com.excogit.suvo.pesatracker.model.DayWiseDataParent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suvo on 4/1/2017.
 */

public class MonthlyDatewiseFrag extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        //dummy data
        DailyDataChild dailyDataChildObj = new DailyDataChild("2:30PM","Sent","abcd","23000");
        List<DailyDataChild> dailyDataChildList = new ArrayList<>();
        dailyDataChildList.add(dailyDataChildObj);

        dailyDataChildObj = new DailyDataChild("1:30PM","Received","XYZ","34000");
        dailyDataChildList.add(dailyDataChildObj);



        DayWiseDataParent dayWiseDataParentObj = new DayWiseDataParent("2","12000","3000","45000", dailyDataChildList);
        List<DayWiseDataParent> dayWiseDataParentList = new ArrayList<>();


        dayWiseDataParentList.add(dayWiseDataParentObj);

        String year=getArguments().getString(StringConstants.YEAR_CONSTANT);
        String month=getArguments().getString(StringConstants.MONTH_CONSTANT);


        Log.i("YEllo@@@AdapY",year);
        Log.i("Yello@@Adap",month);


        View v = inflater.inflate(R.layout.details_page_frag,container,false);

       /* TextView detailsFragTextview = (TextView) v.findViewById(R.id.details_page_textview);
        detailsFragTextview.setText(month+" "+year);
*/

        Context ctx = getActivity().getApplicationContext();
        DbHandler dbHandler = new DbHandler(ctx);

        SQLiteDatabase db = dbHandler.getReadableDatabase();

        RecyclerView detailsFragRecyclerView = (RecyclerView) v.findViewById(R.id.detailsPage_recyclerView);
        Log.i("Yello@@!!#Listlen",String.valueOf(dayWiseDataParentList.size()));
        DailytTrxAdapter adapter = new DailytTrxAdapter(ctx,dbHandler.getDailyData(month,year,db));

        //DailytTrxAdapter adapter = new DailytTrxAdapter(ctx, dayWiseDataParentList);

        detailsFragRecyclerView.setAdapter(adapter);
        detailsFragRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        return v;
    }
}
