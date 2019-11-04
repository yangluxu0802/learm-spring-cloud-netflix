package cn.luxu.bootstrap.feign.fallback;

import cn.luxu.bootstrap.feign.UserClient;
import cn.luxu.model.LoginAppUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: luxu
 * @date: 2019-10-31 17:17
 **/
@Component
@Slf4j
public class UserClientFallback implements UserClient {
    @Override
    public LoginAppUser findByUsername(String username) {
        log.info("UserClient invoke fail");
        return null;
    }
}
