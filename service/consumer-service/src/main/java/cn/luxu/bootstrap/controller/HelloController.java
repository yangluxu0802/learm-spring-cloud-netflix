package cn.luxu.bootstrap.controller;

import cn.luxu.bootstrap.client.HelloRemote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: luxu
 * @date: 2019-10-18 13:56
 **/
@RestController
public class HelloController {

    @Autowired
    private HelloRemote helloRemote;

    /**
     * 输出hello方法
     * @param name 名称
     * @return 远程调用返回值
     */
    @RequestMapping(value = "/hello/{name}")
    public String hello(@PathVariable(value = "name") String name){
        return helloRemote.hello(name);
    }
}
