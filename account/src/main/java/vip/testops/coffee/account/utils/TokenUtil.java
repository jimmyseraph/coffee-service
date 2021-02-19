package vip.testops.coffee.account.utils;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

/**
 * generate json web token
 * @version 1.0
 * @author liudao
 */
public class TokenUtil {
    /**
     * this method use to generate the secret  key
     * @param keyString a unique string to generate the secret key
     * @return the secret key
     */
    public static SecretKey genSecretKey(String keyString) {
        return new SecretKeySpec(keyString.getBytes(), "AES"); //通过AES算法将字节数组加密为私钥
    }

    /**
     * this method is used to generate json web token
     * @param key secret key to encrypt the json string
     * @param username the name of current user
     * @param expire token expire time
     * @return token string
     */
    public static String createToken(SecretKey key, String username, Date expire) {
        SignatureAlgorithm sa = SignatureAlgorithm.HS256; // 设置签名算法
        JwtBuilder builder = Jwts.builder()
                .setAudience(username) // 授权用户
                .setExpiration(expire) // 过期时间
                .signWith(sa, key); // 签名算法和私钥
        return builder.compact();
    }

}
