package vip.testops.coffee.order.utils;

import java.util.Random;

/**
 * some useful string methods
 * @version 1.0
 * @author liudao
 */
public class StringUtils {
    /**
     * test the string is null or empty
     * @param content the string to be tested
     * @return true if the string is null or empty, otherwise return false
     */
    public static boolean isEmptyOrNull(String content){
        if(content == null){
            return true;
        }else return content.trim().equals("");
    }

    /**
     * padding the string from left with the specified character
     * @param content ordinary string to be padding
     * @param len the string length after padding
     * @param pad the character to fill in to the string
     * @return the padding string
     */
    public static String lpadding(String content, int len, char pad){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < len - content.length(); i++){
            sb.append(pad);
        }
        sb.append(content);
        return sb.toString();
    }

    /**
     * to generate a random string with specify length
     * @param len length of the random string
     * @return the random string
     */
    public static String randomString(int len){
        int upperLetterStart = 10;
        int lowerLetterStart = 36;
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < len; i ++){
            int tmp = random.nextInt(62);
            if(tmp >= lowerLetterStart){
                sb.append((char)('a' + (tmp - lowerLetterStart)));
            }else if(tmp > upperLetterStart){
                sb.append((char)('A' + (tmp - upperLetterStart)));
            }else{
                sb.append((char)('0' + tmp));
            }
        }
        return sb.toString();
    }

}

