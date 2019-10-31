package cn.luxu.bootstrap.feign.fallback;

import cn.luxu.bootstrap.feign.UserClient;
import cn.luxu.model.LoginAppUser;

/**
 * @description:
 * @author: luxu
 * @date: 2019-10-31 17:17
 **/
public class UserClientFallback implements UserClient {
    @Override
    public LoginAppUser findByUsername(String username) {
        return null;
    }
}
