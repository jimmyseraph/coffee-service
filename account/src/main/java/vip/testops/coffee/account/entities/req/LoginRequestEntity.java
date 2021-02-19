package vip.testops.coffee.account.entities.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Account login request entity
 * @version 1.0
 * @author liudao
 */
@Data
public class LoginRequestEntity {
    /**
     * username of the user
     */
    private String username;
    /**
     * password of the user
     */
    private String password;
}
