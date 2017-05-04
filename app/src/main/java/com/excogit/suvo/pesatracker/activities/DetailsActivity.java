package com.excogit.suvo.pesatracker.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.excogit.suvo.pesatracker.R;
import com.excogit.suvo.pesatracker.constants.StringConstants;
import com.excogit.suvo.pesatracker.fragments.MainPageYearlyMonthlyDispFrag;
import com.excogit.suvo.pesatracker.fragments.MonthlyDatewiseFrag;
import com.excogit.suvo.pesatracker.fragments.SummaryFrag;

public class DetailsActivity extends AppCompatActivity {

    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.details_page_toolbar);
        toolbar.setTitle(getString(R.string.details_activity_title));
        toolbar.setLogo(R.mipmap.ic_launcher_round);
        setSupportActionBar(toolbar);


        Intent detailsIntet = getIntent();
        String month = detailsIntet.getStringExtra(StringConstants.MONTH_CONSTANT);
        String year = detailsIntet.getStringExtra(StringConstants.YEAR_CONSTANT);

        Bundle bundle = new Bundle();
        bundle.putString(StringConstants.MONTH_CONSTANT,month);
        bundle.putString(StringConstants.YEAR_CONSTANT,year);

        MonthlyDatewiseFrag datewiseFrag = new MonthlyDatewiseFrag();
        datewiseFrag.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.details_page_frag_container,datewiseFrag).commit();

        Toast.makeText(this,month+" "+year,Toast.LENGTH_SHORT).show();


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.details_page_bottom_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment selectedFragment = null;

                switch (item.getItemId())
                {
                    case R.id.menu_action_summary:
                        Toast.makeText(getApplicationContext(),"Summary clicked",Toast.LENGTH_SHORT).show();
                        selectedFragment = new SummaryFrag();
                        getSupportFragmentManager().beginTransaction().replace(R.id.details_page_frag_container,selectedFragment).commit();
                        break;

                    case R.id.menu_action_refresh:
                        Toast.makeText(getApplicationContext(),"Refresh clicked",Toast.LENGTH_SHORT).show();
                        //fragmentLoad();
                        selectedFragment = new MainPageYearlyMonthlyDispFrag();
                        getSupportFragmentManager().beginTransaction().replace(R.id.details_page_frag_container,selectedFragment).commit();
                        break;

                    /*case R.id.menu_action_search:
                        Toast.makeText(getApplicationContext(),"Search clicked",Toast.LENGTH_SHORT).show();
                        break;
*/
                    case R.id.menu_action_home:
                        selectedFragment = new MainPageYearlyMonthlyDispFrag();
                        getSupportFragmentManager().beginTransaction().replace(R.id.details_page_frag_container,selectedFragment).commit();
                        break;

                }

                return true;
            }
        });

    }


    public void add()
    {
        int a=2+6;
    }
}
