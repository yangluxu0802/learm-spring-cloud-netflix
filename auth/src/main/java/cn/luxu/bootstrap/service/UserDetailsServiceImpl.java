package cn.luxu.bootstrap.service;


import cn.luxu.bootstrap.feign.UserClient;
import cn.luxu.model.LoginAppUser;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: luxu
 * @date: 2019-10-29 15:15
 **/
@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    UserClient userClient;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        LoginAppUser loginAppUser = userClient.findByUsername(s);
        if (loginAppUser == null) {
            throw new AuthenticationCredentialsNotFoundException("用户不存在");
        } else if (!loginAppUser.isEnabled()) {
            throw new DisabledException("用户已作废");
        }
        return loginAppUser;
    }
}
