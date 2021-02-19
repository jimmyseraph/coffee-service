package vip.testops.coffee.account.utils;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class UniqueID {
    public static synchronized String getUniqueID() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String salt = UUID.randomUUID().toString();
        String timestamp = System.currentTimeMillis() + "";
        return EncodeUtil.digest(timestamp + salt, "md5");
    }
}

