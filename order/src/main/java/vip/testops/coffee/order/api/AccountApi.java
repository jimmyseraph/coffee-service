package vip.testops.coffee.order.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import vip.testops.coffee.order.api.fallback.AccountApiFallbackFactory;
import vip.testops.coffee.order.entities.ResponseEntity;
import vip.testops.coffee.order.entities.dto.AccountDTO;

@FeignClient(name = "account", fallbackFactory = AccountApiFallbackFactory.class)
public interface AccountApi {
    @RequestMapping(method = RequestMethod.GET, value = "/account/authorize")
    ResponseEntity<AccountDTO> authorize(@RequestParam("token") String token);
}
