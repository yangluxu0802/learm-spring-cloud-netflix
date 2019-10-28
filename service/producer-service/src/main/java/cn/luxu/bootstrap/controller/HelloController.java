package cn.luxu.bootstrap.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: luxu
 * @date: 2019-10-17 16:24
 **/
@RestController
public class HelloController {

    @Value("${server.port}")
    private String serverPort;

    @RequestMapping("hello")
    public String hello(@RequestParam String name) {
        return "this info is from "+serverPort+",hello " + name;
    }
}
