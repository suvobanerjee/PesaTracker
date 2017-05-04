package com.excogit.suvo.pesatracker.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.excogit.suvo.pesatracker.R;
import com.excogit.suvo.pesatracker.constants.StringConstants;
import com.excogit.suvo.pesatracker.db.DbHandler;
import com.excogit.suvo.pesatracker.model.SMSDataModel;

import java.text.SimpleDateFormat;

public class SMSSaveService extends IntentService {



    public SMSSaveService() {
        super("SMSSaveService");
    }


    DbHandler dbHandler = new DbHandler(this);

    SMSDataModel smsModel = new SMSDataModel();

    ResultReceiver resultReceiver ;

    @Override
    protected void onHandleIntent(Intent intent) {

        String sender = intent.getStringExtra(StringConstants.SENDER);
        String msg = intent.getStringExtra(StringConstants.MSG_BODY);
        long timestamp = intent.getLongExtra(StringConstants.TIMESTAMP,0);

        if(intent.hasExtra(StringConstants.RESULT_RECEIVER_CONSTANT)) {
            resultReceiver = intent.getParcelableExtra(StringConstants.RESULT_RECEIVER_CONSTANT);
        }

        Log.i("FromService@@@",sender);
        Log.i("FromService@@@",msg);
        Log.i("FromService@@@Stamp1",String.valueOf(timestamp));



        if(sender.equals(StringConstants.SENDER_AIRTEL))    //for airtel money-Uganda,Kenya,Tanzania
        {
            parseAirtelMsg(msg,timestamp);
        }
        else if(sender.equals(StringConstants.SENDER_MTN))  //for mtn
        {
            parseMTNMsg(msg,timestamp);
        }
        else if(sender.equals(StringConstants.SENDER_AFRICEL))     //for africel
        {

            parseAfricelMsg(msg,timestamp);
        }
        else if(sender.equals(StringConstants.SENDER_MPESA))
        //else if(sender.equals("9004049239"))
        {
            parseMpesaMsg(msg,timestamp);
        }

        SharedPreferences pref = getSharedPreferences(getString(R.string.pref_name),MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(StringConstants.PREF_FIRST_INSTAL_FLAG,false);
        editor.commit();


        if(intent.hasExtra(StringConstants.RESULT_RECEIVER_CONSTANT))
        {
            Bundle bundle = new Bundle();
            resultReceiver.send(100, bundle);
        }
    }


    private void parseAirtelMsg(String msg, long timestamp)
    {
        String[] splittedMsg = msg.split(" ");


        //for airtelMoney Uganda
        if(splittedMsg[0].equals(StringConstants.AIRTEL_PAYBILL_KEY))
        {

            //for airtel paybill
            Log.i("FromService@@@",splittedMsg[0]);
            Log.i("FromService@@@",splittedMsg[1]);
            Log.i("FromService@@@",splittedMsg[2]);
            Log.i("FromService@@@",splittedMsg[6]);

            String[] refinementChargeArray = splittedMsg[6].split("\n");
            String refinedCharge = refinementChargeArray[0];

            smsModel.setAmtSent(commaRemover(splittedMsg[2]));
            smsModel.setAmtCharge(commaRemover(refinedCharge));
            smsModel.setAmtReceived(null);
            smsModel.setSmsBody(msg);
            smsModel.setSender(StringConstants.SENDER_AIRTEL);
            smsModel.setTrxType(StringConstants.AIRTEL_PAYBILL);
            smsModel.setTrxUser(splittedMsg[4]);
            dateConverter(timestamp);


            //save in db
            //sms text,the amount, charges, timestamp, and type of transaction

            dbHandler.insertData(smsModel);
        }
        else if(splittedMsg[0].equals(StringConstants.AIRTEL_AIRTIME_KEY))
        {
            //for airtel airtime
            smsModel.setSmsBody(msg);
            smsModel.setSender(StringConstants.SENDER_AIRTEL);
            smsModel.setAmtCharge(null);
            smsModel.setAmtSent(commaRemover(splittedMsg[4]));
            smsModel.setAmtReceived(null);
            smsModel.setTrxType(StringConstants.AIRTEL_Airtime);

            String[] refinedUserArray = splittedMsg[6].split("\\.");
            smsModel.setTrxUser(refinedUserArray[0]);
            dateConverter(timestamp);

            //save in db
            dbHandler.insertData(smsModel);

        }
        else if(splittedMsg[2].equals(StringConstants.AIRTEL_SENDMONEY_KEY))
        {
            //for airtel send money
            smsModel.setSmsBody(msg);
            smsModel.setSender(StringConstants.SENDER_AIRTEL);

            String[] refinementChargeArray = splittedMsg[12].split("\\.");
            String refinedCharge = refinementChargeArray[0];

            smsModel.setAmtCharge(commaRemover(refinedCharge));
            smsModel.setAmtSent(commaRemover(splittedMsg[4]));
            smsModel.setAmtReceived(null);
            smsModel.setTrxType(StringConstants.AIRTEL_SENDMONEY);

            if(splittedMsg[7].equals("on"))
            {
                smsModel.setTrxUser(splittedMsg[6]);
            }
            else
            {
                smsModel.setTrxUser(splittedMsg[6]+" "+splittedMsg[7]);
            }
            dateConverter(timestamp);

            //save a row in db
            dbHandler.insertData(smsModel);
        }
        else if(splittedMsg[2].equals(StringConstants.AIRTEL_RECEIVEMONEY_KEY))
        {
            //for airtel receive money
            smsModel.setSmsBody(msg);
            smsModel.setSender(StringConstants.SENDER_AIRTEL);
            smsModel.setAmtCharge(null);
            smsModel.setAmtSent(null);

            String[] refinementReceivedArray = splittedMsg[3].split("X");
            String refinedReceived = refinementReceivedArray[1];

            smsModel.setAmtReceived(commaRemover(refinedReceived));
            smsModel.setTrxType(StringConstants.AIRTEL_RECEIVEMONEY);

            if(splittedMsg[6].equals("Your"))
            {
                smsModel.setTrxUser(splittedMsg[5]);
            }
            else
            {
                smsModel.setTrxUser(splittedMsg[5]+" "+splittedMsg[6]);
            }
            dateConverter(timestamp);

            //save db
            dbHandler.insertData(smsModel);
        }
        else if(splittedMsg[1].equals(StringConstants.AIRTEL_WITHDRAWMONEY_KEY))
        {
            //for airtel withdraw money
            smsModel.setSmsBody(msg);
            smsModel.setSender(StringConstants.SENDER_AIRTEL);
            smsModel.setAmtCharge(commaRemover(splittedMsg[8]));
            smsModel.setAmtSent(commaRemover(splittedMsg[4]));
            smsModel.setAmtReceived(null);
            smsModel.setTrxType(StringConstants.AIRTEL_WITHDRAWMONEY);

            String[] refinedUserArray = splittedMsg[7].split("\\.");
            String refineUser = refinedUserArray[0];
            smsModel.setTrxUser(splittedMsg[6]+" "+refineUser);
            dateConverter(timestamp);

            //save db
            dbHandler.insertData(smsModel);
        }
        else if(splittedMsg[1].equals(StringConstants.AIRTEL_DEPOSITMONEY_KEY))
        {
            //for airtel deposit money
            smsModel.setSmsBody(msg);
            smsModel.setSender(StringConstants.SENDER_AIRTEL);
            smsModel.setAmtCharge(null);
            smsModel.setAmtSent(commaRemover(splittedMsg[4]));
            smsModel.setAmtReceived(null);
            smsModel.setTrxType(StringConstants.AIRTEL_DEPOSITMONEY);

/*            String[] refinedUserArray = splittedMsg[7].split(".");
            String refineUser = refinedUserArray[0];*/
            smsModel.setTrxUser(splittedMsg[6]+" "+splittedMsg[7]);
            dateConverter(timestamp);

            //save db
            dbHandler.insertData(smsModel);
        }

        //for airtel money Kenya
        else if (splittedMsg[3].equals(StringConstants.AIRTEL_KE_PAYBILL_KEY))
        {
            //for airtel money kenya paybill
            smsModel.setSmsBody(msg);
            smsModel.setSender(StringConstants.SENDER_AIRTEL);

            String refinedChargeArray[] = splittedMsg[12].split("h");
            String[] dotRemoveRefiendChargeArray = refinedChargeArray[1].split("\\.");

            smsModel.setAmtCharge(dotRemoveRefiendChargeArray[0]);

            String[] refinedSentAmtArr = splittedMsg[2].split("h");
            smsModel.setAmtSent(refinedSentAmtArr[1]);

            smsModel.setAmtReceived(null);
            smsModel.setTrxType(StringConstants.AIRTEL_PAYBILL);
            smsModel.setTrxUser(splittedMsg[5]);
            dateConverter(timestamp);

            //save db
            dbHandler.insertData(smsModel);
        }
        else if (splittedMsg[7].equals(StringConstants.AIRTEL_KE_AIRTIME_KEY))
        {
            //for airtel kenya airtime
            smsModel.setSmsBody(msg);
            smsModel.setSender(StringConstants.SENDER_AIRTEL);
            smsModel.setAmtCharge(null);
            String[] refinedSentAmtArr = splittedMsg[5].split("h");

            smsModel.setAmtSent(refinedSentAmtArr[1]);
            smsModel.setAmtReceived(null);
            smsModel.setTrxType(StringConstants.AIRTEL_Airtime);
            smsModel.setTrxUser(null);
            dateConverter(timestamp);

            //save db
            dbHandler.insertData(smsModel);
        }
        else if (splittedMsg[4].equals(StringConstants.AIRTEL_KE_SENDMONEY_KEY))
        {
            //for airtel kenya send money
            smsModel.setSmsBody(msg);
            smsModel.setSender(StringConstants.SENDER_AIRTEL);

            String refinedChargeArr[] = splittedMsg[16].split("S");
            String dotRemoveArr[] = refinedChargeArr[1].split("\\.");

            smsModel.setAmtCharge(dotRemoveArr[0]);

            String refiendAmtSentArr[] = splittedMsg[5].split("S");
            smsModel.setAmtSent(commaRemover(refiendAmtSentArr[1]));
            smsModel.setAmtReceived(null);
            smsModel.setTrxType(StringConstants.AIRTEL_SENDMONEY);

            if(splittedMsg[8].equals("on"))
            {
                smsModel.setTrxUser(splittedMsg[7]);
            }
            else
            {
                smsModel.setTrxUser(splittedMsg[7]+" "+splittedMsg[8]);
            }
            dateConverter(timestamp);

            //save db
            dbHandler.insertData(smsModel);

        }
        else if (splittedMsg[4].equals(StringConstants.AIRTEL_KE_RECEIVEMONEY_KEY))
        {
            //for airtel kenya receive money
            smsModel.setSmsBody(msg);
            smsModel.setSender(StringConstants.SENDER_AIRTEL);

            String refinedChargeArr[] = splittedMsg[17].split("S");
            Log.i("kenya@@@",splittedMsg[17]);
            String dotRemoveArr[] = refinedChargeArr[1].split("\\.");
            smsModel.setAmtCharge(dotRemoveArr[0]);

            smsModel.setAmtSent(null);

            String refinedAmtReceivedArr[] = splittedMsg[5].split("S");
            smsModel.setAmtReceived(refinedAmtReceivedArr[1]);
            smsModel.setTrxType(StringConstants.AIRTEL_RECEIVEMONEY);

            if(splittedMsg[8].equals("as"))
            {
                smsModel.setTrxUser(splittedMsg[7]);
            }
            else
            {
                smsModel.setTrxUser(splittedMsg[7]+" "+splittedMsg[8]);
            }
            dateConverter(timestamp);

            //save db
            dbHandler.insertData(smsModel);

        }
        else if (splittedMsg[4].equals(StringConstants.AIRTEL_KE_WITHDRAWMONEY_KEY))
        {
            //for airtel money kenya withdraw
            smsModel.setSmsBody(msg);
            smsModel.setSender(StringConstants.SENDER_AIRTEL);

            String refineSentAmtArr[] = splittedMsg[5].split("h");
            smsModel.setAmtSent(refineSentAmtArr[1]);
            smsModel.setAmtReceived(null);
            smsModel.setTrxType(StringConstants.AIRTEL_WITHDRAWMONEY);
            dateConverter(timestamp);

            int j=0;
            for(int i=0;i<msg.length();i++)
            {
                if(splittedMsg[i].equals("Charge:"))
                {
                    j=i+1;

                    String refineChargeArr[] = splittedMsg[j].split("h");
                    String dotRemove[] = refineChargeArr[1].split("\\.");
                    smsModel.setAmtCharge(dotRemove[0]);
                }
                else if(splittedMsg[i].equals("from"))
                {
                    j=i+1;
                    String trxUser="";
                    do {
                        trxUser = trxUser+" ";
                    }while(!splittedMsg[j].equals("on"));
                    smsModel.setTrxUser(trxUser);
                }
            }

            //save db
            dbHandler.insertData(smsModel);
        }

        //new logic for parsing sms
        //for deposit money airtel kenya
        for (int i=0;i<splittedMsg.length;i++)
        {
            if(splittedMsg[i].equals(StringConstants.AIRTEL_KE_DEPOSITMONEY_KEY))
            {
                Log.i("Kenya****","Inside");
                smsModel.setSmsBody(msg);
                smsModel.setSender(StringConstants.SENDER_AIRTEL);
                smsModel.setAmtCharge(null);

                String refinedSentAmtArr[] = splittedMsg[i+1].split("h");
                smsModel.setAmtSent(commaRemover(refinedSentAmtArr[1]));
                smsModel.setAmtReceived(null);
                String trxUser = "";

                int j=1,k=0;
                if (splittedMsg[j].equals("Confirmed."))
                {
                    Log.i("Kenya!!!",splittedMsg[j]);

                    j=msg.indexOf(".");
                    k=msg.indexOf("has");
                    Log.i("Kenya J",String.valueOf(j));
                    Log.i("Kenya K",String.valueOf(k));
                    trxUser = msg.substring(j+1,k-1);

                    Log.i("Kenya user",trxUser);
                    smsModel.setTrxUser(trxUser);

                }

                smsModel.setTrxType(StringConstants.AIRTEL_DEPOSITMONEY);
                dateConverter(timestamp);

                //save db
                dbHandler.insertData(smsModel);

            }
        }

    }





    //**************************************
    // mtn sms parsing
    //**************************************
    private void parseMTNMsg(String msg, long timestamp)
    {
        String[] splittedMsg = msg.split(" ");

        if(splittedMsg[3].equals(StringConstants.MTN_PAYBILL_KEY))
        {
            //for mtn paybill
            smsModel.setSmsBody(msg);
            smsModel.setSender(StringConstants.SENDER_MTN);
            smsModel.setAmtCharge(null);
            smsModel.setAmtSent(commaRemover(splittedMsg[5]));
            smsModel.setAmtReceived(null);
            smsModel.setTrxType(StringConstants.MTN_PAYBILL);
            smsModel.setTrxUser(splittedMsg[7]);
            dateConverter(timestamp);

            //save db
            dbHandler.insertData(smsModel);
        }
        else if(splittedMsg[5].equals(StringConstants.MTN_AIRTIME_KEY))
        {
            //for mtn airtime
            smsModel.setSmsBody(msg);
            smsModel.setSender(StringConstants.SENDER_MTN);
            smsModel.setAmtCharge(null);

            String[] refinedSentAmtArray = splittedMsg[4].split("/-");
            String refinedSentAmt = refinedSentAmtArray[0];

            smsModel.setAmtSent(commaRemover(refinedSentAmt));
            smsModel.setAmtReceived(null);
            smsModel.setTrxType(StringConstants.MTN_Airtime);
            smsModel.setTrxUser(null);
            dateConverter(timestamp);

            //save db
            dbHandler.insertData(smsModel);
        }
        else if(splittedMsg[3].equals(StringConstants.MTN_SENDMONEY_KEY))
        {
            //for mtn send money
            smsModel.setSmsBody(msg);
            smsModel.setSender(StringConstants.SENDER_MTN);

            String[] refinedAmtChargeArray = splittedMsg[11].split("\\.");

            smsModel.setAmtCharge(commaRemover(refinedAmtChargeArray[0]));

            smsModel.setAmtSent(commaRemover(splittedMsg[5]));
            smsModel.setAmtReceived(null);
            smsModel.setTrxType(StringConstants.MTN_SENDMONEY);

            if(splittedMsg[8].equals("on"))
            {
                smsModel.setTrxUser(splittedMsg[7]);
            }
            else
            {
                smsModel.setTrxUser(splittedMsg[6]+" "+splittedMsg[7]);
            }
            dateConverter(timestamp);

            //save db
            dbHandler.insertData(smsModel);
        }
        else if(splittedMsg[3].equals(StringConstants.MTN_RECEIVEMONEY_KEY))
        {
            //for mtn receive money
            smsModel.setSmsBody(msg);
            smsModel.setSender(StringConstants.SENDER_MTN);
            smsModel.setAmtCharge(null);
            smsModel.setAmtSent(null);
            smsModel.setAmtReceived(commaRemover(splittedMsg[5]));
            smsModel.setTrxType(StringConstants.MTN_RECEIVEMONEY);

            if(splittedMsg[8].equals("on")) {
                smsModel.setTrxUser(splittedMsg[7]);
            }
            else
            {
                smsModel.setTrxUser(splittedMsg[7]+" "+splittedMsg[8]);
            }
            dateConverter(timestamp);

            //save db
            dbHandler.insertData(smsModel);
        }
        else if(splittedMsg[3].equals(StringConstants.MTN_WITHDRAWMONEY_KEY))
        {
            //for mtn withdraw money
            smsModel.setSmsBody(msg);
            smsModel.setSender(StringConstants.SENDER_MTN);
            smsModel.setAmtCharge(commaRemover(splittedMsg[14]));
            smsModel.setAmtSent(commaRemover(splittedMsg[5]));
            smsModel.setAmtReceived(null);
            smsModel.setTrxType(StringConstants.MTN_WITHDRAWMONEY);

            String[] refinedTrxUser1Arr = splittedMsg[6].split(":");
            String[] refinedTrxUser2Arr = splittedMsg[7].split("\\.");
            smsModel.setTrxUser(refinedTrxUser1Arr[1]+" "+refinedTrxUser2Arr[0]);
            dateConverter(timestamp);

            //save db
            dbHandler.insertData(smsModel);
        }
       /* keyword not found
       else if(splittedMsg[3].equals(StringConstants.MTN_PAYBILL_KEY))
        {
            //for mtn paybill
            smsModel.setSmsBody(msg);
            smsModel.setSender(StringConstants.SENDER_MTN);
            smsModel.setAmtCharge(null);
            smsModel.setAmtSent(splittedMsg[5]);
            smsModel.setAmtReceived(null);
            smsModel.setTrxType(StringConstants.MTN_PAYBILL);
            smsModel.setTrxUser(splittedMsg[7]);
            dateConverter(timestamp);

            //save db
            dbHandler.insertData(smsModel);
        }*/
    }


    //for parsing africell sms
    private void parseAfricelMsg(String msg,long timestamp)
    {
        String[] splittedMsg = msg.split(" ");

        if(splittedMsg[2].equals(StringConstants.AFRICELL_PAYBILL_KEY))
        {
            //for africell paybill
            smsModel.setSmsBody(msg);
            smsModel.setSender(StringConstants.SENDER_AFRICEL);
            smsModel.setAmtCharge(commaRemover(splittedMsg[8]));
            smsModel.setAmtSent(commaRemover(splittedMsg[4]));
            smsModel.setAmtReceived(null);
            smsModel.setTrxType(StringConstants.AFRICELL_PAYBILL);
            smsModel.setTrxUser(splittedMsg[8]);
            dateConverter(timestamp);

            //save db
            dbHandler.insertData(smsModel);
        }
        else if(splittedMsg[5].equals(StringConstants.AFRICELL_AIRTIME_KEY))
        {
            //for africell airtime
            smsModel.setSmsBody(msg);
            smsModel.setSender(StringConstants.SENDER_AFRICEL);
            smsModel.setAmtCharge(null);
            smsModel.setAmtSent(commaRemover(splittedMsg[4]));
            smsModel.setAmtReceived(null);
            smsModel.setTrxType(StringConstants.AFRICELL_Airtime);
            smsModel.setTrxUser(null);
            dateConverter(timestamp);

            //save db
            dbHandler.insertData(smsModel);
        }
        else if(splittedMsg[2].equals(StringConstants.AFRICELL_SENDMONEY_KEY))
        {
            //for sfricell send money
            smsModel.setSmsBody(msg);
            smsModel.setSender(StringConstants.SENDER_AFRICEL);
            smsModel.setAmtCharge(commaRemover(splittedMsg[12]));
            smsModel.setAmtSent(commaRemover(splittedMsg[4]));
            smsModel.setAmtReceived(null);
            smsModel.setTrxType(StringConstants.AFRICELL_SENDMONEY);

            if(splittedMsg[7].equals("on")) {
                smsModel.setTrxUser(splittedMsg[6]);
            }
            else
            {
                smsModel.setTrxUser(splittedMsg[6]+" "+splittedMsg[7]);
            }
            dateConverter(timestamp);

            //save db
            dbHandler.insertData(smsModel);
        }
        else if(splittedMsg[2].equals(StringConstants.AFRICELL_RECEIVEMONEY_KEY))
        {
            //for africell receive money
            smsModel.setSmsBody(msg);
            smsModel.setSender(StringConstants.SENDER_AFRICEL);
            smsModel.setAmtCharge(null);
            smsModel.setAmtSent(null);

            String[] refinedAmtSentArray = splittedMsg[3].split("X");

            smsModel.setAmtReceived(commaRemover(refinedAmtSentArray[1]));
            smsModel.setTrxType(StringConstants.AFRICELL_RECEIVEMONEY);

            if(splittedMsg[6].equals("Your")) {
                smsModel.setTrxUser(splittedMsg[5]);
            }
            else
            {
                smsModel.setTrxUser(splittedMsg[5]+" "+splittedMsg[6]);
            }
            dateConverter(timestamp);

            //save db
            dbHandler.insertData(smsModel);
        }
        else if(splittedMsg[1].equals(StringConstants.AFRICELL_WITHDRAWMONEY_KEY))
        {
            //for africell withdraw money
            smsModel.setSmsBody(msg);
            smsModel.setSender(StringConstants.SENDER_AFRICEL);
            smsModel.setAmtCharge(commaRemover(splittedMsg[11]));
            smsModel.setAmtSent(commaRemover(splittedMsg[4]));
            smsModel.setAmtReceived(null);
            smsModel.setTrxType(StringConstants.AFRICELL_WITHDRAWMONEY);

            if(splittedMsg[8].contains("Charge:")) {
                smsModel.setTrxUser(splittedMsg[6]+splittedMsg[7]);
            }
            else
            {
                smsModel.setTrxUser(splittedMsg[6]+splittedMsg[7]+" "+splittedMsg[8]);
            }
            dateConverter(timestamp);

            //save db
            dbHandler.insertData(smsModel);
        }
        else if(splittedMsg[5].equals(StringConstants.AFRICELL_DEPOSITMONEY_KEY))
        {
            //for africell deposit money
            smsModel.setSmsBody(msg);
            smsModel.setSender(StringConstants.SENDER_AFRICEL);
            smsModel.setAmtCharge(null);
            smsModel.setAmtSent(commaRemover(splittedMsg[4]));
            smsModel.setAmtReceived(null);
            smsModel.setTrxType(StringConstants.AFRICELL_DEPOSITMONEY);

            if(splittedMsg[8].contains("Balance:")) {
                smsModel.setTrxUser(splittedMsg[6]+splittedMsg[7]);
            }
            else
            {
                smsModel.setTrxUser(splittedMsg[6]+splittedMsg[7]+" "+splittedMsg[8]);
            }
            dateConverter(timestamp);

            //save db
            dbHandler.insertData(smsModel);
        }
    }


    //*****************************************************
    //for mpesa
    //*****************************************************
    private void parseMpesaMsg(String msg, long timestamp)
    {
        String[] splittedMsg = msg.split(" ");
        Log.i("KENYA Mpesa",String.valueOf(splittedMsg.length));

        if (msg.length()>75) {
            Log.i("Kenya MPesa%$#@!","Inside parsing");
            if (splittedMsg[3].equals(StringConstants.MPESA_KE_PAYBILL_KEY)) {
                //for mpesa kenya paybill
                smsModel.setSmsBody(msg);
                smsModel.setSender(StringConstants.SENDER_MPESA);

                int last = splittedMsg.length;
                String[] refinedChargeArr = splittedMsg[last].split("h");
                String dotRemove[] = refinedChargeArr[1].split("\\.");
                smsModel.setAmtCharge(dotRemove[0]);

                String[] refinedSentAmtArr = splittedMsg[2].split("h");
                String dotRemoveSentAmt[] = refinedSentAmtArr[1].split("\\.");
                smsModel.setAmtSent(commaRemover(dotRemoveSentAmt[0]));

                smsModel.setAmtReceived(null);
                smsModel.setTrxType(StringConstants.MPESA_KE_PAYBILL);

                if (splittedMsg[6].equals("on")) {
                    smsModel.setTrxUser(splittedMsg[5]);
                } else {
                    smsModel.setTrxUser(splittedMsg[5] + " " + splittedMsg[6]);
                }
                dateConverter(timestamp);

                //save db
                dbHandler.insertData(smsModel);
            } else if (splittedMsg[5].equals(StringConstants.MPESA_KE_AIRTIME_KEY)) {
                //for mpesa kenya airtime
                smsModel.setSmsBody(msg);
                smsModel.setSender(StringConstants.SENDER_MPESA);

                int last = splittedMsg.length;
                String[] refinedChargeArr = splittedMsg[last].split("h");
                String dotRemove[] = refinedChargeArr[1].split("\\.");
                smsModel.setAmtCharge(dotRemove[0]);

                String[] refinedSentAmtArr = splittedMsg[3].split("h");
                String dotRemoveSentAmt[] = refinedSentAmtArr[1].split("\\.");
                smsModel.setAmtSent(commaRemover(dotRemoveSentAmt[0]));

                smsModel.setAmtReceived(null);
                smsModel.setTrxType(StringConstants.MPESA_KE_Airtime);
                smsModel.setTrxUser(null);

                dateConverter(timestamp);

                //save db
                dbHandler.insertData(smsModel);

            } else if (splittedMsg[3].equals(StringConstants.MPESA_KE_SENDMONEY_KEY)) {
                //for mpesa kenya send money
                smsModel.setSmsBody(msg);
                smsModel.setSender(StringConstants.SENDER_MPESA);


                int last = splittedMsg.length;
                String[] refinedChargeArr = splittedMsg[last].split("h");
                String dotRemove[] = refinedChargeArr[1].split("\\.");
                smsModel.setAmtCharge(dotRemove[0]);

                String[] refinedSentAmtArr = splittedMsg[2].split("h");
                String dotRemoveSentAmt[] = refinedSentAmtArr[1].split("\\.");
                smsModel.setAmtSent(commaRemover(dotRemoveSentAmt[0]));

                smsModel.setAmtReceived(null);
                smsModel.setTrxType(StringConstants.MPESA_KE_SENDMONEY);

                if (splittedMsg[6].equals("on")) {
                    smsModel.setTrxUser(splittedMsg[5]);
                } else if (splittedMsg[7].equals("on")) {
                    smsModel.setTrxUser(splittedMsg[5] + " " + splittedMsg[6]);
                } else {
                    smsModel.setTrxUser(splittedMsg[5] + " " + splittedMsg[6] + " " + splittedMsg[7]);
                }

                dateConverter(timestamp);

                //save db
                dbHandler.insertData(smsModel);

            } else if (splittedMsg[3].equals(StringConstants.MPESA_KE_RECEIVEMONEY_KEY)) {
                //for mpesa kenya receive money
                smsModel.setSmsBody(msg);
                smsModel.setSender(StringConstants.SENDER_MPESA);
                smsModel.setAmtCharge(null);
                smsModel.setAmtSent(null);

                String[] refinedRreceivedAmtArr = splittedMsg[4].split("h");
                String dotRemoveReceivedAmt[] = refinedRreceivedAmtArr[1].split("\\.");
                smsModel.setAmtReceived(commaRemover(dotRemoveReceivedAmt[0]));
                smsModel.setTrxType(StringConstants.MPESA_KE_RECEIVEMONEY);

                if (splittedMsg[7].equals("on")) {
                    smsModel.setTrxUser(splittedMsg[6]);
                } else {
                    smsModel.setTrxUser(splittedMsg[6] + " " + splittedMsg[7]);
                }

                dateConverter(timestamp);

                //save db
                dbHandler.insertData(smsModel);

            }
            //else if (splittedMsg[5].equals(StringConstants.MPESA_KE_WITHDRAWMONEY_KEY))
            else if (splittedMsg[6].contains("Ksh"))
            //else if(msg.contains(StringConstants.MPESA_KE_WITHDRAWMONEY_KEY))
            {
                Log.i("Kenya Mpesa*&^%$","Inside Withdraw");
                //for mpesa kenya withdraw money
                //parsing for the keyword
                String refinedKeywordArr[] = splittedMsg[5].split("M");
                //if (refinedKeywordArr[1].equals(StringConstants.MPESA_KE_WITHDRAWMONEY_KEY))
                {

                    smsModel.setSmsBody(msg);
                    smsModel.setSender(StringConstants.SENDER_MPESA);
                    smsModel.setAmtCharge(null);

                    String[] refinedSentAmtArr = splittedMsg[6].split("h");
                    String dotRemoveSentAmt[] = refinedSentAmtArr[1].split("\\.");
                    smsModel.setAmtSent(commaRemover(dotRemoveSentAmt[0]));

                    smsModel.setAmtReceived(null);


                    for (int i = 0; i < splittedMsg.length; i++) {
                        if (splittedMsg[i].equals("from")) {
                            int j = msg.indexOf("from")+4;
                            int k = msg.indexOf("New");

                            String trxUser = msg.substring(j, k);
                            smsModel.setTrxUser(trxUser);
                        }
                    }
                    smsModel.setTrxType(StringConstants.MPESA_KE_WITHDRAWMONEY);

                    dateConverter(timestamp);

                    //save db
                    dbHandler.insertData(smsModel);

                }
            } else if (splittedMsg[7].equals(StringConstants.MPESA_KE_DEPOSITMONEY_KEY)) {
                //for mpesa kenya deposit money
                smsModel.setSmsBody(msg);
                smsModel.setSender(StringConstants.SENDER_MPESA);
                smsModel.setAmtCharge(null);

                String[] refinedSentAmtArr = splittedMsg[8].split("h");
                String dotRemoveSentAmt[] = refinedSentAmtArr[1].split("\\.");
                smsModel.setAmtSent(commaRemover(dotRemoveSentAmt[0]));

                smsModel.setAmtReceived(null);

                smsModel.setTrxType(StringConstants.MPESA_KE_DEPOSITMONEY);

                for (int i = 0; i < splittedMsg.length; i++) {
                    if (splittedMsg[i].equals("to"))
                    {
                        int j= i-1;
                        if (splittedMsg[j].equals("cash"))
                        {
                            j = msg.indexOf("to")+2;
                            int k = msg.indexOf("New");
                            String trxUser = msg.substring(j, k);
                            smsModel.setTrxUser(trxUser);
                        }
                    }
                }
                dateConverter(timestamp);

                //save db
                dbHandler.insertData(smsModel);
            }
        }
    }





    private void dateConverter(long timestamp)
    {
        Log.i("FrmServiceStamp",String.valueOf(timestamp));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MMM/dd hh:mma");
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        SimpleDateFormat month = new SimpleDateFormat("MMMM");
        SimpleDateFormat day = new SimpleDateFormat("dd");
        SimpleDateFormat time = new SimpleDateFormat("hh:mma");

        String date = sdf.format(timestamp);
        String yearStr = year.format(timestamp);

        String monthStr = month.format(timestamp);

        smsModel.setYear(yearStr);
        smsModel.setMonth(month.format(timestamp));
        smsModel.setDay(day.format(timestamp));
        smsModel.setTime(time.format(timestamp));

        Log.i("FromService@@@DAte",date);
        Log.i("FromService@@@yr",yearStr);
        Log.i("FromServiceMon",monthStr);

    }

    private String commaRemover(String inputString)
    {
        if(inputString.contains(","))
        {
            inputString = inputString.replace(",","");
        }

        return inputString;
    }
}
