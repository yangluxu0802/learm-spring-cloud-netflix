package cn.luxu.bootstrap.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description:
 * @author: luxu
 * @date: 2019-10-28 15:25
 **/
@FeignClient(name = "auth-service",fallback = AuthRemote.AuthRemoteFallback.class)
public interface AuthRemote {

    @RequestMapping(value = "/auth/ignore/{url}")
    Boolean ignoreAuthentication(@PathVariable String url);

    @PostMapping(value = "/auth/permission")
    Boolean hasPermission(String authentication, String url, String method);

    @Component
    class AuthRemoteFallback implements AuthRemote {

        @Override
        public Boolean ignoreAuthentication(String url) {
            return null;
        }

        @Override
        public Boolean hasPermission(String authentication, String url, String method) {
            return null;
        }
    }
}
