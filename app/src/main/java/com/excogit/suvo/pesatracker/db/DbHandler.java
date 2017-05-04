package com.excogit.suvo.pesatracker.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.excogit.suvo.pesatracker.model.DailyDataChild;
import com.excogit.suvo.pesatracker.model.DayWiseDataParent;
import com.excogit.suvo.pesatracker.model.MainPageMonthly;
import com.excogit.suvo.pesatracker.model.MainPageYearwise;
import com.excogit.suvo.pesatracker.model.ParentListDataModel;
import com.excogit.suvo.pesatracker.model.SMSDataModel;
import com.excogit.suvo.pesatracker.model.SummaryChildModel;
import com.excogit.suvo.pesatracker.model.SummaryParentModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suvo on 3/27/2017.
 */

public class DbHandler extends SQLiteOpenHelper{

    //version number
    private static final int DB_VERSION = 1;

    //db name
    private static final String DB_NAME = "pesaTracker";

    //table name
    private static final String TABLE_NAME = "pesaData";

    //column names
    private static final String COL_ID = "id";
    private static final String COL_TIME = "trx_time";
    private static final String COL_RECEIVED_AMT = "received_amt";
    private static final String COL_CHARGE_AMT = "charge_amt";
    private static final String COL_SENT_AMT = "sent_amt";
    private static final String COL_TRX_TYPE = "trx_type";
    private static final String COL_TRX_USER = "trx_user";
    private static final String COL_SMS_BODY = "sms_body";
    private static final String COL_SENDER = "sender";
    private static final String COL_YEAR = "year";
    private static final String COL_MONTH = "month";
    private static final String COL_DAY = "day";

