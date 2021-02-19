package vip.testops.coffee.order.api.fallback;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import vip.testops.coffee.order.api.AccountApi;
import vip.testops.coffee.order.entities.ResponseEntity;
import vip.testops.coffee.order.entities.dto.AccountDTO;

@Component
public class AccountApiFallbackFactory implements FallbackFactory<AccountApi> {
    @Override
    public AccountApi create(Throwable throwable) {
        return new AccountApi() {
            @Override
            public ResponseEntity<AccountDTO> authorize(String token) {
                ResponseEntity<AccountDTO> responseEntity = new ResponseEntity<>();
                responseEntity.setCode(9999);
                responseEntity.setMessage("Internal service is not available.");
                return responseEntity;
            }
        };
    }
}
