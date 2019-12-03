package com.stylefeng.guns.rest.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TransferUtils {
    /**
     * yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     */
    public static String parseDate2String(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(date);
        return format;
    }

    /**
     * yyyy年MM月dd日
     * @param date
     * @return
     */
    public static String parseDate2String2(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        String format = simpleDateFormat.format(date);
        return format;
    }
}
