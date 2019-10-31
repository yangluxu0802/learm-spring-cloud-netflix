package cn.luxu.bootstrap.feign;

import cn.luxu.model.LoginAppUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "user",fallback = UserClient.UserClientFallback.class)
public interface UserClient {

    @GetMapping(value = "/users-anon/internal/{username}")
    LoginAppUser findByUsername(@PathVariable("username") String username);

    @Component
    @Slf4j
    class UserClientFallback implements UserClient{
        @Override
        public LoginAppUser findByUsername(String username) {
            log.info("UserClient invoke fail");
            return null;
        }
    }
}