    public DbHandler(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_PESA_TABLE = "create table "+TABLE_NAME+" ("
                +COL_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+COL_SMS_BODY+ " TEXT, "
                +COL_SENDER+ " TEXT, "+ COL_TIME +" TEXT, "
                +COL_YEAR+ " TEXT, "+ COL_MONTH +" TEXT, "+COL_DAY+" TEXT,"
                +COL_CHARGE_AMT+ " TEXT, "+COL_SENT_AMT+" TEXT, "
                +COL_RECEIVED_AMT+ " TEXT, "+COL_TRX_TYPE+" TEXT, "+COL_TRX_USER+" TEXT)";

        db.execSQL(CREATE_PESA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    //Required operations on db

    //Inserting Data
    public void insertData(SMSDataModel smsModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COL_SMS_BODY,smsModel.getSmsBody());
        cv.put(COL_SENDER,smsModel.getSender());
        cv.put(COL_TIME,smsModel.getTime());
        cv.put(COL_YEAR,smsModel.getYear());
        cv.put(COL_MONTH,smsModel.getMonth());
        cv.put(COL_DAY,smsModel.getDay());
        cv.put(COL_CHARGE_AMT,smsModel.getAmtCharge());
        cv.put(COL_SENT_AMT,smsModel.getAmtSent());
        cv.put(COL_RECEIVED_AMT,smsModel.getAmtReceived());
        cv.put(COL_TRX_TYPE,smsModel.getTrxType());
        cv.put(COL_TRX_USER,smsModel.getTrxUser());

        db.insert(TABLE_NAME,null,cv);
        db.close();

    }



    //main page data
    public List<MainPageYearwise> getDataYearlyMonthly(SQLiteDatabase db)
    {
        List<MainPageYearwise> yearwiseParentList = new ArrayList<>();


        MainPageMonthly mainPageMonthlyObj;
        MainPageYearwise mainPageYearwiseParentObj;

        Cursor monthlyChildCursor;

        Cursor yearWiseParentCursor = db.query(TABLE_NAME,
                new String[]{COL_YEAR, "sum("+COL_CHARGE_AMT+")", "sum("+COL_RECEIVED_AMT+")", "sum("+COL_SENT_AMT+")"},
                null,
                null,
                COL_YEAR,
                null,
                null,
                null);

        if(yearWiseParentCursor != null && yearWiseParentCursor.moveToFirst())
        {
            do {
                monthlyChildCursor = db.query(TABLE_NAME,
                        new String[]{COL_MONTH, "sum("+COL_CHARGE_AMT+")", "sum("+COL_RECEIVED_AMT+")", "sum("+COL_SENT_AMT+")"},
                        COL_YEAR+" =?",
                        new String[]{yearWiseParentCursor.getString(0)},
                        COL_MONTH,
                        null,
                        null);

                List<MainPageMonthly> monthlyChildList = new ArrayList<>();
                if(monthlyChildCursor != null && monthlyChildCursor.moveToFirst())
                {
                    do {
                        if(monthlyChildCursor.getString(1) != null &&
                                monthlyChildCursor.getString(2) != null &&
                                monthlyChildCursor.getString(3) != null)
                        {
                            mainPageMonthlyObj = new MainPageMonthly(monthlyChildCursor.getString(1),
                                    monthlyChildCursor.getString(2),
                                    monthlyChildCursor.getString(3),
                                    monthlyChildCursor.getString(0));
                            monthlyChildList.add(mainPageMonthlyObj);
                        }
                        else if(monthlyChildCursor.getString(1) == null &&
                                monthlyChildCursor.getString(2) == null)
                        {
                            mainPageMonthlyObj = new MainPageMonthly("--",
                                    "--",
                                    monthlyChildCursor.getString(3),
                                    monthlyChildCursor.getString(0));
                            monthlyChildList.add(mainPageMonthlyObj);
                        }
                        else if(monthlyChildCursor.getString(2) == null &&
                                monthlyChildCursor.getString(3) == null)
                        {
                            mainPageMonthlyObj = new MainPageMonthly(monthlyChildCursor.getString(1),
                                    "--",
                                    "--",
                                    monthlyChildCursor.getString(0));
                            monthlyChildList.add(mainPageMonthlyObj);
                        }
                        else if(monthlyChildCursor.getString(1) == null &&
                                monthlyChildCursor.getString(3) == null)
                        {
                            mainPageMonthlyObj = new MainPageMonthly("--",
                                    monthlyChildCursor.getString(2),
                                    "--",
                                    monthlyChildCursor.getString(0));
                            monthlyChildList.add(mainPageMonthlyObj);
                        }
                        else if(monthlyChildCursor.getString(1) == null)
                        {
                            mainPageMonthlyObj = new MainPageMonthly("--",
                                    monthlyChildCursor.getString(2),
                                    monthlyChildCursor.getString(3),
                                    monthlyChildCursor.getString(0));
                            monthlyChildList.add(mainPageMonthlyObj);
                        }
                        else if(monthlyChildCursor.getString(2) == null)
                        {
                            mainPageMonthlyObj = new MainPageMonthly(monthlyChildCursor.getString(1),
                                    "--",
                                    monthlyChildCursor.getString(3),
                                    monthlyChildCursor.getString(0));
                            monthlyChildList.add(mainPageMonthlyObj);
                        }
                        else if(monthlyChildCursor.getString(3) == null)
                        {
                            mainPageMonthlyObj = new MainPageMonthly(monthlyChildCursor.getString(1),
                                    monthlyChildCursor.getString(2),
                                    "--",
                                    monthlyChildCursor.getString(0));
                            monthlyChildList.add(mainPageMonthlyObj);
                        }
                    }while (monthlyChildCursor.moveToNext());

                }
                if(yearWiseParentCursor.getString(1) != null
                        && yearWiseParentCursor.getString(2) != null
                        && yearWiseParentCursor.getString(3) != null)
                    {
                        mainPageYearwiseParentObj = new MainPageYearwise(yearWiseParentCursor.getString(0),
                                yearWiseParentCursor.getString(1),
                                yearWiseParentCursor.getString(2),
                                yearWiseParentCursor.getString(3),
                                monthlyChildList );

                        yearwiseParentList.add(mainPageYearwiseParentObj);
                    }
                    else if(yearWiseParentCursor.getString(1) == null)
                    {
                        mainPageYearwiseParentObj = new MainPageYearwise(yearWiseParentCursor.getString(0),
                                "-",
                                yearWiseParentCursor.getString(2),
                                yearWiseParentCursor.getString(3),
                                monthlyChildList );

                        yearwiseParentList.add(mainPageYearwiseParentObj);
                    }
                    else if(yearWiseParentCursor.getString(2) == null)
                    {
                        mainPageYearwiseParentObj = new MainPageYearwise(yearWiseParentCursor.getString(0),
                                yearWiseParentCursor.getString(1),
                                "-",
                                yearWiseParentCursor.getString(3),
                                monthlyChildList );

                        yearwiseParentList.add(mainPageYearwiseParentObj);
                    }
                    else if(yearWiseParentCursor.getString(3) == null)
                    {
                        mainPageYearwiseParentObj = new MainPageYearwise(yearWiseParentCursor.getString(0),
                                yearWiseParentCursor.getString(1),
                                yearWiseParentCursor.getString(2),
                                "-",
                                monthlyChildList );

                        yearwiseParentList.add(mainPageYearwiseParentObj);
                    }


                /*mainPageYearwiseParentObj = new MainPageYearwise(yearWiseParentCursor.getString(0),
                        yearWiseParentCursor.getString(1),
                        yearWiseParentCursor.getString(2),
                        yearWiseParentCursor.getString(3),
                        monthlyChildList);

                yearwiseParentList.add(mainPageYearwiseParentObj);*/

            }while(yearWiseParentCursor.moveToNext());
        }
        return yearwiseParentList;
    }




    public List<DayWiseDataParent> getDailyData(String month,String year,SQLiteDatabase db) {
        List<DayWiseDataParent> dayWiseDataParentsList = new ArrayList<>();

        DayWiseDataParent dayWiseDataParentObj;
        DailyDataChild dailyDataChildObj;

        Cursor dailyChildCursor;
        Cursor dayWiseParentCursor = db.query(TABLE_NAME,
                new String[]{COL_DAY, "sum(" + COL_CHARGE_AMT + ")", "sum(" + COL_RECEIVED_AMT + ")", "sum(" + COL_SENT_AMT + ")"},
                COL_MONTH + " like '" + month + "' AND " + COL_YEAR + " = " + year,
                null,
                COL_DAY,
                null,
                null);


        if (dayWiseParentCursor != null && dayWiseParentCursor.moveToFirst()) {
            do {


                dailyChildCursor = db.query(TABLE_NAME,
                        new String[]{COL_TIME, COL_TRX_TYPE, COL_TRX_USER, COL_RECEIVED_AMT, COL_SENT_AMT},
                        COL_MONTH + " like '" + month + "' AND " + COL_YEAR + " = " + year + " AND " + COL_DAY + " =?",
                        new String[]{dayWiseParentCursor.getString(0)},
                        null,
                        null,
                        null);
                List<DailyDataChild> dailyDataChildList = new ArrayList<>();
                if (dailyChildCursor != null && dailyChildCursor.moveToFirst()) {
                    do {

                        dailyDataChildObj = new DailyDataChild(dailyChildCursor.getString(0),
                                dailyChildCursor.getString(1),
                                dailyChildCursor.getString(2),
                                dailyChildCursor.getString(3) != null ? dailyChildCursor.getString(3) : dailyChildCursor.getString(4));

                        dailyDataChildList.add(dailyDataChildObj);

                    } while (dailyChildCursor.moveToNext());
                }

                dayWiseDataParentObj = new DayWiseDataParent(dayWiseParentCursor.getString(0)+"-"+month,
                        dayWiseParentCursor.getString(1),
                        dayWiseParentCursor.getString(3),
                        dayWiseParentCursor.getString(2),
                        dailyDataChildList);

                dayWiseDataParentsList.add(dayWiseDataParentObj);

            }while (dayWiseParentCursor.moveToNext());
        }

        return dayWiseDataParentsList;
    }

/*


    //getting monthly data
    public List<MainPageYearwise> getMonthlyData(SQLiteDatabase db) {
    //public void getMonthlyData(SQLiteDatabase db) {

        Log.i("YELLO@@@","IN DB handler");
        List<String> uniqueYearList = new ArrayList<>();
        List<MainPageMonthly> monthlyDataList = new ArrayList<>();
        List<MainPageYearwise> yearlyDataList = new ArrayList<>();


        MainPageMonthly mainPageMonthlyObj;
        MainPageYearwise mainpageYerlyObj;

        Cursor uniqueYearCursor = db.query(true,TABLE_NAME,new String[] {COL_YEAR},null,null,null,null,null,null);

        if(uniqueYearCursor.moveToFirst())
        {
            do
            {
                uniqueYearList.add(uniqueYearCursor.getString(0));
            }while (uniqueYearCursor.moveToNext());
        }

        Cursor monthlyDataYearWiseCursor, yearWiseDataCursor;

        for (String year:uniqueYearList)
        {
            Log.i("DB@Y",year);
            monthlyDataYearWiseCursor = db.query(TABLE_NAME,
                    new String[] {COL_MONTH, "sum("+COL_CHARGE_AMT+")", "sum("+COL_SENT_AMT+")", "sum("+COL_RECEIVED_AMT+")"},
                    COL_YEAR+" =?",
                    new String[] {year},
                    COL_MONTH,
                    null,
                    null,
                    null);



            if(monthlyDataYearWiseCursor != null && monthlyDataYearWiseCursor.moveToFirst())
            {
                Log.i("DB@@@", String.valueOf(monthlyDataYearWiseCursor.getCount()));
                Log.i("DB@@C", String.valueOf(monthlyDataYearWiseCursor.getColumnCount()));
                Log.i("DB@@N", String.valueOf(monthlyDataYearWiseCursor.isNull(1)));
                Log.i("DB@@D",monthlyDataYearWiseCursor.getString(0));


                do {
                    if(monthlyDataYearWiseCursor.getString(1) != null &&
                            monthlyDataYearWiseCursor.getString(2) != null &&
                            monthlyDataYearWiseCursor.getString(3) != null)
                    {
                                mainPageMonthlyObj = new MainPageMonthly(monthlyDataYearWiseCursor.getString(1),
                                        monthlyDataYearWiseCursor.getString(3),
                                        monthlyDataYearWiseCursor.getString(2),
                                        monthlyDataYearWiseCursor.getString(0));
                        monthlyDataList.add(mainPageMonthlyObj);
                    }
                    else if(monthlyDataYearWiseCursor.getString(1) == null &&
                            monthlyDataYearWiseCursor.getString(2) == null)
                    {
                        mainPageMonthlyObj = new MainPageMonthly("--",
                                monthlyDataYearWiseCursor.getString(3),
                                "--",
                                monthlyDataYearWiseCursor.getString(0));
                        monthlyDataList.add(mainPageMonthlyObj);
                    }
                    else if(monthlyDataYearWiseCursor.getString(2) == null &&
                            monthlyDataYearWiseCursor.getString(3) == null)
                    {
                        mainPageMonthlyObj = new MainPageMonthly(monthlyDataYearWiseCursor.getString(1),
                                "--",
                                "--",
                                monthlyDataYearWiseCursor.getString(0));
                        monthlyDataList.add(mainPageMonthlyObj);
                    }
                    else if(monthlyDataYearWiseCursor.getString(1) == null &&
                            monthlyDataYearWiseCursor.getString(3) == null)
                    {
                        mainPageMonthlyObj = new MainPageMonthly("--",
                                "--",
                                monthlyDataYearWiseCursor.getString(2),
                                monthlyDataYearWiseCursor.getString(0));
                        monthlyDataList.add(mainPageMonthlyObj);
                    }
                    else if(monthlyDataYearWiseCursor.getString(1) == null)
                    {
                        mainPageMonthlyObj = new MainPageMonthly("--",
                                monthlyDataYearWiseCursor.getString(3),
                                monthlyDataYearWiseCursor.getString(2),
                                monthlyDataYearWiseCursor.getString(0));
                        monthlyDataList.add(mainPageMonthlyObj);
                    }
                    else if(monthlyDataYearWiseCursor.getString(2) == null)
                    {
                        mainPageMonthlyObj = new MainPageMonthly(monthlyDataYearWiseCursor.getString(1),
                                monthlyDataYearWiseCursor.getString(3),
                                "--",
                                monthlyDataYearWiseCursor.getString(0));
                        monthlyDataList.add(mainPageMonthlyObj);
                    }
                    else if(monthlyDataYearWiseCursor.getString(3) == null)
                    {
                        mainPageMonthlyObj = new MainPageMonthly(monthlyDataYearWiseCursor.getString(1),
                                "--",
                                monthlyDataYearWiseCursor.getString(2),
                                monthlyDataYearWiseCursor.getString(0));
                        monthlyDataList.add(mainPageMonthlyObj);
                    }
                }while (monthlyDataYearWiseCursor.moveToNext());

                yearWiseDataCursor = db.query(TABLE_NAME,
                        new String[]{COL_YEAR, "sum("+COL_CHARGE_AMT+")", "sum("+COL_SENT_AMT+")", "sum("+COL_RECEIVED_AMT+")"},
                        COL_YEAR+"=?",
                        new String[]{year},
                        null,
                        null,
                        null);

                if(yearWiseDataCursor != null && yearWiseDataCursor.moveToFirst())
                {
                    do {
                        if(yearWiseDataCursor.getString(1) != null && yearWiseDataCursor.getString(2) != null && yearWiseDataCursor.getString(3) != null)
                        {
                            mainpageYerlyObj = new MainPageYearwise(yearWiseDataCursor.getString(0),
                                    yearWiseDataCursor.getString(1),
                                    yearWiseDataCursor.getString(3),
                                    yearWiseDataCursor.getString(2),
                                    monthlyDataList );

                            yearlyDataList.add(mainpageYerlyObj);
                        }
                        else if(yearWiseDataCursor.getString(1) == null)
                        {
                            mainpageYerlyObj = new MainPageYearwise(yearWiseDataCursor.getString(0),
                                    "-",
                                    yearWiseDataCursor.getString(3),
                                    yearWiseDataCursor.getString(2),
                                    monthlyDataList );

                            yearlyDataList.add(mainpageYerlyObj);
                        }
                        else if(yearWiseDataCursor.getString(2) == null)
                        {
                            mainpageYerlyObj = new MainPageYearwise(yearWiseDataCursor.getString(0),
                                    yearWiseDataCursor.getString(1),
                                    yearWiseDataCursor.getString(3),
                                    "-",
                                    monthlyDataList );

                            yearlyDataList.add(mainpageYerlyObj);
                        }
                        else if(yearWiseDataCursor.getString(3) == null)
                        {
                            mainpageYerlyObj = new MainPageYearwise(yearWiseDataCursor.getString(0),
                                    yearWiseDataCursor.getString(1),
                                    "-",
                                    yearWiseDataCursor.getString(2),
                                    monthlyDataList );

                            yearlyDataList.add(mainpageYerlyObj);
                        }

                    }while (yearWiseDataCursor.moveToNext());

                    yearWiseDataCursor.close();
                    */
/*ParentListDataModel parentListObj = new ParentListDataModel();
                    parentListObj.setmParentList(yearlyDataList);*//*

                }
            }
            else
            {
                Log.i("DB!!!","Cursor null");
            }
            monthlyDataYearWiseCursor.close();
        }

        return yearlyDataList;
    }
*/








    //getting daywise data for a particular month
    public List<DayWiseDataParent> dayWiseData(String month, String year, SQLiteDatabase db)
    {
        Log.i("Yello@@","inside dayWiseList");
        List<String> uniqueDaysList = new ArrayList<>();
        List<DayWiseDataParent> dayWiseDataParentList = new ArrayList<>();
        List<DailyDataChild> dailyDataChildList = new ArrayList<>();


        DailyDataChild dailyDataChildObj;
        DayWiseDataParent dayWiseDataParentObj;

        Cursor perDayTrxCursor,monthlyTrxCursor;

        Cursor uniqueDaysCursor = db.query(true,
                TABLE_NAME,
                new String[] {COL_DAY},
                COL_YEAR+"=? AND "+COL_MONTH+" like ?",
                new String[]{year,month},
                null,
                null,
                null,
                null);


        if(uniqueDaysCursor != null && uniqueDaysCursor.moveToFirst())
        {
            Log.i("YELO@@Uni_Day",String.valueOf(uniqueDaysCursor.getCount()));
            do {
                uniqueDaysList.add(uniqueDaysCursor.getString(0));
            }while (uniqueDaysCursor.moveToNext());
        }


        for(String day : uniqueDaysList)
        {
            if(day.length()==1) {
                /*perDayTrxCursor = db.query(TABLE_NAME,
                        new String[]{COL_TIME, COL_TRX_TYPE, COL_TRX_USER, COL_CHARGE_AMT, COL_SENT_AMT, COL_RECEIVED_AMT},
                        COL_YEAR + "=? AND " + COL_MONTH + " like ? AND " + COL_DAY + " like ?",
                        new String[]{year, "'" + month + "'", "'0" + day + "'"},
                        null,
                        null,
                        null);*/

                String queryStr = "select "+COL_TIME+" , "+COL_TRX_TYPE+" , "+COL_TRX_USER+" , "+COL_CHARGE_AMT+" , "+COL_SENT_AMT+" , "+COL_RECEIVED_AMT
                        +" from "+TABLE_NAME
                        +" where "+COL_YEAR +"="+year
                        +" AND "+COL_MONTH+" like '"+month+"'"
                        +" AND "+COL_DAY+" like '0"+day+"'";

                Log.i("YELO@@Query",queryStr);
                perDayTrxCursor = db.rawQuery(queryStr,null);
            }
            else
            {
               /* perDayTrxCursor = db.query(TABLE_NAME,
                        new String[]{COL_TIME, COL_TRX_TYPE, COL_TRX_USER, COL_CHARGE_AMT, COL_SENT_AMT, COL_RECEIVED_AMT},
                        COL_YEAR + "=? AND " + COL_MONTH + " like ? AND " + COL_DAY + " like ?",
                        new String[]{year, "'" + month + "'", "'" + day + "'"},
                        null,
                        null,
                        null);*/


               String queryStr = "select "+COL_TIME+" , "+COL_TRX_TYPE+" , "+COL_TRX_USER+" , "+COL_CHARGE_AMT+" , "+COL_SENT_AMT+" , "+COL_RECEIVED_AMT
                +" from "+TABLE_NAME
                +" where "+COL_YEAR +"="+year
                +" AND "+COL_MONTH+" like '"+month+"'"
                +" AND "+COL_DAY+" like '"+day+"'";

                Log.i("YELO@@Query",queryStr);
                perDayTrxCursor = db.rawQuery(queryStr,null);
            }



            Log.i("Yello@@##daily", String.valueOf(perDayTrxCursor.getCount()));
            if(perDayTrxCursor != null && perDayTrxCursor.moveToFirst())
            {
                Log.i("Yello@@#daily", String.valueOf(perDayTrxCursor.getCount()));
                do {
                    dailyDataChildObj = new DailyDataChild(perDayTrxCursor.getString(0),
                            perDayTrxCursor.getString(1),
                            perDayTrxCursor.getString(2),
                            perDayTrxCursor.getString(4) != null ? perDayTrxCursor.getString(4) : perDayTrxCursor.getString(5) );

                    dailyDataChildList.add(dailyDataChildObj);

                }while (perDayTrxCursor.moveToNext());
            }

            perDayTrxCursor.close();

           /* monthlyTrxCursor = db.query(TABLE_NAME,
                    new String[]{COL_DAY, "sum("+COL_CHARGE_AMT+")", "sum("+COL_SENT_AMT+")", "sum("+COL_RECEIVED_AMT+")"},
                    COL_YEAR+"=? AND "+COL_MONTH+" like ?",
                    new String[] {year,"'"+month+"'"},
                    COL_DAY,
                    null,
                    null);
*/
           String monthlyQueryStr ="select "+COL_DAY+" , sum("+COL_CHARGE_AMT+"),sum("+COL_SENT_AMT+"),sum("+COL_RECEIVED_AMT+")"
            +" from "+TABLE_NAME
            +" where "+COL_YEAR+" = "+year
            +" AND "+COL_MONTH+" like '"+month+"'"
            +" group by "+COL_DAY;

            Log.i("YELO***QueryMo",monthlyQueryStr);
           monthlyTrxCursor =db.rawQuery(monthlyQueryStr,null);
            Log.i("Yello@@##month", String.valueOf(monthlyTrxCursor.getCount()));

            if(monthlyTrxCursor != null && monthlyTrxCursor.moveToFirst())
            {
                Log.i("Yello@@#month", String.valueOf(monthlyTrxCursor.getCount()));
                do {

                    Log.i("Yello@@00",monthlyTrxCursor.getString(0));


                    if(monthlyTrxCursor.getString(1) != null &&
                            monthlyTrxCursor.getString(2) != null &&
                            monthlyTrxCursor.getString(3) != null)
                    {
                        dayWiseDataParentObj = new DayWiseDataParent(monthlyTrxCursor.getString(0),
                                monthlyTrxCursor.getString(1),
                                monthlyTrxCursor.getString(2),
                                monthlyTrxCursor.getString(3),
                                dailyDataChildList);

                        dayWiseDataParentList.add(dayWiseDataParentObj);
                    }
                    else if(monthlyTrxCursor.getString(1) == null &&
                            monthlyTrxCursor.getString(2) == null )
                    {
                        dayWiseDataParentObj = new DayWiseDataParent(monthlyTrxCursor.getString(0),
                                "--",
                                "--",
                                monthlyTrxCursor.getString(3),
                                dailyDataChildList);

                        dayWiseDataParentList.add(dayWiseDataParentObj);
                    }
                    else if(monthlyTrxCursor.getString(1) == null &&
                            monthlyTrxCursor.getString(3) == null)
                    {
                        dayWiseDataParentObj = new DayWiseDataParent(monthlyTrxCursor.getString(0),
                                "--",
                                monthlyTrxCursor.getString(2),
                                "--",
                                dailyDataChildList);

                        dayWiseDataParentList.add(dayWiseDataParentObj);
                    }
                    else if(monthlyTrxCursor.getString(1) == null)
                    {
                        dayWiseDataParentObj = new DayWiseDataParent(monthlyTrxCursor.getString(0),
                                "--",
                                monthlyTrxCursor.getString(2),
                                monthlyTrxCursor.getString(3),
                                dailyDataChildList);

                        dayWiseDataParentList.add(dayWiseDataParentObj);
                    }
                    else if(monthlyTrxCursor.getString(2) == null )
                    {
                        dayWiseDataParentObj = new DayWiseDataParent(monthlyTrxCursor.getString(0),
                                monthlyTrxCursor.getString(1),
                                "--",
                                monthlyTrxCursor.getString(3),
                                dailyDataChildList);

                        dayWiseDataParentList.add(dayWiseDataParentObj);
                    }
                    else if(monthlyTrxCursor.getString(3) == null )
                    {
                        dayWiseDataParentObj = new DayWiseDataParent(monthlyTrxCursor.getString(0),
                                monthlyTrxCursor.getString(1),
                                monthlyTrxCursor.getString(2),
                                "--",
                                dailyDataChildList);

                        dayWiseDataParentList.add(dayWiseDataParentObj);
                    }
                }while (monthlyTrxCursor.moveToNext());
            }
            monthlyTrxCursor.close();
        }
        Log.i("Yello!!Lent",String.valueOf(dayWiseDataParentList.size()));
        return dayWiseDataParentList;
    }



    //to get the summary
    public List<SummaryParentModel> getSummary(SQLiteDatabase db)
    {
        SummaryChildModel summaryChildObj;
        SummaryParentModel summaryParentObj;

        List<String> uniqueTrxTypeList = new ArrayList<>();
        List<SummaryChildModel> summaryChildList = new ArrayList<>();
        List<SummaryParentModel> summaryParentList = new ArrayList<>();

        Cursor trxDtlsBasedOnTypeCursor;
        Cursor trxTypeSummaryCursor;

        Cursor uniqueTrxCursor = db.query(true,TABLE_NAME,
                new String[] {COL_TRX_TYPE},
                null,
                null,
                null,
                null,
                null,
                null);

        if (uniqueTrxCursor != null && uniqueTrxCursor.moveToFirst())
        {
            do {
                uniqueTrxTypeList.add(uniqueTrxCursor.getString(0));
            }while (uniqueTrxCursor.moveToNext());
        }

        for (String uniqueTrxType : uniqueTrxTypeList)
        {
            trxDtlsBasedOnTypeCursor = db.query(TABLE_NAME,
                    new String[] {COL_DAY,COL_MONTH,COL_YEAR,COL_TRX_USER,COL_SENT_AMT,COL_RECEIVED_AMT},
                    COL_TRX_TYPE+" like ?",
                    new String[] {uniqueTrxType},
                    null,
                    null,
                    null);

            if(trxDtlsBasedOnTypeCursor != null && trxDtlsBasedOnTypeCursor.moveToFirst())
            {
                summaryChildObj = new SummaryChildModel(trxDtlsBasedOnTypeCursor.getString(0)+"-"+trxDtlsBasedOnTypeCursor.getString(1)+"-"+trxDtlsBasedOnTypeCursor.getString(2),
                        trxDtlsBasedOnTypeCursor.getString(3),
                        trxDtlsBasedOnTypeCursor.getString(4) != null ? trxDtlsBasedOnTypeCursor.getString(4) : trxDtlsBasedOnTypeCursor.getString(5));

                Log.i("Summary@@",trxDtlsBasedOnTypeCursor.getString(4) != null ? trxDtlsBasedOnTypeCursor.getString(4) : trxDtlsBasedOnTypeCursor.getString(5));

                summaryChildList.add(summaryChildObj);
            }


            trxTypeSummaryCursor = db.query(TABLE_NAME,
                    new String[] {COL_TRX_TYPE,"sum(" + COL_RECEIVED_AMT + ")", "sum(" + COL_SENT_AMT + ")"},
                    COL_TRX_TYPE+" like ? ",
                    new String[]{uniqueTrxType},
                    null,
                    null,
                    null);

            if (trxTypeSummaryCursor != null && trxTypeSummaryCursor.moveToFirst())
            {
                summaryParentObj = new SummaryParentModel(trxTypeSummaryCursor.getString(0),
                        trxTypeSummaryCursor.getString(1) != null ? trxTypeSummaryCursor.getString(1) : trxTypeSummaryCursor.getString(2),
                        summaryChildList);

                summaryParentList.add(summaryParentObj);
            }

        }

        return summaryParentList;
    }
}
