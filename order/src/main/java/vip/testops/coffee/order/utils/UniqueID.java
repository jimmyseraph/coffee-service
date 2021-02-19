package vip.testops.coffee.order.utils;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class UniqueID {
    private static long lastTimeStamp = -1L;
    private static long sequence = 0L;

    public static synchronized String getUniqueID() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String salt = UUID.randomUUID().toString();
        String timestamp = System.currentTimeMillis() + "";
        return EncodeUtil.digest(timestamp + salt, "md5");
    }

    public static synchronized String getUniqueNumber(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Date current = new Date();
        if(current.getTime() == lastTimeStamp){
            sequence = (sequence + 1) % 10000L;
        }
        String seqString = StringUtils.lpadding(sequence + "", 4, '0');
        lastTimeStamp = current.getTime();
        return simpleDateFormat.format(current) + seqString;
    }
}