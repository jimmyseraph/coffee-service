package vip.testops.coffee.account.services;

import vip.testops.coffee.account.entities.ResponseEntity;
import vip.testops.coffee.account.entities.dto.AccountDTO;
import vip.testops.coffee.account.entities.vto.LoginVTO;
import vip.testops.coffee.account.entities.vto.TokenVTO;

/**
 * account service for parsing all the events about account
 * @version 1.0
 * @author liudao
 */
public interface AccountService {
    /**
     * login service is used to add a new account to database
     * @param accountDTO the new account info
     * @param responseEntity the response of this service
     */
    void doRegister(AccountDTO accountDTO, ResponseEntity<AccountDTO> responseEntity);

    /**
     * login service is used to do the login request.
     * if login success, the service will return the temporary code
     * @param username login username
     * @param password login password
     * @param responseEntity return entity
     */
    void doLogin(String username, String password, ResponseEntity<LoginVTO> responseEntity);

    /**
     * exchange token service uses auth code to get token
     * @param code auth code
     * @param responseEntity json object
     */
    void doExchangeToken(String code, ResponseEntity<TokenVTO> responseEntity);

    /**
     * authorize service will check token and refresh its expire time
     * @param token user token
     * @param responseEntity json object
     */
    void doAuthorize(String token, ResponseEntity<AccountDTO> responseEntity);

}
