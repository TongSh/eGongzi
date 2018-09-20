package tong.lan.com.hyperledger.utils;

import android.annotation.SuppressLint;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtil {
    static public long date2stamp(String date){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        long startDay = 0;
        try {
            Date dateStart = format.parse(date);
            startDay = (long) (dateStart.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return startDay;
    }

    static public Date getDate(String date){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date ret = null;
        try {
            ret = format.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    static public String getString(int year, int month, int day){
        if (month > 12){
            month -= 12;
            year++;
        }
        DecimalFormat df=new DecimalFormat("0000");
        String y = df.format(year);
        df = new DecimalFormat("00");
        return y+"-"+df.format(month)+"-"+df.format(day);
    }
}
