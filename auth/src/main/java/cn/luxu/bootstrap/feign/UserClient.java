package cn.luxu.bootstrap.feign;

import cn.luxu.bootstrap.feign.fallback.UserClientFallback;
import cn.luxu.model.LoginAppUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "user",fallback = UserClientFallback.class)
public interface UserClient {

    @GetMapping(value = "/users-anon/internal/{username}")
    LoginAppUser findByUsername(@PathVariable("username") String username);

}
