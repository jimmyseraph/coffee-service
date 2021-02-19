package vip.testops.coffee.account.entities.dto;

import lombok.Data;

import java.util.Date;

/**
 * account table object
 * @version 1.0
 * @author liudao
 */
@Data
public class AccountDTO {
    /**
     * accountId column
     */
    private long accountId;
    /**
     * accountName column
     */
    private String accountName;
    /**
     * salt column
     * random string for confuse the password
     */
    private String salt;
    /**
     * password column
     * encrypt with ordinary password + salt
     */
    private String password;
    /**
     * cellphone column
     */
    private String cellphone;
    /**
     * gender column
     * 0-male, 1-female
     */
    private int gender;
    /**
     * create time column
     */
    private Date createTime;
    /**
     * last login time column
     */
    private Date lastLoginTime;
}

