package vip.testops.coffee.account.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import vip.testops.coffee.account.entities.ResponseEntity;
import vip.testops.coffee.account.entities.dto.AccountDTO;
import vip.testops.coffee.account.entities.vto.LoginVTO;
import vip.testops.coffee.account.entities.vto.TokenVTO;
import vip.testops.coffee.account.mappers.AccountMapper;
import vip.testops.coffee.account.services.AccountService;
import vip.testops.coffee.account.utils.EncodeUtil;
import vip.testops.coffee.account.utils.StringUtils;
import vip.testops.coffee.account.utils.TokenUtil;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    private AccountMapper accountMapper;
    private StringRedisTemplate stringRedisTemplate;

    @Value("${code.expire}")
    private int codeExpire;

    @Value("${token.expire}")
    private int tokenExpire;

    @Autowired(required = false)
    public void setAccountMapper(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void doRegister(AccountDTO accountDTO, ResponseEntity<AccountDTO> responseEntity) {
        if(accountMapper.getUserByName(accountDTO.getAccountName()) != null){
            responseEntity.setCode(3001);
            responseEntity.setMessage(accountDTO.getAccountName() + " is already existing");
            return;
        }
        log.info("a new account is ready to insert into database");
        log.info("new account: " + accountDTO);
        if(accountMapper.addAccount(accountDTO) == 1){
            responseEntity.setCode(1000);
            responseEntity.setMessage("register success");
            log.info("account " + accountDTO.getAccountName() + " has been inserted into table.");
        } else {
            responseEntity.setCode(3002);
            responseEntity.setMessage("some error occurred when insert account info into table");
        }

    }

    @Override
    public void doLogin(String username, String password, ResponseEntity<LoginVTO> responseEntity) {
    /*
        check the username whether exists
         */
        AccountDTO accountDTO = accountMapper.getUserByName(username);
        if(accountDTO == null){
            responseEntity.setCode(3001);
            responseEntity.setMessage("username or password is invalid");
            log.info("username " + username + " is not exist");
            return;
        }
        /*
        check the password whether is right
         */
        try {
            password = EncodeUtil.digest(password + accountDTO.getSalt(), "SHA-256");
        } catch (Exception e) {
            responseEntity.setCode(4001);
            responseEntity.setMessage("system error while checking the password");
            log.error("system error while checking the password", e);
            return;
        }
        if(!password.equals(accountDTO.getPassword())){
            responseEntity.setCode(3001);
            responseEntity.setMessage("username or password is invalid");
            log.info("password is invalid");
            return;
        }
        /*
        update the last login time
         */
        accountDTO.setLastLoginTime(new Date());
        try{
            if(accountMapper.updateAccountById(accountDTO) != 1){
                responseEntity.setCode(3001);
                responseEntity.setMessage("update account info failed");
                log.error("update account error");
                return;
            }
        }catch(Exception e){
            responseEntity.setCode(4001);
            responseEntity.setMessage("system error");
            log.error("system error while updating account info", e);
            return;
        }
        log.info("update account " + accountDTO.getAccountName() + " success");
        // saving code to redis
        try {
            String code = EncodeUtil.digest(accountDTO.getAccountName() + System.currentTimeMillis(), "MD5");
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(accountDTO);
            stringRedisTemplate.opsForValue().set(code, jsonString, codeExpire, TimeUnit.MINUTES);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.MINUTE, codeExpire);
            LoginVTO loginVTO = new LoginVTO();
            loginVTO.setCode(code);
            loginVTO.setExpire(calendar.getTime());
            responseEntity.setCode(1000);
            responseEntity.setMessage("login success");
            responseEntity.setData(loginVTO);
        } catch (Exception e) {
            responseEntity.setCode(4001);
            responseEntity.setMessage("system error");
            log.error("system error while generating code", e);
        }

    }

    @Override
    public void doExchangeToken(String code, ResponseEntity<TokenVTO> responseEntity) {
        /*
        check the redis if this code exists
         */
        String jsonString = stringRedisTemplate.opsForValue().get(code);
        if(StringUtils.isEmptyOrNull(jsonString)){
            responseEntity.setCode(3001);
            responseEntity.setMessage("code is invalid");
            return;
        }
        log.info("get account info from redis: " + jsonString);
        /*
        get Account Data from redis
         */
        ObjectMapper objectMapper = new ObjectMapper();
        AccountDTO accountDTO = null;
        try {
            accountDTO = objectMapper.readValue(jsonString, AccountDTO.class);
        } catch (IOException e) {
            responseEntity.setCode(4001);
            responseEntity.setMessage("system error");
            log.error("system error while deserialize the json "+jsonString, e);
            return;
        }
        stringRedisTemplate.delete(code); // delete the key from redis
        /*
        generate token
         */
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, tokenExpire);
        String token = TokenUtil.createToken(
                TokenUtil.genSecretKey(code),
                accountDTO.getAccountName(),
                calendar.getTime());
        log.info("generate token: " + token);
        /*
        save token in redis
         */
        stringRedisTemplate.opsForValue().set(token, jsonString, tokenExpire, TimeUnit.MINUTES);
        /*
        prepare response json object
         */
        responseEntity.setCode(1000);
        responseEntity.setMessage("get token success");
        TokenVTO tokenVTO = new TokenVTO();
        tokenVTO.setToken(token);
        tokenVTO.setExpire(calendar.getTime());
        responseEntity.setData(tokenVTO);

    }

    @Override
    public void doAuthorize(String token, ResponseEntity<AccountDTO> responseEntity) {
        /*
        check the redis if this token exists
         */
        String jsonString = stringRedisTemplate.opsForValue().get(token);
        if(StringUtils.isEmptyOrNull(jsonString)){
            responseEntity.setCode(3001);
            responseEntity.setMessage("token is invalid");
            return;
        }
        log.info("get account info from redis: {}", jsonString);
        /*
        get Account Data from redis
         */
        ObjectMapper objectMapper = new ObjectMapper();
        AccountDTO accountDTO = null;
        try {
            accountDTO = objectMapper.readValue(jsonString, AccountDTO.class);
        } catch (IOException e) {
            responseEntity.setCode(4001);
            responseEntity.setMessage("system error");
            log.error("system error while deserialize the json "+jsonString, e);
            return;
        }
        stringRedisTemplate.expire(token, tokenExpire, TimeUnit.MINUTES);
        responseEntity.setCode(1000);
        responseEntity.setMessage("account token is valid");
        responseEntity.setData(accountDTO);

    }
}
