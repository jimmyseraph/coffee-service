package vip.testops.coffee.account.entities.vto;

import lombok.Data;

import java.util.Date;

@Data
public class LoginVTO {
    private String code;
    private Date expire;
}
