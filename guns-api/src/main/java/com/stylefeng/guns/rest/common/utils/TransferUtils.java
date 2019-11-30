package com.stylefeng.guns.rest.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TransferUtils {
    public static String parseDate2String(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(date);
        return format;
    }
}
