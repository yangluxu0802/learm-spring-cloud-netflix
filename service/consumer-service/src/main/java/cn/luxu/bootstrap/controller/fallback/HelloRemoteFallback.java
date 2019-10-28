package cn.luxu.bootstrap.controller.fallback;

import cn.luxu.bootstrap.client.HelloRemote;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: luxu
 * @date: 2019-10-18 14:42
 **/
@Component
public class HelloRemoteFallback implements HelloRemote {

    /**
     * 远程调用失败，将会回调该方法
     * @param name 名称
     * @return 自定义返回信息
     */
    @Override
    public String hello(String name) {
        return "hello " + name + ", this message is failed";
    }
}
