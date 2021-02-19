package vip.testops.coffee.account.entities.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Account register request entity
 * @version 1.0
 * @author liudao
 */
@Data
public class RegisterRequestEntity {
    /**
     * username of the user
     */
    private String username;
    /**
     * password of the user
     */
    private String password;
    /**
     * confirm password
     */
    private String password2;
    /**
     * the gender of the user
     */
    private String gender;
    /**
     * the cellphone number of the user
     */
    private String cellphone;

}
