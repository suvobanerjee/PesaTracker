package com.excogit.suvo.pesatracker.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.excogit.suvo.pesatracker.R;
import com.excogit.suvo.pesatracker.adapters.SummaryAdapter;
import com.excogit.suvo.pesatracker.db.DbHandler;

/**
 * Created by suvo on 4/6/2017.
 */

public class SummaryFrag extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        DbHandler dbHandler = new DbHandler(getActivity().getApplicationContext());
        SQLiteDatabase db = dbHandler.getReadableDatabase();


        View v = inflater.inflate(R.layout.summary_frag,container,false);

        RecyclerView summaryRecycleView = (RecyclerView) v.findViewById(R.id.summary_frag_recyclerView);

        SummaryAdapter summaryAdapter = new SummaryAdapter(dbHandler.getSummary(db),getActivity());

        summaryRecycleView.setAdapter(summaryAdapter);
        summaryRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return v;
    }
}
