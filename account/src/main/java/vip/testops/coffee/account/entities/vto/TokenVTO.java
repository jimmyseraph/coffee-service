package vip.testops.coffee.account.entities.vto;

import lombok.Data;

import java.util.Date;

@Data
public class TokenVTO {
    private String token;
    private Date expire;
}
