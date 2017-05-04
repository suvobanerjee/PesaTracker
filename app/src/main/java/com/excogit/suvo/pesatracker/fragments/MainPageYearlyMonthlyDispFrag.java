package com.excogit.suvo.pesatracker.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.excogit.suvo.pesatracker.R;
import com.excogit.suvo.pesatracker.activities.MainPage;
import com.excogit.suvo.pesatracker.adapters.MainPageYearlyParentAdapter;
import com.excogit.suvo.pesatracker.db.DbHandler;
import com.excogit.suvo.pesatracker.model.MainPageMonthly;
import com.excogit.suvo.pesatracker.model.MainPageYearwise;
import com.excogit.suvo.pesatracker.model.ParentListDataModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by suvo on 3/24/2017.
 */

public class MainPageYearlyMonthlyDispFrag extends Fragment {


    RecyclerView mainPageRecyclerView;
    MainPageYearlyParentAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        Log.i("YELO@@@@","INSIDE creae view");
        DbHandler dbHandler = new DbHandler(getActivity().getApplicationContext());

        SQLiteDatabase db = dbHandler.getReadableDatabase();

        View v = inflater.inflate(R.layout.main_page_yearwise_frag,container,false);

        //ParentListDataModel parentListDataModel = new ParentListDataModel();



        mainPageRecyclerView = (RecyclerView) v.findViewById(R.id.mainPage_yearwise_recyclerView);

        //dbHandler.getMonthlyData(db);
        //List<MainPageYearwise> parentDataList = parentListDataModel.getmParentList();
        adapter = new MainPageYearlyParentAdapter(getActivity(),dbHandler.getDataYearlyMonthly(db));

        mainPageRecyclerView.setAdapter(adapter);
        mainPageRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        return v;
    }

}
