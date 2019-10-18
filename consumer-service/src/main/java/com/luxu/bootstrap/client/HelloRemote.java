package com.luxu.bootstrap.client;

import com.luxu.bootstrap.controller.fallback.HelloRemoteFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @description: feign远程调用接口 FeignClient注解的name属性值要写服务提供者在注册中心注册的服务名称
 * @author: luxu
 * @date: 2019-10-18 13:55
 **/
@FeignClient(name = "producer-service",fallback = HelloRemoteFallback.class)
public interface HelloRemote {

    /**
     * 远程调用方法
     * @param name 名称
     * @return 远程调用结果
     */
    @RequestMapping(value = "/hello")
    String hello(@RequestParam(value = "name") String name);
}
