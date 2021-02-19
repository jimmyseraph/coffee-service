package vip.testops.coffee.account.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import vip.testops.coffee.account.entities.ResponseEntity;
import vip.testops.coffee.account.entities.dto.AccountDTO;
import vip.testops.coffee.account.entities.req.LoginRequestEntity;
import vip.testops.coffee.account.entities.req.RegisterRequestEntity;
import vip.testops.coffee.account.entities.vto.LoginVTO;
import vip.testops.coffee.account.entities.vto.TokenVTO;
import vip.testops.coffee.account.services.AccountService;
import vip.testops.coffee.account.utils.EncodeUtil;
import vip.testops.coffee.account.utils.StringUtils;

import java.util.Date;
/**
 * Account controller parse request from client which ask for account service
 * @version 1.0
 * @author liudao
 */
@Slf4j
@RestController
@RequestMapping("/account")
public class AccountController {

    private AccountService accountService;

    @Autowired
    public void setAccountService(AccountService accountService){
        this.accountService = accountService;
    }

    /**
     * register api for user to register an account
     * @param registerRequestEntity post body
     * @return json object
     */
    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<AccountDTO> register(
            @RequestBody RegisterRequestEntity registerRequestEntity
            ){
        ResponseEntity<AccountDTO> responseEntity = new ResponseEntity<>();
        /*
        check the username
         */
        if(StringUtils.isEmptyOrNull(registerRequestEntity.getUsername())){
            responseEntity.setCode(2001);
            responseEntity.setMessage("username cannot be null");
            return responseEntity;
        }
        /*
        check the password
         */
        if(StringUtils.isEmptyOrNull(registerRequestEntity.getPassword())){
            responseEntity.setCode(2001);
            responseEntity.setMessage("password cannot be null");
            return responseEntity;
        }
        /*
        check the confirm password
         */
        if(StringUtils.isEmptyOrNull(registerRequestEntity.getPassword2())){
            responseEntity.setCode(2001);
            responseEntity.setMessage("confirm password cannot be null");
            return responseEntity;
        }
        /*
        check the username rule
         */
        if(!registerRequestEntity.getUsername().matches("\\w{6,12}")){
            responseEntity.setCode(2002);
            responseEntity.setMessage("username must form with a-zA-Z_0-9, and the length must between 6 and 12");
            return responseEntity;
        }
        /*
        check the password rule
         */
        if(!registerRequestEntity.getPassword().matches("\\w{6,18}")){
            responseEntity.setCode(2002);
            responseEntity.setMessage("password must form with a-zA-Z_0-9, and the length must between 6 and 18");
            return responseEntity;
        }
        /*
        check the confirm password rule
         */

        if(!registerRequestEntity.getPassword().equals(registerRequestEntity.getPassword2())){
            responseEntity.setCode(2001);
            return  responseEntity;
        }
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountName(registerRequestEntity.getUsername());
        String salt = StringUtils.randomString(6);
        accountDTO.setSalt(salt);
        /*
        digest the password with sha-256
         */
        try {
            accountDTO.setPassword(EncodeUtil.digest(registerRequestEntity.getPassword() + salt, "SHA-256"));
        } catch (Exception e) {
            log.error("error occurred when digested the plain password", e);
            responseEntity.setCode(4001);
            responseEntity.setMessage("system error");
            return responseEntity;
        }
        /*
        check the gender if exist
         */
        if(!StringUtils.isEmptyOrNull(registerRequestEntity.getGender())){
            if(registerRequestEntity.getGender().equalsIgnoreCase("M")){
                accountDTO.setGender(0);
            }else if(registerRequestEntity.getGender().equalsIgnoreCase("F")){
                accountDTO.setGender(1);
            }else {
                responseEntity.setCode(2003);
                responseEntity.setMessage("Gender must be M/F/m/f.");
                return responseEntity;
            }
        }
        /*
        check the cell phone number if exist
         */
        if(!StringUtils.isEmptyOrNull(registerRequestEntity.getCellphone())){
            if(registerRequestEntity.getCellphone().matches("1[0-9]{10}")){
                accountDTO.setCellphone(registerRequestEntity.getCellphone());
            }else{
                responseEntity.setCode(2003);
                responseEntity.setMessage("Cellphone is illegal");
                return responseEntity;
            }
        }
        accountDTO.setCreateTime(new Date());
        accountService.doRegister(accountDTO, responseEntity);
        return responseEntity;
    }

    /**
     * login api for user to login
     * @param loginRequestEntity login request post body
     * @param bindingResult validate result
     * @return json object
     */
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<LoginVTO> login(
            @RequestBody LoginRequestEntity loginRequestEntity,
            BindingResult bindingResult
    ){
        ResponseEntity<LoginVTO> responseEntity = new ResponseEntity<>();
        /*
        check username
         */
        if(StringUtils.isEmptyOrNull(loginRequestEntity.getUsername())){
            responseEntity.setCode(2001);
            responseEntity.setMessage("account cannot be null");
            return responseEntity;
        }
        /*
        check password
         */
        if(StringUtils.isEmptyOrNull(loginRequestEntity.getPassword())){
            responseEntity.setCode(2001);
            responseEntity.setMessage("password cannot be null");
            return responseEntity;
        }

        accountService.doLogin(
                loginRequestEntity.getUsername(),
                loginRequestEntity.getPassword(),
                responseEntity);
        return responseEntity;
    }

    /**
     * token api is used to exchange token with code
     * @param code auth code
     * @return json object
     */
    @GetMapping("/token")
    @ResponseBody
    public ResponseEntity<TokenVTO> getToken(
            @RequestParam(value = "code", required = false) String code
    ){
        ResponseEntity<TokenVTO> responseEntity = new ResponseEntity<>();
        /*
        check the code
         */
        if(StringUtils.isEmptyOrNull(code)){
            responseEntity.setCode(2001);
            responseEntity.setMessage("Code cannot be null.");
            return responseEntity;
        }
        accountService.doExchangeToken(code, responseEntity);
        return responseEntity;
    }

    @GetMapping("/authorize")
    @ResponseBody
    public ResponseEntity<AccountDTO> authorize(
            @RequestParam(value = "token", required = false) String token
    ){
        ResponseEntity<AccountDTO> responseEntity = new ResponseEntity<>();
        /*
        check the token
         */
        if(StringUtils.isEmptyOrNull(token)){
            responseEntity.setCode(2001);
            responseEntity.setMessage("Token is missing.");
            return responseEntity;
        }
        accountService.doAuthorize(token, responseEntity);
        return responseEntity;
    }
}
